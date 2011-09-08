package com.indeadend.bugzilla;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcClientException;
import org.apache.xmlrpc.client.XmlRpcSunHttpTransport;
import org.apache.xmlrpc.client.XmlRpcTransport;
import org.apache.xmlrpc.client.XmlRpcTransportFactory;

/**
 * @author joshis_tweets
 */
public class BugzillaAbstractRPCCall {

    private static XmlRpcClient client = null;

    // Very simple cookie storage
    private final static LinkedHashMap<String, String> cookies = new LinkedHashMap<String, String>();

    private HashMap<String, Object> parameters = new HashMap<String, Object>();
    private String command;

    /**
     * Creates a new instance of the Bugzilla XML-RPC command executor for a specific command
     * @param command A remote method associated with this instance of RPC call executor
     */
    public BugzillaAbstractRPCCall(String command) {
        synchronized (this) {
            this.command = command;
            if (client == null) { // assure the initialization is done only once
                client = new XmlRpcClient();
                XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
                try {
                    config.setServerURL(new URL(BugzillaConstants.BZ_PATH));
                } catch (MalformedURLException ex) {
                    Logger.getLogger(BugzillaAbstractRPCCall.class.getName()).log(Level.SEVERE, null, ex);
                }
                XmlRpcTransportFactory factory = new XmlRpcTransportFactory() {

                    public XmlRpcTransport getTransport() {
                        return new XmlRpcSunHttpTransport(client) {

                            private URLConnection conn;

                            @Override
                            protected URLConnection newURLConnection(URL pURL) throws IOException {
                                conn = super.newURLConnection(pURL);
                                return conn;
                            }

                            @Override
                            protected void initHttpHeaders(XmlRpcRequest pRequest) throws XmlRpcClientException {
                                super.initHttpHeaders(pRequest);
                                setCookies(conn);
                            }

                            @Override
                            protected void close() throws XmlRpcClientException {
                                getCookies(conn);
                            }

                            private void setCookies(URLConnection pConn) {
                                String cookieString = "";
                                for (String cookieName : cookies.keySet()) {
                                    cookieString += "; " + cookieName + "=" + cookies.get(cookieName);
                                }
                                if (cookieString.length() > 2) {
                                    setRequestHeader("Cookie", cookieString.substring(2));
                                }
                            }

                            private void getCookies(URLConnection pConn) {
                                String headerName = null;
                                for (int i = 1; (headerName = pConn.getHeaderFieldKey(i)) != null; i++) {
                                    if (headerName.equals("Set-Cookie")) {
                                        String cookie = pConn.getHeaderField(i);
                                        cookie = cookie.substring(0, cookie.indexOf(";"));
                                        String cookieName = cookie.substring(0, cookie.indexOf("="));
                                        String cookieValue = cookie.substring(cookie.indexOf("=") + 1, cookie.length());
                                        cookies.put(cookieName, cookieValue);
                                    }
                                }
                            }
                        };
                    }
                };
                client.setTransportFactory(factory);
                client.setConfig(config);
            }
        }
    }

    /**
     * Get the parameters of this call, that were set using setParameter method
     * @return Array with a parameter hashmap
     */
    protected Object[] getParameters() {
        return new Object[] {parameters};
    }

    /**
     * Set parameter to a given value
     * @param name Name of the parameter to be set
     * @param value A value of the parameter to be set
     * @return Previous value of the parameter, if it was set already.
     */
    public Object setParameter(String name, Object value) {
        return this.parameters.put(name, value);
    }

    /**
     * Executes the XML-RPC call to Bugzilla instance and returns a map with result
     * @return A map with response
     * @throws XmlRpcException
     */
    public Map execute() throws XmlRpcException {
        return (Map) client.execute(command, this.getParameters());
    }
}