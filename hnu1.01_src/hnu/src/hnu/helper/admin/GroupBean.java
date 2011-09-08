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

public class GroupBean implements Serializable {
    private String id;
    private String text;
    private String members;

    /**
     * Bean constructor
     */
    public GroupBean(String id, String text, String members) {
        this.text = text;
        this.id = id;
        this.members = members;
    }

    /**
     * Returns ID
     * @return ID
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
     * Sets group name
     * @param String group name
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns group name
     * @return group name
     */
    public String getText() {
        return text;
    }

    /**
     * Sets members of group
     * @param String members of group
     */
    public void setMembers(String members) {
        this.members = members;
    }

    /**
     * Returns members of group
     * @return members of group
     */
    public String getMembers() {
        return members;
    }
}
