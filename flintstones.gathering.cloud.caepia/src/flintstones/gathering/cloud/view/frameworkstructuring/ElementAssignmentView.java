package flintstones.gathering.cloud.view.frameworkstructuring;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;


public class ElementAssignmentView extends ViewPart implements ISelectionListener {

	public static final String ID = "flintstones.gathering.cloud.view.elementassignments"; //$NON-NLS-1$
	
	private Composite _container;
	
	private TableViewer _viewer;
	
	private TableViewerColumn _tvcCriterion;
	private TableViewerColumn _tvcAlternative;
	private TableViewerColumn _tvcDomain;
	
	public ElementAssignmentView() {}
	
	@SuppressWarnings("serial")
	class ViewContentProvider implements IStructuredContentProvider {

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object parent) {
			return ((List<String>) parent).toArray(new String[0]);
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
	class CriterionLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			return ((String) obj);
		}
	}

	@SuppressWarnings("serial")
	class AlternativeLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			return ((String) obj);
		}
	}

	@SuppressWarnings("serial")
	class DomainLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			return ((String) obj);
		}
	}
	
	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		RWT.getUISession().setAttribute(ID, this);
	}
	
	@Override
	public void createPartControl(Composite parent) {
		
		_viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		_viewer.setContentProvider(new ViewContentProvider());
		_viewer.setLabelProvider(new ViewLabelProvider());
		
		Table table = _viewer.getTable();
		table.setLinesVisible(true);
		table.setVisible(true);
		table.setHeaderVisible(true);
		
		_tvcCriterion = new TableViewerColumn(_viewer, SWT.NONE);
		_tvcCriterion.getColumn().setText("Criterion");
		_tvcCriterion.setLabelProvider(new CriterionLabelProvider());
		
		_tvcAlternative = new TableViewerColumn(_viewer, SWT.NONE);
		_tvcAlternative.getColumn().setText("Alternative");
		_tvcAlternative.setLabelProvider(new AlternativeLabelProvider());
		
		_tvcDomain = new TableViewerColumn(_viewer, SWT.NONE);
		_tvcDomain.getColumn().setText("Domain");
		_tvcDomain.setLabelProvider(new DomainLabelProvider());
		
		_container = parent;
		_container.setLayout(new FillLayout());
		
		List<String> input = new LinkedList<String>();
		
		_viewer.setInput(input);
	
		
		setModel();
	}

	
	@Override
	public void dispose() {
		_viewer.getTable().dispose();
	}
	
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		setModel();
	}
	
	private void setModel() {
		

	}


	@Override
	public void setFocus() {
		_viewer.getControl().setFocus();
	}
	
}
