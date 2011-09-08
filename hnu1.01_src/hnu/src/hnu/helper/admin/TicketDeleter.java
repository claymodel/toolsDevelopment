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

public class TicketDeleter {
    /**
    * Bean constructor
    */
    public TicketDeleter() {
    }

    /**
     * Executes delete operation.<br />
     * Method deletes all tickets in the database with the id the String[] is containing
     * and the corresponding messages.
    */
    public ActionErrors execute(String[] ticketsToDelete) {
        return execute(StringArrayJoiner.getJoinedString(ticketsToDelete, ",", true));
    }

    /**
     * Executes delete operation.<br />
     * Method deletes all tickets in the database with the id the String is containing
     * and the corresponding messages.
    */
    public ActionErrors execute(String ticketsToDelete) {
        ActionErrors errors = new ActionErrors();

        String sql = "";

        if (ticketsToDelete.indexOf(",") != -1) {
            sql = "DELETE FROM TTicket WHERE tId IN(" + ticketsToDelete + ")";
        } else {
            if (ticketsToDelete.indexOf("'") == -1) {
                ticketsToDelete = "'" + ticketsToDelete + "'";
            }

            sql = "DELETE FROM TTicket WHERE tId=" + ticketsToDelete;
        }

	DataBaseConnection db = new DataBaseConnection();
        ResultSet rs = db.getRSfromStatement("SELECT m.mId FROM TMessage m INNER JOIN TTicket t ON m.mTicket = t.tId WHERE t.tId IN(" + ticketsToDelete + ")");
        String messages = DataBaseConnection.getValuesStringFromResultSet(rs, 1, false);
	db.closeResultSet(rs);

        MessageDeleter messageDeleter = new MessageDeleter();

        messageDeleter.execute(messages);

        if (!DataBaseConnection.execute(sql)) {
            errors.add("dberror", new ActionError("error.database.delete"));
        }

        return errors;
    }
}
