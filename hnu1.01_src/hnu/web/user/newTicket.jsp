<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="hnu.LoginBean" %>
<%@ page import="hnu.admin.form.ConfigForm" %>
<?xml version="1.0" encoding="iso-8859-1" lang="en"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<jsp:useBean id="configForm" class="hnu.admin.form.ConfigForm" scope="request" />
<jsp:useBean id="loginBean" class="hnu.LoginBean" scope="session" />
<logic:equal name="loginBean" property="authd" value="true">
<logic:notEqual name="loginBean" property="type" value="user">
<logic:redirect page="/nopermission.jsp" />
</logic:notEqual>
</logic:equal>
<logic:notEqual name="loginBean" property="authd" value="true">
<logic:redirect page="/login.jsp" />
</logic:notEqual>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
	<head>
                <link rel="stylesheet" href="../styles/<bean:write name='configForm' property='css' />" type="text/css" />
		<title><bean:write name="configForm" property="title" />
			New Ticket
		</title>
 	</head>
	<body>

        <div class="navi">
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
        </div>

        <div class="banner">
             <bean:write name="configForm" property="banner" filter="false" />
        </div>

        <h1 class="main">HNU Helpdesk :: New Ticket</h1>

        <div class="main">
		<html:errors/>
                <html:form action="/user/newTicket">
                <br/>
		<table class="plain">

	  		<tr>
	    		<th>Category</th><td>
                        <html:select name="newTicketForm" property="group">
                            <logic:iterate id="groups" name="newTicketForm" property="groups">
                              <option value="<bean:write name="groups" property="id" filter="true" />">
                               <bean:write name="groups" property="text" />
                              </option>
                            </logic:iterate>
                        </html:select></td><td><html:errors property="groups"/></td>
	  		</tr>

                        <tr>
                        <th>Priority</th><td>
                          <html:select name="newTicketForm" property="priority">
                             <logic:iterate id="prior" name="newTicketForm" property="priorities">
                               <option value="<%=prior.toString()%>">
                                  <%=prior.toString()%>
                               </option>
                             </logic:iterate>
                          </html:select>   5 is lowest priority</td><td><html:errors property="priorities"/></td>
	  		</tr>

	  		<tr>
	    		<th>Subject</td><td><html:text property="subject"/></th><td><html:errors property="subject"/></td>
	  		</tr>
               </table>
                        <p>
                        <table class="plain">
                        <tr>
                        <td><em>Describe your problem:</em><td>
                        </tr>
                        <tr>
	    		<td><html:textarea cols="50" rows="10" property="text"/></td>
                        <td><html:errors property="text"/></td>
                        </tr>
                        </p>
                        </table>
                        <p><html:submit property="ok"/></p>
                        <br/>
                </html:form>
        </div>
	</body>
</html>
