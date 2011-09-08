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

public class GroupsForm extends org.apache.struts.action.ActionForm {
    private String[] id;
    private String move;
    private String groupId;

    /**
     * Reset-Method for Bean
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        id = null;
        groupId = "";
        move = "";
    }

    /**
     * Validate-Method for Bean
     * <ul>Validates if:
     * <li>logged in</li>
     * <li>at least one group-property was selected</li>
     * <li>Move-To-Group is selected to delete, if move is activated</li>
     * </ul>
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (!LoginChecker.isAdminLoggedIn(request)) {
            errors.add("login", new ActionError("error.login.not"));
        }

        if ((id == null) || (id.length == 0)) {
            errors.add("deleteadmins", new ActionError("error.admin.group.delete.missing"));
        }

        if ((move != null) && move.equals("on")) {
            boolean conflict = false;

            for (int i = 0; i < id.length; i++) {
                if (id[i].equals(groupId)) {
                    conflict = true;

                    break;
                }
            }

            if (conflict) {
                errors.add("deletemoveconflict", new ActionError("error.admin.groups.conflict"));
            }
        }

        return errors;
    }

    /**
     * Returns GroupID-Array
     * @return GroupID-Array
     */
    public String[] getId() {
        return id;
    }

    /**
     * Sets GroupID-Array
     * @param String GroupID-Array
     */
    public void setId(String[] id) {
        this.id = id;
    }

    /**
     * Sets move-Attribute
     * @param String move-Attribute
     */
    public void setMove(String move) {
        this.move = move;
    }

    /**
     * Returns move-Attribute
     * @return move-Attribute
     */
    public String getMove() {
        return move;
    }

    /**
     * Sets GroupID to move to
     * @param String GroupID to move to
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * Returns GroupID to move to
     * @return GroupID to move to
     */
    public String getGroupId() {
        return groupId;
    }
}
