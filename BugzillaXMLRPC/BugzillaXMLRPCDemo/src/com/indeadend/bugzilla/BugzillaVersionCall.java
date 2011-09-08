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
 * @author petrdvorak
 */
public class BugzillaVersionCall extends BugzillaAbstractRPCCall {

    public BugzillaVersionCall() {
        super("Bugzilla.version");
    }

    public static String getVersion() {
        Map result = null;
        try {
            // the result should contain one item with ID of logged in user
            result = new BugzillaVersionCall().execute();
        } catch (XmlRpcException ex) {
            Logger.getLogger(BugzillaLoginCall.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (String) result.get("version");
    }

}
