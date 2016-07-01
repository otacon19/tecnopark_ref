package flintstones.gathering.cloud.view.frameworkstructuring;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISizeProvider;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.dao.DAOProblemDomainAssignments;
import flintstones.gathering.cloud.model.Key;
import flintstones.gathering.cloud.model.Problem;

public class DomainAssignmentView extends ViewPart {

	public static final String ID = "flintstones.gathering.cloud.view.domainassignments";

	private Composite _container;
	private List<String> _domainValues;
	private List<String>  _expertValues;
	private List<String>  _alternativeValues;
	private List<String> _criterionValues;
	private Button _applyButton;
	private Combo _domainCombo;
	private Combo _alternativeCombo;
	private Combo _criterionCombo;
	private Boolean _validElements;
	private Boolean _validDomains;
	
	private Problem _problem;
	
	private List<String[]> _domainAssignments;
	
	private List<IAssignmentDomain> _listeners;

	public DomainAssignmentView() {
		_domainAssignments = new LinkedList<String[]>();
		
		_problem = (Problem) RWT.getUISession().getAttribute("valuation-problem");
		
		_domainValues = new LinkedList<String>();
		_expertValues = new LinkedList<String>();
		_alternativeValues = new LinkedList<String>();
		_criterionValues = new LinkedList<String>();
		
		_listeners = new LinkedList<IAssignmentDomain>();
		
		if(_problem != null) {
			extractDomainValues();
			extractExpertValues();
			extractAlternativeValues();
			extractCriterionValues();
			setDomainAssignments();
		}

		_validElements = null;
		_validDomains = null;
	}

	@SuppressWarnings("serial")
	@Override
	public void createPartControl(Composite parent) {
		_container = parent;
		parent.setLayout(new GridLayout(9, false));

		Label label = new Label(parent, SWT.NONE);
		label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("Criterio");
		_criterionCombo = new Combo(parent, SWT.NONE);
		_criterionCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("Alternativa");
		_alternativeCombo = new Combo(parent, SWT.NONE);
		_alternativeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("Dominio");
		_domainCombo = new Combo(parent, SWT.NONE);
		_domainCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		_applyButton = new Button(parent, SWT.NONE);
		_applyButton.setText("Aplicar");
		_applyButton.setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/exit.png").createImage());
		_applyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Key key = new Key(_alternativeCombo.getItem(_alternativeCombo.getSelectionIndex()), _criterionCombo.getItem(_criterionCombo.getSelectionIndex()));
				Map<Key, String> domainAssignment = _problem.getDomainAssignments();
				if(domainAssignment == null) {
					domainAssignment = new HashMap<Key, String>();
				}
				domainAssignment.put(key, _domainCombo.getItem(_domainCombo.getSelectionIndex()));
	
				String[] assignment = new String[3];
				assignment[0] = key.getCriterion();
				assignment[1] = key.getAlternative();
				assignment[2] = _domainCombo.getItem(_domainCombo.getSelectionIndex());
				
				notifyListeners(assignment);
		
				Map<Key, String> assignmentDAO = new HashMap<Key, String>();
				assignmentDAO.put(key, _domainCombo.getItem(_domainCombo.getSelectionIndex()));
				DAOProblemDomainAssignments.getDAO().createProblemDomainAssignment(_problem , assignmentDAO);
			}
		});

		computeState(EComboChange.ALL);
	}
	
	@Override
	public void dispose() {
		_listeners.clear();
	}

	@Override
	public void setFocus() {}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		
		if (ISizeProvider.class == adapter) {
			return new ISizeProvider() {
				public int getSizeFlags(boolean width) {
					return SWT.MIN | SWT.MAX | SWT.FILL;
				}

				public int computePreferredSize(boolean width,
						int availableParallel, int availablePerpendicular,
						int preferredResult) {
					return width ? preferredResult : 43;
				}
			};
		}
		return super.getAdapter(adapter);
	}
	
	private void extractDomainValues() {
		for(String id: _problem.getDomains().keySet()) {
			_domainValues.add(id);
		}	
	}

	private void extractExpertValues() {
		for(String id: _problem.getExperts()) {
			_expertValues.add(id);
		}
	}

	private void extractAlternativeValues() {
		for(String id: _problem.getAlternatives()) {
			_alternativeValues.add(id);
		}
	}

	private void extractCriterionValues() {
		for(String id: _problem.getCriteria()) {
			_criterionValues.add(id);
		}
	}
	
	private void setDomainAssignments() {
		Map<Key, String> domainAssignments = _problem.getDomainAssignments();
		if(domainAssignments != null) {
			if(domainAssignments.size() > 0) {
				for(Key key: domainAssignments.keySet()) {
					String[] assignment = new String[3];
					assignment[0] = key.getCriterion();
					assignment[1] = key.getAlternative();
					assignment[2] = domainAssignments.get(key);
					_domainAssignments.add(assignment);
				}
			}
		}
	}
	
	public List<String[]> getDomainAssignments() {
		return _domainAssignments;
	}

	private void computeState(EComboChange change) {
		boolean oldEnabled;
		
		if ((_validDomains == null) || (_validElements == null)) {
			oldEnabled = true;
		} else {
			oldEnabled = _validDomains && _validElements;
		}

		boolean enabled = false, testElements = true, testDomains = true;

		if (EComboChange.DOMAINS.equals(change)) {
			testElements = false;
		} else if (EComboChange.ELEMENTS.equals(change)) {
			testDomains = false;
		}

		if (testDomains) {
			_validDomains = _domainValues.size() != 0;
		}

		if (testElements) {
			_validElements = ((_expertValues.size() != 1) && (_alternativeValues.size() != 1) && (_criterionValues.size() != 1));
		}

		if ((_validDomains == null) || (_validElements == null)) {
			enabled = false;
		} else {
			enabled = _validDomains && _validElements;
		}

		if (enabled != oldEnabled) {
			for (Control control : _container.getChildren()) {
				control.setEnabled(enabled);
			}
		}

		if (enabled) {
			_alternativeCombo.setItems(_alternativeValues.toArray(new String[0]));
			_criterionCombo.setItems(_criterionValues.toArray(new String[0]));
			_domainCombo.setItems(_domainValues.toArray(new String[0]));
			_alternativeCombo.select(0);
			_criterionCombo.select(0);
			_domainCombo.select(0);

		} else {
			_alternativeCombo.setItems(new String[] {});
			_criterionCombo.setItems(new String[] {});
			_domainCombo.setItems(new String[] {});
		}
	}
	
	public void registerListener(IAssignmentDomain listener) {
		_listeners.add(listener);
	}
	
	public void removeListener(IAssignmentDomain listener) {
		_listeners.remove(listener);
	}
	
	private void notifyListeners(String[] assignment) {
		for(IAssignmentDomain listener: _listeners) {
			listener.notifyAssignmentDomain(assignment);
		}
	}
}
