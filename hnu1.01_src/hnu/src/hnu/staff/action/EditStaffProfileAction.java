package hnu.staff.action;

import hnu.LoginBean;
import hnu.helper.DataBaseConnection;
import hnu.helper.PasswordHash;
import hnu.helper.staff.StaffBean;
import hnu.staff.form.EditStaffProfileForm;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.DiskFile;


/*
 * Copyright (C) 2002-2003 Thomas Maschutznig <thomas.maschutznig@fh-joanneum.at>
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
 * @author Thomas Maschutznig
 */

public class EditStaffProfileAction extends org.apache.struts.action.Action
{
    /**
     * Take new values for staff's profile and write to DB
     * @return ActionForward Struts ActionForward
     * @param mapping Struts ActionMapping
     * @param form Struts ActionForm bean
     * @param request The HttpServletRequest
     * @param response The HttpServletResponse
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward perform(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        ActionErrors errors = new ActionErrors();
        EditStaffProfileForm editStaffProfileForm = (EditStaffProfileForm) form;

        HttpSession sess = request.getSession(false);

        if(sess == null)
        {
            errors.add("general", new ActionError("error.login.not"));
            saveErrors(request, errors);

            return mapping.findForward("failure");
        }

        LoginBean loginBean = (LoginBean) sess.getAttribute("loginBean");

        // bring staffBean up to date
        StaffBean staff = (StaffBean) sess.getAttribute("staffBean");
        staff.setFirstname(editStaffProfileForm.getFirstname());
        staff.setName(editStaffProfileForm.getName());

        String sql = "";

        if(((!editStaffProfileForm.getPass().equals("")) &&
                (!editStaffProfileForm.getPass2().equals(""))) &&
                (editStaffProfileForm.getPass().equals(editStaffProfileForm.getPass2())))
        {
            String md5Pass = PasswordHash.getSHAString(editStaffProfileForm.getPass());
            sql = "UPDATE TStaff SET sName='" + editStaffProfileForm.getName() +
                "', sFirst='" + editStaffProfileForm.getFirstname() +
                "', sPass='" + md5Pass + "'";
        }
         //if
        else
        {
            sql = "UPDATE TStaff SET sName='" + editStaffProfileForm.getName() +
                "', sFirst='" + editStaffProfileForm.getFirstname() + "'";
        }

        // update pic?
        String picFileName = loginBean.getId()+"";
	if((editStaffProfileForm.getPic() != null) && (editStaffProfileForm.getPic().getFileSize()>0))
	{
	  FileInputStream in = null;
	  BufferedOutputStream out = null;

	  try
	  {
	    DiskFile diskFile = editStaffProfileForm.getPic();

	    in = (FileInputStream) diskFile.getInputStream();

	    int ending = diskFile.getFileName().lastIndexOf(".");
	    picFileName += diskFile.getFileName().substring(ending);

	    Properties prop = System.getProperties();
	    String path = prop.getProperty("catalina.home");

	    File file = new File(path+"/webapps/hnu/pics/" + picFileName);

	    out = new BufferedOutputStream(new FileOutputStream(file));

	    byte[] bytearray = new byte[1024];
	    int count = 0;

	    while ((count = in.read(bytearray)) != -1)
	    {
	      out.write(bytearray, 0, count);
	    }

	    out.close();
	    in.close();
	  }
	  catch (Exception ex)
	  {
	    errors.add("pic", new ActionError("error.admin.staff.picture"));
	    return mapping.findForward("failure");
	  }
	  sql += (", sPic='" + picFileName + "' ");
	}//if

        sql += (" WHERE sLogin='" + loginBean.getLogin() + "'");

        if(DataBaseConnection.execute(sql))
        {
            saveErrors(request, errors);

            return mapping.findForward("success");
        }
         //if

        errors.add("general", new ActionError("error.database.update"));
        saveErrors(request, errors);

        return mapping.findForward("failure");
    }
}