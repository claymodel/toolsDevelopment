(function($) {

/**
 * Where extensions to AUI that aren't ready to migrate should be stored.
 *
 * Functions added here should have a @since version and go on the roadmap to
 * be moved over after a suitable delay.
 */

/**
 * AJS.Data will be the namespace for accessing dynamic data passed from the
 * server to JavaScript via the page HTML.
 *
 * @since 3.4
 */
AJS.Data = $.extend({}, AJS.Data, {

    /**
     * Returns a value given a key. If no entry exists with the key, undefined is returned.
     *
     * @method get
     * @param key
     * @return {String}
     */
    get: function (key) {
        var metaEl = $("meta[name=ajs-" + key + "]");
        return metaEl.length ? metaEl.attr("content") : undefined;
    },

    /**
     * Returns true if the value for the provided key is equal to "true", else returns false.
     * otherwise.
     *
     * @method getBoolean
     * @param key
     * @return {boolean}
     */
    getBoolean: function (key) {
        return this.get(key) === "true";
    }

});

})(AJS.$);

