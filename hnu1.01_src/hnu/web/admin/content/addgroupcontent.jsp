<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:form action="/admin/addgroup">
<table class="plain">
  <tr>
    <td>Groupname</td>
    <td><html:text property="name"/></td>
    <td><html:errors property="name"/></td>
  </tr>
</table>
<p><html:submit property="ok" value="Add Group" /></p>
</html:form>
<p><html:errors property="dberror" /></p>
