<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<?xml version="1.0" encoding="iso-8859-1" lang="en"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page import="hnu.admin.form.ConfigForm" %>
<jsp:useBean id="configForm" class="hnu.admin.form.ConfigForm" scope="request" />
<jsp:useBean id="loginBean" scope="session" class="hnu.LoginBean" />
<jsp:useBean id="staffBean" scope="session" class="hnu.helper.staff.StaffBean" />
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title><bean:write name="configForm" property="title" /> :: Work on Ticket - Logged in as <bean:write name="loginBean" property="login" filter="true" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" href="../styles/hnu.css" type="text/css" />
<link rel="alternate stylesheet" href="../styles/hnu2.css" type="text/css" />
</head>
<body><logic:equal name="loginBean" property="authd" value="true">
<div class="navi">
  <p class="left-navbar">Logged in as <bean:write name="loginBean" property="login" filter="true" /></p>
  <p class="left-navbar"><html:link title="StaffArea" href="refrsesscontent.do">StaffArea</html:link></p>
  <p class="left-navbar"><html:link title="Edit/Show your profile" href="fillProfileForm.do">Show/Edit
    Profile</html:link></p>
  <p class="left-navbar"><html:link title="Stats!" href="staffStats.do">Your stats</html:link></p>
  <p class="left-navbar"><html:link title="Search for tickets" href="staffsearchticket.jsp">Search</html:link></p>
  <p class="left-navbar"><html:link title="Logout" href="../logout.jsp">Logout</html:link></p>
</div>
<h1 class="main"><bean:write name="configForm" property="title" /> :: Work on a Ticket</h1>
<div class="banner">
<bean:write name="configForm" property="banner" filter="false" />
</div>
<div class="main">
<div class="errors"><html:errors  property="general"/></div>
<table border="1" cellpadding="2" cellspacing="1" summary="ticket">
  <caption>
  Working on the following ticket
  </caption>
  <tr>
    <th>ID</th>
    <th>From</th>
    <th>Subject</th>
    <th>Group</th>
    <th>Status</th>
    <th>Since</th>
    <th>Priority</th>
  </tr>
  <tr>
  <td><bean:write name="wholeTicket" property="id" /></td>
  <td><bean:write name="wholeTicket" property="user" /></td>
  <td><bean:write name="wholeTicket" property="subject" /></td>
  <td><bean:write name="wholeTicket" property="group" /></td>
  <td><bean:write name="wholeTicket" property="status" /></td>
  <td><bean:write name="wholeTicket" property="timeSince" /></td>
  <td><bean:write name="wholeTicket" property="priority" /></td>
  </tr>
  </table><br />
  <table border="1" cellpadding="2" cellspacing="1" summary="messages">
  <logic:iterate id="message" name="wholeTicket" property="messages">
  <tr>
  <th colspan="1"><bean:write name="message" property="author"/><br />
  <bean:write name="message" property="date"/></th>
  <td colspan="6"><bean:write name="message" property="body" filter="false"/></td>
  </tr>
  </logic:iterate>
</table><br />
<table class="form" border="0" cellpadding="2" cellspacing="2" summary="working form">
  <html:form action="/staff/staffworkonticket">
  <html:hidden name="wholeTicket" property="id"/>
  <tr>
    <td>message</td>
    <td><html:textarea property="message"/><html:errors property="message"/></td>
  </tr>
  <tr>
    <td>ticketStatus</td>
    <td><html:select property="ticketStatus">
	<html:options collection="ticketStatus" labelProperty="name" property="statusID"/>
	</html:select><html:errors property="ticketStatus"/></td>
  </tr>
  <html:submit property="ok"/>
  </html:form>
</table><br /><br /></div></logic:equal>
<logic:notEqual name="loginBean" property="authd" value="true">
<div class="navi">
  <p class="left-navbar">Not logged in</p>
  <p class="left-navbar"><html:link title="HNU :: Login" href="../login.jsp">Login</html:link></p>
</div>
<h1 class="main"><bean:write name="configForm" property="title" /> :: NOT LOGGED IN</h1>
<div class="main">
  <p>Please <html:link title="HNU :: Login" href="../login.jsp">log in</html:link></p>
</div>
</logic:notEqual> </head>
</html>
