package hnu.helper.admin;

import hnu.helper.DateFormatter;

import java.io.Serializable;
import java.util.Date;

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

public class TicketBean implements Serializable {
    private String id;
    private String subject;
    private String priority;
    private Date date;

    /**
     * Bean constructor
     */
    public TicketBean(String id, Date date, String priority, String subject) {
        this.id = id;
        this.date = date;
        this.priority = priority;
        this.subject = subject;
    }

    /**
     * Returns formatted Date
     * @return formatted Date
     */
    public String getDate() {
        DateFormatter df = new DateFormatter();

        return df.getDate(date);
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
     * Sets date
     * @param Date date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Sets subject
     * @param String subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Returns subject
     * @return subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets priority
     * @param String priority
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * Returns priority
     * @return priority
     */
    public String getPriority() {
        return priority;
    }
}
