package sinbad2.database.connector;

import org.eclipse.core.runtime.IConfigurationElement;

public class DatabaseConnectorRegistryExtension {

	protected IConfigurationElement _configuration;

	private DatabaseConnectorRegistryExtension() {
		_configuration = null;
	}

	public DatabaseConnectorRegistryExtension(IConfigurationElement element) {
		this();
		_configuration = element;
	}

	public IConfigurationElement getConfiguration() {
		return _configuration;
	}

	public String getAttribute(EDatabaseConnectorElements element) {
		String result = null;

		if (_configuration != null) {
			result = _configuration.getAttribute(element.toString());
		}

		return result;
	}

}
