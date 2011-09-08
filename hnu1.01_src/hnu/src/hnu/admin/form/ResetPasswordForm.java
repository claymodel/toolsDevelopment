package hnu.admin.form;

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

public class ResetPasswordForm extends org.apache.struts.action.ActionForm {
    private String[] idAdmin;
    private String[] idStaff;
    private String[] idUser;
    private String textfield;
    private String loginname;
    private String newPassword1;
    private String newPassword2;
    private String usermail;

    /**
     * Reset-Method for Bean
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        idAdmin = null;
        idStaff = null;
        idUser = null;
        textfield = null;
        loginname = "";
        newPassword1 = "";
        newPassword2 = "";
	usermail = "";
    }

    /**
     * Validate-Method for Bean
     * <ul>Validates if:
     * <li>logged in</li>
     * <li>at least one account was selected</li>
     * <li>passwords match and are acceptable</li>
     * </ul>
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (!LoginChecker.isAdminLoggedIn(request)) {
            errors.add("login", new ActionError("error.login.not"));
        }

        if ((idStaff == null) || (idStaff.length == 0)) {
            if ((idUser == null) || (idUser.length == 0)) {
                if ((idAdmin == null) || (idAdmin.length == 0)) {
                    errors.add("select", new ActionError("error.admin.user.delete.missing"));
                }
            }
        }

        if (textfield != null) {
            if (newPassword1.length() < 6) {
                errors.add("newpassword1", new ActionError("error.admin.staff.password.length"));
            } else if (!newPassword1.equals(newPassword2)) {
                errors.add("newpassword2", new ActionError("error.admin.staff.password.match"));
            }
        }

        return errors;
    }

    /**
     * Sets Array of Admin-IDs
     * @param String[]
     */
    public void setIdAdmin(String[] idAdmin) {
        this.idAdmin = idAdmin;
    }

    /**
     * Returns Array of Admin-IDs
     * @return String[] Array of Admin-IDs
     */
    public String[] getIdAdmin() {
        return idAdmin;
    }

    /**
     * Sets Array of Staff-IDs
     * @param String[] Array of Staff-IDs
     */
    public void setIdStaff(String[] idStaff) {
        this.idStaff = idStaff;
    }

    /**
     * Returns Array of Staff-IDs
     * @return String[] Array of Staff-IDs
     */
    public String[] getIdStaff() {
        return idStaff;
    }

    /**
     * Sets Array of User-IDs
     * @param String[] Array of User-IDs
     */
    public void setIdUser(String[] idUser) {
        this.idUser = idUser;
    }

    /**
     * Returns Array of User-IDs
     * @return String[] Array of User-IDs
     */
    public String[] getIdUser() {
        return idUser;
    }

    /**
     * Sets Loginname-Flag
     * @param String Loginname-Flag
     */
    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    /**
     * Returns Loginname-Flag
     * @return String Loginname-Flag
     */
    public String getLoginname() {
        return loginname;
    }

    /**
     * Sets textfield-Flag
     * @param String textfield-Flag
     */
    public void setTextfield(String textfield) {
        this.textfield = textfield;
    }

    /**
     * Returns textfield-Flag
     * @return String textfield-Flag
     */
    public String getTextfield() {
        return textfield;
    }

    /**
     * Sets Password
     * @param String Password
     */
    public void setNewPassword1(String newPassword1) {
        this.newPassword1 = newPassword1;
    }

    /**
     * Sets Password
     * @param String Password
     */
    public String getNewPassword1() {
        return newPassword1;
    }

    /**
     * Sets Password-Repetition
     * @param String Password-Repetition
     */
    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }

    /**
     * Returns Password-Repetition
     * @return String Password-Repetition
     */
    public String getNewPassword2() {
        return newPassword2;
    }

    /**
     * Sets if users will be informed about password change
     * @param String if users will be informed about password change
     */
    public void setUsermail(String usermail) {
        this.usermail = usermail;
    }

    /**
     * Returns if users will be informed about password change
     * @return String if users will be informed about password change
       */
    public String getUsermail() {
        return usermail;
    }
}
