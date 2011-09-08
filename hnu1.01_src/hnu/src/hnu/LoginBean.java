package hnu;

import java.io.Serializable;


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

public class LoginBean implements Serializable
{
    /** User/Staff/Admin login */
    private String login = "";

    /** Any of the following: "user", "staff", "admin" */
    private String type = "";

    /** Successfully logged in? */
    private boolean authd = false;

    /** rowID from DB */
    private int id = -1;

    /** Empty constructor */
    public LoginBean()
    {
    }

    /**
     * Constructor
     * @param login the login
     * @param type Type: user/staff/admin
     */
    public LoginBean(String login, String type)
    {
        this.login = login;
        this.type = type;
    }

  // Login

    /**
     * Get the login
     * @return String login
     */
    public String getLogin()
    {
        return login;
    }

    /**
     * Set the login
     * @param login The login
     */
    public void setLogin(String login)
    {
        this.login = login;
    }

    // Type (user, staff, admin)

    /**
     * Set the type
     * @param type String "user", "staff" or "admin"
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * Get usertype
     * @return String Type of user
     */
    public String getType()
    {
        return type;
    }

  // Authd

    /**
     * Set whether logged in or not
     * @param authd Logged in = True
     */
    public void setAuthd(boolean authd)
    {
        this.authd = authd;
    }

    /**
     * Check if logged in
     * @return boolean True=is logged in
     */
    public boolean isAuthd()
    {
        return authd;
    }

  // id

    /**
     * Set the rowID for the datarow represented in this bean (sId, uId, aId)
     * _BE CAREFUL WITH THIS_ as it might be used for UPDATE statements
     * and this way you could pretty easy fsck the DB up
     * @param id int The ID
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Get the rowID of the datarow represented in this bean (sId, uId, aId)
     * @return int The ID
     */
    public int getId()
    {
        return id;
    }
}
