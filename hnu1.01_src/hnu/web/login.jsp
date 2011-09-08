<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<?xml version="1.0" encoding="iso-8859-1" lang="en"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<%@ page import="hnu.admin.form.ConfigForm" %>
<jsp:useBean id="configForm" class="hnu.admin.form.ConfigForm" scope="request" />

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title><bean:write name="configForm" property="title" /> :: Login</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" href="/hnu/styles/<bean:write name='configForm' property='css' />" type="text/css" />
</head>

<body>

<jsp:useBean id="loginBean" scope="session" class="hnu.LoginBean" />

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

<h1 class="main"><bean:write name="configForm" property="title" /> :: Login</h1>

<div class="main">

<div>
<p align="center"><br /><br /><bean:write name="configForm" property="banner" filter="false" /></p>
<p align="center"><bean:write name="configForm" property="title" filter="false" /></p>
</div>

<logic:notEqual name="loginBean" property="authd" value="true">

<html:form action="/login">
<div class="form" align="center" >
<br /><br />
<table class="form" border="0" cellpadding="2" cellspacing="2" summary="Login">
<tr>
 <td>Username:</td>
 <td><input type="text" name="login" value="" /><span class="errors"><html:errors property="login"/></span></td>
</tr>

<tr>
 <td>Password:</td>
 <td><input type="password" name="pass" value="" /><span class="errors"><html:errors property="pass"/></span></td>
</tr>
</table>
<br /><br />
<html:submit property="ok" value="Log in!" />
<br /><br />
<html:errors property="general"/>
</div>
</html:form>
</logic:notEqual>

<p align="center">
<logic:equal name="loginBean" property="authd" value="true">
 <logic:equal name="loginBean" property="type" value="staff">
  You are already logged in. Advance to <html:link title="HNU :: Staffarea" forward="staffHome">staffarea</html:link>.
 </logic:equal>
 <logic:equal name="loginBean" property="type" value="user">
  You are already logged in. Advance to <html:link title="HNU :: Userarea" forward="userHome">userarea</html:link>.
 </logic:equal>
 <logic:equal name="loginBean" property="type" value="admin">
  You are already logged in. Advance to <html:link title="HNU :: Adminarea" href="admin/adminhome.jsp">adminarea</html:link>.
 </logic:equal>
</logic:equal>
</p>
<br />
</div>

</body>
</html>