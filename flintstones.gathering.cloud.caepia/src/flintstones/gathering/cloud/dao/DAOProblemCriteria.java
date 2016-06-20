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

public class DAOProblemCriteria {

	public static final String TABLE = "criteria";
	public static final String PROBLEM = "problem";
	public static final String CRITERION = "criterion";

	private static DAOProblemCriteria _dao = null;

	private DAOProblemCriteria() {
	}

	public static DAOProblemCriteria getDAO() {

		if (_dao == null) {
			_dao = new DAOProblemCriteria();
		}

		return _dao;
	}

	public static String getCreationTableSql() {
		String result = null;

		result = "create table " + TABLE + "(" + PROBLEM
				+ " VARCHAR(50) NOT NULL, " + CRITERION
				+ " TEXT NOT NULL, PRIMARY KEY(" + PROBLEM + ","
				+ CRITERION + "(255)));";

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

	public void createProblemCriteria(Problem problem) {
		try {
			Connection c = getConnection();
			Statement st = c.createStatement();
			String id = problem.getId();
			List<String> criteria = problem.getCriteria();
			for (String criterion : criteria) {
				criterion = criterion.replace( "'" , "''" );
				st.executeUpdate("insert into " + TABLE + " values ('" + id + "','" + criterion + "')");
			}
			st.close();
		} catch (Exception e) {
			
		}
	}
	
	public void removeProblemCriteria(String problem) {
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

	public void removeProblemCriteria(Problem problem) {
		removeProblemCriteria(problem.getId());
	}
	
	public List<String> getProblemCriteria(String problem) {
		List<String> result = new LinkedList<String>();
		
		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String select = "select * from " + TABLE + " where " + PROBLEM
					+ "='" + problem + "';";
			ResultSet rs = st.executeQuery(select);
			while (rs.next()) {
				result.add(rs.getString(CRITERION));
			}
			Collections.sort(result);
		} catch (Exception e) {
			
		}
		
		return result;
	}
	
	public List<String> getProblemCriteria(Problem problem) {
		return getProblemCriteria(problem.getId());
	}

}
