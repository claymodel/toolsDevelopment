package hnu.staff.form;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;


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

public class StaffsearchticketForm extends org.apache.struts.action.ActionForm
{
    /** The string which should be searched for */
    private String searchString;

    /**
     * Reset the form's properties
     * @param mapping Struts ActionMapping
     * @param request HTTP Request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        searchString = "";
    }

    /**
     * Validate the input
     * @param mapping Struts ActionMapping
     * @param request HTTP Request
     * @return ActionErrors Errors which occured
     */
    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request)
    {
      ActionErrors errors = new ActionErrors();

      // validate
      if((searchString == null) || (searchString.equals("")))
      {
        errors.add("searchString", new ActionError("error.staff.search.empty"));
      }//if

      return errors;
    }

    /**
     * Get searchString
     * @return String the search-string
     */
    public String getSearchString()
    {
        return searchString;
    }

    /**
     * Set the searchString
     * @param searchString the search-string
     */
    public void setSearchString(String searchString)
    {
        this.searchString = searchString;
    }
}