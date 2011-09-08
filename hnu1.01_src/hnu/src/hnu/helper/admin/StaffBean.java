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

public class StaffBean implements Serializable {
    private String name;
    private String id;
    private String first;
    private String login;
    private boolean member;

    /**
     * Bean constructor
     */
    public StaffBean(String id, String name, String first, String login, boolean member) {
        this.login = login;
        this.name = name;
        this.id = id;
        this.first = first;
        this.member = member;
    }

    /**
     * Returns name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets ID
     * @param String ID
     */
    public String getId() {
        return id;
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
     * Sets loginname
     * @param String loginname
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Returns loginname
     * @return loginname
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets if is member of current group
     * @param String if is member of current group
     */
    public void setMember(boolean member) {
        this.member = member;
    }

    /**
     * Returns if is member of current group
     * @return if is member of current group
     */
    public boolean isMember() {
        return member;
    }
}
