package hnu.user.action;

import hnu.LoginBean;
import hnu.helper.user.UserHome;
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

public class FillUserHome extends org.apache.struts.action.Action {
  /**
   * Executes Struts-Action:
   * Provides a proper bean for the UserHomeForm.
   * @param org.apache.struts.action.Actionmapping Struts-Actionmapping
   * @param org.apache.struts.action.ActionForm Struts-ActionForm
   * @param javax.servlet.http.HttpServletRequest HttpServletRequest
   * @param javax.servlet.http.HttpServletResponse HttpServletResponse
   * @return
   *   org.apache.struts.action.ActionForward Struts-ActionForward
   */
  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {

    ActionErrors errors = new ActionErrors();
    UserHomeForm uhf = new UserHomeForm();
    UserHome uh = null;
    HttpSession session = request.getSession();
    LoginBean lb = (LoginBean)session.getAttribute("loginBean");

    try {
      if(lb.getId()==-1) {
        uh = new UserHome(lb.getLogin(),false);
        lb.setId(Integer.parseInt(uh.getId()));
        session.removeAttribute("loginBean");
        session.setAttribute("loginBean",lb);
      } else {
        uh = new UserHome(lb.getLogin(),true);
      }
    } catch (Exception ex) {
      session.removeAttribute("loginBean");
      errors.add("db",new ActionError("error.database.general"));
      this.saveErrors(request,errors);
      return mapping.findForward("failure");
    }

    uhf.setId(uh.getId());
    uhf.setLogin(lb.getLogin());

    session.setAttribute("userhomeForm",uhf);
    session.setAttribute("userHome",uh);

    return mapping.findForward("success");
  }
}