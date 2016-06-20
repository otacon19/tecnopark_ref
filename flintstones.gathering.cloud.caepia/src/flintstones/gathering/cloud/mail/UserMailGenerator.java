package flintstones.gathering.cloud.mail;

import flintstones.gathering.cloud.APP;
import flintstones.gathering.cloud.model.User;
import sinbad2.mail.MailAccount;
import sinbad2.mail.MailAccountManager;
import sinbad2.mail.MailException;
import sinbad2.mail.MailMessage;
import sinbad2.mail.MailTransport;

public class UserMailGenerator {

	public static void buildAndSendInvitation(User user) {
		MailMessage result = null;
		
		if (user != null) {
			String subject = "Bievenido a " + APP.APP_NAME;
			String text = "Ha sido registrado en " + APP.APP_NAME + ".\nLa información sobre su cuenta y la URL para acceder al sistema son las siguientes:\nURL: " + APP.URL + "\nUsuario: "
					+ user.getMail() + "\nContraseña: " + user.getPass();
			result = new MailMessage(user.getMail(), subject, text);
		}
		
		sendInvitation(result);
	}
	
	private static void sendInvitation(MailMessage message) {
		if (message != null) {
			MailAccountManager mailAccountManager = MailAccountManager.getInstance();
			MailAccount account = mailAccountManager.getApplicationMailAccount();
			try {
				MailTransport.sendMail(account, message);
			} catch (MailException e) {
				e.printStackTrace();
			}
		}
	}
}
