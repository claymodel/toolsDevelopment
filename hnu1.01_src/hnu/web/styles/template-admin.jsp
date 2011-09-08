<%@ taglib uri='/WEB-INF/struts-template.tld' prefix='template' %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@ page import="hnu.admin.form.ConfigForm" %>
<jsp:useBean id="configForm" class="hnu.admin.form.ConfigForm" scope="request" />

<html>
<head>
<title><bean:write name="configForm" property="title" /> :: <template:get name="title" /></title>
<link rel="stylesheet" href="../styles/<bean:write name='configForm' property='css' />" type="text/css" />
</head>
<body>
<div class="navi">
<template:get name="menu" />
</div>
<div class="banner">
<bean:write name="configForm" property="banner" filter="false" />
</div>
<h1 class="main"><template:get name="title" /></h1>
<div class="main">
<br /><br />
<template:get name="content" />
<br /><br />
</div>
</body>
</html>