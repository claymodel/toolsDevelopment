package hnu.helper.staff;

import hnu.helper.DataBaseConnection;
import hnu.helper.DateFormatter;

import java.sql.ResultSet;
import java.util.Vector;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;


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

public class GetMessagesHelper {
    /** empty Constructor */
    public GetMessagesHelper() {
    }

    /**
     * Get all messages to a certain ticket
     * @param ticketId ID of the ticket
     * @param errors Struts ActionErrors for reporting problems
     * @return Vector messages in a Vector
     */
    public static Vector getAllMessages(int ticketId, ActionErrors errors) {
        Vector allMessages = new Vector();

        // first, find out if its user or staff
        String tmpSql =
            "SELECT m.mId id, m.mAuthor author, s.sLogin sLogin, u.uLogin uLogin, m.mTicket ticketID, "
            + "m.mIsUser isUser, m.mDate datum, m.mText body FROM TMessage m LEFT OUTER JOIN TStaff s ON m.mAuthor = s.sId INNER JOIN "
            + "TUser u ON m.mAuthor = u.uId " + "WHERE m.mTicket=" + ticketId
            + " ORDER BY m.mDate DESC;";
        DataBaseConnection db = new DataBaseConnection();
        ResultSet rs = db.getRSfromStatement(tmpSql);

        try {
            while (rs.next()) {
                DateFormatter dateForm = new DateFormatter();
                hnu.helper.staff.Message message = new hnu.helper.staff.Message();
                String userName = rs.getString("uLogin");
                String staffName = rs.getString("sLogin");

                //String messageDate = rs.getString("datum");
                String messageDate = dateForm.getDate(rs.getTimestamp("datum"));
                String body = rs.getString("body");
                byte isUser = rs.getByte("isUser");

                // if user-message, show the userName
                if (isUser == 1) {
                    message.setAuthor(userName);
                } //if

                // otherwise staffName
                else if (isUser == 0) {
                    message.setAuthor(staffName);
                }
                // if all fails, set a default
                else {
                    message.setAuthor("unknown");
                }

                message.setBody(body);
                message.setDate(messageDate);

                // save in vector
                allMessages.add(message);
            }

            //while
            db.closeResultSet(rs);

            return allMessages;
        } catch (Exception ex) {
            errors.add("general", new ActionError("error.database.select"));
            db.closeResultSet(rs);

            return (null);
        }
    }
}
