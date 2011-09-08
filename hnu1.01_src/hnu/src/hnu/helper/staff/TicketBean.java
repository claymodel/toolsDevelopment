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

public class TicketBean implements Serializable
{
    /** Ticket ID */
    private int id;

    /** User ID */
    private String user;

    /** Ticket subject */
    private String subject;

    /** Group it was sent to */
    private String group;

    /** the status */
    private String status;

    /** When opened? */
    private String timeSince;

    /** Ticket-priority; 1=highest, 5=lowest */
    private byte priority;

    /** ID of the Staff who working on it */
    private int staff = 0;

    /** empty constructor */
    public TicketBean()
    {
    }

  // ID
    /**
     * Get ticket ID
     * @return int ticket ID
     */
    public int getId()
    {
        return id;
    }

    /**
     * Set the ticket ID
     * @param id ticket ID
     */
    public void setId(int id)
    {
        this.id = id;
    }

  // User
    /**
     * Set UserID
     * @param user UserID
     */
    public void setUser(String user)
    {
        this.user = user;
    }

    /**
     * Get UserID
     * @return String UserID
     */
    public String getUser()
    {
        return user;
    }

  // Subject
    /**
     * Set ticketsubject
     * @param subject The subject
     */
    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    /**
     * Get ticketsubject
     * @return String the subject
     */
    public String getSubject()
    {
        return subject;
    }

  // Group
    /**
     * Set Group the ticket was sent to
     * @param group StaffGroup
     */
    public void setGroup(String group)
    {
        this.group = group;
    }

    /**
     * Get the group it was sent to
     * @return String StaffGroup
     */
    public String getGroup()
    {
        return group;
    }

  // Status
    /**
     * Set ticketstatus
     * @param status Ticketstatus
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

    /**
    * Get the ticket's status
    * @return String the status
    */
    public String getStatus()
    {
        return status;
    }

  // Date
    /**
     * Set the date
     * @param timeSince the date
     */
    public void setTimeSince(String timeSince)
    {
        this.timeSince = timeSince;
    }

    /**
     * Get the date it was opened
     * @return String date
     */
    public String getTimeSince()
    {
        return timeSince;
    }

  // Priority
    /**
     * Set the ticket's priority (1=highest, 5=lowest)
     * @param priority Ticketpriority
     */
    public void setPriority(byte priority)
    {
        this.priority = priority;
    }

    /**
     * Get the ticket's priority(1=highest, 5=lowest)
     * @return byte the priority
     */
    public byte getPriority()
    {
        return priority;
    }

  // Staff
    /**
     * Set staffID who is working on it
     * @param staff a staff's ID
     */
    public void setStaff(int staff)
    {
        this.staff = staff;
    }

    /**
     * Get the Staff's ID who is working on it
     * @return int StaffID
     */
    public int getStaff()
    {
        return staff;
    }
}