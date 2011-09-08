// todo remove this??
function placeFocus() {}

AJS.toInit( function () {
    var titleField = AJS.$("#content-title");
    var titleWritten = AJS.$("#titleWritten");
    if (titleField.val() == "") {
        titleField.addClass("newpagetitle");
        titleField.val(AJS.params.defaultContentTitle);

        titleWritten.val("false");
    }

    titleField.focus(function () {
        var $this = AJS.$(this);
        if (titleWritten.val() == "false") {
            $this.val("");
            $this.removeClass("newpagetitle");
        }
        return true;
    }).blur(function () {
        var $this = AJS.$(this);
        if ($this.val() == "") {
            $this.addClass("newpagetitle");
            $this.val(AJS.params.defaultContentTitle);
            titleWritten.val("false");
        }
        else {
            titleWritten.val("true");
        }
        return true;
    });
});
