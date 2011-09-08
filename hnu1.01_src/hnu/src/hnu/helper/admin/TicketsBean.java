package hnu.helper.admin;

import hnu.helper.DataBaseConnection;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

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

public class TicketsBean implements Serializable {
    public Vector allTickets = new Vector();
    private String size;
    private String sortBy;
    private boolean fillBean;

    /**
     * Bean constructor
     */
    public TicketsBean() throws Exception {
    }

    /**
     * Fetches all tickets from dataBase and creates groupBeans.
     */
    private void fillBean() throws Exception {
        String sql = "SELECT tId, tDate, tPriority, tSubject FROM TTicket";

        if ((sortBy != null) && (sortBy.trim().equals("tDate") || sortBy.trim().equals("tId") || sortBy.trim().equals("tPriority") || sortBy.trim().equals("tSubject"))) {
            sql += (" ORDER BY " + sortBy);
        } else {
            sql += " ORDER BY tDate DESC";
        }

        DataBaseConnection db = new DataBaseConnection();
        ResultSet rs = db.getRSfromStatement(sql);

        try {
            while (rs.next()) {
                allTickets.add(new TicketBean(rs.getString("tId"), rs.getDate("tDate"), rs.getString("tPriority"), rs.getString("tSubject")));
            }
        } catch (SQLException ex) {
            db.closeResultSet(rs);
            throw ex;
        }

        db.closeResultSet(rs);

        size = Integer.toString(allTickets.size());
    }

    /**
     * Returns Vector containing ticketBeans
     * @return Vector containing ticketBeans
     */
    public Vector getAllTickets() {
        return allTickets;
    }

    /**
     * Returns Vector containing ticketBeans
     * @return Vector containing ticketBeans
     */
    public void setAllTickets(Vector allTickets) {
        this.allTickets = allTickets;
    }

    /**
     * Sets size of vector
     * @param String size of vector
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * Returns size of vector
     * @return size of vector
     */
    public String getSize() {
        return size;
    }

    /**
     * Sets parameter to order adminBeans
     * @param String parameter to order adminBeans
     */
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    /**
     * Returns parameter to order adminBeans
     * @return parameter to order adminBeans
     */
    public String getSortBy() {
        return sortBy;
    }

    /**
     * Sets command to fill the bean
     * @param String command to fill the bean
     */
    public void setFillBean(boolean fillBean) {
        this.fillBean = fillBean;

        if (fillBean) {
            try {
                fillBean();
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Returns if bean is filled with data (admin-accounts)
     * @return if bean is filled with data (admin-accounts)
     */
    public boolean isFillBean() {
        return fillBean;
    }
}
