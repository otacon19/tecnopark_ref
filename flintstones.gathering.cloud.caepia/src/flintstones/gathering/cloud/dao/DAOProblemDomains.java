package flintstones.gathering.cloud.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import mcdacw.valuation.domain.IDomain;
import sinbad2.database.Database;
import sinbad2.database.DatabaseManager;
import flintstones.gathering.cloud.model.Domain;
import flintstones.gathering.cloud.model.Problem;

public class DAOProblemDomains {

	public static final String TABLE = "domains";
	public static final String ID = "id";
	public static final String PROBLEM = "problem";
	public static final String TYPE = "type";
	public static final String DOMAIN = "domain";

	private static DAOProblemDomains _dao = null;

	private DAOProblemDomains() {
	}

	public static DAOProblemDomains getDAO() {

		if (_dao == null) {
			_dao = new DAOProblemDomains();
		}

		return _dao;
	}

	public static String getCreationTableSql() {
		String result = null;

		result = "create table " + TABLE + "(" + ID
				+ " VARCHAR(50) NOT NULL, " + PROBLEM
				+ " VARCHAR(50) NOT NULL, " + TYPE + " VARCHAR(255) NOT NULL, " + DOMAIN + " TEXT NOT NULL, PRIMARY KEY(" + ID + ","
				+ PROBLEM + "));";

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

	public void createProblemDomains(Problem problem) {
		try {
			Connection c = getConnection();
			Statement st = c.createStatement();
			String problemId = problem.getId();
			Map<String, Domain> domains = problem.getDomains();
			Domain domain = null;
			for (String id : domains.keySet()) {
				domain = domains.get(id);
				st.executeUpdate("insert into " + TABLE + " values ('" + id + "','" + problemId + "','" + domain.getType() + "','" + domain.getDomain().toString() + "')");
			}
			st.close();
		} catch (Exception e) {
			
		}
	}
	
	public void removeProblemDomains(String problem) {
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

	public void removeProblemDomains(Problem problem) {
		removeProblemDomains(problem.getId());
	}
	
	public Map<String, Domain> getProblemDomains(String problem) {
		Map<String, Domain> result = new HashMap<String, Domain>();
		
		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String select = "select * from " + TABLE + " where " + PROBLEM
					+ "='" + problem + "';";
			ResultSet rs = st.executeQuery(select);
			Domain domain = null;
			String id;
			String type;
			IDomain d;
			while (rs.next()) {
				id = rs.getString(ID);
				type = rs.getString(TYPE);
				d = Domain.buildDomain(type, rs.getString(DOMAIN));
				domain = new Domain(id, type, d);
				result.put(id, domain);
			}
		} catch (Exception e) {
			
		}
		
		return result;
	}
	
	public Map<String, Domain> getProblemDomains(Problem problem) {
		return getProblemDomains(problem.getId());
	}

}
