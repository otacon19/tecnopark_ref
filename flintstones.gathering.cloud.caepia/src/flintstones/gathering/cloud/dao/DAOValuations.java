package flintstones.gathering.cloud.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import flintstones.gathering.cloud.model.Key;
import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.model.ProblemAssignment;
import flintstones.gathering.cloud.model.Valuations;
import mcdacw.valuation.domain.Domain;
import mcdacw.valuation.domain.fuzzyset.FuzzySet;
import mcdacw.valuation.domain.numeric.NumericIntegerDomain;
import mcdacw.valuation.domain.numeric.NumericRealDomain;
import mcdacw.valuation.valuation.IntegerIntervalValuation;
import mcdacw.valuation.valuation.IntegerValuation;
import mcdacw.valuation.valuation.LinguisticValuation;
import mcdacw.valuation.valuation.RealIntervalValuation;
import mcdacw.valuation.valuation.RealValuation;
import mcdacw.valuation.valuation.Valuation;
import mcdacw.valuation.valuation.hesitant.EUnaryRelationType;
import mcdacw.valuation.valuation.hesitant.HesitantValuation;
import sinbad2.database.Database;
import sinbad2.database.DatabaseManager;

public class DAOValuations {

	public static final String TABLE = "valuations";
	public static final String PROBLEM = "problem";
	public static final String CRITERION = "criterion";
	public static final String ALTERNATIVE = "alternative";
	public static final String EXPERT = "expert";
	public static final String DOMAIN = "domain";
	public static final String TYPE = "type";
	public static final String VALUE = "value";

	private static DAOValuations _dao = null;

	private DAOValuations() {
	}

	public static DAOValuations getDAO() {

		if (_dao == null) {
			_dao = new DAOValuations();
		}

		return _dao;
	}

	public static String getCreationTableSql() {
		String result = null;

		result = "create table " + TABLE + "(" + PROBLEM
				+ " VARCHAR(50) NOT NULL, " + CRITERION
				+ " TEXT NOT NULL, " + ALTERNATIVE
				+ " TEXT NOT NULL, " + EXPERT
				+ " VARCHAR (50) NOT NULL, " + DOMAIN
				+ " VARCHAR(255) NOT NULL, " + TYPE
				+ " VARCHAR(255) NOT NULL, " + VALUE
				+ " VARCHAR(255) NOT NULL, PRIMARY KEY(" + PROBLEM + ","
				+ CRITERION + "(255)," + ALTERNATIVE + "(255)," + EXPERT + "));";

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

	public void createProblemValuations(Problem problem) {

		try {
			Connection c = getConnection();
			Statement st = c.createStatement();
			String problemId = problem.getId();
			ProblemAssignment assignment;
			Valuation valuation = null;
			String value = "";
			Key key;
			for (String criterion : problem.getCriteria()) {
				for (String alternative : problem.getAlternatives()) {
					key = new Key(alternative, criterion);
					for (String expert : problem.getExperts()) {
						assignment = problem.getAssignment(expert);
						if (assignment != null) {
							valuation = assignment.getValuations().getValuation(key);
							if (valuation != null) {
								value = valuation.changeFormatValuationToString();
								st.executeUpdate("insert into " + TABLE + " values ('"
										+ problemId + "','"
										+ criterion.replace("'", "''") + "','"
										+ alternative.replace("'", "''") + "','" + expert + "','" + valuation.getDomain().getId() + "'," + valuation.getClass().toString() + "','" + value
										+ "')");
							}
						}
					}
				}
			}
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeProblemValuations(String problem) {

		try {
			Connection c = getConnection();
			PreparedStatement pst = c.prepareStatement("delete from " + TABLE + " where " + PROBLEM + " = ?");
			pst.setString(1, problem);
			pst.executeUpdate();
			pst.close();
		} catch (Exception e) {
		}
	}

	public void removeProblemValuations(Problem problem) {
		removeProblemValuations(problem.getId());
	}
	
	public void removeValuation(String problem, Key key) {

		try {
			Connection c = getConnection();
			PreparedStatement pst = c.prepareStatement("delete from " + TABLE + " where " + PROBLEM + " = ?" + " and " + ALTERNATIVE + " = ?" + " and " + CRITERION
					+ " = ?");
			pst.setString(1, problem);
			pst.setString(2, key.getAlternative());
			pst.setString(3, key.getCriterion());
			pst.executeUpdate();
			pst.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Valuations getValuations(String problem, ProblemAssignment assignment, Map<String, Domain> domains) {
		Valuations result = new Valuations();
		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String select = "select * from " + TABLE + " where " + PROBLEM + " = '" + problem + "' and " + EXPERT + " = '" + assignment.getId() + "';";
			ResultSet rs = st.executeQuery(select);

			String criterion;
			String alternative;
			String type;
			String domainId;
			Key key;
			while (rs.next()) {
				criterion = rs.getString(CRITERION);
				alternative = rs.getString(ALTERNATIVE);
				key = new Key(alternative, criterion);
				domainId = rs.getString(DOMAIN);
				type = rs.getString(TYPE);
				Valuation valuation = null;
				
				if(type.equals(IntegerValuation.class.toString())) {
					
					valuation = new IntegerValuation((NumericIntegerDomain) domains.get(domainId), Double.parseDouble(rs.getString(VALUE)));
				
				} else if(type.equals(IntegerIntervalValuation.class.toString())) {
					
					String range = rs.getString(VALUE);
					range = range.replace("[", "").replace("]", "").replace(" ", "");
					String[] minMax = range.split(",");
					valuation = new IntegerIntervalValuation((NumericIntegerDomain) domains.get(domainId), 
							Long.parseLong(minMax[0]), Long.parseLong(minMax[1]));
					
				} else if(type.equals(RealValuation.class.toString())) {
					
					valuation = new RealValuation((NumericRealDomain) domains.get(domainId), Double.parseDouble(rs.getString(VALUE)));
				
				} else if(type.equals(RealIntervalValuation.class.toString())) {
					
					String range = rs.getString(VALUE);
					range = range.replace("[", "").replace("]", "").replace(" ", "");
					String[] minMax = range.split(",");
					valuation = new RealIntervalValuation((NumericRealDomain) domains.get(domainId), 
							Double.parseDouble(minMax[0]), Double.parseDouble(minMax[1]));
					
				} else if(type.equals(LinguisticValuation.class.toString())) {
					
					valuation = new LinguisticValuation();
					valuation.setDomain((FuzzySet) domains.get(domainId));
					((LinguisticValuation) valuation).setLabel(rs.getString(VALUE));
					
				} else if(type.equals(HesitantValuation.class.toString())) {
					
					String hesitant = rs.getString(VALUE);
					FuzzySet fuzzySet =(FuzzySet) domains.get(domainId);
					if(hesitant.contains("Between")) {
			
						hesitant = hesitant.replace("Between ", "");
						String lowerTerm = hesitant.substring(0, hesitant.indexOf(" "));
						String upperTerm = hesitant.substring(hesitant.lastIndexOf(" ") + 1, hesitant.length());

						valuation = new HesitantValuation(fuzzySet);
						((HesitantValuation) valuation).setBinaryRelation(lowerTerm, upperTerm);
					
					} else if(hesitant.contains("At least") || hesitant.contains("At most") || hesitant.contains("Lower than") ||
							hesitant.contains("Greater than")){
						
						valuation = new HesitantValuation((FuzzySet) domains.get(domainId));
						valuation.setDomain(fuzzySet);
				
						EUnaryRelationType unary;
						if(hesitant.contains("At least")) {
							unary = EUnaryRelationType.AtLeast;
						} else if(hesitant.contains("At most")) {
							unary = EUnaryRelationType.AtMost;
						} else if(hesitant.contains("Lower than")) {
							unary = EUnaryRelationType.LowerThan;
						} else {
							unary = EUnaryRelationType.GreaterThan;
						}

						String term = hesitant.substring(hesitant.lastIndexOf(" ") + 1, hesitant.length());
						((HesitantValuation) valuation).setUnaryRelation(unary, fuzzySet.getLabelSet().getLabel(term));
					
					} else {
						valuation = new HesitantValuation((FuzzySet) domains.get(domainId));
						valuation.setDomain(fuzzySet);
						((HesitantValuation) valuation).setLabel(hesitant);
					}
					
				}
				result.setValuation(key, valuation);
			}
			st.close();
		} catch (Exception e) {

		}

		return result;

	}

	public void setValuations(Problem problem, ProblemAssignment assignment) {

		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			Valuations valuations = assignment.getValuations();
			String value;

			for (Key key : valuations.getValuations().keySet()) {
				String criterion = key.getCriterion().replace("'", "''");
				String alternative = key.getAlternative().replace("'", "''");
				value = valuations.getValuation(key).changeFormatValuationToString();

				String update = "update " + TABLE + " set " + VALUE + " = '"
						+ value + "' where " + PROBLEM + " = '"
						+ problem.getId() + "' and " + CRITERION + " = '"
						+ criterion + "' and " + ALTERNATIVE + " = '"
						+ alternative + "' and " + EXPERT + " = '"
						+ assignment.getId() + "';";

				st.executeUpdate(update);
			}

			st.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setValuation(Problem problem, ProblemAssignment assignment,
			Key key, Valuation valuation) {

		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String value;

			String criterion = key.getCriterion().replace("'", "''");
			String alternative = key.getAlternative().replace("'", "''");
			value = valuation.changeFormatValuationToString();
			
			String update = "update " + TABLE + " set " + VALUE + " = '"
					+ value + "' where " + PROBLEM + " = '" + problem.getId()
					+ "' and " + CRITERION + " = '" + criterion + "' and "
					+ ALTERNATIVE + " = '" + alternative + "' and " + EXPERT
					+ " = '" + assignment.getId() + "';";

			st.executeUpdate(update);

			st.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void insertValuation(Problem problem, ProblemAssignment assignment, Key key, Valuation valuation) {

		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String value;

			String criterion = key.getCriterion();
			String alternative = key.getAlternative();
			value = valuation.changeFormatValuationToString();
			
			String statement = "replace into " + TABLE + " values("
					+ "'" + problem.getId() + "'" + ", "
					+ "'" + criterion + "'" + ", "
					+ "'" + alternative + "'" + ", " 
					+ "'" + assignment.getId() + "'" + ", "
					+ "'" + valuation.getDomain().getId() + "'" + ", "
					+ "'" + valuation.getClass().toString() + "'" + ", " 
					+ "'" + value
					+ "');";
			
			st.executeUpdate(statement);

			st.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
