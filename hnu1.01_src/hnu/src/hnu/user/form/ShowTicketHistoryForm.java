package hnu.user.form;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;


/*
 * Copyright (C) 2002-2003 Peter Ortner <peter.ortner@fh-joanneum.at>
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
 * @author Peter Ortner
 */

public class ShowTicketHistoryForm extends org.apache.struts.action.ActionForm {
    private String staff;
    private String date;
    private String id;
    private String status;
    private String priority;
    private java.util.Vector priorities;
    private java.util.Vector messages;
    private String subject;
    private String author;
    private String oldPriority;
    private String newMessage;
    private String ok;
    private String pic;

    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (!ok.equals("Save Changes")) {
            if ((newMessage == null) || (newMessage.trim().length() < 3)) {
                errors.add("message",
                    new ActionError("error.user.showTicket.message"));
            }
        }

        return errors;
    }

    /**
     * Sets messages
     * @param Vector messages
     */
    public void setMessages(java.util.Vector messages) {
        this.messages = messages;
    }

    /**
     * Returns messages
     * @return messages
     */
    public java.util.Vector getMessages() {
        return messages;
    }

    /**
     * Sets staff
     * @param String staff
     */
    public void setStaff(String staff) {
        this.staff = staff;
    }

    /**
     * Returns staff
     * @return staff
     */
    public String getStaff() {
        return staff;
    }

    /**
     * Sets date
     * @param String date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Returns date
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets id
     * @param String id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns id
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets status
     * @param String status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns status
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets priority
     * @param String priority
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * Returns priority
     * @return priority
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets priorities
     * @param Vector priorities
     */
    public void setPriorities(java.util.Vector priorities) {
        this.priorities = priorities;
    }

    /**
     * Returns priorities
     * @return priorities
     */
    public java.util.Vector getPriorities() {
        return priorities;
    }

    /**
     * Sets subject
     * @param String subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Returns subject
     * @return subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets author
     * @param String author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Returns author
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets oldPriority
     * @param String oldPriority
     */
    public void setOldPriority(String oldPriority) {
        this.oldPriority = oldPriority;
    }

    /**
     * Returns oldPriority
     * @return oldPriorityd
     */
    public String getOldPriority() {
        return oldPriority;
    }

    /**
     * Sets newMessage
     * @param String newMessage
     */
    public void setNewMessage(String newMessage) {
      for (int i = 0; i < newMessage.length()-1; i++)
	{
	  if (newMessage.charAt(i)=='\n')
	  {
	    newMessage = newMessage.substring(0,i) + "<br />" + newMessage.substring(i+1) ;
	  }
        }
      this.newMessage = newMessage;
    }

    /**
     * Returns newMessage
     * @return newMessage
     */
    public String getNewMessage() {
        return newMessage;
    }

    /**
     * Sets ok
     * @param String ok
     */
    public void setOk(String ok) {
        this.ok = ok;
    }

    /**
     * Returns ok
     * @return ok
     */
    public String getOk() {
        return ok;
    }

    /**
     * Sets pic
     * @param String pic
     */
    public void setPic(String pic) {
        this.pic = pic;
    }

    /**
     * Returns pic
     * @return pic
     */
    public String getPic() {
        return pic;
    }
}
