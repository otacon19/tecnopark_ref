package flintstones.gathering.cloud.view;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IViewLayout;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import flintstones.gathering.cloud.controller.OpenFrameworkStructuringPerspective;
import flintstones.gathering.cloud.model.User;

/**
 * Configures the perspective layout. This class is contributed through the
 * plugin.xml.
 */
public class ProblemsPerspective implements IPerspectiveFactory {

	public static final String ID = "flintstones.gathering.cloud.problems.perspective";
	
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
			public void perspectiveChanged(IWorkbenchPage page,
					IPerspectiveDescriptor perspective, String changeId) {
			}
			
			@Override
			public void perspectiveActivated(IWorkbenchPage page,
					IPerspectiveDescriptor perspective) {
				if (perspective.getId().equals(ID)) {
					User user = (User) RWT.getUISession().getAttribute("user");
					if (!user.getManageProblems()) {
						new OpenFrameworkStructuringPerspective().run();
					}
				}
			}
		});
	}
	
}
