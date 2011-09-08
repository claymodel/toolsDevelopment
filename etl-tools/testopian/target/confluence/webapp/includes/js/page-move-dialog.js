AJS.toInit(function ($) {
    var MovePageDialog = function (options) {
        options = $.extend({
            spaceKey: AJS.params.spaceKey,
            spaceName: AJS.params.spaceName,
            parentPageTitle: AJS.params.parentPageTitle,
            title: AJS.params.movePageDialogViewPageTitle, // "Move Page: 'Title'
            buttonName: AJS.params.movePageDialogMoveButton, // "Move"
            moveHandler: function (dialog) {
                AJS.log("No move handler defined. Closing dialog.");
                dialog.remove();
            },
            cancelHandler: function (dialog) {
                dialog.remove();
            },
            pageTitleAccessor: function () {
                return AJS.params.pageTitle;
            }
        }, options);

        var newLocation = {
            spaceKey : options.spaceKey,
            spaceName : options.spaceName,
            parentPageTitle : options.parentPageTitle
        };
        var newSpaceKey = options.spaceKey;
        var newSpaceName = options.spaceName;
        var newParentPage = options.parentPageTitle;

        var reorderTargetId = "";
        var reorderTargetPosition = "";

        // called when the ordering of a page is set beneath a parent.
        var reorder = function (targetId, positionIndicator) {
            reorderTargetId = targetId;
            reorderTargetPosition = positionIndicator;
        };

        var structure = AJS.ConfluenceDialog({
            width : 800,
            height: 590,
            id: "move-page-dialog"
        });
        structure.addHeader(options.title);
        structure.addPanel(AJS.params.movePageDialogLocationPanel, AJS.renderTemplate("movePageDialog"), "location-panel", "location-panel-id");
        structure.addPanel(AJS.params.movePageDialogSearchPanel, AJS.renderTemplate("movePageSearchPanel"), "search-panel", "search-panel-id");
        structure.addPanel(AJS.params.movePageDialogHistoryPanel, AJS.renderTemplate("movePageHistoryPanel"), "history-panel", "history-panel-id");
        structure.addPanel(AJS.params.movePageDialogBrowsePanel, AJS.renderTemplate("movePageBrowsePanel"), "browse-panel", "browse-panel-id");

        // panel switching logic
        structure.get("panel:0")[0].onselect = function () {
            $("#new-space-key").val(newSpaceKey);
            $("#new-space").val(newSpaceName);
            $("#new-parent-page").val(newParentPage).select();
        };
        structure.get("panel:1")[0].onselect = function () {
            // always clear out the previous selection
            $("#move-page-dialog .search-panel .search-results .selected").removeClass("selected");
            $("#move-page-dialog input.search-query").focus();
        };
        structure.get("panel:0").select();

        gotoReorderPage = function (dialog) {
            dialog.nextPage();
            var dialogDom = $("#move-page-dialog");
            $(".ordering-panel", dialogDom).movePageOrdering(newSpaceKey, newParentPage, options.pageTitleAccessor(), reorder);
        };

        moveFunction = function (dialog) {
            var space = $("#new-space:visible").val();
            var spaceKey = $("#new-space-key").val();
            var parentPage = $("#new-parent-page:visible").val();
            if (space && (space != newSpaceName || spaceKey != newSpaceKey || parentPage != newParentPage)) {
                AJS.MoveDialog.getBreadcrumbs({spaceKey:spaceKey, pageTitle:parentPage}, function () {
                    options.moveHandler(dialog, spaceKey, space, parentPage, reorderTargetId, reorderTargetPosition, setErrors);
                }, function (xhr) {
                        $('#new-parent-breadcrumbs').html(AJS.renderTemplate("movePageBreadcrumbError"));
                        if (xhr.status == 404) {
                            controls.error(AJS.params.movePageDialogLocationNotFound);
                        }
                    });
            } else {
                options.moveHandler(dialog, newSpaceKey, newSpaceName, newParentPage, reorderTargetId, reorderTargetPosition, setErrors);
            }
        };

        // Decide whether to execute the move or goto the re-order page instead.
        executeMove = function (dialog) {
            if ($("#reorderCheck")[0].checked) {
                gotoReorderPage(dialog);
            } else {
                moveFunction(dialog);
            }
        };

        structure.addButton(options.buttonName, executeMove, "move-button");
        structure.addCancel(AJS.I18n.getText("cancel.name"), options.cancelHandler);
        structure.popup.element.find(".dialog-title").append(AJS.template.load("move-help-link"));

        // Add the ordering page
        structure.addPage()
        .addHeader(options.title)
        .addPanel(AJS.params.movePageDialogOrderingTitle, AJS.renderTemplate("orderingPagePanel"), "ordering-panel", "ordering-panel-id")
        .addLink(AJS.params.movePageDialogBackButton, function(dialog) { dialog.prevPage(); }, "dialog-back-link")
        .addButton(AJS.params.movePageDialogMoveAndOrderButton, moveFunction, "reorder-button")
        .addCancel(AJS.params.movePageDialogCancelButton, options.cancelHandler);

        var moveButton = structure.get("button#" + options.buttonName)[0].item;
        $("button.move-button").before(AJS.renderTemplate("reorderCheckbox"));

        structure.gotoPage(0);
        structure.show();

        var dialog = $("#move-page-dialog");

        // move breadcrumbs to the bottom of all pages on the first page of the dialog (location selection page)
        $(".location-panel .location-info", dialog).appendTo($(".dialog-page-body:first", dialog));

        // error messages next to the buttons
        $(".dialog-button-panel:visible", dialog).prepend(AJS.renderTemplate("movePageErrors"));

        var breadcrumbs = new AJS.MoveDialog.Breadcrumbs($('#new-parent-breadcrumbs'));

        function setErrors(errors) {
            if (!errors || errors.length == 0) {
                $("#move-errors").addClass("hidden");
                $(moveButton).attr("disabled", "");
                return;
            }
            if (!$.isArray(errors)) errors = [ errors ];
            $("#move-errors").text(errors[0]).attr("title", errors.join("\n")).removeClass("hidden");
            structure.gotoPage(0); // errors all show on the first page, where you can correct them
        }

        var controls = {
            moveButton: moveButton,
            clearErrors: function () {
                setErrors([]);
            },
            error: setErrors,

            // called when a destination is selected on one of the panels
            select: function (spaceKey, spaceName, parentPageTitle) {
                AJS.log("select: " +[spaceKey, spaceName, parentPageTitle].join());
                newSpaceKey = spaceKey;
                newSpaceName = spaceName;
                newParentPage = parentPageTitle || "";
                $(moveButton).attr("disabled", "disabled"); // disable submission until the location is validated
                breadcrumbs.update({spaceKey:newSpaceKey, title:newParentPage}, controls);

            }
        };

        // render the current breadcrumbs immediately
        var originalParent = AJS.params.parentPageTitle != "" ? AJS.params.parentPageTitle : AJS.params.fromPageTitle;
        var currentBreadcrumbs = new AJS.MoveDialog.Breadcrumbs($('#current-parent-breadcrumbs'));
        currentBreadcrumbs.update({spaceKey:AJS.params.spaceKey, title:originalParent}, controls);

        $(".location-panel", dialog).movePageLocation(controls);
        $(".search-panel", dialog).movePageSearch(controls);
        $(".history-panel", dialog).movePageHistory(controls);
        structure.get("panel:2")[0].onselect = function () {
            // refresh the history panel every time it loads, in case the user has navigated elsewhere in another tab
            $(".history-panel", dialog).movePageHistory(controls);
        };
        structure.get("panel:3")[0].onselect = function () {
            // always refresh the tree when loading the Browse tab, don't load it initially
			AJS.log("browse: " +[newSpaceKey, newSpaceName, newParentPage].join());
            $(".browse-panel", dialog).movePageBrowse(controls, newSpaceKey, newSpaceName, newParentPage, originalParent, options.pageTitleAccessor());
        };

        $("#new-parent-page").select(); // focus the new parent page input

        return dialog;
    };

    var MovePageParams = function (spaceKey, pageTitle, siblingId, siblingRelativePosition) {
        var params = {
            pageId: AJS.params.pageId,
            spaceKey: spaceKey
        };

        if (siblingId) {
            params.position = siblingRelativePosition; // may be above or below
            params.targetId = siblingId;
        }
        else if (pageTitle != "") {
            params.targetTitle = pageTitle;
            params.position = "append";
        } else {
            params.position = "topLevel";
        }
        return params;
    };

    function viewPageMoveHandler(dialog, newSpaceKey, newSpaceName, newParentPage, newSiblingId, newSiblingPosition, setErrors) {
        dialog = dialog.popup.element;
        dialog.addClass("waiting");
        $("button", dialog).attr("disabled", "disabled");
        var throbber = $("<div class='throbber'></div>");
        dialog.append(throbber);
        var killSpinner = Raphael.spinner(throbber[0], 100, "#666");

        function error(messages) {
            setErrors(messages);
            dialog.removeClass("waiting");
            killSpinner();
            $("button", dialog).attr("disabled", "");
        }

        $.ajax({
            url: contextPath + "/pages/movepage.action",
            type: "GET",
            dataType: "json",
            data: new MovePageParams(newSpaceKey, newParentPage, newSiblingId, newSiblingPosition),
            error: function () {
                error(AJS.params.movePageDialogMoveFailed);
            },
            success: function (data) {
                var errors = [].concat(data.validationErrors || []).concat(data.actionErrors || []).concat(data.errorMessage || []);
                if (errors.length > 0) {
                    error(errors);
                    return;
                }
                window.location.href = contextPath + data.page.url + (data.page.url.indexOf("?") >= 0 ? "&" : "?") + "moved=true";
            }
        });
    }

    $("#action-move-page-dialog-link").click(function (e) {
        e.preventDefault();

        if ($("#move-page-dialog").length > 0) {
            $("#move-page-dialog, body > .shadow, body > .aui-blanket").remove();
        }

        new MovePageDialog({
            moveHandler: viewPageMoveHandler
        });

        return false;
    });

    $("#edit-move-page-dialog-link").click(function (e) {
        e.preventDefault();

        if ($("#move-page-dialog").length > 0) {
            $("#move-page-dialog, body > .shadow, body > .aui-blanket").remove();
        }

        new MovePageDialog({
            spaceKey: $("#newSpaceKey").val(),
            spaceName: $("#space_content").text(),
            parentPageTitle: $("#parentPageString").val(),
            buttonName: AJS.params.movePageDialogMoveButton,
            title: AJS.params.movePageDialogEditPageTitle,
            moveHandler: function (dialog, newSpaceKey, newSpaceName, newParentPage, targetId, newPositionIndicator, setErrors) {
                // TODO: AJAX validation, should use setErrors
                $("#space_content").text(newSpaceName);
                $("#newSpaceKey").val(newSpaceKey);
                $("#parentPageString").val(newParentPage);
                $("#parent_content").text(newParentPage);
                if (newParentPage != "") {
                    $("#position").val("append");
                    $("#parent_info").removeClass("hidden"); // display the new parent info
                } else {
                    $("#parent_info").addClass("hidden"); // hide the parent info since the move is to the top level
                    $("#position").val("topLevel");
                }

                // If explicit position has been set then override the positions that may have been set up
                if (targetId) {
                    $("#targetId").val(targetId);
                    $("#position").val(newPositionIndicator);
                }

                dialog.remove();
            },
            pageTitleAccessor: function () {
                return $("#content-title").val();
            }
        });

        return false;
    });

});
