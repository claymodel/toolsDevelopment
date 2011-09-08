package hnu;

import hnu.helper.DataBaseConnection;
import hnu.helper.PasswordHash;
import hnu.helper.staff.GetTicket;
import hnu.helper.staff.StaffBean;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class LoginAction extends org.apache.struts.action.Action
{
    /**
     * Try to validate login information using validateLogin()
     * If OK, will forward accordingly to the type of user logging in
     * @param mapping Struts ActionMapping
     * @param form Struts ActionForm
     * @param request the HTTPRequest
     * @param response HTTP Response
     * @throws IOException
     * @throws ServletException
     * @return ActionForward
     * */
    public ActionForward perform(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        LoginForm loginForm = (LoginForm) form;
        ActionErrors errors = new ActionErrors();

        String tmpF = this.validateLogin(loginForm.getLogin(),
                loginForm.getPass(), request.getSession(true), errors);

        if(tmpF.equals("failure"))
        {
            errors.add("general",
                new ActionError("error.staff.login.login.wrong"));
        }//if

        saveErrors(request, errors);

        return mapping.findForward(tmpF);
    }

    /**
     * Check if login and pass match with DB; will add corresponding Bean to session
     * @return String The Forward to be used; "failure" if login is wrong
     * @param login String
     * @param pass String
     * @param sess HTTPSession
     * @param errors ActionErrors
     */
    private String validateLogin(String login, String pass, HttpSession sess,
        ActionErrors errors)
    {
        pass = PasswordHash.getSHAString(pass);

	DataBaseConnection db = new DataBaseConnection();

      //===================== user
      //===========================
        if(DataBaseConnection.checkResult(
                    "SELECT uId, uLogin, uPass FROM TUser WHERE uActive='1' AND uLogin='" +
                    login + "' AND uPass='" + pass + "';"))
        {
            LoginBean logBean = new LoginBean(login, "user");

            ResultSet rs = db.getRSfromStatement(
                    "SELECT uId FROM TUser WHERE uActive='1' AND uLogin='" +
                    login + "' AND uPass='" + pass + "';");

            try
            {
                if(!rs.next())
                {
		    db.closeResultSet(rs);
                    return ("failure");
                }//if

                logBean.setId(rs.getInt("uId"));
            }//try
            catch(SQLException ex)
            {
                errors.add("general", new ActionError("error.database.select"));

		db.closeResultSet(rs);
                return ("failure");
            }//catch

            logBean.setAuthd(true);
            sess.setAttribute("loginBean", logBean);
	    db.closeResultSet(rs);

            return ("userHome");
        }//if

      //===================== staff
      //===========================
        else if(DataBaseConnection.checkResult(
                    "SELECT sId, sLogin, sPass FROM TStaff WHERE sLogin='" +
                    login + "' AND sPass='" + pass + "';"))
        {
            LoginBean loginBean = new LoginBean(login, "staff");

            // set ID
            ResultSet rs = db.getRSfromStatement(
                    "SELECT sId, sLogin, sPass FROM TStaff WHERE sLogin='" +
                    login + "' AND sPass='" + pass + "';");

            try
            {
                if(!rs.next())
                {
		    db.closeResultSet(rs);
                    return ("failure");
                }//if

                loginBean.setId(rs.getInt("sId"));

                db.closeResultSet(rs);
            }
             //try
            catch(SQLException ex)
            {
                errors.add("general", new ActionError("error.database.select"));

		db.closeResultSet(rs);
                return ("failure");
            }//catch

            // auth and put into session
            loginBean.setAuthd(true);
            sess.setAttribute("loginBean", loginBean);
	    db.closeResultSet(rs);

            if(this.staffLogin(sess, errors, login, loginBean.getId()))
            {
                return ("staffHome");
            }//if
        }//elseif

      //===================== admin
      //===========================
        else if(DataBaseConnection.checkResult(
                    "SELECT aLogin, aPass FROM TAdmin WHERE aLogin='" + login +
                    "' AND aPass='" + pass + "';"))
        {
            LoginBean logBean = new LoginBean(login, "admin");
            logBean.setAuthd(true);
            sess.setAttribute("loginBean", logBean);

            return ("adminHome");
        }//elseif

        // wrong login
        return ("failure");
    }

    /**
     * Function for logging in a staff-member
     * Will fetch the row from TStaff, put the loginBean into the session
     * and further more it fetches the staff's tickets and new tickets
     * and puts them into the session as well.
     * @param sess The HTTP-Session
     * @param errors ActionErrors
     * @param login Staff's Login
     * @param id Staff's ID
     * @return boolean True if worked
     */
    private boolean staffLogin(HttpSession sess, ActionErrors errors,
        String login, int id)
    {
        // StaffBean
        StaffBean staff = new StaffBean();
        String staffSql =
            "SELECT s.sId staffID, s.sName staffName, s.sFirst staffFirst, s.sLogin staffLogin, " +
            "s.sPic staffPic, g.gId groupID, g.gText groupname " +
            "FROM TStaff s INNER JOIN TGroupMembers gm ON s.sId = gm.gmStaff " +
            "INNER JOIN TGroup g ON gm.gmGroup = g.gId " + "WHERE s.sLogin ='" +
            login + "';";

	DataBaseConnection db = new DataBaseConnection();
        ResultSet rs = db.getRSfromStatement(staffSql);

        try
        {
            if(rs.next())
            {
                staff.setId(rs.getInt("staffID"));
                staff.setName(rs.getString("staffName"));
                staff.setFirstname(rs.getString("staffFirst"));
                staff.setLogin(rs.getString("staffLogin"));
                staff.setPic(rs.getString("staffPic"));

                // get all groupIDs and names and save in Vector
                Vector groupIDVec = new Vector();
                Vector groupnameVec = new Vector();

                do
                {
                    if(rs.getString(1) != null)
                    {
                        groupIDVec.add(new Integer(rs.getInt("groupID")));
                        groupnameVec.add(rs.getString("groupname"));
                    }//if
                }
                while(rs.next());

                db.closeResultSet(rs);

                // arrays for handing over to StaffBean
                String[] groupnamesArray = new String[groupnameVec.size()];
                int[] groupIDArray = new int[groupIDVec.size()];

                // transform Vectors to Arrays
                for(int i = 0; i < groupIDVec.size(); i++)
                {
                    groupIDArray[i] = ((Integer) groupIDVec.get(i)).intValue();
                }//for

                for(int i = 0; i < groupnameVec.size(); i++)
                {
                    groupnamesArray[i] = (String) groupnameVec.get(i);
                }//for

                // put it into the bean finally...
                staff.setGroup(groupIDArray);
                staff.setGroupName(groupnamesArray);

                // off it goes
                sess.setAttribute("staffBean", staff);
            }//if
            // if no tickets yet, insert an empty array to avoid trouble
            else
            {
              staff.setGroup(new int[0]);
              staff.setGroupName(new String[0]);
              sess.setAttribute("staffBean", staff);
            }//else
        }//try
        catch(SQLException ex)
        {
            errors.add("general", new ActionError("error.database.select"));
	    db.closeResultSet(rs);

            return (false);
        }//catch

	db.closeResultSet(rs);

        // Get new tickets which are not taken over yet
        Vector newTickets = GetTicket.getnewTickets(login, id, errors);
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
        Vector staffsTickets = GetTicket.getstaffsTickets(login, id, errors);

        if(staffsTickets == null)
        {
            sess.setAttribute("staffsTickets", new Vector());

            return (true);
        }//if

        // into session
        sess.setAttribute("staffsTickets", staffsTickets);

        // everything went well so far? good.
        return (true);
    }
}
