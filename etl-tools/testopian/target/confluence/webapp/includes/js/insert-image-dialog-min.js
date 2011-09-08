jQuery.fn.sizeToFit=function(){var a=jQuery;this.each(function(){var d=this;var b=a(this).parent();var e=b.height();b.children().each(function(){if(this!=d){e-=a(this).outerHeight()}});var c=a(this).outerHeight()-a(this).height();a(this).css("height",Math.max(0,e-c)+"px")});return this};AJS.Editor.ImageDialog=AJS.Editor.ImageDialog||{beforeShowListeners:[],afterThumbnailsDisplayedListeners:[]};AJS.toInit(function(a){AJS.wikiAttrToString=function(b){var c=[];for(var d in b){if(b.hasOwnProperty(d)){c.push(typeof b[d]=="boolean"?b[d]?d:"":d+"="+b[d])}}return c.length?"|"+c.join(","):""};AJS.Editor.insertImageDialog=function(c,b){this.openImageDialog({submitCallback:c,cancelCallback:b})};AJS.Editor.openImageDialog=function(q){q=q||{};var l=100,g=97;var i="",c=new AJS.Dialog(800,590,"insert-image-dialog");var d=AJS.params.attachmentSourceContentId;var h=function(u){var t={},v=a(".img-align",u).val(),r=!!a(".img-thumbnail",u).attr("checked"),s=!!a(".img-border",u).attr("checked");v!="none"&&(t.align=v);r&&(t.thumbnail=true);s&&(t.border="1");return t};function m(){c.hide().remove();a(document).unbind(".insert-image");q.cancelCallback&&q.cancelCallback()}a(document).bind("keydown.insert-image",function(r){if(r.which==27&&!a("#fancy_overlay").is(":visible")){m();return AJS.stopEvent(r)}});var b=AJS.I18n.getText("image.browser.insert.title");var p=AJS.I18n.getText("image.browser.insert.button");if(q.imageProperties){b=AJS.I18n.getText("image.browser.edit.title");p=AJS.I18n.getText("image.browser.edit.button")}c.addHeader(b);c.addPanel(AJS.I18n.getText("image.browser.attached.images.title"),AJS.renderTemplate("attachedImages",AJS.getTemplate("imagePropertiesForm")),"attachments-panel");c.addPanel(AJS.I18n.getText("image.browser.web.image.title"),AJS.renderTemplate("webImage",AJS.getTemplate("imagePropertiesForm")),"web-image-panel");c.addButton(p,function(r){var s=h(r.getCurrentPanel().body);r.remove();a(document).unbind(".insert-image");q.submitCallback&&q.submitCallback(i,s,d)});c.addCancel(AJS.I18n.getText("cancel.name"),m);c.get("panel:0").setPadding(0);c.get("panel:1").setPadding(0);c.get("panel:0").select();var f=c.get("button:0")[0].item;f.attr("disabled","disabled");a("input.image-url",c.popup.element).bind("keyup click",function(r){var s=a(this).val();i=s;f.attr("disabled",(s!=""&&s!="http://"?"":"disabled"));if(r.which==13){a("input.image-preview",c.popup.element).click();c.get("button:0")[0].item.focus()}});a("input.image-preview",c.popup.element).click(function(){var r=a(this).closest("div");var w=r.find("input.image-url").val();var v=r.find(".image-preview-area");var u=r.find(".image-preview-throbber");u.removeClass("hidden");var t=Raphael.spinner(u[0],60,"#666");var s=r.find(".image-preview-error");v.addClass("faraway");s.addClass("hidden");v.html("");a("<img>").load(function(){t();u.addClass("hidden");v.removeClass("faraway")}).error(function(){t();u.addClass("hidden");s.removeClass("hidden")}).appendTo(v).attr("src",w)});if(q.imageProperties){a(".img-align",c.popup.element).val(q.imageProperties.align||"none");a(".img-thumbnail",c.popup.element).attr("checked",q.imageProperties.thumbnail||"");a(".img-border",c.popup.element).attr("checked",!!q.imageProperties.border||"");if(q.imageProperties.url){c.get("panel:1").select();a("input.image-url",c.popup.element).val(q.imageProperties.url).click();a("input.image-preview",c.popup.element).click()}}AJS.log(AJS.Editor.ImageDialog.beforeShowListeners.length+" beforeShow listeners registered.");a.each(AJS.Editor.ImageDialog.beforeShowListeners,function(){this()});c.show();c.popup.element.find(".dialog-button-panel").append(AJS.template.load("insert-image-did-you-know"));a("select.img-align").focus();var o=a("#upload-attachment form");var k=a("#upload-attachment .image-uploading");var j=a("#upload-attachment .warning");var n=a("#attached-images");AJS.Editor.ImageDialog.clearErrors=function(){j.addClass("hidden");j.empty();n.sizeToFit()};AJS.Editor.ImageDialog.displayErrors=function(s){if(!s||!s.length){return}j.removeClass("hidden");var r=a("ul",j);if(!r.length){r=a("<ul></ul>");r.appendTo(j)}a.each(s,function(t,u){if(!u){return}a("<li>"+u.substring(0,Math.min(g,u.length))+(u.length>g?"&hellip;":"")+"</li>").attr("title",u).appendTo(r)});a("#attached-images").sizeToFit()};AJS.Editor.ImageDialog.imagesContainerSelector="#attached-images .image-list";o.ajaxForm({dataType:"json",data:{contentId:d,responseFormat:"html"},resetForm:true,beforeSubmit:function(){AJS.Editor.ImageDialog.setUploadInProgress(true);AJS.Editor.ImageDialog.clearErrors()},error:function(r){AJS.Editor.ImageDialog.setUploadInProgress(false);AJS.Editor.ImageDialog.displayErrors([AJS.I18n.getText("image.browser.upload.error")]);AJS.log("Response from server was: "+r.responseText)},success:function(r){AJS.Editor.ImageDialog.setUploadInProgress(false);var s=[].concat(r.validationErrors||[]).concat(r.actionErrors||[]).concat(r.errorMessage||[]);if(s.length>0){AJS.Editor.ImageDialog.displayErrors(s);return}AJS.Editor.ImageDialog.refreshWithLatestImages(a.map(r.attachmentsAdded||[],function(t){return t.name}))}});o.find("input:file").change(function(){o.submit()});function e(s){if(Math.max(s.thumbnailWidth,s.thumbnailHeight)>l){if(s.thumbnailHeight>s.thumbnailWidth){s.thumbnailWidth=s.thumbnailWidth*l/s.thumbnailHeight;s.thumbnailHeight=l}else{s.thumbnailHeight=s.thumbnailHeight*l/s.thumbnailWidth;s.thumbnailWidth=l}}var t=s.thumbnailUrl+(s.thumbnailUrl.indexOf("?")+1?"&":"?")+"nonce="+(+new Date);var r=a(AJS.renderTemplate("imageDialogImage",t,s.thumbnailWidth,s.thumbnailHeight,(100-s.thumbnailHeight)/2,s.downloadUrl,s.name));r.find(".image-container").andSelf().hover(function(){a(this).addClass("hover")},function(){a(this).removeClass("hover")});r.find("img").load(function(){r.find(".image-container").removeClass("loading")});r.click(function(u){a("#attached-images .selected").removeClass("selected");r.addClass("selected").focus();i=this.name=this.name||a(".caption",this).text();f.attr("disabled","");return AJS.stopEvent(u)});r.dblclick(function(){a(this).click();f.click()});a(".zoom",r).fancybox({padding:0,zoomSpeedIn:500,zoomSpeedOut:500,overlayShow:true,overlayOpacity:0.5});return r}a(document).bind("keydown.insert-image",function(s){if(!n.is(":visible")){return}if(a("#fancy_overlay").is(":visible")){if(s.which==32){a("#fancy_close").click();s.preventDefault();s.stopPropagation();return false}}else{function r(x){var u=a(".attached-image",n);var w=a(".attached-image.selected",n);var t=u.index(w)+x;if(t<0){t=u.length-1}if(t>=u.length){t=0}var v=u.eq(t);v.click().focus();n.simpleScrollTo(v)}if(s.which==37){r(-1);return AJS.stopEvent(s)}else{if(s.which==38){r(-4);return AJS.stopEvent(s)}else{if(s.which==39){r(1);return AJS.stopEvent(s)}else{if(s.which==40){r(4);return AJS.stopEvent(s)}else{if(s.which==32&&a(".attached-image.selected").length>0){a(".attached-image.selected .zoom").click();return AJS.stopEvent(s)}else{if(s.which==13&&!f.is(":disabled")){f.click();return AJS.stopEvent(s)}}}}}}}});AJS.Editor.ImageDialog.setUploadInProgress=function(r,s){if(r){o.addClass("hidden");k.removeClass("hidden");s?k.html(s):k.html(AJS.I18n.getText("image.browser.upload.image.uploading"))}else{o.removeClass("hidden");k.addClass("hidden")}};AJS.Editor.ImageDialog.refreshWithLatestImages=function(r){r=a.map(r||[],function(s){return s&&s.toLowerCase()});a.ajax({type:"GET",url:AJS.params.contextPath+"/pages/attachedimages.action",dataType:"json",data:{contentId:d},error:function(){n.find(".loading-message").remove();n.append(AJS.renderTemplate("imageDialogErrorRetrievingAttachments"))},success:function(s){n.find(".loading-message").remove();n.find(".image-list").empty();n.find(".no-attachments").remove();a(s.images||[]).each(function(){if(this.name&&a.inArray(this.name.toLowerCase(),r)!=-1){n.find(".image-list").prepend(e(this))}else{n.find(".image-list").append(e(this))}});if(n.find(".image-list li").length==0){n.append(AJS.renderTemplate("imageDialogNoAttachments"))}n.sizeToFit().click(function(){i=null;f.attr("disabled","disabled");a(this).find(".selected").removeClass("selected")});if(r.length){n.find(".image-list li:first").click()}else{if(q.imageProperties&&q.imageProperties.imageFileName){n.find("img[src*=/"+q.imageProperties.imageFileName+"?]").click()}}AJS.log(AJS.Editor.ImageDialog.afterThumbnailsDisplayedListeners.length+" afterThumbnailsDisplayed listeners registered.");a.each(AJS.Editor.ImageDialog.afterThumbnailsDisplayedListeners,function(){this()});var u=[];var t=a.map(s.images||[],function(v){return v.name&&v.name.toLowerCase()});a.each(r,function(v,w){a.inArray(w,t)==-1&&u.push(AJS.renderTemplate("imageNotThumbnailable",w))});u&&AJS.Editor.ImageDialog.displayErrors(u)}})};AJS.Editor.ImageDialog.refreshWithLatestImages()};a("#editor-insert-image").click(function(c){AJS.Editor.storeTextareaBits();var b=document.getElementById("markupTextarea");AJS.Editor.insertImageDialog(function(d,e){AJS.Editor.Markup.insertOrUpdateText(AJS.format("\n!{0}{1}!\n",d,AJS.wikiAttrToString(e)),b)});return AJS.stopEvent(c)})});