package flintstones.gathering.cloud.view;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IViewLayout;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import flintstones.gathering.cloud.controller.ExportProblemAction;
import flintstones.gathering.cloud.controller.ExportProblemActionMenu;
import flintstones.gathering.cloud.controller.OpenFrameworkStructuringPerspective;
import flintstones.gathering.cloud.dao.DAOProblemAssignments;
import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.model.User;

/**
 * Configures the perspective layout. This class is contributed through the
 * plugin.xml.
 */
public class ProblemsPerspective implements IPerspectiveFactory {

	public static final String ID = "flintstones.gathering.cloud.problems.perspective"; //$NON-NLS-1$
	
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		layout.addView(ProblemsView.ID, IPageLayout.LEFT, 0.25f, editorArea);
		IViewLayout viewLayout = layout.getViewLayout(ProblemsView.ID);
		viewLayout.setCloseable(false);
		viewLayout.setMoveable(false);
		
		layout.addView(ProblemView.ID, IPageLayout.TOP, 0.5f, editorArea);
		viewLayout = layout.getViewLayout(ProblemView.ID);
		viewLayout.setCloseable(false);
		viewLayout.setMoveable(false);
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().addPerspectiveListener(new IPerspectiveListener() {
			
			@Override
			public void perspectiveChanged(IWorkbenchPage page, IPerspectiveDescriptor perspective, String changeId) {
			}
			
			@Override
			public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
				if (perspective.getId().equals(ID)) {
					User user = (User) RWT.getUISession().getAttribute("user"); //$NON-NLS-1$
					if (!user.getManageProblems()) {
						if(((OpenFrameworkStructuringPerspective) RWT.getUISession().getAttribute("open-frameworkstructuring-action") == null)) { //$NON-NLS-1$
							new OpenFrameworkStructuringPerspective().run();
						} else {
							((OpenFrameworkStructuringPerspective) RWT.getUISession().getAttribute("open-frameworkstructuring-action")).run(); //$NON-NLS-1$
						}
					}
					
					if(DAOProblemAssignments.getDAO().isAvailableToExport((Problem) RWT.getUISession().getAttribute("problem"))) { //$NON-NLS-1$
						((ExportProblemAction) RWT.getUISession().getAttribute("export-action")).setEnabled(true); //$NON-NLS-1$
						((ExportProblemActionMenu) RWT.getUISession().getAttribute("export-action-menu")).setEnabled(true); //$NON-NLS-1$
					} else {
						((ExportProblemAction) RWT.getUISession().getAttribute("export-action")).setEnabled(false); //$NON-NLS-1$
						((ExportProblemActionMenu) RWT.getUISession().getAttribute("export-action-menu")).setEnabled(false); //$NON-NLS-1$
					}
				}
			}
		});
	}
	
}
