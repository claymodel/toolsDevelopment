package hnu;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
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

public class LoginForm extends org.apache.struts.action.ActionForm
{
    /** Staff login */
    private String login;

    /** Staff pass */
    private String pass;

    /**
     * Reset the properties
     * @param mapping Struts ActionMapping
     * @param request The HTTPRequest
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        login = "";
        pass = "";
    }

    /**
     * Validate if input is generally OK (not emtpty..)
     * @param mapping Struts ActionMapping
     * @param request The HTTPRequest
     * @return ActionErrors collection of all ActionError-Objects
     */
    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();

        // check if input is useful
        // login
        if((login == null) || (login.equals("")))
        {
            errors.add("login",
                new ActionError("error.staff.login.login.missing"));
        }

        //if
        // pass
        if((pass == null) || (pass.equals("")))
        {
            errors.add("pass", new ActionError("error.staff.login.pass.missing"));
        }

        return errors;
    }

    /**
     * Returns the login attribute
     * @return String login
     */
    public String getLogin()
    {
        return login;
    }

    /**
     * Set the login attribute
     * @param login The login
     */
    public void setLogin(String login)
    {
        this.login = login;
    }

    /**
     * Get the password
     * @return String pass
     */
    public String getPass()
    {
        return pass;
    }

    /**
     * Set the password attribute
     * @param pass The password
     */
    public void setPass(String pass)
    {
        this.pass = pass;
    }
}