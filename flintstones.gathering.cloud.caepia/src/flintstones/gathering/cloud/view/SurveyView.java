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
import flintstones.gathering.cloud.model.Key;
import flintstones.gathering.cloud.model.KeyDomainAssignment;
import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.model.ProblemAssignment;
import flintstones.gathering.cloud.model.User;
import flintstones.gathering.cloud.model.Valuations;
import mcdacw.valuation.domain.Domain;
import mcdacw.valuation.valuation.Valuation;

public class SurveyView extends ViewPart {

	public static final String ID = "flintstones.gathering.cloud.view.surveyView";

	private TableViewer _viewer;
	private TableItem _valuationSelected;
	private Button _sendAssignmets;
	
	private ValuationView _valuationView;
	
	private Problem _problem;
	private Valuations _valuations;
	private ProblemAssignment _problemAssignment;

	public SurveyView() {
		
		_problem = (Problem) RWT.getUISession().getAttribute("valuation-problem");
	
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
	public void createPartControl(final Composite parent) {
		parent.setLayout(new GridLayout(1, true));
		
		Composite viewerComposite = new Composite(parent, SWT.NONE);
		viewerComposite.setLayout(new GridLayout(1, true));
		viewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		_viewer = new TableViewer(viewerComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		_viewer.setContentProvider(new ViewContentProvider());
		_viewer.setLabelProvider(new ViewLabelProvider());
		
		final Table table = _viewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setLinesVisible(true);
		table.setVisible(true);
		table.setHeaderVisible(true);
		
		TableViewerColumn tc_criterion = new TableViewerColumn(_viewer, SWT.NONE);
		tc_criterion.getColumn().setText("Criterio");
		tc_criterion.setLabelProvider(new CriterionLabelProvider());
		
		TableViewerColumn tc_alternative = new TableViewerColumn(_viewer, SWT.NONE);
		tc_alternative.getColumn().setText("Alternativa");
		tc_alternative.setLabelProvider(new AlternativeLabelProvider());
		
		TableViewerColumn tc_valuation = new TableViewerColumn(_viewer, SWT.NONE);
		tc_valuation.getColumn().setText("Valoración");
		tc_valuation.setLabelProvider(new ValuationLabelProvider());
		
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
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
		
		Composite buttonComposite = new Composite(parent, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(1, true));
		buttonComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.RIGHT, true, false, 1, 1));
		_sendAssignmets = new Button(buttonComposite, SWT.BORDER);
		_sendAssignmets.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		_sendAssignmets.setText("Enviar");
		_sendAssignmets.setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/mail_20.png").createImage());
		_sendAssignmets.setEnabled(false);
		_sendAssignmets.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				_problemAssignment.setMake(true);
				DAOProblemAssignments.getDAO().makeAssignment(_problem, _problemAssignment);
			}
		});
		
		if(_problem != null) {
			setModel();
		}
	}

	private void setModel() {
		User user = (User) RWT.getUISession().getAttribute("user");
		
		Map<Problem, ProblemAssignment> model = DAOProblemAssignments.getDAO().getUserProblemAssignments(user);
		for(Problem p: model.keySet()) {
			if(p.getId().equals(_problem.getId())) {
				_problemAssignment = model.get(p);
			}
		}
		
		if(_problemAssignment != null) {
	
			_valuations = _problemAssignment.getValuations();
	
			List<String[]> input = new LinkedList<String[]>();
			for(String a: _problem.getAlternatives()) {
				for(String c: _problem.getCriteria()) {
					String[] values = new String[3];
					values[0] = a;
					values[1] = c;
					if(_valuations != null) {
						Valuation v = _valuations.getValuation(new Key(a, c));
						if(v != null) {
							values[2] = v.changeFormatValuationToString(); 
						} else {
							values[2] = "No asignada";
						}
					} else {
						values[2] = "No asignada";
					}
					input.add(values);
				}
			}
			_viewer.setInput(input);
			_viewer.refresh();
		}
	}

	@Override
	public void setFocus() {
		_viewer.getTable().setFocus();
	}

	public void refresh() {
		_problem = (Problem) RWT.getUISession().getAttribute("valuation-problem");
		setModel();
		_viewer.refresh();
		
		checkMakeAssignment();
	}

	public void addValuation(Valuation valuation) {
		Key key = new Key(_valuationSelected.getText(1), _valuationSelected.getText(0));
		_valuations.getValuations().put(key, valuation);
		_problemAssignment.setValuations(_valuations);
		DAOValuations.getDAO().insertValuation(_problem, _problemAssignment, key, valuation);
		
		_valuationSelected.setText(2, valuation.changeFormatValuationToString());
		
		checkMakeAssignment();	
		
	}
	
	private void checkMakeAssignment() {
		if(_valuations.getValuations().size() == _viewer.getTable().getItemCount()) {
			_sendAssignmets.setEnabled(true);
		} else {
			_sendAssignmets.setEnabled(false);
		}
	}

	public void removeValuation(Valuation valuation) {
		KeyDomainAssignment key = new KeyDomainAssignment(_valuationSelected.getText(1), _valuationSelected.getText(0), _problemAssignment.getId());
		_valuations.getValuations().remove(key);
		_problemAssignment.setValuations(_valuations);
		DAOValuations.getDAO().removeValuation(_problem.getId(), key);
		
		_valuationSelected.setText(2, "No asignada");
	}
}
