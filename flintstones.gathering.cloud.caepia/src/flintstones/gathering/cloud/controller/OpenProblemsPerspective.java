package flintstones.gathering.cloud.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.ICommandIds;
import flintstones.gathering.cloud.nls.Messages;

/**
 * When run, this action will open another instance of a view.
 */
@SuppressWarnings("serial")
public class OpenProblemsPerspective extends Action {

	public OpenProblemsPerspective() {
		setToolTipText(Messages.OpenProblemsPerspective_Show_problems);
		setId(ICommandIds.CMD_OPEN_PROBLEMS_PERSPECTIVE);
		setActionDefinitionId(ICommandIds.CMD_OPEN_PROBLEMS_PERSPECTIVE);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/problems.png")); //$NON-NLS-1$ //$NON-NLS-2$
		RWT.getUISession().setAttribute("open-problems-action", this); //$NON-NLS-1$
	}

	public void run() {
		IWorkbench workbench = PlatformUI.getWorkbench();

		try {
			workbench.showPerspective("flintstones.gathering.cloud.problems.perspective", workbench.getActiveWorkbenchWindow()); //$NON-NLS-1$
			this.setEnabled(false);
			((OpenGatheringPerspective) RWT.getUISession().getAttribute("open-gathering-action")).setEnabled(true); //$NON-NLS-1$

		} catch (WorkbenchException e1) {
		}
	}
}
