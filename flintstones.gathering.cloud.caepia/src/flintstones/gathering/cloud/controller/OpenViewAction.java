package flintstones.gathering.cloud.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.ICommandIds;
import flintstones.gathering.cloud.nls.Messages;
import flintstones.gathering.cloud.view.ProblemView;
import flintstones.gathering.cloud.view.SurveyView;

/**
 * When run, this action will open another instance of a view.
 */
@SuppressWarnings("serial")
public class OpenViewAction extends Action {

	private final IWorkbenchWindow window;
	private final String viewId;

	public OpenViewAction(IWorkbenchWindow window, String label, String viewId) {
		this.window = window;
		this.viewId = viewId;
		setText(label);
		setId(ICommandIds.CMD_OPEN);
		setActionDefinitionId(ICommandIds.CMD_OPEN);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/sample2.gif")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void run() {
		
		if(window != null) {	
			try {
				IViewPart view = (IViewPart) RWT.getUISession().getAttribute(viewId);
				if (view != null) {
					if (viewId.equals(ProblemView.ID)) {
						((ProblemView) view).refresh();
					} else if (viewId.equals(SurveyView.ID)) {
						((SurveyView) view).refresh();
					}
				} else {
					view = window.getActivePage().showView(viewId);
					RWT.getUISession().setAttribute(viewId, view);
				}
			} catch (PartInitException e) {
				MessageDialog.openError(window.getShell(), Messages.OpenViewAction_Error, Messages.OpenViewAction_Open_view_error + e.getMessage());
			}
		}
	}
}
