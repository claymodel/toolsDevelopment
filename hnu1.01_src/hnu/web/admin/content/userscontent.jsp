<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="hnu.helper.admin.UsersBean" %>

<jsp:useBean id="allUsers" class="hnu.helper.admin.UsersBean" />
<logic:present parameter="sortBy"><logic:equal parameter="sortBy" value="id">
<jsp:setProperty name="allUsers" property="sortBy" value="uId" />
</logic:equal><logic:equal parameter="sortBy" value="first">
<jsp:setProperty name="allUsers" property="sortBy" value="uFirst" />
</logic:equal><logic:equal parameter="sortBy" value="name">
<jsp:setProperty name="allUsers" property="sortBy" value="uName" />
</logic:equal><logic:equal parameter="sortBy" value="login">
<jsp:setProperty name="allUsers" property="sortBy" value="uLogin" />
</logic:equal><logic:equal parameter="sortBy" value="mail">
<jsp:setProperty name="allUsers" property="sortBy" value="uMail" />
</logic:equal><logic:equal parameter="sortBy" value="active">
<jsp:setProperty name="allUsers" property="sortBy" value="uActive" />
</logic:equal></logic:present>
<jsp:setProperty name="allUsers" property="fillBean" value="true" />

<logic:notEqual name="allUsers" property="size" value="0">
<html:form action="/admin/deleteusers.do">
</p>Available Users: <bean:write name="allUsers" property="size" filter="true" /></p>
<table>
  <tr>
    <th></th>
    <th><html:link href="users.jsp?sortBy=id"> ID </html:link></th>
    <th><html:link href="users.jsp?sortBy=name">Name</html:link></th>
    <th><html:link href="users.jsp?sortBy=first"> Firstname </html:link></th>
    <th><html:link href="users.jsp?sortBy=login">Loginname</html:link></th>
    <th><html:link href="users.jsp?sortBy=mail">E-Mail</html:link></th>
    <th><html:link href="users.jsp?sortBy=active">Activated</html:link></th>
  </tr>
  <logic:iterate id="UsersIt" name="allUsers" property="allUsers">
  <tr>
    <td><input type="checkbox" name="id" value="<bean:write name="UsersIt" property="id" filter="true" />" /></td>
    <td><bean:write name="UsersIt" property="id" filter="true" /></td>
    <td><bean:write name="UsersIt" property="name" filter="true" /></td>
    <td><bean:write name="UsersIt" property="first" filter="true" /></td>
    <td><bean:write name="UsersIt" property="login" filter="true" /></td>
    <td><bean:write name="UsersIt" property="mail" filter="true" /></td>
    <td><bean:write name="UsersIt" property="active" filter="true" /></td>
  </tr>
  </logic:iterate>
</table>
<p><html:submit property="ok" value="Delete Users"/></p>
<p><html:errors/></p>
</html:form>
</logic:notEqual>

<logic:equal name="allUsers" property="size" value="0">
<p><i>No User Accounts available</i></p>
</logic:equal>