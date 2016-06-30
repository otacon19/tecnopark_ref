package flintstones.gathering.cloud.view.frameworkstructuring;

import java.util.LinkedList;
import java.util.List;

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
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class ElementAssignmentView extends ViewPart implements IAssignmentDomain {

	public static final String ID = "flintstones.gathering.cloud.view.elementassignments"; //$NON-NLS-1$
	
	private TableViewer _viewer;
	
	private TableViewerColumn _tvcCriterion;
	private TableViewerColumn _tvcAlternative;
	private TableViewerColumn _tvcDomain;
	
	private List<String[]> _domainAssignments;
	
	public ElementAssignmentView() {
		_domainAssignments = new LinkedList<String[]>();
		
		DomainAssignmentView domainAssignmentView = null;
		IViewReference viewReferences[] = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();
		for (int i = 0; i < viewReferences.length; i++) {
			if (DomainAssignmentView.ID.equals(viewReferences[i].getId())) {
				domainAssignmentView = (DomainAssignmentView) viewReferences[i].getView(false);
			}
		}
		
		domainAssignmentView.registerListener(this);
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
	class CriterionLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			return ((String[]) obj)[0];	
		}
	}

	@SuppressWarnings("serial")
	class AlternativeLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			return ((String[]) obj)[1];
		}
	}

	@SuppressWarnings("serial")
	class DomainLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			return ((String[]) obj)[2];
		}
	}
	
	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		RWT.getUISession().setAttribute(ID, this);
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
		
		_tvcCriterion = new TableViewerColumn(_viewer, SWT.NONE);
		_tvcCriterion.getColumn().setText("Criterion");
		_tvcCriterion.setLabelProvider(new CriterionLabelProvider());
		
		_tvcAlternative = new TableViewerColumn(_viewer, SWT.NONE);
		_tvcAlternative.getColumn().setText("Alternative");
		_tvcAlternative.setLabelProvider(new AlternativeLabelProvider());
		
		_tvcDomain = new TableViewerColumn(_viewer, SWT.NONE);
		_tvcDomain.getColumn().setText("Domain");
		_tvcDomain.setLabelProvider(new DomainLabelProvider());
		
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
		
		_viewer.setInput(_domainAssignments);
		
	}

	
	@Override
	public void dispose() {
		_viewer.getTable().dispose();
	}
	
	@Override
	public void setFocus() {
		_viewer.getControl().setFocus();
	}

	@Override
	public void notifyAssignmentDomain(String[] assignment) {
		if(!checkAssignment(assignment)) {
			_domainAssignments.add(assignment);
		}
		_viewer.refresh();
	}

	private boolean checkAssignment(String[] assignment) {
		
		for(String[] a: _domainAssignments) {
			if(a[0] == assignment[0] && a[1] == assignment[1]) { 
				if(a[2] != assignment[2]) {
					int index = _domainAssignments.indexOf(a);
					_domainAssignments.remove(a);
					_domainAssignments.add(index, assignment);
					
					return true;
				}
			}
		}
		return false;
	}
}
