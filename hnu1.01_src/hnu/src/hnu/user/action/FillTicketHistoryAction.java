package hnu.user.action;

import hnu.LoginBean;
import hnu.helper.DataBaseConnection;
import hnu.helper.DateFormatter;
import hnu.helper.user.Message;
import hnu.user.form.ShowTicketHistoryForm;
import hnu.user.form.UserHomeForm;

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

public class FillTicketHistoryAction extends org.apache.struts.action.Action {
    /**
     * Executes Struts-Action:
     * Provides a proper bean for the ShowTicketHistoryForm.
     * @param org.apache.struts.action.Actionmapping Struts-Actionmapping
     * @param org.apache.struts.action.ActionForm Struts-ActionForm
     * @param javax.servlet.http.HttpServletRequest HttpServletRequest
     * @param javax.servlet.http.HttpServletResponse HttpServletResponse
     * @return
     *   org.apache.struts.action.ActionForward Struts-ActionForward
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        ActionErrors errors = new ActionErrors();
        ShowTicketHistoryForm th = new ShowTicketHistoryForm();
        UserHomeForm uhf = (UserHomeForm) session.getAttribute("userhomeForm");

        Vector priorities = new Vector();

        for (int i = 5; i >= 1; i--) {
            priorities.add(String.valueOf(i));
        }

        th.setPriorities(priorities);

        String status = "";
        String[] tickets = uhf.getTicketId();
        String sql = "SELECT * FROM TTicket WHERE tId=" + tickets[0];
        String staff = "";
        java.util.Date date = null;
        DateFormatter df = new DateFormatter();
        DataBaseConnection db = new DataBaseConnection();
        ResultSet rs = null;

        try {
            rs = db.getRSfromStatement(sql);

            if (rs.next()) {
                th.setDate(df.getDate(rs.getTimestamp("tDate")));
                th.setId(rs.getString("tId"));
                th.setSubject(rs.getString("tSubject"));
                th.setPriority(rs.getString("tPriority"));
                th.setOldPriority(rs.getString("tPriority"));
                status = rs.getString("tStatus");
                staff = rs.getString("tStaff");
            }
        } catch (Exception ex) {
            errors.add("db", new ActionError("error.database.general"));
            this.saveErrors(request, errors);
            db.closeResultSet(rs);

            return mapping.findForward("failure");
        }

        db.closeResultSet(rs);

        sql = "SELECT stText FROM TStatus WHERE stId=" + status + ";";

        try {
            rs = db.getRSfromStatement(sql);

            if (rs.next()) {
                status = rs.getString("stText");
            }
        } catch (Exception ex) {
            errors.add("db", new ActionError("error.database.general"));
            this.saveErrors(request, errors);
            db.closeResultSet(rs);

            return mapping.findForward("failure");
        }

        db.closeResultSet(rs);
        th.setStatus(status);

        if (!staff.equals("0")) {
            sql = "SELECT sName, sFirst, sPic FROM TStaff WHERE sId=" + staff + ";";

            try {
                rs = db.getRSfromStatement(sql);

                if (rs.next()) {
                    staff = rs.getString("sFirst") + " " + rs.getString("sName");
                    th.setPic(rs.getString("sPic"));
                }
            } catch (Exception ex) {
                errors.add("db", new ActionError("error.database.general"));
                this.saveErrors(request, errors);
                db.closeResultSet(rs);

                return mapping.findForward("failure");
            }
        } //if staff !0
        else {
            staff = "no staff";
        }

        db.closeResultSet(rs);
        th.setStaff(staff);

        sql = "SELECT * FROM TMessage WHERE mTicket=" + tickets[0] + " ORDER BY mDate DESC;";

        Vector messages = new Vector();
        String author = "";
        String isUser = "";

        try {
            rs = db.getRSfromStatement(sql);

            while (rs.next()) {
                author = rs.getString("mAuthor");
                isUser = rs.getString("mIsUser");

                if (isUser.equals("1")) {
                    LoginBean lb = (LoginBean) session.getAttribute("loginBean");

                    author = lb.getLogin();
                } else {
                    sql = "SELECT sName, sFirst FROM TStaff WHERE sId=" + author + ";";

                    ResultSet rsNew = null;

                    try {
                        rsNew = db.getRSfromStatement(sql);

                        if (rsNew.next()) {
                            author = rsNew.getString("sFirst") + " " + rsNew.getString("sName");
                        }

                        db.closeResultSet(rsNew);
                    } catch (Exception ex) {
                        errors.add("db", new ActionError("error.database.general"));
                        this.saveErrors(request, errors);
                        db.closeResultSet(rsNew);

                        return mapping.findForward("failure");
                    }
                }

                //else
                messages.add(new Message(df.getDate(rs.getTimestamp("mDate")), rs.getString("mText"), author));
            }
        } catch (Exception ex) {
            errors.add("db", new ActionError("error.database.general"));
            this.saveErrors(request, errors);
            db.closeResultSet(rs);

            return mapping.findForward("failure");
        }

        db.closeResultSet(rs);
        th.setMessages(messages);

        session.setAttribute("showTicketHistoryForm", th);

        return mapping.findForward("success");
    }
}
