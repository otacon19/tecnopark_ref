package flintstones.gathering.cloud.view;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewLayout;

/**
 * Configures the perspective layout. This class is contributed through the
 * plugin.xml.
 */
public class GatheringPerspective implements IPerspectiveFactory {

	public static final String ID = "flintstones.gathering.cloud.gathering.perspective";
	
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		layout.addView(SurveysView.ID, IPageLayout.LEFT, 0.25f, editorArea);
		IViewLayout viewLayout = layout.getViewLayout(SurveysView.ID);
		viewLayout.setCloseable(false);
		viewLayout.setMoveable(false);
		
		layout.addView(ConfidenceView.ID, IPageLayout.BOTTOM, 0.35f, SurveysView.ID);
		viewLayout = layout.getViewLayout(ConfidenceView.ID);
		viewLayout.setCloseable(false);
		viewLayout.setMoveable(false);
		
		layout.addView(SurveyView.ID, IPageLayout.TOP, 0.5f, editorArea);
		viewLayout = layout.getViewLayout(SurveyView.ID);
		viewLayout.setCloseable(false);
		viewLayout.setMoveable(false);
		
		layout.addView(ValuationView.ID, IPageLayout.BOTTOM, 0.6f, SurveyView.ID);
		viewLayout = layout.getViewLayout(ValuationView.ID);
		viewLayout.setCloseable(false);
		viewLayout.setMoveable(false);
	}
	
}
