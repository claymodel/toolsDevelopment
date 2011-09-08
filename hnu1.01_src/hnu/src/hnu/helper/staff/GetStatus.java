package hnu.helper.staff;

import hnu.helper.DataBaseConnection;

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

public class GetStatus {
    /** empty constructor */
    public GetStatus() {
    }

    /**
     * Get all status from TStatus as Vector
     * @param errors Struts ActionErrors for reporting problems
     * @return Vector all status
     */
    public static Vector getStatus(ActionErrors errors) {
        Vector ticketStatus = new Vector();

        String tmpSql = "SELECT st.stId statusID, st.stText statusName FROM TStatus st;";
        DataBaseConnection db = new DataBaseConnection();
        ResultSet rs = db.getRSfromStatement(tmpSql);

        try {
            while (rs.next()) {
                TicketStatus status = new TicketStatus();

                status.setStatusID(rs.getInt("statusID"));
                status.setName(rs.getString("statusName"));

                ticketStatus.add(status);
            }

            //while
            db.closeResultSet(rs);

            return ticketStatus;
        } catch (Exception ex) {
            errors.add("general", new ActionError("error.database.select"));
            db.closeResultSet(rs);

            return (null);
        }
    }
}
