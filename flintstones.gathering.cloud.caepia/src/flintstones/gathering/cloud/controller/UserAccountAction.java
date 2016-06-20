package flintstones.gathering.cloud.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.ICommandIds;
import flintstones.gathering.cloud.model.User;
import flintstones.gathering.cloud.view.UserAccountDialog;

/**
 * When run, this action will open another instance of a view.
 */
@SuppressWarnings("serial")
public class UserAccountAction extends Action {

	private final IWorkbenchWindow window;

	public UserAccountAction(IWorkbenchWindow window) {
		super("Cuenta de usuario");
		this.window = window;
		setToolTipText("Cuenta de usuario");
		User user = (User) RWT.getUISession().getAttribute("user");
		String text = "";
		if (user != null) {
			text = user.getMail();
			String[] tokens = text.split("@");
			text = tokens[0] + " en " + tokens[1]; 
			if (user.getAdmin()) {
				text += " (administrador)";
			}
		}
		setText(text);
		setId(ICommandIds.CMD_USER_ACCOUNT);
		setActionDefinitionId(ICommandIds.CMD_USER_ACCOUNT);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"flintstones.gathering.cloud", "/icons/user_22.png"));
	}

	public void run() {
		UserAccountDialog uad = new UserAccountDialog(window.getShell());
		uad.open();
	}
}
