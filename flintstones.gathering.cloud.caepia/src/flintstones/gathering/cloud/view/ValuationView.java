package flintstones.gathering.cloud.view;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
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
import mcdacw.valuation.domain.fuzzyset.FuzzySet;
import mcdacw.valuation.domain.fuzzyset.label.LabelLinguisticDomain;
import mcdacw.valuation.domain.numeric.NumericIntegerDomain;
import mcdacw.valuation.domain.numeric.NumericRealDomain;
import mcdacw.valuation.valuation.IntegerValuation;

public class ValuationView extends ViewPart {
	
	public static final String ID = "flintstones.gathering.cloud.view.valuationview";

	private Spinner _valueSpinner;
	private Spinner _valueSpinnerMax;
	private Spinner _valueSpinnerMin;
	private Button _removeButton;
	private Button _valuateButton;
	private Composite _parent;
	
	private double _value;
	private Object _valueMax;
	private Object _valueMin;
	private LabelLinguisticDomain _label;
	
	private Domain _domain;
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
	}

	public void setDomain(Domain domain) {
		_domain = domain;
		setPanelValuation();
	}

	private void setPanelValuation() {
		
		if(_domain != null) {
			disposeButtons();
			if(_problem.getDomainValuations().get(_domain.getId()).equals(XMLValues.INTEGER)) {
				createIntegerPanel();
				createButtons();
			} else if(_problem.getDomainValuations().get(_domain.getId()).equals(XMLValues.INTEGER_INTERVAL)) {
				createIntegerIntervalPanel();
				createButtons();
			} else if(_problem.getDomainValuations().get(_domain.getId()).equals(XMLValues.REAL)) {
				createRealPanel();
				createButtons();
			} else if(_problem.getDomainValuations().get(_domain.getId()).equals(XMLValues.REAL_INTERVAL)) {
				createRealIntervalPanel();
				createButtons();
			} else if(_problem.getDomainValuations().get(_domain.getId()).equals(XMLValues.LINGUISTIC)) {
				createLinguisticPanel();
				createButtons();
			}
			
		} else {
			_domain = null;
		}
		_parent.layout();
	}

	private void disposeButtons() {
		
		for(Control control: _parent.getChildren()) {
			control.dispose();
		}
	}

	@SuppressWarnings("serial")
	private void createButtons() {	
		final Composite buttonsPart = new Composite(_parent, SWT.NONE);
		buttonsPart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 3, 1));
		buttonsPart.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		GridLayout layout = new GridLayout(2, true);
		buttonsPart.setLayout(layout);

		_removeButton = new Button(buttonsPart, SWT.BORDER);
		_removeButton.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, true, 1, 1));
		_removeButton.setText("Borrar");
		_removeButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE).createImage());

		_valuateButton = new Button(buttonsPart, SWT.BORDER);
		_valuateButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));
		_valuateButton.setImage(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/valuation.png").createImage());
		_valuateButton.setText("Evaluar");
		_valuateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(_problem.getDomainValuations().get(_domain.getId()).equals(IntegerValuation.class.toString())) {
					IntegerValuation valuation = new IntegerValuation((NumericIntegerDomain) _domain, _value);
					_surveyView.addValuation(valuation);
				}
			}
		});
		
		((GridData) _removeButton.getLayoutData()).widthHint = 80;
		((GridData) _valuateButton.getLayoutData()).widthHint = 80;
		
		buttonsPart.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				Point sizeRemove = _removeButton.getSize();
				Point sizeValuate = _valuateButton.getSize();

				Point size = new Point((sizeRemove.x < sizeValuate.x) ? sizeValuate.x : sizeRemove.x, sizeRemove.y);
				_removeButton.setSize(size);
				_valuateButton.setSize(size);
				((GridData) _removeButton.getLayoutData()).widthHint = 80;
				((GridData) _valuateButton.getLayoutData()).widthHint = 80;
			}
		});
	}
	
	@SuppressWarnings("serial")
	private void createIntegerPanel() {
		_parent.setLayout(new GridLayout(5, true));
		
		Label label = new Label(_parent, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("Cantarell", 17, SWT.BOLD)); //$NON-NLS-1$
		GridData gd = new GridData(SWT.CENTER, SWT.CENTER, false, false, 5, 1);
		gd.verticalIndent = 15;
		label.setLayoutData(gd);
		label.setText("Valoración entera");
		label.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		Label value = new Label(_parent, SWT.NONE);
		value = new Label(_parent, SWT.NONE);
		gd = new GridData(SWT.CENTER, SWT.BOTTOM, true, false, 3, 1);
		gd.verticalIndent = 15;
		value.setLayoutData(gd);
		value.setText("Valor");
		value.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		new Label(_parent, SWT.NONE);
		
		new Label(_parent, SWT.NONE);
		_valueSpinner = new Spinner(_parent, SWT.BORDER | SWT.READ_ONLY);
		_valueSpinner.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 3, 1));
		_valueSpinner.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		new Label(_parent, SWT.NONE);
		value = new Label(_parent, SWT.NONE);
		value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
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
		GridLayout layout = new GridLayout(2, false);
		layout.verticalSpacing = 15;
		_parent.setLayout(layout);
		
		Label label = new Label(_parent, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("Cantarell", 17, SWT.BOLD)); //$NON-NLS-1$
		GridData gd = new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1);
		gd.verticalIndent = 15;
		label.setLayoutData(gd);
		label.setText("Valoración intervalar entera");
		label.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		Composite spinnerComposite = new Composite(_parent, SWT.NONE);
		spinnerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
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
		gd.widthHint = 80;
		_valueSpinnerMin.setLayoutData(gd);
		_valueSpinnerMin.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		Label intervalUpperLabel = new Label(intervalSpinnerComposite, SWT.NONE);
		intervalUpperLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		intervalUpperLabel.setText("Límite superior");
		intervalUpperLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		_valueSpinnerMax = new Spinner(intervalSpinnerComposite, SWT.BORDER);
		gd = new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1);
		gd.widthHint = 80;
		_valueSpinnerMax.setLayoutData(gd);
		_valueSpinnerMax.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		if(((NumericIntegerDomain) _domain).getInRange()) {
			_valueMin = ((NumericIntegerDomain) _domain).getMin();
			_valueMax = ((NumericIntegerDomain) _domain).getMax();
			
			int value = ((int) _valueMax / 2);
			
			_valueSpinnerMin.setMinimum((int) _valueMin);
			_valueSpinnerMin.setMaximum(value);
			_valueSpinnerMax.setMinimum(value);
			_valueSpinnerMax.setMaximum((int) _valueMax);
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
		_parent.setLayout(new GridLayout(5, true));
		
		Label label = new Label(_parent, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("Cantarell", 17, SWT.BOLD)); //$NON-NLS-1$
		GridData gd = new GridData(SWT.CENTER, SWT.CENTER, false, false, 5, 1);
		gd.verticalIndent = 15;
		label.setLayoutData(gd);
		label.setText("Valoración real");
		label.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		Label value = new Label(_parent, SWT.NONE);
		value = new Label(_parent, SWT.NONE);
		gd = new GridData(SWT.CENTER, SWT.BOTTOM, true, false, 3, 1);
		gd.verticalIndent = 15;
		value.setLayoutData(gd);
		value.setText("Valor");
		value.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		new Label(_parent, SWT.NONE);
		
		new Label(_parent, SWT.NONE);
		_valueSpinner = new Spinner(_parent, SWT.BORDER);
		_valueSpinner.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 3, 1));
		
		new Label(_parent, SWT.NONE);
		value = new Label(_parent, SWT.NONE);
		value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
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
		GridLayout layout = new GridLayout(2, false);
		layout.verticalSpacing = 15;
		_parent.setLayout(layout);
		
		Label label = new Label(_parent, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("Cantarell", 17, SWT.BOLD)); //$NON-NLS-1$
		GridData gd = new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1);
		gd.verticalIndent = 15;
		label.setLayoutData(gd);
		label.setText("Valoración intervalar real");
		label.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		Composite spinnerComposite = new Composite(_parent, SWT.NONE);
		spinnerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
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
		gd.widthHint = 80;
		_valueSpinnerMin.setLayoutData(gd);
		_valueSpinnerMin.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		Label intervalUpperLabel = new Label(intervalSpinnerComposite, SWT.NONE);
		intervalUpperLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		intervalUpperLabel.setText("Límite superior");
		intervalUpperLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		_valueSpinnerMax = new Spinner(intervalSpinnerComposite, SWT.BORDER);
		gd = new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1);
		gd.widthHint = 80;
		_valueSpinnerMax.setLayoutData(gd);
		_valueSpinnerMax.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		int value = 0;
		
		if(((NumericRealDomain) _domain).getInRange()) {
			
			_valueMin = ((NumericRealDomain) _domain).getMin();
			_valueMax = ((NumericRealDomain) _domain).getMax();
			
			 value = (((Double) _valueMax).intValue() / 2);
			
			_valueSpinnerMin.setDigits(2);
			_valueSpinnerMin.setMinimum((int) ((double)  _valueMin * 100d));
			_valueSpinnerMin.setMaximum((int) (value * 100d));
			_valueSpinnerMax.setDigits(2);
			_valueSpinnerMax.setMinimum((int) (value * 100d));
			_valueSpinnerMax.setMaximum((int) ((double) _valueMax * 100d));
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
		_parent.setLayout(new GridLayout(5, true));
		
		Label label = new Label(_parent, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("Cantarell", 11, SWT.BOLD)); //$NON-NLS-1$
		GridData gd = new GridData(SWT.CENTER, SWT.CENTER, false, false, 5, 1);
		gd.verticalIndent = 15;
		label.setLayoutData(gd);
		label.setText("Valoración lingüística");
		label.setBackground(new Color(Display.getCurrent(), 255, 255, 255));

		Label value = new Label(_parent, SWT.NONE);
		value = new Label(_parent, SWT.NONE);
		gd = new GridData(SWT.CENTER, SWT.BOTTOM, true, false, 3, 1);
		gd.verticalIndent = 15;
		value.setLayoutData(gd);
		value.setText("Valor");
		value.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		new Label(_parent, SWT.NONE);
		
		String[] labels = new String[((FuzzySet) _domain).getLabelSet().getCardinality()];
		
		int cont = 0;
		for(LabelLinguisticDomain labelDomain: ((FuzzySet) _domain).getLabelSet().getLabels()) {
			labels[cont] = labelDomain.getName();
			cont++;
		}
		
		new Label(_parent, SWT.NONE);
		final Combo labelCombo = new Combo(_parent, SWT.BORDER);
		labelCombo.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 3, 1));
		labelCombo.setItems(labels);
		labelCombo.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		
		new Label(_parent, SWT.NONE);
		value = new Label(_parent, SWT.NONE);
		value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
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
	
	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void setFocus() {}
	
}
