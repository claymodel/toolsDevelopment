package hnu.helper.admin;

import hnu.helper.DataBaseConnection;
import hnu.helper.StringArrayJoiner;

import java.sql.ResultSet;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

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

public class UserDeleter {
    /**
    * Bean constructor
    */
    public UserDeleter() {
    }

    /**
     * Executes delete operation.<br />
     * Method deletes all users in the database with the id the String[] is containing
     * and the corresponding tickets.
    */
    public ActionErrors execute(String[] usersToDelete) {
        return execute(StringArrayJoiner.getJoinedString(usersToDelete, ",", true));
    }

    /**
     * Executes delete operation.<br />
     * Method deletes all users in the database with the id the String is containing
     * and the corresponding tickets.
    */
    public ActionErrors execute(String usersToDelete) {
        ActionErrors errors = new ActionErrors();
        String[] tId = null;

        String sql = "";

        if (usersToDelete.indexOf(",") != -1) {
            sql = "DELETE FROM TUser WHERE uId IN(" + usersToDelete + ")";
        } else {
            if (usersToDelete.indexOf("'") == -1) {
                usersToDelete = "'" + usersToDelete + "'";
            }

            sql = "DELETE FROM TUser WHERE uId=" + usersToDelete;
        }

	DataBaseConnection db = new DataBaseConnection();
        ResultSet rs = db.getRSfromStatement("SELECT t.tId FROM TTicket t INNER JOIN TUser u ON t.tUser = u.uId WHERE u.uId IN(" + usersToDelete + ")");
        String tickets = DataBaseConnection.getValuesStringFromResultSet(rs, 1, false);
	db.closeResultSet(rs);

        TicketDeleter ticketDeleter = new TicketDeleter();

        ticketDeleter.execute(tickets);

        if (!DataBaseConnection.execute(sql)) {
            errors.add("dberror", new ActionError("error.database.delete"));
        }

        return errors;
    }
}
