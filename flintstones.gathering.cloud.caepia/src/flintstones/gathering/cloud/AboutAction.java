package flintstones.gathering.cloud;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * Opens an &quot;About RAP&quot; message dialog.
 */
@SuppressWarnings("serial")
public class AboutAction extends Action {

	private final IWorkbenchWindow window;

	public AboutAction(IWorkbenchWindow window) {
		super("Acerca de");
		setId(this.getClass().getName());
		this.window = window;
	}

	public void run() {
		if(window != null) {	
			String title = "Acerca de FLINTSTONES Gathering Cloud";
			String msg = "TODO";
			MessageDialog.openInformation(window.getShell(), title, msg);
		}
	}
}
