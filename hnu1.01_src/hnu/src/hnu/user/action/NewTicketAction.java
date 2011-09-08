package hnu.user.action;

import hnu.LoginBean;
import hnu.helper.DataBaseConnection;
import hnu.user.form.NewTicketForm;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.Enumeration;

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

public class NewTicketAction extends org.apache.struts.action.Action {
  /**
   * Executes Struts-Action:
   * Stores a new ticket in the database.
   * @param org.apache.struts.action.Actionmapping Struts-Actionmapping
   * @param org.apache.struts.action.ActionForm Struts-ActionForm
   * @param javax.servlet.http.HttpServletRequest HttpServletRequest
   * @param javax.servlet.http.HttpServletResponse HttpServletResponse
   * @return
   *   org.apache.struts.action.ActionForward Struts-ActionForward
   */
  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    NewTicketForm ntf = (NewTicketForm) form;
    ActionErrors errors = new ActionErrors();
    HttpSession session = request.getSession();
    LoginBean lb = (LoginBean)session.getAttribute("loginBean");

    Enumeration enum = session.getAttributeNames();

    while(enum.hasMoreElements()) {

      if(enum.nextElement().toString().equals("ticketCreationFlag")){

        session.removeAttribute("ticketCreationFlag");

        String sql="INSERT INTO TTicket (tUser, tGroup, tPriority, tSubject, tStatus, tDate) VALUES ('";
        sql = sql + lb.getId() +"','"+ntf.getGroup()+"','"+ntf.getPriority()+"','"+ntf.getSubject()+"',1,NOW());";
        String ticketId="";

        boolean db = DataBaseConnection.execute(sql);

        if(!db) {
          errors.add("db",new ActionError("error.user.create"));
          this.saveErrors(request,errors);
          return mapping.findForward("failure");
        }

        sql="SELECT tId FROM TTicket WHERE tSubject='"+ntf.getSubject()+"';";

	DataBaseConnection dbConn = new DataBaseConnection();
	ResultSet rs = null;

        try {
          rs = dbConn.getRSfromStatement(sql);
          if(rs.next()) {
            ticketId=rs.getString("tId");
          }
        } catch (Exception ex) {
          errors.add("db",new ActionError("error.user.create"));
          this.saveErrors(request,errors);
	  dbConn.closeResultSet(rs);
          return mapping.findForward("failure");
        }
	dbConn.closeResultSet(rs);

        sql="";
        sql="INSERT INTO TMessage (mText,mAuthor,mIsUser,mTicket,mDate) VALUES ('"+ntf.getText()+"',"+lb.getId();
        sql+=",1,"+ticketId+",NOW());";

        db = DataBaseConnection.execute(sql);

        if(!db) {
          errors.add("db",new ActionError("error.user.ticket.create"));
          this.saveErrors(request,errors);
          return mapping.findForward("failure");
        } else {
          return mapping.findForward("success");
        }
      }//if enum-element
    }//while
    errors.add("db",new ActionError("error.user.create"));
    this.saveErrors(request,errors);
    return mapping.findForward("failure");
  }//perform
}