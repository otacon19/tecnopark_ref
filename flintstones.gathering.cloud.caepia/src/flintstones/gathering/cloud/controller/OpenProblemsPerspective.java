package flintstones.gathering.cloud.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.ICommandIds;

/**
 * When run, this action will open another instance of a view.
 */
@SuppressWarnings("serial")
public class OpenProblemsPerspective extends Action {

	public OpenProblemsPerspective() {
		setToolTipText("Mostrar problemas");
		setId(ICommandIds.CMD_OPEN_PROBLEMS_PERSPECTIVE);
		setActionDefinitionId(ICommandIds.CMD_OPEN_PROBLEMS_PERSPECTIVE);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"flintstones.gathering.cloud", "/icons/problems.png"));
		RWT.getUISession().setAttribute("open-problems-action", this);
	}

	public void run() {
		IWorkbench workbench = PlatformUI.getWorkbench();

		try {
			workbench.showPerspective(
					"flintstones.gathering.cloud.problems.perspective",
					workbench.getActiveWorkbenchWindow());
			this.setEnabled(false);
			((OpenGatheringPerspective) RWT.getUISession().getAttribute("open-gathering-action")).setEnabled(true);

		} catch (WorkbenchException e1) {
		}
	}
}
