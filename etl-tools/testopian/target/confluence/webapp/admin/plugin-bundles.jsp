<%@ page import="com.atlassian.plugin.Plugin" %>
<%@ page import="com.atlassian.spring.container.ContainerManager" %>
<%@ page import="org.osgi.framework.Bundle" %>
<%@ page import="org.osgi.framework.ServiceReference" %>
<%@ page import="com.atlassian.plugin.PluginAccessor" %>
<%@ page import="com.atlassian.plugin.osgi.container.OsgiContainerManager" %>
<html>
<head>
    <meta name="decorator" content="atl.general"/>
</head>
    <h2>Plugins</h2>
    <ul>
<%
    PluginAccessor pluginAccessor = (PluginAccessor) ContainerManager.getInstance().getContainerContext().getComponent("pluginAccessor");
    OsgiContainerManager osgiContainerManager = (OsgiContainerManager) ContainerManager.getInstance().getContainerContext().getComponent("osgiContainerManager");
%>
<% for (Plugin plugin : pluginAccessor.getPlugins()) {
    Plugin p = plugin;
%>
        <li><%=p.getKey()%></li>
<% } %>
    </ul>

    <h2>Bundles</h2>
    <table border="1">
        <tr>
            <th>Name</th>
            <th>Version</th>
            <th>State</th>
            <th>Services</th>
        </tr>

<% for (Bundle bundle : osgiContainerManager.getBundles()) {
%>
        <tr>
            <td>
                <%=bundle.getSymbolicName()%>
            </td>
            <td>
                <%=bundle.getHeaders().get("Bundle-Version")%>
            </td>
            <td>
                <%
                    String state = "N/A";
                    switch (bundle.getState()) {
                        case Bundle.INSTALLED : state = "Installed"; break;
                        case Bundle.ACTIVE : state = "Active"; break;
                        case Bundle.RESOLVED : state = "Resolved"; break;
                    }
                %>
                <%=state%>
            </td>
            <td>
                &nbsp;
            <%  if (bundle.getRegisteredServices() != null && bundle.getRegisteredServices().length > 0) { %>
                <table border="1" width="100%">
             <tr>
                 <th>Interfaces</th>
                 <th>Properties</th>
                 <th>Using Bundles</th>
             </tr>

            <%  for (ServiceReference service : bundle.getRegisteredServices()) { %>
                    <tr>
                        <td>
                            <% String[] infs = (String[]) service.getProperty("objectClass");
                                for (String inf : infs) {%>
                                <li><%=inf%></li>
                            <% } %>

                        </td>
                        <td>
                            <ul>
                            <% for (String key : service.getPropertyKeys()) {
                                if (!"objectClass".equals(key)) { %>
                                <li><%=key%> - <%=service.getProperty(key)%></li>
                            <% }} %>
                            </ul>
                        </td>
                        <td> &nbsp;
                            <ul>
                            <% if (service.getUsingBundles() != null) {
                                    for (Bundle user : service.getUsingBundles()) {%>

                                    <li><%=user.getSymbolicName()%></li>
                            <%      }
                                }%>
                            </ul>
                        </td>
                    </tr>

                <%  } %>
                </table>
                <% } %>
            </td>
        </tr>

<% } %>
        </table>

</html>