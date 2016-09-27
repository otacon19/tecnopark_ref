package flintstones.gathering.cloud.view.frameworkstructuring;

import java.util.Collections;
import java.util.Comparator;
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
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.nls.Messages;

public class ElementAssignmentView extends ViewPart implements IAssignmentDomain {

	public static final String ID = "flintstones.gathering.cloud.view.elementassignments"; //$NON-NLS-1$
	
	private TableViewer _viewer;
	
	private TableViewerColumn _tvcCriterion;
	private TableViewerColumn _tvcAlternative;
	private TableViewerColumn _tvcDomain;
	
	private List<String[]> _domainAssignments;
	
	@SuppressWarnings("rawtypes")
	private static class DataComparator implements Comparator {
		@Override
		public int compare(Object d1, Object d2) {
			String c1 = ((String[]) d1)[0];
			String c2 = ((String[]) d2)[0];
			String a1 = ((String[]) d1)[1];
			String a2 = ((String[]) d2)[1];

			int criterionComparation = extractInt(c1) - extractInt(c2);
			if (criterionComparation != 0) {
				return criterionComparation;
			} else if (extractInt(a1) - (extractInt(a2)) != 0) {
				return extractInt(a1) - extractInt(a2);
			} else {
				return 0;
			}
		}

		int extractInt(String s) {
			String num = s.replaceAll("\\D", "");
			return num.isEmpty() ? 0 : Integer.parseInt(num);
		}
	}
	
	public ElementAssignmentView() {
		DomainAssignmentView domainAssignmentView = null;
		IViewReference viewReferences[] = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();
		for (int i = 0; i < viewReferences.length; i++) {
			if (DomainAssignmentView.ID.equals(viewReferences[i].getId())) {
				domainAssignmentView = (DomainAssignmentView) viewReferences[i].getView(false);
			}
		}
		
		domainAssignmentView.registerListener(this);
		
		_domainAssignments = domainAssignmentView.getDomainAssignments();
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
	
	@SuppressWarnings({ "serial", "unchecked" })
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
		_tvcCriterion.getColumn().setText(Messages.ElementAssignmentView_Criterion);
		_tvcCriterion.setLabelProvider(new CriterionLabelProvider());
		_tvcCriterion.getColumn().setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/criterion_20.png").createImage()); //$NON-NLS-1$ //$NON-NLS-2$
		
		_tvcAlternative = new TableViewerColumn(_viewer, SWT.NONE);
		_tvcAlternative.getColumn().setText(Messages.ElementAssignmentView_Alternative);
		_tvcAlternative.setLabelProvider(new AlternativeLabelProvider());
		_tvcAlternative.getColumn().setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/alternative_20.png").createImage()); //$NON-NLS-1$ //$NON-NLS-2$
		
		_tvcDomain = new TableViewerColumn(_viewer, SWT.NONE);
		_tvcDomain.getColumn().setText(Messages.ElementAssignmentView_Domain);
		_tvcDomain.setLabelProvider(new DomainLabelProvider());
		_tvcDomain.getColumn().setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/domain_id.png").createImage()); //$NON-NLS-1$ //$NON-NLS-2$
		
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
		
		Collections.sort(_domainAssignments, new DataComparator());
		
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
			if(a[0].equals(assignment[0]) && a[1].equals(assignment[1])) { 
					int index = _domainAssignments.indexOf(a);
					_domainAssignments.remove(a);
					_domainAssignments.add(index, assignment);
					
					return true;
				
			}
		}
		return false;
	}
}
