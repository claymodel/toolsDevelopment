package hnu.user.action;

import hnu.LoginBean;
import hnu.helper.DataBaseConnection;
import hnu.helper.PasswordHash;
import hnu.helper.SendMail;
import hnu.user.form.UserForm;

import java.io.IOException;
import java.net.InetAddress;

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

public class UserAction extends org.apache.struts.action.Action {
    /**
     * Executes Struts-Action:
     * Stores a new user in the database.
     * Sends a mail with the activation-link to the entered mail-address
     * Saves changes related to the the user details and the passwords in the database
     * @param org.apache.struts.action.Actionmapping Struts-Actionmapping
     * @param org.apache.struts.action.ActionForm Struts-ActionForm
     * @param javax.servlet.http.HttpServletRequest HttpServletRequest
     * @param javax.servlet.http.HttpServletResponse HttpServletResponse
     * @return
     *   org.apache.struts.action.ActionForward Struts-ActionForward
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserForm uf = (UserForm) form;
        ActionErrors errors = new ActionErrors();
        HttpSession sess = request.getSession();
        LoginBean logBean = (LoginBean) sess.getAttribute("loginBean");

        if (logBean.isAuthd()) {
            if (uf.getOk().equals("Change password")) {
                boolean db = DataBaseConnection.execute("UPDATE TUser SET uPass='" + PasswordHash.getSHAString(uf.getPass().trim()) + "' WHERE uId=" + uf.getId() + ";");

                if (!db) {
                    errors.add("db", new ActionError("error.user.changePassword"));
                    this.saveErrors(request, errors);

                    return mapping.findForward("failure");
                } else {
                    return mapping.findForward("showUser");
                }
            }

            if (uf.getOk().equals("Change details")) {
                String sql = "UPDATE TUser SET uName='" + uf.getName() + "', uFirst='" + uf.getFirst() + "', uMail='" + uf.getMail();

                sql = sql + "', uStreet='" + uf.getStreet() + "', uCity='" + uf.getCity() + "', uCountry='" + uf.getCountry() + "', uTelephone='";
                sql = sql + uf.getTelephone() + "',uDob='" + uf.getYear() + "-" + uf.getMonth() + "-" + uf.getDay() + "', uZip='" + uf.getZip() + "' WHERE uId=" + uf.getId() + ";";

                boolean db = DataBaseConnection.execute(sql);

                if (!db) {
                    errors.add("db", new ActionError("error.user.history.changeDetails"));
                    this.saveErrors(request, errors);

                    return mapping.findForward("failure");
                } else {
                    return mapping.findForward("showUser");
                }
            }
        } else {
            String sql = "INSERT INTO TUser (uName,uFirst,uMail,uLogin,uStreet,uCity,uCountry,uDob,uTelephone,uPass,uDate,uZip) VALUES ('" + uf.getName() + "','" + uf.getFirst() + "','" + uf.getMail() + "','" + uf.getLogin();

            sql = sql + "','" + uf.getStreet() + "','" + uf.getCity() + "','" + uf.getCountry() + "','" + uf.getYear() + "-" + uf.getMonth() + "-" + uf.getDay() + "','" + uf.getTelephone();
            sql = sql + "','" + PasswordHash.getSHAString(uf.getPass().trim()) + "',NOW(),'" + uf.getZip() + "');";

            boolean db = DataBaseConnection.execute(sql);
            java.sql.ResultSet rs = null;

            if (!db) {
                errors.add("db", new ActionError("error.user.create"));
                this.saveErrors(request, errors);

                return (mapping.findForward("failure"));
            } else {
                String idInsert = "";
		DataBaseConnection dbconn = null;

                try {
                    dbconn = new DataBaseConnection();

                    rs = dbconn.getRSfromStatement("SELECT uId FROM TUser WHERE uLogin='" + uf.getLogin() + "';");

                    if (rs.next()) {
                        idInsert = rs.getString("uId");
                    }

                    dbconn.closeResultSet(rs);
                } catch (Exception ex) {
                    errors.add("db", new ActionError("error.database.general"));
                    this.saveErrors(request, errors);

		    dbconn.closeResultSet(rs);
                    return mapping.findForward("failure");
                }

                InetAddress localaddr = InetAddress.getLocalHost();

                String body = "To activate your Account please hit the link:\n\n";
                String url = "http://" + localaddr.getHostAddress() + ":" + request.getServerPort() + "/hnu/user/activateAction.do?id=" + idInsert;

                body += url;
                body += "\n\nYour HNU-team";

                SendMail sm = new SendMail();
                boolean mail = sm.sendMail(uf.getMail(), "HNU :: Account activation", body);

                if (mail) {
                    return (mapping.findForward("success"));
                } else {
                    errors.add("db", new ActionError("error.user.mail"));
                    this.saveErrors(request, errors);

                    return (mapping.findForward("failure"));
                }
            }
        }

        return (mapping.findForward("failure"));
    }
     //perform
}
 //class
