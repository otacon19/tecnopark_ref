package flintstones.gathering.cloud.view;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import net.sourceforge.nattable.NatTable;
import net.sourceforge.nattable.data.IColumnPropertyAccessor;
import net.sourceforge.nattable.data.IDataProvider;
import net.sourceforge.nattable.data.ListDataProvider;
import net.sourceforge.nattable.data.ReflectiveColumnPropertyAccessor;
import net.sourceforge.nattable.layer.DataLayer;

public class SurveyView extends ViewPart {

	public static final String ID = "flintstones.gathering.cloud.view.surveyView";

	@Override
	public void createPartControl(Composite parent) {
		String[] propertyNames = { "firstName", "lastName", "gender", "married", "birthday" };

		List<String> examples = new LinkedList<String>();

		IColumnPropertyAccessor<String> columnPropertyAccessor = new ReflectiveColumnPropertyAccessor<String>(propertyNames);
		IDataProvider bodyDataProvider = new ListDataProvider<String>(examples, columnPropertyAccessor);
		DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
		
		NatTable natTable = new NatTable(parent, SWT.NO_REDRAW_RESIZE | SWT.DOUBLE_BUFFERED | SWT.BORDER, bodyDataLayer);

		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void refresh() {
	}

}
