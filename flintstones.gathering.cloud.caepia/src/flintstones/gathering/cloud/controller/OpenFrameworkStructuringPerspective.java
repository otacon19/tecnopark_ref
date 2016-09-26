package flintstones.gathering.cloud.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.ICommandIds;
import flintstones.gathering.cloud.nls.Messages;

@SuppressWarnings("serial")
public class OpenFrameworkStructuringPerspective extends Action {

	public OpenFrameworkStructuringPerspective() {
		setToolTipText(Messages.OpenFrameworkStructuringPerspective_Show_framework_structuring0);
		setId(ICommandIds.CMD_OPEN_FRAMEWORK_STRUCTURING_PERSPECTIVE);
		setActionDefinitionId(ICommandIds.CMD_OPEN_FRAMEWORK_STRUCTURING_PERSPECTIVE);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/frameworkstructuring.png")); //$NON-NLS-1$ //$NON-NLS-2$
		RWT.getUISession().setAttribute("open-frameworkstructuring-action", this); //$NON-NLS-1$
	}

	public void run() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		try {
			workbench.showPerspective("flintstones.gathering.cloud.frameworkstructuring.perspective", workbench.getActiveWorkbenchWindow()); //$NON-NLS-1$
			this.setEnabled(false);
			((OpenGatheringPerspective) RWT.getUISession().getAttribute("open-gathering-action")).setEnabled(true); //$NON-NLS-1$
			
		} catch (WorkbenchException e1) {
		}
	}
}
