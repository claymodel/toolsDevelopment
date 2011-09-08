<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="hnu.admin.form.ConfigForm" %>
<jsp:useBean id="configForm" class="hnu.admin.form.ConfigForm" scope="request" />

<html>
<head>
<title><bean:write name="configForm" property="title" /> :: No permission</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" href="styles/<bean:write name='configForm' property='css' />" type="text/css" />
</head>
<body>
<h1 class="main"><bean:write name="configForm" property="title" /> :: No Permission</h1>

  <logic:notEqual name="loginBean" property="authd" value="true">
  <div class="navi">
  <p class="left-navbar">You are not <br />logged in</p>
  <p class="left-navbar"><html:link title="Login" forward="Login">Login</html:link></p>
   <logic:notEqual name="configForm" property="loginmode" value="1">
   <p class="left-navbar">New? <html:link title="Signup" forward="registerUser">Register!</html:link></p>
   </logic:notEqual>
  </div>
  </logic:notEqual>
  <logic:equal name="loginBean" property="authd" value="true">
  <div class="navi">
   <p class="left-navbar">Logged in as <bean:write name="loginBean" property="login" filter="true" /></p>
   <p class="left-navbar"><html:link title="Logout" href="logout.jsp">Logout</html:link></p>
  </div>
</logic:equal>

<div class="main">

<div>
<p align="center"><br /><br /><bean:write name="configForm" property="banner" filter="false" /></p>
<p align="center"><bean:write name="configForm" property="title" filter="false" /></p>
</div>

<div align="center"><br /><br />
<p>You have no permission to view this site!</p>
<br /><br /></div>
</body>
</html>
