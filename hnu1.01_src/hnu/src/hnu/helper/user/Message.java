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

public class Message implements Serializable {
	private String date;
	private String text;
	private String author;

	/**
	 * Bean constructor
	 */
	public Message(String date, String text, String author) {
		this.date = date;
		this.text = text;
		this.author = author;
	}

	/**
	 * Returns Date
	 * 
	 * @return Date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets Date
	 * 
	 * @param String
	 *            Date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Sets text
	 * 
	 * @param String
	 *            text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Returns text
	 * 
	 * @param text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets author
	 * 
	 * @param String
	 *            author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * Returns author
	 * 
	 * @return author
	 */
	public String getAuthor() {
		return author;
	}
}