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

public class AdminDeleter {
    /**
     * Constructor
     */
    public AdminDeleter() {
    }

    /**
     * Executes delete operation.<br />
     * Method deletes all admins-accounts in the database with the id the String[] is containing.
    */
    public ActionErrors execute(String[] adminsToDelete) {
        return execute(StringArrayJoiner.getJoinedString(adminsToDelete, ",", true));
    }

    /**
     * Executes delete operation.<br />
     * Method deletes all admins-accounts in the database with the id the string is containing.
    */
    public ActionErrors execute(String adminsToDelete) {
        ActionErrors errors = new ActionErrors();

        String sql = "";

        if (adminsToDelete.indexOf(",") != -1) {
            sql = "DELETE FROM TAdmin WHERE aId IN(" + adminsToDelete + ")";
        } else {
            if (adminsToDelete.indexOf("'") == -1) {
                adminsToDelete = "'" + adminsToDelete + "'";
            }

            sql = "DELETE FROM TAdmin WHERE aId=" + adminsToDelete;
        }

        if (!DataBaseConnection.execute(sql)) {
            errors.add("dberror", new ActionError("error.database.delete"));
        }

        return errors;
    }
}
