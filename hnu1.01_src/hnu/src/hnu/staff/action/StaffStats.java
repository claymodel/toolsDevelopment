package hnu.staff.action;

import hnu.LoginBean;
import hnu.helper.DataBaseConnection;
import hnu.helper.staff.GetStatus;
import hnu.helper.staff.StatsBean;
import hnu.helper.staff.TicketStatus;
import hnu.helper.staff.TicketStatusCount;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.Iterator;
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

public class StaffStats extends org.apache.struts.action.Action
{
  /**
   * <p>This action will retrieve various stats from the DB and put them into
   * the session as "statsBean".</p>
   * @param mapping Struts ActionMapping
   * @param form Struts ActionForm
   * @param request HTTP Request
   * @param response HTTP Response
   * @return ActionForward Struts ActionForward
   * @throws IOException
   * @throws ServletException
   */
  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
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

    // clear old StatsBean
    if (sess.getAttribute("statsBean")!=null)
    {
      sess.removeAttribute("statsBean");
    }//if

    int ticketsTotal=0;
    int ticketsStaff=0;
    int messagesStaff=0;
    Vector ticketsStaffPerStatus = new Vector();
    try
    {
      // get number of tickets total
      DataBaseConnection db = new DataBaseConnection();
      ResultSet rs = db.getRSfromStatement("SELECT COUNT(tId) tickets FROM TTicket;");
      if (rs.next())
      {
        ticketsTotal = rs.getInt("tickets");
      }//if
      db.closeResultSet(rs);

      // get number of staff's tickets total
      rs = db.getRSfromStatement("SELECT COUNT(t.tId) ticketsStaff FROM TTicket t WHERE t.tStaff="+loginBean.getId()+";");
      if (rs.next())
      {
        ticketsStaff = rs.getInt("ticketsStaff");
      }//if
      db.closeResultSet(rs);

      // get number of messages total
      rs = db.getRSfromStatement("SELECT COUNT(m.mId) messagesStaff FROM TMessage m WHERE m.mAuthor="+loginBean.getId()+" AND m.mIsUser=0;");
      if (rs.next())
      {
        messagesStaff = rs.getInt("messagesStaff");
      }//if
      db.closeResultSet(rs);

      // get number of tickets per status
      Vector status = GetStatus.getStatus(errors);
      // do this only if status are returned
      if (status!=null)
      {
        Vector tmpStatsPerStatus = new Vector();
        Iterator it = status.iterator();
        // for each status...
        while (it.hasNext())
        {
          TicketStatusCount statCount = new TicketStatusCount();
          TicketStatus tmpStatus = (TicketStatus)it.next();
	  DataBaseConnection dbconn = new DataBaseConnection();
          rs = dbconn.getRSfromStatement("SELECT COUNT(t.tId) tickets FROM TTicket t WHERE t.tStaff="+loginBean.getId()+" AND t.tStatus="+tmpStatus.getStatusID()+";");
          if (rs.next())
          {
            // get the count and fill the properties of the TicketStatusCount bean
            statCount.setCount(rs.getInt("tickets"));
            statCount.setName(tmpStatus.getName());
            statCount.setStatusID(tmpStatus.getStatusID());

            // ...and save in the temporary Vector
            tmpStatsPerStatus.add(statCount);
          }//if
	  db.closeResultSet(rs);
        }//while

        // set the Vector that will be stored in the StatsBean
        ticketsStaffPerStatus = tmpStatsPerStatus;
      }//if


      // set StatsBean
      StatsBean statsBean = new StatsBean();
      statsBean.setTicketsTotal(ticketsTotal);
      statsBean.setTicketsStaff(ticketsStaff);
      statsBean.setMessagesStaff(messagesStaff);
      statsBean.setTicketsStaffPerStatus(ticketsStaffPerStatus);

      // into session
      sess.setAttribute("statsBean", statsBean);

      // if everything worked so far, go ahead and show them stats!
      return(mapping.findForward("success"));
    }//try
    catch (Exception ex)
    {
      errors.add("general", new ActionError("error.database.select"));
      return(mapping.findForward("failure"));
    }//catch
  }
}