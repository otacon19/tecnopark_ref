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

	public static final String TABLE = "assignments"; //$NON-NLS-1$
	public static final String PROBLEM = "problem"; //$NON-NLS-1$
	public static final String ID = "id"; //$NON-NLS-1$
	public static final String USER = "user"; //$NON-NLS-1$
	public static final String MAKE = "make"; //$NON-NLS-1$

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

		result = "create table " + TABLE + "(" + PROBLEM //$NON-NLS-1$ //$NON-NLS-2$
				+ " VARCHAR(50) NOT NULL, " + ID + " VARCHAR(50) NOT NULL, " //$NON-NLS-1$ //$NON-NLS-2$
				+ USER + " VARCHAR(255), " + MAKE + " VARCHAR(5), PRIMARY KEY(" //$NON-NLS-1$ //$NON-NLS-2$
				+ PROBLEM + "," + ID + "));"; //$NON-NLS-1$ //$NON-NLS-2$

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
					st.executeUpdate("insert into " + TABLE + " values ('" + id + "','" + problemUserId + "','" + userId + "','" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
							+ Boolean.toString(make) + "')"); //$NON-NLS-1$
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
			PreparedStatement pst = c.prepareStatement("delete from " + TABLE //$NON-NLS-1$
					+ " where " + PROBLEM + " = ?"); //$NON-NLS-1$ //$NON-NLS-2$
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

			String select = "select * from " + TABLE + " where " + PROBLEM //$NON-NLS-1$ //$NON-NLS-2$
					+ "='" + problem + "';"; //$NON-NLS-1$ //$NON-NLS-2$
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

			String select = "select * from " + TABLE + " where " + USER + "='" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ user.getMail() + "';"; //$NON-NLS-1$
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
			String uId = (user != null) ? user.getMail() : ""; //$NON-NLS-1$
			String update = "update " + TABLE + " set " + USER + " = '" + uId //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ "' where " + PROBLEM + " = '" + problem.getId() //$NON-NLS-1$ //$NON-NLS-2$
					+ "' and " + ID + " = '" + assignment.getId() + "';"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
			String update = "update " + TABLE + " set " + MAKE + " = '" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ Boolean.toString(make) + "' where " + PROBLEM + " = '" //$NON-NLS-1$ //$NON-NLS-2$
					+ problem.getId() + "' and " + ID + " = '" //$NON-NLS-1$ //$NON-NLS-2$
					+ assignment.getId() + "' and " + USER + " = '" //$NON-NLS-1$ //$NON-NLS-2$
					+ assignment.getUser().getMail() + "';"; //$NON-NLS-1$
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
	
	public boolean isAvailableToExport(Problem problem) {

		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String select = "select * from " + TABLE + " where " + PROBLEM + "='" + problem + "';"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			ResultSet rs = st.executeQuery(select);
						
			while (rs.next()) {
				boolean check = Boolean.parseBoolean(rs.getString(MAKE));
				if(!check) {
					return false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}
}
