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

public class StatsBean implements Serializable
{
    /** tickets total */
    private int ticketsTotal = 0;

    /** Tickets owned by the one staffmember */
    private int ticketsStaff = 0;

    /** Tickets owned by the one staffmember, listed per status */
    private java.util.Vector ticketsStaffPerStatus = new java.util.Vector();

    /** number of messages from the one staff */
    private int messagesStaff=0;

    /** empty constructor */
    public StatsBean()
    {
    }

    /**
     * Get tickets total
     * @return int How many tickets in TTicket
     */
    public int getTicketsTotal()
    {
        return ticketsTotal;
    }

    /**
     * Set tickets total
     * @param ticketsTotal how many tickets in TTicket
     */
    public void setTicketsTotal(int ticketsTotal)
    {
        this.ticketsTotal = ticketsTotal;
    }

    /**
     * Set how many tickets owned by the one staffmember
     * @param ticketsStaff number of tickets
     */
    public void setTicketsStaff(int ticketsStaff)
    {
        this.ticketsStaff = ticketsStaff;
    }

    /**
     * How many tickets owned by the one staffmember
     * @return int unmber of tickets
     */
    public int getTicketsStaff()
    {
        return ticketsStaff;
    }

    /**
     * Set, how many tickets owned by the one staffmember,
     * in a vector, per status
     * @param ticketsStaffPerStatus staff's tickets per status in a Vector
     */
    public void setTicketsStaffPerStatus(java.util.Vector ticketsStaffPerStatus)
    {
        this.ticketsStaffPerStatus = ticketsStaffPerStatus;
    }

    /**
     * Number of staff's tickets per status
     * @return Vector Vector with stats per status
     */
    public java.util.Vector getTicketsStaffPerStatus()
    {
        return ticketsStaffPerStatus;
    }

    /**
     * Set number of messages from the staff
     * @param messagesStaff number of messages
     */
    public void setMessagesStaff(int messagesStaff)
    {
        this.messagesStaff = messagesStaff;
    }

    /**
     * Get number of messages from the staff
     * @return int Number of messages
     */
    public int getMessagesStaff()
    {
        return messagesStaff;
    }
}
