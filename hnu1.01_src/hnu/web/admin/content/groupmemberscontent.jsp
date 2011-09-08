<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="hnu.helper.admin.StaffCollectionBean" %>

<jsp:useBean scope="session" id="groupStaff" class="hnu.helper.admin.StaffCollectionBean" />

<logic:equal name="groupStaff" property="group" value="">
<p>Invalid Group. Goto <html:link title="Groups" href="groups.jsp"> Groups</html:link>.</p>
</logic:equal>

<html:errors/>
<h2>Members of Group <bean:write name="groupStaff" property="groupName" filter="true" /></h2>
<logic:notEqual name="groupStaff" property="size" value="0"> <html:form action="/admin/groupmembers">
<input type="hidden" name="group" value="<bean:write name="groupStaff" property="group" filter="true" />" />
<table>
  <tr>
    <th></th>
    <th><a href="changegroupmembers.do?sortBy=id&gId=<bean:write name="groupStaff" property="group" filter="true" />">ID</a></th>
    <th><a href="changegroupmembers.do?sortBy=first&gId=<bean:write name="groupStaff" property="group" filter="true" />">Firstname</a></th>
    <th><a href="changegroupmembers.do?sortBy=name&gId=<bean:write name="groupStaff" property="group" filter="true" />">Name</a></th>
    <th><a href="changegroupmembers.do?sortBy=login&gId=<bean:write name="groupStaff" property="group" filter="true" />">Loginame</a></th>
  </tr>
  <logic:iterate id="staffIt" name="groupStaff" property="allStaff">
  <tr>
    <td>
	  <logic:equal name="staffIt" property="member" value="true">
      <input type="checkbox" name="idRemove" value="<bean:write name="staffIt" property="id" filter="true" />" checked="checked" /> </logic:equal> <logic:notEqual name="staffIt" property="member" value="true">
      <input type="checkbox" name="idAdd" value="<bean:write name="staffIt" property="id" filter="true" />" />
	  </logic:notEqual>
	</td>
    <td> <bean:write name="staffIt" property="id" filter="true" /> </td>
    <td> <bean:write name="staffIt" property="first" filter="true" /> </td>
    <td> <bean:write name="staffIt" property="name" filter="true" /> </td>
    <td> <bean:write name="staffIt" property="login" filter="true" /> </td>
  </tr>
  </logic:iterate>
</table>
<p><html:submit property="ok" value="Change membership" /></p>
<p><html:errors /></p>
</html:form>
</logic:notEqual>
<logic:equal name="groupStaff" property="size" value="0">
<p><em>No Staff Accounts available</i></p>
</logic:equal>
