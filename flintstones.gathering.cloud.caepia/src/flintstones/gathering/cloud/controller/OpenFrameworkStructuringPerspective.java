package flintstones.gathering.cloud.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.ICommandIds;

@SuppressWarnings("serial")
public class OpenFrameworkStructuringPerspective extends Action {

	public OpenFrameworkStructuringPerspective() {
		setToolTipText("Mostrar framework structuring");
		setId(ICommandIds.CMD_OPEN_FRAMEWORK_STRUCTURING_PERSPECTIVE);
		setActionDefinitionId(ICommandIds.CMD_OPEN_FRAMEWORK_STRUCTURING_PERSPECTIVE);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/frameworkstructuring.png"));
		RWT.getUISession().setAttribute("open-frameworkstructuring-action", this);
	}

	public void run() {
		IWorkbench workbench = PlatformUI.getWorkbench();

		try {
			workbench.showPerspective("flintstones.gathering.cloud.frameworkstructuring.perspective", workbench.getActiveWorkbenchWindow());
			this.setEnabled(false);
			((OpenProblemsPerspective) RWT.getUISession().getAttribute("open-problems-action")).setEnabled(true);
			((OpenGatheringPerspective) RWT.getUISession().getAttribute("open-gathering-action")).setEnabled(true);

		} catch (WorkbenchException e1) {
		}
	}
}
