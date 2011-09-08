package hnu.helper.admin;

import hnu.helper.DataBaseConnection;
import hnu.helper.StringArrayJoiner;

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

public class StaffCollectionBean implements Serializable {
    public Vector allStaff = new Vector();
    private String group;
    private String size;
    private String groupName;
    private String sortBy;
    private boolean fillBean;

    /**
     * Bean constructor
     */
    public StaffCollectionBean() {
    }

    /**
     * Bean constructor.
     * Fills Bean.
     */
    public StaffCollectionBean(String gId, String sortBy) throws Exception {
        this.sortBy = sortBy;
        fillBean(gId);
    }

    /**
     * Fetches all staff-accounts from dataBase and creates staffBeans.
     */
    private void fillBean(String gId) throws SQLException {
        if (gId != null) {
            group = gId;
        } else {
            group = "";
        }

        ResultSet rs = null;
        String sql = "SELECT sId, sName, sFirst, sLogin FROM TStaff";

        if (gId != null) {
            sql += (" s INNER JOIN TGroupMembers g ON s.sId = g.gmStaff WHERE g.gmGroup=" + gId);

            DataBaseConnection db = new DataBaseConnection();

            rs = db.getRSfromStatement("SELECT gText from TGroup WHERE gId='" + gId + "'");

            if (rs.next()) {
                try {
                    groupName = rs.getString(1);
                } catch (SQLException ex) {
                    groupName = "Unknown Groupname";
                }
            }

            db.closeResultSet(rs);
        }

        boolean sorted = false;

        if ((sortBy != null) && (sortBy.trim().equals("sId") || sortBy.trim().equals("sName") || sortBy.trim().equals("sFirst") || sortBy.trim().equals("sLogin"))) {
            sql += (" ORDER BY " + sortBy);
            sorted = true;
        } else {
            sql += " ORDER BY sLogin";
        }

        DataBaseConnection db = new DataBaseConnection();

        rs = db.getRSfromStatement(sql);

        Vector membersVector = null;

        try {
            membersVector = new Vector();

            while (rs.next()) {
                allStaff.add(new StaffBean(rs.getString("sId"), rs.getString("sName"), rs.getString("sFirst"), rs.getString("sLogin"), true));
                membersVector.add(rs.getString("sId"));
            }
        } catch (SQLException ex) {
            db.closeResultSet(rs);
            throw ex;
        }

        db.closeResultSet(rs);

        if (gId != null) {
            String members = StringArrayJoiner.getJoinedString(membersVector, ",", true);

            sql = "SELECT sId, sName, sFirst, sLogin FROM TStaff WHERE sId NOT IN (" + members + ")";

            if (sorted) {
                sql += (" ORDER BY " + sortBy);
            } else {
                sql += " ORDER BY sLogin";
            }

            db = new DataBaseConnection();
            rs = db.getRSfromStatement(sql);

            while (rs.next()) {
                allStaff.add(new StaffBean(rs.getString("sId"), rs.getString("sName"), rs.getString("sFirst"), rs.getString("sLogin"), false));
            }

            db.closeResultSet(rs);
        }

        size = Integer.toString(allStaff.size());
    }

    /**
     * Returns Vector containing staffBeans
     * @return Vector containing staffBeans
     */
    public Vector getAllStaff() {
        return allStaff;
    }

    /**
     * Sets Vector containing staffBeans
     * @param String Vector containing staffBeans
     */
    public void setAllStaff(Vector allStaff) {
        this.allStaff = allStaff;
    }

    /**
     * Sets group
     * @param String group
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * Returns group
     * @return group
     */
    public String getGroup() {
        return group;
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
     * Sets groupname
     * @param String groupname
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Returns groupname
     * @return groupname
     */
    public String getGroupName() {
        return groupName;
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
                fillBean(null);
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
