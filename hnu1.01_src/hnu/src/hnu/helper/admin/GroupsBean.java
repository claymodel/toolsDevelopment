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

public class GroupsBean implements Serializable {
    public Vector allGroups = new Vector();
    private String size;
    private String sortBy;
    private boolean fillBean;

    /**
     * Bean constructor
     */
    public GroupsBean() {
    }

    /**
     * Fetches all groups from dataBase and creates groupBeans.
     */
    private void fillBean() throws Exception {
        String sql = "SELECT g.gId, g.gText, COUNT(gm.gmGroup) members FROM TGroup g LEFT OUTER JOIN TGroupMembers gm ON gm.gmGroup = g.gId GROUP BY g.gId";

        if ((sortBy != null) && (sortBy.trim().equals("gId") || sortBy.trim().equals("gText"))) {
            sql += (" ORDER BY g." + sortBy);
        } else {
            sql += " ORDER BY g.gText";
        }

        DataBaseConnection db = new DataBaseConnection();
        ResultSet rs = db.getRSfromStatement(sql);

        try {
            while (rs.next()) {
                allGroups.add(new GroupBean(rs.getString("gId"), rs.getString("gText"), rs.getString("members")));
            }
        } catch (SQLException ex) {
            db.closeResultSet(rs);
            throw ex;
        }

        db.closeResultSet(rs);
        size = Integer.toString(allGroups.size());
    }

    /**
     * Returns Vector containing groupBeans
     * @return Vector containing groupBeans
     */
    public Vector getAllGroups() {
        return allGroups;
    }

    /**
     * Sets Vector containing groupBeans
     * @param Vector containing groupBeans
     */
    public void setAllGroups(Vector allGroups) {
        this.allGroups = allGroups;
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
     * Returns if bean is filled with data (admin-accounts)
     * @return if bean is filled with data (admin-accounts)
     */
    public boolean isFillBean() {
        return fillBean;
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
}
