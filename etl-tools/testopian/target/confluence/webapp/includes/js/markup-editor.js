AJS.Editor.Markup = {
    
    // This function stores the selected and unselected text for the textarea in hidden fields on the form
    // This should be called before insertOrUpdateText.
    storeTextareaBits : function (currentForm, textAreaObject, doNotFocus) {
        if (textAreaObject.selectionStart != null) {
            // for netscape, mozilla, gecko
            textAreaObject.sel = textAreaObject.value.substr(textAreaObject.selectionStart, textAreaObject.selectionEnd - textAreaObject.selectionStart);
            textAreaObject.sel1 = textAreaObject.value.substr(0, textAreaObject.selectionStart);
            textAreaObject.sel2 = textAreaObject.value.substr(textAreaObject.selectionEnd);
            currentForm.selectedText.value = textAreaObject.sel;
        }
        else {
            if (document.selection && document.selection.createRange) {
                // for ie
                try {
                    // Focus the textarea so IE will get the correct selection range.
                    !doNotFocus && currentForm.elements[AJS.params.parametersName].focus();
                }
                catch (e) {
                    // ignore
                }
                var ieRange = document.selection.createRange();
                textAreaObject.caretPos = ieRange.duplicate();
                currentForm.selectedText.value = ieRange.text;
            }
        }

        return currentForm.selectedText.value;
    },

    // Inserts text into the text area specified (currently compatible with Netscape, mozilla, ie)
    // Note: for IE compatibility, storeCaret(this) must be called in the onclick, onselect and onkeyup events
    // of the text area object specified
    insertOrUpdateText: function (text, textAreaObject) {
        if (window.getSelection && textAreaObject.selectionStart && textAreaObject.selectionStart != null) {
            textAreaObject.value = textAreaObject.sel1 + text + textAreaObject.sel2;
            textAreaObject.focus();
            textAreaObject.selectionStart = textAreaObject.selectionEnd = textAreaObject.sel1.length + text.length;
        } else if (textAreaObject.createTextRange && textAreaObject.caretPos) {
            // for IE
            // IE supports createTextRange(), test for non-null caretPos (if its been set)
            var caretPos = textAreaObject.caretPos;
            caretPos.text = caretPos.text.charAt(caretPos.text.length - 1) == ' ' ? text + ' ' : text;
        } else {
            // for ie users that don't set the caret OR
            // for other browsers
            // just append link at the end of the current text inside the text area
            textAreaObject.value += text;
        }
    }
};