package flintstones.gathering.cloud.view;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
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
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.controller.CloseViewAction;
import flintstones.gathering.cloud.controller.OpenViewAction;
import flintstones.gathering.cloud.dao.DAOProblemAssignments;
import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.model.ProblemAssignment;
import flintstones.gathering.cloud.model.User;
import flintstones.gathering.cloud.nls.Messages;

public class SurveysView extends ViewPart {

	public static final String ID = "flintstones.gathering.cloud.view.surveysView"; //$NON-NLS-1$

	private Map<Problem, ProblemAssignment> model;
	private TableViewer viewer;

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		RWT.getUISession().setAttribute(ID, this);
	}

	class TableElement {
		private Problem problem = null;
		private ProblemAssignment assignment = null;

		public TableElement(Problem problem, ProblemAssignment assignment) {
			this.problem = problem;
			this.assignment = assignment;
		}

		public Problem getProblem() {
			return problem;
		}

		public ProblemAssignment getAssignment() {
			return assignment;
		}
	}

	@SuppressWarnings("serial")
	class ViewContentProvider implements IStructuredContentProvider {

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object parent) {
			List<TableElement> result = new LinkedList<TableElement>();

			model = (Map<Problem, ProblemAssignment>) parent;
			for (Problem p : model.keySet()) {
				result.add(new TableElement(p, model.get(p)));
			}

			return result.toArray(new TableElement[0]);
		}
	}

	@SuppressWarnings("serial")
	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			return ((Problem) obj).toString();
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	@SuppressWarnings("serial")
	class ProblemLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			return ((TableElement) obj).problem.getId();
		}
	}

	@SuppressWarnings("serial")
	class ExpertLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			ProblemAssignment assignment = ((TableElement) obj).assignment;
			return assignment.getId();
		}
	}

	private Object createModel() {
		User user = (User) RWT.getUISession().getAttribute("user"); //$NON-NLS-1$
		Map<Problem, ProblemAssignment> auxModel = DAOProblemAssignments.getDAO().getUserProblemAssignments(user);
		model = new HashMap<Problem, ProblemAssignment>();
		for (Problem problem : auxModel.keySet()) {
			if (!auxModel.get(problem).getMake()) {
				model.put(problem, auxModel.get(problem));
			}
		}
		return model;
	}

	@SuppressWarnings("serial")
	public void createPartControl(final Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		viewer.setContentProvider(new ViewContentProvider());

		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableViewerColumn tvc = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn tc = tvc.getColumn();
		tc.setText(Messages.SurveysView_Problem);
		tc.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT));
		tvc.setLabelProvider(new ProblemLabelProvider());

		tvc = new TableViewerColumn(viewer, SWT.NONE);
		tc = tvc.getColumn();
		tc.setText(Messages.SurveysView_Expert);
		tc.setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/expert_20.png").createImage()); //$NON-NLS-1$ //$NON-NLS-2$
		tvc.setLabelProvider(new ExpertLabelProvider());

		parent.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle area = parent.getClientArea();
				Point preferredSize = viewer.getTable().computeSize(SWT.DEFAULT, SWT.DEFAULT);
				int width = area.width - 2 * viewer.getTable().getBorderWidth();
				if (preferredSize.y > area.height + viewer.getTable().getHeaderHeight()) {
					Point vBarSize = viewer.getTable().getVerticalBar().getSize();
					width -= vBarSize.x;
				}
				
				if(viewer.getTable().getItemCount() == 0) {
					viewer.getTable().getColumn(0).setWidth(parent.getSize().x / 2 - 1);
					viewer.getTable().getColumn(1).setWidth(parent.getSize().x / 2 - 1);
					viewer.getTable().setSize(area.width, area.height);
				} else {
					Point oldSize = viewer.getTable().getSize();
					if (oldSize.x > area.width) {
						viewer.getTable().getColumn(0).pack();
						viewer.getTable().getColumn(1).setWidth(width - viewer.getTable().getColumn(0).getWidth());
						viewer.getTable().setSize(area.width, area.height);
					} else {
						viewer.getTable().setSize(area.width, area.height);
						viewer.getTable().getColumn(0).pack();
						viewer.getTable().getColumn(1).setWidth(width - viewer.getTable().getColumn(0).getWidth());
					}
				}
			}
		});

		hookListeners();
		refreshModel();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private void hookListeners() {
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				updateSelection();
			}
		});
	}

	public void refreshModel() {
		viewer.setInput(createModel());

		if (model.isEmpty()) {
			viewer.setSelection(new StructuredSelection(), false);
		} else {
			viewer.setSelection(new StructuredSelection(viewer.getElementAt(0)), true);
		}
		updateSelection();

		//packColumns();
	}

	private void updateSelection() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		TableElement te = null;
		if (!selection.isEmpty()) {
			te = (TableElement) selection.getFirstElement();
		}

		Problem problem = null;
		ProblemAssignment assignment = null;
		if (te != null) {
			problem = te.problem;
			assignment = te.assignment;
		}
		RWT.getUISession().setAttribute("valuation-problem", problem); //$NON-NLS-1$
		RWT.getUISession().setAttribute("valuation-problem-assignment", assignment); //$NON-NLS-1$

		if (te != null) {
			OpenViewAction ova = new OpenViewAction(getSite().getWorkbenchWindow(), "survey", SurveyView.ID); //$NON-NLS-1$
			ova.run();
		} else {
			CloseViewAction cva = new CloseViewAction(getSite().getWorkbenchWindow(), "survey", SurveyView.ID); //$NON-NLS-1$
			cva.run();
		}
	}
}
