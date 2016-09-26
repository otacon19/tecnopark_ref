package flintstones.gathering.cloud.mail;

import flintstones.gathering.cloud.APP;
import flintstones.gathering.cloud.model.User;
import flintstones.gathering.cloud.nls.Messages;
import sinbad2.mail.MailAccount;
import sinbad2.mail.MailAccountManager;
import sinbad2.mail.MailException;
import sinbad2.mail.MailMessage;
import sinbad2.mail.MailTransport;

public class UserMailGenerator {

	public static void buildAndSendInvitation(User user) {
		MailMessage result = null;
		
		if (user != null) {
			String subject = Messages.UserMailGenerator_Welcome_to + APP.APP_NAME;
			String text = Messages.UserMailGenerator_You_has_been_registered + APP.APP_NAME + Messages.UserMailGenerator_The_account_information_and_your_URL_to_access_to_the_system_are_the_next + APP.URL + Messages.UserMailGenerator_User
					+ user.getMail() + Messages.UserMailGenerator_Password + user.getPass();
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
