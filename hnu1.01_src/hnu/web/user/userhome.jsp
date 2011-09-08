<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="hnu.LoginBean" %>
<%@ page import="hnu.admin.form.ConfigForm" %>
<?xml version="1.0" encoding="iso-8859-1" lang="en"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<jsp:useBean id="configForm" class="hnu.admin.form.ConfigForm" scope="request" />
<jsp:useBean id="loginBean" class="hnu.LoginBean" scope="session" />
<logic:equal name="loginBean" property="authd" value="true">
<logic:notEqual name="loginBean" property="type" value="user">
<logic:redirect page="/nopermission.jsp" />
</logic:notEqual>
</logic:equal>
<logic:notEqual name="loginBean" property="authd" value="true">
<logic:redirect page="/login.jsp" />
</logic:notEqual>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
        <head>
                <link rel="stylesheet" href="../styles/<bean:write name='configForm' property='css' />" type="text/css" />
		<title><bean:write name="configForm" property="title" />
                        :: Home of <bean:write name="userhomeForm" property="login" />
                </title>
         </head>
        <body>

<div class="navi">
     <p class="left-navbar">
        Logged in as <html:link title="my home" forward="myhome"><bean:write name="userhomeForm" property="login"/></html:link>
     </p>

     <p class="left-navbar">
         <html:link title="Make a new Trouble Ticket" forward="newTicket">New Ticket</html:link><br/>
         <html:link title="Edit my details" forward="editUser">Edit my details</html:link><br/>
         <html:link title="my home" forward="myhome">Back to home</html:link><br/>
     </p>

     <p class="left-navbar">
        <html:link title="Logout" href="../logout.jsp">Logout</html:link>
     </p>
</div>

<div class="banner">
     <bean:write name="configForm" property="banner" filter="false" />
</div>

<h1 class="main">HNU Helpdesk :: Home of <bean:write name="userhomeForm" property="login" /></h1>

<div class="main">
                <html:errors/>
                <html:form action="/user/userhome">
                <br/>
                <table>
                          <tr>
                            <td>You have made <bean:write name="userHome" property="ticketCount"/> Ticket(s).</td>
                          </tr>
                </table>
<br />
<logic:notEqual name="userHome" property="ticketCount" value="0">
                <table border=1>
                          <tr>
                            <th></th>
                            <th>Subject</th>
                            <th>Status</th>
                          </tr>
                          <logic:iterate id="Ticket" name="userHome" property="allUserTickets">
                          <tr>
                            <td><input type="checkbox" name="ticketId" value="<bean:write name="Ticket" property="ticketId" />" /></td>
                            <td><bean:write name="Ticket" property="subject" filter="true" /></td>
                            <td><bean:write name="Ticket" property="status" filter="true" /></td>
                          </tr>
                          </logic:iterate>
                </table>
<br />
                <table class="plain">
                          <tr>
                            <td><html:submit value="Ticket Details" property="submit" /></td>
                            <td><html:submit value="Set as solved" property="submit" /></td>
                            <td><html:errors property="button"/></td>
                          </tr>
                </table>
</logic:notEqual>
<logic:equal name="userHome" property="ticketCount" value="0">
     <p><html:link title="Make a new Trouble Ticket" forward="newTicket">New Trouble Ticket</html:link></p>
</logic:equal>
                </html:form>
<br />
</div>

        </body>
</html>
