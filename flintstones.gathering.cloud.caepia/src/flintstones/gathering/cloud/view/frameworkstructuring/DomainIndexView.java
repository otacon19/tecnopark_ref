package flintstones.gathering.cloud.view.frameworkstructuring;

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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import flintstones.gathering.cloud.model.Problem;
import mcdacw.valuation.domain.Domain;

public class DomainIndexView extends ViewPart {

	public static final String ID = "flintstones.gathering.cloud.view.domainindex"; //$NON-NLS-1$
	
	private TableViewer _viewer;
	
	private TableViewerColumn _id;
	private TableViewerColumn _type;
	
	private Problem _problem;
	
	@SuppressWarnings("serial")
	class ViewContentProvider implements IStructuredContentProvider {

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object parent) {
			return ((List<Domain>) parent).toArray(new Domain[0]);
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
	class IdLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			return ((Domain) obj).getName();
		}
	}

	@SuppressWarnings("serial")
	class TypeLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			return ((Domain) obj).getType();
		}
	}
	
	@SuppressWarnings("serial")
	@Override
	public void createPartControl(Composite parent) {
		_viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		_viewer.setContentProvider(new ViewContentProvider());
		_viewer.setLabelProvider(new ViewLabelProvider());
		
		Table table = _viewer.getTable();
		table.setLinesVisible(true);
		table.setVisible(true);
		table.setHeaderVisible(true);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem ti = (TableItem) e.item;
				String id = ti.getText();
			}
		});
		
		_id = new TableViewerColumn(_viewer, SWT.NONE);
		_id.getColumn().setText("Id");
		_id.getColumn().setWidth(145);
		_id.setLabelProvider(new IdLabelProvider());
		
		_type = new TableViewerColumn(_viewer, SWT.NONE);
		_type.getColumn().setText("Type");
		_type.getColumn().setWidth(80);
		_type.setLabelProvider(new TypeLabelProvider());
		
		_problem = (Problem) RWT.getUISession().getAttribute("problem");
		
		Map<String, Domain> domains = _problem.getDomains();
		List<Domain> ds = new LinkedList<Domain>();
		for(String id: domains.keySet()) {
			ds.add(domains.get(id));
		}
		_viewer.setInput(ds);
	}

	@Override
	public void setFocus() {
		_viewer.getTable().setFocus();
	}

}
