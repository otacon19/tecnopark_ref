package sinbad2.database.connector;

import java.sql.SQLException;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;

public class DatabaseConnectorFactory {

	private static DatabaseConnectorFactory _instance = null;

	private DatabaseConnectorManager _databaseConnectorManager;

	private DatabaseConnectorFactory() {
		super();
		_databaseConnectorManager = DatabaseConnectorManager.getInstance();
	}

	public static DatabaseConnectorFactory getInstance() {
		if (_instance == null) {
			_instance = new DatabaseConnectorFactory();
		}
		return _instance;
	}

	public DatabaseConnector createDatabaseConnector(String extensionId, Map<String, String> parameters)
			throws CoreException, SQLException {
		DatabaseConnector result = null;
		result = _databaseConnectorManager.createDatabaseConnector(extensionId);
		result.connection(parameters);
		return result;
	}
}
