package flintstones.gathering.cloud.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.ICommandIds;
import flintstones.gathering.cloud.model.User;
import flintstones.gathering.cloud.nls.Messages;
import flintstones.gathering.cloud.view.UserAccountDialog;

/**
 * When run, this action will open another instance of a view.
 */
@SuppressWarnings("serial")
public class UserAccountAction extends Action {

	private final IWorkbenchWindow window;

	public UserAccountAction(IWorkbenchWindow window) {
		super(Messages.UserAccountAction_User_account);
		this.window = window;
		setToolTipText(Messages.UserAccountAction_User_account);
		User user = (User) RWT.getUISession().getAttribute("user"); //$NON-NLS-1$
		String text = ""; //$NON-NLS-1$
		if (user != null) {
			text = user.getMail();
			String[] tokens = text.split("@"); //$NON-NLS-1$
			text = tokens[0] + Messages.UserAccountAction_in + tokens[1]; 
			if (user.getAdmin()) {
				text += Messages.UserAccountAction_admin;
			}
		}
		setText(text);
		setId(ICommandIds.CMD_USER_ACCOUNT);
		setActionDefinitionId(ICommandIds.CMD_USER_ACCOUNT);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"flintstones.gathering.cloud", "/icons/user_22.png")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void run() {
		UserAccountDialog uad = new UserAccountDialog(window.getShell());
		uad.open();
	}
}
