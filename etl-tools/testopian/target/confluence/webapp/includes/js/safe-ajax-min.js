(function(){var a=jQuery.ajax;AJS.safe={ajax:function(b){if(b.data&&typeof b.data=="object"){b.data.atl_token=jQuery("#atlassian-token").attr("content");return a(b)}},get:function(){jQuery.ajax=AJS.safe.ajax;try{return jQuery.get.apply(jQuery,arguments)}finally{jQuery.ajax=a}},getScript:function(b,c){return AJS.safe.get(b,null,c,"script")},getJSON:function(b,c,d){return AJS.safe.get(b,c,d,"json")},post:function(b,d,e,c){jQuery.ajax=AJS.safe.ajax;try{return jQuery.post.apply(jQuery,arguments)}finally{jQuery.ajax=a}}}})();AJS.safeAjax=function(a){return AJS.safe.ajax(a)};