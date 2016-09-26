package flintstones.gathering.cloud.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import flintstones.gathering.cloud.mail.UserMailGenerator;
import flintstones.gathering.cloud.model.User;
import sinbad2.database.Database;
import sinbad2.database.DatabaseManager;

public class DAOUser {

	public static final String TABLE = "user"; //$NON-NLS-1$
	public static final String MAIL = "mail"; //$NON-NLS-1$
	public static final String PASS = "pass"; //$NON-NLS-1$
	public static final String ADMIN = "admin"; //$NON-NLS-1$
	public static final String PROBLEMS = "problems"; //$NON-NLS-1$

	private static DAOUser _dao = null;

	private DAOUser() {
	}

	public static DAOUser getDAO() {

		if (_dao == null) {
			_dao = new DAOUser();
		}

		return _dao;
	}

	public static String getCreationTableSql() {
		String result = null;

		result = "create table " + TABLE + "(" + MAIL //$NON-NLS-1$ //$NON-NLS-2$
				+ " VARCHAR(255) NOT NULL, " + PASS + " VARCHAR(8) NOT NULL, " //$NON-NLS-1$ //$NON-NLS-2$
				+ ADMIN + " VARCHAR(5) NOT NULL, " + PROBLEMS //$NON-NLS-1$
				+ " VARCHAR(5) NOT NULL, PRIMARY KEY(" + MAIL + "));"; //$NON-NLS-1$ //$NON-NLS-2$

		return result;
	}

	public Connection getConnection() {
		Connection result = null;
		
		Database db = DatabaseManager.getInstance().getApplicationDatabase();

		if (db != null) {
			result = db.getConnection();
		}
		return result;
	}

	public void createUser(User user) {

		try {
			Connection c = getConnection();
			Statement st = c.createStatement();
			st.executeUpdate("insert into " + TABLE + " values ('" //$NON-NLS-1$ //$NON-NLS-2$
					+ user.getMail() + "','" + user.getPass() + "','" //$NON-NLS-1$ //$NON-NLS-2$
					+ Boolean.toString(user.getAdmin()) + "','" //$NON-NLS-1$
					+ Boolean.toString(user.getManageProblems()) + "')"); //$NON-NLS-1$
			st.close();
		} catch (Exception e) {
		}
	}

	public void removeUser(String mail) {

		try {
			Connection c = getConnection();
			PreparedStatement pst = c.prepareStatement("delete from " + TABLE //$NON-NLS-1$
					+ " where " + MAIL + " = ?"); //$NON-NLS-1$ //$NON-NLS-2$
			pst.setString(1, mail);
			pst.executeUpdate();
			pst.close();
		} catch (Exception e) {
		}
	}

	public void removeUser(User user) {
		removeUser(user.getMail());
	}

	public User getUser(String mail) {
		User result = null;

		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String select = "select * from " + TABLE + " where " + MAIL + "='" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ mail + "';"; //$NON-NLS-1$
			ResultSet rs = st.executeQuery(select);
			while (rs.next()) {
				result = new User(rs.getString(MAIL), rs.getString(PASS),
						Boolean.parseBoolean(rs.getString(ADMIN)),
						Boolean.parseBoolean(rs.getString(PROBLEMS)));
			}
		} catch (Exception e) {
		}

		return result;
	}

	public List<User> getAllUsers() {

		List<User> result = new LinkedList<User>();

		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String select = "select * from " + TABLE + ";"; //$NON-NLS-1$ //$NON-NLS-2$
			ResultSet rs = st.executeQuery(select);
			while (rs.next()) {
				result.add(new User(rs.getString(MAIL), rs.getString(PASS),
						Boolean.parseBoolean(rs.getString(ADMIN)), Boolean
								.parseBoolean(rs.getString(PROBLEMS))));
			}
		} catch (Exception e) {
		}

		return result;
	}

	public User getUser(String mail, String pass) {
		User result = null;

		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String select = "select * from " + TABLE + " where " + MAIL + "='" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ mail + "' and " + PASS + "='" + pass + "';"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			ResultSet rs = st.executeQuery(select);
			while (rs.next()) {
				result = new User(rs.getString(MAIL), rs.getString(PASS),
						Boolean.parseBoolean(rs.getString(ADMIN)),
						Boolean.parseBoolean(rs.getString(PROBLEMS)));
			}
		} catch (Exception e) {
		}

		return result;
	}

	public boolean validatePass(String mail, String pass) {
		boolean result = false;

		User user = getUser(mail);
		if (user != null) {
			result = user.getPass().equals(pass);
		}
		return result;
	}

	public void changePass(User user, String pass) {
		if (!pass.equals(user.getPass())) {

			try {
				Connection c = getConnection();
				Statement st = c.createStatement();

				String update = "update " + TABLE + " set " + PASS + " = '" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						+ pass + "' where " + MAIL + " = '" + user.getMail() //$NON-NLS-1$ //$NON-NLS-2$
						+ "';"; //$NON-NLS-1$
				st.executeUpdate(update);
				st.close();
			} catch (Exception e) {

			}
		}
	}

	public User createAdminAccount(String mail) {
		User result = null;

		UUID.randomUUID().toString();
		String pass = UUID.randomUUID().toString().substring(0, 8);

		result = new User(mail, pass, true, true);
		createUser(result);
		
		sendInvitationToExpert(result);

		return result;
	}

	public User createAccount(String mail) {
		User result = null;

		UUID.randomUUID().toString();
		String pass = UUID.randomUUID().toString().substring(0, 8);

		result = new User(mail, pass, false, false);
		createUser(result);
		
		sendInvitationToExpert(result);

		return result;
	}
	
	public void sendInvitationToExpert(User user) {
		
		UserMailGenerator.buildAndSendInvitation(user);
	}
	
	public void modifyAdminPermission(String mail, boolean admin) {
		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String update = "update " + TABLE + " set " + ADMIN + " = '" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ Boolean.toString(admin) + "' where " + MAIL + " = '" + mail //$NON-NLS-1$ //$NON-NLS-2$
					+ "';"; //$NON-NLS-1$
			st.executeUpdate(update);
			st.close();
		} catch (Exception e) {

		}
		
	}
	
	public void modifyManageProblemsPermission(String mail, boolean manageProblems) {
		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String update = "update " + TABLE + " set " + PROBLEMS + " = '" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ Boolean.toString(manageProblems) + "' where " + MAIL + " = '" + mail //$NON-NLS-1$ //$NON-NLS-2$
					+ "';"; //$NON-NLS-1$
			st.executeUpdate(update);
			st.close();
		} catch (Exception e) {

		}
	}
}
