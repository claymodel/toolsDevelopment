package hnu.helper.admin;

import java.io.Serializable;

/*
 * Copyright (C) 2002-2003 Martin Maier <martin.maier@fh-joanneum.at>
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
 * @author Martin Maier
 */

public class UserBean implements Serializable {
    private String name;
    private String id;
    private String first;
    private String login;
    private String mail;
    private String active;

    /**
     * Bean constructor
     */
    public UserBean(String id, String name, String first, String login, String mail, String active) {
        this.name = name;
        this.id = id;
        this.first = first;
        this.login = login;
        this.mail = mail;
        this.active = active;
    }

    /**
     * Returns name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns ID
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets firstname
     * @param String firstname
     */
    public void setFirst(String first) {
        this.first = first;
    }

    /**
     * Returns firstname
     * @return firstname
     */
    public String getFirst() {
        return first;
    }

    /**
     * Sets login
     * @param String login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Returns login
     * @return login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets mail
     * @param String mail
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Returns mail
     * @return mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * Sets active
     * @param String active
     */
    public void setActive(String active) {
        this.active = active;
    }

    /**
     * Returns active
     * @return active
     */
    public String getActive() {
        return active;
    }

    /**
     * Sets ID
     * @param String ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets name
     * @param String name
     */
    public void setName(String name) {
        this.name = name;
    }
}
