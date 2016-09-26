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
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.nls.Messages;
import mcdacw.valuation.domain.Domain;

public class DomainIndexView extends ViewPart {

	public static final String ID = "flintstones.gathering.cloud.view.domainindex"; //$NON-NLS-1$
	
	private TableViewer _viewer;
	
	private TableViewerColumn _id;
	private TableViewerColumn _type;
	
	private List<ISelectedDomain> _listeners;
	
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
	public void createPartControl(final Composite parent) {
		_listeners = new LinkedList<ISelectedDomain>();
		
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
				notifyListeners(ti.getText());
			}
		});
		
		_id = new TableViewerColumn(_viewer, SWT.NONE);
		_id.getColumn().setText("Id"); //$NON-NLS-1$
		_id.setLabelProvider(new IdLabelProvider());
		_id.getColumn().setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/domain_id.png").createImage()); //$NON-NLS-1$ //$NON-NLS-2$
		
		_type = new TableViewerColumn(_viewer, SWT.NONE);
		_type.getColumn().setText(Messages.DomainIndexView_Type);
		_type.setLabelProvider(new TypeLabelProvider());
		_type.getColumn().setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/element.png").createImage()); //$NON-NLS-1$ //$NON-NLS-2$
		
		_problem = (Problem) RWT.getUISession().getAttribute("valuation-problem"); //$NON-NLS-1$
		
		List<Domain> ds = new LinkedList<Domain>();
		
		if(_problem != null) {
			Map<String, Domain> domains = _problem.getDomains();
			for(String id: domains.keySet()) {
				if(!id.equals("auto_generated_importance") && !id.equals("auto_generated_knowledge")) { //$NON-NLS-1$ //$NON-NLS-2$
					ds.add(domains.get(id));
				}
			}
		}
		
		_viewer.setInput(ds);
		
		parent.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle area = parent.getClientArea();
				Point preferredSize = _viewer.getTable().computeSize(SWT.DEFAULT, SWT.DEFAULT);
				int width = area.width - 2 * _viewer.getTable().getBorderWidth();
				if (preferredSize.y > area.height + _viewer.getTable().getHeaderHeight()) {
					Point vBarSize = _viewer.getTable().getVerticalBar().getSize();
					width -= vBarSize.x;
				}
				
				if(_viewer.getTable().getItemCount() == 0) {
					_viewer.getTable().getColumn(0).setWidth(parent.getSize().x / 2 - 1);
					_viewer.getTable().getColumn(1).setWidth(parent.getSize().x / 2 - 1);
					_viewer.getTable().setSize(area.width, area.height);
				} else {
					Point oldSize = _viewer.getTable().getSize();
					if (oldSize.x > area.width) {
						_viewer.getTable().getColumn(0).pack();
						_viewer.getTable().getColumn(1).setWidth(width - _viewer.getTable().getColumn(0).getWidth());
						_viewer.getTable().setSize(area.width, area.height);
					} else {
						_viewer.getTable().setSize(area.width, area.height);
						_viewer.getTable().getColumn(0).pack();
						_viewer.getTable().getColumn(1).setWidth(width - _viewer.getTable().getColumn(0).getWidth());
					}
				}
			}
		});
	}
	
	public void registerListener(ISelectedDomain listener) {
		_listeners.add(listener);
	}
	
	public void removeListener(ISelectedDomain listener) {
		_listeners.remove(listener);
	}
	
	private void notifyListeners(String id) {
		for(ISelectedDomain listener: _listeners) {
			listener.notifySelectedDomain(id);
		}
	}

	@Override
	public void setFocus() {
		_viewer.getTable().setFocus();
	}

}
