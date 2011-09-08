AJS.Editor = (function($) { return {
    // Save the last edit mode in case the user changes to preview and from there to the other edit mode...
    // then we will have to convert the markup to XHTML or vice verca.
    lastEditMode: null,
    lastKnownGoodContent: null,
    contentHasChangedSinceLastAutoSave: false,
    isDraftSaved: false,
    originalWikiContent: "",

    syncTitleFieldWithForm: function() {
        var hiddenContentTitle = AJS.$("#hidden-content-title");
        if (hiddenContentTitle.length) {
            // Title field has been moved out of form to top of page,
            // copy the current field value into hidden field if value written.
            var title = "";

            var titleWrittenField = AJS.$("#titleWritten");
            if (!titleWrittenField.length || titleWrittenField.val() != "false") {
                // Creating a page - title may be "New Page" as placeholder, don't copy.
                title = AJS.$("#content-title").val();
            }

            hiddenContentTitle.val(title);
        }
    },

    // Save/Cancel fire unload, but draft shouldn't be saved.
    isSubmitting: false,

    // Flag used to determine if handleUnload function should run.
    isUnloaded: false,

    hasContentChanged : function () {
        var rte = AJS.params.useWysiwyg && this.inRichTextMode();
        if (!rte && !this.contentHasChangedSinceLastAutoSave)
            return false;

        return this.editorHasContentChanged(rte);
    },

    editorHasContentChanged: function (isRTEMode) {
        if (isRTEMode)
            return this.Adapter.editorHasContentChanged();

        return this.originalWikiContent != this.getCurrentFormContent();
    },

    /**
     * Saves a draft.
     *
     * @param options
     *      async : whether the xml http request should operate synchronously or asynchronously
     *      onSuccessHandler : callback that is invoked on draft save success. Function should be formatted like this: function (responseData, isEditingNewPage) {}
     *      onErrorHandler : callback that is invoked on draft save error. Function should be formatted like this: function (responseBody) {}
     *      forceSave : forces a draft save even if there are no content changes (that is, AJS.Editor.hasContentChanged() == false)
     */
    saveDraft: function (options) {
        var defaults = { async: true };
        if (typeof options == "boolean") { // to enable backwards compatability with saveDraft(boolean async) {}
            options = { async: options };
        } else if (typeof options == "number") { // firebug is reporting that this method is being called with weird numbers - I can't for life of me find in the code where
            options = defaults;
        } else {
            options = AJS.$.extend({}, defaults, options);
        }

        if (!AJS.params.saveDrafts || AJS.Editor.isSubmitting || (!options.forceSave && !AJS.Editor.hasContentChanged())) {
            return;
        }
        AJS.Editor.syncTitleFieldWithForm();
        var form = AJS.Editor.getCurrentForm();
        var draftData = {
            pageId : AJS.params.pageId,
            type : AJS.params.draftType,
            title : AJS.$("#hidden-content-title").val(),
            content : AJS.Editor.getCurrentFormContent()
        };

        var newSpaceKey = AJS.$("#newSpaceKey");
        if (newSpaceKey.length) {
            draftData.spaceKey = newSpaceKey.val();
        } else {
            draftData.spaceKey = encodeURIComponent(AJS.params.spaceKey);
        }
        var originalVersion = AJS.$("#originalVersion");
        if (originalVersion.length) {
            draftData.pageVersion = parseInt(originalVersion.val(), 10);
        }

        var draftStatus = AJS.$("#draft-status");
        var resetWysiwygContent = AJS.params.useWysiwyg && AJS.Editor.inRichTextMode();

        var jsTime = function (date) { // dodgy time function
            var h = date.getHours();
            var m = date.getMinutes();
            var ampm = h > 11 ? "PM" : "AM";
            h = h % 12;
            return (h == 0 ? "12" : h) + ":" + (m < 10 ? "0" : "") + m + " " + ampm;
        };

        var saveDraftCallback = function (response) {
            AJS.Editor.contentHasChangedSinceLastAutoSave = false;
            if (resetWysiwygContent) {
                AJS.Editor.Adapter.editorResetContentChanged();
            }
            if (response.success)
            {
                AJS.Editor.isDraftSaved = true;
                var detail = {};
                try {
                    detail = eval("(" + response.response + ")");
                } catch (e) {
                    // ignore exception in eval
                }
                var time = detail.time || jsTime(new Date());
                draftStatus.removeClass("error");
                if (AJS.params.newPage)
                    draftStatus.html(AJS.format(AJS.I18n.getText("draft.saved.at.new"), time));
                else
                    draftStatus.html(AJS.format(AJS.I18n.getText("draft.saved.at"), time, "<a id='view-diff-link-heartbeat' class='view-diff-link' href=#>", "</a>"));
                if (!AJS.params.contentId || AJS.params.contentId === "0")
                    AJS.params.contentId = detail.draftId;
                if (AJS.$.isFunction(options.onSuccessHandler)) {
                    options.onSuccessHandler(detail, AJS.params.newPage);
                }
            } else {
                draftStatus.addClass("error");
                draftStatus.html(response.response);
                if (AJS.$.isFunction(options.onErrorHandler)) {
                    options.onErrorHandler(response.response);
                }
            }
        };
        draftStatus.html(AJS.I18n.getText("draft.saving"));

        draftData['xhtml'] = (form.xhtml.value == "true");
        AJS.safe.ajax({
            type: "POST",
            url: AJS.params.contextPath + "/json/savedraft.action",
            data: draftData,
            success: saveDraftCallback,
            error: function () { saveDraftCallback({ success: false, response: AJS.I18n.getText("draft.saving.timed.out") }); },
            dataType: "json",
            timeout: 30000 // 30 seconds
        });
    },

    // function to send the form to discard/use the draft
    sendFormDraft: function(flagName) {
        this.handleBeforeUnload = function() {};
        var form = this.getCurrentForm();

        this.addHiddenElement(form, flagName, "true");
        this.addHiddenElement(form, "contentChanged", "" + this.hasContentChanged());
        this.addHiddenElement(form, "pageId", AJS.params.pageId);
        if (!form.spaceKey) {
            this.addHiddenElement(form, "spaceKey", AJS.params.spaceKey);
        }

        form.action =  (AJS.params.newPage ? "create" : "edit") + AJS.params.draftType + ".action";
        form.submit();
    },

    /**
     * Returns a relative URL to resume the draft saved for this page
     */
    getResumeDraftUrl: function () {
        var urlParts = [];
        urlParts.push(contextPath);
        urlParts.push("/pages/" + (AJS.params.newPage ? "create" : "edit") + AJS.params.draftType + ".action");
        urlParts.push("?useDraft=true");
        urlParts.push("&pageId=" + AJS.params.pageId);
        urlParts.push("&contentChanged=" + this.hasContentChanged());
        this.getCurrentForm().spaceKey && urlParts.push("&spaceKey=" + AJS.params.spaceKey);
        return urlParts.join("");
    },

    addHiddenElement : function (form, name, value) {
        var el = document.createElement("input");
        el.type = "hidden";
        el.name = name;
        el.value = value;
        form.appendChild(el);
    },

    getCurrentFormContent : function() {
        var form = this.getCurrentForm();
        if (AJS.params.useWysiwyg && form.xhtml.value == 'true') {
            return this.Adapter.getEditorHTML();
        }
        if (form.markupTextarea) {
            return form.markupTextarea.value;
        }
    },

    /**
     * Returns the currently entered title.
     *
     * @return the current editor title, null if not in edit mode, or editing a comment (i.e. something without a title)
     */
    getCurrentTitle : function() {
        return $('#content-title') && $('#content-title').val(); 
    },

    /* This function will be invoked when the form gets submitted. */
    contentFormSubmit: function(e) {
        this.handleBeforeUnload = function() {};
        this.syncTitleFieldWithForm();

        AJS.$("#locationShowing").val("" + AJS.isVisible("#location_div"));
        AJS.$("#labelsShowing").val("" + AJS.isVisible("#labels_div"));

        // CONF-12750 Disable the title field outside the form
        // to prevent Safari 2.0 from sending the "title" field twice
        AJS.$(".editable-title #content-title").attr("disabled","disabled");

        this.isSubmitting = this.checkCaptchaResponse(e);
        return this.isSubmitting;
    },

    // Method checks whether the captchaResponse textfield is empty.
    checkCaptchaResponse: function(e) {
        if (e.target.name == "cancel") {
            return true;
        }

        var captchaTextField = AJS.$("#captchaResponse");

        if (captchaTextField.val() == "") {
            AJS.$("#captchaError").css("display", "block");
            window.scroll(0, 0);
            e.stopPropagation();
            return false;
        }
        return true;
    },

    /**
     * When editing a page then heart beats will double up in function and also detect concurrent edits.
     * When creating a new page 'concurrent edit' is a bit meaningless so the heartbeat will serve just
     * the single purpose of keeping the session alive.
     */
    heartbeat: function() {
        var data = {
            dataType: "json",
            contentId: AJS.params.pageId,
            draftType: AJS.params.draftType
        };
        
        if (AJS.params.pageId == "0" || AJS.params.contentType == "comment") {
            AJS.safe.post(AJS.params.contextPath + "/json/heartbeat.action", {});
        } else {
            AJS.safe.post(AJS.params.contextPath + "/json/startheartbeatactivity.action", data, function(activityResponses) {
                var otherUsersAreEditing = activityResponses.length;
                if (otherUsersAreEditing) {
                    var outerSpan = AJS.$("#other-users-span");
                    outerSpan.empty();
                    for (var i = 0; i < otherUsersAreEditing; ++i) {
                        if (i > 0) {
                            outerSpan.append(", ");
                        }

                        var activityResponse = activityResponses[i];
                        outerSpan.append(AJS('a').attr('href', AJS.params.contextPath + '/display/~' + encodeURIComponent(activityResponse.userName)).text(activityResponse.fullName));
                        if (activityResponse.lastEditMessage != null) {
                            outerSpan.append(" ").append(AJS('span').addClass('smalltext').text(activityResponse.lastEditMessage));
                        }
                    }
                }
                AJS.setVisible("#heartbeat-div", !!otherUsersAreEditing);
            }, "json");
        }
    },

    disableFrame: function(body) {
        //disable all forms, buttons and links in the iframe
        AJS.$("form", body).each(function() {
            AJS.$(this).unbind();
            this.onsubmit = function() {
                return false;
            };
        });
        AJS.$("a", body).each(function() {
            AJS.$(this).attr("target", "_top").unbind();
        });
        AJS.$("input", body).each(function() {
            AJS.$(this).unbind();
        });
    },

    /* This function should be invoked when the preview frame has finished loading its content.
       It is responsible for updating the height of frame body to the actual content's height.
      */
      previewFrameOnload: function (body, iframe) {
          AJS.Editor.disableFrame(body);
          var $iframe = AJS.$(iframe || "#previewArea iframe"),
              prevHeight = 0,
              counter = 0,
              content = AJS.$("#main", body)[0],
              originalHeight = $iframe.height();

          content && (function () {
              var height = content.scrollHeight;
              if (prevHeight != height) {
                  if (height != $iframe.height()) {
                      $iframe.height(Math.max(height, originalHeight)); // never make it smaller than the default height
                  }
                  prevHeight = height;
                  counter = 0;
              } else {
                  counter++;
              }
              // uppper limit check for content height changes
              if (counter < 500) {
                  setTimeout(arguments.callee, 500);
              }
          })();
      },

    showRichText : function (show) {
        if (!AJS.params.useWysiwyg)
            return;

        AJS.setVisible("#wysiwyg", show);
        AJS.setCurrent("#wysiwygTab", show);

        if (show) {
            this.Adapter.onShowEditor();
            // now we are in rich text mode, and may change the content, so any value in lastKnownGoodContent is obsolete
            this.lastKnownGoodContent = null;
            AJS.$("#main").addClass("active-richtext");
        }
        else {
            this.Adapter.onHideEditor();
            AJS.$("#main").removeClass("active-richtext");
        }
    },

    showMarkup: function (show) {
        var form = this.getCurrentForm(),
            fname1 = (show ? "removeClass" : "addClass"),
            fname2 = (show ? "addClass" : "removeClass");
        AJS.$("#markup")[fname1]("hidden");
        AJS.$("#markupTab")[fname2]("current");
        AJS.$("#sidebar")[fname1]("hidden");
        AJS.$("#addcomment-sidebar")[fname1]("hidden");
        AJS.$(form)[fname2]("markup");
        AJS.$("#linkinserters")[fname1]("hidden");
        AJS.$("#main")[fname2]("active-wikimarkup");
    },

    showPreview : function (show) {
        var fname1 = (show ? "removeClass" : "addClass"),
            fname2 = (show ? "addClass" : "removeClass");
        AJS.$("#preview")[fname1]("hidden");
        AJS.$("#previewTab")[fname2]("current");
        AJS.$("#main")[fname2]("active-preview");
    },

    /**
    * Set up the page for rich text or markup editing
    */
    setMode : function(mode) {
        var wasRichText = this.inRichTextMode();
        var form = this.getCurrentForm();

        if (mode != AJS.params.actionPreview) {
            AJS.$("input[name=xhtml]", this.getCurrentForm()).val(mode == AJS.params.actionRichtext);
        }

        if (AJS.params.remoteUser && AJS.params.useWysiwyg) {
            this.showDefaultEditorLinks(mode);
        }

        // DON'T CHANGE THE ORDERING OF SHOWS
        // FIREFOX RENDERING GLITCHES WHEN PAGE LOADS TOO QUICKLY (if showMarkup() isn't first)
        if (mode == AJS.params.actionRichtext) {
            this.showMarkup(false);
            this.showRichText(true);
            this.showPreview(false);
        }
        else if (mode == AJS.params.actionMarkup) {
            this.showMarkup(true);
            this.showRichText(false);
            this.showPreview(false);

            // CONF-18837. IE8 needs px size to avoid textarea scrolling-on-selection bug
            if ($.browser.msie && $.browser.version.charAt() == 8) {
                var wikiMarkupElement = AJS.$("#markup");
                AJS.$("#markupTextarea").width(wikiMarkupElement.width()).height(wikiMarkupElement.height());
            }
        } else if (mode == AJS.params.actionPreview) {
            if (wasRichText) {
                // get the editor content in case we come back to wiki-markup
                this.lastKnownGoodContent = this.Adapter.getEditorHTML();
            }
            this.showPreview(true);
            this.showRichText(false);
            this.showMarkup(false);
        }

        AJS.$("input[name=mode]", form).val(mode);
    },

    /**
     * Returns the ID of the appropriate content object to use when rendering the editor's content.
     * For pages, blogs, existing comments or drafts it is the ID of that object.
     * For new comments it is the ID of the page or blog to which the comment belongs.
     */
    getContentId : function() {
        if (+AJS.params.contentId)
            return AJS.params.contentId;
        if (+AJS.params.pageId)
            return AJS.params.pageId;
        return "0"; // ensure we always return "0" or an actual id.
    },

    changeMode : function(newMode) {

        //## allowModeChange() only exists when WYSIWYG is enabled, so don't do a check otherwise (CONF-4935)
        // if the editor is in a state where the mode chnage will break things (e.g. not yet fully initialised)
        // don't allow the change
        if (AJS.params.useWysiwyg && this.inRichTextMode() && !AJS.Editor.Adapter.allowModeChange()) {
            return false;
        }

        var oldMode = AJS.$("input[name=mode]", this.getCurrentForm()).val();
        if (oldMode == newMode) {
            return false;
        }

        this.showWaitImage(true);

        if (AJS.params.saveDrafts) {
            // If the contentId is "0" we want to make sure we
            // save the draft before loading the content (by attempting to force it to run synchronously).
            var async = (AJS.params.contentId === "0" ? false : true);
            this.saveDraft(async);
        }

        var contentId = this.getContentId();
        if (newMode == AJS.params.actionMarkup) {
            if (oldMode == AJS.params.actionPreview) {
                if (AJS.Editor.lastEditMode == AJS.params.actionMarkup) { // Markup -> Preview -> Markup (no conversion)
                    this.replysetTextArea(null);
                }
                else { // WYSIWYG -> Preview -> Markup (convert HTML to wiki markup)
                    AJS.safe.post(AJS.params.contextPath + "/json/convertxhtmltowikimarkupwithoutpage.action", {pageId: contentId, xhtml: AJS.Editor.lastKnownGoodContent}, this.replysetTextArea, "json");
                }
            }
            else { // WYSIWYG -> Markup, so just convert
                AJS.safe.post(AJS.params.contextPath + "/json/convertxhtmltowikimarkupwithoutpage.action", {pageId: contentId, xhtml: AJS.Editor.Adapter.getEditorHTML()}, this.replysetTextArea, "json");
            }
        }
        else if (newMode == AJS.params.actionRichtext) {
            // If the current mode is preview...
            if (oldMode == AJS.params.actionPreview && AJS.Editor.lastEditMode == AJS.params.actionRichtext) {
                // WYSIWYG -> Preview -> WYSIWYG
                // We don't need to reload or convert the contents of the tinyMCE editor
                this.replysetEditorValue(null);
            } else {
                // Markup -> Preview -> WYSIWYG
                // Convert the markup to be used with WYSIWYG
                // Markup -> WYSIWYG, so just grab the contents of the markup textarea and convert it to be used with WYSIWYG
                AJS.safe.post(AJS.params.contextPath + "/json/convertwikimarkuptoxhtmlwithoutpagewithspacekey.action", {pageId: contentId, spaceKey: AJS.params.spaceKey, wikiMarkup: AJS.$("#markupTextarea").val()}, this.replysetEditorValue, "json");
            }
        }
        else { // Preview
            var queryParams = { "contentId": contentId,
                                "contentType": AJS.params.contentType,
                                "spaceKey": AJS.params.spaceKey };

            if (oldMode == AJS.params.actionRichtext) { // WYSIWYG -> Preview
                AJS.Editor.lastEditMode = AJS.params.actionRichtext;
                AJS.Editor.lastKnownGoodContent = queryParams.xHtml = AJS.Editor.Adapter.getEditorHTML();
            }
            else { // Markup -> Preview
                AJS.Editor.lastEditMode = AJS.params.actionMarkup;
                queryParams.wikiMarkup = AJS.$("#markupTextarea").val();
            }
            AJS.$.post(AJS.params.contextPath + "/pages/rendercontent.action", queryParams, AJS.Editor.replysetPreviewArea);
        }

        return false;
    },

    showWaitImage : function (flag) {
        AJS.$("#wysiwygWaitImage").css("visibility", (flag ? "visible" : "hidden"));
    },

    replysetTextArea : function (s) {
        if (s != null) {
            AJS.$("#markupTextarea").val(s);
            if (AJS.params.saveDrafts)
            {
                AJS.Editor.originalWikiContent = s;
            }
        }
        AJS.Editor.setMode(AJS.params.actionMarkup);
        AJS.Editor.showWaitImage(false);
    },

    replysetEditorValue : function (s) {
        AJS.Editor.showWaitImage(false);
        AJS.Editor.setMode(AJS.params.actionRichtext);
        AJS.Editor.Adapter.setEditorValue(s);
    },

    replysetPreviewArea : function (html) {
        AJS.Editor.showWaitImage(false);
        AJS.Editor.setMode(AJS.params.actionPreview);
        // Set the iframe source to an empty JS statement to avoid secure/nonsecure warnings on https, without
        // needing a back-end call.
        var src = AJS.params.staticResourceUrlPrefix + "/blank.html";
        AJS.$("#previewArea").html('<iframe src="' + src + '" scrolling="no" frameborder="0"></iframe>');
        var iframe = AJS.$("#previewArea iframe")[0];
        var doc = iframe.contentDocument || iframe.contentWindow.document;
        doc.write(html);
        doc.close(); // for firefox
    },

    inRichTextMode : function () {
        return AJS.$("input[name=mode]", this.getCurrentForm()).val() == AJS.params.actionRichtext;
    },

    // Called by Adapter oninit
    onInit : function () {
        AJS.Editor.setMode(AJS.params.editorMode);
    },

    handleUnload : function() {
        if (AJS.Editor.isUnloaded) {
            return;
        }

        AJS.Editor.isUnloaded = true;
        if (AJS.params.saveDrafts) {
            AJS.Editor.saveDraft(false);
        }
    },

    /**
     * Returns a string which represents the message to display when a user navigates away from editing a page.
     */
    handleBeforeUnload: function() {
        if (typeof seleniumAlert != "undefined") { // TODO: Find a better way to detect Selenium.
            return;
        }

        // You can't rely on the draft being saved before this.
        if (AJS.Editor.hasContentChanged()) {
            if (AJS.params.saveDrafts) {
                return AJS.I18n.getText("saved.draft");
            }

            return AJS.I18n.getText("unsaved.comment.lost");
        }
        else if (AJS.Editor.isDraftSaved) {
            return AJS.I18n.getText("saved.draft");
        }
    },

    storeTextareaBits: function (doNotFocus) {
        return AJS.Editor.Markup.storeTextareaBits(this.getCurrentForm(), AJS.$("#markupTextarea")[0], doNotFocus);
    },

    setRichTextDefault : function (value) {
        AJS.safe.post(AJS.params.contextPath + "/json/setpreferenceusereditwysiwyg.action", {useWysiwyg: value}, function () {}, "json");
        AJS.Editor.editorPreference = (value ? AJS.params.actionRichtext : AJS.params.actionMarkup);
        AJS.$("#makeRichTextDefault").addClass("hidden");
        AJS.$("#makeMarkupDefault").addClass("hidden");
    },

    // Hide and show the "make default" editor links, based on what mode the user is currently in
    showDefaultEditorLinks : function (currentMode) {
        var defaultIsWysiwyg = (AJS.Editor.editorPreference == AJS.params.actionRichtext);
        var showRichTextDefault, showMarkupDefault = false;

        // If we are in MARKUP mode, show the text to set markup as default
        if (defaultIsWysiwyg && currentMode == AJS.params.actionMarkup) {
            showMarkupDefault = true;
        }
        // If we are in RICHTEXT mode, show the text to set richtext as default
        else if (!defaultIsWysiwyg && currentMode == AJS.params.actionRichtext) {
            showRichTextDefault = true;
        }

        AJS.$("#makeRichTextDefault")[showRichTextDefault ? "removeClass" : "addClass"]("hidden");
        AJS.$("#makeMarkupDefault")[showMarkupDefault ? "removeClass" : "addClass"]("hidden");
    },

    contentChangeHandler : function () {
        this.contentHasChangedSinceLastAutoSave = true;
    },

    getCurrentForm : function() {
        return AJS.$("form[name=" + AJS.params.formName + "]")[0];
    },

    openMacroBrowser : function(e) {
        var t = AJS.Editor,
            mb = AJS.MacroBrowser,
            textarea = $("#markupTextarea");

        // store the current selection & scroll for later when we insert macro
        var range = t.Markup.selection = textarea.selectionRange();
        t.Markup.scrollTop = textarea.scrollTop();

        var selectedMacro = mb.getSelectedMacro(range.textBefore, textarea.val());
        mb.open({
            markupMode : true,
            selectedMacro : selectedMacro,
            selectedMarkup : range.text,
            onComplete : AJS.Editor.macroBrowserComplete,
            onCancel : AJS.Editor.macroBrowserCancel
        });
        return AJS.stopEvent(e);
    },

    // Constructs and inserts the macro markup from the insert macro page.
    macroBrowserComplete : function(macro) {
        var t = AJS.Editor,
            textarea = $("#markupTextarea"),
            m = AJS.MacroBrowser.settings.selectedMacro;
        if (m) { // select and replace the current macro markup
            textarea.selectionRange(m.startIndex, m.startIndex + m.markup.length);
        }
        else if (t.Markup.selection) {
            textarea.selectionRange(t.Markup.selection.start, t.Markup.selection.end);
        }
        textarea.selection(macro.markup);
        textarea.scrollTop(t.Markup.scrollTop);
    },
    macroBrowserCancel : function() {
        var t = AJS.Editor,
            textarea = $("#markupTextarea");
        if (t.Markup.selection) {
            textarea.selectionRange(t.Markup.selection.start, t.Markup.selection.end);
        }
        textarea.scrollTop(t.Markup.scrollTop);
    }
};})(AJS.$);

AJS.toInit(function ($) {

    AJS.Editor.editorPreference = AJS.params.editorMode;

    $("#wysiwygTab a:first").click(function (e) {
        AJS.Editor.changeMode(AJS.params.actionRichtext);
        e.preventDefault();
    });

    $("#markupTab a:first").click(function (e) {
        AJS.Editor.changeMode(AJS.params.actionMarkup);
        e.preventDefault();
    });

    $("#previewTab a:first").click(function (e) {
        AJS.Editor.changeMode(AJS.params.actionPreview);
        e.preventDefault();
    });

    $("#makeRichTextDefault").click(function (e) {
        AJS.Editor.setRichTextDefault(true);
        e.preventDefault();
    });

    $("#makeMarkupDefault").click(function (e) {
        AJS.Editor.setRichTextDefault(false);
        e.preventDefault();
    });

    $("#editor-insert-macro").click(AJS.Editor.openMacroBrowser);

    $("#markupTextarea").select(function () {
        AJS.Editor.storeTextareaBits(true);
    }).keyup(function (e) {
        AJS.Editor.contentChangeHandler();

        if (e.ctrlKey) {
            if (e.keyCode == 77) {// bind ctrl+m to insert image
                $("#editor-insert-image").click();
                return false;
            }
            if (e.shiftKey && e.keyCode == 65) { // bind ctrl+shift+a to insert macro
                $("#editor-insert-macro").click();
                return false;
            }
        }
    }).change(function () {
        AJS.Editor.contentChangeHandler();
    });

    $(".submit-buttons").click(function (e) {
        AJS.Editor.contentFormSubmit(e);
    });

    $(".editor-template-link").click(function (e) {
        var form = AJS.$("#createpageform")[0];

        if ((AJS.Editor.hasContentChanged() || AJS.Editor.isDraftSaved) && !confirm(AJS.I18n.getText("template.will.overwrite.changes"))) {
            return;
        }

        form.action = "createpage-choosetemplate.action";
        AJS.Editor.contentFormSubmit(e);
        form.submit();
    });

    if (AJS.params.useWysiwyg) {
        var errorHandler = function(message) {
            AJS.Editor.showWaitImage(false);
            // Ignore DWR errors because they almost always occur when users
            // click a link or submit during draft/heartbeat transmission.
            // Displaying a message when this occurs is just annoying.
        };
        // Initialisation
        // We should note here that the content has NOT finished loading
        AJS.Editor.Adapter.addOnInitCallback(AJS.Editor.onInit);
        AJS.Editor.Adapter.editorOnLoad();
    }

    // bind the function to be run when the preview frame is loaded
    $(window).bind("render-content-loaded", function(e, body) {
        var iframe = $("#previewArea iframe");
        if (iframe.contents().find("body")[0] == body) {
            AJS.Editor.previewFrameOnload(body, iframe);
        }
    });

    window.onbeforeunload = function() {
        return AJS.Editor.handleBeforeUnload();
    };

    if (AJS.params.saveDrafts) {
        $(window).unload(AJS.Editor.handleUnload);
        $.getJSON(AJS.params.contextPath + "/json/getdraftsaveinterval.action", {}, function (interval) {
                setInterval(AJS.Editor.saveDraft, interval);
        });
    }

    if (AJS.params.heartbeat) {
        AJS.Editor.heartbeat();
        $.getJSON(AJS.params.contextPath + "/json/getheartbeatinterval.action", {}, function (interval) {
            setInterval(AJS.Editor.heartbeat, interval);
        });
    }

    // Move title field to place of title text
    var titleText = $("#title-text");
    var titleField = $("#content-title");
    var titleLabel = $("#content-title-label");
    if (titleText.length && titleField.length) { //only true for edit page screen in default theme
        var div = document.createElement("div");
        $(div).addClass("editable-title")
              .append(titleLabel)
              .append(titleField);
        if (!$.browser.msie) { // IE can't use full width due to CSS bugs
            $(window).load(function () { // wait until images are loaded
                var logo = $("#title-heading img.logo");
                if (logo.length && logo.css("display") != "none") {
                    $(div).css("marginLeft", $("#title-heading img.logo").width() + 10 + "px"); // adjust for custom logos
                }
                else {
                    $(div).css("marginLeft", 0);
                }
            });
        }
        titleText.replaceWith(div);

        // Hidden field title will exist for pages created from links.
        var hiddenFields = $("#hidden-content-title");
        if (!hiddenFields.length) {
            var hiddenField = document.createElement("input");
            hiddenField.id = "hidden-content-title";
            hiddenField.type = "hidden";
            hiddenField.name = "title";
            hiddenField = $(hiddenField);

            var titleWrittenField = $("#titleWritten");
            if (!titleWrittenField.length || titleWrittenField.val() != "false") {
                hiddenField.val(titleField.val());
            }

            var editorDiv = $("#wiki-editor");
            editorDiv.before(hiddenField);
        }
    }

    AJS.Editor.originalWikiContent = AJS.Editor.getCurrentFormContent();
});
