jQuery.fn.sizeToFit = function () {
    var $ = jQuery;
    this.each(function () {
        var content = this;
        var container = $(this).parent();
        var outerHeight = container.height();
        container.children().each(function () {
            if (this != content) {
                outerHeight -= $(this).outerHeight();
            }
        });
        var paddingAndBorderHeight = $(this).outerHeight() - $(this).height();
        $(this).css("height", Math.max(0, outerHeight - paddingAndBorderHeight) + "px");
    });
    return this;
};

AJS.Editor.ImageDialog = AJS.Editor.ImageDialog || {
    /**
     * Listeners called just before the image dialog is shown
     */
    beforeShowListeners: [],

    /**
     * Listeners called after thumbnails of images have been drawn
     */
    afterThumbnailsDisplayedListeners: []
};

AJS.toInit(function ($) {
    AJS.wikiAttrToString = function (attr) {
        var res = [];
        for (var prop in attr) if (attr.hasOwnProperty(prop)) {
            res.push(typeof attr[prop] == "boolean" ? attr[prop] ? prop : "" : prop + "=" + attr[prop]);
        }
        return res.length ? "|" + res.join(",") : "";
    };

    AJS.Editor.insertImageDialog = function (insertCallback, cancelCallback) {
        this.openImageDialog({
            submitCallback: insertCallback,
            cancelCallback: cancelCallback
        });
    };

    /**
     * Opens the image dialog. If the options include an imageProperties object, the image represented by the
     * properties will be edited. If not, an insert dialog is shown.
     *
     * @param options for opening the dialog, includes:
     *  - submitCallback called when the dialog form is submitted
     *  - cancelCallback called when the dialog is cancelled
     *  - imageProperties properties of an image to load in the dialog
     */
    AJS.Editor.openImageDialog = function (options) {
        options = options || {};

        var dim = 100,
            ERROR_MAX_LENGTH = 97;
        var selectedName = "",
            popup = new AJS.Dialog(800, 590, "insert-image-dialog");

        // For pages and blogs this is their own pageId. For comments, pageId is the page they are on.
        // For drafts it is contentId.
        var attachmentSourceContentId = AJS.params.attachmentSourceContentId;

        var buildParams = function(context) {
            var res = {},
                align = $(".img-align", context).val(),
                thumb = !!$(".img-thumbnail", context).attr("checked"),
                border = !!$(".img-border", context).attr("checked");
            align != "none" && (res.align = align);
            thumb && (res.thumbnail = true);
            border && (res.border = "1");
            return res;
        };

        function killDialog() {
            popup.hide().remove();
            $(document).unbind(".insert-image");
            options.cancelCallback && options.cancelCallback();
        }

        $(document).bind("keydown.insert-image", function (e) {
            if (e.which == 27 && !$("#fancy_overlay").is(":visible")) {
                killDialog();
                return AJS.stopEvent(e);
            }
        });

        var dialogTitle = AJS.I18n.getText("image.browser.insert.title");
        var submitText = AJS.I18n.getText("image.browser.insert.button");
        if (options.imageProperties) {
            dialogTitle = AJS.I18n.getText("image.browser.edit.title");
            submitText = AJS.I18n.getText("image.browser.edit.button");
        }
        popup.addHeader(dialogTitle);
        popup.addPanel(AJS.I18n.getText("image.browser.attached.images.title"), AJS.renderTemplate("attachedImages", AJS.getTemplate("imagePropertiesForm")), "attachments-panel");
        popup.addPanel(AJS.I18n.getText("image.browser.web.image.title"), AJS.renderTemplate("webImage", AJS.getTemplate("imagePropertiesForm")), "web-image-panel");
        popup.addButton(submitText, function (dialog) {
            var imageParams = buildParams(dialog.getCurrentPanel().body);
            dialog.remove();
            $(document).unbind(".insert-image");
            options.submitCallback && options.submitCallback(selectedName, imageParams, attachmentSourceContentId);
        });
        popup.addCancel(AJS.I18n.getText("cancel.name"), killDialog);
        popup.get("panel:0").setPadding(0);
        popup.get("panel:1").setPadding(0);
        popup.get("panel:0").select();
        // Unfortunately we need to access the insert button by id since the name can vary depending on language.
        var insertButton = popup.get("button:0")[0].item;
        insertButton.attr("disabled", "disabled");

        $("input.image-url", popup.popup.element).bind("keyup click", function (e) {
            var val = $(this).val();
            selectedName = val;
            insertButton.attr("disabled", (val != "" && val != "http://" ? "" : "disabled"));
            if (e.which == 13) {
                $("input.image-preview", popup.popup.element).click();
                popup.get("button:0")[0].item.focus(); // move focus to Insert button
            }
        });
        $("input.image-preview", popup.popup.element).click(function () {
            var container = $(this).closest("div");
            var src = container.find("input.image-url").val();
            var preview = container.find(".image-preview-area");
            var throbber = container.find(".image-preview-throbber");
            throbber.removeClass("hidden");
            var killSpinner = Raphael.spinner(throbber[0], 60, "#666");
            var error = container.find(".image-preview-error");
            preview.addClass("faraway");
            error.addClass("hidden");
            preview.html("");
            $("<img>").load(function () {
                killSpinner();
                throbber.addClass("hidden");
                preview.removeClass("faraway");
            }).error(function () {
                killSpinner();
                throbber.addClass("hidden");
                error.removeClass("hidden");
            }).appendTo(preview).attr("src", src);
        });
        if (options.imageProperties) {
            $(".img-align", popup.popup.element).val(options.imageProperties.align || "none");
            $(".img-thumbnail", popup.popup.element).attr("checked", options.imageProperties.thumbnail || "");
            $(".img-border", popup.popup.element).attr("checked", !!options.imageProperties.border || "");

            if (options.imageProperties.url) {
                popup.get("panel:1").select();
                $("input.image-url", popup.popup.element).val(options.imageProperties.url).click();
                $("input.image-preview", popup.popup.element).click();
            }
        }

        AJS.log(AJS.Editor.ImageDialog.beforeShowListeners.length + " beforeShow listeners registered.");
        $.each(AJS.Editor.ImageDialog.beforeShowListeners, function () {
            this();
        });
        popup.show();
        popup.popup.element.find(".dialog-button-panel").append(AJS.template.load("insert-image-did-you-know"));

        $("select.img-align").focus();

        var uploadForm = $("#upload-attachment form");
        var uploadingMessage = $("#upload-attachment .image-uploading");
        var uploadingError = $("#upload-attachment .warning");
        var imageContainer = $("#attached-images");

        /**
         * Clear any existing errors.
         */
        AJS.Editor.ImageDialog.clearErrors = function () {
            uploadingError.addClass("hidden");
            uploadingError.empty();
            imageContainer.sizeToFit();
        };

        /**
         * Displays errors. Subsequent calls will not overwrite existing messages but append to them.
         * To clear existing messages, use AJS.Editor.ImageDialog.clearErrors().
         * @param messages an array of error messages
         */
        AJS.Editor.ImageDialog.displayErrors = function (messages) {
            if (!messages || !messages.length) {
                return;
            }
            uploadingError.removeClass("hidden");
            var $errors = $("ul", uploadingError);
            if (!$errors.length) {
                $errors = $("<ul></ul>");
                $errors.appendTo(uploadingError);
            }
            $.each(messages, function(index, value) {
                if (!value) {
                    return;
                }
                $("<li>" + value.substring(0, Math.min(ERROR_MAX_LENGTH, value.length)) + (value.length > ERROR_MAX_LENGTH ? "&hellip;" : "") + "</li>").attr("title", value).appendTo($errors);
            });
            $("#attached-images").sizeToFit();
        };

        /**
         * Returns a CSS selector to locate the the thumbnails/images container of this dialog.
         * (for plugins).
         */
        AJS.Editor.ImageDialog.imagesContainerSelector = "#attached-images .image-list";

        uploadForm.ajaxForm({
            dataType: "json",
            data: {
                contentId: attachmentSourceContentId,
                responseFormat: "html" // ensure response comes back as HTML for IE compatibility
            },
            resetForm: true,
            beforeSubmit: function () {
                AJS.Editor.ImageDialog.setUploadInProgress(true);
                AJS.Editor.ImageDialog.clearErrors();
            },
            error: function (xhr) {
                AJS.Editor.ImageDialog.setUploadInProgress(false);
                AJS.Editor.ImageDialog.displayErrors([AJS.I18n.getText("image.browser.upload.error")]);
                AJS.log("Response from server was: " + xhr.responseText);
            },
            success: function (response) {
                AJS.Editor.ImageDialog.setUploadInProgress(false);
                var errors = [].concat(response.validationErrors || []).concat(response.actionErrors || []).concat(response.errorMessage || []);
                if (errors.length > 0) {
                    AJS.Editor.ImageDialog.displayErrors(errors);
                    return;
                }
                AJS.Editor.ImageDialog.refreshWithLatestImages($.map(response.attachmentsAdded || [], function (element) {
                    return element.name;
                }));
            }
        });
        uploadForm.find("input:file").change(function () { uploadForm.submit(); });

        function renderImage(img) {
            if (Math.max(img.thumbnailWidth, img.thumbnailHeight) > dim) {
                if (img.thumbnailHeight > img.thumbnailWidth) {
                    img.thumbnailWidth = img.thumbnailWidth * dim / img.thumbnailHeight;
                    img.thumbnailHeight = dim;
                } else {
                    img.thumbnailHeight = img.thumbnailHeight * dim / img.thumbnailWidth;
                    img.thumbnailWidth = dim;
                }
            }
            var nonceUrl = img.thumbnailUrl + (img.thumbnailUrl.indexOf("?") + 1 ? "&" : "?") + "nonce=" + (+new Date);
            var result = $(AJS.renderTemplate("imageDialogImage", nonceUrl, img.thumbnailWidth, img.thumbnailHeight,
                    (100 - img.thumbnailHeight) / 2, img.downloadUrl, img.name));
            result.find(".image-container").andSelf().hover(function () {
                $(this).addClass("hover");
            }, function () {
                $(this).removeClass("hover");
            });
            result.find("img").load(function () {
                result.find(".image-container").removeClass("loading");
            });
            result.click(function (e) {
                $("#attached-images .selected").removeClass("selected");
                result.addClass("selected").focus();
                selectedName = this.name = this.name || $(".caption", this).text();
                insertButton.attr("disabled", "");
                return AJS.stopEvent(e); // prevent propagation to container, which when clicked deselects
            });
            result.dblclick(function () {
                $(this).click();
                insertButton.click();
            });
            $(".zoom", result).fancybox({
                padding: 0,
                zoomSpeedIn: 500,
                zoomSpeedOut: 500,
                overlayShow: true,
                overlayOpacity: 0.5
            });
            return result;
        }

        $(document).bind("keydown.insert-image", function (e) {
            if (!imageContainer.is(":visible")) return;
            if ($("#fancy_overlay").is(":visible")) {
                if (e.which == 32) { // space bar
                    $("#fancy_close").click();
                    e.preventDefault();
                    e.stopPropagation();
                    return false;
                }
            } else {
                function moveSelection(delta) {
                    var results = $(".attached-image", imageContainer);
                    var selected = $(".attached-image.selected", imageContainer);
                    var index = results.index(selected) + delta;
                    if (index < 0) index = results.length - 1;
                    if (index >= results.length) index = 0;

                    var next = results.eq(index);
                    next.click().focus();
                    imageContainer.simpleScrollTo(next);
                }

                if (e.which == 37) { // left
                    moveSelection(-1);
                    return AJS.stopEvent(e);
                } else if (e.which == 38) { // up
                    moveSelection(-4);
                    return AJS.stopEvent(e);
                } else if (e.which == 39) { // right
                    moveSelection(1);
                    return AJS.stopEvent(e);
                } else if (e.which == 40) { // down
                    moveSelection(4);
                    return AJS.stopEvent(e);
                } else if (e.which == 32 && $(".attached-image.selected").length > 0) { // space bar
                    $(".attached-image.selected .zoom").click();
                    return AJS.stopEvent(e);
                } else if (e.which == 13 && !insertButton.is(":disabled")) { // enter
                    insertButton.click();
                    return AJS.stopEvent(e);
                }
            }
        });

        /**
         * Activates/deactivates the in progress animation and message
         * @param inprogress whether or not the dialog should indicate something is in progress or not
         * @param message [optional] a custom message to use in place of the default
         */
        AJS.Editor.ImageDialog.setUploadInProgress = function (inprogress, message) {
            if (inprogress) {
                uploadForm.addClass("hidden");
                uploadingMessage.removeClass("hidden");
                message ? uploadingMessage.html(message) : uploadingMessage.html(AJS.I18n.getText("image.browser.upload.image.uploading"));
            } else {
                uploadForm.removeClass("hidden");
                uploadingMessage.addClass("hidden");
            }
        };

        /**
         * Fetch and render all latest images
         * @param justAttached [optional] an array of filenames that have just been attached (we want to promote these in some way)
         */
        AJS.Editor.ImageDialog.refreshWithLatestImages = function (justAttached) {
            justAttached = $.map(justAttached || [], function (filename) {
                return filename && filename.toLowerCase();
            }); // ensure we dealing with lowercase filenames
            $.ajax({
                type: "GET",
                url: AJS.params.contextPath + "/pages/attachedimages.action",
                dataType: "json",
                data: {
                    contentId: attachmentSourceContentId
                },
                error: function () {
                    imageContainer.find(".loading-message").remove();
                    imageContainer.append(AJS.renderTemplate("imageDialogErrorRetrievingAttachments"));
                },
                success: function (data) {
                    // clean up container first before inserting images
                    imageContainer.find(".loading-message").remove();
                    imageContainer.find(".image-list").empty();
                    imageContainer.find(".no-attachments").remove();

                    $(data.images || []).each(function () {
                        if (this.name && $.inArray(this.name.toLowerCase(), justAttached) != -1) {
                            imageContainer.find(".image-list").prepend(renderImage(this));
                        } else {
                            imageContainer.find(".image-list").append(renderImage(this));
                        }
                    });
                    if (imageContainer.find(".image-list li").length == 0) {
                        imageContainer.append(AJS.renderTemplate("imageDialogNoAttachments"));
                    }
                    imageContainer.sizeToFit().click(function () {
                        // deselect when clicking outside the images
                        selectedName = null;
                        insertButton.attr("disabled", "disabled");
                        $(this).find(".selected").removeClass("selected");
                    });

                    if (justAttached.length) {
                        // ensure the first attached image is selected
                        imageContainer.find(".image-list li:first").click();
                    } else if (options.imageProperties && options.imageProperties.imageFileName) {
                        // If editing an existing image, select the image
                        imageContainer.find("img[src*=/" + options.imageProperties.imageFileName + "?]").click();
                    }

                    AJS.log(AJS.Editor.ImageDialog.afterThumbnailsDisplayedListeners.length + " afterThumbnailsDisplayed listeners registered.");
                    $.each(AJS.Editor.ImageDialog.afterThumbnailsDisplayedListeners, function () {
                        this();
                    });

                    // handle non-thumbnailable files
                    var notThumbnailableErrors = [];
                    var imageFilenames = $.map(data.images || [], function (image) {
                        return image.name && image.name.toLowerCase();
                    });
                    $.each(justAttached, function (index, value) {
                        $.inArray(value, imageFilenames) == -1 && notThumbnailableErrors.push(AJS.renderTemplate("imageNotThumbnailable", value));
                    });
                    notThumbnailableErrors && AJS.Editor.ImageDialog.displayErrors(notThumbnailableErrors);
                }
            });
        };
        AJS.Editor.ImageDialog.refreshWithLatestImages();
    };

    $("#editor-insert-image").click(function (e) {
        AJS.Editor.storeTextareaBits();
        var textarea = document.getElementById("markupTextarea");
        AJS.Editor.insertImageDialog(function (selectedName, params) {
            AJS.Editor.Markup.insertOrUpdateText(
                AJS.format("\n!{0}{1}!\n", selectedName, AJS.wikiAttrToString(params)),
                textarea);
        });
        return AJS.stopEvent(e);
    });
});
