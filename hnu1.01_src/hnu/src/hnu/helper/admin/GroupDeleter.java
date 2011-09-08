package hnu.helper.admin;

import hnu.helper.DataBaseConnection;
import hnu.helper.StringArrayJoiner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

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

public class GroupDeleter {
    /**
    * Constructor
    */
    public GroupDeleter() {
    }

    /**
     * Executes delete operation.<br />
     * Method deletes all groups in the database with the id the String[] is containing.
    */
    public ActionErrors execute(String[] groupsToDelete) {
        return execute(StringArrayJoiner.getJoinedString(groupsToDelete, ",", true));
    }

    /**
     * Executes move operation.<br />
     * If an staff-account is member of a group to be deleted, method will move those accounts
     * to an other group.
    */
    public ActionErrors move(String[] groupsToDelete, String group) {
        return move(StringArrayJoiner.getJoinedString(groupsToDelete, ",", true), group);
    }

    /**
     * Executes delete operation.<br />
     * Method deletes all groups in the database with the id the String is containing.
    */
    public ActionErrors execute(String groupsToDelete) {
        ActionErrors errors = new ActionErrors();

        String sql = "";

        if (groupsToDelete.indexOf(",") != -1) {
            sql = "DELETE FROM TGroup WHERE gId IN(" + groupsToDelete + ")";
        } else {
            if (groupsToDelete.indexOf("'") == -1) {
                groupsToDelete = "'" + groupsToDelete + "'";
            }

            sql = "DELETE FROM TGroup WHERE gId=" + groupsToDelete;
        }

        if (!DataBaseConnection.execute(sql)) {
            errors.add("dbcloseerror", new ActionError("error.database.delete"));
        }

        return errors;
    }

    /**
     * Executes move operation.<br />
     * If an staff-account is member of a group to be deleted, method will move those accounts
     * to an other group.
    */
    public ActionErrors move(String usersGroups, String group) {
        ActionErrors errors = new ActionErrors();

        String sqlSelect = "";
        String sqlDelete = "";

        if (usersGroups.indexOf(",") != -1) { // just one account to delete
            sqlSelect = "SELECT DISTINCT gmStaff FROM TGroupMembers WHERE gmGroup IN(" + usersGroups + ")";
            sqlDelete = "DELETE FROM TGroupMember WHERE gmGroup IN(" + usersGroups + ")";
        } else { // more one account to delete
            if (usersGroups.indexOf("'") == -1) { //if not quoted
                usersGroups = "'" + usersGroups + "'";
            }

            sqlSelect = "SELECT DISTINCT gmStaff FROM TGroupMembers WHERE gmGroup=" + usersGroups;
            sqlDelete = "DELETE FROM TGroupMembers WHERE gmGroup=" + usersGroups;
        }

        DataBaseConnection db = new DataBaseConnection();
        ResultSet rs = db.getRSfromStatement(sqlSelect);

	Vector userToAdd = new Vector();
	try {
	  while (rs.next()) {
	    userToAdd.add(rs.getString("gmStaff"));
	  }
	} catch (SQLException ex) {

	}

        if (!DataBaseConnection.execute(sqlDelete)) {
            errors.add("dberror", new ActionError("error.database.delete"));
        }

        String sql = "";

        try {
	    Iterator it = userToAdd.iterator();
            while (it.hasNext()) {
                sql = "INSERT INTO TGroupMembers(gmStaff,gmGroup) VALUES ('" + it.next().toString() + "','" + group + "')";
                DataBaseConnection.execute(sql);
            }
        } catch (Exception ex) {

        }

        db.closeResultSet(rs);

        return errors;
    }
}
