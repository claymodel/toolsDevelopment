(function($) {
/**
 * A collection of reusable Confluence UI Components.
 * @module Components
 */

/**
 * Generic Confluence helper functions.
 *
 * @static
 * @since 3.3
 * @class Confluence
 * @namespace AJS
 * @requires AJS, jQuery
 */
AJS.Confluence = {
    /**
     * Returns the context path defined in the 'ajs-context-path' meta tag.
     *
     * e.g. /confluence
     *
     * @method getContextPath
     * @return {String}
     */
    getContextPath : function() {
        return AJS.Data.get("context-path");
    },

    /**
     * Returns the product Build Number defined in the 'ajs-build-number' meta tag.
     *
     * e.g. 2021
     *
     * @method getBuildNumber
     * @return {String}
     */
    getBuildNumber : function() {
        return AJS.Data.get("build-number");
    },

    /**
     * Binder components, in the AJS.Confluence.Binder namespace are executed.
     * This can be called when new elements are added to the page after page load
     * (e.g. dialog is created) and the components need to bound to the new elements.
     *
     * @method initBinderComponents
     */
    runBinderComponents: function () {
        AJS.log("AJS.Confluence: run binder components");
        for (var i in AJS.Confluence.Binder) {
            if (AJS.Confluence.Binder.hasOwnProperty(i)) {
                try {
                    AJS.Confluence.Binder[i]();
                } catch(e) {
                    AJS.log("Exception in initialising of component '" + i + "': " + e.message);
                }
            }
        }
    },

    /**
     * @deprecated Use AJS.Confluence.Binder.placeFocus instead.
     */
    placeFocus: function () {
        AJS.Confluence.Binder.placeFocus();
    }
};

/**
 * Binders are components that bind, dependent on the markup in the page.
 * <p>
 * Objects added to the AJS.Confluence.Binder namespace are run on page load and must be
 * functions which can be executed several times on a page.
 * 
 * @class Binder
 * @namespace AJS.Confluence
 */
AJS.Confluence.Binder = {
    /**
     * Automatically place the focus on an input field with class 'data-focus'.  The element
     * with the highest value wins.  If more than one index has the same value, one will be picked
     * indeterminately.
     *
     * Note, we could use the HTML5 autofocus attribute, but it only expects one element in the document
     * to have such an attribute specified.
     *
     * @method placeFocus
     */
    placeFocus: function () {
        var element,max = -1;
        AJS.$("input[data-focus]").each(function() {
            var $this = AJS.$(this),
                thisFocus = $this.attr("data-focus");
            if (thisFocus > max) {
                max = thisFocus;
                element = $this;
            }
        });
        element && element.focus();
    }
};

AJS.toInit(function () {
    AJS.Confluence.runBinderComponents();
});

})(AJS.$);