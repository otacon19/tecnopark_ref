package flintstones.gathering.cloud.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.ICommandIds;
import flintstones.gathering.cloud.view.SurveyView;

/**
 * When run, this action will open another instance of a view.
 */
@SuppressWarnings("serial")
public class OpenGatheringPerspective extends Action {

	public OpenGatheringPerspective() {
		setToolTipText("Mostrar gathering");
		setId(ICommandIds.CMD_OPEN_GATHERING_PERSPECTIVE);
		setActionDefinitionId(ICommandIds.CMD_OPEN_GATHERING_PERSPECTIVE);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/gathering.png"));
		RWT.getUISession().setAttribute("open-gathering-action", this);
	}

	public void run() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		try {
			workbench.showPerspective("flintstones.gathering.cloud.gathering.perspective", workbench.getActiveWorkbenchWindow());
			this.setEnabled(false);
			((OpenProblemsPerspective) RWT.getUISession().getAttribute("open-problems-action")).setEnabled(true);
			((OpenFrameworkStructuringPerspective) RWT.getUISession().getAttribute("open-frameworkstructuring-action")).setEnabled(true);
			
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
