package flintstones.gathering.cloud.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import flintstones.gathering.cloud.model.Problem;
import sinbad2.database.Database;
import sinbad2.database.DatabaseManager;

public class DAOProblemExperts {

	public static final String TABLE = "experts"; //$NON-NLS-1$
	public static final String PROBLEM = "problem"; //$NON-NLS-1$
	public static final String EXPERT = "expert"; //$NON-NLS-1$

	private static DAOProblemExperts _dao = null;

	private DAOProblemExperts() {
	}

	public static DAOProblemExperts getDAO() {

		if (_dao == null) {
			_dao = new DAOProblemExperts();
		}

		return _dao;
	}

	public static String getCreationTableSql() {
		String result = null;

		result = "create table " + TABLE + "(" + PROBLEM //$NON-NLS-1$ //$NON-NLS-2$
				+ " VARCHAR(50) NOT NULL, " + EXPERT //$NON-NLS-1$
				+ " VARCHAR(255) NOT NULL, PRIMARY KEY(" + PROBLEM + "," //$NON-NLS-1$ //$NON-NLS-2$
				+ EXPERT + "));"; //$NON-NLS-1$

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

	public void createProblemExperts(Problem problem) {
		try {
			Connection c = getConnection();
			Statement st = c.createStatement();
			String id = problem.getId();
			List<String> experts = problem.getExperts();
			for (String expert : experts) {
				st.executeUpdate("insert into " + TABLE + " values ('" + id + "','" + expert + "')"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			}
			st.close();
		} catch (Exception e) {
			
		}
	}
	
	public void removeProblemExperts(String problem) {
		try {
			Connection c = getConnection();
			PreparedStatement pst = c.prepareStatement("delete from " + TABLE //$NON-NLS-1$
					+ " where " + PROBLEM + " = ?"); //$NON-NLS-1$ //$NON-NLS-2$
			pst.setString(1, problem);
			pst.executeUpdate();
			pst.close();
		} catch (Exception e) {
		}
	}

	public void removeProblemExperts(Problem problem) {
		removeProblemExperts(problem.getId());
	}
	
	public List<String> getProblemExperts(String problem) {
		List<String> result = new LinkedList<String>();
		
		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String select = "select * from " + TABLE + " where " + PROBLEM //$NON-NLS-1$ //$NON-NLS-2$
					+ "='" + problem + "';"; //$NON-NLS-1$ //$NON-NLS-2$
			ResultSet rs = st.executeQuery(select);
			while (rs.next()) {
				result.add(rs.getString(EXPERT));
			}
			Collections.sort(result);
		} catch (Exception e) {
			
		}
		
		return result;
	}
	
	public List<String> getProblemExperts(Problem problem) {
		return getProblemExperts(problem.getId());
	}

}
