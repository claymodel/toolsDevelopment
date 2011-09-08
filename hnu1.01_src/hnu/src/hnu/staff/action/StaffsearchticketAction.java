package hnu.staff.action;

import hnu.LoginBean;
import hnu.helper.DataBaseConnection;
import hnu.helper.DateFormatter;
import hnu.helper.staff.TicketBean;
import hnu.staff.form.StaffsearchticketForm;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.Vector;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
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

public class StaffsearchticketAction extends org.apache.struts.action.Action {
    /**
     * Search for the specified string in tickets (user, subject)
     * @param mapping Struts ActionMapping
     * @param form Struts ActionForm
     * @param request HTTP Request
     * @param response HTTP Response
     * @return ActionForward Struts ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        StaffsearchticketForm staffsearchticketForm = (StaffsearchticketForm) form;
        ActionErrors errors = new ActionErrors();

        HttpSession sess = request.getSession(false);

        // no session??? back to login..
        if (sess == null) {
            errors.add("general", new ActionError("error.login.not"));
            saveErrors(request, errors);

            return mapping.findForward("Login");
        }

        DateFormatter df = new DateFormatter();
        LoginBean loginBean = (LoginBean) sess.getAttribute("loginBean");

        // Collection of Tickets
        Vector ticketsColl = new Vector();

        // if there are previous results, delete them
        if (sess.getAttribute("searchResults") != null) {
            sess.removeAttribute("searchResults");
        }
         //if

        String sql = "SELECT t.tId ticketID, u.uLogin uLogin, t.tSubject Subject, t.tStaff staff, g.gText gName, " + "st.stText tStatus, t.tDate timeSince, t.tPriority priority " + "FROM TTicket t INNER JOIN TGroupMembers gm ON t.tGroup = gm.gmGroup " + "INNER JOIN TStaff s ON gm.gmStaff = s.sId " + "INNER JOIN TGroup g ON gm.gmGroup = g.gId " + "INNER JOIN TUser u ON u.uId = t.tUser " + "INNER JOIN TStatus st ON st.stId = t.tStatus " + "WHERE s.sLogin = '" + loginBean.getLogin() + "' " + "AND t.tStaff = " + loginBean.getId() + " " + "AND (u.uLogin LIKE '%" + staffsearchticketForm.getSearchString() + "%' OR t.tSubject LIKE '%" + staffsearchticketForm.getSearchString() + "%') " + "ORDER BY priority ASC, timeSince DESC, uLogin;";

        DataBaseConnection db = new DataBaseConnection();
        ResultSet rs = db.getRSfromStatement(sql);

        try {
            while (rs.next()) {
                // create one ticket from the current row
                TicketBean ticket = new TicketBean();

                ticket.setId(rs.getInt("ticketID"));
                ticket.setUser(rs.getString("uLogin"));
                ticket.setSubject(rs.getString("Subject"));
                ticket.setGroup(rs.getString("gName"));
                ticket.setStatus(rs.getString("tStatus"));
                ticket.setTimeSince(df.getDate(rs.getTimestamp("timeSince")));
                ticket.setPriority(rs.getByte("priority"));
                ticket.setStaff(rs.getInt("staff"));

                // put into the collection
                ticketsColl.add(ticket);
            }
             //while

            // into session and forward
            sess.setAttribute("searchResults", ticketsColl);
            db.closeResultSet(rs);

            return (mapping.findForward("success"));
        } //try
        catch (Exception ex) {
            errors.add("general", new ActionError("error.database.select"));
            db.closeResultSet(rs);

            return (mapping.findForward("failure"));
        }
         //catch
    }
}
