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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wb.swt.SWTResourceManager;

import flintstones.gathering.cloud.model.Problem;
import mcdacw.valuation.domain.Domain;
import mcdacw.valuation.domain.numeric.NumericIntegerDomain;
import mcdacw.valuation.valuation.IntegerValuation;

public class ValuationView extends ViewPart {
	
	public static final String ID = "flintstones.gathering.cloud.view.valuationview";

	private Spinner _valueSpinner;
	private Button _removeButton;
	private Button _valuateButton;
	private Composite _parent;
	
	private int _value;
	
	private Domain _domain;
	
	private Problem _problem;
	
	public ValuationView() {
		_problem = (Problem) RWT.getUISession().getAttribute("valuation-problem");
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
			if(_problem.getDomainValuations().get(_domain.getId()).equals(IntegerValuation.class.toString())) {
				createIntegerPanel();
			}
			createButtons();
		} else {
			_domain = null;
		}
		_parent.layout();
	}
	
	private void disposeButtons() {
		if(_removeButton != null) {
			_removeButton.dispose();
		}
		if(_valuateButton != null) {
			_valuateButton.dispose();
		}
		if(_valueSpinner != null) {
			_valueSpinner.dispose();
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
				
			}
		});
		
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
	
	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void setFocus() {}

}
