package sinbad2.mail.extension;

import org.eclipse.core.runtime.IConfigurationElement;

public class MailAccountRegistryExtension {

	protected IConfigurationElement _configuration;

	private MailAccountRegistryExtension() {
		_configuration = null;
	}

	public MailAccountRegistryExtension(IConfigurationElement element) {
		this();
		_configuration = element;
	}

	public IConfigurationElement getConfiguration() {
		return _configuration;
	}

	public String getAttribute(EMailAccountElements element) {
		String result = null;

		if (_configuration != null) {
			result = _configuration.getAttribute(element.toString());
		}

		return result;
	}

}
