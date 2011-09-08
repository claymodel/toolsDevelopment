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

public class UsersBean implements Serializable {
    public Vector allUsers = new Vector();
    private String size;
    private boolean fillBean;
    private String sortBy;

    /**
     * Bean constructor
     */
    public UsersBean() {
    }

    /**
     * Fetches all users from dataBase and creates userBeans.
     */
    private void fillBean() throws SQLException {
        String active = "";
        String sql = "SELECT uId, uName, uFirst, uLogin, uMail, uActive FROM TUser";

        if ((sortBy != null) && (sortBy.trim().equals("uId") || sortBy.trim().equals("uName") || sortBy.trim().equals("uFirst") || sortBy.trim().equals("uLogin") || sortBy.trim().equals("uMail") || sortBy.trim().equals("uActive"))) {
            sql += (" ORDER BY " + sortBy);
        } else {
            sql += " ORDER BY uLogin";
        }

	DataBaseConnection db = new DataBaseConnection();
        ResultSet rs = db.getRSfromStatement(sql);

	try {
	  while (rs.next()) {
	    if (rs.getInt("uActive") == 1) {
	      active = "Yes";
	    } else {
	      active = "No";
	    }

	    allUsers.add(new UserBean(rs.getString("uId"), rs.getString("uName"), rs.getString("uFirst"), rs.getString("uLogin"), rs.getString("uMail"), active));
	  }
	}
	catch (SQLException ex) {
	  db.closeResultSet(rs);
	  throw ex;
	}
	db.closeResultSet(rs);

        size = Integer.toString(allUsers.size());
    }

    /**
     * Returns Vector containing userBeans
     * @return Vector containing userBeans
     */
    public Vector getAllUsers() {
        return allUsers;
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

    /**
     * Sets parameter to order userBeans
     * @param String parameter to order userBeans
     */
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    /**
     * Returns parameter to order userBeans
     * @return parameter to order userBeans
     */
    public String getSortBy() {
        return sortBy;
    }
}
