package hnu.helper.admin;

import hnu.helper.DataBaseConnection;
import hnu.helper.PasswordHash;
import hnu.helper.SendMail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * Copyright (C) 2002-2003 Martin Maier <martin.maier@fh-joanneum.at>
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

/**
 * @author Martin Maier
 */

public class PasswordResetter {
    /**
    * Bean constructor
    */
    public PasswordResetter() {
    }

    /**
     * Method sets all selected passwords to the MD5-Hashes from a string.
    */
    private void setPasswordsToValue(String value, String updateSQL,
        String[] stringArray, boolean mail) {
        if (stringArray != null) {
            ResultSet rs = null;

            DataBaseConnection.execute(updateSQL + " '"
                + PasswordHash.getSHAString(value) + "'");

            if (mail) {
                for (int i = 0; i < stringArray.length; i++) {
                    sendMailtoUser(stringArray[i], value);
                }
            }
        }
    }

    /**
     * Method sets all selected passwords to the MD5-Hashes from the corresponding loginname.
    */
    private void setPasswordsToLogin(String loginnameSQL, String updateSQL,
        String[] stringArray, String identifier, boolean mail) {
        if (stringArray != null) {
            // Tomcat seems to have problems with using a lot of connections from
            // pool within a short time (i.e. for-loop).
            // TomCat cannot release the connections that quickly they are required.
            // That is why the getConnection-Method is used here, to avoid this
            // problem. So just only one connection is used.
            DataBaseConnection db = new DataBaseConnection();
            Connection conn = db.getDBConnection();
            Statement stmt = null;

            try {
                stmt = conn.createStatement();
            } catch (SQLException ex) {
                try {
                    conn.close();

                    return;
                } catch (SQLException sqlEx) {
                    return;
                }
            }

            ResultSet rs = null;
            String login = null;

            for (int i = 0; i < stringArray.length; i++) {
                try {
                    rs = stmt.executeQuery(loginnameSQL + " '" + stringArray[i]
                            + "'");
                    login = rs.getString(1);
                } catch (SQLException ex) {
                    login = "";
                }

                String sql = updateSQL + " '"
                    + PasswordHash.getSHAString(login) + "' WHERE "
                    + identifier + "='" + stringArray[i] + "'";

                try {
                    stmt.execute(sql);

                    if (mail) {
                        sendMailtoUser(stringArray[i], login);
                    }
                } catch (SQLException ex) {
                    try {
                        conn.close();

                        return;
                    } catch (SQLException sqlEx) {
                        return;
                    }
                }
            }

            try {
                stmt.close();
            } catch (SQLException ex) {
            }

            try {
                conn.close();
            } catch (SQLException ex) {
            }
        }
    }

    private void sendMailtoUser(String uId, String password) {
        DataBaseConnection db = new DataBaseConnection();
        ResultSet rs = db.getRSfromStatement(
                "SELECT uMail, uName, uFirst FROM TUser WHERE uId='" + uId
                + "'");
        String recipient = "";

        try {
            while (rs.next()) {
                recipient = rs.getString("uMail");
            }
        } catch (SQLException ex) {
            db.closeResultSet(rs);

            return;
        }

        db.closeResultSet(rs);

        String subject = "HNU :: Your Password has been changed";
        String body = "The password for your account has been changed to \n\n"
            + password + "\n\n-- \nHNU TroubleTicketSystem";

        SendMail mailSender = new SendMail();

        mailSender.sendMail(recipient, subject, body);
    }

    /**
     * Method sets all selected admin-passwords to the MD5-Hashes from the corresponding loginname.
    */
    public void setAdminPasswordToLoginname(String[] aId) {
        setPasswordsToLogin("SELECT aLogin FROM TAdmin WHERE aId =",
            "UPDATE TAdmin SET aPass =", aId, "aId", false);
    }

    /**
     * Method sets all selected user-passwords to the MD5-Hashes from the corresponding loginname.
    */
    public void setUsersPasswordToLoginname(String[] uId, boolean mail) {
        setPasswordsToLogin("SELECT uLogin FROM TUser WHERE uId =",
            "UPDATE TUser SET uPass =", uId, "uId", mail);
    }

    /**
     * Method sets all selected staff-passwords to the MD5-Hashes from the corresponding loginname.
    */
    public void setStaffPasswordToLoginname(String[] sId) {
        setPasswordsToLogin("SELECT sLogin FROM TStaff WHERE sId =",
            "UPDATE TStaff SET sPass =", sId, "sId", false);
    }

    /**
     * Method sets all selected admin-passwords to the MD5-Hashes from a string value.
    */
    public void setAdminPasswordToValue(String value, String[] aId) {
        setPasswordsToValue(value, "UPDATE TAdmin SET aPass =", aId, false);
    }

    /**
     * Method sets all selected user-passwords to the MD5-Hashes from a string value.
    */
    public void setUsersPasswordToValue(String value, String[] uId, boolean mail) {
        setPasswordsToValue(value, "UPDATE TUser SET uPass =", uId, mail);
    }

    /**
     * Method sets all selected staff-passwords to the MD5-Hashes from a string value.
    */
    public void setStaffPasswordToValue(String value, String[] sId) {
        setPasswordsToValue(value, "UPDATE TStaff SET sPass =", sId, false);
    }
}
