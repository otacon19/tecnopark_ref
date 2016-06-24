package flintstones.gathering.cloud.view.frameworkstructuring;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

public class ElementAssignmentView extends ViewPart {

	public static final String ID = "flintstones.gathering.cloud.view.elementassignments"; //$NON-NLS-1$

	private String _partName = null;
	private Composite _container;
	private TabFolder _tabFolder = null;
	
	private ElementAssignmentView _instance;
	
	@Override
	public void createPartControl(Composite parent) {
		_instance = this;
		_partName = getPartName();
		_container = parent;
		_container.setLayout(new FillLayout());
		
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
	
	private void setModel() {
		
		if (_tabFolder != null) {
			for (TabItem tabItem : _tabFolder.getItems()) {
				tabItem.getControl().dispose();
			}
			_tabFolder.dispose();
			_tabFolder = null;
		}
		if (_selectedElement != null) {
			List<ProblemElement> toDisplay = new LinkedList<ProblemElement>();
			if (_selectedElement instanceof Expert) {
				List<Expert> nextLevelExperts = new LinkedList<Expert>();
				nextLevelExperts.add((Expert) _selectedElement);
				do {
					List<Expert> aux = new LinkedList<Expert>(nextLevelExperts);
					nextLevelExperts = new LinkedList<Expert>();
					for (Expert expert : aux) {
						if (expert.hasChildren()) {
							nextLevelExperts.addAll(expert.getChildren());
						} else {
							toDisplay.add(expert);
						}
					}
				} while (!nextLevelExperts.isEmpty());
			} else if (_selectedElement instanceof Alternative) {
				toDisplay.add(_selectedElement);

			} else if (_selectedElement instanceof Criterion) {
				List<Criterion> nextLevelCriteria = new LinkedList<Criterion>();
				nextLevelCriteria.add((Criterion) _selectedElement);
				do {
					List<Criterion> aux = new LinkedList<Criterion>(
							nextLevelCriteria);
					nextLevelCriteria = new LinkedList<Criterion>();
					for (Criterion criterion : aux) {
						if (criterion.hasSubcriteria()) {
							nextLevelCriteria
									.addAll(criterion.getSubcriteria());
						} else {
							toDisplay.add(criterion);
						}
					}
				} while (!nextLevelCriteria.isEmpty());

			}
			_tabFolder = new TabFolder(_container, SWT.BORDER);
			for (ProblemElement element : toDisplay) {
				TabItem tabItem = new TabItem(_tabFolder, SWT.NULL);
				tabItem.setText(element.getCanonicalId());
				ElementAssignmentsTable elementAssignmentsTable = new ElementAssignmentsTable(_tabFolder);
				tabItem.setControl(elementAssignmentsTable);
				elementAssignmentsTable.setModel(element);
			}
			if (_selectedElement instanceof Alternative) {
				_instance.setPartName(_partName + " | " + Messages.ElementAssignmentsView_Alternative); //$NON-NLS-1$
			} else if (_selectedElement instanceof Criterion) {
				_instance.setPartName(_partName + " | " + Messages.ElementAssignmentsView_Criterion); //$NON-NLS-1$
			} else {
				_instance.setPartName(_partName + " | " + Messages.ElementAssignmentsView_Expert); //$NON-NLS-1$

			}
		} else {
			_instance.setPartName(_partName);
			forceSelection();
		}
	}

	
	private void forceSelection() {
		
		if(_elementView == null) {
			for(IViewReference reference: getSite().getPage().getViewReferences()) {
				if(reference.getId().equals(ElementView.ID)) {
					_elementView = (ElementView) reference.getView(true);
				}
			}
		} else {
			StructuredSelection selection = (StructuredSelection) _elementView.getSelection();
			if(selection != null) {
				if(!selection.isEmpty()) {
					setElementSelected(selection);
					setModel();
				}
			}
		}
		
	}
	
	private void setElementSelected(StructuredSelection selection) {
		ProblemElement element = null;
		
		if(!selection.isEmpty()) {
			element = (ProblemElement) selection.getFirstElement();
		}
		_selectedElement = element;
	}


	@Override
	public void setFocus() {}

	
}
