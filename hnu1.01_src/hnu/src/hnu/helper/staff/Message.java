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

public class Message implements Serializable
{
    /** Message date */
    private String date;

    /** Message body */
    private String body;

    /** Message author */
    private String author;

    /**
     * Constructor
     * @param author Message author
     * @param body Message body
     * @param date Message date
     */
    public Message(String author, String body, String date)
    {
        this.setAuthor(author);
        this.setBody(body);
        this.setDate(date);
    }

    /** empty constructor */
    public Message()
    {
    }

  // Date
    /**
     * Get messagedate
     * @return String Date
     */
    public String getDate()
    {
        return date;
    }

    /**
     * Set message date
     * @param date The date
     */
    public void setDate(String date)
    {
        this.date = date;
    }

  // Body
    /**
     * Set messagebody
     * @param body The text
     */
    public void setBody(String body)
    {
        this.body = body;
    }

    /**
     * Get messagebody
     * @return String The text
     */
    public String getBody()
    {
        return body;
    }

  // Author
    /**
     * Set messageauthor
     * @param author The author
     */
    public void setAuthor(String author)
    {
        this.author = author;
    }

    /**
     * Get messageauthor
     * @return String The author
     */
    public String getAuthor()
    {
        return author;
    }
}
