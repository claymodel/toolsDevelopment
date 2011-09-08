AJS.toInit(function($) {
    // superbatching means that this script could be on every page
    if (!AJS.params.linkToThisPageLink) {
        return;
    }

    var dialog = new AJS.Dialog(600, 210, "link-page-popup")
        .addHeader(AJS.params.linkToThisPageHeading)
        .addPanel(AJS.params.linkToThisPageHeading, "<form id='link-page-popup-form' class='aui'>" +
                               "<fieldset>" +
                               "</fieldset>" +
                               "</form>")
        .addCancel(AJS.params.linkToThisPageClose, function(e) { hideDialog(e); });
  
    // add shortcut tip
    if (Confluence.KeyboardShortcuts.enabled || AJS.Data.get("use-keyboard-shortcuts") == "true") {
        dialog.addHelpText(AJS.params.shortcutDialogTip, {shortcut: "k"});
    }

    var keydownHandler = function(e) {
        if (e.which == 27) { // ESC
            hideDialog(dialog);
        }
    };

    var links = [
        {
            label: AJS.params.linkToThisPageLink,
            id: "link",
            value: $("link[rel=canonical]").attr("href")
        },
        {
            label: AJS.params.linkToThisPageTinyLink,
            id: "tiny-link",
            value: $("link[rel=shortlink]").attr("href")
        },
        {
            label: AJS.params.linkToThisPageWikiMarkup,
            id: "wiki-markup",
            value: $("meta[name=wikilink]").attr("content")
        }
    ];

    $.each(links, function() {
        $("#link-page-popup-form fieldset").append(AJS.format(
                "<div class='field-group'>" +
                    "<label for=''link-popup-field-{0}''>{1}:</label>" +
                    "<input id=''link-popup-field-{0}'' readonly=''readonly'' value='''' class=''text'' type=''text''>" +
                "</div>", this.id, this.label)).find("input:last").val(this.value);
    });

    var hideDialog = function(dialog) {
        dialog.hide();
        $(document).unbind("keydown", keydownHandler);
    };

    $("#link-to-page-link").click(function(e) {
        // If the fieldset isn't available skip the dialoglogin
        if (!$("#link-to-page-fields")[0]) {
            alert("Due to customisations made to this space's layout, Confluence is unable to load the links dialog. " +
                  "For details on how to correct this, please visit the 'Upgrading Custom Layouts' page in our Confluence " +
                  "Documentation, or contact your administrators.");
            return;
        }
        $(this).parents(".ajs-drop-down")[0].hide();
        dialog.show();
        $(document).keydown(keydownHandler);
        $("#link-page-popup-form #link-popup-field-tiny-link").select();
        return AJS.stopEvent(e);
    });

    var linkText = $("#link-page-popup-form fieldset input.text");
    linkText.focus(function() {
        $(this).select();
    });

    // On Safari the mouse up event deselects the text
    linkText.mouseup(function(e){
        e.preventDefault();
    });

});
