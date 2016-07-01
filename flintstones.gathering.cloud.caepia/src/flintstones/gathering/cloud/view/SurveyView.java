package flintstones.gathering.cloud.view;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import flintstones.gathering.cloud.model.Problem;

public class SurveyView extends ViewPart {

	public static final String ID = "flintstones.gathering.cloud.view.surveyView";

	private Problem _problem;

	public SurveyView() {
		_problem = (Problem) RWT.getUISession().getAttribute("valuation-problem");
	}

	@SuppressWarnings("serial")
	class ViewContentProvider implements IStructuredContentProvider {

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object parent) {
			return ((List<String[]>) parent).toArray(new String[0][0]);
		}
	}
	
	@SuppressWarnings("serial")
	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			return (String) obj;
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}
	
	@Override
	public void createPartControl(Composite parent) {
		List<String> alternatives = new LinkedList<String>();

		alternatives.add("a1");
		alternatives.add("a2");
		alternatives.add("a3");
		alternatives.add("a4");

		List<String> criteria = new LinkedList<String>();

		criteria.add("c1");
		criteria.add("c2");
		criteria.add("c3");
		criteria.add("c4");

		if (_problem != null) {
			alternatives = _problem.getAlternatives();
			criteria = _problem.getCriteria();
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void refresh() {
	}

}
