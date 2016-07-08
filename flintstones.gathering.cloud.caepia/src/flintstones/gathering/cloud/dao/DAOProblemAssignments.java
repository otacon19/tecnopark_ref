package flintstones.gathering.cloud.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.model.ProblemAssignment;
import flintstones.gathering.cloud.model.User;
import mcdacw.valuation.domain.Domain;
import sinbad2.database.Database;
import sinbad2.database.DatabaseManager;

public class DAOProblemAssignments {

	public static final String TABLE = "assignments";
	public static final String PROBLEM = "problem";
	public static final String ID = "id";
	public static final String USER = "user";
	public static final String MAKE = "make";

	private static DAOProblemAssignments _dao = null;

	private DAOProblemAssignments() {
	}

	public static DAOProblemAssignments getDAO() {

		if (_dao == null) {
			_dao = new DAOProblemAssignments();
		}

		return _dao;
	}

	public static String getCreationTableSql() {
		String result = null;

		result = "create table " + TABLE + "(" + PROBLEM
				+ " VARCHAR(50) NOT NULL, " + ID + " VARCHAR(50) NOT NULL, "
				+ USER + " VARCHAR(255), " + MAKE + " VARCHAR(5), PRIMARY KEY("
				+ PROBLEM + "," + ID + "));";

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

	public void createProblemAssignments(Problem problem) {

		try {
			Connection c = getConnection();
			Statement st = c.createStatement();
			String id = problem.getId();
			Map<String, ProblemAssignment> assignments = problem.getAssignments();
			User user = null;
			String userId;
			ProblemAssignment assignment = null;
			boolean make = false;
			for (String problemUserId : assignments.keySet()) {
				assignment = assignments.get(problemUserId);
				if (assignment != null) {
					user = assignment.getUser();
					userId = user.getMail();
					make = assignment.getMake();
					st.executeUpdate("insert into " + TABLE + " values ('" + id + "','" + problemUserId + "','" + userId + "','"
							+ Boolean.toString(make) + "')");
				}
				DAOValuations.getDAO().createProblemValuations(problem);
			}
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeProblemAssignments(String problem) {

		try {
			Connection c = getConnection();
			PreparedStatement pst = c.prepareStatement("delete from " + TABLE
					+ " where " + PROBLEM + " = ?");
			pst.setString(1, problem);
			pst.executeUpdate();
			pst.close();
			DAOValuations.getDAO().removeProblemValuations(problem);
		} catch (Exception e) {
		}
	}

	public void removeProblemAssignments(Problem problem) {
		removeProblemAssignments(problem.getId());
	}

	public Map<String, ProblemAssignment> getProblemAssignments(String problem, List<String> experts, Map<String, Domain> domains) {
		Map<String, ProblemAssignment> result = new HashMap<String, ProblemAssignment>();

		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String select = "select * from " + TABLE + " where " + PROBLEM
					+ "='" + problem + "';";
			ResultSet rs = st.executeQuery(select);
			DAOUser daoUser = DAOUser.getDAO();
			DAOValuations daoValuations = DAOValuations.getDAO();
			String uId;
			ProblemAssignment assignment;
			while (rs.next()) {
				uId = rs.getString(ID);
				assignment = new ProblemAssignment(uId, daoUser.getUser(rs.getString(USER)));
				assignment.setValuations(daoValuations.getValuations(problem, assignment, domains));
				assignment.setMake(Boolean.parseBoolean(rs.getString(MAKE)));
				result.put(uId, assignment);
			}
			for (String expert : experts) {
				if (!result.containsKey(expert)) {
					result.put(expert, null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public Map<Problem, ProblemAssignment> getUserProblemAssignments(User user) {
		Map<Problem, ProblemAssignment> result = new HashMap<Problem, ProblemAssignment>();

		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String select = "select * from " + TABLE + " where " + USER + "='"
					+ user.getMail() + "';";
			ResultSet rs = st.executeQuery(select);

			DAOProblem daoProblem = DAOProblem.getDAO();
			Problem problem;
			ProblemAssignment problemAssignment;
			while (rs.next()) {
				problem = daoProblem.getProblem(rs.getString(PROBLEM));
				problemAssignment = problem.getAssignment(rs.getString(ID));
				result.put(problem, problemAssignment);
			}
		} catch (Exception e) {

		}

		return result;
	}

	public void setAssignment(Problem problem, ProblemAssignment assignment) {
		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			User user = assignment.getUser();
			String uId = (user != null) ? user.getMail() : "";
			String update = "update " + TABLE + " set " + USER + " = '" + uId
					+ "' where " + PROBLEM + " = '" + problem.getId()
					+ "' and " + ID + " = '" + assignment.getId() + "';";
			st.executeUpdate(update);
			st.close();
			DAOValuations.getDAO().setValuations(problem, assignment);

		} catch (Exception e) {

		}
	}

	public void makeAssignment(Problem problem, ProblemAssignment assignment) {

		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			boolean make = assignment.getMake();
			String update = "update " + TABLE + " set " + MAKE + " = '"
					+ Boolean.toString(make) + "' where " + PROBLEM + " = '"
					+ problem.getId() + "' and " + ID + " = '"
					+ assignment.getId() + "' and " + USER + " = '"
					+ assignment.getUser().getMail() + "';";
			st.executeUpdate(update);
			st.close();
			
			DAOProblem daoProblem = DAOProblem.getDAO();
			Problem p = daoProblem.getProblem(problem.getId());
			boolean complete = true;
			for (String aux : p.getAssignments().keySet()) {
				if (!p.getAssignment(aux).getMake()) {
					complete = false;
				}
			}
			
			if (complete) {
				daoProblem.allExpertsHaveSentTheirAssessments(p);
			}

		} catch (Exception e) {

		}
	}

}
