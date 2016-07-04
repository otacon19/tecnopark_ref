package flintstones.gathering.cloud.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Map;

import flintstones.gathering.cloud.model.Problem;
import sinbad2.database.Database;
import sinbad2.database.DatabaseManager;

public class DAOProblemValuations {
	
	public static final String TABLE = "problemvaluations";
	public static final String DOMAIN_ID = "domain_id";
	public static final String VALUATION_ID = "valuation_id";
	
	private static DAOProblemValuations _dao = null;
	
	public DAOProblemValuations() {}
	
	public static DAOProblemValuations getDAO() {

		if (_dao == null) {
			_dao = new DAOProblemValuations();
		}

		return _dao;
	}

	public static String getCreationTableSql() {
		String result = null;

		result = "create table " + TABLE + "(" + DOMAIN_ID
				+ " VARCHAR(255) NOT NULL, " + VALUATION_ID + " VARCHAR(255) NOT NULL, PRIMARY KEY(" + DOMAIN_ID + "));";

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
	
	public void createDomainValuations(Problem problem) {

		try {
			Connection c = getConnection();
			Statement st = c.createStatement();
			Map<String, String> domainValuations = problem.getDomainValuations();
			for(String domainId: domainValuations.keySet()) {
				st.executeUpdate("insert into " + TABLE + " values ('" + domainId + "','" + domainValuations.get(domainId) + "')");
			}
			st.close();
		} catch (Exception e) {
		}
	}

	public void removeDomainValuations(Problem problem) {
		try {
			Connection c = getConnection();
			Map<String, String> domainValuations = problem.getDomainValuations();
			PreparedStatement pst = null;
			for(String domainId: domainValuations.keySet()) {
				pst = c.prepareStatement("delete from " + TABLE + " where " + DOMAIN_ID + " = ?");
				pst.setString(1, domainId);
				pst.executeUpdate();
			}
			pst.close();
		} catch (Exception e) {
		}
	}
}
