package hnu.user.action;

import hnu.LoginBean;
import hnu.helper.DataBaseConnection;
import hnu.user.form.UserForm;

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

public class FillUserDetails extends org.apache.struts.action.Action {
  /**
   * Executes Struts-Action:
   * Provides a proper bean for the UserForm.
   * @param org.apache.struts.action.Actionmapping Struts-Actionmapping
   * @param org.apache.struts.action.ActionForm Struts-ActionForm
   * @param javax.servlet.http.HttpServletRequest HttpServletRequest
   * @param javax.servlet.http.HttpServletResponse HttpServletResponse
   * @return
   *   org.apache.struts.action.ActionForward Struts-ActionForward
   */
  public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    UserForm userForm = new UserForm();
    ActionErrors errors = new ActionErrors();
    HttpSession session = request.getSession();
    Vector days = new Vector();
    Vector months = new Vector();
    Vector years = new Vector();
    LoginBean lb = (LoginBean)session.getAttribute("loginBean");

    days.add("");
    months.add("");
    years.add("");
    for (int i=1;i<32;i++ ) {
      days.add(String.valueOf(i));
    }
    for (int i=1;i<13;i++ ) {
      months.add(String.valueOf(i));
    }
    for (int i=1950;i<2003;i++ ) {
      years.add(String.valueOf(i));
    }

    userForm.setDays(days);
    userForm.setMonths(months);
    userForm.setYears(years);

    if(lb.getId()>=0) {
      DataBaseConnection db = new DataBaseConnection();
      ResultSet rs = null;
      try {
        rs = db.getRSfromStatement("SELECT * FROM TUser WHERE uId="+lb.getId()+";");
        if(rs.next()) {
          userForm.setCity(rs.getString("uCity"));
          userForm.setCountry(rs.getString("uCountry"));
          userForm.setFirst(rs.getString("uFirst"));
          userForm.setId(rs.getString("uId"));
          userForm.setLogin(rs.getString("uLogin"));
          userForm.setMail(rs.getString("uMail"));
          userForm.setName(rs.getString("uName"));
          userForm.setStreet(rs.getString("uStreet"));
          userForm.setTelephone(rs.getString("uTelephone"));
          userForm.setZip(rs.getString("uZip"));
          userForm.setExistingUser(true);

          session.setAttribute("userForm", userForm);
        }
      } catch (Exception ex) {
        errors.add("db",new ActionError("error.database.general"));
        this.saveErrors(request,errors);
	db.closeResultSet(rs);
        return mapping.findForward("failure");
      }
      db.closeResultSet(rs);

      try {
        ResultSet rs1 = db.getRSfromStatement("SELECT YEAR(uDob) y, MONTH(uDob) m, DAYOFMONTH(uDob) d FROM TUser WHERE uId="+lb.getId()+";");
        if(rs1.next()) {
          userForm.setYear(rs1.getString("y"));
          userForm.setMonth(rs1.getString("m"));
          userForm.setDay(rs1.getString("d"));
        }
      }
      catch (Exception ex) {
        errors.add("db",new ActionError("error.database.general"));
        this.saveErrors(request,errors);
	db.closeResultSet(rs);
        return mapping.findForward("failure");
      }
      db.closeResultSet(rs);

    } else //uId
    {
      userForm.setYear("");
      userForm.setExistingUser(false);
      session.setAttribute("userForm",userForm);
    }

    return mapping.findForward("success");
  }
}