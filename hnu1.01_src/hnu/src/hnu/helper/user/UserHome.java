package hnu.helper.user;

import hnu.helper.DataBaseConnection;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

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

public class UserHome implements Serializable {
	private java.util.Vector allUserTickets = new Vector();
	private String id;
	private int ticketCount;
	private String login;

	/**
	 * Bean constructor Fills the Vector allUserTickets with the Tickets auf a
	 * user
	 */
	public UserHome(String login, boolean existingUser) throws Exception {
		if (existingUser) {
			DataBaseConnection db = new DataBaseConnection();
			ResultSet rs = null;

			try {
				rs =
					db.getRSfromStatement(
						"SELECT t.tId, st.stText, t.tSubject, u.uLogin, u.uId FROM TTicket t inner join TUser u on t.tUser = u.uId inner join TStatus st on st.stId = t.tStatus WHERE u.uLogin='"
							+ login
							+ "' ORDER BY tDate;");

				while (rs.next()) {
					allUserTickets.add(
						new Ticket(
							rs.getString("tSubject"),
							rs.getString("stText"),
							rs.getString("tId")));
					ticketCount++;
					this.id = rs.getString("uId");
					this.login = rs.getString("uLogin");
				}
			} catch (SQLException ex) {
				db.closeResultSet(rs);
				throw ex;
			}

			db.closeResultSet(rs);
		}
	}

	/**
	 * Returns allUserTickets
	 * 
	 * @return allUserTickets
	 */
	public java.util.Vector getAllUserTickets() {
		return allUserTickets;
	}

	/**
	 * Sets allUserTickets
	 * 
	 * @param Vector
	 *            allUserTickets
	 */
	public void setAllUserTickets(java.util.Vector allUserTickets) {
		this.allUserTickets = allUserTickets;
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
	 * Returns id
	 * 
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets ticketCount
	 * 
	 * @param int
	 *            ticketCount
	 */
	public void setTicketCount(int ticketCount) {
		this.ticketCount = ticketCount;
	}

	/**
	 * Returns ticketCount
	 * 
	 * @return ticketCount
	 */
	public int getTicketCount() {
		return ticketCount;
	}

	/**
	 * Sets login
	 * 
	 * @param String
	 *            login
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * Returns login
	 * 
	 * @return login
	 */
	public String getLogin() {
		return login;
	}
}
