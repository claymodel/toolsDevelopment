/**
 * Shortens the set of elements by replacing the last character of each with ellipsis
 * until the condition returns true. Typical usage:
 *
 *   $("#some-list li").shortenUntil(function () { return $("#some-list").width() < 500; });
 *
 * @param condition shortening of elements will happen until this function returns true
 */
jQuery.fn.shortenUntil = function (condition) {
    var $ = jQuery;
    var current = 0;
    while (!condition() && current < this.length) {
        var currentText = $(this[current]).text();
        if (currentText == "\u2026") {
            current++;
            continue;
        }
        $(this[current]).text(currentText.replace(/[^\u2026]\u2026?$/, "\u2026"));
    }
    return this;
};

/**
 * Renders a set of breadcrumbs in the specified element. Typical usage:
 *
 *   $("some-element").renderBreadcrumbs([ { title: "Dashboard", url: "/dashboard.action" }, ... ]);
 *
 * @param items an array of objects with 'title' and 'url' properties, representing the breadcrumbs.
 */
jQuery.fn.renderBreadcrumbs = function (items) {
    var $ = jQuery,
        el = this,
        html = [],
        i = 0,
        last = items.length - 1;
    
    var space = items[i];
    var spaceClass = space.url.indexOf("~") >= 0 ? "personalspacedesc" : "spacedesc";
    html.push(AJS.renderTemplate("movePageBreadcrumb", space.title,
            space.url, (i == last ? "last content-type-" + spaceClass : ""), space.title));

    while (i++ < last) {
        var parent = items[i];
        html.push(AJS.renderTemplate("movePageBreadcrumb", parent.title,
            parent.url, (i == last ? "last content-type-" + items.type : ""), parent.title));
    }
    
    var lessThanContainer = function () {
		return el.width() < el.closest(".breadcrumbs-container").width();
	};
    
    // shorten the middle items first then the space (first item) and then the last item 
    this.html(html.join(""));
    var breadcrumbItems = $('li a span', this);
    breadcrumbItems.each(function(index){
    	if (index != 0 && index != last){
    		$(this).shortenUntil(lessThanContainer);
    	}
    });
    $(breadcrumbItems.get(0)).shortenUntil(lessThanContainer);
    $(breadcrumbItems.get(last)).shortenUntil(lessThanContainer);
    return this;
};

AJS.toInit(function ($) {
    var contextPath = $("#confluence-context-path").attr("content");

    // returns false if the breadcrumb contains the current page
    function isValidLocation(breadcrumbs) {
        for (var i=1; i<breadcrumbs.length; i++) { // skip dashboard and space title
            if (breadcrumbs[i].title == AJS.params.pageTitle) {
                return false;
            }
        }
        return true;
    };

    if (!AJS.MoveDialog) AJS.MoveDialog = {};

    var breadcrumbCache = {}; // cached for entire request -- if this isn't okay, move it into Breadcrumbs class below

    /**
     * Handles retrieval of breadcrumbs via AJAX and caching of the responses until the page reloads.
     * 
     * Possible options:
     * 
     * spaceKey - The space key for the space containing the object you want breadcrumbs for. It can be the space by itself or an 
     *            object within the space
     * title - The page title for the page you want breadcrumbs for or a page with an attachment you want breadcrumbs for.
     * fileName - the name of the attachment you want breadcrumbs for. 
     * userName - the name of the User you want breadcrumbs for. If this option is specified, the others are ignored and the user
     *            breadcrumbs are returned. 
     */
    AJS.MoveDialog.getBreadcrumbs = function (options, success, error) {
        var cacheKey = options.userName ? options.userName : 
        	(options.pageId ? (options.pageId + ":" + options.fileName): 
        		(options.spaceKey + ":" + options.title + ":" + options.postingDay + ":" + options.fileName));
        
        if (cacheKey in breadcrumbCache) {
            success(breadcrumbCache[cacheKey], "success");
            return;
        }       
       
        $.ajax({
            type: "GET",
            dataType: "json",
            data: options,
            url: contextPath + "/pages/breadcrumb.action",
            error: error || function () { },
            success:  function (data, textStatus) {
                if (!data || !data.breadcrumbs) {
                    error(data, textStatus);
                    return;
                }
                
                var breadcrumbs = $.makeArray(data.breadcrumbs);
                
                // strip out "Dashboard" and "People"
                while (breadcrumbs[0] && (/dashboard.action$/.test(breadcrumbs[0].url) || 
                	  (data.type != "userinfo" && /peopledirectory.action$/.test(breadcrumbs[0].url)))) {
                    breadcrumbs.shift();
                }
                breadcrumbs.type = data.type;
                
                breadcrumbCache[cacheKey] = breadcrumbs;
                success(breadcrumbs, textStatus);
            }
        });
    };

    /**
     * Returns an object with an 'update' method, which can be called to render a breadcrumb
     * with that location inside the breadcrumbsElement.
     *
     * @param breadcrumbsElement the element (usually a 'ul') where the breadcrumb will be
     * rendered.
     */
    AJS.MoveDialog.Breadcrumbs = function (breadcrumbsElement) {

        var requestCount = 0;

        function displayBreadcrumbs(spaceKey, breadcrumbs, controls) {
            breadcrumbsElement.renderBreadcrumbs(breadcrumbs);
            var validLocation = spaceKey != AJS.params.spaceKey || isValidLocation(breadcrumbs);
            if (validLocation) {
                controls.clearErrors();
                $(controls.moveButton).attr("disabled", "");
            } else {
                controls.error(AJS.params.movePageDialogInvalidLocation);
                $("li:last-child", breadcrumbsElement).addClass("warning");
            }
        }

        return {
            /**
             * Updates the breadcrumb to the specified location. Any errors are handled by
             * calling 'controls.error' with the message.
             *
             * @param options available options
             * 
             * spaceKey - The space key for the space containing the object you want breadcrumbs for. It can be the space by itself or an 
		     *            object within the space
		     * title - The page title for the page you want breadcrumbs for or a page with an attachment you want breadcrumbs for.
		     * fileName - the name of the attachment you want breadcrumbs for. 
		     * userName - the name of the User you want breadcrumbs for. If this option is specified, the others are ignored and the user
		     *            breadcrumbs are returned. 
             * @param controls should contain an 'error' function which is used to pass
             * errors back to the caller, and a 'clearErrors' which indicates no errors
             * occurred
             */
            update: function (options, controls) {
                breadcrumbsElement.html(AJS.renderTemplate("movePageBreadcrumbLoading"));
                var thisRequest = requestCount += 1;

                // Breadcrumbs and errors should only be displayed for the latest request.
                var isRequestStale = function() {
                    if (thisRequest != requestCount) {
                        AJS.log("Breadcrumb response for ");
                        AJS.log(options);
                        AJS.log(" is stale, ignoring.");
                        return true;
                    }
                    return false;
                };
                
                AJS.MoveDialog.getBreadcrumbs(options,
                    function (breadcrumbs, textStatus) {
                        if (isRequestStale()) return;

                        if (textStatus != "success" || !breadcrumbs) {
                            breadcrumbsElement.html(AJS.renderTemplate("movePageBreadcrumbError", textStatus));
                            return;
                        }
                        displayBreadcrumbs(options.spaceKey, breadcrumbs, controls);
                    },
                    function (xhr) {
                        if (isRequestStale()) return;

                        breadcrumbsElement.html(AJS.renderTemplate("movePageBreadcrumbError"));
                        if (xhr.status == 404) {
                            controls.error(AJS.params.movePageDialogLocationNotFound);
                        }
                    }
                );
            }
        };
    };
});
