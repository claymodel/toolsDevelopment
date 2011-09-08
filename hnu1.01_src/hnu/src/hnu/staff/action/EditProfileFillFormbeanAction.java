package hnu.staff.action;

import hnu.LoginBean;
import hnu.helper.DataBaseConnection;
import hnu.staff.form.EditStaffProfileForm;

import java.io.IOException;
import java.sql.ResultSet;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/*
 * Copyright (C) 2002-2003 Thomas Maschutznig <thomas.maschutznig@fh-joanneum.at>
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
 * @author Thomas Maschutznig
 */

public class EditProfileFillFormbeanAction extends org.apache.struts.action.Action {
    /**
     * Fills the EditStaffProfileForm so that the staff's details
     * are displayed when loading the editStaffProfile.jsp
     * @param mapping Struts ActionMapping
     * @param form Struts ActionForm
     * @param request HTTP-Request
     * @param response HTTP-Response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // do not create a new session here
        HttpSession sess = request.getSession(false);
        LoginBean loginBean = (LoginBean) sess.getAttribute("loginBean");

        // no session, not logged in or not a staff
        if ((sess == null) || (loginBean == null)) {
            return (mapping.findForward("failure"));
        }

        if (!loginBean.isAuthd() || !(loginBean.getType()).equals("staff")) {
            return (mapping.findForward("failure"));
        }

        if (sess.getAttribute("editStaffProfileForm") == null) {
            // create new object from StaffBean and fill
            EditStaffProfileForm profileForm = new EditStaffProfileForm();

            if (!this.fillBean(profileForm, loginBean)) {
                mapping.findForward("failure");
            }

            // put it into session
            sess.setAttribute("editStaffProfileForm", profileForm);

            return (mapping.findForward("success"));
        } //if
        else {
            // just forward
            return (mapping.findForward("success"));
        }
         //else
    }

    /**
     * Fill the EditStaffProfileForm object specified
     * @param profileForm An EditStaffProfileForm-object used for editStaffProfile.jsp
     * @param staff The Staff's LoginBean
     * @return boolean true if worked
     */
    public boolean fillBean(EditStaffProfileForm profileForm, LoginBean staff) {
        DataBaseConnection db = new DataBaseConnection();
        ResultSet rs = db.getRSfromStatement("SELECT * FROM TStaff WHERE sLogin='" + staff.getLogin() + "';");

        try {
            // rs not empty
            if (rs.next()) {
                profileForm.setFirstname(rs.getString("sFirst"));
                profileForm.setName(rs.getString("sName"));
                profileForm.setPicName(rs.getString("sPic"));

                db.closeResultSet(rs);

                return true;
            }
             //if

            // rs empty
            db.closeResultSet(rs);

            return false;
        } //try
        catch (Exception ex) {
            db.closeResultSet(rs);

            return false;
        } //catch
    }
}
