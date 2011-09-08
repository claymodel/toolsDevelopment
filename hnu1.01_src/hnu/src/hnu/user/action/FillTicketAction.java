package hnu.user.action;

import hnu.LoginBean;
import hnu.helper.DataBaseConnection;
import hnu.helper.user.Group;
import hnu.user.form.NewTicketForm;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.Vector;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
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

public class FillTicketAction extends org.apache.struts.action.Action {
    /**
     * Executes Struts-Action:
     * Provides a proper bean for the NewTicketForm.
     * @param org.apache.struts.action.Actionmapping Struts-Actionmapping
     * @param org.apache.struts.action.ActionForm Struts-ActionForm
     * @param javax.servlet.http.HttpServletRequest HttpServletRequest
     * @param javax.servlet.http.HttpServletResponse HttpServletResponse
     * @return
     *   org.apache.struts.action.ActionForward Struts-ActionForward
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        HttpSession session = request.getSession();
        ActionErrors errors = new ActionErrors();
        LoginBean lb = (LoginBean) session.getAttribute("loginBean");
        NewTicketForm nt = new NewTicketForm();

        String sql = "SELECT * FROM TGroup;";
        Vector groups = new Vector();
        Vector priorities = new Vector();

        DataBaseConnection db = new DataBaseConnection();
        ResultSet rs = null;

        try {
            rs = db.getRSfromStatement(sql);

            while (rs.next()) {
                groups.add(new Group(rs.getString("gId"), rs.getString("gText")));
            }

            db.closeResultSet(rs);
        } catch (Exception ex) {
            errors.add("db", new ActionError("error.database.general"));
            this.saveErrors(request, errors);
            db.closeResultSet(rs);

            return mapping.findForward("failure");
        }

        for (int i = 5; i >= 1; i--) {
            priorities.add(String.valueOf(i));
        }

        nt.setGroups(groups);
        nt.setPriorities(priorities);
        nt.setUser(String.valueOf(lb.getId()));
        session.setAttribute("newTicketForm", nt);
        session.setAttribute("ticketCreationFlag", "1");

        return mapping.findForward("showNewTicket");
    }
}
