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

public class GroupMembersForm extends org.apache.struts.action.ActionForm {
    private String[] idAdd;
    private String[] idRemove;
    private String group;

    /**
     * Reset-Method for Bean
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        idAdd = null;
        idRemove = null;
        group = null;
    }

    /**
     * Validate-Method for Bean
     * <ul>Validates if:
     * <li>logged in</li>
     * <li>groupname was entered</li>
     * </ul>
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (!LoginChecker.isAdminLoggedIn(request)) {
            errors.add("login", new ActionError("error.login.not"));
        }

        if (group == null) {
            errors.add("group", new ActionError("error.admin.groupmembers.group.missing"));
        }

        return errors;
    }

    /**
     * Sets StaffID-Array to add to Group
     * @param String StaffID-Array to add to Group
     */
    public void setIdAdd(String[] idAdd) {
        this.idAdd = idAdd;
    }

    /**
     * Returns StaffID-Array to add to Group
     * @return StaffID-Array to add to Group
     */
    public String[] getIdAdd() {
        return idAdd;
    }

    /**
     * Sets StaffID-Array to remove from Group
     * @param String StaffID-Array to remove from Group
     */
    public void setIdRemove(String[] idRemove) {
        this.idRemove = idRemove;
    }

    /**
     * Returns StaffID-Array to remove from Group
     * @return StaffID-Array to remove from Group
     */
    public String[] getIdRemove() {
        return idRemove;
    }

    /**
     * Sets Statusname
     * @param String Statusname
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * Returns Group-ID
     * @return Group-ID
     */
    public String getGroup() {
        return group;
    }
}
