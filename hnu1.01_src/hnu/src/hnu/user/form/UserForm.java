package hnu.user.form;

import hnu.helper.DataBaseConnection;
import hnu.helper.PasswordHash;

import java.sql.ResultSet;

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

public class UserForm extends org.apache.struts.action.ActionForm {
  /**
   * Validate-Method for Bean
   * <ul>Validates if:
   * <li>last name was entered</li>
   * <li>first name was entered</li>
   * <li>mail address was entered</li>
   * <li>login name was entered with at least 6 characters and is not used within the whole database</li>
   * <li>the two passwords are similar and at least 5 characters</li>
   * <li>the old password is similar to the one in the database (only for password change)</li>
   * </ul>
   */
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

    ActionErrors errors = new ActionErrors();
    boolean passFill = true;
    boolean oldIsCorrect=true;
    boolean checkPass=true;

    if (name==null || name.equals("")){
      errors.add("name",new ActionError("error.user.name"));
    }
    if (first==null || first.equals("")){
      errors.add("first",new ActionError("error.user.first"));
    }
    if (mail==null || mail.equals("")){
      errors.add("mail",new ActionError("error.user.mail"));
    }
    if (login==null || login.equals("")){
      errors.add("login",new ActionError("error.user.login"));
    }
    if (login.trim().length()<6) {
      errors.add("login",new ActionError("error.user.loginLength"));
    }

    if(ok.equals("Change details")) {
      checkPass=false;
    } else {
      if (pass==null || pass.equals("")){
        passFill=false;
        errors.add("pass",new ActionError("error.user.pass"));
      }
      if (pass2==null || pass2.equals("")){
        passFill=false;
        errors.add("pass2",new ActionError("error.user.pass"));
      }
      if(pass.trim().length()<5) {
        passFill=false;
        errors.add("pass",new ActionError("error.user.passLength"));
      }
    }

    if (ok.equals("Change password")) {
      if (passOld==null || passOld.trim().equals("")) {
        errors.add("passOld",new ActionError("error.user.passOld"));
        passFill=false;
      } else {
        String sql = "SELECT uId FROM TUser WHERE uId=" + id + " AND uPass='" + PasswordHash.getSHAString(passOld) + "'";
	ResultSet res = null;
	DataBaseConnection db = null;
        try {
	  db = new DataBaseConnection();
          res = db.getRSfromStatement(sql);
          if(!res.next()) {
            passOld="";
            oldIsCorrect=false;
            errors.add("passOld",new ActionError("error.user.passOldFalse"));
          }
	  db.closeResultSet(res);
        } catch (Exception ex) {
	  db.closeResultSet(res);
          errors.add("db",new ActionError("error.database.general"));
        }
      }
    }


    if(ok.equals("Change details")) {
      checkPass=false;
    }

    if(passFill && oldIsCorrect && checkPass) {
      if(!pass.equals(pass2)) {
        errors.add("passEqual",new ActionError("error.user.passEqual"));
        pass="";
        pass2="";
      }
    }

    if(!existingUser) {
      String[] table = {"TUser", "TAdmin", "TStaff"};
      String[] prefix = {"u", "a", "s"};

      for(int i=0; i<table.length;i++) {
        String sql = "SELECT "+prefix[i]+"Login FROM "+table[i]+" WHERE "+prefix[i]+"Login='"+login+"';";
        if(DataBaseConnection.checkResult(sql)) {
          errors.add("login", new ActionError("errors.user.register.existingLogin"));
        }
      }//for
    }

    return errors;
  }

  private String id;
  private String name;
  private String first;
  private String mail;
  private String login;
  private String street;
  private String zip;
  private String city;
  private String country;
  private String dob;
  private String telephone;
  private String pass;
  private String active;
  private String pass2;
  private boolean existingUser;
  private String passOld;
  private String ok;
  private String day;
  private String month;
  private String year;
  private java.util.Vector days;
  private java.util.Vector months;
  private java.util.Vector years;

  /**
   * Returns id
   * @return id
   */
  public String getId() {
    return id;
  }

  /**
   * Sets id
   * @param String id
   */
  public void setId(String id) {
    this.id = id.trim();
  }

  /**
   * Returns name
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets name
   * @param String name
   */
  public void setName(String name) {
    this.name = name.trim();
  }

  /**
   * Returns first
   * @return first
   */
  public String getFirst() {
    return first;
  }

  /**
   * Sets first
   * @param String first
   */
  public void setFirst(String first) {
    this.first = first.trim();
  }

  /**
   * Returns mail
   * @return mail
   */
  public String getMail() {
    return mail;
  }

  /**
   * Sets mail
   * @param String mail
   */
  public void setMail(String mail) {
    this.mail = mail.trim();
  }

  /**
   * Returns login
   * @return login
   */
  public String getLogin() {
    return login;
  }

  /**
   * Sets login
   * @param String login
   */
  public void setLogin(String login) {
    this.login = login.trim();
  }

  /**
   * Returns street
   * @return street
   */
  public String getStreet() {
    return street;
  }

  /**
   * Sets street
   * @param String street
   */
  public void setStreet(String street) {
    if(street!=null) {
      this.street = street.trim();
    }
  }

  /**
   * Returns zip
   * @return zip
   */
  public String getZip() {
    return zip;
  }

  /**
   * Sets zip
   * @param String zip
   */
  public void setZip(String zip) {
    if(zip!=null) {
      this.zip = zip.trim();
    }
  }

  /**
   * Returns city
   * @return city
   */
  public String getCity() {
    return city;
  }

  /**
   * Sets city
   * @param String city
   */
  public void setCity(String city) {
    this.city = city.trim();
  }

  /**
   * Returns country
   * @return country
   */
  public String getCountry() {
    return country;
  }

  /**
   * Sets country
   * @param String country
   */
  public void setCountry(String country) {
    if(country!=null) {
      this.country = country.trim();
    }
  }

  /**
   * Returns dob
   * @return dob
   */
  public String getDob() {
    return dob;
  }

  /**
   * Sets dob
   * @param String dob
   */
  public void setDob(String dob) {
    this.dob = dob.trim();
  }

  /**
   * Returns telephone
   * @return telephone
   */
  public String getTelephone() {
    return telephone;
  }

  /**
   * Sets telephone
   * @param String telephone
   */
  public void setTelephone(String telephone) {
    if(telephone!=null) {
      this.telephone = telephone.trim();
    }
  }

  /**
   * Returns pass
   * @return pass
   */
  public String getPass() {
    return pass;
  }

  /**
   * Sets pass
   * @param String pass
   */
  public void setPass(String pass) {
    this.pass = pass.trim();
  }

  /**
   * Returns active
   * @return active
   */
  public String getActive() {
    return active;
  }

  /**
   * Sets active
   * @param String active
   */
  public void setActive(String active) {
    this.active = active.trim();
  }

  /**
   * Sets pass2
   * @param String pass2
   */
  public void setPass2(String pass2)
  {
    this.pass2 = pass2.trim();
  }

  /**
   * Returns pass2
   * @return pass2
   */
  public String getPass2()
  {
    return pass2;
  }

  /**
   * Sets existingUser
   * @param boolean existingUser
   */
  public void setExistingUser(boolean existingUser) {
    this.existingUser = existingUser;
  }

  /**
   * Returns existingUser
   * @return existingUser
   */
  public boolean isExistingUser() {
    return existingUser;
  }

  /**
   * Sets passOld
   * @param String passOld
   */
  public void setPassOld(String passOld) {
    this.passOld = passOld.trim();
  }

  /**
   * Returns passOld
   * @return passOld
   */
  public String getPassOld() {
    return passOld;
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
   * Sets day
   * @param String day
   */
  public void setDay(String day) {
    this.day = day;
  }

  /**
   * Returns day
   * @return day
   */
  public String getDay() {
    return day;
  }

  /**
   * Sets month
   * @param String month
   */
  public void setMonth(String month) {
    this.month = month;
  }

  /**
   * Returns month
   * @return month
   */
  public String getMonth() {
    return month;
  }

  /**
   * Sets year
   * @param String year
   */
  public void setYear(String year) {
    this.year = year;
  }

  /**
   * Returns year
   * @return year
   */
  public String getYear() {
    return year;
  }

  /**
   * Sets days
   * @param Vector days
   */
  public void setDays(java.util.Vector days) {
    this.days = days;
  }

  /**
   * Returns days
   * @return days
   */
  public java.util.Vector getDays() {
    return days;
  }

  /**
   * Sets months
   * @param Vector months
   */
  public void setMonths(java.util.Vector months) {
    this.months = months;
  }

  /**
   * Returns months
   * @return months
   */
  public java.util.Vector getMonths() {
    return months;
  }

  /**
   * Sets years
   * @param Vector years
   */
  public void setYears(java.util.Vector years) {
    this.years = years;
  }

  /**
   * Returns years
   * @return years
   */
  public java.util.Vector getYears() {
    return years;
  }

}