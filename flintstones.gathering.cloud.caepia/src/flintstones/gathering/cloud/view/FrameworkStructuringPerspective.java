package flintstones.gathering.cloud.view;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewLayout;

import flintstones.gathering.cloud.view.frameworkstructuring.DomainAssignmentView;

public class FrameworkStructuringPerspective implements IPerspectiveFactory{

public static final String ID = "flintstones.gathering.cloud.frameworkstructuring.perspective";
	
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		layout.addView(DomainAssignmentView.ID, IPageLayout.LEFT, 0.25f, editorArea);
		IViewLayout viewLayout = layout.getViewLayout(SurveysView.ID);
		viewLayout.setCloseable(false);
		viewLayout.setMoveable(false);

	}
}
