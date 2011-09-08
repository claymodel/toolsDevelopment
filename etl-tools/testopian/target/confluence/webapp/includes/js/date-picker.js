AJS.toInit(function($) {

    /**
     * The calendar interationalisation script (e.g. calendar-en.js) must load after the calendar.js resource
     * but before the Calendar setup.  We load the internationalisation script dynamically here and call setup
     * in the callback.
     */
    var calendarI18nScriptUrl = AJS.params.staticResourceUrlPrefix + "/includes/js/calendar/lang/" + AJS.params.calendarI18nFile;

    $.getScript(calendarI18nScriptUrl, function () {
        // Hack to avoid bug in jscalendar - JRA-7713
        Calendar._TT["WEEKEND"] = Calendar._TT["WEEKEND"] || "0,6";
        Calendar._TT["DAY_FIRST"] = Calendar._TT["DAY_FIRST"] || "Display %s first";

        var disallowFutureDates = function (date) {
            var today = new Date();
            return (date > today);
        };

        $("#posting-day").val(new Date().getTime());

        Calendar.setup({
               firstDay       :    0,               // first day of the week
               displayArea    :    "show_e",        // ID of the span where the date is to be shown
               inputField     :    "posting-day",   // id of the input field
               button         :    "date_edit",     // trigger for the calendar (button ID)
               align          :    "Tl",            // alignment (defaults to "Bl")
               singleClick    :    true,
               ifFormat       :    "%r",            // format we read in - millis
               daFormat       :    "%A, %B %d, %Y", // format we display
               dateStatusFunc :    disallowFutureDates
        });
    });
});
