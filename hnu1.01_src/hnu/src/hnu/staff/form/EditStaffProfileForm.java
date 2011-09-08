package hnu.staff.form;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
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

public class EditStaffProfileForm extends org.apache.struts.action.ActionForm
{
    /** Staff family name*/
    private String name;

    /** Staff firstname*/
    private String firstname;

    /** Password */
    private String pass;

    /** Password for checking*/
    private String pass2;

    /** picturename */
    private DiskFile pic;

    /** URL to the picture */
    private String picName;

    /**
     * Reset the attributes to their defaults
     * @param mapping Struts Actionmapping
     * @param request The HttpServletRequest
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        name = "";
        firstname = "";
        pass = "";
        pass2 = "";
        pic = null;
    }

    /**
     * Validate the input
     * @returns ActionErrors Errors that occured during validation
     * @param mapping Struts Actionmapping
     * @param request The HttpServletRequest
     */
    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();

        // validate
        // name missing
        if((name == null) || (name.equals("")))
        {
            errors.add("name",
                new ActionError("error.staff.editprofile.name.missing"));
        }//if

        // firstname
        if((firstname == null) || (firstname.equals("")))
        {
            errors.add("firstname",
                new ActionError("error.staff.editprofile.firstname.missing"));
        }//if

        // any pass-field set at all, then check
        if(((pass != null) && (!pass.equals(""))) ||
                ((pass2 != null) && (!pass2.equals(""))))
        {
            // either empty?
            if((pass == null) || (pass.equals("")))
            {
                errors.add("pass",
                    new ActionError("error.staff.editprofile.pass.missing"));
            }//if

            if((pass2 == null) || (pass2.equals("")))
            {
                errors.add("pass",
                    new ActionError("error.staff.editprofile.pass.missing"));
            }//if

            // pass must be equal to pass2
            if(!pass.equals(pass2))
            {
                errors.add("pass2",
                    new ActionError("error.staff.editprofile.pass2.mismatch"));
            }//if
        }//if

        return errors;
    }

  // name

    /**
     * Get the set family name
     * @return String Staff familyname
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set familyname
     * @param name Staff's family name
     */
    public void setName(String name)
    {
        this.name = name;
    }

  // firstname

    /**
     * Get the staff's firstname
     * @return String Staff's firstname
     */
    public String getFirstname()
    {
        return firstname;
    }

    /**
     * Set Staff's firstname
     * @param firstname Staff firstname
     */
    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

  // pass

    /**
     * Get the password
     * @return String Password
     */
    public String getPass()
    {
        return pass;
    }

    /**
     * Set the password
     * @param pass Staff password
     */
    public void setPass(String pass)
    {
        this.pass = pass;
    }

  // pass2

    /**
     * Get the password #2, used for checking against typos
     * @return String Password #2
     */
    public String getPass2()
    {
        return pass2;
    }

    /**
     * Set the password #2, used for checking against typos
     * @param pass2 Password #2
     */
    public void setPass2(String pass2)
    {
        this.pass2 = pass2;
    }

  // pic

    /**
     * Set the picture
     * @param pic picturename
     */
    public void setPic(DiskFile pic)
    {
        this.pic = pic;
    }

    /**
     * Get the picture
     * @return String picturename
     */
    public DiskFile getPic()
    {
        return pic;
    }

    /**
     * Set the pictureURL
     * @param picName URL of the picture
     */
    public void setPicName(String picName)
    {
      this.picName = picName;
    }

    /**
     * Get the pictureURL
     * @return String picturename
     */
    public String getPicName()
    {
      return "../pics/"+picName;
    }
}