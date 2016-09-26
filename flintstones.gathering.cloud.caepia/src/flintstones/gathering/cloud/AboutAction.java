package flintstones.gathering.cloud;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import flintstones.gathering.cloud.nls.Messages;

/**
 * Opens an &quot;About RAP&quot; message dialog.
 */
@SuppressWarnings("serial")
public class AboutAction extends Action {

	private final IWorkbenchWindow window;

	public AboutAction(IWorkbenchWindow window) {
		super(Messages.AboutAction_About0);
		setId(this.getClass().getName());
		this.window = window;
	}

	public void run() {
		if(window != null) {	
			String title = Messages.AboutAction_AboutAbout_Flintstones_Gathering_Cloud;
			String msg = "TODO"; //$NON-NLS-1$
			MessageDialog.openInformation(window.getShell(), title, msg);
		}
	}
}
