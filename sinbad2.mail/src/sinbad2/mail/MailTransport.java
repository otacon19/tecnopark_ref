package sinbad2.mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailTransport {

	public static void sendMail(MailAccount account, MailMessage message) throws MailException {
		try {
			Transport.send(composeMessage(account, message));
		} catch (MessagingException e) {
			throw new MailException(e.getMessage());
		}
	}

	private static Message composeMessage(MailAccount account, MailMessage message) throws MessagingException {
		Message mimeMessage = buildMessage(account);
		mimeMessage.setFrom(new InternetAddress(account.getMail()));
		mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(message.getTo()));
		mimeMessage.setSubject(message.getSubject());
		mimeMessage.setText(message.getText());
		return mimeMessage;
	}

	private static Message buildMessage(MailAccount account) {
		return new MimeMessage(account.newSession());
	}
}