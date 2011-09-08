/**
 * Confluence Comments Binder Component.
 *
 * @class comments
 * @namespace AJS.Confluence.Binder
 */
AJS.Confluence.Binder.comments = function() {
    var $ = AJS.$;
    if ($(window).data("comments-bound")) {
        AJS.log("Currently, this comment binding can only be done once per page");
        return;
    }

    var collapseTransition = function (comment) {
        var imageTransition = function (image, reverse) {
            return {
                animate: function (pos) {
                    image.style.height = AJS.animation.interpolate(pos, 48, 24, reverse) + "px";
                    image.style.width = image.style.height;
                    image.style.marginLeft = AJS.animation.interpolate(pos, 0, 12, reverse) + "px";
                },
                onFinish: function () {
                    image.style.height = '';
                    image.style.width = '';
                    image.style.marginLeft = '';
                }
            };
        };
        var opacityTransition = function (el, reverse) {
            return {
                start: 1.0,
                end: 0.0,
                reverse: reverse,
                animate: function (pos) {
                    el.style.opacity = pos;
                    el.style.filter = "alpha(opacity=" + (pos * 100) + ")";
                },
                onFinish: function () {
                    el.style.opacity = "";
                    el.style.filter = "alpha(opacity=" + (reverse ? 100 : 0) + ")";
                }
            };
        };
        var heightTransition = function (el, reverse) {
            if (!reverse)
                el.originalHeight = jQuery(el).height();
            return {
                start: el.originalHeight || 50,
                end: 0,
                reverse: reverse,
                animate: function (pos) {
                    el.style.height = pos + "px";
                },
                onFinish: function () {
                    el.style.height = '';
                }
            };
        };

        var body = jQuery(comment).find('.comment-body')[0];
        var reverse = comment.className.indexOf("collapsed") >= 0;
        return AJS.animation.combine([
            imageTransition(jQuery(comment).parent().find('.comment-user-logo img, .comment-user-logo .replacement')[0], reverse),
            opacityTransition(body, reverse),
            opacityTransition(jQuery(comment).find('.excerpt')[0], !reverse),
            heightTransition(body, reverse),
            {
                onFinish: function () {
                    if (reverse)
                        jQuery(comment).removeClass('collapsed');
                    else
                        jQuery(comment).addClass('collapsed');
                }
            }
        ]);
    };

    /*
     * Bind collapsing comment functionality to comment-toggle class.
     */
    var commentToggle = function () {
        var toggle = this;
        $(toggle).unbind('click');
        AJS.animation.add(collapseTransition($(toggle).parent()[0]));
        AJS.animation.add({
            onFinish: function () { $(toggle).click(commentToggle); } /* rebind */
        });
        AJS.animation.start();
    };

    if (AJS.isIE6) {
        $(".logo.anonymous").each(function () {
            var div = document.createElement("div");
            div.className = "replacement";
            AJS.applyPngFilter(div, this.src);
            $(this).replaceWith(div);
        });
        $(".comment-actions .comment-permalink a").each(function () {
            $(this).addClass("filtered");
            var path_light = $(this).css("background-image").replace(/^url\(\"?|\"?\)$/g, ""); // remove url(...) surrounding actual URL
            var path_dark = path_light.replace("light", "dark");
            AJS.applyPngFilter(this, path_light);
            this.style.cursor = "pointer";
            this.style.background = "none";
            $(this).hover(function () {
                AJS.applyPngFilter(this, path_dark);
            }, function () {
                AJS.applyPngFilter(this, path_light);
            });
        });
    }

    /*
     * Alternate colours of comments. Doing this with threaded comments in the backend
     * is painful.
     */
    $('#comments-section .comment:odd').addClass('odd');

    /*
     * Remove comment pop-up confirmation.
     */
    $('.comment-action-remove a').click(function() {
        if(confirm(AJS.params.deleteCommentConfirmMessage)) {
            this.href = this.href + '&confirm=yes';
            return true;
        }
        return false;
    });

    var toggleComments = function(show) {
        $('#page-comments').toggleClass("hidden", !show);
        $('.comments-show-hide').toggleClass("comments-showing", show);
        $('.icon.comments-show-hide').toggleClass("icon-section-closed", !show);
        $('.icon.comments-show-hide').toggleClass("icon-section-opened", show);
    };

    /*
     * Toggle links for hiding and showing the comments section.
     */
    $('.comments-show-hide').click(function() {
        toggleComments(!$(this).hasClass("comments-showing"));
        return false;
    });
};

