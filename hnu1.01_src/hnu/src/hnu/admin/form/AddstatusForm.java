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

public class AddstatusForm extends org.apache.struts.action.ActionForm {
    private String text;

    /**
     * Reset-Method for Bean
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        text = "";
    }

    /**
     * Validate-Method for Bean
     * <ul>Validates if:
     * <li>logged in</li>
     * <li>status already exists</li>
     * </ul>
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (!LoginChecker.isAdminLoggedIn(request)) {
            errors.add("login", new ActionError("error.login.not"));
        }

        if (DataBaseConnection.checkResult("SELECT stId from TStatus WHERE stText='" + text + "'")) {
            errors.add("text", new ActionError("error.admin.status.exist"));
        }

        return errors;
    }

    /**
     * Sets Statusname
     * @param String Statusname
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns Statusname
     * @return Statusname
     */
    public String getText() {
        return text;
    }
}
