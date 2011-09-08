package hnu.helper;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

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

public class DateFormatter {
  private Properties properties = new Properties();
  private int time = 0;
  private SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

  /**
   * Time/Date formats:
   * 0: dd.mm.yyyy hh:mm a
   * 1: yyyy-mm-dd hh:mm a
   * 2: weekday, dd.mm.yyyy hh:mm a
   * 3: weekday, yyyy-mm-dd hh:mm a
   * 4: dd.mm.yyyy
   * 5: yyyy-mm-dd
   */

  /**
   * Load configuration from ApplikationConfig.properties and sets date-pattern.
   * */
  public DateFormatter() {
    try {
      Properties prop = System.getProperties();
      String path = prop.getProperty("catalina.home");
      String configStr = path + "/webapps/hnu/WEB-INF/ApplicationConfig.properties";
      properties.load(new FileInputStream(configStr));
      time = Integer.valueOf(properties.getProperty("time")).intValue();

      switch (time) {
	case 0: simpleDateFormat.applyPattern("dd.MM.yyyy 'at' hh:mm a"); break;
	case 1: simpleDateFormat.applyPattern("yyyy-MM-dd 'at' hh:mm a"); break;
	case 2: simpleDateFormat.applyPattern("E, dd.MM.yyyy 'at' hh:mm a"); break;
	case 3: simpleDateFormat.applyPattern("E, yyyy-MM-dd 'at' hh:mm a"); break;
	case 4: simpleDateFormat.applyPattern("dd.MM.yyyy"); break;
	case 5: simpleDateFormat.applyPattern("yyyy-MM-dd"); break;
      }
    } catch (Exception ex) {

    }
  }

  /**
   * Generates Date-String depending on HNU configuration.
   * @param java.util.Date Date to format
   * @return final-formatted Date for use within HNU
   * */
  public String getDate(Date date) {
    try {
      String returnDate = simpleDateFormat.format(date);
      return returnDate;

    } catch (Exception ex) {
      return "#Date not parseable#";
    }
  }
}