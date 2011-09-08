AJS.Labels = (function($) { return {

    // Variable to enforce one label operation at a time
    operationInProgress: false,

    // Updates and displays the status message if any. Use empty string to clear it out.
    updateStatus: function(statusMessage) {
        $("#labelOperationStatus").html(statusMessage);
        if(statusMessage != "")
            $("#waitImageAndStatus").addClass("open");
        else
            $("#waitImageAndStatus").removeClass("open");
    },
    // Use before any new label operation calls. It clears out all previous error messages and updates the status.
    startOperation: function(statusMessage) {
        AJS.Labels.operationInProgress = true;
        $("#errorSpan").html("");
        AJS.Labels.labelOperationError("");
        AJS.Labels.updateStatus(statusMessage);
    },
    // User after any label operation calls have finished. It clears out the status message.
    finishOperation: function() {
        AJS.Labels.updateStatus("");
        AJS.Labels.operationInProgress = false;
    },
    // Updates and displays an error message. Mainly for server and dwr errors.
    handleError: function (htmlMessage) {
        AJS.Labels.operationInProgress = false;
        AJS.Labels.updateStatus("");
        $("#errorSpan").html(htmlMessage);
    },
    // Updates and displays label operation error messages. Mainly for errors when ajax response is not success.
    labelOperationError: function(htmlMessage) {
        $("#labelOperationErrorMessage").html(htmlMessage);
        if(htmlMessage != "")
            AJS.setVisible("#labelOperationErrorContainer", true);
        else
            AJS.setVisible("#labelOperationErrorContainer", false);
    },
    // Adds the labels present in the input field.
    addLabelFromInput: function() {
        return AJS.Labels.addLabel($("#labelsString").val());
    },
    // Adds the given label to the current page
    addLabel: function(label) {
        if ($("#labelsString").val() != "")
            AJS.setVisible("#labels-section-title-none", false);
        if (!AJS.Labels.operationInProgress && label && label != "") {
            AJS.Labels.startOperation("Adding label...");
            var data = {
                entityIdString: AJS.params.pageId,
                labelString: label
            };
            AJS.safe.ajax({
                type: "POST",
                url: AJS.params.contextPath + "/json/addlabelactivity.action",
                data: data,
                success: AJS.Labels.addLabelCallback,
                error: AJS.Labels.addLabelErrorHandler,
                dataType: "json"
            })
        }
        return false;
    },
    addLabelCallback: function (response) {
        if (response.success) {
            $("#labelsList").html($("#labelsList").html() + response.response);
            // rebind the remove links for the newly added labels
            $(".labels-editor .remove-label").unbind('click');
            $(".labels-editor .remove-label").click(AJS.Labels.removeLabel);
            $("#labelsString").val("");
        }
        else {
            AJS.Labels.labelOperationError(response.response);
        }
        // clear the text box and focus on it should the user want to add another label
        $("#labelsString").focus();
        AJS.safe.ajax({
            url: AJS.params.contextPath + "/json/suggestlabelsactivity.action",
            data: {entityIdString: AJS.params.pageId},
            success: AJS.Labels.suggestedLabelsCallback,
            error: AJS.Labels.suggestedLabelsErrorHandler,
            dataType: "json"
        });
        AJS.Labels.finishOperation();
    },
    addLabelErrorHandler: function () {
        AJS.Labels.handleError("[41a] Error connecting to the server. The labels have not been updated.");
    },
    removeLabel: function () {
        if (!AJS.Labels.operationInProgress) {
            AJS.Labels.startOperation('Removing label ...');
            var labelId = AJS.$(this).parent().attr("id").replace(/^label-/, "");
            var data = {
                entityIdString: AJS.params.pageId,
                labelIdString: labelId
            };
            AJS.safe.ajax({
                type: "POST",
                url: AJS.params.contextPath + "/json/removelabelactivity.action",
                data: data,
                success: AJS.Labels.removeLabelCallback(labelId),
                error: AJS.Labels.removeLabelErrorHandler,
                dataType: "json"
            });
        }
        return false;
    },
    removeLabelCallback : function(labelId) {
        return function(response) {
            if (response.success) {
                var label = $("#label-" + labelId);
                label.fadeOut("normal", function () {
                    label.remove();
                    if($("#labelsList").children().length == 0) 
                        AJS.setVisible("#labels-section-title-none", true);
                });
            }
            else {
                AJS.Labels.labelOperationError(response.response);
            }
            AJS.Labels.finishOperation();
        };
    },
    removeLabelErrorHandler: function (response) {
        var message = "Error connecting to the server. The labels have not been updated";
        if(response) message += ": " + response;

        AJS.Labels.handleError(message);
    },
    suggestedLabelsCallback: function (response) {
        if (!response.success) return;
        $("#suggestedLabelsSpan").html(response.response);
        $("#suggestedLabelsSpan .suggested-label").click(function (e) {
            var val = $('#labelsString').val();
            if (val.length > 0) val += " ";
            val += $(this).text();
            $('#labelsString').val(val);
            var toRemove = this;
            if ($(this).parent().find("a").length == 1) { // if we're the last suggestion
                toRemove = $(this).parent();
            }
            $(toRemove).fadeOut(function () { $(this).remove(); });
            e.preventDefault();
        });
    },
    suggestedLabelsErrorHandler: function(response) {
        var message = "Error finding suggested labels. The labels have not been updated";
        if(response) message += ": " + response;
        AJS.Labels.handleError(message);
    },
    // Binds the autocomplete labels ajax call to the labels input field.
    // Labels are added on select of the autocomplete drop down if the input field is within a form.
    bindAutocomplete: function() {
        var labelInput = $("#labelsString"), addLabelOnSelect = labelInput.parents("#add-labels-form").length;
        if (!labelInput.length) return;

        var dropDownPlacement = function (dropDown) {
            $("#labelsAutocompleteList").append(dropDown);
        };
        var onselect = function (selection) {
            if (selection.find("a.label-suggestion").length) {

                var span = $("span", selection);
                var contentProps = $.data(span[0], "properties");

                if(addLabelOnSelect) {
                    AJS.Labels.addLabel(contentProps.name);
                }
                else { // on the edit page, we don't add labels but update the text field
                       // this hacky code was copied from uberlabels.js
                    var value = labelInput.val();
                    var tokens = AJS.Labels.queryTokens;
                    var last_token_pos = -1, token = "";
                    for (var i = 0; i < tokens.length; i++) {
                        token = tokens[i];
                        var this_token_pos = value.lastIndexOf(token);
                        if (this_token_pos > last_token_pos)
                            last_token_pos = this_token_pos;
                    }
                    if (last_token_pos != -1) {
                        var new_value = value.substr(0, last_token_pos);
                        var whitespace = value.substr(last_token_pos + token.length).match(/^\s+/);
                        if (whitespace)
                            new_value += whitespace[0];
                        labelInput.val(new_value + contentProps.name);
                    }
                    else {
                        labelInput.val(contentProps.name);
                    }
                }
            }
        };
        var onshow = function() {
            if (!$("#labelsAutocompleteList .label-suggestion").length) {
                this.hide();
            }
            else if(!addLabelOnSelect) {
                // remove hrefs if we're not going to add the label on select
                var labels = $("#labelsAutocompleteList a.label-suggestion");
                for(var i=0,ii=labels.length; i<ii; i++) {
                    labels.get(i).href = "#";
                }
            }
        };
        var url = "/labels/autocompletelabel.action";
        $(window).bind("quicksearch.ajax-success", function(e, data) {
            if (data.url == url) {
                AJS.Labels.queryTokens = (data.json && data.json.queryTokens) || [];
                return false;
            }
        });
        $(window).bind("quicksearch.ajax-error", function(e, data) {
            if (data.url == url) {
                AJS.Labels.queryTokens = [];
                return false;
            }
        });
        labelInput.quicksearch(url, onshow, {
            makeParams: function(val) {
                return {
                    query: val,
                    contentId: AJS.params.pageId || ""
                };
            },
            dropdownPlacement : dropDownPlacement,
            ajsDropDownOptions : {
                selectionHandler: function (e, selection) {
                        onselect(selection);
                        this.hide();
                        e.preventDefault();
                    }
                }
        });
    },
    TagExtractor: function(tagPrefix, extractFromUrls)
    {
        this.tagPrefix = tagPrefix;
        this.extractFromUrls = extractFromUrls;
        this.TAG_END = " ";

        this.parseTagsTo = function(content, tagHandler)
        {
            //simple backtracking implementation
            var isInUrl = function(tagStart, content)
            {
                var currentPos = tagStart;

                while (currentPos > 0)
                {
                    switch (content[currentPos])
                    {
                        case '/':
                        case '&':
                            return true;
                        case ' ':
                            return false;
                        default:
                            currentPos--;
                    }
                }

                return false;
            };

            var index = 0;

            while (index < content.length)
            {
                var tagStart = content.indexOf(this.tagPrefix, index);

                if (tagStart == -1) //we've hit the end
                    return;

                if (!this.extractFromUrls && isInUrl(tagStart, content))
                {
                    index = tagStart + 1;
                }
                else
                {
                    var tagContentStart = tagStart + this.tagPrefix.length;
                    var tagEnd = content.indexOf(this.TAG_END, tagContentStart);
                    var nextTagStart = content.indexOf(this.tagPrefix, tagContentStart);

                    if (nextTagStart != -1 && (tagEnd == -1 || nextTagStart < tagEnd))
                        tagEnd = nextTagStart;

                    if (tagEnd == -1) //content ends with a tag
                        tagEnd = content.length;

                    if (tagEnd != tagStart + this.tagPrefix.length) //ignore zero-length tags
                        tagHandler(content.substring(tagContentStart, tagEnd), tagContentStart, tagEnd);

                    index = tagEnd;
                }
            }
        }
    }
};})(AJS.$);

AJS.toInit(function () {
    AJS.Labels.bindAutocomplete();
});
