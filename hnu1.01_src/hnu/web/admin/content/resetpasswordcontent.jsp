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

<%@ page import="hnu.helper.admin.UsersBean" %>
<jsp:useBean id="allUsers" class="hnu.helper.admin.UsersBean" />
<logic:present parameter="sortBy">
<logic:equal parameter="sortBy" value="id">
<jsp:setProperty name="allUsers" property="sortBy" value="uId" />
</logic:equal>
<logic:equal parameter="sortBy" value="first">
<jsp:setProperty name="allUsers" property="sortBy" value="uFirst" />
</logic:equal>
<logic:equal parameter="sortBy" value="name">
<jsp:setProperty name="allUsers" property="sortBy" value="uName" />
</logic:equal>
<logic:equal parameter="sortBy" value="login">
<jsp:setProperty name="allUsers" property="sortBy" value="uLogin" />
</logic:equal>
<logic:equal parameter="sortBy" value="mail">
<jsp:setProperty name="allUsers" property="sortBy" value="uMail" />
</logic:equal>
<logic:equal parameter="sortBy" value="active">
<jsp:setProperty name="allUsers" property="sortBy" value="uActive" />
</logic:equal>
</logic:present>
<jsp:setProperty name="allUsers" property="fillBean" value="true" />

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
<p><html:errors/></p>

<table class="plain"><tr><td>
<html:form action="/admin/resetpassword">
<p>        <table class="grid" width="100%">
	<caption><bean:write name="allUsers" property="size" filter="true" /> User Accounts</caption>
          <tr>
            <th></th>
            <th><html:link href="resetpassword.jsp?sortBy=id">ID</html:link></th>
            <th><html:link href="resetpassword.jsp?sortBy=first">Firstname</html:link></th>
            <th><html:link href="resetpassword.jsp?sortBy=name">Name</html:link></th>
            <th><html:link href="resetpassword.jsp?sortBy=login">Loginname</html:link></th>
          </tr>
          <logic:iterate id="UsersIt" name="allUsers" property="allUsers">
          <tr>
            <td><input type="checkbox" name="idUser" value="<bean:write name="UsersIt" property="id" filter="true" />" /></td>
            <td><bean:write name="UsersIt" property="id" filter="true" /></td>
            <td><bean:write name="UsersIt" property="first" filter="true" /></td>
            <td><bean:write name="UsersIt" property="name" filter="true" /></td>
            <td><bean:write name="UsersIt" property="login" filter="true" /></td>
          </tr>
          </logic:iterate>
        </table></p>
</td></tr>
<tr><td>
	  <p><table class="grid" width="100%">
	  <caption><bean:write name="allStaff" property="size" filter="true" /> Staff Accounts</caption>
	  <tr>
	  <th></th>
	  <th><html:link href="resetpassword.jsp?sortBy=id">ID</html:link></th>
	  <th><html:link href="resetpassword.jsp?sortBy=first">Firstname</html:link></th>
	  <th><html:link href="resetpassword.jsp?sortBy=name">Name</html:link></th>
	  <th><html:link href="resetpassword.jsp?sortBy=login">Loginname</html:link></th>
	  </tr>
 	   <logic:iterate id="staffIt" name="allStaff" property="allStaff">
              <tr>
	      <td><input type="checkbox" name="idStaff" value="<bean:write name="staffIt" property="id" filter="true" />" /></td>
	      <td><bean:write name="staffIt" property="id" filter="true" /></td>
	      <td><bean:write name="staffIt" property="first" filter="true" /></td>
	      <td><bean:write name="staffIt" property="name" filter="true" /></td>
	      <td><bean:write name="staffIt" property="login" filter="true" /></td>
              </tr>
	   </logic:iterate>
      </table></p>
</td></tr>
<tr><td>
	  <p><table class="grid" width="100%">
	  <caption><bean:write name="allAdmins" property="size" filter="true" /> Admin Accounts</caption>
	  <tr>
	  <th></th>
	  <th><html:link href="resetpassword.jsp?sortBy=id">ID</html:link></th>
	  <th><html:link href="resetpassword.jsp?sortBy=name">Name</html:link></th>
	  </tr>
       <logic:iterate id="adminIt" name="allAdmins" property="allAdmins">
	  <tr>
		<td><input type="checkbox" name="idAdmin" value="<bean:write name="adminIt" property="id" filter="true" />" /></td>
		<td><bean:write name="adminIt" property="id" filter="true" /></td>
		<td><bean:write name="adminIt" property="name" filter="true" /></td>
	  </tr>
       </logic:iterate>
       </table></p>
</td></tr>
<tr><td align="center">
<p><table class="grid">
<caption>E-Mail</caption>
<tr><th>Password changes</th></tr>
<tr><td align="center"><html:checkbox property="usermail" /> Inform User about password changes via Email</td></tr>
</table></p>
</td></tr>
<tr><td align="center">
<p><table class="grid">
<caption>Change Password(s) to</caption>
<tr>
<th>Loginname</th>
<th>Enter Password</th>
</tr>
<td align="center"><html:submit property="loginname" value="Loginname" /></td>
<td align="center">
  <table><tr>
  <td class="plain">New Password</td>
  <td class="plain"><html:password property="newPassword1" /></td>
  </tr><tr>
  <td class="plain">Repeat Password</td>
  <td class="plain"><html:password property="newPassword2" /></td>
  </tr></table>
<html:submit property="textfield" value="Set new Password" />
</td>
</table></p>
</td></tr>
</table>
</html:form>

