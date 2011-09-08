/**
 * Displays the search results from search.action in a grid display of the current element.
 *
 * @param queryString the query string which the search was performed on
 * @param data JSON format of the results returned by search.action
 * @param controls an object with controls defined.
 * It should have a 'select' function to handle onclick events in the grid.
 * @param messages i18n messages required for the grid.
 */
jQuery.fn.searchResultsGrid = function(queryString, data, controls, messages) {
    var $ = jQuery, resultsContainer = this;

    if (!data.results || data.results.length == 0) {
        var message = AJS.format(messages.noSearchResults, AJS.escapeEntities(queryString));
        resultsContainer.html("<div class='no-results'>" + message + "</div>");
        return;
    }

    resultsContainer.html(AJS.getTemplate("searchResultsGrid").toString());

    var startIndex = data.startIndex + 1,
        endIndex = data.startIndex + data.results.length;
    var resultsCount = AJS.format(messages.resultsCount, startIndex, endIndex,
        data.total, AJS.escapeEntities(queryString));
    resultsContainer.prepend(AJS.renderTemplate("searchResultsGridCount", AJS.html(resultsCount)));

    for (var i=0; i<data.results.length; i++) {
        var item = data.results[i];
        var el = $(AJS.renderTemplate("searchResultsGridRow", [
            item.title,
            item.url,
            item.type,
            item.spaceName,
            item.spaceKey,
            item.friendlyDate,
            item.date
        ]));
        el.selectableEffects(resultsContainer, controls.select, item);
        resultsContainer.find("table").append(el);
    }

    $(".search-result:first", resultsContainer).click();
};