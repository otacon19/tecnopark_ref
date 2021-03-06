package flintstones.gathering.cloud.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import flintstones.gathering.cloud.model.Problem;
import sinbad2.database.Database;
import sinbad2.database.DatabaseManager;

public class DAOProblemValuations {
	
	public static final String TABLE = "problemvaluations"; //$NON-NLS-1$
	public static final String PROBLEM_ID = "problem_id"; //$NON-NLS-1$
	public static final String DOMAIN_ID = "domain_id"; //$NON-NLS-1$
	public static final String VALUATION_ID = "valuation_id"; //$NON-NLS-1$
	
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

		result = "create table " + TABLE + "(" + PROBLEM_ID + " VARCHAR(100) NOT NULL, " + DOMAIN_ID //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ " VARCHAR(255) NOT NULL, " + VALUATION_ID + " VARCHAR(255) NOT NULL, PRIMARY KEY(" + PROBLEM_ID + "," + DOMAIN_ID  + "));"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

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
				st.executeUpdate("insert into " + TABLE + " values ('" + problem.getId() + "','" + domainId + "','" + domainValuations.get(domainId) + "')"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			}
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeDomainValuations(String id) {
		try {
			Connection c = getConnection();
			PreparedStatement pst = null;
			pst = c.prepareStatement("delete from " + TABLE + " where " + PROBLEM_ID + " = ?"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			pst.setString(1, id);
			pst.executeUpdate();
			pst.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Map<String, String> getDomainValuations(String problem) {
		Map<String, String> result = new HashMap<String, String>();
		
		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String select = "select * from " + TABLE + " where " + PROBLEM_ID + "='" + problem + "';"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			ResultSet rs = st.executeQuery(select);
			while (rs.next()) {
				String domainID = rs.getString(DOMAIN_ID);
				String valuationID = rs.getString(VALUATION_ID);
				result.put(domainID, valuationID);
			}
		} catch (Exception e) {
			
		}
		
		return result;
	}
}
