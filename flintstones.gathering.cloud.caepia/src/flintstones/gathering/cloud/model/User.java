package flintstones.gathering.cloud.model;

public class User {

	private String _mail;
	private String _pass;
	private boolean _admin;
	private boolean _manageProblems;
	
	public User() {
		_mail = null;
		_pass = null;
		_admin = false;
		_manageProblems = false;
	}
	
	public User(String mail, String pass, boolean admin, boolean manageProblems) {
		setMail(mail);
		setPass(pass);
		setAdmin(admin);
		setManageProblems(manageProblems);
	}
	
	public void setMail(String mail) {
		_mail = mail;
	}
	
	public void setPass(String pass) {
		_pass = pass;
	}
	
	public void setAdmin(boolean admin) {
		_admin = admin;
	}
	
	public void setManageProblems(boolean manageProblems) {
		_manageProblems = manageProblems;
	}
	
	public boolean getManageProblems() {
		return _manageProblems;
	}
	
	public String getMail() {
		return _mail;
	}
	
	public String getPass() {
		return _pass;
	}
	
	public boolean getAdmin() {
		return _admin;
	}
}
