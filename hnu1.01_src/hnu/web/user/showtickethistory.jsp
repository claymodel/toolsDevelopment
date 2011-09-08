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
			Ticket History
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

       <h1 class="main">HNU Helpdesk :: Ticket history</h1>

        <div class="main">
		<html:errors/>
                <html:form action="/user/showtickethistory">
                <br />
		<table>
	  		<tr>
	    		<th align="left" >Subject: </th><td><bean:write name="showTicketHistoryForm" property="subject"/></td>
	  		</tr>

	  		<tr>
	    		<th align="left" >Date: </th><td><bean:write name="showTicketHistoryForm" property="date"/></td>
	  		</tr>

	  		<tr>
	    		<th align="left" >Status: </th><td><bean:write name="showTicketHistoryForm" property="status"/></td>
	  		</tr>

                        <tr>
                        <th align="left" >Staff: </th><td>
			<logic:present name="showTicketHistoryForm" property="pic">
			   <logic:notEqual name="showTicketHistoryForm" property="pic" value="">
                             <a href="#" onclick="window.open('../pics/<bean:write name="showTicketHistoryForm" property="pic"/>', 'tinyWindow', 'width=230,height=300')" ><bean:write name="showTicketHistoryForm" property="staff"/></a>
			   </logic:notEqual>
			</logic:present>
			<logic:notPresent name="showTicketHistoryForm" property="pic">
                             <bean:write name="showTicketHistoryForm" property="staff"/>
			</logic:notPresent>
			   </td>
	  		</tr>

                       <tr>
                       <th align="left" >Priority: </th><td>
                          <html:select name="showTicketHistoryForm" property="priority">
                             <logic:iterate id="prior" name="showTicketHistoryForm" property="priorities">
                               <html:option value="<%=prior.toString()%>">
                                  <%=prior.toString()%>
                               </html:option>
                             </logic:iterate>
                          </html:select> 5 is lowest priority</td>
	  		</tr>
                  </table>
                  <br />
                        <html:submit value="Save Changes" property="ok"/>
                  <br />
                  <hr />

<br />
		<table border=2>
                   <logic:iterate id="message" name="showTicketHistoryForm" property="messages">
                     <tr>
                       <th align="left"><bean:write name="message" property="date" />
                           <br />
                           <bean:write name="message" property="author" />
                       </th>
                       <td><bean:write name="message" property="text" filter="false" /></td>
                     </tr>
                   </logic:iterate>
                </table>
                <br />
                <h3 align="center"> New message: </h3>
                <p align="center"><html:textarea property="newMessage" cols="60" rows="10" /><html:errors property="message"/></p>
                <p align="center"><html:submit value="Submit Message" property="ok" /></p>
                <br/>
                </html:form>
</div>
        </body>
</html>
