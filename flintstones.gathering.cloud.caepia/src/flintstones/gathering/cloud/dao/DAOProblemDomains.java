package flintstones.gathering.cloud.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import sinbad2.database.Database;
import sinbad2.database.DatabaseManager;
import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.xml.XMLValues;
import mcdacw.valuation.domain.Domain;
import mcdacw.valuation.domain.numeric.NumericIntegerDomain;
import mcdacw.valuation.domain.numeric.NumericRealDomain;

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
				st.executeUpdate("insert into " + TABLE + " values ('" + id + "','" + problemId + "','" + domain.getType() + "','" + domain.toString() + "')");
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
	
	//TODO
	public Map<String, Domain> getProblemDomains(String problem) {
		Map<String, Domain> result = new HashMap<String, Domain>();
		
		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String select = "select * from " + TABLE + " where " + PROBLEM + "='" + problem + "';";
			ResultSet rs = st.executeQuery(select);
			Domain domain = null;
			String id;
			String type;
			String value;
			while (rs.next()) {
				
				id = rs.getString(ID);
				type = rs.getString(TYPE);
				value = rs.getString(DOMAIN);
	
				if(type.equals(XMLValues.NUMERIC_INTEGER_DOMAIN)) {
					
					try {
						String[] tokens = value.split(",");
						int min = Integer.parseInt(tokens[0].substring(1));
						int max = Integer.parseInt(tokens[1].substring(0, tokens[1].length() - 1));
						domain = new NumericIntegerDomain();
						((NumericIntegerDomain) domain).setMinMax(min, max);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				} else if(type.equals(XMLValues.NUMERIC_REAL_DOMAIN)) {
					
					try {
						String[] tokens = value.split(",");
						double min = Double.parseDouble(tokens[0].substring(1));
						double max = Double.parseDouble(tokens[1].substring(0, tokens[1].length() - 1));
						domain = new NumericRealDomain();
						((NumericRealDomain) domain).setMinMax(min, max);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				} else if(type.equals(XMLValues.FUZZY_SET)) {
					
					try {
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				} else if(type.equals(XMLValues.UNBALANCED)) {
					
				}
				
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
