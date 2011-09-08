<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ page import="hnu.helper.admin.StaffCollectionBean" %>
<jsp:useBean id="allStaff" class="hnu.helper.admin.StaffCollectionBean" />

<logic:present parameter="sortBy">
<logic:equal parameter="sortBy" value="id">
<jsp:setProperty name="allStaff" property="sortBy" value="sId" />
</logic:equal>
<logic:equal parameter="sortBy" value="first">
<jsp:setProperty name="allStaff" property="sortBy" value="sFirst" />
</logic:equal>
<logic:equal parameter="sortBy" value="name">
<jsp:setProperty name="allStaff" property="sortBy" value="sName" />
</logic:equal>
<logic:equal parameter="sortBy" value="login">
<jsp:setProperty name="allStaff" property="sortBy" value="sLogin" />
</logic:equal>
</logic:present>

<jsp:setProperty name="allStaff" property="fillBean" value="true" />

Available Staff Accounts: <bean:write name="allStaff" property="size" filter="true" /><br />
<logic:notEqual name="allStaff" property="size" value="0"> <html:form action="/admin/deletestaff.do">
<table>
  <tr>
    <th></th>
    <th><html:link href="staff.jsp?sortBy=id">ID</html:link></th>
    <th><html:link href="staff.jsp?sortBy=first">Firstname</html:link></th>
    <th><html:link href="staff.jsp?sortBy=name">Name</html:link></th>
    <th><html:link href="staff.jsp?sortBy=login">Loginname</html:link></th>
  </tr>
  <logic:iterate id="staffIt" name="allStaff" property="allStaff">
  <tr>
    <td><input type="checkbox" name="id" value="<bean:write name="staffIt" property="id" filter="true" />" /></td>
    <td><bean:write name="staffIt" property="id" filter="true" /></td>
    <td><bean:write name="staffIt" property="first" filter="true" /></td>
    <td><bean:write name="staffIt" property="name" filter="true" /></td>
    <td><bean:write name="staffIt" property="login" filter="true" /></td>
  </tr>
  </logic:iterate>
</table>
<p><html:submit property="ok" value="Delete Staff" /></p>
<p><html:errors/></p>
</html:form>
</logic:notEqual>
<logic:equal name="allStaff" property="size" value="0"> <i><br />
<p>No Staff Accounts available</p>
</i> </logic:equal>

