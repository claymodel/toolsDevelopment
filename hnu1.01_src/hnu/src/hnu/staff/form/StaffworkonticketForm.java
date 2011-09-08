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

public class StaffworkonticketForm extends org.apache.struts.action.ActionForm
{
    /** Staff's Message */
    private String message;

    /** The status of the ticket */
    private String ticketStatus;

    /**
     * Reset all properties
     * @param mapping Struts ActionMapping
     * @param request HTTP-Request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        message = "";
        ticketStatus = "";
    }

    /**
     * Validate the form-data
     * If OK, forwards to the StaffworkonticketAction
     * @param mapping Struts ActionMapping
     * @param request HTTP-Request
     * @return ActionErrors
     */
    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();

        // reply-message
        if((message == null) || (message.equals("")))
        {
            errors.add("message",
                new ActionError("error.staff.workticket.message"));
        }//if

        // ticketstatus
        if((ticketStatus == null) || (ticketStatus.equals("")))
        {
            errors.add("ticketStatus",
                new ActionError("error.staff.workticket.status"));
        }//if
        return errors;
    }

  // message

    /**
     * Get the message
     * @return String
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Set message
     * @param message The staff's message
     */
    public void setMessage(String message)
    {
        for (int i = 0; i < message.length()-1; i++)
        {
	  if (message.charAt(i)=='\n')
	  {
	    message = message.substring(0,i) + "<br />" + message.substring(i+1) ;
	  }
        }

        this.message = message;
    }

  // status

    /**
     * Get the ticket's status
     * @return String
     */
    public String getTicketStatus()
    {
        return ticketStatus;
    }

    /**
     * Set the ticket's status
     * @param ticketStatus ticket's status
     */
    public void setTicketStatus(String ticketStatus)
    {
        this.ticketStatus = ticketStatus;
    }
}