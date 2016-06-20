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

public class DAOProblemAlternatives {

	public static final String TABLE = "alternatives";
	public static final String PROBLEM = "problem";
	public static final String ALTERNATIVE = "alternative";

	private static DAOProblemAlternatives _dao = null;

	private DAOProblemAlternatives() {
	}

	public static DAOProblemAlternatives getDAO() {

		if (_dao == null) {
			_dao = new DAOProblemAlternatives();
		}

		return _dao;
	}

	public static String getCreationTableSql() {
		String result = null;

		result = "create table " + TABLE + "(" + PROBLEM
				+ " VARCHAR(50) NOT NULL, " + ALTERNATIVE
				+ " TEXT NOT NULL, PRIMARY KEY(" + PROBLEM + ","
				+ ALTERNATIVE + "(255)));";

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

	public void createProblemAlternatives(Problem problem) {
		try {
			Connection c = getConnection();
			Statement st = c.createStatement();
			String id = problem.getId();
			List<String> alternatives = problem.getAlternatives();
			for (String alternative : alternatives) {
				alternative = alternative.replace( "'" , "''" );
				st.executeUpdate("insert into " + TABLE + " values ('" + id + "','" + alternative + "')");
			}
			st.close();
		} catch (Exception e) {
			
		}
	}
	
	public void removeProblemAlternatives(String problem) {
		try {
			Connection c = getConnection();
			PreparedStatement pst = c.prepareStatement("delete from " + TABLE
					+ " where " + PROBLEM + " = ?");
			pst.setString(1, problem);
			pst.executeUpdate();
			pst.close();
		} catch (Exception e) {
		}
	}

	public void removeProblemAlternatives(Problem problem) {
		removeProblemAlternatives(problem.getId());
	}
	
	public List<String> getProblemAlternatives(String problem) {
		List<String> result = new LinkedList<String>();
		
		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String select = "select * from " + TABLE + " where " + PROBLEM
					+ "='" + problem + "';";
			ResultSet rs = st.executeQuery(select);
			while (rs.next()) {
				result.add(rs.getString(ALTERNATIVE));
			}
			Collections.sort(result);
		} catch (Exception e) {
			
		}
		
		return result;
	}
	
	public List<String> getProblemAlternatives(Problem problem) {
		return getProblemAlternatives(problem.getId());
	}

}
