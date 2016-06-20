package flintstones.gathering.cloud.mail;

import flintstones.gathering.cloud.APP;
import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.model.User;
import sinbad2.mail.MailAccount;
import sinbad2.mail.MailAccountManager;
import sinbad2.mail.MailException;
import sinbad2.mail.MailMessage;
import sinbad2.mail.MailTransport;

public class ProblemMailGenerator {
	
	public static void buildAndSendAllAssignmentsProblemMail(Problem problem) {
		MailMessage result = null;
		
		String to = problem.getAdmin().getMail();
		if (to != null) {
			String subject = "Problema completado";
			String text = "Todos los expertos han enviado sus valoraciones en el problema '" + problem.getId() + "'";
			result = new MailMessage(to, subject, text);
		}
		
		sendNotificationProblem(result);
	}
	
	private static void sendNotificationProblem(MailMessage message) {
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
	
	public static void buildAndSendInvitationProblemMail(User user, String id) {
		MailMessage result = null;
		
		if (user != null) {
			String subject = "Invitación al problema '" + id + "'";
			String text = "Ha sido invitado a participar en el problema '" + id + "'\nPor favor, accede a " + APP.APP_NAME + ", identifiquese con su cuenta de usuario y contraseña y proporcione sus valoraciones.\n\n" + APP.URL;
			result = new MailMessage(user.getMail(), subject, text);
		}
		
		sendInvitationProblem(result);
	}
	
	private static void sendInvitationProblem(MailMessage message) {
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
