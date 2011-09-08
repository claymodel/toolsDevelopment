package hnu.helper;

import hnu.LoginBean;

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

public class LoginChecker {
    /**
     * Creates a new LoginChecker.
     */
    public LoginChecker() {
    }

    /**
     * Compares userType parameter with the type-property of the LoginBean in the
     * session.
     * @param  userType  Usertype to be compared
     *         request   HttpServletRequest, that contains the Loginbean
     * @return      true if Strings match, otherwise false
     * @see         HttpServletRequest
      */
    private static boolean isLoggedIn(String userType, HttpServletRequest request) {
        String type = getLoginType(request.getSession().getAttribute("loginBean"));

        if (type == null) {
            return false;
        }

        if (type.equals(userType)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if there is a LoginBean-Object in the session and compares the
     * Type-String in the Bean with "admin", if a bean is found. True is only
     * returned, if there is a loginbean and its type-property is "admin".
     * @param  request  HttpServletRequest, that contains the session
     * @return      true if admin is logged in, otherwise false
     * @see         HttpServletRequest
      */
    public static boolean isAdminLoggedIn(HttpServletRequest request) {
        return isLoggedIn("admin", request);
    }

    /**
     * Checks if there is a LoginBean-Object in the session and compares the
     * Type-String in the Bean with "staff", if a bean is found. True is only
     * returned, if there is a loginbean and its type-property is "staff".
     * @param  request  HttpServletRequest, that contains the session
     * @return      true if staff is logged in, otherwise false
     * @see         HttpServletRequest
      */
    public static boolean isUserLoggedIn(HttpServletRequest request) {
        return isLoggedIn("user", request);
    }

    /**
     * Checks if there is a LoginBean-Object in the session and compares the
     * Type-String in the Bean with "user", if a bean is found. True is only
     * returned, if there is a loginbean and its type-property is "user".
     * @param  request  HttpServletRequest, that contains the session
     * @return      true if user is logged in, otherwise false
     * @see         HttpServletRequest
      */
    public static boolean isStaffLoggedIn(HttpServletRequest request) {
        return isLoggedIn("staff", request);
    }

    /**
     * Tries to cast an Object to LoginBean. If that is not possible null is returned.
     * If it is a LoginBean, method returns type-property of the bean.
     * @param  obj  Object that is casted to LoginBean
     * @return      String, that contains type-property of LoginBean
     * @see         LoginBean
      */
    private static String getLoginType(Object obj) {
        if (obj instanceof LoginBean) {
            LoginBean loginBean = (LoginBean) obj;

            return loginBean.getType();
        } else {
            return null;
        }
    }
}
