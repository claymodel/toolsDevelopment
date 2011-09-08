package hnu.admin.action;

import hnu.admin.form.GroupMembersForm;
import hnu.helper.DataBaseConnection;
import hnu.helper.StringArrayJoiner;

import java.io.IOException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
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

public class GroupmembersAction extends org.apache.struts.action.Action {
  /**
   * Executes Struts-Action:
   * Changes groupmemberships for staff-accounts.
   * @param org.apache.struts.action.Actionmapping Struts-Actionmapping
   * @param org.apache.struts.action.ActionForm Struts-ActionForm
   * @param javax.servlet.http.HttpServletRequest HttpServletRequest
   * @param javax.servlet.http.HttpServletResponse HttpServletResponse
   * @return
   *   org.apache.struts.action.ActionForward Struts-ActionForward
   */
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        GroupMembersForm groupMembersForm = (GroupMembersForm) form;
        ActionErrors errors = new ActionErrors();

        String group = groupMembersForm.getGroup();
        String[] idAdd = groupMembersForm.getIdAdd();
        String[] idRemove = groupMembersForm.getIdRemove();

        if (group == null) {
            errors.add("group", new ActionError("error.admin.groupmembers.group.missing"));
            this.saveErrors(request, errors);
            groupMembersForm.reset(mapping, request);

            return new ActionForward(mapping.findForward("failure").getPath().concat("?gId=" + group));
        }

        String staffNotToRemove = StringArrayJoiner.getJoinedString(idRemove, ",", true);
        String sql = "DELETE FROM TGroupMembers WHERE gmGroup=" + group + " AND gmStaff NOT IN (" + staffNotToRemove + ")";

        if (!DataBaseConnection.execute(sql)) {
            errors.add("group", new ActionError("error.database.delete"));
            this.saveErrors(request, errors);
            groupMembersForm.reset(mapping, request);

            return mapping.findForward("failure");
        }

        if (idAdd != null) {
            for (int i = 0; i < idAdd.length; i++) {
                sql = "INSERT INTO TGroupMembers(gmGroup,gmStaff) VALUES('" + group + "','" + idAdd[i] + "')";

                if (!DataBaseConnection.execute(sql)) {
                    errors.add("dberror", new ActionError("error.database.insert"));
                }
            }
        }

        groupMembersForm.reset(mapping, request);

        if (errors.empty()) {
            return new ActionForward(mapping.findForward("success").getPath().concat("?gId=" + group));
        } else {
            this.saveErrors(request, errors);

            return new ActionForward(mapping.findForward("failure").getPath().concat("?gId=" + group));
        }
    }
}
