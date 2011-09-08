package hnu.admin.form;

import hnu.helper.DataBaseConnection;
import hnu.helper.LoginChecker;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.DiskFile;

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

public class AddStaffForm extends org.apache.struts.action.ActionForm {
    private String name;
    private String first;
    private String login;
    private String pass1;
    private String pass2;
    private DiskFile pic;

    /**
     * Reset-Method for Bean
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        name = "";
        first = "";
        login = "";
        pass1 = "";
        pass2 = "";
	pic = null;
    }

    /**
     * Validate-Method for Bean
     * <ul>Validates if:
     * <li>logged in</li>
     * <li>name was entered</li>
     * <li>first name was entered</li>
     * <li>loginname was entered</li>
     * <li>loginname already exists</li>
     * <li>passwords match and are acceptable</li>
     * </ul>
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (!LoginChecker.isAdminLoggedIn(request)) {
            errors.add("login", new ActionError("error.login.not"));
        }

        if ((name == null) || name.equals("")) {
            errors.add("name", new ActionError("error.admin.staff.name"));
        }

        if ((first == null) || first.equals("")) {
            errors.add("first", new ActionError("error.admin.staff.first"));
        }

        if ((login == null) || login.equals("")) {
            errors.add("login", new ActionError("error.admin.staff.login"));
        } else {
            if (login.length() < 5) {
                errors.add("login", new ActionError("error.admin.staff.login.length"));
            } else {
                if (DataBaseConnection.checkResult("SELECT sId FROM TStaff WHERE sLogin='" + login + "'")
		    || DataBaseConnection.checkResult("SELECT uId FROM TUser WHERE uLogin='" + login + "'")
		    || DataBaseConnection.checkResult("SELECT aId FROM TAdmin WHERE aLogin='" + login + "'")) {
                    errors.add("login", new ActionError("error.admin.staff.login.exists"));
                }
            }
        }

        if ((pass1 == null) || (pass1.length() < 6)) {
            errors.add("pass1", new ActionError("error.admin.staff.password.length"));
        }

        if ((pass2 == null) || !pass2.equals(pass1)) {
            errors.add("pass2", new ActionError("error.admin.staff.password.match"));
        }

        return errors;
    }

    /**
     * Returns Loginname
     * @return Loginname
     */
    public String getName() {
        return name;
    }

    /**
     * Sets Password-Repetition
     * @param String Password-Repetition
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns Loginname
     * @return Loginname
     */
    public String getFirst() {
        return first;
    }

    /**
     * Sets Password-Repetition
     * @param String Password-Repetition
     */
    public void setFirst(String first) {
        this.first = first;
    }

    /**
     * Returns Loginname
     * @return Loginname
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets Loginname
     * @param String Loginname
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Returns Password
     * @return Password
     */
    public String getPass1() {
        return pass1;
    }

    /**
     * Sets Password
     * @param String Password
     */
    public void setPass1(String pass1) {
        this.pass1 = pass1;
    }

    /**
     * Returns UploadFile
     * @return UploadFile
     */
    public DiskFile getPic() {
        return pic;
    }

    /**
     * Sets UploadFile
     * @param String UploadFile
     */
    public void setPic(DiskFile pic) {
        this.pic = pic;
    }

    /**
     * Sets Password-Repetition
     * @param String Password-Repetition
     */
    public void setPass2(String pass2) {
        this.pass2 = pass2;
    }

    /**
     * Returns Password-Repetition
     * @return Password-Repetition
     */
    public String getPass2() {
        return pass2;
    }
}
