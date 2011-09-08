<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<table class="plain">
  <html:form action="/admin/addstaff" enctype="multipart/form-data" method="post">
  <tr>
    <td>Name *</td>
    <td><html:text property="name"/></td>
    <td><html:errors property="name"/></td>
  </tr>
  <tr>
    <td>Firstname *</td>
    <td><html:text property="first"/></td>
    <td><html:errors property="first"/></td>
  </tr>
  <tr>
    <td>Loginname *</td>
    <td><html:text property="login"/></td>
    <td><html:errors property="login"/></td>
  </tr>
  <tr>
    <td>Password *</td>
    <td><html:password property="pass1"/></td>
    <td><html:errors property="pass1"/></td>
  </tr>
  <tr>
    <td>Repeat Password *</td>
    <td><html:password property="pass2"/></td>
    <td><html:errors property="pass2"/></td>
  </tr>
  <tr>
    <td>Picture</td>
    <td colspan="2"><html:file property="pic"/> <html:errors property="pic"/></td>
  </tr>
</table>
<p>* means required</p>
<p><html:submit property="ok" value="Add Staff" /></p>
<p><html:errors property="dberror" /></p>
</html:form>
