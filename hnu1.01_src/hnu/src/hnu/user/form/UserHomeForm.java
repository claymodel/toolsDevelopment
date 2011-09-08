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

public class UserHomeForm extends org.apache.struts.action.ActionForm {
  /**
   * Validate-Method for Bean
   * <ul>Validates if:
   * <li>only one checkbox was selected (Ticket Details)</li>
   * <li>at least one checkbox was selected (Set as solved)</li>
   * </ul>
   */
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    ActionErrors errors = new ActionErrors();

    if(ticketId==null) {
      errors.add("button",new ActionError("errors.user.home.noTicket"));
    } else {
      if(submit.equals("Ticket Details") && ticketId.length>1) {
        errors.add("button", new ActionError("errors.user.history.ids"));
        ticketId=null;
      }
    }
    return errors;
  }

  private String id;
  private String login;
  private String submit;
  private String[] ticketId;

  /**
   * Returns id
   * @return id
   */
  public String getId() {
    return id;
  }

  /**
   * Sets id
   * @param String idears
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Sets login
   * @param String login
   */
  public void setLogin(String login) {
    this.login = login;
  }

  /**
   * Returns login
   * @return login
   */
  public String getLogin() {
    return login;
  }

  /**
   * Sets submit
   * @param String submit
   */
  public void setSubmit(String submit)
  {
    this.submit = submit;
  }

  /**
   * Returns submit
   * @return submit
   */
  public String getSubmit()
  {
    return submit;
  }

  /**
   * Sets ticketId
   * @param String-Array ticketId
   */
  public void setTicketId(String[] ticketId)
  {
    this.ticketId = ticketId;
  }

  /**
   * Returns ticketId
   * @return ticketId
   */
  public String[] getTicketId()
  {
    return ticketId;
  }

}