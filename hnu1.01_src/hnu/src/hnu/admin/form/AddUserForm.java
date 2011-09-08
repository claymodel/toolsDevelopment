package hnu.admin.form;

import hnu.helper.DataBaseConnection;
import hnu.helper.LoginChecker;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
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

public class AddUserForm extends org.apache.struts.action.ActionForm {
    private String name;
    private String first;
    private String mail;
    private String login;
    private String street;
    private String zip;
    private String city;
    private String country;
    private String telephone;
    private String pass;
    private String pass2;
    private String day;
    private String month;
    private String year;

    /**
     * Reset-Method for Bean
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        name = "";
        first = "";
        mail = "";
        login = "";
        street = "";
        zip = "";
        city = "";
        country = "";
        telephone = "";
        pass = "";
        pass2 = "";
        day = "";
        month = "";
        year = "";
    }

    /**
     * Validate-Method for Bean
     * <ul>Validates if:
     * <li>logged in</li>
     * <li>name was entered</li>
     * <li>first name was entered</li>
     * <li>loginname was entered</li>
     * <li>mail was entered</li>
     * <li>loginname already exists</li>
     * <li>size of loginname is aceptable</li>
     * <li>passwords match and are acceptable</li>
     * </ul>
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (!LoginChecker.isAdminLoggedIn(request)) {
            errors.add("notloggedin", new ActionError("error.login.not"));
        }

        if ((name == null) || name.equals("")) {
            errors.add("name", new ActionError("error.user.name"));
        }

        if ((first == null) || first.equals("")) {
            errors.add("first", new ActionError("error.user.first"));
        }

        if ((mail == null) || mail.equals("")) {
            errors.add("mail", new ActionError("error.user.mail"));
        }

        if (login != null) {
            if (login.equals("")) {
                errors.add("login", new ActionError("error.user.login"));
            } else {
                if (login.length() < 5) {
                    errors.add("login", new ActionError("error.admin.staff.login.length"));
                } else {
                    if (DataBaseConnection.checkResult("SELECT sId FROM TStaff WHERE sLogin='" + login + "'") || DataBaseConnection.checkResult("SELECT uId FROM TUser WHERE uLogin='" + login + "'") || DataBaseConnection.checkResult("SELECT aId FROM TAdmin WHERE aLogin='" + login + "'")) {
                        errors.add("login", new ActionError("errors.user.register.existingLogin"));
                    }
                }
            }
        }

        if (pass != null) {
            if (pass.equals("")) {
                errors.add("pass", new ActionError("error.user.pass"));
            } else {
                if (pass.length() < 6) {
                    errors.add("pass", new ActionError("error.admin.staff.password.length"));
                } else {
                    if (!pass.equals(pass2)) {
                        errors.add("passEqual", new ActionError("error.user.passEqual"));
                    }
                }
            }
        }

        return errors;
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
     * Returns firstname
     * @return firstname
     */
    public String getFirst() {
        return first;
    }

    /**
     * Sets firstname
     * @param String firstname
     */
    public void setFirst(String first) {
        this.first = first.trim();
    }

    /**
     * Returns email-adress
     * @return email-adress
     */
    public String getMail() {
        return mail;
    }

    /**
     * Sets email-adress
     * @param String email-adress
     */
    public void setMail(String mail) {
        this.mail = mail.trim();
    }

    /**
     * Returns loginname
     * @return loginname
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets loginname
     * @param String loginname
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
     * @param street
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Returns ZIP
     * @return ZIP
     */
    public String getZip() {
        return zip;
    }

    /**
     * Sets ZIP
     * @param String ZIP
     */
    public void setZip(String zip) {
        this.zip = zip;
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
        this.city = city;
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
        this.country = country;
    }

    /**
     * Returns telephonenumber
     * @return telephonenumber
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Sets telephonenumber
     * @param String telephonenumber
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * Returns password
     * @return password
     */
    public String getPass() {
        return pass;
    }

    /**
     * Sets password
     * @param String password
     */
    public void setPass(String pass) {
        this.pass = pass.trim();
    }

    /**
     * Sets password-repetition
     * @param String password-repetition
     */
    public void setPass2(String pass2) {
        this.pass2 = pass2.trim();
    }

    /**
     * Returns password-repetition
     * @return password-repetition
     */
    public String getPass2() {
        return pass2;
    }

    /**
     * Sets day of birthday
     * @param String day of birthday
     */
    public void setDay(String day) {
        this.day = day;
    }

    /**
     * Returns day of birthday
     * @return day of birthday
     */
    public String getDay() {
        return day;
    }

    /**
     * Sets month of birthday
     * @param String month of birthday
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * Returns month of birthday
     * @return month of birthday
     */
    public String getMonth() {
        return month;
    }

    /**
     * Sets year of birthday
     * @param String year of birthday
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * Returns year of birthday
     * @return year of birthday
     */
    public String getYear() {
        return year;
    }
}
