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

public class GetTicket
{
    /** If >-1, this specific ID is fetched */
    private static int specificId = -1;

    /** empty constructor */
    public GetTicket()
    {
    }

   /**
    * Fetch all new, unowned tickets from TTicket
    * @param login Staff's login
    * @param id Staff's ID
    * @param errors Struts ActionErrors for reporting problems
    * @return Vector all new tickets belonging to staff's group(s)
    */
    public static Vector getnewTickets(String login, int id, ActionErrors errors)
    {
        // Collection of Tickets
        Vector ticketsColl = new Vector();
        String ticketsSql =
            "SELECT t.tId ticketID, u.uLogin uLogin, t.tSubject Subject, t.tStaff staff, g.gText gName, " +
            "st.stText tStatus, t.tDate timeSince, t.tPriority priority " +
            "FROM TTicket t INNER JOIN TGroupMembers gm ON t.tGroup = gm.gmGroup " +
            "INNER JOIN TStaff s ON gm.gmStaff = s.sId " +
            "INNER JOIN TGroup g ON gm.gmGroup = g.gId " +
            "INNER JOIN TUser u ON u.uId = t.tUser " +
            "INNER JOIN TStatus st ON st.stId = t.tStatus " +
            "WHERE s.sLogin = '" + login + "' " + "AND t.tStaff = 0 AND t.tStatus!=4 " +
            "ORDER BY priority ASC, timeSince DESC, uLogin;";

	DataBaseConnection db = new DataBaseConnection();
        ResultSet rs = db.getRSfromStatement(ticketsSql);
        DateFormatter df = new DateFormatter();

        try
        {
            while(rs.next())
            {
                // create one ticket from the current row
                TicketBean ticket = new TicketBean();
                ticket.setId(rs.getInt("ticketID"));
                ticket.setUser(rs.getString("uLogin"));
                ticket.setSubject(rs.getString("Subject"));
                ticket.setGroup(rs.getString("gName"));
                ticket.setStatus(rs.getString("tStatus"));
                ticket.setTimeSince(df.getDate(rs.getTimestamp("timeSince")));
                ticket.setPriority(rs.getByte("priority"));
                ticket.setStaff(rs.getInt("staff"));

                // put into the collection
                ticketsColl.add(ticket);
            }//while
	    db.closeResultSet(rs);

            return (ticketsColl);
        }//try
        catch(Exception ex)
        {
            errors.add("general", new ActionError("error.database.select"));
	    db.closeResultSet(rs);

            return (null);
        }//catch
    }

    /**
     * Fetch all tickets this staff has taken over; exclude those which are solved!<br />
     * If specificId is set >-1, will only fetch this ticket
     * @param login Staff's login
     * @param id Staff's id
     * @param errors Struts ActionErrors
     * @return Vector all or a specific one of staff's tickets
     */
    public static Vector getstaffsTickets(String login, int id,
        ActionErrors errors)
    {
        // Collection of Tickets
        Vector ticketsColl = new Vector();
        String ticketsSql =
            "SELECT t.tId ticketID, u.uLogin uLogin, t.tSubject Subject, t.tStaff staff, g.gText gName, " +
            "st.stText tStatus, t.tDate timeSince, t.tPriority priority " +
            "FROM TTicket t INNER JOIN TGroupMembers gm ON t.tGroup = gm.gmGroup " +
            "INNER JOIN TStaff s ON gm.gmStaff = s.sId " +
            "INNER JOIN TGroup g ON gm.gmGroup = g.gId " +
            "INNER JOIN TUser u ON u.uId = t.tUser " +
            "INNER JOIN TStatus st ON st.stId = t.tStatus " +
            "WHERE s.sLogin = '" + login + "' " + "AND t.tStaff = " + id + " ";

        // if specificId is set, then select a specific ticket
        if(specificId > -1)
        {
            ticketsSql += ("AND t.tId = " + getSpecificId() + " ");
        }
         //if

        ticketsSql += "AND t.tStatus!=4 ORDER BY priority ASC, timeSince DESC, uLogin;";

	DataBaseConnection db = new DataBaseConnection();
        ResultSet rs = db.getRSfromStatement(ticketsSql);
        DateFormatter df = new DateFormatter();

        try
        {
            while(rs.next())
            {
                // create one ticket from the current row
                TicketBean ticket = new TicketBean();
                ticket.setId(rs.getInt("ticketID"));
                ticket.setUser(rs.getString("uLogin"));
                ticket.setSubject(rs.getString("Subject"));
                ticket.setGroup(rs.getString("gName"));
                ticket.setStatus(rs.getString("tStatus"));
                ticket.setTimeSince(df.getDate(rs.getTimestamp("timeSince")));
                ticket.setPriority(rs.getByte("priority"));
                ticket.setStaff(rs.getInt("staff"));

                // put into the collection
                ticketsColl.add(ticket);
            }//while

	    db.closeResultSet(rs);
            return (ticketsColl);
        }//try
        catch(Exception ex)
        {
            errors.add("general", new ActionError("error.database.select"));

	    db.closeResultSet(rs);
            return (null);
        }//catch
    }

    /**
     * Fetch <strong>all</strong> tickets this staff has taken over<br />
     * Includes solved ones<br />
     * If specificId is set >-1, will only fetch this ticket
     * @param login Staff's login
     * @param id Staff's id
     * @param errors Struts ActionErrors
     * @return Vector all or a specific one of staff's tickets
     */
    public static Vector getallstaffsTickets(String login, int id,
        ActionErrors errors)
    {
        // Collection of Tickets
        Vector ticketsColl = new Vector();
        String ticketsSql =
            "SELECT t.tId ticketID, u.uLogin uLogin, t.tSubject Subject, t.tStaff staff, g.gText gName, " +
            "st.stText tStatus, t.tDate timeSince, t.tPriority priority " +
            "FROM TTicket t INNER JOIN TGroupMembers gm ON t.tGroup = gm.gmGroup " +
            "INNER JOIN TStaff s ON gm.gmStaff = s.sId " +
            "INNER JOIN TGroup g ON gm.gmGroup = g.gId " +
            "INNER JOIN TUser u ON u.uId = t.tUser " +
            "INNER JOIN TStatus st ON st.stId = t.tStatus " +
            "WHERE s.sLogin = '" + login + "' " + "AND t.tStaff = " + id + " ";

        // if specificId is set, then select a specific ticket
        if(specificId > -1)
        {
            ticketsSql += ("AND t.tId = " + getSpecificId() + " ");
        }
         //if

        ticketsSql += "ORDER BY priority ASC, timeSince DESC, uLogin;";

	DataBaseConnection db = new DataBaseConnection();
        ResultSet rs = db.getRSfromStatement(ticketsSql);
        DateFormatter df = new DateFormatter();

        try
        {
            while(rs.next())
            {
                // create one ticket from the current row
                TicketBean ticket = new TicketBean();
                ticket.setId(rs.getInt("ticketID"));
                ticket.setUser(rs.getString("uLogin"));
                ticket.setSubject(rs.getString("Subject"));
                ticket.setGroup(rs.getString("gName"));
                ticket.setStatus(rs.getString("tStatus"));
                ticket.setTimeSince(df.getDate(rs.getTimestamp("timeSince")));
                ticket.setPriority(rs.getByte("priority"));
                ticket.setStaff(rs.getInt("staff"));

                // put into the collection
                ticketsColl.add(ticket);
            }//while

	    db.closeResultSet(rs);
            return (ticketsColl);
        }//try
        catch(Exception ex)
        {
            errors.add("general", new ActionError("error.database.select"));

	    db.closeResultSet(rs);
            return (null);
        }//catch
    }

    public static void setSpecificId(int Id)
    {
        specificId = Id;
    }

    public static int getSpecificId()
    {
        return specificId;
    }
}