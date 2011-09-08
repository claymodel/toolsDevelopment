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

public class AdminBean implements Serializable {
    public String name;
    private String id;

    /**
     * Bean constructor
     */
    public AdminBean(String name, String id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Returns Name
     * @return Name
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
     * Sets ID
     * @param String ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets Name
     * @param String Name
     */
    public void setName(String name) {
        this.name = name;
    }
}
