package flintstones.gathering.cloud.view;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.model.ProblemAssignment;

public class ProblemView extends ViewPart {

	public static final String ID = "flintstones.gathering.cloud.view.problemView";

	private Problem _problem;

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		RWT.getUISession().setAttribute(ID, this);
		_problem = (Problem) RWT.getUISession().getAttribute("problem");
		setPartName((_problem != null) ? "Problema: " + _problem.getId() : "Problema no seleccionado");
	}

	private TableViewer viewer;

	class TableElement {
		private String expert = null;
		private ProblemAssignment assignment = null;

		public TableElement(String expert, ProblemAssignment assignment) {
			this.expert = expert;
			this.assignment = assignment;
		}

		public String getExpert() {
			return expert;
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

		public Object[] getElements(Object parent) {
			List<TableElement> result = new LinkedList<TableElement>();

			Problem p = ((Problem) parent);
			Map<String, ProblemAssignment> pa = p.getAssignments();
			for (String expert : pa.keySet()) {
				result.add(new TableElement(expert, pa.get(expert)));
			}

			return result.toArray(new TableElement[0]);
		}
	}

	@SuppressWarnings("serial")
	class ExpertLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			return ((TableElement) obj).getExpert();
		}
	}

	@SuppressWarnings("serial")
	class UserLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			ProblemAssignment assignment = ((TableElement) obj).getAssignment();
			return assignment.getUser().getMail();
		}
	}

	@SuppressWarnings("serial")
	class MakeLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			ProblemAssignment assignment = ((TableElement) obj).getAssignment();
			if (assignment.getMake()) {
				return "yes";
			} else {
				return "no";
			}
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		
		viewer = new TableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		viewer.setContentProvider(new ViewContentProvider());

		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableViewerColumn tvc = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn tc = tvc.getColumn();
		tc.setText("Identificador del experto");
		tc.setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/expert_20.png").createImage());
		tvc.setLabelProvider(new ExpertLabelProvider());

		tvc = new TableViewerColumn(viewer, SWT.NONE);
		tc = tvc.getColumn();
		tc.setText("Mail del experto");
		tc.setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/mail_20.png").createImage());
		tvc.setLabelProvider(new UserLabelProvider());

		tvc = new TableViewerColumn(viewer, SWT.NONE);
		tc = tvc.getColumn();
		tc.setText("¿El experto ha enviado sus valoraciones?");
		tc.setImage(AbstractUIPlugin.imageDescriptorFromPlugin(
				"flintstones.gathering.cloud", "/icons/vote_20.png").createImage());
		tvc.setLabelProvider(new MakeLabelProvider());
		
		refresh();
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	private void packColumns() {
		for (TableColumn column : viewer.getTable().getColumns()) {
			column.pack();
			column.setWidth(column.getWidth() + 40);
		}
	}

	public void refresh() {
		_problem = (Problem) RWT.getUISession().getAttribute("problem");
		if (_problem != null) {
			viewer.getTable().setVisible(true);
			viewer.setInput(_problem);
			packColumns();
		} else {
			viewer.getTable().setVisible(false);
		}
		setPartName((_problem != null) ? "Problema: " + _problem.getId() : "Problema no seleccionado");
	}
}
