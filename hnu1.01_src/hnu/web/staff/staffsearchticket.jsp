<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<?xml version="1.0" encoding="iso-8859-1" lang="en"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page import="hnu.admin.form.ConfigForm" %>
<jsp:useBean id="configForm" class="hnu.admin.form.ConfigForm" scope="request" />
<jsp:useBean id="loginBean" scope="session" class="hnu.LoginBean" />
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title><bean:write name="configForm" property="title" /> :: TicketSearch - Logged in as <bean:write name="loginBean" property="login" filter="true" /></title>
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
  <p class="left-navbar"><html:link title="Stats!" href="staffStats.do">Your stats</html:link></p>
  <p class="left-navbar">Search</p>
  <p class="left-navbar"><html:link title="Logout" href="../logout.jsp">Logout</html:link></p>
</div>
<div class="banner">
<bean:write name="configForm" property="banner" filter="false" />
</div>
<h1 class="main"><bean:write name="configForm" property="title" /> :: TicketSearch</h1>
<div class="main">
  <div class="errors"><html:errors  property="general"/></div>
  <html:form action="/staff/staffsearchticket">
  <div class="form" align="center" >
    <br />
    <table class="form" border="0" cellpadding="2" cellspacing="2" summary="Login">
      <tr>
        <th>Search for</th>
        <td><html:text property="searchString"/><html:errors property="searchString"/></td>
      </tr>
    </table><br />
    <html:submit property="ok"/><br /><br />
  </div>
  </html:form>
</div>
</logic:equal>
<logic:notEqual name="loginBean" property="authd" value="true">
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