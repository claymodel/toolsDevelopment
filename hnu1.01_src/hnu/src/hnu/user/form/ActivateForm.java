package hnu.user.form;

import hnu.helper.DataBaseConnection;
import hnu.helper.PasswordHash;

import java.sql.ResultSet;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;


/*
 * Copyright (C) 2002-2003 Peter Ortner <peter.ortner@fh-joanneum.at>
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
 * @author Peter Ortner
 */

public class ActivateForm extends org.apache.struts.action.ActionForm {
    private String pass;
    private String id;
    private String login;

    /**
     * Validate-Method for Bean
     * <ul>Validates if:
     * <li>password is entered and correct</li>
     * <li>no exception occurs</li>
     * </ul>
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if ((pass == null) && pass.trim().equals("")) {
            errors.add("pass", new ActionError("error.user.activate.pass"));
        }

        String sql = "SELECT uId FROM TUser WHERE uId=" + id + " AND uPass='" + PasswordHash.getSHAString(pass) + "'";

        DataBaseConnection db = new DataBaseConnection();
        ResultSet res = null;

        try {
            res = db.getRSfromStatement(sql);

            if (!res.next()) {
                pass = "";
                errors.add("pass", new ActionError("error.user.passOldFalse"));
            }
        } catch (Exception ex) {
            errors.add("db", new ActionError("error.database.general"));
        }

        db.closeResultSet(res);

        return errors;
    }

    /**
     * Returns pass
     * @return pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * Sets pass
     * @parm String pass
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * Sets id
     * @parm String id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns id
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets login
     * @parm String login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Returns login
     * @return login
     */
    public String getLogin() {
        return login;
    }
}
