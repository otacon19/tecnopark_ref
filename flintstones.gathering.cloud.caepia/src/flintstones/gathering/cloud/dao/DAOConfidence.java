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

public class DAOConfidence {

	public static final String TABLE = "experts_condifence";
	public static final String PROBLEM = "problem";
	public static final String EXPERT = "expert";
	public static final String ALTERNATIVE = "alternative";
	public static final String CRITERION = "criterion";
	public static final String CONFIDENCE = "confidence";
	
	private static DAOConfidence _dao = null;
	
	public DAOConfidence() {}
	
	public static DAOConfidence getDAO() {

		if (_dao == null) {
			_dao = new DAOConfidence();
		}

		return _dao;
	}

	public static String getCreationTableSql() {
		String result = null;

		result = "create table " + TABLE + "(" + PROBLEM
				+ " VARCHAR(50) NOT NULL, " + CRITERION + " TEXT NOT NULL, "
				+ ALTERNATIVE + " TEXT NOT NULL, "
				+ EXPERT + " VARCHAR(255) NOT NULL, "
				+ CONFIDENCE + " VARCHAR(255) NOT NULL,"
				+ "PRIMARY KEY(" + PROBLEM + "," + CRITERION + "(255)," + ALTERNATIVE + "(255)," + EXPERT + "));";

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

	public void removeConfidenceProblem(String problem) {

		try {
			Connection c = getConnection();
			PreparedStatement pst = c.prepareStatement("delete from " + TABLE + " where " + PROBLEM + " = ?");
			pst.setString(1, problem);
			pst.executeUpdate();
			pst.close();
		} catch (Exception e) {
		}
	}

	public void removeConfidencesProblem(Problem problem) {
		removeConfidenceProblem(problem.getId());
	}
	
	public void removeConfidence(String problem, KeyDomainAssignment key) {

		try {
			Connection c = getConnection();
			PreparedStatement pst = c.prepareStatement("delete from " + TABLE + " where " + PROBLEM + " = ?" + " and " + ALTERNATIVE + " = ?" + " and " + CRITERION
					+ " = ?" + " and " + EXPERT + " = ?");
			pst.setString(1, problem);
			pst.setString(2, key.getAlternative());
			pst.setString(3, key.getCriterion());
			pst.setString(4, key.getExpert());
			pst.executeUpdate();
			pst.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<KeyDomainAssignment, Double> getConfidencesExpert(String problem, String expertId) {
		Map<KeyDomainAssignment, Double> result = new HashMap<KeyDomainAssignment, Double>();
		
		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String select = "select * from " + TABLE + " where " + PROBLEM + " = '" + problem + "' and " + EXPERT + " = '" + expertId + "';";
			ResultSet rs = st.executeQuery(select);

			String criterion, alternative, expert;
			while (rs.next()) {
				criterion = rs.getString(CRITERION);
				alternative = rs.getString(ALTERNATIVE);
				expert = rs.getString(EXPERT);
				result.put(new KeyDomainAssignment(alternative, criterion, expert), Double.parseDouble(rs.getString(CONFIDENCE)));
			}
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}
	
	public void insertConfidence(Problem problem, KeyDomainAssignment key, double confidence) {

		try {
			Connection c = getConnection();
			Statement st = c.createStatement();
			
			String criterion = key.getCriterion();
			String alternative = key.getAlternative();
			String expert = key.getExpert();
			
			String statement = "replace into " + TABLE + " values("
					+ "'" + problem.getId() + "'" + ", "
					+ "'" + criterion + "'" + ", "
					+ "'" + alternative + "'" + ", " 
					+ "'" + expert + "'" + ", "
					+ "'" + confidence
					+ "');";
			
			st.executeUpdate(statement);

			st.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
