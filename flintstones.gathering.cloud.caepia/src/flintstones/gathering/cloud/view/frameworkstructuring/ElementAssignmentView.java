package flintstones.gathering.cloud.view.frameworkstructuring;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import flintstones.gathering.cloud.model.Problem;

public class ElementAssignmentView extends ViewPart implements ISelectionListener {

	public static final String ID = "flintstones.gathering.cloud.view.elementassignments"; //$NON-NLS-1$

	private String _partName = null;
	private String _selectedElement;
	
	private Composite _container;
	private TabFolder _tabFolder = null;
	
	private Problem _problem;
	
	private ElementAssignmentView _instance;
	
	
	public ElementAssignmentView() {}
	
	@Override
	public void createPartControl(Composite parent) {
		_instance = this;
		
		_partName = getPartName();
		_container = parent;
		_container.setLayout(new FillLayout());
		
		_problem = (Problem) RWT.getUISession().getAttribute("problem");
		
		setModel();
	}

	
	@Override
	public void dispose() {
		
		if(_tabFolder != null) {
			if(!_tabFolder.isDisposed()) {
				for(TabItem item: _tabFolder.getItems()) {
					item.getControl().dispose();
				}
			}
			_tabFolder.dispose();
			_tabFolder = null;
		}

		super.dispose();
	}
	
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		setElementSelected((StructuredSelection) selection);
		setModel();
	}
	
	private void setModel() {
		
		if (_tabFolder != null) {
			for (TabItem tabItem : _tabFolder.getItems()) {
				tabItem.getControl().dispose();
			}
			_tabFolder.dispose();
			_tabFolder = null;
		}
		if (_selectedElement != null) {
			List<String> toDisplay = new LinkedList<String>();
			toDisplay.add(_selectedElement);
			
			_tabFolder = new TabFolder(_container, SWT.BORDER);
			for (String element : toDisplay) {
				TabItem tabItem = new TabItem(_tabFolder, SWT.NULL);
				tabItem.setText(element);
				ElementAssignmentTable elementAssignmentsTable = new ElementAssignmentTable(_tabFolder);
				tabItem.setControl(elementAssignmentsTable);
				elementAssignmentsTable.setModel(element);
			}
			
			if (_problem.isAlternative(_selectedElement)) {
				_instance.setPartName(_partName + " | " + "alternative"); //$NON-NLS-1$
			} else if (_problem.isCriterion(_selectedElement)) {
				_instance.setPartName(_partName + " | " + "criterion"); //$NON-NLS-1$
			} else {
				_instance.setPartName(_partName + " | " + "expert"); //$NON-NLS-1$

			}
		} else {
			_instance.setPartName(_partName);
		}
	}

	private void setElementSelected(StructuredSelection selection) {
		String element = null;
		
		if(!selection.isEmpty()) {
			element = (String) selection.getFirstElement();
		}
		_selectedElement = element;
	}


	@Override
	public void setFocus() {}
	
}
