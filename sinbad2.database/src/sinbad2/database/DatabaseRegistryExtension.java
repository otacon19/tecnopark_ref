package sinbad2.database;

import org.eclipse.core.runtime.IConfigurationElement;

public class DatabaseRegistryExtension {

	protected IConfigurationElement _configuration;

	private DatabaseRegistryExtension() {
		_configuration = null;
	}

	public DatabaseRegistryExtension(IConfigurationElement element) {
		this();
		_configuration = element;
	}

	public IConfigurationElement getConfiguration() {
		return _configuration;
	}

	public String getAttribute(EDatabaseElements element) {
		String result = null;

		if (_configuration != null) {
			result = _configuration.getAttribute(element.toString());
		}

		return result;
	}

}
