AJS.toInit(function ($) {
    var toggleLabels = function (e) {
        $('#labels_div').toggleClass("hidden");
        $("#labels_info").toggleClass("hidden");

        if ($('#labels_div').hasClass("hidden")) {
            $("#labels_info").html($("#labelsString").val().toLowerCase());
            $("#labels_edit_link").html(AJS.I18n.getText("edit.name"));
        }
        else {
            AJS.safe.ajax({
                url: AJS.Data.get("context-path") + "/json/suggestlabelsactivity.action",
                data: {entityIdString: AJS.params.pageId},
                success: AJS.Labels.suggestedLabelsCallback,
                error: AJS.Labels.suggestedLabelsErrorHandler,
                dataType: "json"
            });
            $("#labels_edit_link").html(AJS.I18n.getText("done.name"));
        }

        if (e) e.preventDefault();
    };

    var labelsShowing = $("#labelsShowing");
    if (labelsShowing.length && labelsShowing.val() == "true") {
        toggleLabels();
    }

    $("#labels_edit_link").click(toggleLabels);
});

