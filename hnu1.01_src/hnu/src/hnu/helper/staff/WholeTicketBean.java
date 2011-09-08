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

public class WholeTicketBean extends TicketBean implements Serializable
{
    /** All messages */
    private java.util.Vector messages = null;

    /** empty constructor */
    public WholeTicketBean()
    {
    }

    /**
     * Set messages
     * @param messages all ticket's messages
     */
    public void setMessages(java.util.Vector messages)
    {
        this.messages = messages;
    }

    /**
     * Get all messages
     * @return Vector All messages
     */
    public java.util.Vector getMessages()
    {
        return messages;
    }
}