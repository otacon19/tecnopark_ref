package flintstones.gathering.cloud.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import flintstones.gathering.cloud.mail.ProblemMailGenerator;
import flintstones.gathering.cloud.model.Key;
import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.model.ProblemAssignment;
import flintstones.gathering.cloud.model.User;
import flintstones.gathering.cloud.model.Valuations;
import mcdacw.valuation.domain.Domain;
import sinbad2.database.Database;
import sinbad2.database.DatabaseManager;

public class DAOProblem {

	public static final String TABLE = "problem";
	public static final String ID = "id";
	public static final String ADMIN = "admin";

	private static DAOProblem _dao = null;

	private DAOProblem() {
	}

	public static DAOProblem getDAO() {

		if (_dao == null) {
			_dao = new DAOProblem();
		}

		return _dao;
	}

	public static String getCreationTableSql() {
		String result = null;

		result = "create table " + TABLE + "(" + ID
				+ " VARCHAR(50) NOT NULL, " + ADMIN + " VARCHAR(255) NOT NULL, PRIMARY KEY(" + ID + "));";

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
	
	public void createProblem(Problem problem) {

		try {
			Connection c = getConnection();
			Statement st = c.createStatement();
			st.executeUpdate("insert into " + TABLE + " values ('" + problem.getId() + "','" + problem.getAdmin().getMail() + "')");
			st.close();
			DAOProblemCriteria.getDAO().createProblemCriteria(problem);
			DAOProblemAlternatives.getDAO().createProblemAlternatives(problem);
			DAOProblemExperts.getDAO().createProblemExperts(problem);
			DAOProblemAssignments.getDAO().createProblemAssignments(problem);
			DAOProblemDomains.getDAO().createProblemDomains(problem);
			DAOProblemDomainAssignments.getDAO().createProblemDomainAssignments(problem);
			DAOProblemValuations.getDAO().createDomainValuations(problem);
		} catch (Exception e) {
		}
	}

	public void removeProblem(String id) {

		try {
			Connection c = getConnection();
			PreparedStatement pst = c.prepareStatement("delete from " + TABLE
					+ " where " + ID + " = ?");
			pst.setString(1, id);
			pst.executeUpdate();
			pst.close();
			DAOProblemCriteria.getDAO().removeProblemCriteria(id);
			DAOProblemAlternatives.getDAO().removeProblemAlternatives(id);
			DAOProblemExperts.getDAO().removeProblemExperts(id);
			DAOProblemAssignments.getDAO().removeProblemAssignments(id);
			DAOProblemDomains.getDAO().removeProblemDomains(id);
			DAOProblemDomainAssignments.getDAO().removeProblemDomainAssignments(id);
			DAOProblemValuations.getDAO().removeDomainValuations(problem);
		} catch (Exception e) {
		}
	}

	public void removeProblem(Problem problem) {
		removeProblem(problem.getId());
	}

	private List<Problem> getProblems(ResultSet rs) {
		List<Problem> result = new LinkedList<Problem>();
		try {
			DAOUser daoUser = DAOUser.getDAO();
			DAOProblemCriteria daoProblemCriteria = DAOProblemCriteria.getDAO();
			DAOProblemAlternatives daoProblemAlternatives = DAOProblemAlternatives.getDAO();
			DAOProblemExperts daoProblemExperts = DAOProblemExperts.getDAO();
			DAOProblemAssignments daoProblemAssignments = DAOProblemAssignments.getDAO();
			DAOProblemDomains daoProblemDomains = DAOProblemDomains.getDAO();
			DAOProblemDomainAssignments daoProblemDomainAssignments = DAOProblemDomainAssignments.getDAO();
			String problemId;
			String problemAdmin;
			User problemUser;
			List<String> problemCriteria;
			List<String> problemAlternatives;
			List<String> problemExperts;
			Map<String, ProblemAssignment> problemAssignments;
			Map<String, Domain> domains;
			Map<Key, String> domainAssignments;
			while (rs.next()) {
				problemId = rs.getString(ID);
				problemAdmin = rs.getString(ADMIN);
				problemUser = daoUser.getUser(problemAdmin);
				problemCriteria = daoProblemCriteria.getProblemCriteria(problemId);
				problemAlternatives = daoProblemAlternatives.getProblemAlternatives(problemId);
				problemExperts = daoProblemExperts.getProblemExperts(problemId);
				problemAssignments = daoProblemAssignments.getProblemAssignments(problemId, problemExperts);
				domainAssignments = daoProblemDomainAssignments.getProblemDomainAssignments(problemId);
				domains = daoProblemDomains.getProblemDomains(problemId);
				result.add(new Problem(problemId, problemUser, problemCriteria, problemAlternatives, problemExperts, domains, domainAssignments, problemAssignments));
			}
		} catch (Exception e) {
			
		}
		return result;
	}
	
	public Problem getProblem(String id) {
		Problem result = null;

		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String select = "select * from " + TABLE + " where " + ID + "='"
					+ id + "';";
			List<Problem> problems = getProblems(st.executeQuery(select));
			if (!problems.isEmpty()) {
				result = problems.get(0);
			}
			st.close();
		} catch (Exception e) {
		}

		return result;
	}
	
	public List<Problem> getProblems(User admin) {
		List<Problem> result = new LinkedList<Problem>();

		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String select = "select * from " + TABLE + " where " + ADMIN + "='"
					+ admin.getMail() + "';";
			result = getProblems(st.executeQuery(select));
			st.close();
		} catch (Exception e) {
		}
		
		return result;
	}
	
	public List<Problem> getAllProblems() {

		List<Problem> result = new LinkedList<Problem>();

		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String select = "select * from " + TABLE + ";";
			result = getProblems(st.executeQuery(select));
		} catch (Exception e) {
		}
		
		return result;
	}
	
	public void setAssignment(Problem problem, ProblemAssignment assignment) {
		problem.setAssignment(assignment);
		DAOProblemAssignments.getDAO().setAssignment(problem, assignment);
	}
	
	public void setAssignment(Problem problem, String id, User user) {
		setAssignment(problem, new ProblemAssignment(id, user));
	}
	
	public void setAssignment(Problem problem, String id, User user, Valuations valuations) {
		setAssignment(problem, new ProblemAssignment(id, user, valuations));
	}
	
	public void allExpertsHaveSentTheirAssessments(Problem problem) {
		ProblemMailGenerator.buildAndSendAllAssignmentsProblemMail(problem);
	}
}
