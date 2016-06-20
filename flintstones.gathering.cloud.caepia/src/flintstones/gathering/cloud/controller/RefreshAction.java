package flintstones.gathering.cloud.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.ICommandIds;
import flintstones.gathering.cloud.view.ProblemsView;

/**
 * When run, this action will open another instance of a view.
 */
@SuppressWarnings("serial")
public class RefreshAction extends Action {

	private final IWorkbenchWindow window;

	public RefreshAction(IWorkbenchWindow window) {
		this.window = window;
		setText("Refrescar información de problemas");
		setId(ICommandIds.CMD_REFRESH);
		setActionDefinitionId(ICommandIds.CMD_REFRESH);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"flintstones.gathering.cloud", "/icons/refresh.png"));
	}

	public void run() {

		if (MessageDialog.openConfirm(window.getShell(), "Refrescar información de problemas",
				"¿Refrescar información de problemas?")) {
			OpenProblemsPerspective action = (OpenProblemsPerspective) RWT
					.getUISession().getAttribute("open-problems-action");
			if (action.isEnabled()) {
				action.run();
			}
			ProblemsView view = (ProblemsView) RWT.getUISession().getAttribute(
					ProblemsView.ID);
			view.refreshModel();
		}
	}
}
