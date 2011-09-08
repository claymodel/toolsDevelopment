package hnu.staff.action;

import hnu.LoginBean;
import hnu.helper.DataBaseConnection;
import hnu.staff.form.StaffworkonticketForm;

import java.io.IOException;

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

public class StaffworkonticketAction extends org.apache.struts.action.Action {
	/**
	 * Inserts a staff's reply to a ticket into the DB (TMessage)
	 * 
	 * @param mapping Struts ActionMapping
	 * @param form ActionForm
	 * @param request HTTP-Request
	 * @param response HTTP-Response @returns ActionForward
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward perform(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException {
		ActionErrors errors = new ActionErrors();
		int tId = Integer.parseInt(request.getParameter("id"));
		StaffworkonticketForm staffworkonticketForm =
			(StaffworkonticketForm) form;

		HttpSession sess = request.getSession(false);

		// no session??? back to login..
		if (sess == null) {
			errors.add("general", new ActionError("error.login.not"));
			saveErrors(request, errors);

			return mapping.findForward("Login");
		}

		LoginBean loginBean = (LoginBean) sess.getAttribute("loginBean");

		//        String sql =
		//            "INSERT INTO TMessage(mDate, mAuthor, mIsUser, mTicket, mText) " +
		//            "VALUES(NOW(), " + loginBean.getId() + ", 0, " + tId + ", '" +
		//            staffworkonticketForm.getMessage() + "');";
		String sql =
			"INSERT INTO TMessage (mText,mAuthor,mIsUser,mTicket,mDate) VALUES (?,?,0,?,NOW() );";
		Object[] parameters = new Object[3];
		parameters[0] = staffworkonticketForm.getMessage();
		parameters[1] = new Integer(loginBean.getId());
		parameters[2] = new Integer(tId);

		String sql2 =
			"UPDATE TTicket SET tStatus="
				+ staffworkonticketForm.getTicketStatus()
				+ " "
				+ "WHERE tId="
				+ tId
				+ ";";

		if (DataBaseConnection.execute(sql)
			& DataBaseConnection.execute(sql2)) {
			saveErrors(request, errors);

			return mapping.findForward("success");
		} //if

		errors.add("general", new ActionError("error.database.update"));
		saveErrors(request, errors);

		return mapping.findForward("failure");
	}
}