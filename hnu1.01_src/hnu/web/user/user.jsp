<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="hnu.admin.form.ConfigForm" %>
<?xml version="1.0" encoding="iso-8859-1" lang="en"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<jsp:useBean id="configForm" class="hnu.admin.form.ConfigForm" scope="request" />

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
	<head>
                <link rel="stylesheet" href="../styles/<bean:write name='configForm' property='css' />" type="text/css" />
		<title><bean:write name="configForm" property="title" />
                    <logic:match name="userForm" property="existingUser" value="false">
                       Registration form
                    </logic:match>
                    <logic:match name="userForm" property="existingUser" value="true">
                       Details of <bean:write name="userForm" property="login" />
                    </logic:match>
		</title>
 	</head>
	<body>

<div class="navi">
<logic:match name="userForm" property="existingUser" value="true">
     <p class="left-navbar">
        Logged in as <html:link title="my home" forward="myhome"><bean:write name="userhomeForm" property="login"/></html:link>
     </p>

     <p class="left-navbar">
         <html:link title="Make a new Trouble Ticket" forward="newTicket">New Ticket</html:link><br/>
         <html:link title="Edit my details" forward="editUser">Edit my details</html:link><br/>
         <html:link title="my home" forward="myhome">Back to home</html:link><br/>
     </p>

     <p class="left-navbar">
        <html:link title="Logout" href="../logout.jsp">Logout</html:link>
     </p>
</logic:match>
<logic:match name="userForm" property="existingUser" value="false">
     <p class="left-navbar">
        <html:link title="Login" href="../login.jsp">Login</html:link>
     </p>
</logic:match>
</div>

<div class="banner">
         <bean:write name="configForm" property="banner" filter="false" />
</div>

            <logic:match name="userForm" property="existingUser" value="false">
               <h1 class="main">HNU Helpdesk :: Registration form</h1>
            </logic:match>
            <logic:match name="userForm" property="existingUser" value="true">
               <h1 class="main">HNU Helpdesk :: Details of <bean:write name="userForm" property="login" /></h1>
            </logic:match>

<div class="main">
		<br /><html:errors/>
		<html:form action="/user/user">
                <br/>
                <table class="plain">
                <tr><td><em>* means required</em></td></tr>
                <logic:match name="userForm" property="existingUser" value="false">
                    <tr><td><em>You activate your account over a mail address.</em></td></tr>
                </logic:match>
                </table>
                <br/>

                <table>
                        <logic:match name="userForm" property="existingUser" value="false">
                        <tr>
                            <td>Login*</td><td><html:text property="login"/><html:errors property="login"/></td>
	  		</tr>
                        </logic:match>

                        <tr>
                        <td>First name*</td><td><html:text property="first"/><html:errors property="first"/></td>
	  		</tr>

	  		<tr>
	    		<td>Last name*</td><td><html:text property="name"/><html:errors property="name"/></td>
	  		</tr>

	  		<tr>
	    		<td>Mail address*</td><td><html:text property="mail"/><html:errors property="mail"/></td>
	  		</tr>

	  		<tr>
	    		<td>Street</td><td><html:text property="street"/><html:errors property="street"/></td>
	  		</tr>

	  		<tr>
	    		<td>ZIP</td><td><html:text property="zip"/><html:errors property="zip"/></td>
	  		</tr>

	  		<tr>
	    		<td>City</td><td><html:text property="city"/><html:errors property="city"/></td>
	  		</tr>

	  		<tr>
	    		<td>Country</td><td><html:text property="country"/><html:errors property="country"/></td>
	  		</tr>

	  		<tr>
	    		<td>Day of Birth</td>
                        <td>Day:&nbsp
                        <html:select name="userForm" property="day">
                             <logic:iterate id="d" name="userForm" property="days">
                               <html:option value="<%=d.toString()%>">
                                  <%=d.toString()%>
                               </html:option>
                             </logic:iterate>
                        </html:select>
                        Month:&nbsp
                        <html:select name="userForm" property="month">
                             <logic:iterate id="m" name="userForm" property="months">
                               <html:option value="<%=m.toString()%>">
                                  <%=m.toString()%>
                               </html:option>
                             </logic:iterate>
                        </html:select>
                        Year:&nbsp
                        <html:select name="userForm" property="year">
                             <logic:iterate id="y" name="userForm" property="years">
                               <html:option value="<%=y.toString()%>">
                                  <%=y.toString()%>
                               </html:option>
                             </logic:iterate>
                        </html:select>
                        <html:errors property="dob"/></td>
                        </tr>

	  		<tr>
	    		<td>Telephone</td><td><html:text property="telephone"/><html:errors property="telephone"/></td>
	  		</tr>

                        <logic:match name="userForm" property="existingUser" value="true">
                        </table>
                        <br/>
                        <html:submit property="ok" value="Change details"/>
                        <br />
                        <hr />
                        <br />
                        <table>
                        </logic:match>

                        <logic:match name="userForm" property="existingUser" value="true">
                        <tr>
                            <td>Old password*</td><td><html:password property="passOld"/><html:errors property="passOld"/></td>
	  		</tr>
                        </logic:match>

	  		<tr>
	    		<td>Password*</td><td><html:password property="pass"/><html:errors property="pass"/></td>
	  		</tr>

                        <tr>
                        <td>Re-enter password*</td><td><html:password property="pass2"/><html:errors property="pass2"/></td>
	  		</tr>

                    </table>
                        <logic:match name="userForm" property="existingUser" value="true">
                             <br/>
                             <html:submit property="ok" value="Change password"/>
                             <br/><br/>
                        </logic:match>

                        <logic:match name="userForm" property="existingUser" value="false">
                             <br/>
                             <html:submit property="ok" value="Submit Data"/>
                             <br/><br/>
                        </logic:match>
                    </html:form>

</div>
	</body>
</html>
