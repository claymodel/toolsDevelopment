package hnu.staff.action;

import hnu.LoginBean;
import hnu.helper.staff.GetMessagesHelper;
import hnu.helper.staff.GetStatus;
import hnu.helper.staff.GetTicket;
import hnu.helper.staff.TicketBean;
import hnu.helper.staff.WholeTicketBean;

import java.io.IOException;
import java.util.Vector;

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

public class GetMessages extends org.apache.struts.action.Action
{
    /**
     * This action will retrieve all corresponding messages of a ticket
     * from TMessage and put them into the session as <strong>wholeTicket</strong>.
     * It will also load the status from TStatus and make them available
     * under <strong>ticketStatus</strong>.<br />
     * @param mapping Struts ActionMapping
     * @param form Struts ActionForm
     * @param request HTTP-Request
     * @param response HTTP-Response
     * @return Struts Action-Forward
     * @throws IOException
     * @throws ServletException
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
            return (mapping.findForward("Login"));
        }

        if((loginBean.isAuthd() != true) ||
                !(loginBean.getType()).equals("staff"))
        {
            return (mapping.findForward("failure"));
        }

        // check and remove any "old" content in session
        if(sess.getAttribute("woleTicket") != null)
        {
            sess.removeAttribute("wholeTicket");
        }

        if(sess.getAttribute("ticketStatus") != null)
        {
            sess.removeAttribute("ticketStatus");
        }

        int tId = Integer.parseInt(request.getParameter("tId"));
        Vector allMessages = GetMessagesHelper.getAllMessages(tId, errors);
        Vector status = GetStatus.getStatus(errors);

        // make sure a history and status are returned
        if((allMessages != null) && (status != null))
        {
            GetTicket.setSpecificId(tId);

            Vector staffsTickets = GetTicket.getallstaffsTickets(loginBean.getLogin(),
                    loginBean.getId(), errors);
            GetTicket.setSpecificId(-1);

            WholeTicketBean wholeTicket = new WholeTicketBean();
            TicketBean ticketBean = (TicketBean) staffsTickets.firstElement();

            // transfer the data from the ticketBean
            wholeTicket.setGroup(ticketBean.getGroup());
            wholeTicket.setId(ticketBean.getId());
            wholeTicket.setPriority(ticketBean.getPriority());
            wholeTicket.setStaff(ticketBean.getStaff());
            wholeTicket.setStatus(ticketBean.getStatus());
            wholeTicket.setSubject(ticketBean.getSubject());
            wholeTicket.setTimeSince(ticketBean.getTimeSince());
            wholeTicket.setUser(ticketBean.getUser());

            // plus add the messages
            wholeTicket.setMessages(allMessages);

            // put the ticket plus status into the session
            sess.setAttribute("wholeTicket", wholeTicket);
            sess.setAttribute("ticketStatus", status);

            return mapping.findForward("success");
        }

        //if
        // if there was an error go back to staffHome
        saveErrors(request, errors);

        return mapping.findForward("failure");
    }
}