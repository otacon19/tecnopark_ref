package sinbad2.database.connector;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import sinbad2.database.DatabaseHelper;

import java.sql.Connection;

public abstract class DatabaseConnector {

	protected Map<String, String> _parameters;
	protected boolean _validParameters;
	protected Connection _connection;
	protected Statement _currentStatement;
	protected DatabaseHelper _databaseHelper;

	public DatabaseConnector() {
		_parameters = null;
		_currentStatement = null;
	}

	public void connection(Map<String, String> parameters) throws SQLException {
		setParameters(parameters);
		createConnection();
	}

	private void setParameters(Map<String, String> parameters) throws SQLException {
		if (validParameters(parameters)) {
			_parameters = parameters;
			extractParameters();
		} else {
			throw new SQLException("Invalid database parameters"); //$NON-NLS-1$
		}
	}

	private boolean validParameters(Map<String, String> parameters) {
		if (parameters != null) {
			for (String parameter : getParametersKeys()) {
				if (!parameters.containsKey(parameter)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public abstract void extractParameters();
	
	public void setDatabaseHelper(DatabaseHelper databaseHelper) {
		_databaseHelper = databaseHelper;
	}
	
	public DatabaseHelper getDatabaseHelper() {
		return _databaseHelper;
	}
	
	public Connection getConnection() {
		return _connection;
	}

	public Map<String, String> getParameters() {
		return _parameters;
	}

	public abstract String[] getParametersKeys();

	public abstract void createConnection() throws SQLException;

	public void close() {
		try {
			closeConnection();
		} catch (SQLException e) {
			System.err.println("Database close connection fail"); //$NON-NLS-1$
		}
	}

	public abstract void closeConnection() throws SQLException;


}
