package flintstones.gathering.cloud.view;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.dao.DAOConfidence;
import flintstones.gathering.cloud.dao.DAOProblemAssignments;
import flintstones.gathering.cloud.model.KeyDomainAssignment;
import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.model.ProblemAssignment;
import flintstones.gathering.cloud.model.User;

import org.eclipse.swt.widgets.Table;

public class ConfidenceView extends ViewPart {

	public static final String ID = "flintstones.gathering.cloud.view.confidenceview";
	
	private Button _saveConfidenceButton;
	private TableViewer _viewer;
	
	private List<Spinner> _spinners;

	private SurveyView _surveyView;
	
	private Problem _problem;
	
	public ConfidenceView() {
		_problem = (Problem) RWT.getUISession().getAttribute("valuation-problem");
		
		_spinners = new LinkedList<Spinner>();
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
	class ConfidenceLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			return "";
		}
	}
	
	@SuppressWarnings("serial")
	@Override
	public void createPartControl(final Composite parent) {
		GridLayout layout = new GridLayout(1, false);
		layout.marginBottom = 0;
		layout.marginTop = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		parent.setLayout(layout);
		
		Composite compositeTable = new Composite(parent, SWT.NONE);
		GridLayout layout_2 = new GridLayout(1, false);
		layout.marginBottom = 0;
		layout.marginTop = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		compositeTable.setLayout(layout_2);
		compositeTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		_viewer = new TableViewer(compositeTable, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.NONE);
		Table table = _viewer.getTable();
		table.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		_viewer.setContentProvider(new ViewContentProvider());
		_viewer.getTable().setHeaderVisible(true);
		_viewer.getTable().setLinesVisible(true);
		
		TableViewerColumn criterion = new TableViewerColumn(_viewer, SWT.NONE);
		criterion.setLabelProvider(new CriterionLabelProvider());
		criterion.getColumn().setText("Criterio");
		criterion.getColumn().setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/criterion_20.png").createImage());
		criterion.getColumn().pack();
		
		TableViewerColumn alternative = new TableViewerColumn(_viewer, SWT.NONE);
		alternative.setLabelProvider(new AlternativeLabelProvider());
		alternative.getColumn().setText("Alternativa");
		alternative.getColumn().setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/alternative_20.png").createImage());
		alternative.getColumn().pack();
		
		TableViewerColumn confidence = new TableViewerColumn(_viewer, SWT.NONE);
		confidence.setLabelProvider(new ConfidenceLabelProvider());
		confidence.getColumn().setText("Confianza");
		confidence.getColumn().setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/balance_20.png").createImage());
		confidence.getColumn().pack();
		
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
					_viewer.getTable().getColumn(0).setWidth(parent.getSize().x / 3 - 1);
					_viewer.getTable().getColumn(1).setWidth(parent.getSize().x / 3 - 1);
					_viewer.getTable().getColumn(2).setWidth(parent.getSize().x / 3 - 1);
					_viewer.getTable().setSize(area.width, area.height);
				} else {
					Point oldSize = _viewer.getTable().getSize();
					if (oldSize.x > area.width) {
						_viewer.getTable().getColumn(0).pack();
						_viewer.getTable().getColumn(1).pack();
						_viewer.getTable().getColumn(2).setWidth(width - _viewer.getTable().getColumn(1).getWidth());
						_viewer.getTable().setSize(area.width, area.height);
					} else {
						_viewer.getTable().setSize(area.width, area.height);
						_viewer.getTable().getColumn(0).pack();
						_viewer.getTable().getColumn(1).pack();
						_viewer.getTable().getColumn(2).setWidth(width - _viewer.getTable().getColumn(1).getWidth());
					}
				}
			}
		});
		
		Composite compositeButton = new Composite(parent, SWT.NONE);
		compositeButton.setLayout(new GridLayout(1, false));
		compositeButton.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1));
		_saveConfidenceButton = new Button(compositeButton, SWT.NONE);
		_saveConfidenceButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1 ,1));
		_saveConfidenceButton.setText("Guardar");
		_saveConfidenceButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ETOOL_SAVE_EDIT));
		_saveConfidenceButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				_saveConfidenceButton.setEnabled(false);
				
				User user = (User) RWT.getUISession().getAttribute("user");
				Map<Problem, ProblemAssignment> problemsAssignment = DAOProblemAssignments.getDAO().getUserProblemAssignments(user);
				ProblemAssignment problemAssignment = null;
				for(Problem pr: problemsAssignment.keySet()) {
					if(pr.getId().equals(_problem.getId())) {
						problemAssignment = problemsAssignment.get(pr);
					}
				}
				
				if(problemAssignment != null) {
					for(Spinner s: _spinners) {
						String a = (String) s.getData("alternative");
						String c = (String) s.getData("criterion");
						double confidence = s.getSelection() / 100d;
						KeyDomainAssignment key = new KeyDomainAssignment(a, c, problemAssignment.getId());
						DAOConfidence.getDAO().insertConfidence(_problem, key, confidence);
					}
				}
				
				if(_surveyView == null) {
					IViewReference viewReferences[] = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();
					for (int i = 0; i < viewReferences.length; i++) {
						if (SurveyView.ID.equals(viewReferences[i].getId())) {
							_surveyView = (SurveyView) viewReferences[i].getView(false);
						}
					}
				}
				_surveyView.confidencesSaved();
			}
			
		});
		
		setInput();
	}
	
	@SuppressWarnings("serial")
	private void setInput() {
		List<String[]> input = new LinkedList<String[]>();
		
		if(_problem != null) {
			for(String a: _problem.getAlternatives()) {
				for(String c: _problem.getCriteria()) {
					String[] data = new String[3];
					data[0] = a;
					data[1] = c;
					input.add(data);
				}
			}
		}
		
		_viewer.setInput(input);
		
		User user = (User) RWT.getUISession().getAttribute("user");
		ProblemAssignment problemAssignment = null;
		Map<Problem, ProblemAssignment> model = DAOProblemAssignments.getDAO().getUserProblemAssignments(user);
		for(Problem p: model.keySet()) {
			if(p.getId().equals(_problem.getId())) {
				problemAssignment = model.get(p);
			}
		}
		
		Map<KeyDomainAssignment, Double> confidences = DAOConfidence.getDAO().getConfidencesExpert(_problem.getId(), problemAssignment.getId());
		double value;
		
		TableItem[] items = _viewer.getTable().getItems();
		for(int i = 0; i < items.length; ++i) {
			TableEditor editor = new TableEditor(_viewer.getTable());
			Spinner spinner = new Spinner(_viewer.getTable(), SWT.NONE);
			spinner.setDigits(2);
			spinner.setMinimum((int) (0 * 100d));
			spinner.setMaximum((int) (0.5 * 100d));
			spinner.pack();
			spinner.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if(!_saveConfidenceButton.isEnabled()) {
						_saveConfidenceButton.setEnabled(true);
					}
				}
			});
			
			TableItem item = items[i];
			String criterion = item.getText(0);
			String alternative = item.getText(1);
			
			if(!confidences.isEmpty()) {
				value = confidences.get(new KeyDomainAssignment(alternative, criterion, problemAssignment.getId()));
				spinner.setSelection((int) (value * 100d));
			} else {
				spinner.setSelection((int) (0.25 * 100d));
			}
			
			spinner.setData("criterion", criterion);
			spinner.setData("alternative", alternative);
			
		    editor.minimumWidth = spinner.getSize().x;
		    editor.horizontalAlignment = SWT.CENTER;
		    editor.setEditor(spinner, items[i], 2);
		    
		    _spinners.add(spinner);
		}
	}
	
	@Override
	public void setFocus() {
		_viewer.getTable().setFocus();
	}

}
