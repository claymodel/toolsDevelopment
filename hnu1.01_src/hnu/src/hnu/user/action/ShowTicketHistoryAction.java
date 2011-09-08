package hnu.user.action;

import hnu.helper.DataBaseConnection;
import hnu.user.form.ShowTicketHistoryForm;
import hnu.user.form.UserHomeForm;

import java.io.IOException;

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

public class ShowTicketHistoryAction extends org.apache.struts.action.Action {
  /**
   * Executes Struts-Action:
   * Stores a new Message in the database.
   * Saves changes related to the priority in the database.
   * @param org.apache.struts.action.Actionmapping Struts-Actionmapping
   * @param org.apache.struts.action.ActionForm Struts-ActionForm
   * @param javax.servlet.http.HttpServletRequest HttpServletRequest
   * @param javax.servlet.http.HttpServletResponse HttpServletResponse
   * @return
   *   org.apache.struts.action.ActionForward Struts-ActionForward
   */
  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    HttpSession session = request.getSession();
    ActionErrors errors = new ActionErrors();
    ShowTicketHistoryForm thf = (ShowTicketHistoryForm) form;
    UserHomeForm uhf = (UserHomeForm)session.getAttribute("userhomeForm");

    String priority=thf.getPriority();
    String old = thf.getOldPriority();

    if(thf.getOk().equals("Save Changes")) {
      if(!priority.equals(old)) {
        String sql = "UPDATE TTicket SET tPriority="+priority+" WHERE tId="+thf.getId()+";";

        boolean db = DataBaseConnection.execute(sql);

        if(!db) {
          errors.add("db",new ActionError("error.user.history.updatePriority"));
          this.saveErrors(request,errors);
          return mapping.findForward("failure");
        }
      }
    }

    if(thf.getOk().equals("Submit Message")) {
      String sql="INSERT INTO TMessage (mText,mAuthor,mIsUser,mTicket,mDate) VALUES ('"+thf.getNewMessage()+"',"+uhf.getId();
      sql=sql+",1,"+thf.getId()+",NOW());";

      boolean db = DataBaseConnection.execute(sql);

      if(!db) {
        errors.add("db",new ActionError("error.user.history.submitMessage"));
        this.saveErrors(request,errors);
        return mapping.findForward("failure");
      } else {
        return mapping.findForward("success");
      }
    }
    return mapping.findForward("failure");
  }
}