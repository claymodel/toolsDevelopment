<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<h2>Welcome <em><bean:write name="loginBean" property="login" filter="true" /></em>! Who do you want to delete today?</h2>
<table class="background">
  <tr height="40">
    <td width="200"><html:link href="users.jsp">Delete User Account</html:link></td>
    <td width="200"><html:link href="adduser.jsp" >Add User Account</html:link></td>
    <td width="200"><html:link href="displaygroups.jsp">Change Group-Memberships</html:link></td>
  </tr>
  <tr height="40">
    <td><html:link href="staff.jsp">Delete Staff Account</html:link></td>
    <td><html:link href="addstaff.jsp" >Add Staff Account</html:link></td>
    <td><html:link href="resetpassword.jsp" >Reset Passwords</html:link></td>
  </tr>
  <tr height="40">
    <td><html:link href="admins.jsp">Delete Admin Account</html:link></td>
    <td><html:link href="addadmin.jsp" >Add Admin Account</html:link></td>
    <td><html:link href="config.jsp" >Change Configuration</html:link></td>
  </tr>
  <tr height="40">
    <td><html:link href="tickets.jsp">Delete Tickets</html:link></td>
    <td><html:link href="addgroup.jsp" >Add Group</html:link></td>
    <td></td>
  </tr>
  <tr height="40">
    <td><html:link href="groups.jsp">Delete Group</html:link></td>
    <td><html:link href="addstatus.jsp" >Add Status</html:link></td>
    <td></td>
  </tr>
  <tr height="40">
    <td><html:link href="status.jsp">Delete Status</html:link></td>
    <td></td>
    <td><html:link href="../logout.jsp">Logout</html:link></td>
  </tr height="40">
</table>
