<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<?xml version="1.0" encoding="iso-8859-1" lang="en"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page import="hnu.admin.form.ConfigForm" %>
<jsp:useBean id="configForm" class="hnu.admin.form.ConfigForm" scope="request" />
<jsp:useBean id="loginBean" scope="session" class="hnu.LoginBean" />
<jsp:useBean id="editStaffProfileForm" scope="session" class="hnu.staff.form.EditStaffProfileForm" />
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title><bean:write name="configForm" property="title" /> :: Staff-Profile - Logged in as <bean:write name="loginBean" property="login" filter="true" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" href="../styles/hnu.css" type="text/css" />
<link rel="alternate stylesheet" href="../styles/hnu2.css" type="text/css" />
</head>
<body>
<logic:equal name="loginBean" property="authd" value="true">
<div class="navi">
  <p class="left-navbar">Logged in as <bean:write name="loginBean" property="login" filter="true" /></p>
  <p class="left-navbar"><html:link title="StaffArea" href="refrsesscontent.do">StaffArea</html:link></p>
  <p class="left-navbar">Show/Edit Profile</p>
  <p class="left-navbar"><html:link title="Stats!" href="staffStats.do">Your stats</html:link></p>
  <p class="left-navbar"><html:link title="Search for tickets" href="staffsearchticket.jsp">Search</html:link></p>
  <p class="left-navbar"><html:link title="Logout" href="../logout.jsp">Logout</html:link></p>
</div>
<h1 class="main"><bean:write name="configForm" property="title" /> :: Edit your profile</h1>
<div class="banner">
<bean:write name="configForm" property="banner" filter="false" />
</div>
<div class="main"><br />
  <div class="errors"><html:errors  property="general"/></div><br />
  <html:form enctype="multipart/form-data" action="/staff/editStaffProfile">
  <table border="1" cellpadding="2" cellspacing="1" summary="new tickets">
    <tr>
      <td>Name</td>
      <td><html:text property="name"/><span class="errors"><html:errors property="name"/></span></td>
    </tr>
    <tr>
      <td>Firstname</td>
      <td><html:text property="firstname"/><span class="errors"><html:errors property="firstname"/></span></td>
    </tr>
    <tr>
      <td>Password</td>
      <td><html:password property="pass"/><span class="errors"><html:errors property="pass"/></span></td>
    </tr>
    <tr>
      <td>Again</td>
      <td><html:password property="pass2"/><span class="errors"><html:errors property="pass2"/></span></td>
    </tr>
    <tr>
      <td>Picture</td>
      <td><html:file property="pic" accept="gif,jpg,png"/><span class="errors"><html:errors property="pic"/></span></td>
    </tr>
    <tr>
      <td>Your current pic</td><td><img alt="Your picture" src='<bean:write name="editStaffProfileForm" property="picName" />' /></td>
    </tr>
  </table><br />
  <html:submit property="ok"/><br /><br />
  </html:form> </div><br />
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