package hnu.helper.user;

import java.io.Serializable;

/*
 * Copyright (C) 2002-2003 Peter Ortner <peter.ortner@fh-joanneum.at>
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
 * @author Peter Ortner
 */

public class Ticket implements Serializable {

	private String subject;
	private String status;
	private String ticketId;

	/**
	 * Bean constructor
	 */
	public Ticket(String subject, String status, String ticketId) {
		this.subject = subject;
		this.status = status;
		this.ticketId = ticketId;
	}

	/**
	 * Returns subject
	 * 
	 * @return subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets subject
	 * 
	 * @param String
	 *            subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Sets status
	 * 
	 * @param String
	 *            status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Returns status
	 * 
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets id
	 * 
	 * @param String
	 *            id
	 */
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	/**
	 * Returns id
	 * 
	 * @return id
	 */
	public String getTicketId() {
		return ticketId;
	}
}