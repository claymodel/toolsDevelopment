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
      New Ticket
   </title>
</head>

<body>

<div class="navi">
     <p class="left-navbar">
        <html:link title="Login" href="../login.jsp">Login</html:link>
     </p>
</div>

<div class="banner">
    <bean:write name="configForm" property="banner" filter="false" />
</div>

<h1 class="main">
   HNU Helpdesk :: Registration
</h1>

<div class="main">
   <p>
   <br/>
   <em>You will receive a mail with a link which will activate your account!</em>
   <br/>
   <em>Account was created successfully!</em>
   <br/>
   <br/>
   </p>
</div>

</body>
</html>
