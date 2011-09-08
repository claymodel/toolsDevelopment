<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/taglibs-session.tld" prefix="sess" %>
  <?xml version="1.0" encoding="iso-8859-1" lang="en"?>
  <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

  <%@ page import="hnu.admin.form.ConfigForm" %>
  <jsp:useBean id="configForm" class="hnu.admin.form.ConfigForm" scope="request" />
  <jsp:useBean id="loginBean" scope="session" class="hnu.LoginBean" />

  <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
  <head>
  <title><bean:write name="configForm" property="title" filter="false" /> :: Logout</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
  <link rel="stylesheet" href="styles/<bean:write name='configForm' property='css' />" type="text/css" />
  </head>

  <body>

  <div class="navi">
   <p class="left-navbar">You are not<br /> logged in</p>
   <p class="left-navbar"><html:link title="HNU :: Login" forward="Login">Login</html:link></p>
  </div>

  <h1 class="main"><bean:write name="configForm" property="title" filter="false" /> :: Logout</h1>

    <div class="main" align="center">
    <p align="center"><br /><br /><bean:write name="configForm" property="banner" filter="false" /></p>
    <p align="center"><bean:write name="configForm" property="title" filter="false" /></p>
    <logic:notEqual name="loginBean" property="authd" value="true">
    <p><br/>You are not logged in.</p>
    <p>Click here to <html:link title="Login" href="login.jsp">login</html:link></p>
    </logic:notEqual>

    <logic:equal name="loginBean" property="authd" value="true">
     <jsp:setProperty name="loginBean" property="authd" value="false" />
     <sess:invalidate />
     <p><br />Successfully logged out.</p>
     <p>Click here to <a href="login.jsp">login</a></p>
    </logic:equal>
    <br /><br />
    </div>
</body>
</html>
