package sinbad2.database.connector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import sinbad2.database.DatabaseHelper;

public class DatabaseConnectorManager {

	private final String EXTENSION_POINT = "sinbad2.database.connector"; //$NON-NLS-1$

	private static DatabaseConnectorManager _instance = null;

	private DatabaseHelper _databaseHelper = null;
	private Map<String, DatabaseConnector> _databasesConnectors;
	private Map<String, DatabaseConnectorRegistryExtension> _registers;

	private DatabaseConnectorManager() {
		_databaseHelper = new DatabaseHelper();
		_databasesConnectors = new HashMap<String, DatabaseConnector>();
		_registers = new HashMap<String, DatabaseConnectorRegistryExtension>();
		loadRegistersExtension();
	}

	private void loadRegistersExtension() {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] extensions = reg.getConfigurationElementsFor(EXTENSION_POINT);

		DatabaseConnectorRegistryExtension registry;
		for (IConfigurationElement extension : extensions) {
			registry = new DatabaseConnectorRegistryExtension(extension);
			addDatabaseConnectorRegistry(registry);
		}
	}

	private void addDatabaseConnectorRegistry(DatabaseConnectorRegistryExtension registry) {
		_registers.put(registry.getAttribute(EDatabaseConnectorElements.id), registry);
	}

	public DatabaseConnector createDatabaseConnector(String extensionId) {
		DatabaseConnector result = null;
		try {
			DatabaseConnectorRegistryExtension registry = _registers.get(extensionId);
			IConfigurationElement configuration = registry.getConfiguration();
			result = (DatabaseConnector) configuration
					.createExecutableExtension(EDatabaseConnectorElements.connector.toString());
			addDatabaseConnector(extensionId, result);
		} catch (CoreException e) {
			System.err.println("Database connector \"" + extensionId + "\" creation fail"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return result;
	}

	public static DatabaseConnectorManager getInstance() {
		if (_instance == null) {
			_instance = new DatabaseConnectorManager();
		}

		return _instance;
	}

	public Map<String, DatabaseConnector> getAvailableDatabasesConnectors() {
		return _databasesConnectors;
	}

	public Set<String> getSupportedDatabasesConnectors() {
		return _registers.keySet();
	}

	public DatabaseConnectorRegistryExtension getDatabaseRegistryExtension(String id) {
		return _registers.get(id);
	}

	private void addDatabaseConnector(String id, DatabaseConnector databaseConnector) {
		databaseConnector.setDatabaseHelper(_databaseHelper);
		_databasesConnectors.put(id, databaseConnector);
	}

}
