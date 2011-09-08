package hnu.admin.form;

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

public class AdminsForm extends org.apache.struts.action.ActionForm {
    private String[] id;

    /**
     * Reset-Method for Bean
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
      id = null;
    }

    /**
     * Validate-Method for Bean
     * <ul>Validates if:
     * <li>logged in</li>
     * <li>at least one admin account was checked</li>
     * </ul>
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (!LoginChecker.isAdminLoggedIn(request)) {
            errors.add("login", new ActionError("error.login.not"));
        }

        if ((id == null) || (id.length == 0)) {
            errors.add("deleteadmins", new ActionError("error.admin.admin.delete.missing"));
        }

        return errors;
    }

    /**
     * Sets Array of Admin-IDs
     * @param String[] Array of Admin-IDs
     */
    public void setId(String[] id) {
        this.id = id;
    }

    /**
     * Returns Array of Admin-IDs
     * @return String[] Array of Admin-IDs
     */
    public String[] getId() {
        return id;
    }
}
