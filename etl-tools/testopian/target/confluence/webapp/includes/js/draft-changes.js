AJS.toInit(function($) {

    var popup;

    var buildPopup = function() {
        popup = new AJS.Dialog(860, 530, "view-diff-draft-dialog");
            var heading = AJS.params.draftHeading;
            popup.addHeader(heading.replace(/\{0\}/, ""));
            var draftDialog = $("#draft-changes-dialog");
            popup.addPanel("Diff", draftDialog);
            popup.addButton(AJS.params.resumeDraft, function (e) {
                popup.hide();
                if (AJS.Editor) {
                    AJS.Editor.sendFormDraft("useDraft");
                } else {
                    window.location = $(this).attr("data-href");
                }
            }, "resume-diff-link");
            popup.addButton(AJS.params.mergeDraft, function (e) {
                popup.hide();
                window.parent.location = $(this).attr("data-href");
            }, "merge-diff-link");
            popup.addButton(AJS.params.discardDraft, function (e) {
                popup.hide();
                if (AJS.Editor) {
                    AJS.Editor.sendFormDraft("discardDraft");
                } else {
                    window.location = $(this).attr("data-href");
                }
            }, "discard-diff-link");
            popup.addCancel(AJS.I18n.getText("close.name"), function () {popup.hide();});
            draftDialog.removeClass("hidden");
    };

      var loadDiffInDialog = function(data, draftId) {
        var buildDiff = function (data) {
            if (!data.numChanges)
                return AJS.params.draftNoChanges;

            var textHtml = "";
            var chunkLists = data.chunks;
            var length = chunkLists.length;
            for (var i = 0; i < length; i++)
            {
                textHtml += chunkLists[i].text;
            }
            return textHtml;
        };

        $("#diff-view").html(buildDiff(data));
        var heading = AJS.params.draftHeading;
        popup.addHeader(heading.replace(/\{0\}/, data.title));

        var atl_token = $("#atlassian-token").attr("content");

        // Change the link url
        var contextPath = AJS.General.getContextPath();
        $(".merge-diff-link").attr("data-href", contextPath + "/pages/resumedraft.action?draftId=" + draftId);
        $(".resume-diff-link").attr("data-href", contextPath + "/pages/resumedraft.action?draftId=" + draftId);
        $(".discard-diff-link").attr("data-href", contextPath + "/users/deletedraft.action?draftId=" + draftId + "&atl_token=" + atl_token);

        AJS.setVisible("#merge-warning", data.isMergeRequired);
        AJS.setVisible(".merge-diff-link", data.isMergeRequired);
        AJS.setVisible(".resume-diff-link", !data.isMergeRequired);
    };

    var getDiffForLink = function(difflink) {
        var pageId, username, draftId;
        var loadDiffParamsFromLink = function(difflinkClass) {
            var matched = /draftPageId:([^ ]*)/.exec(difflinkClass);
            pageId = matched ? matched[1] : AJS.params.pageId;


            matched = /username:([^ ]*)/.exec(difflinkClass);
            username = matched ? matched[1] : AJS.params.remoteUser;

            matched = /draftId:([^ ]*)/.exec(difflinkClass);
            draftId = matched ? matched[1] : null;
        };

        loadDiffParamsFromLink(difflink.attr("class"));

        // TODO - check AJS.post, AJS.getJSON etc for safe
        AJS.safeAjax({
            url: AJS.General.getContextPath() + "/draftchanges/viewdraftchanges.action",
            type: "GET",
            dataType: "json",
            data: {
                "pageId": pageId,
                "username": username
            },
            success: function(data) {
                if (data.actionErrors) {  // TODO - make nicer
                    var errorHtml = "";
                    var errors = data.actionErrors;
                    for (var i = 0; i < errors.length; i++)
                    {
                        AJS.log("error: " + (errors[i]));
                        errorHtml = errorHtml + "<div>" + errors[i] + "</div>";
                    }
                    $("#diff-view").html(errorHtml);
                } else {
                    loadDiffInDialog(data, draftId);
                }
            },
            error: function(data) {
                var msg = data["errors"] || "An unknown error has occurred. Please check your logs";
                $("#diff-view").html(msg);
            }
        });
    };

    var openDiffDialog = function(difflink, showMenu) {

        if (AJS.Editor)
            AJS.Editor.saveDraft(false);

        if (!popup) {
            buildPopup();
        }
        popup.addHeader(AJS.params.loadingHeading);
        $("#diff-view").html("<tr><td id='draft-changes-waiting-icon'>Loading...</td></tr>");
        AJS.setVisible("#diff-links", showMenu);
        getDiffForLink(difflink);
        popup.show();
    };

    // For edit page
    $("#draft-status").click(function (e) {
        var target = $(e.target);
        if (target.hasClass("view-diff-link")) {
            openDiffDialog(target, false);
        }
        return AJS.stopEvent(e);
    });

    // For "View my drafts" page and banner
    $(".view-diff-link").click(function (e) {
        var difflink = $(this);

        openDiffDialog(difflink, true);
        return AJS.stopEvent(e);
    });
});
