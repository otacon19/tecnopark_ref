package flintstones.gathering.cloud.view;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewLayout;

import flintstones.gathering.cloud.view.frameworkstructuring.DomainAssignmentView;
import flintstones.gathering.cloud.view.frameworkstructuring.DomainIndexView;
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
		
		layout.addView(ElementAssignmentView.ID, IPageLayout.RIGHT, 0.2f, editorArea);
		IViewLayout viewLayout2 = layout.getViewLayout(ElementAssignmentView.ID);
		viewLayout2.setCloseable(false);
		viewLayout2.setMoveable(false);
		
		layout.addView(DomainIndexView.ID, IPageLayout.LEFT, 0.8f, editorArea);
		IViewLayout viewLayout3 = layout.getViewLayout(DomainIndexView.ID);
		viewLayout3.setCloseable(false);
		viewLayout3.setMoveable(false);
	}
}
