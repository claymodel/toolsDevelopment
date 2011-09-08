/**
 * Controls the Form component of the Page Permissions dialog.
 */
AJS.PagePermissions.Controls = function (permissionManager) {
    var $ = AJS.$;

    /**
     * Adds validation error messages for unknown or duplicate names.
     */
    var validator = {

        handleNonExistentEntityNames : function (entityNames) {
            if (!entityNames || !entityNames.length)
                return;

            var commaDelimitedNames = entityNames.join(", ");

            var errorMsg = AJS.I18n.getText("page.perms.error.invalid.entity.names") + " " + commaDelimitedNames;
            $("#page-permissions-error-div").find("div").text(errorMsg).end().removeClass("hidden");
            permissionManager.refreshLayout();
        },

        isDuplicateEntityForType : function (entity, permissionType) {
            var matches = $("#page-permissions-table ." + permissionType + "-permission-row .permission-entity-name").filter(function() {
                return $(this).text() == entity.name;
            });

            return matches.length > 0;
        },

        // TODO - currently unused because duplicated entity row is now highlighted. If design sticks, remove this method.
//        handleDuplicateEntityName : function(entityName) {
//            this.duplicateNames.push(entityName);
//            var commaDelimitedNames = this.duplicateNames.join(", ");
//            this.validationErrors["duplicateNames"] = AJS.params["page-perms-duplicate-names"] + " " + commaDelimitedNames;
//
//            this.updateAndShowValidationErrors();
//        },

        resetValidationErrors : function () {
            $("#page-permissions-error-div").addClass("hidden");
            permissionManager.refreshLayout();
        }
    };

    /**
     * Handles typing of user/names and groups with autocomplete and placeholder text.
     */
    var nameField = (function() {
        var input = $("#page-permissions-names-input");
        var autocompleted = $("#page-permissions-names-hidden");

        // The placeholder will be set as the initial value of the input.
        var placeholder = input.val();
        var placeholderClass = "input-placeholder";

        input.keypress(function (e) {
            if (e.keyCode == Event.KEY_RETURN) {
                namesEntered();
                input.focus();
                return false;
            }
            return true;
        });

        // TODO(dtaylor) Merge this with username from macro-browser-fields.js when time allows.
        input.quicksearch("/json/contentnamesearch.action?type=userinfo", null,
            {
                dropdownPostprocess : function (list) {
                    // remove the "search for" at the bottom of the list
                    $("ol.last", list).remove();

                    // check if there are items in the drop down. If none then display a
                    // message telling the user this
                    if ($("ol", list).length == 0) {
                        list.append(AJS.renderTemplate("permissions-username-no-suggestion-template"));
                    }
                },
                dropdownPlacement : function (dropDown) {
                    var placer = AJS("div");
                    placer.addClass("page-perms-name-dropdown-wrapper aui-dd-parent");
                    placer.append(dropDown);
                    input.after(placer);
                    // dropDownEscapeHandler(input, dropDown);
                },
                ajsDropDownOptions : {
                    selectionHandler: function (e, selection) {
                        // if the user selected the "no matches" message then do nothing
                        if (selection.hasClass("message")) return;

                        var contentProps = $.data(selection.find("span")[0], "properties");
                        var username = contentProps.href.substr(contentProps.href.lastIndexOf("/") + 2);
                        autocompleted.val(unescape(username.replace(/\+/g, " ")));
                        input.val("");
                        namesEntered();
                        this.hide();
                        e.preventDefault();
                    }
               }
            }
        );
        input.focus(function() {
            if (input.hasClass(placeholderClass)) {
                input.removeClass(placeholderClass).val("");
            }
            var ol = input.next(".aui-dd-parent");
            if (!ol.length) {
                return;
            }
            // Reset the position of the autocomplete list each time the input gets focus. This allows for the window
            // being resized (and for the input being hidden when the position is originally calculated).            
            ol.show();
            var expectedLeftOffset = input.offset().left;
            if (ol.offset().left != expectedLeftOffset) {
                ol.css("margin-left", 0);       // "reset" the offset.
                var olMarginLeft = expectedLeftOffset - ol.offset().left;
                ol.css("margin-left", olMarginLeft + "px");
            }
            var expectedTopOffset = input.offset().top + input.outerHeight();
            if (ol.offset().top != expectedTopOffset) {
                ol.css("margin-top", 0);       // "reset" the offset.
                var olMarginTop = expectedTopOffset - ol.offset().top;
                ol.css("margin-top", olMarginTop + "px");
            }
            ol.css({
                "width" : input.outerWidth()
            });
            ol.hide();
        });
        input.blur(function() {
            if (input.val() == "") {
                input.addClass(placeholderClass).val(placeholder);
            }
        });
        return {
            getValue : function() {
                var names = autocompleted.val();
                if (names) {
                    autocompleted.val("");
                } else {
                    names = input.val();
                    if (names == placeholder) {
                        names = "";
                    }
                }
                return names;
            },

            addPlaceholder : function() {
                input.addClass(placeholderClass).val(placeholder);
            },

            /**
             * Removes a name from the input field (called after the name is found at the back end)
             */
            removeFromNameInput : function (nameToRemove) {
                if (!nameToRemove)
                    return;

                var value = input.val();
                if (!value)
                    return;

                var entityNames = value.split(",");
                for (var i = 0; i < entityNames.length; i++) {
                    entityNames[i] = $.trim(entityNames[i]);
                }

                // remove all empty strings and the entity name that's just been added
                entityNames = $.grep(entityNames, function (name) {
                    return name != "" && name != nameToRemove;
                });

                if (entityNames.length) {
                    input.val(entityNames.join(", "));
                } else {
                    if (document.activeElement == input[0]) {
                        input.val("");
                    } else {
                        this.addPlaceholder();
                    }
                }
            }
        };
    })();

    /**
     * Called when the user hits Enter or clicks the Add button.
     */
    var namesEntered = function () {
        validator.resetValidationErrors();
        permissionManager.table.clearHighlight();
        var names = nameField.getValue();
        if (!names)
            return;

        permissionManager.addNames(names);
    };

    // Choose Me button (User and Group button are wired with VM component
    $("#page-permissions-choose-me").click(function(e) {
        validator.resetValidationErrors();
        permissionManager.addNames($(this).find(".remote-user-name").text());
        return AJS.stopEvent(e);
    });

    $("#permissions-error-div-close").click(function(e) {
        validator.resetValidationErrors();
        return AJS.stopEvent(e);
    });

    // Typed user list submit
    $("#add-typed-names").click(namesEntered);

    return {
        validator: validator,
        
        nameField: nameField,

        setVisible : function (show) {
            AJS.setVisible("#page-permissions-editor-form", show);
            AJS.setVisible(".remove-permission-link", show);

            // Set the user/group name field placeholder
            if (show) nameField.addPlaceholder();
        },

        isShowing : function() {
            return !$("#page-permissions-editor-form").hasClass("hidden");
        },

        getPermissionType : function() {
            return !!$("#restrictViewRadio:checked").length ? "view" : "edit";
        }
    };
};

