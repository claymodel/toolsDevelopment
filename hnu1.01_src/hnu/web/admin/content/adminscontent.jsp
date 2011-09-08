<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ page import="hnu.helper.admin.AdminsBean" %>
<jsp:useBean id="allAdmins" class="hnu.helper.admin.AdminsBean" />
<logic:present parameter="sortBy">
<logic:equal parameter="sortBy" value="id">
<jsp:setProperty name="allAdmins" property="sortBy" value="aId" />
</logic:equal>
<logic:equal parameter="sortBy" value="login">
<jsp:setProperty name="allAdmins" property="sortBy" value="aLogin" />
</logic:equal>
</logic:present>
<jsp:setProperty name="allAdmins" property="fillBean" value="true" />

<p>Available Admins Accounts: <bean:write name="allAdmins" property="size" filter="true" /></p>
<logic:notEqual name="allAdmins" property="size" value="0">
<html:form action="/admin/deleteadmins.do">
<table>
  <tr>
    <th></th>
    <th><html:link href="admins.jsp?sortBy=id">ID</html:link></th>
    <th><html:link href="admins.jsp?sortBy=name">Name</html:link></th>
  </tr>
  <logic:iterate id="adminIt" name="allAdmins" property="allAdmins">

  <tr>
    <td align="center">
      <logic:equal name="adminIt" property="id" value="1"> <!-- built in -->
      b
      </logic:equal>
       <logic:notEqual name="adminIt" property="id" value="1"> <!-- built in -->
         <input type="checkbox" name="id" value="<bean:write name="adminIt" property="id" filter="true" />" />
       </logic:notEqual>
    </td>
    <td><bean:write name="adminIt" property="id" filter="true" /></td>
    <td><bean:write name="adminIt" property="name" filter="true" /></td>
  </tr>
  </logic:iterate>
</table>
<p><html:submit property="ok" value="Delete Admins" /></p>
<p>b&nbsp;&nbsp;&nbsp;built-in</p>
<p><html:errors /></p>
</html:form>
</logic:notEqual>
<logic:equal name="allAdmins" property="size" value="0"> <i><br />
<p>No Admin Accounts available</p>
</i> </logic:equal>
