<%@ page import="javax.naming.InitialContext,
                 javax.sql.DataSource,
                 javax.naming.Context,
                 java.util.Hashtable,
                 javax.naming.NamingException,
                 java.sql.*,
                 java.util.Enumeration,
                 java.util.StringTokenizer" %>

<%@ page contentType="text/html; charset=UTF-8" %>

<!--
If you want to use this utility to double check your database settings *before* setting
up Confluence you need to set it up within a separate web application in your app. server. -->
<html>
<head>
    <title>Atlassian Database Check Utility</title>

</head>

<body>
    <h3>Atlassian Database Check Utility.</h3>
    <%
        String operation = request.getParameter("operation");
        String driverStr = request.getParameter("driver");
        String jdbcURL = request.getParameter("jdbcURL");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String initialContext = request.getParameter("initialContext");
        String provider = request.getParameter("provider");
        String ds = request.getParameter("ds");

        if (operation == null)
        {
            out.println("Please choose an operation below: ");
        }
        else if ("reset".equals(operation))
        {
            operation = null;
            driverStr = null;
            jdbcURL = null;
            username = null;
            password = null;
            initialContext = null;
            provider = null;
            ds = null;
        }
        else if (operation != null && "standard".equals(operation))
        {
            out.println("Connecting to database via JDBC URL:");

            try
            {
                out.println("<ol>");
                out.print("<li>Found driver (" + driverStr + ") on classpath? ");
                Driver driver = null;
                try
                {
                    driver = (Driver) Class.forName(driverStr).newInstance();

                    if (driver == null)
                        throw new ClassNotFoundException("Could not find " + driverStr + " on classpath.");

                    out.println("<strong class=\"good\">ok</strong>");
                }
                catch (java.lang.ClassNotFoundException e)
                {
                    out.println("<em class=\"bad\">Driver failed to load. Check your classpath: " + e + "</em>");
                }

                out.print("<li>Open a connection to the database via JDBC (" + jdbcURL + ")? ");

                try
                {
                    Class.forName(driverStr);
                    Connection conn = DriverManager.getConnection(jdbcURL, username, password);
                    Statement st = conn.createStatement();
                    st.executeQuery("SELECT 1;");

                    out.println("<strong class=\"good\">ok</strong>");
                    conn.close();
                }
                catch (SQLException e)
                {
                    out.println("<em class=\"bad\">Connection failed to open on the JDBC URL: " + e + "</em>");
                }
            }
            catch(Exception e)
            {
                out.println(e);
            }
                out.println("</ol>");
        }
        else if (operation != null && "datasource".equals(operation))
        {
            out.println("Connecting to database via Datasource: ");

            out.println("<ol>");

            try
            {
                DataSource dsrc = null;
                Context ctx = null;

                if ((initialContext != null || provider != null) && (!"".equals(initialContext) || !"".equals(provider)))
                {
                    Hashtable ht = new Hashtable();
                    ht.put(Context.INITIAL_CONTEXT_FACTORY, initialContext);
                    ht.put(Context.PROVIDER_URL, provider);

                    try
                    {
                        out.print("<li> Build InitialContext with INITIAL_CONTEXT_FACTORY (" + initialContext + ") and PROVIDER_URL ("+ provider + ")?" );
                        ctx = new InitialContext(ht);
                        out.println("<strong class=\"good\">ok</strong>");
                    }
                    catch (NamingException e)
                    {
                        out.println("<em class=\"bad\">Couldn't build InitialContext. Check your JNDI configuration: " + e + "</em>");
                    }
                }
                else
                {
                    try
                    {
                        out.print("<li> Gain InitialContext?: " );
                        ctx = new InitialContext();
                        out.println("<strong class=\"good\">ok</strong>");
                    }
                    catch (NamingException e)
                    {
                        out.println("<em class=\"bad\">Couldn't build InitialContext. Check your JNDI configuration: " + e + "</em>");
                    }
                }

                try
                {
                    out.print("<li> Locate Datasource (" + ds + ") in InitialContext?: " );
                    dsrc = (DataSource) ctx.lookup(ds); //let the user specify the exact JNDI name themselves

                    if (dsrc == null)
                        throw new NamingException("Could not locate " + ds);

                    out.println("<strong class=\"good\">ok</strong>");
                }
                catch (NamingException e)
                {
                    out.println("<em class=\"bad\">Couldn't locate Datasource (" + ds + "). Check your JNDI configuration: " + e + "</em>");
                }
                catch (ClassCastException e)
                {
                    out.println("<em class=\"bad\">Couldn't locate Datasource (" + ds + "). Whatever we found, it wasn't a Datasource: " + e + "</em>");
                }

                out.print("<li> Open a connection via Datasource (" + ds + ")? " );

                try
                {
                    Connection conn = dsrc.getConnection();
                    Statement st = conn.createStatement();
                    st.executeQuery("SELECT 1;");
                    conn.close();

                    out.println("<strong class=\"good\">ok</strong>");
                }
                catch (SQLException e)
                {
                    out.println("<em class=\"bad\">Couldn't open a connection on Datasource (" + ds + "): " + e + "</em>");
                }
                catch (NullPointerException e)
                {
                    out.println("<em class=\"bad\">Couldn't open a connection on Datasource (" + ds + "): " + e + "</em>");
                }

                out.println("</ol>");
            }
            catch (NullPointerException e)
            {
                out.println(e);
            }
            catch (Exception e)
            {
                out.println(e);
            }
        }


        if (driverStr == null)
            driverStr = "";

        if (jdbcURL == null)
            jdbcURL = "";

        if (username == null)
            username = "";

        if (password == null)
            password = "";

        if (ds == null)
            ds = "";

        if (initialContext == null)
            initialContext = "";

        if (provider == null)
            provider = "";

    %>
    <p/>
    <p/>
        <form name="dbform" method="post" action="testdatabase.jsp">
            <input type="hidden" name="operation" value="standard" />
            <table border="0" width="100%">
                <tr>
                    <th colspan=2 align="left">Test a Standard Database Connection:</th>
                </tr>
                <tr>
                    <td width="20%">
                        Driver Class Name: &nbsp;
                     </td>
                     <td width="80%">
                        <input type="text" name="driver" value="<%=driverStr %>" size="40"/>
                    </td>
                </tr>
                <tr>
                     <td>
                         JDBC URL: &nbsp;
                     </td>
                     <td>
                         <input type="text" name="jdbcURL" value="<%=jdbcURL %>" size="60"/>
                     </td>
                 </tr>
                <tr>
                    <td>
                        DB username: &nbsp;
                     </td>
                     <td>
                        <input type="text" name="username" value="<%=username %>" size="40"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        DB password: &nbsp;
                     </td>
                     <td>
                        <input type="text" name="password" value="<%=password %>" size="40"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type="submit" value="Test JDBC URL" />
                    </td>
                </tr>
          </table>
        </form>
    <p/>
    <p/>
        <form method="POST" action="testdatabase.jsp">
            <input type="hidden" name="operation" value="datasource" />
            <table border="0" width="100%">
                <tr>
                    <th colspan=2 align="left">Test Connecting through a Datasource:</th>
                </tr>
                <tr>
                    <td width="20%">Datasource Name: &nbsp;</td>
                    <td width="80%">
                        <input type="text" name="ds" size="40" value="<%=ds%>">
                    </td>
                </tr>
                <tr>
                    <td>JNDI Initial Context: &nbsp;</td>
                    <td>
                        <input type="text" name="initialContext" size="40" value="<%=initialContext%>">
                    </td>
                </tr>
                <tr>
                    <td>Provider URL: &nbsp;</td>
                    <td>
                        <input type="text" name="provider" size="40" value="<%=provider%>">
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type="submit" value="Test Datasource"></td>
                    <td>
                </tr>
                <tr>
                    <td>
                        Hint:
                    </td>
                    <td>
                        If either the Initial Context or Provider URL is present, I will try to build a JNDI context based on your data. <br/>
                        Leave both fields blank to connect to a default Context.
                    <td>
                </tr>

            </table>
        </form>

    <p/>
        <form method="post" action="testdatabase.jsp">
            <input type="hidden" name="operation" value="reset" />
            <input type="submit" value="Reset Everything" />
        </form>

    <p>

    <p/>

        <form method="post" action="testdatabase.jsp">
            <input type="hidden" name="operation" value="showClasspath" />

            <input type="submit" value="Show System Classpath">
        </form>

    <%
        if (operation != null && "showClasspath".equals(operation))
        {
    %>
    Current view of System Classpath:
    <ul>
    <%
        String classpathrep = System.getProperty("java.class.path");
        StringTokenizer st = new StringTokenizer(classpathrep, ":;");

        while (st.hasMoreTokens())
            out.println("<li> " + st.nextToken());
    %>
    </ul>

    <%
        }
    %>
    <p>
        Drivers currently visible on the classpath system. If you do not see your driver here it does not mean you cannot access it. It might need to be explicitly loaded by the JVM.
    </p>

    <ul>
    <%
        Enumeration enu = DriverManager.getDrivers();

        while (enu.hasMoreElements())
        {
            Object o = enu.nextElement();
    
            out.println("<li> " + o.getClass().getName() );
        }
    %>
    </ul>



    <p>Example MySQL JDBC URL: <em class="good"><samp>jdbc:mysql://localhost/test</samp></em></p>
    <p>Example Postgres JDBC URL: <em class="good"><samp>jdbc:postgres://localhost:5432/test</samp></em></p>
</body>
</html>