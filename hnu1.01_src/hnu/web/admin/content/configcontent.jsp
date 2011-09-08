<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="hnu.admin.form.ConfigForm" %>
<%@ page import="hnu.helper.admin.ConfigBean" %>
<jsp:useBean id="configBean" class="hnu.helper.admin.ConfigBean" scope="request" />
<jsp:useBean id="configForm" class="hnu.admin.form.ConfigForm" scope="request" />

<html:form action="/admin/updateconfig.do">

<table class="plain">
<tr><td><p><table width="100%">
  <tr>
    <th  colspan="2">Cascading StyleSheet</th>
  </tr>
  <tr>
  <td>
  Select Cascading-StyleShee-File
        <logic:present name="configBean" property="css">
        <html:select property="css">
	<option selected="selected" value="<bean:write name="configForm" property="css" />">
	Active CSS: <bean:write name="configForm" property="css" />
	</option>
	<logic:iterate id="configBeanIt" name="configBean" property="css">
	  <option value="<bean:write name="configBeanIt" />">
	  <bean:write name="configBeanIt" />
	  </option>
	</logic:iterate>
        </html:select>
	</logic:present>
        <logic:notPresent name="configBean" property="css">
	<p><em>No StyleSheets found in path /styles</em></p>
	</logic:notPresent>
  </td>
    <td><html:errors property="css"/></td>
  </tr>
</table></p>

</td></tr>

<tr><td><p><table width="100%">
  <tr>
    <th colspan="2">Loginmode</th>
  </tr>
  <tr>
    <td><html:radio property="loginmode" value="0" /> User can register</td>
  </tr>
  <tr>
    <td><html:radio property="loginmode" value="1" /> User are not allowed to
      register</td>
    <td><html:errors property="loginmode"/></td>
  </tr>
</table></p>
</td></tr>

<tr><td><p><table width="100%">
  <tr>
    <th colspan="2">DateFormat<br />Current: <em><bean:write name="configBean" property="dateExample" /></em></th>
  </tr>
  <tr>
    <td><html:radio property="time" value="0" /> dd.mm.yyyy hh:mm</td>
  </tr>
  <tr>
    <td><html:radio property="time" value="1" /> yyyy-mm-dd hh:mm</td>
  </tr>
  <tr>
    <td><html:radio property="time" value="2" /> weekday, dd.mm.yyyy hh:mm</td>
  </tr>
  <tr>
    <td><html:radio property="time" value="3" /> weekday, yyyy-mm-dd hh:mm</td>
  </tr>
  <tr>
    <td><html:radio property="time" value="4" /> dd.mm.yyyy</td>
  </tr>
  <tr>
    <td><html:radio property="time" value="5" /> yyyy-mm-dd</td>
    <td><html:errors property="time"/></td>
  </tr>
</table></p>
</td></tr>

<tr><td><p><table width="100%">
  <tr>
    <th colspan="2">Title for this Site</th>
  </tr>
  <tr>
    <td align="center"><html:textarea property="title" cols="50" rows="3" /></td>
    <td><html:errors property="titles"/></td>
  </tr>
</table></p>
</td></tr>

<tr><td><p><table width="100%">
  <tr>
    <th colspan="2">Banner-HTML-Code</th>
  </tr>
  <tr>
    <td><html:textarea property="banner" cols="70" rows="7" /></td>
    <td><html:errors property="banner"/></td>
  </tr>

</table></p>

</td></tr>

</table>

<p><html:submit property="ok" value="Change Configuration" /></p>
</html:form>
<p><html:errors/></p>
