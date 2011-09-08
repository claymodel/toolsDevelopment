package hnu.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

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

public class SendMail {
    /**
    * Class constructor
    */
    public SendMail() {
    }

    /**
    * Sends a simple mail message.
    * @param String recipient of mail message
    * @param String subject of mail message
    * @param String text of mail message
    */
    public boolean sendMail(String recipient, String subject, String body) {
        Properties props = new Properties();

        try {
            Properties prop = System.getProperties();
            String path = prop.getProperty("catalina.home");
            String configStr = path + "/webapps/hnu/WEB-INF/db.properties";

            props.load(new FileInputStream(configStr));
        } catch (IOException ex) {
            return false;
        }

        Session session = Session.getDefaultInstance(props, null);

        MimeMessage msg = new MimeMessage(session);

        try {
            msg.setFrom(new InternetAddress(props.getProperty("sender")));

            MimeMultipart mimeMultipart = new MimeMultipart();
            MimeBodyPart mimeBodyPart = new MimeBodyPart();

            mimeBodyPart.setText(body);
            mimeMultipart.addBodyPart(mimeBodyPart);

            msg.setContent(mimeMultipart);
            msg.setRecipients(Message.RecipientType.TO, recipient);
            msg.setSubject(subject);
            msg.setSentDate(new Date());

            Transport.send(msg);
        } catch (MessagingException ex) {
            ex.printStackTrace();

            return false;
        }

        return true;
    }
}
