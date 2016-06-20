package sinbad2.mail;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class MailAccount {
	
	private Properties _properties;
	private String _mail;
	private String _password;
	
	public MailAccount() {
		super();
		_properties = null;
		_mail = null;
		_password = null;
	}
	
	public MailAccount(Properties properties, String mail, String password) {
		this();
		setProperties(properties);
		setMail(mail);
		setPassword(password);		
	}
	
	public void setProperties(Properties properties) {
		_properties = properties;
	}
	
	public Properties getProperties() {
		return _properties;
	}
	
	public void setMail(String mail) {
		_mail = mail;
	}
	
	public String getMail() {
		return _mail;
	}
	
	public void setPassword(String password) {
		_password = password;
	}
	
	public String getPassword() {
		return _password;
	}
	
	public Session newSession() {
		Session session = Session.getInstance(_properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(_mail, _password);			}
		});
		return session;
	}
}