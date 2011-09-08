<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<?xml version="1.0" encoding="iso-8859-1" lang="en"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page import="hnu.admin.form.ConfigForm" %>
<jsp:useBean id="configForm" class="hnu.admin.form.ConfigForm" scope="request" />
<jsp:useBean id="loginBean" scope="session" class="hnu.LoginBean" />
<jsp:useBean id="staffBean" scope="session" class="hnu.helper.staff.StaffBean" />
<jsp:useBean id="statsBean" scope="session" class="hnu.helper.staff.StatsBean" />
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title><bean:write name="configForm" property="title" /> :: Statistics - Logged in as <bean:write name="loginBean" property="login" filter="true" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" href="../styles/hnu.css" type="text/css" />
<link rel="alternate stylesheet" href="../styles/hnu2.css" type="text/css" />
</head>
<body>
<logic:equal name="loginBean" property="authd" value="true">
<div class="navi">
  <p class="left-navbar">Logged in as <bean:write name="loginBean" property="login" filter="true" /></p>
  <p class="left-navbar"><html:link title="StaffArea" href="refrsesscontent.do">StaffArea</html:link></p>
  <p class="left-navbar"><html:link title="Edit/Show your profile" href="fillProfileForm.do">Show/Edit
    Profile</html:link></p>
  <p class="left-navbar">Your stats</p>
  <p class="left-navbar"><html:link title="Search for tickets" href="staffsearchticket.jsp">Search</html:link></p>
  <p class="left-navbar"><html:link title="Logout" href="../logout.jsp">Logout</html:link></p>
</div>
<div class="banner">
<bean:write name="configForm" property="banner" filter="false" />
</div>
<h1 class="main"><bean:write name="configForm" property="title" /> :: Statistics</h1>
<div class="main">
  <div class="errors"><html:errors  property="general"/></div>
  <table border="1" cellpadding="2" cellspacing="1" summary="new tickets">
    <caption>
    Some Stats
    </caption>
    <tr>
      <th>Tickets total</th><td><bean:write name="statsBean" property="ticketsTotal"/></td>
    </tr>
    <tr>
      <th>Your tickets</th><td><bean:write name="statsBean" property="ticketsStaff"/></td>
    </tr>
    <logic:iterate id="status" name="statsBean" property="ticketsStaffPerStatus">
    <tr>
      <th><bean:write name="status" property="name"/></th><td><bean:write name="status" property="count"/></td>
    </tr>
    </logic:iterate>
    <tr>
      <th>Your messages</th><td><bean:write name="statsBean" property="messagesStaff"/></td>
    </tr>
  </table><br />
</div>
</logic:equal> <logic:notEqual name="loginBean" property="authd" value="true">
<div class="navi">
  <p class="left-navbar">Not logged in</p>
  <p class="left-navbar"><html:link title="HNU :: Login" href="../login.jsp">Login</html:link></p>
</div>
<h1 class="main"><bean:write name="configForm" property="title" /> :: NOT LOGGED IN</h1>
<div class="main">
  <p>Please <html:link title="HNU :: Login" href="../login.jsp">log in</html:link></p>
</div>
</logic:notEqual>
</body>
</html>