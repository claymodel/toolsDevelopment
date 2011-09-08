package hnu.helper.admin;

import hnu.helper.DateFormatter;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

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

public class ConfigBean implements Serializable {
    private String dateExample;
    private String[] css;

    /**
     * Bean constructor
     * Reads a list of all available stylesheet from disk.
     */
    public ConfigBean() {
        DateFormatter df = new DateFormatter();

        dateExample = df.getDate(new Date(System.currentTimeMillis()));

	Properties prop = System.getProperties();
	String path = prop.getProperty("catalina.home");
	String stylesStr = path + "/webapps/hnu/styles";

        File f = new File(stylesStr);
        Vector vec = new Vector();

        if (f.isDirectory()) {
            String[] children = f.list();

            for (int i = 0; i < children.length; i++) {
                if (children[i].endsWith(".css")) {
                    vec.add(children[i]);
                }
            }

            if (vec.size() > 0) {
                css = new String[vec.size()];
            }

            Iterator it = vec.iterator();
            int counter = 0;

            while (it.hasNext()) {
                css[counter] = (String) it.next();
                counter++;
            }
        }
    }

    /**
     * Returns an example Date with the configured DateFormat
     * @return an example Date with the configured DateFormat
     */
    public String getDateExample() {
        return dateExample;
    }

    /**
     * Sets an example Date with the configured DateFormat
     * @param String an example Date with the configured DateFormat
     */
    public void setDateExample(String dateExample) {
        this.dateExample = dateExample;
    }

    /**
     * Sets String[] of names of all avaiable stylesheets for the system
     * @param String[] of names of all avaiable stylesheets for the system
     */
    public void setCss(String[] css) {
        this.css = css;
    }

    /**
     * Returns String[] of names of all avaiable stylesheets for the system
     * @return String[] of names of all avaiable stylesheets for the system
     */
    public String[] getCss() {
        return css;
    }
}
