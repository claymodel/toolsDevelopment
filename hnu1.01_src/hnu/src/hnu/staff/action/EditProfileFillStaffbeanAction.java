package hnu.staff.action;

import hnu.LoginBean;
import hnu.helper.DataBaseConnection;
import hnu.helper.staff.StaffBean;

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
 * @deprecated
 */

@Deprecated
public class EditProfileFillStaffbeanAction {
    /**
     * Fills the StaffBean
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

        if (sess.getAttribute("staffBean") == null) {
            // create new object from StaffBean and fill
            StaffBean staffBean = new StaffBean();

            staffBean.setLogin(loginBean.getLogin());

            // put it into session
        } //if
        else {
            // check if all properties filled, then forward
            return (mapping.findForward("success"));
        }

        //else
        return (mapping.findForward("failure"));
    }

    /**
     * Fill the EditStaffProfileForm object specified
     * @param staff a StaffBean to fill
     * @return boolean true if worked
     */
    public boolean fillBean(StaffBean staff) {
        DataBaseConnection db = new DataBaseConnection();
        ResultSet rs = db.getRSfromStatement("SELECT * FROM TStaff WHERE sLogin='" + staff.getLogin() + "';");

        try {
            if (rs.next()) {
                staff.setFirstname(rs.getString("sFirst"));
                staff.setName(rs.getString("sName"));
            }

            //if
        } //try
        catch (Exception ex) {
            db.closeResultSet(rs);

            return false;
        }

        //catch
        db.closeResultSet(rs);

        return false;
    }
}
