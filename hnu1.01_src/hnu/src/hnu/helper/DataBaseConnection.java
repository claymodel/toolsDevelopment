package hnu.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 * @author Martin Maier, Jason Harrop, Michael Smith
 */

public class DataBaseConnection {

    private static Log log = LogFactory.getLog(DataBaseConnection.class);

    /** Constructor of class */
    public DataBaseConnection() {
    }

    /** Fetches new DataBaseConnection from Pool */
    public Connection getDBConnection() {
        Connection conn = null;

        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/hnuDB");

            conn = ds.getConnection();
        } catch (Exception sqle) {
            log.error("Couln't get connection from DataSource", sqle);
            //sqle.printStackTrace();
        }

        return conn;
    }

    /**
     * This method closes a Connection-object and a Statement-object from
     * a Resulset-object.
     * IMPORTANT: Use this always after working on ResultSet returned by getRSfromStatement().
     */
    public boolean closeResultSet(ResultSet rs) {
        boolean returnValue = true;

        Statement stmt = null;
        Connection conn = null;

        if (rs !=null ) {
            try {
                stmt = rs.getStatement();
            } catch (SQLException ex) {
                log.debug("Couldn't getStatement from rs.");
                returnValue = false;
            }
        }
        if (stmt != null) {
            try {
                conn = stmt.getConnection();
            } catch (SQLException ex) {
                log.debug("Couldn't getConnection from stmt.");
                returnValue = false;
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                log.debug("Couldn't close stmt.");
                returnValue = false;
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                log.debug("Couldn't close conn.");
                returnValue = false;
            }
        }

        return returnValue;
    }

    /**
     * Excutes SQL-Statement and returns true if one or more records are returned.
     * If no record is returned or an Exception was thrown, the method reutrns false.
     * Method can be used for queries if a specific record exists.
     */
    public static boolean checkResult(String sql) {
        DataBaseConnection db = new DataBaseConnection();
        ResultSet rs = db.getRSfromStatement(sql);
        
        boolean returnValue = false;

        try {
            if (rs.next()) {
                returnValue = true;
            }
        } catch (Exception ex) {
            returnValue = false;
        } finally {
            db.closeResultSet(rs);
        }

        return returnValue;
    }

    /**
     * Executes a SQL-String and returns a Resultset from database.
     * Can be used best for SELECT-Language.
     */
    public ResultSet getRSfromStatement(String sql) {
        Connection conn = this.getDBConnection();
        Statement stmt = null;
        ResultSet rs = null;

        log.debug("About to execute: " + sql);
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException sqlEx) {
            log.error("Error executing: '" + sql + "'", sqlEx);
            try {
                stmt.close();
                    // only do that if there is an exception, since
                    // closing the statement automatically closes the resultset!
            } catch (Exception ex) {
                log.error("Couldn't close the statement.", ex);
            } finally {
                try {
                    conn.close();
                    // only do this if there is an exception, since
                    // closing the connection seems to close the rs.
                } catch (SQLException ex) {
                    log.error("Couldn't close the connection.", ex);
                }
            }
        }
        return rs;
    }

    /**
     * Executes a SQL-String and returns a Resultset from database.
     * Can be used best for SELECT-Language.
     */
    public ResultSet getRSfromStatement(String sql, Object[] parameters) {
        Connection conn = this.getDBConnection();
        Statement stmt = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;

        log.debug("About to execute: " + sql);
        try {
            if ( parameters == null || (parameters.length==0) ) {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
            } else {
                pStmt = conn.prepareStatement( sql );
                for (int i=0; i<parameters.length; i++) {
                    log.debug( "parameter " + i + ": " + parameters[i] );
                    pStmt.setObject(i+1, parameters[i]);
                }
                rs = pStmt.executeQuery();
            }
            log.debug(".. executed. ");
        } catch (SQLException sqlEx) {
            log.error("Error executing: '" + sql + "'", sqlEx);
            try {
                if (stmt != null) {
                    stmt.close();
                    // only do that if there is an exception, since
                    // closing the statement automatically closes the resultset!
                }
                if (pStmt != null) {
                    pStmt.close();
                }
            } catch (Exception ex) {
                log.error("Couldn't close the statement or connection.", ex);
            } finally {
                try {
                    conn.close();
                    // only do this if there is an exception, since
                    // closing the connection seems to close the rs.
                } catch (SQLException ex) {
                    log.error("Couldn't close the connection.", ex);
                }
            }
        }
        return rs;
    }



    /**
     * Executes a SQL-String and returns true if everything went ok.
     * Can be used for DML and DDL. <br />Pete, do not use with SELECTs.
     */
    public static boolean execute(String sql) {
        DataBaseConnection db = new DataBaseConnection();
        boolean returnValue = false;
        Connection conn = db.getDBConnection();
        Statement stmt = null;

        log.debug("About to execute: " + sql);
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            log.debug(".. executed without exception (hope to return 'true') ");
            returnValue = true;
            //stmt.close();  - why do this here and in finally?
            //conn.close();
        } catch (SQLException ex) {
            log.error("Error executing: '" + sql + "'", ex);
        } finally {
            try {
                stmt.close();
            } catch (Exception ex) {
                log.error("Couldn't close the statement (so returning 'false').", ex);
                returnValue = false;
            } finally {
                // irrespective of whether closing the statement succeeds
                try {
                    conn.close();
                } catch (SQLException ex) {
                    log.error("Couldn't close the connection (so returning 'false').", ex);
                    returnValue = false;
                }
            }
        }

        return returnValue;
    }


    /**
     * Create and Execute an SQL PreparedStatement and returns true if
     * everything went ok. Can be used for DML and DDL.
     * Borrows from apache.commons.scaffold.sql.StatementUtils (see
     * jakarta-commons-sandbox/scaffold)
     */
    public static boolean execute(String sql, Object[] parameters) {
        DataBaseConnection db = new DataBaseConnection();
        boolean returnValue = false;
        Connection conn = db.getDBConnection();
        PreparedStatement pStmt = null;
        Statement stmt = null;

        log.debug("About to execute: " + sql);
        try {
            if ( parameters == null || (parameters.length==0) ) {
                stmt = conn.createStatement();
                stmt.executeUpdate(sql);
            } else {
                pStmt = conn.prepareStatement( sql );
                for (int i=0; i<parameters.length; i++) {
                    log.debug( "parameter " + i + ": " + parameters[i] );
                    pStmt.setObject(i+1, parameters[i]);
                }
                pStmt.executeUpdate();
            }
            log.debug(".. executed without exception (hope to return 'true') ");
            returnValue = true;
        } catch (SQLException ex) {
            log.error("Error executing: '" + sql + "'", ex);
            returnValue = false;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (pStmt != null) {
                    pStmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                log.error("Couldn't close the statement or connection.", ex);
            }
        }
        return returnValue;
    }



    /**
     * Returns a String containing all values of a row of a resultset.
     * rs: the Data-ResultsSet<br />
     * row: which row of the ResultSet rs should be used<br />
     * brackets: if true, method surround the string with brackets<br />
     * This method can be used with workarounds for sub-selects with MySQL.<br />
     *<br />
     * Example:<br />
     * getValuesStringFromResultSet(rs, 1, true) returns<br />
     * "('123','23455','2341','1234')"<br />
     */
    public static String getValuesStringFromResultSet(ResultSet rs, int row,
        boolean brackets) {
        String tempStr = new String("");

        try {
            if (rs.next()) {
                if (brackets) {
                    tempStr = "(";
                }

                tempStr += ("'" + rs.getString(row) + "'");
            } else {
                if (brackets) {
                    return "('')";
                } else {
                    return "''";
                }
            }

            while (rs.next()) {
                tempStr += (",'" + rs.getString(row) + "'");
            }

            if (brackets) {
                return tempStr + ")";
            } else {
                return tempStr;
            }
        } catch (Exception ex) {
            return "('')";
        }
    }
}
