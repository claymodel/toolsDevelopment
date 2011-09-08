/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bugzillaxmlrpcdemo;

import com.indeadend.bugzilla.BugzillaVersionCall;

/**
 *
 * @author petrdvorak
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(BugzillaVersionCall.getVersion());
    }

}
