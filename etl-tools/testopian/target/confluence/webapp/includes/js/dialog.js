/**
 * Provides Confluence-specific overrides of AJS.Dialog defaults
 */
AJS.ConfluenceDialog = function(options) {
    var dialog;
    options = options || {};
    options = jQuery.extend({}, {
        keypressListener: function(e) {
            if (e.keyCode === 27) {
                // if dropdown is currently showing, leave the dialog and let the dropdown close itself
                if (!jQuery(".aui-dropdown", dialog.popup.element).is(":visible")) {
                    if (typeof options.onCancel == "function") {
                        options.onCancel();
                    } else {
                        dialog.hide();
                    }
                }
            }
            else if (e.keyCode === 13) {
                // Enter key pressed
                if (!jQuery(".aui-dropdown", dialog.popup.element).is(":visible")) {
                    // No dropdown showing - enter is on dialog.
                    var nodeName = e.target.nodeName && e.target.nodeName.toLowerCase();
                    if (nodeName != "textarea" && typeof options.onSubmit == "function") {
                        options.onSubmit();
                    }
                }
            }
        },
        width: 865,
        height: 530
    }, options);
    dialog = new AJS.Dialog(options);

    jQuery.aop.around({ target: dialog, method: "addButton" },
        function (invocation) {
            if (invocation.arguments[0]) {
                invocation.arguments[0] = AJS.I18n.getText(invocation.arguments[0]);
            }
            return invocation.proceed();
        }
    );

    return dialog;
};

// Automatically bind our components when a dialog is shown
AJS.toInit(function($){
    $(document).bind("showLayer", AJS.Confluence.runBinderComponents);

    // 3.5 - Move this into AUI if it proves useful
    AJS.Dialog.prototype.addHelpText = function(template, args) {
        if (!template)
        {
            // Don't do anything if there is no text to add.
            // This stops us printing 'undefined'.
            return;
        }

        var text = template;
        if (args) {
            text = AJS.template(template).fill(args).toString();
        }

        var page = this.page[this.curpage];
        if (!page.buttonpanel) {
            page.addButtonPanel();
        }

        // The text may include html i.e. links or strongs 
        var tip = $("<div class='dialog-tip'></div>").html(text);
        page.buttonpanel.append(tip);
    }
});
