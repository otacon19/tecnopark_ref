package flintstones.gathering.cloud.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.ICommandIds;
import flintstones.gathering.cloud.nls.Messages;
import flintstones.gathering.cloud.view.SurveyView;

/**
 * When run, this action will open another instance of a view.
 */
@SuppressWarnings("serial")
public class OpenGatheringPerspective extends Action {

	public OpenGatheringPerspective() {
		setToolTipText(Messages.OpenGatheringPerspective_Show_gathering);
		setId(ICommandIds.CMD_OPEN_GATHERING_PERSPECTIVE);
		setActionDefinitionId(ICommandIds.CMD_OPEN_GATHERING_PERSPECTIVE);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/gathering.png")); //$NON-NLS-1$ //$NON-NLS-2$
		RWT.getUISession().setAttribute("open-gathering-action", this); //$NON-NLS-1$
	}

	public void run() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		try {
			workbench.showPerspective("flintstones.gathering.cloud.gathering.perspective", workbench.getActiveWorkbenchWindow()); //$NON-NLS-1$
			this.setEnabled(false);
			((OpenProblemsPerspective) RWT.getUISession().getAttribute("open-problems-action")).setEnabled(true); //$NON-NLS-1$
			((OpenFrameworkStructuringPerspective) RWT.getUISession().getAttribute("open-frameworkstructuring-action")).setEnabled(true); //$NON-NLS-1$
			
			SurveyView surveyView = null;
			IViewReference viewReferences[] = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();
			for (int i = 0; i < viewReferences.length; i++) {
				if (SurveyView.ID.equals(viewReferences[i].getId())) {
					surveyView = (SurveyView) viewReferences[i].getView(false);
				}
			}
			if(surveyView != null) {
				surveyView.refresh();
			}
			
		} catch (WorkbenchException e1) {
		}
	}
}
