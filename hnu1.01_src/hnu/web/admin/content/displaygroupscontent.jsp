<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ page import="hnu.helper.admin.GroupsBean" %>
<jsp:useBean id="allGroups" class="hnu.helper.admin.GroupsBean"  />
<jsp:setProperty name="allGroups" property="fillBean" value="true" />

<p>Available Groups: <bean:write name="allGroups" property="size" filter="true" /></p>
<table>
  <tr>
    <th>Group</th>
    <th>Members</th>
  </tr>
  <logic:iterate id="groupIt" name="allGroups" property="allGroups">
  <tr>
    <td> <a href="changegroupmembers.do?gId=<bean:write name="groupIt" property="id" filter="true" />" /> <bean:write name="groupIt" property="text" filter="true" /></a> </td>
    <td> <bean:write name="groupIt" property="members" filter="true" /> Members</a> </td>
  </tr>
  </logic:iterate>
</table>
<p><html:errors /></p>
