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

public class StatusCollectionBean implements Serializable {
    public Vector allStatus = new Vector();
    private String size;
    private String sortBy;
    private boolean fillBean;

    /**
     * Bean constructor
     */
    public StatusCollectionBean() {
    }

    /**
     * Fetches all status-entries from dataBase and creates statusBeans.
     */
    private void fillBean() throws Exception {
        String sql = "SELECT stId, stText FROM TStatus";

        if ((sortBy != null) && (sortBy.trim().equals("stId") || sortBy.trim().equals("stText"))) {
            sql += (" ORDER BY " + sortBy);
        } else {
            sql += " ORDER BY stText";
        }

        DataBaseConnection db = new DataBaseConnection();
        ResultSet rs = db.getRSfromStatement(sql);

        try {
            while (rs.next()) {
                allStatus.add(new StatusBean(rs.getString("stText"), rs.getString("stId")));
            }
        } catch (SQLException ex) {
	  db.closeResultSet(rs);
	  throw ex;
        }
        db.closeResultSet(rs);

        size = Integer.toString(allStatus.size());
    }

    /**
     * Returns Vector containing statusBeans
     * @return Vector containing statusBeans
     */
    public Vector getAllStatus() {
        return allStatus;
    }

    /**
     * Sets Vector containing statusBeans
     * @param String Vector containing statusBeans
     */
    public void setAllStatus(Vector allStatus) {
        this.allStatus = allStatus;
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
     * Sets parameter to order statusBeans
     * @param String parameter to order statusBeans
     */
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    /**
     * Returns parameter to order statusBeans
     * @return parameter to order statusBeans
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
     * Returns if bean is filled with data (status-entries)
     * @return if bean is filled with data (status-entries)
     */
    public boolean isFillBean() {
        return fillBean;
    }
}
