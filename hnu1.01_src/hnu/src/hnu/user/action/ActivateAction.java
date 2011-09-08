package hnu.user.action;

import hnu.LoginBean;
import hnu.helper.DataBaseConnection;
import hnu.user.form.ActivateForm;

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

public class ActivateAction extends org.apache.struts.action.Action {
  /**
   * Executes Struts-Action:
   * Activates a user.
   * @param org.apache.struts.action.Actionmapping Struts-Actionmapping
   * @param org.apache.struts.action.ActionForm Struts-ActionForm
   * @param javax.servlet.http.HttpServletRequest HttpServletRequest
   * @param javax.servlet.http.HttpServletResponse HttpServletResponse
   * @return
   *   org.apache.struts.action.ActionForward Struts-ActionForward
   */
  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    ActivateForm activateForm = (ActivateForm) form;
    ActionErrors errors = new ActionErrors();

    boolean db = DataBaseConnection.execute("UPDATE TUser SET uActive=1 WHERE uId="+activateForm.getId()+";");

    if(!db) {
      errors.add("db", new ActionError("error.user.activate.db"));
      this.saveErrors(request,errors);
      return mapping.findForward("failure");
    } else {
      LoginBean lb = new LoginBean();
      lb.setAuthd(true);
      lb.setId(Integer.parseInt(activateForm.getId()));
      lb.setType("user");
      lb.setLogin(activateForm.getLogin());
      HttpSession sess = request.getSession();
      sess.setAttribute("loginBean",lb);
      return mapping.findForward("success");
    }
  }
}