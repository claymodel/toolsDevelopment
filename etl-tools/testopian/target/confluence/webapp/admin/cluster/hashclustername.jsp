<%@ page import="com.atlassian.spring.container.ContainerManager" %>
<%@ page import="com.atlassian.confluence.cluster.NameResolver" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head><title>Generate Multicast IP address</title></head>
  <body>

  <h1>Generate multicast IP address</h1>

  <form action="" method="get">

      <label>Cluster name: </label><input name="clusterName"type="text" value="" >
      
      <button type="submit">Generate</button>
  </form>

  <%
  if (request.getParameter("clusterName") != null) {
      NameResolver nameResolver = (NameResolver) ContainerManager.getComponent("clusterManager");
  %>

  <h2>Generated address for cluster name "<%=request.getParameter("clusterName")%>"</h2>

  <div><%=nameResolver.resolveName(request.getParameter("clusterName")).getHostAddress()%></div>

  <% } %>
  </body>
</html>