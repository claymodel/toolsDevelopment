package hnu.helper;

import java.util.Iterator;
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

public class StringArrayJoiner {
    /**
     * Creates a String from a Stringarray.
     * @param  str  Stringarray
     * @param  seperator  String that is inserted between two elements (i.e. komma)
     * @param  quoted  if the elements should be quoted with a "'".
     * @return      String with all Elements of the array.
      */
    public static String getJoinedString(String[] str, String seperator, boolean quoted) {
        if (str == null) {
            if (quoted) {
                return "''";
            } else {
                return "";
            }
        }

        String returnString = "";

        for (int i = 0; i < str.length; i++) {
            if (str[i].length() > 0) {
                if (quoted) {
                    returnString += ("'" + str[i] + "'");
                } else {
                    returnString += str[i];
                }

                if (i != (str.length - 1)) {
                    returnString += seperator;
                }
            }
        }

        return returnString;
    }

    /**
     * Creates a String from a Vector. If an error occurs while parsing an element,
     * "" will be uses instead.
     * @param  vec  Vector with Strings
     *         seperator  String that is inserted between two elements (i.e. komma)
     *         quoted  if the elements should be quoted with a "'".
     * @return      String with all Elements of the array.
      */
    public static String getJoinedString(Vector vec, String seperator, boolean quoted) {
        if (vec.size() == 0) {
            return getJoinedString((String[]) null, seperator, quoted);
        }

        String returnString = "";
        String[] strArray = new String[vec.size()];
        int counter = 0;
        Iterator it = vec.iterator();

        while (it.hasNext()) {
            try {
                strArray[counter] = (String) it.next();
                counter++;
            } catch (Exception ex) {
                strArray[counter] = "";
                counter++;
            }
        }

        return getJoinedString(strArray, seperator, quoted);
    }
}
