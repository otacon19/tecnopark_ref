package sinbad2.mail;

import java.util.Properties;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import sinbad2.mail.extension.EMailAccountElements;
import sinbad2.mail.extension.EMailAccountPropertyElements;
import sinbad2.mail.extension.MailAccountRegistryExtension;

public class MailAccountManager {

	private final String EXTENSION_POINT = "sinbad2.mail.account"; //$NON-NLS-1$

	private static MailAccountManager _instance = null;

	private MailAccount _mailAccount;
	private MailAccountRegistryExtension _registry;

	private MailAccountManager() {
		_mailAccount = null;
		_registry = null;
		loadMailAccountRegistryExtension();
	}

	private void loadMailAccountRegistryExtension() {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] extensions = reg.getConfigurationElementsFor(EXTENSION_POINT);
		if (extensions.length == 1) {
			_registry = new MailAccountRegistryExtension(extensions[0]);
		}
	}

	public static MailAccountManager getInstance() {
		if (_instance == null) {
			_instance = new MailAccountManager();
		}

		return _instance;
	}

	public MailAccount getApplicationMailAccount() {
		if (_mailAccount == null) {
			initMailAccount();
		}
		return _mailAccount;
	}

	private void initMailAccount() {
		try {
			Properties properties = extractProperties();
			String mail = _registry.getAttribute(EMailAccountElements.mail);
			String password = _registry.getAttribute(EMailAccountElements.password);
			_mailAccount = new MailAccount(properties, mail, password);
		} catch (Exception e) {
			System.err.println("Mail account configuration fail"); //$NON-NLS-1$
		}
	}

	private Properties extractProperties() {
		Properties result = new Properties();
		IConfigurationElement[] properties = _registry.getConfiguration()
				.getChildren(EMailAccountElements.property.toString());
		for (IConfigurationElement property : properties) {
			result.put(property.getAttribute(EMailAccountPropertyElements.id.toString()),
					property.getAttribute(EMailAccountPropertyElements.value.toString()));
		}
		return result;
	}

}
