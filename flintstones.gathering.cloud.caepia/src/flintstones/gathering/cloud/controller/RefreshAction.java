package flintstones.gathering.cloud.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.ICommandIds;
import flintstones.gathering.cloud.nls.Messages;
import flintstones.gathering.cloud.view.ProblemsView;

/**
 * When run, this action will open another instance of a view.
 */
@SuppressWarnings("serial")
public class RefreshAction extends Action {

	private final IWorkbenchWindow window;

	public RefreshAction(IWorkbenchWindow window) {
		this.window = window;
		setText(Messages.RefreshAction_Refresh_problems_information);
		setId(ICommandIds.CMD_REFRESH);
		setActionDefinitionId(ICommandIds.CMD_REFRESH);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"flintstones.gathering.cloud", "/icons/refresh.png")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void run() {

		if (MessageDialog.openConfirm(window.getShell(), Messages.RefreshAction_Refresh_problems_information,
				Messages.RefreshAction_Do_you_want_to_refresh_problem_information)) {
			OpenProblemsPerspective action = (OpenProblemsPerspective) RWT
					.getUISession().getAttribute("open-problems-action"); //$NON-NLS-1$
			if (action.isEnabled()) {
				action.run();
			}
			ProblemsView view = (ProblemsView) RWT.getUISession().getAttribute(
					ProblemsView.ID);
			view.refreshModel();
		}
	}
}
