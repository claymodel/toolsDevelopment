package hnu.staff.action;

import hnu.LoginBean;
import hnu.helper.staff.GetTicket;

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

public class RefrSessContent extends org.apache.struts.action.Action
{
    /**
     * <p>Refresh the staff's tickets and new tickets as well as staffBean
     * in the session when going to the staffHome.jsp</p>
     * @param mapping Struts ActionMapping
     * @param form Struts ActionForm
     * @param request HTTP Request
     * @param response HTTP Response
     * @return Struts ActionForward
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
        // if not a staff, send back to the login page...
        // if they are logged in, there'll be an apropriate link to their home
        return (mapping.findForward("Login"));
      }

      // Get new tickets which are not taken over yet
      Vector newTickets = GetTicket.getnewTickets(loginBean.getLogin(), loginBean.getId(), errors);
      // avoid having no Vector at all
      if(newTickets == null)
      {
        sess.setAttribute("newTickets", new Vector());
      }//if
      else
      {
        // into session
        sess.setAttribute("newTickets", newTickets);
      }//else

      // Get tickets this staff has already taken over
      Vector staffsTickets = GetTicket.getstaffsTickets(loginBean.getLogin(), loginBean.getId(), errors);

      if(staffsTickets == null)
      {
        sess.setAttribute("staffsTickets", new Vector());

        return (mapping.findForward("staffHome"));
      }//if

      // into session
      sess.setAttribute("staffsTickets", staffsTickets);
      return(mapping.findForward("staffHome"));
    }
}