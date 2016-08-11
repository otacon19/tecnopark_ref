package flintstones.gathering.cloud.view;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wb.swt.SWTResourceManager;

import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.xml.XMLValues;
import mcdacw.valuation.domain.Domain;
import mcdacw.valuation.domain.DomainChart;
import mcdacw.valuation.domain.fuzzyset.FuzzySet;
import mcdacw.valuation.domain.fuzzyset.jfreechart.LinguisticDomainChart;
import mcdacw.valuation.domain.fuzzyset.label.LabelLinguisticDomain;
import mcdacw.valuation.domain.numeric.NumericIntegerDomain;
import mcdacw.valuation.domain.numeric.NumericRealDomain;
import mcdacw.valuation.domain.numeric.jfreechart.NumericIntegerDomainChart;
import mcdacw.valuation.domain.numeric.jfreechart.NumericRealDomainChart;
import mcdacw.valuation.valuation.IntegerIntervalValuation;
import mcdacw.valuation.valuation.IntegerValuation;
import mcdacw.valuation.valuation.LinguisticValuation;
import mcdacw.valuation.valuation.RealIntervalValuation;
import mcdacw.valuation.valuation.RealValuation;
import mcdacw.valuation.valuation.Valuation;
import mcdacw.valuation.valuation.hesitant.EUnaryRelationType;
import mcdacw.valuation.valuation.hesitant.HesitantValuation;

public class ValuationView extends ViewPart {
	
	public static final String ID = "flintstones.gathering.cloud.view.valuationview";

	private Spinner _valueSpinner;
	private Spinner _valueSpinnerMax;
	private Spinner _valueSpinnerMin;
	private Combo _hesitantEvaluationCombo1;
	private Combo _hesitantEvaluationCombo2;
	private Button _removeButton;
	private Button _valuateButton;
	private Button _primaryButton;
	private Button _compositeButton;
	private Button _unaryRelationshipButton;
	private Button _binaryRelationshipButton;
	private Composite _parent;
	private Composite _valuationComposite;
	private Composite _chartComposite;
	private Composite _buttonsPart;
	private Composite _hesitantRelationshipComposite;
	private Composite _hesitantValueComposite;
	
	private Label _betweenLabel;
	private Label _andLabel;
	
	private DomainChart _chart;
	
	private double _value;
	private Object _valueMax;
	private Object _valueMin;
	private List<Integer> _selectIndexes;
	private List<Integer> _binaryIndexes;
	private Pair<EUnaryRelationType, LabelLinguisticDomain>_unaryIndexes;
	
	private ModifyListener _hesitantEvaluationCombo1ModifyListener;
	private ModifyListener _hesitantEvaluationCombo2ModifyListener;
	
	private Domain _domain;
	private LabelLinguisticDomain _label;
	private LabelLinguisticDomain _upperTerm;
	private LabelLinguisticDomain _lowerTerm;
	private LabelLinguisticDomain _term;
	
	private EUnaryRelationType _unaryRelation;
	
	private Valuation _valuation;
	
	private Problem _problem;
	
	private SurveyView _surveyView;
	
	public ValuationView() {
		_problem = (Problem) RWT.getUISession().getAttribute("valuation-problem");
		
		_surveyView = null;
		IViewReference viewReferences[] = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();
		for (int i = 0; i < viewReferences.length; i++) {
			if (SurveyView.ID.equals(viewReferences[i].getId())) {
				_surveyView = (SurveyView) viewReferences[i].getView(false);
			}
		}
	}
	
	@Override
	public void createPartControl(Composite parent) {
		_parent = parent;
		GridLayout layout = new GridLayout(4, true);
		_parent.setLayout(layout);
	}

	public void setDomain(Domain domain) {
		dispose();
		
		_domain = domain;
		setPanelValuation();
	}

	private void setPanelValuation() {
		_valuation = null;
		
		if(_domain != null) {
			dispose();
			if(_domain.getId().equals("auto_generated_importance")) {
				createHesitantPanel();
				createButtons();
				createLinguisticChart();
			} else if(_problem.getDomainValuations().get(_domain.getId()).equals(XMLValues.INTEGER)) {
				createIntegerPanel();
				createButtons();
				createIntegerChart();
			} else if(_problem.getDomainValuations().get(_domain.getId()).equals(XMLValues.INTEGER_INTERVAL)) {
				createIntegerIntervalPanel();
				createButtons();
				createIntegerChart();
			} else if(_problem.getDomainValuations().get(_domain.getId()).equals(XMLValues.REAL)) {
				createRealPanel();
				createButtons();
				createRealChart();
			} else if(_problem.getDomainValuations().get(_domain.getId()).equals(XMLValues.REAL_INTERVAL)) {
				createRealIntervalPanel();
				createButtons();
				createRealChart();
			} else if(_problem.getDomainValuations().get(_domain.getId()).equals(XMLValues.LINGUISTIC)) {
				createLinguisticPanel();
				createButtons();
				createLinguisticChart();
			} else if(_problem.getDomainValuations().get(_domain.getId()).equals(XMLValues.HESITANT)) {
				createHesitantPanel();
				createButtons();
				createLinguisticChart();
			}
			_parent.layout();
		} else {
			_domain = null;
		}
	}

	private void disposeControls() {
		
		if(!_parent.isDisposed()) {
			for(Control control: _parent.getChildren()) {
				if(!control.isDisposed()) {
					control.dispose();
				}
			}
		}
	}

	@SuppressWarnings("serial")
	private void createButtons() {	
		_buttonsPart = new Composite(_parent, SWT.NONE);
		_buttonsPart.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		_buttonsPart.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		GridLayout layout = new GridLayout(1, false);
		_buttonsPart.setLayout(layout);

		_valuateButton = new Button(_buttonsPart, SWT.BORDER);
		_valuateButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		((GridData) _valuateButton.getLayoutData()).heightHint = 35;
		_valuateButton.setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/valuation.png").createImage());
		_valuateButton.setText("Evaluar");
		_valuateButton.pack();
		_valuateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(_domain.getId().equals("auto_generated_importance")) {
					_valuation = new HesitantValuation((FuzzySet) _domain);
					if(_upperTerm != null && _lowerTerm != null) {
						((HesitantValuation) _valuation).setBinaryRelation(_lowerTerm, _upperTerm);
					} else if(_term != null) {
						((HesitantValuation) _valuation).setUnaryRelation(_unaryRelation, _term);
					} else {
						_label = ((FuzzySet) _domain).getLabelSet().getLabel(_hesitantEvaluationCombo2.getItem(_hesitantEvaluationCombo2.getSelectionIndex()));
						((HesitantValuation) _valuation).setLabel(_label);
					}
					
					_upperTerm = null;
					_lowerTerm = null;
					_term = null;
					_label = null;
					
				} else if(_problem.getDomainValuations().get(_domain.getId()).equals(XMLValues.INTEGER)) {
					_valuation = new IntegerValuation((NumericIntegerDomain) _domain, _value);
				} else if(_problem.getDomainValuations().get(_domain.getId()).equals(XMLValues.INTEGER_INTERVAL)) {
					_valuation = new IntegerIntervalValuation((NumericIntegerDomain) _domain, (int) _valueMin, (int) _valueMax);
				} else if(_problem.getDomainValuations().get(_domain.getId()).equals(XMLValues.REAL)) {
					_valuation = new RealValuation((NumericRealDomain) _domain, _value);
				} else if(_problem.getDomainValuations().get(_domain.getId()).equals(XMLValues.REAL_INTERVAL)) {
					_valuation = new RealIntervalValuation((NumericRealDomain) _domain, (double) _valueMin, (double) _valueMax);
				} else if(_problem.getDomainValuations().get(_domain.getId()).equals(XMLValues.LINGUISTIC)) {
					_valuation = new LinguisticValuation();
					_valuation.setDomain(_domain);
					((LinguisticValuation) _valuation).setLabel(_label);
				} else if(_problem.getDomainValuations().get(_domain.getId()).equals(XMLValues.HESITANT)) {
					_valuation = new HesitantValuation((FuzzySet) _domain);
					if(_upperTerm != null && _lowerTerm != null) {
						((HesitantValuation) _valuation).setBinaryRelation(_lowerTerm, _upperTerm);
					} else if(_term != null) {
						((HesitantValuation) _valuation).setUnaryRelation(_unaryRelation, _term);
					} else {
						_label = ((FuzzySet) _domain).getLabelSet().getLabel(_hesitantEvaluationCombo2.getItem(_hesitantEvaluationCombo2.getSelectionIndex()));
						((HesitantValuation) _valuation).setLabel(_label);
					}
					
					_upperTerm = null;
					_lowerTerm = null;
					_term = null;
					_label = null;
				}
				
				_surveyView.addValuation(_valuation);
			}
		});
		
		_removeButton = new Button(_buttonsPart, SWT.BORDER);
		_removeButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		((GridData) _removeButton.getLayoutData()).heightHint = 35;
		_removeButton.setText("Borrar");
		_removeButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE).createImage());
		_removeButton.pack();
		_removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				_surveyView.removeValuation(_valuation);
			}
		});
	}
	
	@SuppressWarnings("serial")
	private void createIntegerPanel() {
		_valuationComposite = new Composite(_parent, SWT.BORDER);
		_valuationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		_valuationComposite.setLayout(new GridLayout(5, true));
		
		Label label = new Label(_valuationComposite, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("Cantarell", 17, SWT.BOLD)); //$NON-NLS-1$
		GridData gd = new GridData(SWT.CENTER, SWT.FILL, true, false, 5, 1);
		gd.verticalIndent = 15;
		label.setLayoutData(gd);
		label.setText("Valoración entera");
		label.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		Label value = new Label(_valuationComposite, SWT.NONE);
		value = new Label(_valuationComposite, SWT.NONE);
		gd = new GridData(SWT.CENTER, SWT.FILL, true, false, 3, 1);
		gd.verticalIndent = 15;
		value.setLayoutData(gd);
		value.setText("Valor");
		value.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		new Label(_valuationComposite, SWT.NONE);
		
		new Label(_valuationComposite, SWT.NONE);
		_valueSpinner = new Spinner(_valuationComposite, SWT.BORDER | SWT.READ_ONLY);
		_valueSpinner.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 3, 1));
		_valueSpinner.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		new Label(_valuationComposite, SWT.NONE);
		value = new Label(_valuationComposite, SWT.NONE);
		value.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1));
		value.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		int min = ((NumericIntegerDomain) _domain).getMin();
		int max = ((NumericIntegerDomain) _domain).getMax();
		_value = ((max + min) / 2);

		_valueSpinner.setMinimum((int) min);
		_valueSpinner.setMaximum((int) max);
		_valueSpinner.setSelection((int) _value);

		_valueSpinner.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				_value = _valueSpinner.getSelection();
			}
		});	
	}
	
	@SuppressWarnings("serial")
	private void createIntegerIntervalPanel() {
		_valuationComposite = new Composite(_parent, SWT.BORDER);
		_valuationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		_valuationComposite.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		_valuationComposite.setLayout(new GridLayout(2, false));
		
		Label label = new Label(_valuationComposite, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("Cantarell", 17, SWT.BOLD)); //$NON-NLS-1$
		GridData gd = new GridData(SWT.CENTER, SWT.FILL, true, false, 2, 1);
		gd.verticalIndent = 15;
		label.setLayoutData(gd);
		label.setText("Valoración intervalar entera");
		label.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		Composite spinnerComposite = new Composite(_valuationComposite, SWT.NONE);
		spinnerComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true, 1, 1));
		spinnerComposite.setLayout(new GridLayout(1, false));
		spinnerComposite.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		Composite intervalSpinnerComposite = new Composite(spinnerComposite, SWT.NONE);
		intervalSpinnerComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1));
		intervalSpinnerComposite.setLayout(new GridLayout(2,  false));
		intervalSpinnerComposite.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		 gd = new GridData(SWT.CENTER, SWT.FILL, false, true, 1, 1);
		Label intervalLowerLabel = new Label(intervalSpinnerComposite, SWT.NONE);
		intervalLowerLabel.setLayoutData(gd);
		intervalLowerLabel.setText("Límite inferior");
		intervalLowerLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		_valueSpinnerMin = new Spinner(intervalSpinnerComposite, SWT.BORDER);
		gd = new GridData(SWT.CENTER, SWT.FILL, false, true, 1, 1);
		_valueSpinnerMin.setLayoutData(gd);
		_valueSpinnerMin.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		Label intervalUpperLabel = new Label(intervalSpinnerComposite, SWT.NONE);
		intervalUpperLabel.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true, 1, 1));
		intervalUpperLabel.setText("Límite superior");
		intervalUpperLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		_valueSpinnerMax = new Spinner(intervalSpinnerComposite, SWT.BORDER);
		gd = new GridData(SWT.CENTER, SWT.FILL, false, true, 1, 1);
		_valueSpinnerMax.setLayoutData(gd);
		_valueSpinnerMax.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		if(((NumericIntegerDomain) _domain).getInRange()) {
			_valueMin = ((NumericIntegerDomain) _domain).getMin();
			_valueMax = ((NumericIntegerDomain) _domain).getMax();
			
			int value = ((int) _valueMax / 2);
			
			_valueSpinnerMin.setMinimum((int) _valueMin);
			_valueSpinnerMin.setSelection((int) _valueMin);
			_valueSpinnerMin.setMaximum(value);
			_valueSpinnerMax.setMinimum(value);
			_valueSpinnerMax.setMaximum((int) _valueMax);
			_valueSpinnerMax.setSelection((int) _valueMax);
		} else {
			_valueMin = 0;
			_valueMax = 0;
			
			_valueSpinnerMin.setMinimum(Integer.MIN_VALUE);
			_valueSpinnerMin.setMaximum(Integer.MAX_VALUE);
			_valueSpinnerMax.setMinimum(Integer.MIN_VALUE);
			_valueSpinnerMax.setMaximum(Integer.MAX_VALUE);
		}
		
		_valueSpinnerMin.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				_valueMin = _valueSpinnerMin.getSelection();
				_valueSpinnerMin.setMaximum((int) _valueMax);
				_valueSpinnerMax.setMinimum((int) _valueMin);
			}
		});
		
		_valueSpinnerMax.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				_valueMax = _valueSpinnerMax.getSelection();
				_valueSpinnerMax.setMinimum((int) _valueMin);
				_valueSpinnerMin.setMaximum((int) _valueMax);
			}
		});	
	}
	
	@SuppressWarnings("serial")
	private void createRealPanel() {
		_valuationComposite = new Composite(_parent, SWT.BORDER);
		_valuationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		_valuationComposite.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		_valuationComposite.setLayout(new GridLayout(5, true));
		
		Label label = new Label(_valuationComposite, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("Cantarell", 17, SWT.BOLD)); //$NON-NLS-1$
		GridData gd = new GridData(SWT.CENTER, SWT.FILL, true, false, 5, 1);
		gd.verticalIndent = 15;
		label.setLayoutData(gd);
		label.setText("Valoración real");
		label.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		Label value = new Label(_valuationComposite, SWT.NONE);
		value = new Label(_valuationComposite, SWT.NONE);
		gd = new GridData(SWT.CENTER, SWT.FILL, true, false, 3, 1);
		gd.verticalIndent = 15;
		value.setLayoutData(gd);
		value.setText("Valor");
		value.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		new Label(_valuationComposite, SWT.NONE);
		
		new Label(_valuationComposite, SWT.NONE);
		_valueSpinner = new Spinner(_valuationComposite, SWT.BORDER);
		_valueSpinner.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 3, 1));
		
		new Label(_valuationComposite, SWT.NONE);
		value = new Label(_valuationComposite, SWT.NONE);
		value.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true, 1, 1));
		value.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		double min = (double) ((NumericRealDomain) _domain).getMin();
		double max = (double) ((NumericRealDomain) _domain).getMax();
		_value = ((max + min) / 2);

		_valueSpinner.setDigits(2);
		_valueSpinner.setMinimum((int) (min * 100d));
		_valueSpinner.setMaximum((int) (max * 100d));
		_valueSpinner.setSelection((int) (_value * 100d));

		_valueSpinner.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				_value = _valueSpinner.getSelection() / 100d;;
			}
		});
	}
	
	@SuppressWarnings("serial")
	private void createRealIntervalPanel() {
		_valuationComposite = new Composite(_parent, SWT.BORDER);
		_valuationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		_valuationComposite.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		_valuationComposite.setLayout(new GridLayout(2, false));
		
		Label label = new Label(_valuationComposite, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("Cantarell", 17, SWT.BOLD)); //$NON-NLS-1$
		GridData gd = new GridData(SWT.CENTER, SWT.FILL, true, false, 2, 1);
		gd.verticalIndent = 15;
		label.setLayoutData(gd);
		label.setText("Valoración intervalar real");
		label.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		Composite spinnerComposite = new Composite(_valuationComposite, SWT.NONE);
		spinnerComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true, 1, 1));
		spinnerComposite.setLayout(new GridLayout(1, false));
		spinnerComposite.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		Composite intervalSpinnerComposite = new Composite(spinnerComposite, SWT.NONE);
		intervalSpinnerComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1));
		intervalSpinnerComposite.setLayout(new GridLayout(2,  false));
		intervalSpinnerComposite.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		 gd = new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1);
		Label intervalLowerLabel = new Label(intervalSpinnerComposite, SWT.NONE);
		intervalLowerLabel.setLayoutData(gd);
		intervalLowerLabel.setText("Límite inferior");
		intervalLowerLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		_valueSpinnerMin = new Spinner(intervalSpinnerComposite, SWT.BORDER);
		gd = new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1);
		_valueSpinnerMin.setLayoutData(gd);
		_valueSpinnerMin.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		Label intervalUpperLabel = new Label(intervalSpinnerComposite, SWT.NONE);
		intervalUpperLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		intervalUpperLabel.setText("Límite superior");
		intervalUpperLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		_valueSpinnerMax = new Spinner(intervalSpinnerComposite, SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		_valueSpinnerMax.setLayoutData(gd);
		_valueSpinnerMax.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		int value = 0;
		
		if(((NumericRealDomain) _domain).getInRange()) {
			_valueMin = ((NumericRealDomain) _domain).getMin();
			_valueMax = ((NumericRealDomain) _domain).getMax();
			
			 value = (((Double) _valueMax).intValue() / 2);
			
			_valueSpinnerMin.setDigits(2);
			_valueSpinnerMin.setMinimum((int) ((double)  _valueMin * 100d));
			_valueSpinnerMin.setSelection((int) ((double)  _valueMin * 100d));
			_valueSpinnerMin.setMaximum((int) ((double) _valueMax * 100d));
			_valueSpinnerMax.setDigits(2);
			_valueSpinnerMax.setMinimum((int) (value * 100d));
			_valueSpinnerMax.setMaximum((int) ((double) _valueMax * 100d));
			_valueSpinnerMax.setSelection((int) ((double) _valueMax * 100d));
		} else {
			_valueMin = 0;
			_valueMax = 0;
			
			_valueSpinnerMin.setDigits(2);
			_valueSpinnerMin.setMinimum((int) Double.NEGATIVE_INFINITY);
			_valueSpinnerMin.setMaximum((int) Double.POSITIVE_INFINITY);
			_valueSpinnerMax.setDigits(2);
			_valueSpinnerMax.setMinimum((int) Double.NEGATIVE_INFINITY);
			_valueSpinnerMax.setMaximum((int) Double.POSITIVE_INFINITY);
		}
		
		
		_valueSpinnerMin.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				_valueMin = _valueSpinnerMin.getSelection() / 100d;
				_valueSpinnerMin.setMaximum((int) ((double) _valueMax * 100d));
				_valueSpinnerMax.setMinimum((int) ((double) _valueMin * 100d));
			}
		});
		
		_valueSpinnerMax.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				_valueMax = _valueSpinnerMax.getSelection() / 100d;
				_valueSpinnerMax.setMinimum((int) ((double) _valueMin * 100d));
				_valueSpinnerMin.setMaximum((int) ((double) _valueMax * 100d));
			}
		});	
	}
	
	@SuppressWarnings("serial")
	private void createLinguisticPanel() {
		_valuationComposite = new Composite(_parent, SWT.BORDER);
		_valuationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		_valuationComposite.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		_valuationComposite.setLayout(new GridLayout(5, true));
		
		Label label = new Label(_valuationComposite, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("Cantarell", 17, SWT.BOLD)); //$NON-NLS-1$
		GridData gd = new GridData(SWT.CENTER, SWT.FILL, true, false, 5, 1);
		gd.verticalIndent = 15;
		label.setLayoutData(gd);
		label.setText("Valoración lingüística");
		label.setBackground(new Color(Display.getCurrent(), 255, 255, 255));

		Label value = new Label(_valuationComposite, SWT.NONE);
		value = new Label(_valuationComposite, SWT.NONE);
		gd = new GridData(SWT.CENTER, SWT.BOTTOM, true, false, 3, 1);
		gd.verticalIndent = 15;
		value.setLayoutData(gd);
		value.setText("Valor");
		value.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		new Label(_valuationComposite, SWT.NONE);
		
		String[] labels = new String[((FuzzySet) _domain).getLabelSet().getCardinality()];
		
		int cont = 0;
		for(LabelLinguisticDomain labelDomain: ((FuzzySet) _domain).getLabelSet().getLabels()) {
			labels[cont] = labelDomain.getName();
			cont++;
		}
		
		new Label(_valuationComposite, SWT.NONE);
		final Combo labelCombo = new Combo(_valuationComposite, SWT.BORDER);
		labelCombo.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 3, 1));
		labelCombo.setItems(labels);
		labelCombo.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		new Label(_valuationComposite, SWT.NONE);
		value = new Label(_valuationComposite, SWT.NONE);
		value.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		value.setBackground(new Color(Display.getCurrent(), 255, 255, 255));

		_label  = ((FuzzySet) _domain).getLabelSet().getLabel(0);
		
		labelCombo.setText(_label.getName());
		labelCombo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				_label  = ((FuzzySet) _domain).getLabelSet().getLabel(labelCombo.getText());
			}
		});
	}
	
	@SuppressWarnings("serial")
	private void createHesitantPanel() {
		_valuationComposite = new Composite(_parent, SWT.BORDER);
		_valuationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		_valuationComposite.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		_valuationComposite.setLayout(new GridLayout(4, false));
		
		Label label = new Label(_valuationComposite, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("Cantarell", 17, SWT.BOLD)); //$NON-NLS-1$
		GridData gd = new GridData(SWT.CENTER, SWT.FILL, true, false, 4, 1);
		gd.verticalIndent = 15;
		label.setLayoutData(gd);
		label.setText("Valoración hesitant");
		
		Composite buttonsComposite = new Composite(_valuationComposite, SWT.NONE);
		buttonsComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 4, 1));
		buttonsComposite.setLayout(new GridLayout(2, false));
		
		Composite hesitantButtonsComposite = new Composite(buttonsComposite, SWT.NONE);
		hesitantButtonsComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		hesitantButtonsComposite.setLayout(new GridLayout(1,  false));
		
		_primaryButton = new Button(hesitantButtonsComposite, SWT.RADIO);
		_primaryButton.setText("Primaria");
		
		_compositeButton = new Button(hesitantButtonsComposite, SWT.RADIO);
		_compositeButton.setText("Compuesta");
		
		_hesitantRelationshipComposite = new Composite(buttonsComposite, SWT.NONE);
		_hesitantRelationshipComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		_hesitantRelationshipComposite.setLayout(new GridLayout(1,  false));
		
		_unaryRelationshipButton = new Button(_hesitantRelationshipComposite, SWT.RADIO);
		_unaryRelationshipButton.setText("Unaria");
		
		_binaryRelationshipButton = new Button(_hesitantRelationshipComposite, SWT.RADIO);
		_binaryRelationshipButton.setText("Binaria");
		
		_selectIndexes = new LinkedList<Integer>();
		_binaryIndexes = new LinkedList<Integer>();
		_unaryIndexes = new Pair<EUnaryRelationType, LabelLinguisticDomain>();
	
		setHesitantForm();
		
		_primaryButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(_hesitantRelationshipComposite.getEnabled()) {
					_hesitantRelationshipComposite.setEnabled(false);
					_unaryRelationshipButton.setEnabled(false);
					_binaryRelationshipButton.setEnabled(false);
				}
				
				checkHesitantValues(false, false, false, true, false);
				
				modifyHesitantSelection();
			}
		});
		
		_compositeButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {

				if(!_hesitantRelationshipComposite.getEnabled()) {
					_hesitantRelationshipComposite.setEnabled(true);
					_unaryRelationshipButton.setEnabled(true);
					_binaryRelationshipButton.setEnabled(true);
					if(_binaryRelationshipButton.getSelection()) {
						checkHesitantValues(true, true, true, true, false);
					} else {
						checkHesitantValues(false, true, false, true, false);
						
						_selectIndexes.add(0);
						_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex());
						_unaryRelation = EUnaryRelationType.LowerThan;
						_term = ((FuzzySet) _domain).getLabelSet().getLabel(_hesitantEvaluationCombo2.getSelectionIndex());
						_unaryIndexes.add(_unaryRelation, _term);
						
					}
					
					modifyHesitantSelection();
				}
			}
		});
		
		_unaryRelationshipButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(_hesitantRelationshipComposite.getEnabled()) {
					if(_compositeButton.getSelection()) {
						checkHesitantValues(false, true, false, true, false);
					} else {
						checkHesitantValues(false, false, false, true, false);
					}
					
					modifyHesitantSelection();
				}
			}
		});
		
		_binaryRelationshipButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(_hesitantRelationshipComposite.getEnabled()) {
					if(_compositeButton.getSelection()) {
						checkHesitantValues(true, true, true, true, false);
						
						String items1[] = _hesitantEvaluationCombo1.getItems();
						int pos1 = _hesitantEvaluationCombo1.getSelectionIndex();
						int pos2 = items1.length + _hesitantEvaluationCombo2.getSelectionIndex();;
						
						_selectIndexes.add(pos1);
						_selectIndexes.add(pos2);
						_binaryIndexes.add(pos1);
						_binaryIndexes.add(pos2);
						_lowerTerm = ((FuzzySet) _domain).getLabelSet().getLabel(pos1);
						_upperTerm = ((FuzzySet) _domain).getLabelSet().getLabel(pos2);
						
					} else {
						checkHesitantValues(false, false, false, true, false);
					}

					modifyHesitantSelection();
				}
			}
		});
	}
	
	@SuppressWarnings({ "serial" })
	private void checkHesitantValues(boolean between, boolean hesitantCombo1, boolean and, boolean hesitantCombo2, boolean first) {
		
		if(_valuation == null) {
			_selectIndexes.clear();
			_binaryIndexes.clear();
			_unaryIndexes.clear();
		}
		
		int fields = 0;
		
		if(between) {
			fields++;
		}
		
		if(hesitantCombo1) {
			fields++;
		}
		
		if(and) {
			fields++;
		}
		
		if(hesitantCombo2) {
			fields++;
		}
		
		if(_hesitantValueComposite != null) {
			_hesitantValueComposite.dispose();
		}
		
		_hesitantValueComposite = new Composite(_valuationComposite, SWT.NONE);
		_hesitantValueComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 4, 1));
		GridLayout layout = new GridLayout(fields, false);
		_hesitantValueComposite.setLayout(layout);
		
		if(between) {
			if(_betweenLabel != null) {
				_betweenLabel.dispose();
			}
			_betweenLabel = new Label(_hesitantValueComposite, SWT.NONE);
			_betweenLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
			_betweenLabel.setText("Between");
		}
		
		if(hesitantCombo1) {
			if(_hesitantEvaluationCombo1 != null) {
				_hesitantEvaluationCombo1.dispose();
			}
			_hesitantEvaluationCombo1 = new Combo(_hesitantValueComposite, SWT.BORDER);
			_hesitantEvaluationCombo1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			_hesitantEvaluationCombo1.addSelectionListener(new SelectionAdapter() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					modifyHesitantSelection();
				}
			});
		}
		
		if(and) {
			if(_andLabel != null) {
				_andLabel.dispose();
			}
			_andLabel = new Label(_hesitantValueComposite, SWT.NONE);
			_andLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
			_andLabel.setText("And");
		}
		
		if(hesitantCombo2) {
			if(_hesitantEvaluationCombo2 != null) {
				_hesitantEvaluationCombo2.dispose();
			}
			_hesitantEvaluationCombo2 = new Combo(_hesitantValueComposite, SWT.BORDER);
			_hesitantEvaluationCombo2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			_hesitantEvaluationCombo2.addSelectionListener(new SelectionAdapter() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					modifyHesitantSelection();
				}
			});
		}
		
		String[] items = new String[((FuzzySet) _domain).getLabelSet().getCardinality()];
		for(int i = 0; i < items.length; ++i) {
			items[i] = ((FuzzySet) _domain).getLabelSet().getLabel(i).getName();
		}
		
		boolean composite = false;
		if(_hesitantEvaluationCombo1 != null) {
			if(!_hesitantEvaluationCombo1.isDisposed()) {
				composite = true;
			}
		}
		
		if(composite) {
			boolean binary = false;
			if(_andLabel != null) {
				if(!_andLabel.isDisposed()) {
					binary = true;
					int length = items.length;
					String auxItems1[] = new String[1];
					String auxItems2[] = new String[length - 1];
					for(int i = 0; i < length; ++i) {
						if(i == 0) {
							auxItems1[i] = items[i];
						} else {
							auxItems2[i - 1] = items[i];
						}
					}
					_hesitantEvaluationCombo1.setItems(auxItems1);
					_hesitantEvaluationCombo1.select(0);
					_hesitantEvaluationCombo1.pack();
					_hesitantEvaluationCombo2.setItems(auxItems2);
					_hesitantEvaluationCombo2.select(0);
					_hesitantEvaluationCombo2.pack();
				}
			}
			
			if(!binary) {
				String[] unaryTypes = new String[EUnaryRelationType.values().length];
				for(int i = 0; i < EUnaryRelationType.values().length; ++i) {
					unaryTypes[i] = EUnaryRelationType.values()[i].toString();
				}
				_hesitantEvaluationCombo1.setItems(unaryTypes);
				_hesitantEvaluationCombo1.select(0);
				_hesitantEvaluationCombo1.pack();
				_hesitantEvaluationCombo2.setItems(items);
				_hesitantEvaluationCombo2.select(0);
				_hesitantEvaluationCombo2.pack();	
			}
		} else {
			_hesitantEvaluationCombo2.setItems(items);
			_hesitantEvaluationCombo2.select(0);
			_hesitantEvaluationCombo2.pack();
		}
		
		if(first) {
			if(_valuation != null) {
				int pos[] = ((HesitantValuation) _valuation).getEnvelopeIndex();
				if(((HesitantValuation) _valuation).isPrimary()) {
					_hesitantEvaluationCombo2.select(pos[1]);
				} else if(((HesitantValuation) _valuation).isUnary()) {
					switch(((HesitantValuation) _valuation).getUnaryRelation()) {
					case AtLeast:
						_hesitantEvaluationCombo1.select(1);
						_hesitantEvaluationCombo2.select(pos[0]);
						break;
					case AtMost:
						_hesitantEvaluationCombo1.select(0);
						_hesitantEvaluationCombo2.select(pos[1]);
						break;
					case GreaterThan:
						_hesitantEvaluationCombo1.select(3);
						_hesitantEvaluationCombo2.select(pos[0] - 1);
						break;
					case LowerThan:
						_hesitantEvaluationCombo1.select(2);
						_hesitantEvaluationCombo2.select(pos[1] + 1);
						break;
					default:
						break;
					}
				} else {
					int length = items.length;
					String[] auxItems1 = new String[pos[1]];
					String[] auxItems2 = new String[length - (pos[0] + 1)];
					
					int j = 0;
					for(int i = 0; i < length; ++i) {
						if(i < pos[1]) {
							auxItems1[i] = items[i];
						}
						
						if(i > pos[0]) {
							auxItems2[j] = items[i];
							++j;
						}
					}
					
					_hesitantEvaluationCombo1.setItems(auxItems1);
					_hesitantEvaluationCombo1.select(pos[0]);
					_hesitantEvaluationCombo2.setItems(auxItems2);
					_hesitantEvaluationCombo2.select(pos[1] - (pos[0] + 1));
				}
			}
		}
		
		if(_hesitantEvaluationCombo1 != null) {
			if(!_hesitantEvaluationCombo1.isDisposed()) {
				_hesitantEvaluationCombo1ModifyListener = new ModifyListener() {

					@Override
					public void modifyText(ModifyEvent e) {
						boolean binary = false;
						
						_selectIndexes.clear();
						
						if(_andLabel != null) {
							if(!_andLabel.isDisposed()) {
								binary = true;
								String items1[] = _hesitantEvaluationCombo1.getItems();
								String items2[] = _hesitantEvaluationCombo2.getItems();
								
								int i = 0;
								boolean find = false;
								String pivotItem = items1[items1.length - 1];
								
								do {
									if(items2[i].equals(pivotItem)) {
										find = true;
									} else {
										++i;
									}
								} while((!find) && (i < items2.length));
								
								int pos1 = _hesitantEvaluationCombo1.getSelectionIndex();
								int pos2 = items1.length + _hesitantEvaluationCombo2.getSelectionIndex();
								
								if(find) {
									pos2 -= i + 1;
								}
								_selectIndexes.add(pos1);
								_selectIndexes.add(pos2);
								_binaryIndexes.add(pos1);
								_binaryIndexes.add(pos2);
								_lowerTerm = ((FuzzySet) _domain).getLabelSet().getLabel(pos1);
								_upperTerm = ((FuzzySet) _domain).getLabelSet().getLabel(pos2);
								
								
								_hesitantEvaluationCombo1.removeModifyListener(_hesitantEvaluationCombo1ModifyListener);
								_hesitantEvaluationCombo2.removeModifyListener(_hesitantEvaluationCombo2ModifyListener);
								
								int length = items2.length + items1.length;
								if(find) {
									length -= i + 1;
								}
								String[] items = new String[length];
								int j = 0;
								for(int pos = 0; pos < length; ++pos) {
									if(pos < items1.length) {
										items[pos] = items1[pos];
									} else {
										if(find) {
											items[pos] = items2[j + (i + 1)];
										} else {
											items[pos] = items2[j];
										}
										++j;
									}
								}
								String[] auxItems1 = new String[pos2];
								String[] auxItems2 = new String[length - (pos1 + 1)];
								j = 0;
								for(int pos = 0; pos < length; ++pos) {
									if(pos < pos2) {
										auxItems1[pos] = items[pos];
									}
									if(pos > pos1) {
										auxItems2[j] = items[pos];
										++j;
									}
								}
								
								_hesitantEvaluationCombo1.setItems(auxItems1);
								_hesitantEvaluationCombo1.select(pos1);
								_hesitantEvaluationCombo2.setItems(auxItems2);
								_hesitantEvaluationCombo2.select(pos2 - (pos1 + 1));
								
								_hesitantEvaluationCombo1.addModifyListener(_hesitantEvaluationCombo1ModifyListener);
								_hesitantEvaluationCombo2.addModifyListener(_hesitantEvaluationCombo2ModifyListener);
							}
						}
						
						if(!binary) {
							String value = _hesitantEvaluationCombo1.getItems()[_hesitantEvaluationCombo1.getSelectionIndex()];
							if (EUnaryRelationType.GreaterThan.toString().equals(value)) {
								_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex() + 1);
								_selectIndexes.add(((FuzzySet) _domain).getLabelSet().getCardinality() - 1);
								_unaryRelation = EUnaryRelationType.GreaterThan;
								_term = ((FuzzySet) _domain).getLabelSet().getLabel(_hesitantEvaluationCombo2.getSelectionIndex());
								_unaryIndexes.add(_unaryRelation, _term);
							} else if(EUnaryRelationType.LowerThan.toString().equals(value)) {
								_selectIndexes.add(0);
								_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex() - 1);
								_unaryRelation = EUnaryRelationType.LowerThan;
								_term = ((FuzzySet) _domain).getLabelSet().getLabel(_hesitantEvaluationCombo2.getSelectionIndex());
								_unaryIndexes.add(_unaryRelation, _term);
							} else if(EUnaryRelationType.AtLeast.toString().equals(value)) {
								_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex());
								_selectIndexes.add(((FuzzySet) _domain).getLabelSet().getCardinality() - 1);
								_unaryRelation = EUnaryRelationType.AtLeast;
								_term = ((FuzzySet) _domain).getLabelSet().getLabel(_hesitantEvaluationCombo2.getSelectionIndex());
								_unaryIndexes.add(_unaryRelation, _term);
							} else if(EUnaryRelationType.AtMost.toString().equals(value)) {
								_selectIndexes.add(0);
								_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex());
								_unaryRelation = EUnaryRelationType.AtMost;
								_term = ((FuzzySet) _domain).getLabelSet().getLabel(_hesitantEvaluationCombo2.getSelectionIndex());
								_unaryIndexes.add(_unaryRelation, _term);
							}
						}	
					}
				};
				
				_hesitantEvaluationCombo1.addModifyListener(_hesitantEvaluationCombo1ModifyListener);
			}
		}
		
		_hesitantEvaluationCombo2ModifyListener = new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				
				_selectIndexes.clear();
				
				boolean binary = false;
				if(_andLabel != null) {
					if(!_andLabel.isDisposed()) {
						binary = true;
						String items1[] = _hesitantEvaluationCombo1.getItems();
						String items2[] = _hesitantEvaluationCombo2.getItems();
						
						int i = 0;
						boolean find = false;
						String pivotItem = items1[items1.length - 1];
						
						do {
							if (items2[i].equals(pivotItem)) {
								find = true;
							} else {
								i++;
							}
						} while((!find) && (i < items2.length));
						
						int pos1 = _hesitantEvaluationCombo1.getSelectionIndex();
						int pos2 = items1.length + _hesitantEvaluationCombo2.getSelectionIndex();
						
						if (find) {
							pos2 -= i + 1;
						}
						
						_selectIndexes.add(pos1);
						_selectIndexes.add(pos2);
						_binaryIndexes.add(pos1);
						_binaryIndexes.add(pos2);
						_lowerTerm = ((FuzzySet) _domain).getLabelSet().getLabel(pos1);
						_upperTerm = ((FuzzySet) _domain).getLabelSet().getLabel(pos2);
						
						_hesitantEvaluationCombo1.removeModifyListener(_hesitantEvaluationCombo1ModifyListener);
						_hesitantEvaluationCombo2.removeModifyListener(_hesitantEvaluationCombo2ModifyListener);
						int length = items2.length + items1.length;

						if (find) {
							length -= i + 1;
						}
						String[] items = new String[length];
						int j = 0;
						for (int pos = 0; pos < length; pos++) {
							if (pos < items1.length) {
								items[pos] = items1[pos];
							} else {
								if (find) {
									items[pos] = items2[j + (i + 1)];
								} else {
									items[pos] = items2[j];
								}
								++j;
							}
						}
						String auxItems1[] = new String[pos2];
						String auxItems2[] = new String[length - (pos1 + 1)];
						j = 0;
						for (int pos = 0; pos < length; pos++) {
							if (pos < pos2) {
								auxItems1[pos] = items[pos];
							}
							if (pos > pos1) {
								auxItems2[j] = items[pos];
								j++;
							}
						}
						_hesitantEvaluationCombo1.setItems(auxItems1);
						_hesitantEvaluationCombo1.select(pos1);
						_hesitantEvaluationCombo2.setItems(auxItems2);
						_hesitantEvaluationCombo2.select(pos2 - (pos1 + 1));
						
						_hesitantEvaluationCombo1.addModifyListener(_hesitantEvaluationCombo1ModifyListener);
						_hesitantEvaluationCombo2.addModifyListener(_hesitantEvaluationCombo2ModifyListener);
					}
				}
				
				if (!binary) {
					boolean unary = false;
					if (_hesitantEvaluationCombo1 != null) {
						if (!_hesitantEvaluationCombo1.isDisposed()) {
							unary = true;
							
							String value = _hesitantEvaluationCombo1.getItems()[_hesitantEvaluationCombo1.getSelectionIndex()];
							if (EUnaryRelationType.GreaterThan.toString().equals(value)) {
								_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex() + 1);
								_selectIndexes.add(((FuzzySet) _domain).getLabelSet().getCardinality() - 1);
								_unaryRelation = EUnaryRelationType.GreaterThan;
								_term = ((FuzzySet) _domain).getLabelSet().getLabel(_hesitantEvaluationCombo2.getSelectionIndex());
								_unaryIndexes.add(_unaryRelation, _term);
							} else if(EUnaryRelationType.LowerThan.toString().equals(value)) {
								_selectIndexes.add(0);
								_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex() - 1);
								_unaryRelation = EUnaryRelationType.LowerThan;
								_term = ((FuzzySet) _domain).getLabelSet().getLabel(_hesitantEvaluationCombo2.getSelectionIndex());
								_unaryIndexes.add(_unaryRelation, _term);
							} else if(EUnaryRelationType.AtLeast.toString().equals(value)) {
								_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex());
								_selectIndexes.add(((FuzzySet) _domain).getLabelSet().getCardinality() - 1);
								_unaryRelation = EUnaryRelationType.AtLeast;
								_term = ((FuzzySet) _domain).getLabelSet().getLabel(_hesitantEvaluationCombo2.getSelectionIndex());
								_unaryIndexes.add(_unaryRelation, _term);
							} else if(EUnaryRelationType.AtMost.toString().equals(value)) {
								_selectIndexes.add(0);
								_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex());
								_unaryRelation = EUnaryRelationType.AtMost;
								_term = ((FuzzySet) _domain).getLabelSet().getLabel(_hesitantEvaluationCombo2.getSelectionIndex());
								_unaryIndexes.add(_unaryRelation, _term);
							}
						}
					}
					
					if (!unary) {
						_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex());
						_label = ((FuzzySet) _domain).getLabelSet().getLabel(_hesitantEvaluationCombo2.getItem(_selectIndexes.get(0)));
					}
					
				}
			}
		};
		_hesitantEvaluationCombo2.addModifyListener(_hesitantEvaluationCombo2ModifyListener);
		_hesitantValueComposite.layout();
		_valuationComposite.layout();
		
	}

	private void modifyHesitantSelection() {
	
		_selectIndexes.clear();
		
		if((_hesitantEvaluationCombo1 != null) && (!_hesitantEvaluationCombo1.isDisposed())) {
			if((_andLabel != null) && (!_andLabel.isDisposed())) {
				String items1[] = _hesitantEvaluationCombo1.getItems();
				String items2[] = _hesitantEvaluationCombo2.getItems();
				
				int i = 0;
				boolean find = false;
				String pivotItem = items1[items1.length - 1];
				
				do {
					if(items2[i].equals(pivotItem)) {
						find = true;
					} else {
						++i;
					}
				} while((!find) && (i < items2.length));
				
				int pos1 = _hesitantEvaluationCombo1.getSelectionIndex();
				int pos2 = items1.length + _hesitantEvaluationCombo2.getSelectionIndex();
				
				if(find) {
					pos2 -= i + 1;
				}
				
				_selectIndexes.add(pos1);
				_selectIndexes.add(pos2);
	
				_lowerTerm = ((FuzzySet) _domain).getLabelSet().getLabel(pos1);
				_upperTerm = ((FuzzySet) _domain).getLabelSet().getLabel(pos2);
			} else {
				String value = _hesitantEvaluationCombo1.getItems()[_hesitantEvaluationCombo1.getSelectionIndex()];
				if (EUnaryRelationType.GreaterThan.toString().equals(value)) {
					_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex() + 1);
					_selectIndexes.add(((FuzzySet) _domain).getLabelSet().getCardinality() - 1);
				} else if(EUnaryRelationType.LowerThan.toString().equals(value)) {
					_selectIndexes.add(0);
					_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex() - 1);
				} else if(EUnaryRelationType.AtLeast.toString().equals(value)) {
					_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex());
					_selectIndexes.add(((FuzzySet) _domain).getLabelSet().getCardinality() - 1);
				} else if(EUnaryRelationType.AtMost.toString().equals(value)) {
					_selectIndexes.add(0);
					_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex());
				}
			}
		} else {
			_selectIndexes.add(_hesitantEvaluationCombo2.getSelectionIndex());
			_label = ((FuzzySet) _domain).getLabelSet().getLabel(_hesitantEvaluationCombo2.getItem(_selectIndexes.get(0)));
		}
	}

	private void setHesitantForm() {
		
		if(_valuation == null) {
			checkHesitantValues(false, false, false, true, true);	
			_hesitantRelationshipComposite.setEnabled(false);
			_unaryRelationshipButton.setSelection(true);
			_unaryRelationshipButton.setEnabled(false);
			_binaryRelationshipButton.setSelection(false);
			_binaryRelationshipButton.setEnabled(false);
			_primaryButton.setSelection(true);
			_compositeButton.setSelection(false);
		}else {
			if(((HesitantValuation) _valuation).isPrimary()) {
				checkHesitantValues(false, false, false, true, true);
				_hesitantRelationshipComposite.setEnabled(false);
				_unaryRelationshipButton.setSelection(true);
				_unaryRelationshipButton.setEnabled(false);
				_binaryRelationshipButton.setEnabled(false);
				_primaryButton.setSelection(true);
			} else if(((HesitantValuation) _valuation).isUnary()) {
				checkHesitantValues(false, true, false, true, true);
				_hesitantRelationshipComposite.setEnabled(true);
				_compositeButton.setSelection(true);
				_unaryRelationshipButton.setSelection(true);
				_unaryRelationshipButton.setEnabled(true);
				_binaryRelationshipButton.setEnabled(true);
			} else {
				checkHesitantValues(true, true, true, true, true);
				_hesitantRelationshipComposite.setEnabled(true);
				_compositeButton.setSelection(true);
				_binaryRelationshipButton.setSelection(true);
				_unaryRelationshipButton.setEnabled(true);
				_binaryRelationshipButton.setEnabled(true);
			}
		}
		
		if(((FuzzySet) _domain).getLabelSet().getCardinality() == 1) {
			_compositeButton.setEnabled(false);
		} else {
			_compositeButton.setEnabled(true);
		}
	}
	
	@SuppressWarnings("serial")
	private void createIntegerChart() {
		_chartComposite = new Composite(_parent, SWT.NONE);
		_chartComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1));
		_chartComposite.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		GridLayout layout = new GridLayout(1, true);
		_chartComposite.setLayout(layout);
		
		_chartComposite.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				_chart = new NumericIntegerDomainChart();
				_chart.initialize(_domain, _chartComposite.getSize().x, _chartComposite.getSize().y, SWT.NONE);
				_chartComposite.setBackgroundImage(_chart.createImage());
			}
		});
	}
	
	
	@SuppressWarnings("serial")
	private void createRealChart() {
		_chartComposite = new Composite(_parent, SWT.NONE);
		_chartComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1));
		_chartComposite.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		GridLayout layout = new GridLayout(1, true);
		_chartComposite.setLayout(layout);
		
		_chartComposite.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				_chart = new NumericRealDomainChart();
				_chart.initialize(_domain, _chartComposite.getSize().x, _chartComposite.getSize().y, SWT.NONE);
				_chartComposite.setBackgroundImage(_chart.createImage());
			}
		});
	}


	@SuppressWarnings("serial")
	private void createLinguisticChart() {
		_chartComposite = new Composite(_parent, SWT.NONE);
		_chartComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1));
		_chartComposite.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		GridLayout layout = new GridLayout(1, true);
		_chartComposite.setLayout(layout);
		
		_chartComposite.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				_chart = new LinguisticDomainChart();
				_chart.initialize(_domain, _chartComposite.getSize().x, _chartComposite.getSize().y, SWT.NONE);
				_chartComposite.setBackgroundImage(_chart.createImage());
			}
		});
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		disposeControls();
	}

	@Override
	public void setFocus() {}
	
}
