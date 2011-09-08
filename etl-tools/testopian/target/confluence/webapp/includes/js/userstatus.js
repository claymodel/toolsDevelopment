AJS.toInit(function($) {
    function isBlank(string) {
        return $.trim(string).length == 0;
    }

    var popup,
        maxChars = 140;

    function createPopUp() {
        var popup = new AJS.Dialog(650, 200, "update-user-status-dialog");
        var $dialog = popup.popup.element;
        popup.addHeader(AJS.params.statusDialogHeading || "What are you working on?");
        popup.addPanel("Set Status", "<form class='aui update-status'>" + // TODO AUIfy this
                                     "<fieldset>" +
                                     "<legend class='assistive'>Status Update</legend>" +
                                     "<label for='status-text' class='assistive'>" + AJS.params.statusDialogAccessibilityLabel + "</label>" +
                                     "<textarea name='status-text' id='status-text'></textarea>" +
                                     "<span id='update-status-chars-left'>" + maxChars + "</span>" +
                                     "<div id='dialog-current-status' class='current-user-latest-status'>" +
                                     (AJS.params.statusDialogLatestLabel || "Latest:") +
                                     " <span class='status-text'></span></div>" +
                                     "</fieldset>" +
                                     "</form>");
        popup.addButton(AJS.params.statusDialogUpdateButtonLabel || "Update", updateStatus, "status-update-button");
        popup.addCancel(AJS.params.statusDialogCancelButtonLabel || "Cancel", function (dialog) {dialog.hide();});
        $dialog.find(".dialog-button-panel").append("<span class='error-message'></span>");
        popup.setError = function(html) {
            $("#update-user-status-dialog .error-message").html(html)
        };

        // add shortcut tip
        if (Confluence.KeyboardShortcuts.enabled || AJS.Data.get("use-keyboard-shortcuts") === "true") {
            popup.addHelpText(AJS.params.shortcutDialogTip, {shortcut: "s"});
        }

        return popup;
    }

    function setCurrentStatus(status) {
        $(".current-user-latest-status .status-text").html(status.text);

        $(".current-user-latest-status a[id^=view]").each(function() {
            var $this = $(this),
                href = $this.attr("href");
            $this.attr("href", href.replace(/\d+$/, status.id))
                   .text(status.friendlyDate)
                   .attr("title", new Date(status.date).toLocaleString());
        });
    }

    function getLatestStatus() {
        $.getJSON(AJS.Confluence.getContextPath() + "/status/current.action", function(data) {
            if (data.errorMessage != null) {
                popup.setError(data.errorMessage);
            }
            else {
                setCurrentStatus(data);
            }
        });
    }

    var updateStatus = function() {
        var textarea = $("#update-user-status-dialog #status-text")
                       .attr("disabled", "disabled")
                       .attr("readonly", "readonly")
                       .blur();
        var text = textarea.val();
        $(".status-update-button").attr("disabled", "disabled");

        if (text.length > maxChars || isBlank(text)) {
            return false;
        }
        AJS.safe.ajax({
            url: AJS.Confluence.getContextPath() + "/status/update.action",
            type: "POST",
            dataType: "json",
            data: {
                "text": text
            },
            success: function(data) {
                if (data.errorMessage != null) {
                    popup.setError(data.errorMessage);
                }
                else {
                    setCurrentStatus(data);
                    textarea.val("");
                    setTimeout(function() { popup.hide(); }, 1000);
                }
            },
            error: function(xhr, text, error) {
                AJS.log("Error updating status: " + text);
                AJS.log(error);
                popup.setError("There was an error - " + error);
            }
        });
    };
    $("#set-user-status-link").click(function(e) {
        var dropDown = $(this).parents(".ajs-drop-down")[0];
        dropDown && dropDown.hide();

        if (typeof popup == "undefined") {
            popup = createPopUp();
            var $charsLeft = $("#update-status-chars-left");
            var $updateButton = $(".status-update-button").attr("disabled", "disabled");
            $("#update-user-status-dialog form.update-status #status-text").keydown(function(e) {
                if (e.which == 13) { // Enter
                    updateStatus();
                }
            }).bind("blur focus change " + ($.browser.mozilla ? "paste input" : "keyup"), function() {
                var length = maxChars - $(this).val().length;
                $charsLeft.removeClass("over-limit").removeClass("close-to-limit").text(length);
                $updateButton.removeAttr("disabled");

                if (isBlank($(this).val())) {
                    $updateButton.attr("disabled", "disabled");
                }
                if (length < 0) {
                    $charsLeft.addClass("over-limit").html("&minus;" + -length);
                    $updateButton.attr("disabled", "disabled");
                }
                else if (length < 20) {
                    $charsLeft.addClass("close-to-limit");
                }
            });
            $("#update-user-status-dialog form.update-status").submit(function(e) {
                updateStatus();
                return AJS.stopEvent(e);
            });
        }
        popup.setError("");
        getLatestStatus();
        popup.show();
        $("#update-user-status-dialog #status-text").removeAttr("readonly").removeAttr("disabled").focus();
        return AJS.stopEvent(e);
    });
});
