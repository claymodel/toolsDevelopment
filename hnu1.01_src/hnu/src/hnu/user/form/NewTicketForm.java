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

public class NewTicketForm extends org.apache.struts.action.ActionForm {
  /**
   * Validate-Method for Bean
   * <ul>Validates if:
   * <li>subject was entered</li>
   * <li>messagetext was entered</li>
   * </ul>
   */
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
  {
    ActionErrors errors = new ActionErrors();

    if(subject==null || subject.trim().equals("")) {
      errors.add("subject",new ActionError("errors.user.ticket.subject"));
    }

    if(text==null || text.trim().equals("")) {
      errors.add("text",new ActionError("errors.user.ticket.text"));
    }

    return errors;
  }
  private String user;
  private String group;
  private String priority;
  private String subject;
  private String status;
  private String text;
  private java.util.Vector groups;
  private java.util.Vector priorities;
  private String tId;

  /**
   * Returns user
   * @return user
   */
  public String getUser()
  {
    return user;
  }

  /**
 * Sets user
 * @parm String user
   */
  public void setUser(String user)
  {
    this.user = user;
  }

  /**
 * Sets user
 * @parm int user
   */
  public void setUser(int user)
  {
    this.user = user+"";
  }

  /**
 * Returns group
 * @return group
   */
  public String getGroup()
  {
    return group;
  }

  /**
 * Sets group
 * @parm String group
   */
  public void setGroup(String group)
  {
    this.group = group;
  }

  /**
 * Returns priority
 * @return priority
   */
  public String getPriority()
  {
    return priority;
  }

  /**
 * Sets priority
 * @parm String priority
   */
  public void setPriority(String priority)
  {
    this.priority = priority;
  }

  /**
 * Returns subject
 * @return subject
   */
  public String getSubject()
  {
    return subject;
  }

  /**
 * Sets subject
 * @parm String subject
   */
  public void setSubject(String subject)
  {
    this.subject = subject;
  }

  /**
 * Returns status
 * @return status
   */
  public String getStatus()
  {
    return status;
  }

  /**
 * Sets status
 * @parm String status
   */
  public void setStatus(String status)
  {
    this.status = status;
  }

  /**
 * Returns text
 * @return text
   */
  public String getText()
  {
    return text;
  }

  /**
 * Sets text
 * @parm String text
   */
  public void setText(String text)
  {
    for (int i = 0; i < text.length()-1; i++)
	{
	  if (text.charAt(i)=='\n')
	  {
	    text = text.substring(0,i) + "<br />" + text.substring(i+1) ;
	  }
        }
    this.text = text;
  }

  /**
 * Sets groups
 * @parm Vector groups
   */
  public void setGroups(java.util.Vector groups)
  {
    this.groups = groups;
  }

  /**
 * Returns groups
 * @return groups
   */
  public java.util.Vector getGroups()
  {
    return groups;
  }

  /**
 * Sets priorities
 * @parm Vector priorities
   */
  public void setPriorities(java.util.Vector priorities)
  {
    this.priorities = priorities;
  }

  /**
 * Returns priorities
 * @return priorities
   */
  public java.util.Vector getPriorities()
  {
    return priorities;
  }

  /**
 * Sets tId
 * @parm String tId
   */
  public void setTId(String tId)
  {
    this.tId = tId;
  }

  /**
 * Returns tId
 * @return tId
   */
  public String getTId()
  {
    return tId;
  }
}