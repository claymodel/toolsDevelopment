AJS.toInit(function(b){var a=AJS.params.staticResourceUrlPrefix+"/includes/js/calendar/lang/"+AJS.params.calendarI18nFile;b.getScript(a,function(){Calendar._TT.WEEKEND=Calendar._TT.WEEKEND||"0,6";Calendar._TT.DAY_FIRST=Calendar._TT.DAY_FIRST||"Display %s first";var c=function(e){var d=new Date();return(e>d)};b("#posting-day").val(new Date().getTime());Calendar.setup({firstDay:0,displayArea:"show_e",inputField:"posting-day",button:"date_edit",align:"Tl",singleClick:true,ifFormat:"%r",daFormat:"%A, %B %d, %Y",dateStatusFunc:c})})});