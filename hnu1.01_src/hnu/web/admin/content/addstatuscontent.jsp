<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html:form action="/admin/addstatus">
<table class="plain">
  <tr>
    <td>Statusname <html:text property="text" /> </td>
    <td><html:errors property="text" /></td>
  </tr>
</table>
<p><html:submit property="ok" value="Add Status"/></p>
</html:form>
<p><html:errors property="dberror" /></p>
