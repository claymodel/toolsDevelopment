/**
 * Displays default text in the input field when its value is empty.
 * If the browser supports placeholder input attributes (HTML5), then
 * we skip this component.
 *
 * Usage:
 * <pre>
 * &lt;input placeholder="Some default text"&gt;
 * </pre>
 *
 * Events thrown: reset.placeholder
 * 
 * @class placeholder
 * @namespace AJS.Confluence.Binder
 */
AJS.Confluence.Binder.placeholder = function() {
    var $ = AJS.$;

    // browser supports placeholder, no need to do anything
    var temp = document.createElement('input');
    if('placeholder' in temp)
        return;

    // support old attributes defaul-text, cause it was introduced in 3.3.
    $("input[placeholder][data-placeholder-bound!=true]," +
      "input.default-text[data-placeholder-bound!=true]").each(function() {

        var $this = $(this).attr("data-placeholder-bound", "true"),
            defaultText = $this.attr("placeholder") || $this.attr("data-default-text"),
            applyDefaultText = function() {
                if(!$.trim($this.val()).length) {
                    $this.val(defaultText)
                         .addClass("placeholded")
                         .trigger("reset.placeholder");
                    $this.trigger("reset.default-text");
                }
            };

        applyDefaultText();
        $this.blur(applyDefaultText).focus(function() {
            if($this.hasClass("placeholded")) {
                $this.val("");
                $this.removeClass("placeholded");
            }
        });
    });
};

// for backwards compatability
AJS.Confluence.Binder.inputDefaultText = AJS.Confluence.Binder.placeholder;