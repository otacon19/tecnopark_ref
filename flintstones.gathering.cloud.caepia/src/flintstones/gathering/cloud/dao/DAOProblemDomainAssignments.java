package flintstones.gathering.cloud.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import flintstones.gathering.cloud.model.KeyDomainAssignment;
import flintstones.gathering.cloud.model.Problem;
import sinbad2.database.Database;
import sinbad2.database.DatabaseManager;

public class DAOProblemDomainAssignments {

	public static final String TABLE = "domainassignment";
	public static final String PROBLEM = "problem";
	public static final String ALTERNATIVE = "alternative";
	public static final String CRITERION = "criterion";
	public static final String EXPERT = "expert";
	public static final String DOMAIN = "domain";

	private static DAOProblemDomainAssignments _dao = null;

	private DAOProblemDomainAssignments() {
	}

	public static DAOProblemDomainAssignments getDAO() {

		if (_dao == null) {
			_dao = new DAOProblemDomainAssignments();
		}

		return _dao;
	}

	public static String getCreationTableSql() {
		String result = null;

		result = "create table " + TABLE + "(" + PROBLEM
				+ " VARCHAR(50) NOT NULL, " + ALTERNATIVE
				+ " TEXT NOT NULL, " + CRITERION + " TEXT NOT NULL, " + EXPERT + " TEXT NOT NULL, " + DOMAIN + " VARCHAR(255) NOT NULL, PRIMARY KEY(" + PROBLEM + ","
				+ ALTERNATIVE + "(255)," + CRITERION + "(255)," + EXPERT + "(255)));";

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

	public void createProblemDomainAssignments(Problem problem) {
		try {
			Connection c = getConnection();
			Statement st = c.createStatement();
			String problemId = problem.getId();
			Map<KeyDomainAssignment, String> domainAssignments = problem.getDomainAssignments();
			String domain = null;
			for (KeyDomainAssignment key : domainAssignments.keySet()) {
				domain = domainAssignments.get(key);
				st.executeUpdate("insert into " + TABLE + " values ('" + problemId + "','" + key.getAlternative().replace("'", "''") + "','" + key.getCriterion().replace("'", "''") + "','" + key.getExpert().replace("'",  "''") + "','" + domain + "')");
			}
			st.close();
		} catch (Exception e) {
			
		}
	}
	
	public void createProblemDomainAssignment(Problem problem, Map<KeyDomainAssignment, String> assignment) {
		try {
			Connection c = getConnection();
			Statement st = c.createStatement();
			String problemId = problem.getId();
			String domain = null;
			
			for(KeyDomainAssignment key: assignment.keySet()) {
				domain = assignment.get(key);
				st.executeUpdate("replace into " + TABLE + " values ('" + problemId + "','" + key.getAlternative().replace("'", "''") + "','" + key.getCriterion().replace("'", "''") + "','" + key.getExpert().replace("'", "''") + "','" + domain + "')");
			}
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeProblemDomainAssignments(String problem) {
		try {
			Connection c = getConnection();
			PreparedStatement pst = c.prepareStatement("delete from " + TABLE + " where " + PROBLEM + " = ?");
			pst.setString(1, problem);
			pst.executeUpdate();
			pst.close();
		} catch (Exception e) {
		}
	}

	public void removeProblemDomainAssignments(Problem problem) {
		removeProblemDomainAssignments(problem.getId());
	}
	
	public Map<KeyDomainAssignment, String> getProblemDomainAssignments(String problem) {
		Map<KeyDomainAssignment, String> result = new HashMap<KeyDomainAssignment, String>();
		
		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String select = "select * from " + TABLE + " where " + PROBLEM + "='" + problem + "';";
			ResultSet rs = st.executeQuery(select);
			String criterion;
			String alternative;
			String expert;
			String domain;
			while (rs.next()) {
				criterion = rs.getString(CRITERION);
				alternative = rs.getString(ALTERNATIVE);
				domain = rs.getString(DOMAIN);
				expert = rs.getString(EXPERT);
				result.put(new KeyDomainAssignment(alternative, criterion, expert), domain);
			}
		} catch (Exception e) {
			
		}
		
		return result;
	}
	
	public Map<KeyDomainAssignment, String> getProblemDomainAssignments(Problem problem) {
		return getProblemDomainAssignments(problem.getId());
	}

}
