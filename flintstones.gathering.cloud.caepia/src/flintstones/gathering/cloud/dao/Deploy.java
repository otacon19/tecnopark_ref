package flintstones.gathering.cloud.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import flintstones.gathering.cloud.APP;
import flintstones.gathering.cloud.model.User;
import sinbad2.database.Database;
import sinbad2.database.DatabaseManager;

public class Deploy {

	private Connection c;
	
	private Deploy() throws Exception {
		
		Database db = DatabaseManager.getInstance().getApplicationDatabase();
		
		c = db.getConnection();
	}

	public static void make() {
				
		try {
			Deploy d = new Deploy();
			Statement s = d.c.createStatement();
			d.removeTables(s);
			d.createTables(s);
			
			d.createAdminAccount();
			s.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void removeTables(Statement s) throws SQLException {
		s.executeUpdate("drop table if exists " + DAOUser.TABLE + ";");
		s.executeUpdate("drop table if exists " + DAOProblem.TABLE + ";");
		s.executeUpdate("drop table if exists " + DAOProblemCriteria.TABLE + ";");
		s.executeUpdate("drop table if exists " + DAOProblemAlternatives.TABLE + ";");
		s.executeUpdate("drop table if exists " + DAOProblemExperts.TABLE + ";");
		s.executeUpdate("drop table if exists " + DAOProblemAssignments.TABLE + ";");
		s.executeUpdate("drop table if exists " + DAOProblemDomains.TABLE + ";");
		s.executeUpdate("drop table if exists " + DAOProblemDomainAssignments.TABLE + ";");
		s.executeUpdate("drop table if exists " + DAOValuations.TABLE + ";");
		s.executeUpdate("drop table if exists " + DAOProblemValuations.TABLE + ";");
	}
	
	private void createTables(Statement s) throws SQLException {
		s.executeUpdate(DAOUser.getCreationTableSql());
		s.executeUpdate(DAOProblem.getCreationTableSql());
		s.executeUpdate(DAOProblemCriteria.getCreationTableSql());
		s.executeUpdate(DAOProblemAlternatives.getCreationTableSql());
		s.executeUpdate(DAOProblemExperts.getCreationTableSql());
		s.executeUpdate(DAOProblemAssignments.getCreationTableSql());
		s.executeUpdate(DAOProblemDomains.getCreationTableSql());
		s.executeUpdate(DAOProblemDomainAssignments.getCreationTableSql());
		s.executeUpdate(DAOValuations.getCreationTableSql());
		s.executeUpdate(DAOProblemValuations.getCreationTableSql());
	}
	
	private void createAdminAccount() {
		User user = new User(APP.ADMIN_MAIL, APP.ADMIN_PASS, true, true);
		DAOUser.getDAO().createUser(user);
	}
 }
