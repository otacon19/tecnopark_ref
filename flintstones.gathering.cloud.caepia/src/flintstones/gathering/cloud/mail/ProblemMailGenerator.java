package flintstones.gathering.cloud.mail;

import flintstones.gathering.cloud.APP;
import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.model.User;
import flintstones.gathering.cloud.nls.Messages;
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
			String subject = Messages.ProblemMailGenerator_Problem_completed;
			String text = Messages.ProblemMailGenerator_All_experts_have_sent_their_assessments + problem.getId() + "'"; //$NON-NLS-2$
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
			String subject = Messages.ProblemMailGenerator_Problem_invitation + id + "'"; //$NON-NLS-2$
			String text = Messages.ProblemMailGenerator_You_have_been_invited_to_participate_in_the_problem + id + Messages.ProblemMailGenerator_Please_access + APP.APP_NAME + Messages.ProblemMailGenerator_Identify_yourself_with_your_user_account_and_password_and_provide_your_assessments + APP.URL;
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
