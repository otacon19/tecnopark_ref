package sinbad2.database.connector.mysql;

import java.sql.DriverManager;
import java.sql.SQLException;

import sinbad2.database.connector.DatabaseConnector;

public class MySQLDatabaseConnector extends DatabaseConnector {

	public static final String PLUGIN_ID = "sinbad2.database.mysql"; //$NON-NLS-1$

	public static String[] PARAMETERS = { "url", "dbname", "user", "password" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	private String _url;
	private String _dbName;
	private String _userName;
	private String _password;

	public MySQLDatabaseConnector() {
		_connection = null;
		_parameters = null;
	}

	@Override
	public void extractParameters() {
		_url = _parameters.get("url"); //$NON-NLS-1$
		_dbName = _parameters.get("dbname"); //$NON-NLS-1$
		_userName = _parameters.get("user"); //$NON-NLS-1$
		_password = _parameters.get("password"); //$NON-NLS-1$
	}

	@Override
	public void createConnection() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			_connection = DriverManager.getConnection("jdbc:mysql://" + _url + "/" + _dbName, _userName, _password); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (ClassNotFoundException e) {
			throw new SQLException("com.mysql.jdbc.Driver not found"); //$NON-NLS-1$
		}
	}

	@Override
	public void closeConnection() throws SQLException {
		_connection.close();
	}

	@Override
	public String[] getParametersKeys() {
		return PARAMETERS;
	}

}
