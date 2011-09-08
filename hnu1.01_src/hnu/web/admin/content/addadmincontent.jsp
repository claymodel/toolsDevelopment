<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>

<table class="plain">
  <html:form action="/admin/addadmin">
  <tr>
    <td>Loginname *</td>
    <td><html:text property="loginname"/></td>
    <td><html:errors property="loginname"/></td>
  </tr>
  <tr>
    <td>Password *</td>
    <td><html:password property="password1"/></td>
    <td><html:errors property="password1"/></td>
  </tr>
  <tr>
    <td>Repeat Password *</td>
    <td><html:password property="password2"/></td>
    <td><html:errors property="password2"/></td>
  </tr>
</table>
<p>* means required</p>
<p><html:submit property="ok" value="Add Admin"/></p>
</html:form>
<p><html:errors property="dberror" /></p>
