package flintstones.gathering.cloud.view.frameworkstructuring;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import de.kupzog.ktable.KTable;

public class ElementAssignmentTable extends KTable {

	private ElementAssignmentsTableContentProvider _model;

	public ElementAssignmentTable(Composite parent) {
		super(parent, SWT.NO_BACKGROUND | SWT.V_SCROLL | SWT.FLAT);
		setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		_model = null;
	}
	
	public void setModel(ProblemElement element) {
		_model = new ElementAssignmentsTableContentProvider(this, element);
		setModel(_model);
		getParent().getParent().layout();
	}
	
	@Override
	public void dispose() {
		_model.dispose();
		super.dispose();
	}
	
}
