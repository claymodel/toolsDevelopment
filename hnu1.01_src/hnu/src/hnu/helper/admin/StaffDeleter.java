package hnu.helper.admin;

import hnu.helper.DataBaseConnection;
import hnu.helper.StringArrayJoiner;

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

public class StaffDeleter {
    /**
     * Constructor
     */
    public StaffDeleter() {
    }
    /**
     * Executes delete operation.<br />
     * Method deletes all staff-accounts in the database with the id the String[] is containing
     * and all corresponing groupmemberships.
     */
    public ActionErrors execute(String[] messagesToDelete) {
        return execute(StringArrayJoiner.getJoinedString(messagesToDelete, ",", true));
    }
    /**
     * Executes delete operation.<br />
     * Method deletes all staff-accounts in the database with the id the String is containing and
     * all corresponing groupmemberships.
     */
    public ActionErrors execute(String messagesToDelete) {
        ActionErrors errors = new ActionErrors();

        String sql = "";
        String sqlGM = "";

        if (messagesToDelete.indexOf(",") != -1) {
            sql = "DELETE FROM TStaff WHERE sId IN(" + messagesToDelete + ")";
            sqlGM = "DELETE FROM TGroupMembers WHERE gmStaff IN (" + messagesToDelete + ")";
        } else {
            if (messagesToDelete.indexOf("'") == -1) {
                messagesToDelete = "'" + messagesToDelete + "'";
            }

            sql = "DELETE FROM TStaff WHERE sId=" + messagesToDelete;
            sqlGM = "DELETE FROM TGroupMembers WHERE gmStaff=" + messagesToDelete;
        }

        if (!DataBaseConnection.execute(sql)) {
            errors.add("dberror", new ActionError("error.database.delete"));
        }

        if (!DataBaseConnection.execute(sqlGM)) {
            errors.add("dberror", new ActionError("error.database.delete"));
        }

        return errors;
    }
}
