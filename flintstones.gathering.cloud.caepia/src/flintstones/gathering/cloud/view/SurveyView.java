package flintstones.gathering.cloud.view;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import flintstones.gathering.cloud.dao.DAOProblemAssignments;
import flintstones.gathering.cloud.model.Key;
import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.model.ProblemAssignment;
import flintstones.gathering.cloud.model.User;
import flintstones.gathering.cloud.model.Valuation;
import flintstones.gathering.cloud.model.Valuations;

public class SurveyView extends ViewPart {

	public static final String ID = "flintstones.gathering.cloud.view.surveyView";

	private TableViewer _viewer;
	
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

	@SuppressWarnings("serial")
	class AlternativeLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			return ((String[]) obj)[0];
		}
	}
	
	@SuppressWarnings("serial")
	class CriterionLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			return ((String[]) obj)[1];	
		}
	}

	@SuppressWarnings("serial")
	class ValuationLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			return ((String[]) obj)[2];
		}
	}
	
	@SuppressWarnings("serial")
	@Override
	public void createPartControl(final Composite parent) {
		_viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		_viewer.setContentProvider(new ViewContentProvider());
		_viewer.setLabelProvider(new ViewLabelProvider());
		
		Table table = _viewer.getTable();
		table.setLinesVisible(true);
		table.setVisible(true);
		table.setHeaderVisible(true);
		
		TableViewerColumn tc_criterion = new TableViewerColumn(_viewer, SWT.NONE);
		tc_criterion.getColumn().setText("Criterio");
		tc_criterion.setLabelProvider(new CriterionLabelProvider());
		
		TableViewerColumn tc_alternative = new TableViewerColumn(_viewer, SWT.NONE);
		tc_alternative.getColumn().setText("Alternativa");
		tc_alternative.setLabelProvider(new AlternativeLabelProvider());
		
		TableViewerColumn tc_valuation = new TableViewerColumn(_viewer, SWT.NONE);
		tc_valuation.getColumn().setText("Valoración");
		tc_valuation.setLabelProvider(new ValuationLabelProvider());
		
		parent.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle area = parent.getClientArea();
				Point oldSize = _viewer.getTable().getSize();
				if (oldSize.x > area.width) {
					_viewer.getTable().getColumn(0).setWidth(parent.getSize().x / 3);
					_viewer.getTable().getColumn(1).setWidth(parent.getSize().x / 3 - 1);
					_viewer.getTable().getColumn(2).setWidth(parent.getSize().x / 3 - 1);
					_viewer.getTable().setSize(area.width, area.height);
				} else {
					_viewer.getTable().setSize(area.width, area.height);
					_viewer.getTable().getColumn(0).setWidth(parent.getSize().x / 3);
					_viewer.getTable().getColumn(1).setWidth(parent.getSize().x / 3 - 1);
					_viewer.getTable().getColumn(2).setWidth(parent.getSize().x / 3 - 1);
				}
			}
		});
		
		if(_problem != null) {
			setModel();
		}
	}

	private void setModel() {
		User user = (User) RWT.getUISession().getAttribute("user");
		Map<Problem, ProblemAssignment> model = DAOProblemAssignments.getDAO().getUserProblemAssignments(user);
		ProblemAssignment problemAssignment = model.get(_problem);
		Valuations valuations = problemAssignment.getValuations();
		
		List<String[]> input = new LinkedList<String[]>();
		for(String a: _problem.getAlternatives()) {
			for(String c: _problem.getCriteria()) {
				if(valuations != null) {
					String[] values = new String[3];
					values[0] = a;
					values[1] = c;
					Valuation v = valuations.getValuation(new Key(a, c));
					if(v != null) {
						values[2] = v.toString(); 
					} else {
						values[2] = "Not assigned";
					}
					input.add(values);
				}
			}
		}
		_viewer.setInput(input);
	}

	@Override
	public void setFocus() {
		_viewer.getTable().setFocus();
	}

	public void refresh() {
		_problem = (Problem) RWT.getUISession().getAttribute("valuation-problem");
		setModel();
	}
}
