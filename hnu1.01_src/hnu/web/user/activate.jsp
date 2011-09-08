<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="hnu.admin.form.ConfigForm" %>
<?xml version="1.0" encoding="iso-8859-1" lang="en"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<jsp:useBean id="configForm" class="hnu.admin.form.ConfigForm" scope="request" />

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
	<head>
            <link rel="stylesheet" href="../styles/<bean:write name='configForm' property='css' />" type="text/css" />
            <title><bean:write name="configForm" property="title" />
               Activation form
            </title>
 	</head>
	<body>

        <div class="navi">

        </div>

        <div class="banner">
             <bean:write name="configForm" property="banner" filter="false" />
        </div>

        <h1 class="main">HNU Helpdesk :: Activation form</h1>

        <div class="main">

		<html:errors/>
                <html:form action="/user/activate">
                <br/>
		<table class="plain">
	  		<tr>
	    		<td>Enter your password:</td><td><html:password property="pass"/><html:errors property="pass"/></td>
	  		</tr>
                 </table>
                 <br/>
                        <p><html:submit value="Activate" property="ok"/></p>
                 <br/>
			</html:form>

        </div>
	</body>
</html>
