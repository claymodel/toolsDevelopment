<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ page import="hnu.helper.admin.StatusCollectionBean" %>
<jsp:useBean id="allStatus" class="hnu.helper.admin.StatusCollectionBean" />

<logic:present parameter="sortBy">
<logic:equal parameter="sortBy" value="id">
<jsp:setProperty name="allStatus" property="sortBy" value="stId" />
</logic:equal>
<logic:equal parameter="sortBy" value="text">
<jsp:setProperty name="allStatus" property="sortBy" value="stText" />
</logic:equal>
</logic:present>

<jsp:setProperty name="allStatus" property="fillBean" value="true" />

<p>Available Status Messages: <bean:write name="allStatus" property="size" filter="true" /></p>
<logic:notEqual name="allStatus" property="size" value="0">
<html:form action="/admin/deletestatus.do">
<p><table>
  <tr>
    <th></th>
    <th><html:link href="status.jsp?sortBy=id">ID</html:link></th>
    <th><html:link href="status.jsp?sortBy=text">Description</html:link></th>
  </tr>
  <logic:iterate id="statusIt" name="allStatus" property="allStatus">
  <tr>
    <td align="center">
      <logic:equal name="statusIt" property="id" value="1"> <!-- built in -->
      b
      </logic:equal>
      <logic:equal name="statusIt" property="id" value="2"> <!-- built in -->
      b
      </logic:equal>
      <logic:equal name="statusIt" property="id" value="3"> <!-- built in -->
      b
      </logic:equal>
      <logic:equal name="statusIt" property="id" value="4"> <!-- built in -->
      b
      </logic:equal>
      <logic:notEqual name="statusIt" property="id" value="1"> <!-- built in -->
      <logic:notEqual name="statusIt" property="id" value="2"> <!-- built in -->
      <logic:notEqual name="statusIt" property="id" value="3"> <!-- built in -->
      <logic:notEqual name="statusIt" property="id" value="4"> <!-- built in -->
       <input type="checkbox" name="id" value="<bean:write name="statusIt" property="id" filter="true" />" />
      </logic:notEqual>
      </logic:notEqual>
      </logic:notEqual>
      </logic:notEqual>
    </td>
    <td><bean:write name="statusIt" property="id" filter="true" /></td>
    <td><bean:write name="statusIt" property="name" filter="true" /></td>
  </tr>
  </logic:iterate>
</table></p>
<table>
<tr>
  <th>Change Tickets</th>
</tr>
<tr>
<td>
<html:checkbox property="move" value="on" /> Change Tickets with deleted Status to:
<html:select property="status">
  <logic:iterate id="statusIt" name="allStatus" property="allStatus">
	<option value="<bean:write name="statusIt" property="id" filter="true" />">
		<bean:write name="statusIt" property="name" filter="true" />
	</option>
  </logic:iterate>
</html:select>
</td>
</tr>
</table>
</p>
<p><html:submit property="ok" value="Delete Status" /></p>
<p>b&nbsp;&nbsp;&nbsp;built-in</p>
<p><html:errors/></p>
</html:form>
</logic:notEqual>
<logic:equal name="allStatus" property="size" value="0"> <i><br />
<p>No Status Messages available</p>
</i> </logic:equal>
