package hnu.admin.action;

import hnu.admin.form.AddUserForm;
import hnu.helper.DataBaseConnection;
import hnu.helper.PasswordHash;

import java.io.IOException;
import java.sql.Date;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
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

public class AddUserAction extends org.apache.struts.action.Action {
    /**
     * Executes Struts-Action:
     * Adds a new user-account to the database.
     * @param org.apache.struts.action.Actionmapping Struts-Actionmapping
     * @param org.apache.struts.action.ActionForm Struts-ActionForm
     * @param javax.servlet.http.HttpServletRequest HttpServletRequest
     * @param javax.servlet.http.HttpServletResponse HttpServletResponse
     * @return
     *   org.apache.struts.action.ActionForward Struts-ActionForward
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        AddUserForm addUserForm = (AddUserForm) form;
        ActionErrors errors = new ActionErrors();

	String dateStr = null;
	Date date = null;
	try {
	  date = Date.valueOf(addUserForm.getYear() + "-" + addUserForm.getMonth() + "-" + addUserForm.getDay());
	  dateStr = date.toString();
	} catch (Exception ex) {
	  dateStr = "";
	}

        String sql = "INSERT INTO TUser (uName,uFirst,uMail,uLogin,uStreet,uCity,uCountry,uDob,uTelephone,uDate,uPass) VALUES ('"
		   + addUserForm.getName() + "','" + addUserForm.getFirst()
		   + "','" + addUserForm.getMail() + "','" + addUserForm.getLogin()
		   + "','" + addUserForm.getStreet() + "','" + addUserForm.getCity()
		   + "','" + addUserForm.getCountry() + "','" + dateStr
		   + "','" + addUserForm.getTelephone() + "', NOW(), '"
		   + PasswordHash.getSHAString(addUserForm.getPass()) + "')";

        if (DataBaseConnection.execute(sql)) {
            addUserForm.reset(mapping, request);

            return mapping.findForward("success");
        } else {
            errors.add("dberror", new ActionError("error.database.insert"));
            this.saveErrors(request, errors);

            return mapping.findForward("failure");
        }
    }
}
