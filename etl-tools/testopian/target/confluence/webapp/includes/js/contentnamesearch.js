AJS.toInit(function ($) {

    /**
     * Append the drop down to the form element with the class quick-nav-drop-down
     */
    var quickNavPlacement = function (input) {
        return function (dropDown) {
            input.closest("form").find(".quick-nav-drop-down").append(dropDown);
        };
    };
    var quickSearchQuery = $("#quick-search-query"),
        spaceBlogSearchQuery = $("#space-blog-search-query"),
        confluenceSpaceKey = $("#confluence-space-key");

    quickSearchQuery.quicksearch("/json/contentnamesearch.action", null, {
        dropdownPlacement : quickNavPlacement(quickSearchQuery)
    });

    if (spaceBlogSearchQuery.length && confluenceSpaceKey.length) {
        spaceBlogSearchQuery.quicksearch("/json/contentnamesearch.action?type=blogpost&spaceKey=" + 
                AJS("i").html(confluenceSpaceKey.attr("content")).text(), null, {
            dropdownPlacement : quickNavPlacement(spaceBlogSearchQuery)
        });
    }
});
