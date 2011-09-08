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

public class TicketStatus implements Serializable
{
    /** StatusID */
    private int statusID;

    /** Statustext*/
    private String name;

    /** Empty constructor*/
    public TicketStatus()
    {
    }

    /**
     * Get StatusID
     * @return int StatusID
     */
    public int getStatusID()
    {
        return statusID;
    }

    /**
     * Set statusID
     * @param statusID The ID
     */
    public void setStatusID(int statusID)
    {
        this.statusID = statusID;
    }

    /**
     * Set StatusName
     * @param name Text
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get the text
     * @return String Statustext
     */
    public String getName()
    {
        return name;
    }
}