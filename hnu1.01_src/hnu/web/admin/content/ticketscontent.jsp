<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@ page import="hnu.helper.admin.TicketsBean" %>
<jsp:useBean id="allTickets" class="hnu.helper.admin.TicketsBean" />

<logic:present parameter="sortBy">
<logic:equal parameter="sortBy" value="id">
<jsp:setProperty name="allTickets" property="sortBy" value="tId" />
</logic:equal>
<logic:equal parameter="sortBy" value="date">
<jsp:setProperty name="allTickets" property="sortBy" value="tDate DESC" />
</logic:equal>
<logic:equal parameter="sortBy" value="priority">
<jsp:setProperty name="allTickets" property="sortBy" value="tPriority" />
</logic:equal>
<logic:equal parameter="sortBy" value="subject">
<jsp:setProperty name="allTickets" property="sortBy" value="tSubject" />
</logic:equal>
</logic:present>

<jsp:setProperty name="allTickets" property="fillBean" value="true" />

<p>Available Tickets: <bean:write name="allTickets" property="size" filter="true" /></p>
<logic:notEqual name="allTickets" property="size" value="0">
<html:form action="/admin/deletetickets.do">
 <table>
   <tr>
     <th></th>
     <th><html:link href="tickets.jsp?sortBy=id">ID</html:link></th>
     <th><html:link href="tickets.jsp?sortBy=date">Date</html:link></th>
     <th><html:link href="tickets.jsp?sortBy=priority">Priority</html:link></th>
     <th><html:link href="tickets.jsp?sortBy=subject">Subject</html:link></th>
   </tr>
   <logic:iterate id="TicketsIt" name="allTickets" property="allTickets">
   <tr>
     <td><input type="checkbox" name="id" value="<bean:write name="TicketsIt" property="id" filter="true" />" /></td>
     <td><bean:write name="TicketsIt" property="id" filter="true" /></td>
     <td><bean:write name="TicketsIt" property="date" filter="true" /></td>
     <td><bean:write name="TicketsIt" property="subject" filter="true" /></td>
     <td><bean:write name="TicketsIt" property="priority" filter="true" /></td>
   </tr>
   </logic:iterate>
 </table>
<p><html:submit property="ok" value="Delete Tickets" /></p>
</p><html:errors/></p>
</html:form>
</logic:notEqual>
<logic:equal name="allTickets" property="size" value="0">
<p><em>No Tickets available</em></p>
</logic:equal>