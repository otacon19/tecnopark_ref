package sinbad2.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import sinbad2.database.connector.DatabaseConnector;
import sinbad2.database.connector.DatabaseConnectorFactory;
import sinbad2.database.connector.DatabaseConnectorManager;

public class DatabaseManager {

	private final String EXTENSION_POINT = "sinbad2.database"; //$NON-NLS-1$

	private static DatabaseManager _instance = null;

	private Database _database;
	private DatabaseRegistryExtension _databaseRegistry;

	private DatabaseManager() {
		_database = null;
		_databaseRegistry = null;
		loadDatabaseRegistryExtension();
	}

	private void loadDatabaseRegistryExtension() {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] extensions = reg.getConfigurationElementsFor(EXTENSION_POINT);
		if (extensions.length == 1) {
			_databaseRegistry = new DatabaseRegistryExtension(extensions[0]);
		}
	}

	public static DatabaseManager getInstance() {
		if (_instance == null) {
			_instance = new DatabaseManager();
		}

		return _instance;
	}

	public Database getApplicationDatabase() {
		if (_database == null) {
			if (_databaseRegistry != null) {
				initDatabase();
			}
		}
		return _database;
	}

	private void initDatabase() {
		if (supportedDatabaseConnector()) {
			try {
				DatabaseConnectorFactory databaseConnectorFactory = DatabaseConnectorFactory.getInstance();
				DatabaseConnector databaseConnector = databaseConnectorFactory.createDatabaseConnector(_databaseRegistry.getAttribute(EDatabaseElements.connector), extractParameters());
				_database = new Database(_databaseRegistry.getAttribute(EDatabaseElements.id), databaseConnector);
			} catch (Exception e) {
				System.err.println("Database creation fail"); //$NON-NLS-1$
			}
		}
	}

	private boolean supportedDatabaseConnector() {
		String connector = _databaseRegistry.getAttribute(EDatabaseElements.connector);
		DatabaseConnectorManager databaseConnectorManager = DatabaseConnectorManager.getInstance();
		Set<String> supportedDatabasesConnectors = databaseConnectorManager.getSupportedDatabasesConnectors();
		return supportedDatabasesConnectors.contains(connector);
	}

	private Map<String, String> extractParameters() {
		Map<String, String> result = new HashMap<String, String>();
		IConfigurationElement[] parameters = _databaseRegistry.getConfiguration()
				.getChildren(EDatabaseElements.parameter.toString());
		for (IConfigurationElement parameter : parameters) {
			result.put(parameter.getAttribute(EDatabaseParameterElements.id.toString()),
					parameter.getAttribute(EDatabaseParameterElements.value.toString()));
		}
		return result;
	}

}
