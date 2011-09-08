package hnu.helper.staff;

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

public class StaffBean implements Serializable
{
    /** Family name */
    private String name = "";

    /** First name */
    private String firstname = "";

    /** Name used for logging in */
    private String login = "";

    /** The password for authenticating */
    private String pass = "";

    /** IDs of the groups the staffmember belongs to */
    private int[] group;

    /** URI to the staff's picture */
    private String pic = "";

    /** Names of the groups the staffmember belongs to */
    private String[] groupName;

    /** The ID of the corresponding row in the DB */
    private int id = -1;

    /** empty constructor */
    public StaffBean()
    {
    }

  // Name
    /**
     * Return staff's family name
     * @return String Name of staff
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set staff's family name
     * @param name Family name
     */
    public void setName(String name)
    {
        this.name = name;
    }

  // Firstname
    /**
     * Set staff's first name
     * @param firstname Staff firstname
     */
    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    /**
     * Returns staff's first name
     * @return String First name
     */
    public String getFirstname()
    {
        return firstname;
    }

  // Login
    /**
     * Set staff's login handle
     * @param login Staff login
     */
    public void setLogin(String login)
    {
        this.login = login;
    }

    /**
     * Return staff's login handle
     * @return String Staff login
     */
    public String getLogin()
    {
        return login;
    }

  // Password
    /**
     * Set staff's password from DB (Has to be <b>MD5</b> hashed!)
     * @param pass MD5 hashed pass
     */
    public void setPass(String pass)
    {
        this.pass = pass;
    }

    /**
     * Returns staff's password from DB (<b>MD5</b> hashed!)
     * @return String password
     */
    public String getPass()
    {
        return pass;
    }

  // GroupID
    /**
     * Set the groupID-array
     * @param group ID of the group
     */
    public void setGroup(int[] group)
    {
        this.group = group;
    }

    /**
     * Returns the ID of the group the staff belongs to in an array
     * @return int Group
     */
    public int[] getGroup()
    {
        return group;
    }

  // Picture
    /**
     * Set a URI to the staff's picture
     * @param pic URI to eh pic of the staffmember
     */
    public void setPic(String pic)
    {
        this.pic = pic;
    }

    /**
     * Returns a URI to the staff's picture
     * @return String URI
     */
    public String getPic()
    {
        return pic;
    }

  // Groupnames
    /**
     * Get the groupnames the staff belongs to
     * @return String groupname
     */
    public String[] getGroupName()
    {
        return groupName;
    }

    /**
     * Set the groupnames the staff belongs to
     * @return String groupname
     */
    public void setGroupName(String[] groupName)
    {
        this.groupName = groupName;
    }

  // ID
    /**
     * Set the ID of the DB-row this object is representing
     * @param id of the DB-row
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Returns the ID of the represented DB-row
     * @return int ID
     */
    public int getId()
    {
        return id;
    }
}