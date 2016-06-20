package sinbad2.database;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import sinbad2.database.DatabaseHelper;
import sinbad2.database.connector.DatabaseConnector;

import java.sql.Connection;

public class Database {

	private String _id;
	private DatabaseConnector _databaseConnector;
	protected Statement _currentStatement;
	
	private Database() {
		_id = null;
		_databaseConnector = null;
		_currentStatement = null;
	}

	public Database(String id, DatabaseConnector databaseConnector) throws SQLException {
		this();
		setId(id);
		setDatabaseConnector(databaseConnector);
	}
	
	private void setId(String id) {
		_id = id;
	}
	
	public String getId() {
		return _id;
	}
	
	private void setDatabaseConnector(DatabaseConnector databaseConnector) {
		_databaseConnector = databaseConnector;
	}

	public void close() {
		_databaseConnector.close();
	}

	public Statement createStatement() throws SQLException {
		return getConnection().createStatement();
	}
	
	public Connection getConnection() {
		return _databaseConnector.getConnection();
	}
	
	private DatabaseHelper getDatabaseHelper() {
		return _databaseConnector.getDatabaseHelper();
	}

	public void startStatement() throws SQLException {
		if (_currentStatement != null) {
			closeStatement();
		}
		_currentStatement = createStatement();
	}

	public void closeStatement() throws SQLException {
		if (_currentStatement != null) {
			_currentStatement.close();
			_currentStatement = null;
		}
	}

	public void createTableStatement(String tableName, String[] columns, String[] properties, String[] keys)
			throws SQLException {
		getDatabaseHelper().createTableStatement(getConnection(), tableName, columns, properties, keys);
	}

	public void createTable(Statement statement, String tableName, String[] columns, String[] properties, String[] keys)
			throws SQLException {
		getDatabaseHelper().createTable(statement, tableName, columns, properties, keys);
	}

	public void createTable(String tableName, String[] columns, String[] properties, String[] keys) throws SQLException {
		if (_currentStatement == null) {
			throw new SQLException("Closed statement"); //$NON-NLS-1$
		}
		getDatabaseHelper().createTable(_currentStatement, tableName, columns, properties, keys);
	}

	public void dropTableStatement(String[] tableNames) throws SQLException {
		getDatabaseHelper().dropTableStatement(getConnection(), tableNames);
	}

	public void dropTable(Statement statement, String[] tableNames) throws SQLException {
		getDatabaseHelper().dropTable(statement, tableNames);
	}

	public void dropTable(String[] tableNames) throws SQLException {
		if (_currentStatement == null) {
			throw new SQLException("Closed statement"); //$NON-NLS-1$
		}
		getDatabaseHelper().dropTable(_currentStatement, tableNames);
	}

	public void insertIntoStatement(String tableName, String[] columns, String[] values, Boolean[] strings)
			throws SQLException {
		getDatabaseHelper().insertIntoStatement(getConnection(), tableName, columns, values, strings);
	}

	public void InsertInto(Statement statement, String tableName, String[] columns, String[] values, Boolean[] strings)
			throws SQLException {
		getDatabaseHelper().insertInto(statement, tableName, columns, values, strings);
	}

	public void insertInto(String tableName, String[] columns, String[] values, Boolean[] strings) throws SQLException {
		if (_currentStatement == null) {
			throw new SQLException("Closed statement"); //$NON-NLS-1$
		}
		getDatabaseHelper().insertInto(_currentStatement, tableName, columns, values, strings);
	}

	public void deleteFromStatement(String tableName, String[] columns, String[] values, Boolean[] strings)
			throws SQLException {
		getDatabaseHelper().deleteFromStatement(getConnection(), tableName, columns, values, strings);
	}

	public void deleteFrom(Statement statement, String tableName, String[] columns, String[] values, Boolean[] strings)
			throws SQLException {
		getDatabaseHelper().deleteFrom(statement, tableName, columns, values, strings);
	}

	public void deleteFrom(String tableName, String[] columns, String[] values, Boolean[] strings) throws SQLException {
		if (_currentStatement == null) {
			throw new SQLException("Closed statement"); //$NON-NLS-1$
		}
		getDatabaseHelper().deleteFrom(_currentStatement, tableName, columns, values, strings);
	}

	public List<Map<String, Object>> selectStatement(String tableName, String[] columns, String[] parameters,
			String[] conditions, String[] values, Boolean[] strings) throws SQLException {
		return getDatabaseHelper().selectStatement(getConnection(), tableName, columns, parameters, conditions, values,
				strings);
	}

	public List<Map<String, Object>> select(Statement statement, String tableName, String[] columns,
			String[] parameters, String[] conditions, String[] values, Boolean[] strings) throws SQLException {
		return getDatabaseHelper().select(statement, tableName, columns, parameters, conditions, values, strings);
	}

	public List<Map<String, Object>> select(String tableName, String[] columns, String[] parameters,
			String[] conditions, String[] values, Boolean[] strings) throws SQLException {
		if (_currentStatement == null) {
			throw new SQLException("Closed statement"); //$NON-NLS-1$
		}
		return getDatabaseHelper().select(_currentStatement, tableName, columns, parameters, conditions, values, strings);
	}

	public List<Map<String, Object>> selectAllStatement(String tableName) throws SQLException {
		return getDatabaseHelper().selectAllStatement(getConnection(), tableName);
	}

	public List<Map<String, Object>> selectAll(Statement statement, String tableName) throws SQLException {
		return getDatabaseHelper().selectAll(statement, tableName);
	}

	public List<Map<String, Object>> selectAll(String tableName) throws SQLException {
		if (_currentStatement == null) {
			throw new SQLException("Closed statement"); //$NON-NLS-1$
		}
		return getDatabaseHelper().selectAll(_currentStatement, tableName);
	}

	public void updateStatement(String tableName, String[] columnsToModify, String[] newValues,
			Boolean[] newValuesStrings, String[] parameters, String[] conditions, String[] values, Boolean[] strings)
					throws SQLException {
		getDatabaseHelper().updateStatement(getConnection(), tableName, columnsToModify, newValues, newValuesStrings,
				parameters, conditions, values, strings);
	}

	public void update(Statement statement, String tableName, String[] columnsToModify, String[] newValues,
			Boolean[] newValuesStrings, String[] parameters, String[] conditions, String[] values, Boolean[] strings)
					throws SQLException {
		getDatabaseHelper().update(statement, tableName, columnsToModify, newValues, newValuesStrings, parameters,
				conditions, values, strings);
	}

	public void update(String tableName, String[] columnsToModify, String[] newValues, Boolean[] newValuesStrings,
			String[] parameters, String[] conditions, String[] values, Boolean[] strings) throws SQLException {
		if (_currentStatement == null) {
			throw new SQLException("Closed statement"); //$NON-NLS-1$
		}
		getDatabaseHelper().update(_currentStatement, tableName, columnsToModify, newValues, newValuesStrings, parameters,
				conditions, values, strings);
	}

}
