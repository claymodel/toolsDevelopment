(function() {
     var $ = AJS.$;

     var makeRestMatrixFromData = function (response) {
        if (!response || !response.group){
            throw new Error("Invalid JSON format");
        }

        var matrix = [];

        for (var i=0, ii=response.group.length; i<ii; i++) {
            matrix.push(response.group[i].result);
        }
        return matrix;
    };

    var bind = function(input, contentTypes, valueTemplate, displayHandler) {

        var $input = $(input),
            boundKey = "data-autocomplete-content-bound";

        if ($input.attr(boundKey))
            return;

        if (typeof contentTypes == "string")
            contentTypes = [contentTypes];

        $input.attr(boundKey, "true").attr("autocomplete", "off");
        var typesString = contentTypes.join(","),
            maxResults = $input.attr("data-max") || 10,
            alignment = $input.attr("data-alignment") || "left",
            spaceKey = $input.attr("data-spacekey"),
            noResults = $input.attr("data-none-message"),
            template = $input.attr("data-template") || valueTemplate,
            targetSelector = $input.attr("data-target"),
            dropDownTarget = $input.attr("data-dropdown-target"),
            dropDownPosition = null;

        if (dropDownTarget) {
            dropDownPosition = $(dropDownTarget);
        }
        else {
            dropDownPosition = $("<div></div>");
            $input.after(dropDownPosition);
        }
        dropDownPosition.addClass("aui-dd-parent autocomplete");

        $input.quicksearch(AJS.REST.getBaseUrl() + "search/name.json", null, {
            onShow: function() {
                $input.trigger("open.autocomplete-content", { contentTypes: contentTypes});
            },
            makeParams : function(val) {
                var params = {
                    "max-results": maxResults,
                    type: typesString,
                    query: val
                };
                if (spaceKey) {
                    params.spaceKey = spaceKey;
                }
                return params;
            },
            dropdownPlacement: function(dd) {
                dropDownPosition.append(dd);
            },
            makeRestMatrixFromData : makeRestMatrixFromData,
            addDropdownData : function (matrix) {
                if (!matrix.length) {
                    if (noResults) {
                        matrix.push([{
                            name: noResults,
                            className: "no-results",
                            href: "#"
                        }]);
                    }
                }

                return matrix;
            },
            ajsDropDownOptions : {
                alignment: alignment,
                displayHandler: displayHandler,
                selectionHandler: function (e, selection) {

                    if (selection.find(".search-for").length) {
                        $input.trigger("selected.autocomplete-content", { contentTypes: contentTypes, searchFor: $input.val() });
                        return;
                    }
                    if (selection.find(".no-results").length) {
                        AJS.log("no results selected");
                        this.hide();
                        e.preventDefault();
                        return;
                    }

                    var contentProps = selection.data("properties");

                    $input.val(AJS.template(template).fillHtml(contentProps.restObj));

                    if (targetSelector) {
                        var value = AJS.template(valueTemplate).fillHtml(contentProps.restObj);
                        $(targetSelector).val(value);
                    }

                    $input.trigger("selected.autocomplete-content", { contentTypes: contentTypes, content: contentProps.restObj, selection: selection });
                    this.hide();
                    e.preventDefault();
                }
           }
        });
    };

    /**
     * Space Autocomplete Binder Component.
     * <br>
     * Example markup:
     * &lt;input class="autocomplete-space" data-max="10" data-none-message="No results" data-template="{name}"&gt;
     *
     * Events Triggered:
     * open.autocomplete-content, selected.autocomplete-content
     *
     * @since 3.4
     * @class autocompleteSpace
     * @namespace AJS.Confluence.Binder
     */
    AJS.Confluence.Binder.autocompleteSpace = function(scope) {
        scope = scope || document.body;
        $("input.autocomplete-space", scope).each(function() {
            bind(this, ["spacedesc", "personalspacedesc"], "{name}",
                function(obj) {
                    return obj.name;
                });
        });
    };

    /**
     * Attachment Autocomplete Binder Component.
     * <br>
     * Example markup:
     * &lt;input class="autocomplete-attachment" data-max="10" data-none-message="No results" data-template="{fileName}"&gt;
     *
     * Events Triggered:
     * open.autocomplete-content, selected.autocomplete-content
     *
     * @since 3.4
     * @class autocompleteAttachment
     * @namespace AJS.Confluence.Binder
     */
    AJS.Confluence.Binder.autocompleteAttachment = function(scope) {
        scope = scope || document.body;
        $("input.autocomplete-attachment", scope).each(function() {
            bind(this, "attachment", "{fileName}",
                function(obj) {
                    var str = (obj.restObj && obj.restObj.fileName) || obj.name;
                    if (obj.restObj && obj.restObj.space && obj.restObj.space.title) {
                        str += " (" + obj.restObj.space.title + ")";
                    }
                    return str;
                });
        });
    };

    var contentDisplayHandler =  function(obj) {
        return (obj.restObj && obj.restObj.title) || obj.name;
    };

    /**
     * Page Autocomplete Binder Component.
     * <br>
     * Example markup:
     * &lt;input class="autocomplete-page" data-max="10" data-none-message="No results" data-template="{title}"&gt;
     *
     * Events Triggered:
     * open.autocomplete-content, selected.autocomplete-content
     *
     * @since 3.4
     * @class autocompletePage
     * @namespace AJS.Confluence.Binder
     */
    AJS.Confluence.Binder.autocompletePage = function(scope) {
        scope = scope || document.body;
        $("input.autocomplete-page", scope).each(function() {
            bind(this, "page", "{title}", contentDisplayHandler);
        });
    };

    /**
     * Blog post Autocomplete Binder Component.
     * <br>
     * Example markup:
     * &lt;input class="autocomplete-blogpost" data-max="10" data-none-message="No results" data-template="{title}"&gt;
     *
     * Events Triggered:
     * open.autocomplete-content, selected.autocomplete-content
     *
     * @since 3.4
     * @class autocompleteBlogpost
     * @namespace AJS.Confluence.Binder
     */
    AJS.Confluence.Binder.autocompleteBlogpost = function(scope) {
        scope = scope || document.body;
        $("input.autocomplete-blogpost", scope).each(function() {
            bind(this, "blogpost", "{title}", contentDisplayHandler);
        });
    };

    /**
     * Confluence content Autocomplete Binder Component i.e. Pages, Blog posts
     * <br>
     * Example markup:
     * &lt;input class="autocomplete-confluence-content" data-max="10" data-none-message="No results" data-template="{title}"&gt;
     * 
     * Events Triggered:
     * open.autocomplete-content, selected.autocomplete-content
     *
     * @since 3.4
     * @class autocompleteConfluenceContent
     * @namespace AJS.Confluence.Binder
     */
    AJS.Confluence.Binder.autocompleteConfluenceContent = function(scope) {
        scope = scope || document.body;
        $("input.autocomplete-confluence-content", scope).each(function() {
            bind(this, ["page", "blogpost"], "{title}", contentDisplayHandler);
        });
    };
})();