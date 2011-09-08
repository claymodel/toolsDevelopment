package hnu.staff.action;

import hnu.LoginBean;
import hnu.helper.DataBaseConnection;
import hnu.helper.staff.GetTicket;

import java.io.IOException;
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

public class TakeTickets extends org.apache.struts.action.Action
{
    /**
     * Updates TTicket and sets ticketownership to the staff
     * It also updates the <code>staffsTicket</code> and <code>newTickets</code>
     * Vectors in the session
     * @param mapping Struts ActionMapping
     * @param form Struts ActionForm
     * @param request HTTP-Request
     * @param response HTTP-Response
     * @return ActionForward
     * @throws ServletException
     * @throws IOException
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        ActionErrors errors = new ActionErrors();

        // do not create a new session here
        HttpSession sess = request.getSession(false);
        LoginBean loginBean = (LoginBean) sess.getAttribute("loginBean");

        // no session, not logged in or not a staff
        if((sess == null) || (loginBean == null))
        {
            return (mapping.findForward("failure"));
        }

        if((loginBean.isAuthd() != true) ||
                !(loginBean.getType()).equals("staff"))
        {

            return (mapping.findForward("failure"));
        }

        String[] ids = request.getParameterValues("ticketId");
        // if empty..
        if ((ids==null) || (ids.length<1))
        {
          errors.add("general", new ActionError("error.staff.taketicket.none"));
          this.saveErrors(request, errors);
          return (mapping.findForward("failure"));
        }//if

        boolean updateVal = false;

        for(int i = 0; i < ids.length; i++)
        {
            // take over un-owned tickets
            updateVal = DataBaseConnection.execute("UPDATE TTicket SET tStaff=" +
                    loginBean.getId() + " WHERE tId=" + ids[i] +
                    " AND tStaff=0;");

            if(!updateVal)
            {
                errors.add("general", new ActionError("error.database.general"));
                saveErrors(request, errors);

                return (mapping.findForward("failure"));
            }//if
        }//for

        // if works, then was successful
        if(updateVal)
        {
            // upate new tickets
            Vector newtickets = GetTicket.getnewTickets(loginBean.getLogin(),
                    loginBean.getId(), errors);

            if(newtickets != null)
            {
                sess.removeAttribute("newTickets");
                sess.setAttribute("newTickets", newtickets);
            }//if
            else
            {
                sess.removeAttribute("newTickets");
                sess.setAttribute("newTickets", new Vector());
            }

            // upate staffs tickets
            Vector staffstickets = GetTicket.getstaffsTickets(loginBean.getLogin(),
                    loginBean.getId(), errors);

            if(staffstickets != null)
            {
                sess.removeAttribute("staffsTickets");
                sess.setAttribute("staffsTickets", staffstickets);
            }//if
            else
            {
                sess.removeAttribute("staffsTickets");
                sess.setAttribute("staffsTickets", new Vector());
            }

            return (mapping.findForward("success"));
        }//if

        // got here? Database error must've occured
        errors.add("general", new ActionError("error.database.general"));
        saveErrors(request, errors);

        return (mapping.findForward("failure"));
    }
}