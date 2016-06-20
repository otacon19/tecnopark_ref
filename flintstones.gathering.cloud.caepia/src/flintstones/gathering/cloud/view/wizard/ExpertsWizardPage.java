package flintstones.gathering.cloud.view.wizard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.layout.FillLayout;

import flintstones.gathering.cloud.model.Problem;

@SuppressWarnings("serial")
public class ExpertsWizardPage extends WizardPage {

	private Table table;
	private TableElement[] model;
	private boolean pageComplete;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	class TableElement {
		String id;
		String mail;

		TableElement(String id) {
			this.id = id;
			mail = null;
		}

		TableElement(String id, String mail) {
			this(id);
			this.mail = mail;
		}
	}

	/**
	 * Create the wizard.
	 */
	public ExpertsWizardPage() {
		super("wizardPage");
		setTitle("Expertos del problema");
		setDescription(null);
		pageComplete = false;
	}

	class TableViewerContentProvider implements IStructuredContentProvider {

		@Override
		public Object[] getElements(Object inputElement) {
			model = null;

			List<String> experts = ((Problem) inputElement).getExperts();
			model = new TableElement[experts.size()];

			int pos = 0;
			for (String e : experts) {
				model[pos++] = new TableElement(e, null);
			}
			return model;
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	class ExpertsLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			return ((TableElement) obj).id;
		}
	}

	class AssignmentLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			String mail = ((TableElement) obj).mail;
			return (mail != null) ? mail : "Insertar mail del experto";
		}
	}

	class AssignmentEditingSupport extends EditingSupport {

		private final TableViewer viewer;
		private final CellEditor editor;

		public AssignmentEditingSupport(TableViewer viewer) {
			super(viewer);
			this.viewer = viewer;
			this.editor = new TextCellEditor(viewer.getTable());
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return editor;
		}

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

		@Override
		protected Object getValue(Object element) {
			String result = ((TableElement) element).mail;
			return (result != null) ? result : "";
		}

		@Override
		protected void setValue(Object element, Object value) {
			String mail = String.valueOf(value);
			if (!mail.matches(EMAIL_PATTERN)) {
				MessageDialog.openError(getShell(), "Mail inválido",
						"Mail inválido");
			} else {
				((TableElement) element).mail = mail;
				viewer.update(element, null);
			}

			validate();
		}

	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		TableViewer tableViewer = new TableViewer(container, SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		tableViewer.setContentProvider(new TableViewerContentProvider());

		TableViewerColumn tvc = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tc = tvc.getColumn();
		tc.setText("Identificador del experto");
		tvc.setLabelProvider(new ExpertsLabelProvider());

		tvc = new TableViewerColumn(tableViewer, SWT.NONE);
		tc = tvc.getColumn();
		tc.setText("Mail del experto");
		tvc.setLabelProvider(new AssignmentLabelProvider());
		tvc.setEditingSupport(new AssignmentEditingSupport(tableViewer));

		tableViewer.setInput(((ImportWizard) getWizard()).getProblem());
		packColumns();
		validate();
	}

	private void packColumns() {
		for (TableColumn c : table.getColumns()) {
			c.pack();
			c.setWidth(c.getWidth() + 10);
		}
	}

	private void validate() {
		Set<String> mails = new HashSet<String>();
		pageComplete = true;

		int pos = 0;
		TableElement e;
		while ((pos < model.length) && (pageComplete)) {
			e = model[pos++];
			if (e.mail == null) {
				setErrorMessage("Empty mails");
				pageComplete = false;
			} else {
				if (mails.contains(e.mail)) {
					setErrorMessage("Duplicate mails");
					pageComplete = false;
				} else {
					mails.add(e.mail);
				}
			}
		}
		
		if (pageComplete) {
			setErrorMessage(null);
		}

		packColumns();
		setPageComplete(pageComplete);
	}
	
	public Map<String, String> getMails() {
		Map<String, String> result = new HashMap<String, String>();
		
		for (TableElement e : model) {
			result.put(e.id, e.mail);
		}
		
		return result;
	}
}
