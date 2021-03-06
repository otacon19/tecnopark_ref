package flintstones.gathering.cloud.view.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.layout.FillLayout;

import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.nls.Messages;

@SuppressWarnings("serial")
public class AlternativesWizardPage extends WizardPage {
	
	private Table table;

	/**
	 * Create the wizard.
	 */
	public AlternativesWizardPage() {
		super("wizardPage"); //$NON-NLS-1$
		setTitle(Messages.AlternativesWizardPage_Alternatives_problem);
		setDescription(null);
	}
	
	class TableViewerContentProvider implements IStructuredContentProvider {

		@Override
		public Object[] getElements(Object inputElement) {
			return ((Problem) inputElement).getAlternatives().toArray(new String[0]);
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
		
	}

	class AlternativeLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			return ((String) obj);
		}
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		TableViewer tableViewer = new TableViewer(container, SWT.BORDER);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		tableViewer.setContentProvider(new TableViewerContentProvider());
		
		TableViewerColumn tvc = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tc = tvc.getColumn();
		tc.setText(Messages.AlternativesWizardPage_Alternatives);
		tvc.setLabelProvider(new AlternativeLabelProvider());
		
		tableViewer.setInput(((ImportWizard) getWizard()).getProblem());
		packColumns();
	}

	private void packColumns() {
		for (TableColumn c : table.getColumns()) {
			c.pack();
			c.setWidth(c.getWidth() + 10);
		}
	}
}
