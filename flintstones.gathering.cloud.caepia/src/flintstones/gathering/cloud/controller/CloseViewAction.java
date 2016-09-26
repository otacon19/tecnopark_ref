package flintstones.gathering.cloud.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.ICommandIds;

/**
 * When run, this action will open another instance of a view.
 */
@SuppressWarnings("serial")
public class CloseViewAction extends Action {

	private final IWorkbenchWindow window;
	private final String viewId;

	public CloseViewAction(IWorkbenchWindow window, String label, String viewId) {
		this.window = window;
		this.viewId = viewId;
		setText(label);
		setId(ICommandIds.CMD_CLOSE);
		setActionDefinitionId(ICommandIds.CMD_CLOSE);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"flintstones.gathering.cloud", "/icons/sample2.gif")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void run() {

		if (window != null) {
			IViewPart view = (IViewPart) RWT.getUISession()
					.getAttribute(viewId);
			if (view != null) {
				RWT.getUISession().setAttribute(viewId, null);
				window.getActivePage().hideView(view);
			}
		}
	}
}
