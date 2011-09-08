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

public class Group implements Serializable {
	private String id;
	private String text;

	/**
	 * Bean constructor
	 */
	public Group(String id, String text) {
		this.id = id;
		this.text = text;
	}

	/**
	 * Returns id
	 * 
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets id
	 * 
	 * @param String
	 *            id
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return text
	 */
	public String getText() {
		return text;
	}
}