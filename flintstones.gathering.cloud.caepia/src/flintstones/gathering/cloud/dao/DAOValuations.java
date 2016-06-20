package flintstones.gathering.cloud.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import flintstones.gathering.cloud.model.Key;
import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.model.ProblemAssignment;
import flintstones.gathering.cloud.model.Valuation;
import flintstones.gathering.cloud.model.Valuations;
import sinbad2.database.Database;
import sinbad2.database.DatabaseManager;

public class DAOValuations {

	public static final String TABLE = "valuations";
	public static final String PROBLEM = "problem";
	public static final String CRITERION = "criterion";
	public static final String ALTERNATIVE = "alternative";
	public static final String EXPERT = "expert";
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
							valuation = assignment.getValuations()
									.getValuation(key);
							if (valuation != null) {
								value = valuation.getValue();
							}
						}
						st.executeUpdate("insert into " + TABLE + " values ('"
								+ problemId + "','"
								+ criterion.replace("'", "''") + "','"
								+ alternative.replace("'", "''") + "','" + expert + "','" + value
								+ "')");
					}
				}
			}
			st.close();
		} catch (Exception e) {

		}
	}

	public void removeProblemValuations(String problem) {

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

	public void removeProblemValuations(Problem problem) {
		removeProblemValuations(problem.getId());
	}

	public Valuations getValuations(String problem, ProblemAssignment assignment) {
		Valuations result = new Valuations();
		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String select = "select * from " + TABLE + " where " + PROBLEM
					+ " = '" + problem + "' and " + EXPERT + " = '"
					+ assignment.getId() + "';";
			ResultSet rs = st.executeQuery(select);

			String criterion;
			String alternative;
			Key key;
			while (rs.next()) {
				criterion = rs.getString(CRITERION);
				alternative = rs.getString(ALTERNATIVE);
				key = new Key(alternative, criterion);
				result.setValuation(key, new Valuation(rs.getString(VALUE)));
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
				value = valuations.getValuation(key).getValue();

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
			value = valuation.getValue();

			String update = "update " + TABLE + " set " + VALUE + " = '"
					+ value + "' where " + PROBLEM + " = '" + problem.getId()
					+ "' and " + CRITERION + " = '" + criterion + "' and "
					+ ALTERNATIVE + " = '" + alternative + "' and " + EXPERT
					+ " = '" + assignment.getId() + "';";

			st.executeUpdate(update);

			st.close();

		} catch (Exception e) {

		}
	}
}
