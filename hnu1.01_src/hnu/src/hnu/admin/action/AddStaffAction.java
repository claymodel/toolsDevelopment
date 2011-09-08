package hnu.admin.action;

import hnu.admin.form.AddStaffForm;
import hnu.helper.DataBaseConnection;
import hnu.helper.PasswordHash;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Properties;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.DiskFile;

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

public class AddStaffAction extends org.apache.struts.action.Action {
    /**
     * Executes Struts-Action:
     * Adds a new staff-account to the database.
     * @param org.apache.struts.action.Actionmapping Struts-Actionmapping
     * @param org.apache.struts.action.ActionForm Struts-ActionForm
     * @param javax.servlet.http.HttpServletRequest HttpServletRequest
     * @param javax.servlet.http.HttpServletResponse HttpServletResponse
     * @return
     *   org.apache.struts.action.ActionForward Struts-ActionForward
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        AddStaffForm addStaffForm = (AddStaffForm) form;
        ActionErrors errors = new ActionErrors();

        String sql = "INSERT INTO TStaff(sName,sFirst,sLogin,sPass) VALUES ("
            + "'" + addStaffForm.getName() + "'," + "'"
            + addStaffForm.getFirst() + "'," + "'" + addStaffForm.getLogin()
            + "'," + "'" + PasswordHash.getSHAString(addStaffForm.getPass1())
            + "')";

        if (!DataBaseConnection.execute(sql)) {
            errors.add("dberror", new ActionError("error.database.insert"));
        }

        sql = "SELECT sId FROM TStaff WHERE sLogin='" + addStaffForm.getLogin()
            + "'";

        DataBaseConnection db = new DataBaseConnection();
        ResultSet rs = db.getRSfromStatement(sql);

        if (rs == null) {
            errors.add("dberror", new ActionError("error.database.select"));
        }

        FileInputStream in = null;
        BufferedOutputStream out = null;

        try {
            if ((rs != null) && rs.next() && errors.empty()
                    && (addStaffForm.getPic() != null)) {
                DiskFile diskFile = addStaffForm.getPic();

                if (diskFile.getFileSize() > 0) {
                    in = (FileInputStream) diskFile.getInputStream();

                    String filename = rs.getString("sId");
                    int ending = diskFile.getFileName().lastIndexOf(".");

                    filename += diskFile.getFileName().substring(ending);

		    Properties prop = System.getProperties();
		    String path = prop.getProperty("catalina.home");
		    String picStr = path + "/webapps/hnu/pics/";

                    File file = new File(picStr + filename);

                    out = new BufferedOutputStream(new FileOutputStream(file));

                    byte[] bytearray = new byte[1024];
                    int count = 0;

                    while ((count = in.read(bytearray)) != -1) {
                        out.write(bytearray, 0, count);
                    }

                    out.close();
                    in.close();

                    sql = "UPDATE TStaff SET sPic='" + filename
                        + "' WHERE sId='" + rs.getString("sId") + "'";

                    if (!DataBaseConnection.execute(sql)) {
                        errors.add("pic",
                            new ActionError("error.database.update"));
                    }
                }
            }
        } catch (Exception ex) {
            errors.add("pic", new ActionError("error.admin.staff.picture"));
            ex.printStackTrace();
        }

        db.closeResultSet(rs);

        if (!errors.empty()) {
            this.saveErrors(request, errors);

            return mapping.findForward("failure");
        } else {
            addStaffForm.reset(mapping, request);

            return mapping.findForward("success");
        }
    }
}
