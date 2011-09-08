<%@ taglib uri='/WEB-INF/struts-template.tld' prefix='template' %>
<%@ taglib uri='/WEB-INF/struts-logic.tld' prefix='logic' %>

<%@ page import="hnu.LoginBean" %>
<jsp:useBean id="loginBean" class="hnu.LoginBean" scope="session" />
<logic:equal name="loginBean" property="authd" value="true">
<logic:notEqual name="loginBean" property="type" value="admin">
<logic:redirect page="/nopermission.jsp" />
</logic:notEqual>
</logic:equal>
<logic:notEqual name="loginBean" property="authd" value="true">
<logic:redirect page="/login.jsp" />
</logic:notEqual>

<template:insert template="../styles/template-admin.jsp">
  <template:put name="title" content="Change Group Memberships" direct="true" />
  <template:put name="menu" content="../admin/menu.jsp" />
  <template:put name="content" content="../admin/content/displaygroupscontent.jsp" />
</template:insert>