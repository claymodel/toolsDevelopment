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

public class StatusForm extends org.apache.struts.action.ActionForm {
    private String[] id;
    private String move;
    private String status;

    /**
     * Reset-Method for Bean
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
      id = null;
      move = "";
      status = "";
    }
    /**
     * Validate-Method for Bean
     * <ul>Validates if:
     * <li>logged in</li>
     * <li>at least one status was selected</li>
     * <li>Update-To-Status is selected to delete, if move is activated</li>
     * </ul>
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (!LoginChecker.isAdminLoggedIn(request)) {
            errors.add("login", new ActionError("error.login.not"));
        }

        if ((id == null) || (id.length == 0)) {
            errors.add("deletestatus", new ActionError("error.admin.status.delete.missing"));
        }

        if (move.equals("on")) {
            boolean conflict = false;

            for (int i = 0; i < id.length; i++) {
                if (id[i].equals(status)) {
                    conflict = true;

                    break;
                }
            }

            if (conflict) {
                errors.add("deletemoveconflict", new ActionError("error.admin.status.conflict"));
            }
        }

        return errors;
    }
    /**
     * Sets Array of Status-IDs
     * @param String[] Array of Status-IDs
     */
    public void setId(String[] id) {
        this.id = id;
    }
    /**
     * Returns Array of Status-IDs
     * @return String[] Array of Status-IDs
     */
    public String[] getId() {
        return id;
    }
    /**
     * Sets Move-Flag
     * @param String Move-Flag
     */
    public void setMove(String move) {
        this.move = move;
    }
    /**
     * Returns Move-Flag
     * @return String Move-Flag
     */
    public String getMove() {
        return move;
    }
    /**
     * Sets Status to move to
     * @param String Status to move to
     */
    public void setStatus(String status) {
        this.status = status;
    }
    /**
     * Returns Status to move to
     * @return String Status to move to
     */
    public String getStatus() {
        return status;
    }
}
