AJS.toInit(function ($) {
    $(".diff-snipped").each(function() {
        var snip = $(this).find(".diff-snip-text.hidden");
        snip.removeClass("hidden").hide();       // Hide, JQuery-style`
        this.style.cursor = "pointer";
        this.title = "Click to toggle hidden lines of context";  //todo
        $(this).click(function() {
            var hidden = $(this).find("div:hidden");
            var visible = $(this).find("div:visible");
            visible.slideUp(function() {
                hidden.slideDown();
            });
        });
    });
});
