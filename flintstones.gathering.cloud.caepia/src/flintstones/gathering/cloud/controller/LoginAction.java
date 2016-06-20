package flintstones.gathering.cloud.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.ICommandIds;
import flintstones.gathering.cloud.view.LoginDialog;
import flintstones.gathering.cloud.model.User;
/**
 * When run, this action will show a message dialog.
 */
@SuppressWarnings("serial")
public class LoginAction extends Action {

	private final IWorkbenchWindow window;
	private User _user;

	public LoginAction(String text, IWorkbenchWindow window) {
		super(text);
		this.window = window;
		setId(ICommandIds.CMD_LOGIN_ACTION);
		setActionDefinitionId(ICommandIds.CMD_LOGIN_ACTION);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/sample3.gif"));
	}

	public void run() {
		_user = null;
		LoginDialog dialog = new LoginDialog(window.getShell());
		if (dialog.open() == IDialogConstants.OK_ID) {
			_user = dialog.getUser();
		}
		
		if (_user == null) {
			ActionFactory.QUIT.create(window).run();
		}
	}
	
	public User getUser() {
		return _user;
	}
}
