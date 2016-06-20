package sinbad2.mail;

public class MailMessage {

	private String _to;
	private String _subject;
	private String _text;
	
	public MailMessage() {
		super();
		_to = null;
		_subject = null;
		_text = null;
	}
	
	public MailMessage(String to, String subject, String text) {
		this();
		setTo(to);
		setSubject(subject);
		setText(text);
	}
	
	public void setTo(String to) {
		_to = to;
	}
	
	public String getTo() {
		return _to;
	}
	
	public void setSubject(String subject) {
		_subject = subject;
	}
	
	public String getSubject() {
		return _subject;
	}
	
	public void setText(String text) {
		_text = text;
	}
	
	public String getText() {
		return _text;
	}
}
