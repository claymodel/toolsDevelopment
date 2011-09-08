package hnu.admin.form;

import hnu.helper.DataBaseConnection;
import hnu.helper.LoginChecker;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

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

public class AddgroupForm extends org.apache.struts.action.ActionForm {
    private String name;

    /**
     * Reset-Method for Bean
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        name = "";
    }

    /**
     * Validate-Method for Bean
     * <ul>Validates if:
     * <li>logged in</li>
     * <li>length of group is okay</li>
     * <li>group already exists</li>
     * </ul>
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (!LoginChecker.isAdminLoggedIn(request)) {
            errors.add("login", new ActionError("error.login.not"));
        }

        if (name.length() < 3) {
            errors.add("name", new ActionError("error.admin.group.length"));
        }

        if (DataBaseConnection.checkResult("SELECT gId FROM TGroup WHERE gText='" + name + "'")) {
            errors.add("name", new ActionError("error.admin.group.exists"));
        }

        return errors;
    }

    /**
     * Returns Groupname
     * @return Groupname
     */
    public String getName() {
        return name;
    }
    /**
     * Sets Groupname
     * @param String Groupname
     */
    public void setName(String name) {
        this.name = name;
    }
}
