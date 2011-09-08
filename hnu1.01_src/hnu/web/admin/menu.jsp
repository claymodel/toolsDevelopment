<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="hnu.LoginBean" %>
<jsp:useBean id="loginBean" class="hnu.LoginBean" scope="session" />

<p class="left-navbar">
Logged in as <em><a href="adminhome.jsp"><bean:write name="loginBean" property="login" /></a></em><br>
</p>

<p class="left-navbar">
Delete:<br>
<html:link href="users.jsp">User Account</html:link><br />
<html:link href="staff.jsp">Staff Account</html:link><br />
<html:link href="admins.jsp">Admin Account</html:link><br />
<html:link href="tickets.jsp">Tickets</html:link><br />
<html:link href="groups.jsp">Group</html:link><br />
<html:link href="status.jsp">Status</html:link><br />
</p>


<p class="left-navbar">
Add:<br>
<html:link href="adduser.jsp" >User Account</html:link><br />
<html:link href="addstaff.jsp" >Staff Account</html:link><br />
<html:link href="addadmin.jsp" >Admin Account</html:link><br />
<html:link href="addgroup.jsp" >Group</html:link><br />
<html:link href="addstatus.jsp" >Status</html:link><br />
</p>

<p class="left-navbar">
<html:link href="displaygroups.jsp">Group Memberships</html:link><br />
<html:link href="resetpassword.jsp" >Passwords</html:link><br />
<html:link href="config.jsp" >Configuration</html:link><br />
</p>

<p class="left-navbar">
<html:link href="../logout.jsp">Logout</html:link>
</p>