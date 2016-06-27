package flintstones.gathering.cloud.view;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewLayout;

import flintstones.gathering.cloud.view.frameworkstructuring.DomainAssignmentView;
import flintstones.gathering.cloud.view.frameworkstructuring.ElementAssignmentView;

public class FrameworkStructuringPerspective implements IPerspectiveFactory{

public static final String ID = "flintstones.gathering.cloud.frameworkstructuring.perspective";
	
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		layout.addView(DomainAssignmentView.ID, IPageLayout.TOP, 0.1f, editorArea);
		IViewLayout viewLayout = layout.getViewLayout(DomainAssignmentView.ID);
		viewLayout.setCloseable(false);
		viewLayout.setMoveable(false);
		
		layout.addView(ElementAssignmentView.ID, IPageLayout.BOTTOM, 0.9f, editorArea);
		IViewLayout viewLayout2 = layout.getViewLayout(ElementAssignmentView.ID);
		viewLayout2.setCloseable(false);
		viewLayout2.setMoveable(false);

	}
}
