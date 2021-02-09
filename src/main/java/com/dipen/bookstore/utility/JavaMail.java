package com.dipen.bookstore.utility;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMail {
	
	public static void sendmail(String emailTo) throws AddressException, MessagingException, IOException {
		   Properties props = new Properties();
		   props.put("mail.smtp.auth", "true");
		   props.put("mail.smtp.starttls.enable", "true");
		   props.put("mail.smtp.host", "smtp.gmail.com");
		   props.put("mail.smtp.port", "587");
		   
		   Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		      protected PasswordAuthentication getPasswordAuthentication() {
		         return new PasswordAuthentication("global.yellow.group@gmail.com", "Glob@l1234");
		      }
		   });
		   Message msg = new MimeMessage(session);
		   msg.setFrom(new InternetAddress("global.yellow.group@gmail.com", false));

		   msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
		   msg.setSubject("Bookstore Registration");
		   msg.setContent("Congratulations on successful registration to our bookstore app", "text/html");
		   msg.setSentDate(new Date());

		  
		   Transport.send(msg);   
		   System.out.println("Email is send");

	
}
}
 
