package hnu.admin.form;

import hnu.helper.DataBaseConnection;
import hnu.helper.LoginChecker;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

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

public class AddAdminForm extends org.apache.struts.action.ActionForm {
    private String loginname;
    private String password1;
    private String password2;

    /**
     * Reset-Method for Bean
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        loginname = "";
        password1 = "";
        password2 = "";
    }

    /**
     * Validate-Method for Bean
     * <ul>Validates if:
     * <li>logged in</li>
     * <li>length of loginname is okay</li>
     * <li>loginname already exists</li>
     * <li>passwords match and are acceptable</li>
     * </ul>
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (!LoginChecker.isAdminLoggedIn(request)) {
            errors.add("login", new ActionError("error.login.not"));
        }

        if (loginname.length() < 5) {
            errors.add("loginname", new ActionError("error.admin.admin.login.length"));
        } else {
            if (DataBaseConnection.checkResult("SELECT sId FROM TStaff WHERE sLogin='" + loginname + "'") || DataBaseConnection.checkResult("SELECT uId FROM TUser WHERE uLogin='" + loginname + "'") || DataBaseConnection.checkResult("SELECT aId FROM TAdmin WHERE aLogin='" + loginname + "'")) {
                errors.add("login", new ActionError("error.admin.admin.login.exists"));
            }
        }

        if (!password1.equals(password2)) {
            errors.add("password1", new ActionError("error.admin.admin.password.match"));
        } else {
            if (password1.length() < 6) {
                errors.add("password1", new ActionError("error.admin.admin.password.length"));
            }
        }

        return errors;
    }

    /**
     * Returns Loginname
     * @return Loginname
     */
    public String getLoginname() {
        return loginname;
    }

    /**
     * Sets Loginname
     * @param String Loginname
     */
    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    /**
     * Sets Password
     * @param String Password
     */
    public String getPassword1() {
        return password1;
    }

    /**
     * Returns Password
     * @return Password
     */
    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    /**
     * Returns Password-Repetition
     * @return Password-Repetition
     */
    public String getPassword2() {
        return password2;
    }

    /**
     * Sets Password-Repetition
     * @param String Password-Repetition
     */
    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}
