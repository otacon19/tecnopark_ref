package flintstones.gathering.cloud.view;

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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.dao.DAOProblemAssignments;
import flintstones.gathering.cloud.dao.DAOProblemDomainAssignments;
import flintstones.gathering.cloud.dao.DAOValuations;
import flintstones.gathering.cloud.model.KeyDomainAssignment;
import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.model.ProblemAssignment;
import flintstones.gathering.cloud.model.User;
import flintstones.gathering.cloud.model.Valuations;
import flintstones.gathering.cloud.nls.Messages;
import mcdacw.valuation.domain.Domain;
import mcdacw.valuation.valuation.Valuation;

public class SurveyView extends ViewPart {
	
	public static final String ID = "flintstones.gathering.cloud.view.surveyView"; //$NON-NLS-1$

	private TableViewer _assignmentsViewer;
	private TableViewer _importanceViewer;
	private TableViewer _thresholdViewer;
	private TabFolder _tabFolder;
	private TableItem _valuationSelected;
	private TableItem _importanceSelected;
	private TableItem _thresholdSelected;
	private Button _sendAssignmets;
	private Composite _assignmentsComposite;
	private Composite _importanceComposite;
	private Composite _thresholdComposite;
	
	private ValuationView _valuationView;
	
	private Problem _problem;
	private Valuations _valuations;
	private ProblemAssignment _problemAssignment;

	public SurveyView() {
		
		_problem = (Problem) RWT.getUISession().getAttribute("valuation-problem"); //$NON-NLS-1$
		
		User user = (User) RWT.getUISession().getAttribute("user"); //$NON-NLS-1$
		Map<Problem, ProblemAssignment> model = DAOProblemAssignments.getDAO().getUserProblemAssignments(user);
		for(Problem p: model.keySet()) {
			if(p.getId().equals(_problem.getId())) {
				_problemAssignment = model.get(p);
			}
		}
		
		_valuations = new Valuations();
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
	class AlternativeLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			return ((String[]) obj)[0];
		}
	}
	
	@SuppressWarnings("serial")
	class CriterionLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			return ((String[]) obj)[1];	
		}
	}

	@SuppressWarnings("serial")
	class ValuationLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			return ((String[]) obj)[2];
		}
	}
	
	@SuppressWarnings("serial")
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		_tabFolder = new TabFolder(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		_tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		_tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(_valuationView != null) {
					_valuationView.dispose();
				}
			}
		});
		
		createAssignmentsComposite();
		createImportanceComposite();
		createThresholdComposite();
		
		Composite buttonComposite = new Composite(parent, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(1, true));
		buttonComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.RIGHT, true, false, 1, 1));
		_sendAssignmets = new Button(buttonComposite, SWT.BORDER);
		_sendAssignmets.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		_sendAssignmets.setText(Messages.SurveyView_Send);
		_sendAssignmets.setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/mail_20.png").createImage()); //$NON-NLS-1$ //$NON-NLS-2$
		_sendAssignmets.setEnabled(false);
		_sendAssignmets.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				_problemAssignment.setMake(true);
				DAOProblemAssignments.getDAO().makeAssignment(_problem, _problemAssignment);
				_sendAssignmets.setEnabled(false);
			}
		});
		
		if(_problem != null) {
			setModel();
		}
	}

	@SuppressWarnings("serial")
	private void createAssignmentsComposite() {
		_assignmentsComposite = new Composite(_tabFolder, SWT.NONE);
		_assignmentsComposite.setLayout(new GridLayout(1, false));
		
		Composite viewerComposite = new Composite(_assignmentsComposite, SWT.NONE);
		viewerComposite.setLayout(new GridLayout(1, true));
		viewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		_assignmentsViewer = new TableViewer(viewerComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		_assignmentsViewer.setContentProvider(new ViewContentProvider());
		_assignmentsViewer.setLabelProvider(new ViewLabelProvider());
		
		final Table table = _assignmentsViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setLinesVisible(true);
		table.setVisible(true);
		table.setHeaderVisible(true);
		
		TableViewerColumn tc_criterion = new TableViewerColumn(_assignmentsViewer, SWT.NONE);
		tc_criterion.getColumn().setText(Messages.SurveyView_Criterion);
		tc_criterion.setLabelProvider(new CriterionLabelProvider());
		tc_criterion.getColumn().setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/criterion_20.png").createImage()); //$NON-NLS-1$ //$NON-NLS-2$
		
		TableViewerColumn tc_alternative = new TableViewerColumn(_assignmentsViewer, SWT.NONE);
		tc_alternative.getColumn().setText(Messages.SurveyView_Alternative);
		tc_alternative.setLabelProvider(new AlternativeLabelProvider());
		tc_alternative.getColumn().setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/alternative_20.png").createImage()); //$NON-NLS-1$ //$NON-NLS-2$
		
		TableViewerColumn tc_valuation = new TableViewerColumn(_assignmentsViewer, SWT.NONE);
		tc_valuation.getColumn().setText(Messages.SurveyView_Valuation);
		tc_valuation.setLabelProvider(new ValuationLabelProvider());
		tc_valuation.getColumn().setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/valuation.png").createImage()); //$NON-NLS-1$ //$NON-NLS-2$
		
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				_importanceSelected = null;
				
				_valuationSelected = (TableItem) e.item;
				String criterion = _valuationSelected.getText(0);
				String alternative = _valuationSelected.getText(1);
				
				DAOProblemDomainAssignments daoProblemDomainAssignments = DAOProblemDomainAssignments.getDAO();
				Map<KeyDomainAssignment, String> domainAssignments = daoProblemDomainAssignments.getProblemDomainAssignments(_problem);
				
				KeyDomainAssignment key = new KeyDomainAssignment(alternative, criterion, _problemAssignment.getId());
				String idDomain = domainAssignments.get(key);
				Domain domain = _problem.getDomains().get(idDomain);
				
				if(_valuationView == null) {
					IViewReference viewReferences[] = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();
					for (int i = 0; i < viewReferences.length; i++) {
						if (ValuationView.ID.equals(viewReferences[i].getId())) {
							_valuationView = (ValuationView) viewReferences[i].getView(false);
						}
					}
				}
				_valuationView.setDomain(domain);
			}
		});
		
		TabItem tabItem = new TabItem(_tabFolder, SWT.NONE, 0);
		tabItem.setText(Messages.SurveyView_Assessments);
		tabItem.setControl(_assignmentsComposite);
		
		_assignmentsComposite.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle area = _assignmentsComposite.getClientArea();
				Point oldSize = _assignmentsViewer.getTable().getSize();
				if (oldSize.x > area.width) {
					_assignmentsViewer.getTable().getColumn(0).setWidth(_assignmentsComposite.getSize().x / 3);
					_assignmentsViewer.getTable().getColumn(1).setWidth(_assignmentsComposite.getSize().x / 3 - 1);
					_assignmentsViewer.getTable().getColumn(2).setWidth(_assignmentsComposite.getSize().x / 3 - 1);
					_assignmentsViewer.getTable().setSize(area.width, area.height);
				} else {
					_assignmentsViewer.getTable().setSize(area.width, area.height);
					_assignmentsViewer.getTable().getColumn(0).setWidth(_assignmentsComposite.getSize().x / 3);
					_assignmentsViewer.getTable().getColumn(1).setWidth(_assignmentsComposite.getSize().x / 3 - 1);
					_assignmentsViewer.getTable().getColumn(2).setWidth(_assignmentsComposite.getSize().x / 3 - 1);
				}
			}
		});
	}

	@SuppressWarnings("serial")
	private void createImportanceComposite() {
		_importanceComposite = new Composite(_tabFolder, SWT.NONE);
		_importanceComposite.setLayout(new GridLayout(1, true));
		
		Composite viewerComposite = new Composite(_importanceComposite, SWT.NONE);
		viewerComposite.setLayout(new GridLayout(1, true));
		viewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		_importanceViewer = new TableViewer(viewerComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		_importanceViewer.setContentProvider(new ViewContentProvider());
		_importanceViewer.setLabelProvider(new ViewLabelProvider());
		
		final Table table = _importanceViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setLinesVisible(true);
		table.setVisible(true);
		table.setHeaderVisible(true);
		
		TableViewerColumn tc_criterion = new TableViewerColumn(_importanceViewer, SWT.NONE);
		tc_criterion.getColumn().setText(Messages.SurveyView_Criterion);
		tc_criterion.setLabelProvider(new CriterionLabelProvider());
		tc_criterion.getColumn().setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/criterion_20.png").createImage()); //$NON-NLS-1$ //$NON-NLS-2$
		
		TableViewerColumn tc_valuation = new TableViewerColumn(_importanceViewer, SWT.NONE);
		tc_valuation.getColumn().setText(Messages.SurveyView_Valuation);
		tc_valuation.setLabelProvider(new ValuationLabelProvider());
		tc_valuation.getColumn().setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/valuation.png").createImage()); //$NON-NLS-1$ //$NON-NLS-2$
		
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				_valuationSelected = null;
				_thresholdSelected = null;
				
				_importanceSelected = (TableItem) e.item;
				
				if(_valuationView == null) {
					IViewReference viewReferences[] = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();
					for (int i = 0; i < viewReferences.length; i++) {
						if (ValuationView.ID.equals(viewReferences[i].getId())) {
							_valuationView = (ValuationView) viewReferences[i].getView(false);
						}
					}
				}
				
				Domain domain = _problem.getDomains().get("auto_generated_importance"); //$NON-NLS-1$
				_valuationView.setDomain(domain);
			}
		});
		
		_importanceComposite.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle area = _importanceComposite.getClientArea();
				Point oldSize = _importanceViewer.getTable().getSize();
				if (oldSize.x > area.width) {
					_importanceViewer.getTable().getColumn(0).setWidth(_importanceComposite.getSize().x / 2 - 1);
					_importanceViewer.getTable().getColumn(1).setWidth(_importanceComposite.getSize().x / 2 - 1);
					_importanceViewer.getTable().setSize(area.width, area.height);
				} else {
					_importanceViewer.getTable().setSize(area.width, area.height);
					_importanceViewer.getTable().getColumn(0).setWidth(_importanceComposite.getSize().x / 2 - 1);
					_importanceViewer.getTable().getColumn(1).setWidth(_importanceComposite.getSize().x / 2 - 1);
				}
			}
		});
		
		TabItem tabItem = new TabItem(_tabFolder, SWT.NONE, 1);
		tabItem.setText(Messages.SurveyView_Importance);
		tabItem.setControl(_importanceComposite);
	}
	
	@SuppressWarnings("serial")
	private void createThresholdComposite() {
		_thresholdComposite = new Composite(_tabFolder, SWT.NONE);
		_thresholdComposite.setLayout(new GridLayout(1, true));
		
		Composite viewerComposite = new Composite(_thresholdComposite, SWT.NONE);
		viewerComposite.setLayout(new GridLayout(1, true));
		viewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		_thresholdViewer = new TableViewer(viewerComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		_thresholdViewer.setContentProvider(new ViewContentProvider());
		_thresholdViewer.setLabelProvider(new ViewLabelProvider());
		
		final Table table = _thresholdViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setLinesVisible(true);
		table.setVisible(true);
		table.setHeaderVisible(true);
		
		TableViewerColumn tc_criterion = new TableViewerColumn(_thresholdViewer, SWT.NONE);
		tc_criterion.getColumn().setText(Messages.SurveyView_Criterion);
		tc_criterion.setLabelProvider(new CriterionLabelProvider());
		tc_criterion.getColumn().setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/criterion_20.png").createImage()); //$NON-NLS-1$ //$NON-NLS-2$
		
		TableViewerColumn tc_valuation = new TableViewerColumn(_thresholdViewer, SWT.NONE);
		tc_valuation.getColumn().setText(Messages.SurveyView_Valuation);
		tc_valuation.setLabelProvider(new ValuationLabelProvider());
		tc_valuation.getColumn().setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/valuation.png").createImage()); //$NON-NLS-1$ //$NON-NLS-2$
		
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				_valuationSelected = null;
				_importanceSelected = null;
				
				_thresholdSelected = (TableItem) e.item;
				
				if(_valuationView == null) {
					IViewReference viewReferences[] = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();
					for (int i = 0; i < viewReferences.length; i++) {
						if (ValuationView.ID.equals(viewReferences[i].getId())) {
							_valuationView = (ValuationView) viewReferences[i].getView(false);
						}
					}
				}
				
				Domain domain = _problem.getDomains().get("auto_generated_knowledge"); //$NON-NLS-1$
				_valuationView.setDomain(domain);
			}
		});
		
		_thresholdComposite.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle area = _thresholdComposite.getClientArea();
				Point oldSize = _thresholdViewer.getTable().getSize();
				if (oldSize.x > area.width) {
					_thresholdViewer.getTable().getColumn(0).setWidth(_thresholdComposite.getSize().x / 2 - 1);
					_thresholdViewer.getTable().getColumn(1).setWidth(_thresholdComposite.getSize().x / 2 - 1);
					_thresholdViewer.getTable().setSize(area.width, area.height);
				} else {
					_thresholdViewer.getTable().setSize(area.width, area.height);
					_thresholdViewer.getTable().getColumn(0).setWidth(_thresholdComposite.getSize().x / 2 - 1);
					_thresholdViewer.getTable().getColumn(1).setWidth(_thresholdComposite.getSize().x / 2 - 1);
				}
			}
		});
		
		TabItem tabItem = new TabItem(_tabFolder, SWT.NONE, 2);
		tabItem.setText(Messages.SurveyView_Knowledge);
		tabItem.setControl(_thresholdComposite);
	}
	
	private void setModel() {
		
		if(_problemAssignment != null) {
	
			_valuations = _problemAssignment.getValuations();
	
			List<String[]> input = new LinkedList<String[]>();
			for(String a: _problem.getAlternatives()) {
				//TODO
				if(!a.equals("criterion_importance") && !a.equals("expert_knowledge")) {
					for(String c: _problem.getCriteria()) {
						String[] values = new String[3];
						values[0] = a;
						values[1] = c;
						if(_valuations != null) {
							Valuation v = _valuations.getValuation(new KeyDomainAssignment(a, c, _problemAssignment.getId()));
							if(v != null) {
								values[2] = v.changeFormatValuationToString(); 
							} else {
								values[2] = Messages.SurveyView_Unassigned;
							}
						} else {
							values[2] = Messages.SurveyView_Unassigned;
						}
						input.add(values);
					}
				}
			}
			_assignmentsViewer.setInput(input);
			_assignmentsViewer.refresh();
			
			input = new LinkedList<String[]>();
			for(String c: _problem.getCriteria()) {
				String[] values = new String[3];
				values[1] = c;
				
				if(_valuations != null) {
					Valuation v = _valuations.getValuation(new KeyDomainAssignment("null_importance", c, _problemAssignment.getId())); //$NON-NLS-1$
					if(v != null) {
						values[2] = v.changeFormatValuationToString(); 
					} else {
						values[2] = Messages.SurveyView_Unassigned;
					}
				} else {
					values[2] = Messages.SurveyView_Unassigned;
				}
				input.add(values);
			}
			
			_importanceViewer.setInput(input);
			_importanceViewer.refresh();
			
			input = new LinkedList<String[]>();
			for(String c: _problem.getCriteria()) {
				String[] values = new String[3];
				values[1] = c;
				
				if(_valuations != null) {
					Valuation v = _valuations.getValuation(new KeyDomainAssignment("null_threshold", c, _problemAssignment.getId())); //$NON-NLS-1$
					if(v != null) {
						values[2] = v.changeFormatValuationToString(); 
					} else {
						values[2] = Messages.SurveyView_Unassigned;
					}
				} else {
					values[2] = Messages.SurveyView_Unassigned;
				}
				input.add(values);
			}
			
			_thresholdViewer.setInput(input);
			_thresholdViewer.refresh();
		}
	}

	@Override
	public void setFocus() {
		_assignmentsViewer.getTable().setFocus();
	}

	public void refresh() {
		_problem = (Problem) RWT.getUISession().getAttribute("valuation-problem"); //$NON-NLS-1$
		if(_problem != null) {
			setModel();
		}
		_assignmentsViewer.refresh();
		
		checkMakeAssignment();
	}

	public void addValuation(Valuation valuation) {
		if(_valuationSelected != null) {
			KeyDomainAssignment key = new KeyDomainAssignment(_valuationSelected.getText(1), _valuationSelected.getText(0), _problemAssignment.getId());
			_valuations.getValuations().put(key, valuation);
			_problemAssignment.setValuations(_valuations);
			DAOValuations.getDAO().insertValuation(_problem, key, valuation);
			
			_valuationSelected.setText(2, valuation.changeFormatValuationToString());
		} else if(_importanceSelected != null) {
			KeyDomainAssignment key = new KeyDomainAssignment("null_importance", _importanceSelected.getText(0), _problemAssignment.getId()); //$NON-NLS-1$
			_valuations.getValuations().put(key, valuation);
			_problemAssignment.setValuations(_valuations);
			DAOValuations.getDAO().insertValuation(_problem, key, valuation);
			
			_importanceSelected.setText(1, valuation.changeFormatValuationToString());
		} else if(_thresholdSelected != null) {
			KeyDomainAssignment key = new KeyDomainAssignment("null_threshold", _thresholdSelected.getText(0), _problemAssignment.getId()); //$NON-NLS-1$
			_valuations.getValuations().put(key, valuation);
			_problemAssignment.setValuations(_valuations);
			DAOValuations.getDAO().insertValuation(_problem, key, valuation);
			
			_thresholdSelected.setText(1, valuation.changeFormatValuationToString());
		}
 		
		checkMakeAssignment();		
	}
	
	private void checkMakeAssignment() {
		if((_valuations.getValuations().size() == _assignmentsViewer.getTable().getItemCount() + _importanceViewer.getTable().getItemCount() + _thresholdViewer.getTable().getItemCount()) 
				&& _assignmentsViewer.getTable().getItemCount() > 0 && _importanceViewer.getTable().getItemCount() > 0 
				&& _thresholdViewer.getTable().getItemCount() > 0 && !_problemAssignment.getMake()) {
			_sendAssignmets.setEnabled(true);
		} else {
			_sendAssignmets.setEnabled(false);
		}
	}

	public void removeValuation(Valuation valuation) {
		if(_valuationSelected != null) {
			KeyDomainAssignment key = new KeyDomainAssignment(_valuationSelected.getText(1), _valuationSelected.getText(0), _problemAssignment.getId());
			_valuations.getValuations().remove(key);
			_problemAssignment.setValuations(_valuations);
			DAOValuations.getDAO().removeValuation(_problem.getId(), key);
			
			_valuationSelected.setText(2, Messages.SurveyView_Unassigned);
		} else if(_importanceSelected != null) {
			KeyDomainAssignment key = new KeyDomainAssignment("null_importance", _importanceSelected.getText(0), _problemAssignment.getId()); //$NON-NLS-1$
			_valuations.getValuations().remove(key);
			_problemAssignment.setValuations(_valuations);
			DAOValuations.getDAO().removeValuation(_problem.getId(), key);
			
			_importanceSelected.setText(1, Messages.SurveyView_Unassigned);
		} else if(_thresholdSelected != null) {
			KeyDomainAssignment key = new KeyDomainAssignment("null_threshold", _thresholdSelected.getText(0), _problemAssignment.getId()); //$NON-NLS-1$
			_valuations.getValuations().remove(key);
			_problemAssignment.setValuations(_valuations);
			DAOValuations.getDAO().removeValuation(_problem.getId(), key);
			
			_thresholdSelected.setText(1, Messages.SurveyView_Unassigned);
		}
	}
}
