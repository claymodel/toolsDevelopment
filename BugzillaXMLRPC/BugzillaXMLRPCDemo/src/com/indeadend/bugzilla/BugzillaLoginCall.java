/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.indeadend.bugzilla;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlrpc.XmlRpcException;

/**
 *
 * @author joshis
 */
public class BugzillaLoginCall extends BugzillaAbstractRPCCall {

    /**
     * Create a Bugzilla login call instance and set parameters
     */
    public BugzillaLoginCall(String username, String password) {
        super("User.login");
        setParameter("login", username);
        setParameter("password", password);
    }

    /**
     * Perform the login action and set the login cookies
     * @returns True if login is successful, false otherwise. The method sets Bugzilla login cookies.
     */
    public boolean login() {
        Map result = null;
        try {
            // the result should contain one item with ID of logged in user
            result = this.execute();
        } catch (XmlRpcException ex) {
            Logger.getLogger(BugzillaLoginCall.class.getName()).log(Level.SEVERE, null, ex);
        }
        return !(result == null || result.isEmpty());
    }

}