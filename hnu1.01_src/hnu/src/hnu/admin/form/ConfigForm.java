package hnu.admin.form;

import hnu.helper.LoginChecker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

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

public class ConfigForm extends org.apache.struts.action.ActionForm {
    private String css = "";
    private String loginmode = "";
    private String time = "";
    private String title = "";
    private String banner = "";

    /**
     * Constructor for Bean<br />
     * Configuration is loaed from Properties-File ApplicationConfig.properties
     */
    public ConfigForm() {
        load();
    }

    /**
     * Loads configuration from Properties-File ApplicationConfig.properties
     */
    public void load() {
        Properties config = new Properties();

        try {
            File f = new File("./");
            Vector vec = new Vector();

	    Properties prop = System.getProperties();
	    String path = prop.getProperty("catalina.home");
	    String configStr = path + "/webapps/hnu/WEB-INF/ApplicationConfig.properties";

            config.load(new FileInputStream(configStr));
            css = config.getProperty("css");
            loginmode = config.getProperty("forbidUserRegister");
            banner = config.getProperty("banner");
            title = config.getProperty("title");
            time = config.getProperty("time");
        } catch (IOException ex) {
        }
    }

    /**
     * Stores configuration to Properties-File ApplicationConfig.properties
     */
    public boolean store() {
        Properties config = new Properties();

        try {
            config.setProperty("css", css);
            config.setProperty("forbidUserRegister", loginmode);
            config.setProperty("banner", banner);
            config.setProperty("title", title);
            config.setProperty("time", time);

	    Properties sysProp = System.getProperties();
	    String path = sysProp.getProperty("catalina.home");
	    String propPath = path + "/webapps/hnu/WEB-INF/ApplicationConfig.properties";

            config.store(new FileOutputStream(propPath), "");

            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Reset-Method for Bean
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.load();
    }

    /**
     * Validate-Method for Bean
     * <ul>Validates if:
     * <li>logged in</li>
     * <li>Timemode-Parameter was set</li>
     * <li>Loginmode-Parameter was set</li>
     * </ul>
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (!LoginChecker.isAdminLoggedIn(request)) {
            errors.add("login", new ActionError("error.login.not"));
        }

        if (loginmode.equals("")) {
            errors.add("loginmode", new ActionError("error.admin.config.loginmode.missing"));
        }

        if (time.equals("")) {
            errors.add("time", new ActionError("error.admin.config.time.missing"));
        }

        return errors;
    }

    /**
     * Returns Path to CSS
     * @return String Path to CSS
     */
    public String getCss() {
        return css;
    }

    /**
     * Sets Path to CSS
     * @param String Path to CSS
     */
    public void setCss(String css) {
        this.css = css;
    }

    /**
     * Returns Loginmode
     * @return String Loginmode
     */
    public String getLoginmode() {
        return loginmode;
    }

    /**
     * Sets Loginmode
     * @param String Loginmode
     */
    public void setLoginmode(String loginmode) {
        this.loginmode = loginmode;
    }

    /**
     * Returns Numeric Identifier for TimeFormat/DateFormat
     * @return String Numeric Identifier for TimeFormat/DateFormat
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets Numeric Identifier for TimeFormat/DateFormat
     * @param String Numeric Identifier for TimeFormat/DateFormat
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Returns HTML-Code for Browser-Title
     * @return String HTML-Code for Browser-Title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets HTML-Code for Browser-Title
     * @param String HTML-Code for Browser-Title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns HTML-Code for Banner-Section
     * @return String[] HTML-Code for Banner-Section
     */
    public String getBanner() {
        return banner;
    }

    /**
     * Sets HTML-Code for Banner-Section
     * @param String HTML-Code for Banner-Section
     */
    public void setBanner(String banner) {
        this.banner = banner;
    }
}
