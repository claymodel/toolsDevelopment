<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ page import="hnu.helper.admin.GroupsBean" %>
<jsp:useBean id="allGroups" class="hnu.helper.admin.GroupsBean" />
<logic:present parameter="sortBy"><logic:equal parameter="sortBy" value="id">
<jsp:setProperty name="allGroups" property="sortBy" value="gId" />
</logic:equal><logic:equal parameter="sortBy" value="text">
<jsp:setProperty name="allGroups" property="sortBy" value="gText" />
</logic:equal></logic:present>
<jsp:setProperty name="allGroups" property="fillBean" value="true" />

<p>Available Groups: <bean:write name="allGroups" property="size" filter="true" /></p>
<logic:notEqual name="allGroups" property="size" value="0"> <html:form action="/admin/deletegroups">
<p><table>
  <tr>
    <th></th>
    <th><html:link href="groups.jsp?sortBy=id">ID</html:link></th>
    <th><html:link href="groups.jsp?sortBy=text">Name</html:link></th>
  </tr>
  <logic:iterate id="groupIt" name="allGroups" property="allGroups">
  <tr>
    <td><input type="checkbox" name="id" value="<bean:write name="groupIt" property="id" filter="true" />" /></td>
    <td><bean:write name="groupIt" property="id" filter="true" /></td>
    <td><a href="changegroupmembers.do?gId=<bean:write name="groupIt" property="id" filter="true" />" /> <bean:write name="groupIt" property="text" filter="true" /></a></td>
  </tr>
  </logic:iterate>
</table></p>

<p><table>
  <tr>
    <th>Move Users</th>
  </tr>
  <tr>
  <td>
  <html:checkbox property="move" value="on" /> Move Users to Group:
	<html:select property="groupId">
	<logic:iterate id="groupIt" name="allGroups" property="allGroups">
          <p><option value="<bean:write name="groupIt" property="id" filter="true" />">
  	  <bean:write name="groupIt" property="text" filter="true" />
          </option></p>
        </logic:iterate>
        </html:select>
   </td>
  </tr>
</table></p>
<p><html:submit property="ok" value="Delete Groups" /></p>
<p><html:errors/></p>
<br />
</html:form> </logic:notEqual> <logic:equal name="allGroups" property="size" value="0"> <i><br />
No Groups available<br />
</i> </logic:equal>