package flintstones.gathering.cloud.view;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.APP;
import flintstones.gathering.cloud.dao.DAOUser;
import flintstones.gathering.cloud.dao.Deploy;
import flintstones.gathering.cloud.model.User;
import flintstones.gathering.cloud.nls.Messages;

@SuppressWarnings("serial")
public class AdminPanelDialog extends TitleAreaDialog {

	private static final Image CHECKED = AbstractUIPlugin
			.imageDescriptorFromPlugin("flintstones.gathering.cloud", //$NON-NLS-1$
					"icons/checked.gif").createImage(); //$NON-NLS-1$
	private static final Image UNCHECKED = AbstractUIPlugin
			.imageDescriptorFromPlugin("flintstones.gathering.cloud", //$NON-NLS-1$
					"icons/unchecked.gif").createImage(); //$NON-NLS-1$

	private List<User> model;

	public AdminPanelDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.PRIMARY_MODAL);

		model = DAOUser.getDAO().getAllUsers();
	}

	@Override
	public void create() {
		super.create();
		setTitle(Messages.AdminPanelDialog_Admin_panel);
		setMessage(Messages.AdminPanelDialog_Admin_panel, IMessageProvider.INFORMATION);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.AdminPanelDialog_Close,
				true);
	}

	class TableElement implements Comparable<TableElement> {

		User user = null;
		String mail = null;
		Boolean admin = null;
		Boolean problems = null;

		TableElement(User user) {
			this.user = user;
			mail = user.getMail();
			admin = user.getAdmin();
			problems = user.getManageProblems();
		}

		@Override
		public int compareTo(TableElement o) {

			int result = 0;

			if (o != null) {
				result = mail.compareTo(o.mail);
			} else {
				result = -1;
			}
			return result;
		}

	}

	class ViewContentProvider implements IStructuredContentProvider {

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			@SuppressWarnings("unchecked")
			List<User> users = (List<User>) parent;

			List<TableElement> result = new LinkedList<TableElement>();

			for (User user : users) {
				result.add(new TableElement(user));
			}

			Collections.sort(result);
			return result.toArray(new TableElement[0]);
		}
	}

	class MailLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			return ((TableElement) obj).mail;
		}
	}

	class AdminLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			return ""; //$NON-NLS-1$
		}

		@Override
		public Image getImage(Object obj) {
			if (((TableElement) obj).admin) {
				return CHECKED;
			} else {
				return UNCHECKED;
			}
		}
	}

	class ManageProblemsLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			return ""; //$NON-NLS-1$
		}

		@Override
		public Image getImage(Object obj) {
			if (((TableElement) obj).problems) {
				return CHECKED;
			} else {
				return UNCHECKED;
			}
		}
	}

	class AdminEditingSupport extends EditingSupport {

		private final TableViewer viewer;

		public AdminEditingSupport(TableViewer viewer) {
			super(viewer);
			this.viewer = viewer;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return new CheckboxCellEditor(null, SWT.CHECK | SWT.READ_ONLY);
		}

		@Override
		protected boolean canEdit(Object element) {
			return (!((TableElement) element).mail.equals(APP.ADMIN_MAIL));
		}

		@Override
		protected Object getValue(Object element) {
			return ((TableElement) element).admin;
		}

		@Override
		protected void setValue(Object element, Object value) {
			TableElement te = (TableElement) element;
			te.admin = (Boolean) value;
			te.user.setAdmin((Boolean) value);
			DAOUser.getDAO().modifyAdminPermission(te.mail, (Boolean) value);
			viewer.update(element, null);
		}
	}

	class ManageProblemsEditingSupport extends EditingSupport {

		private final TableViewer viewer;

		public ManageProblemsEditingSupport(TableViewer viewer) {
			super(viewer);
			this.viewer = viewer;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return new CheckboxCellEditor(null, SWT.CHECK | SWT.READ_ONLY);
		}

		@Override
		protected boolean canEdit(Object element) {
			return (!((TableElement) element).mail.equals(APP.ADMIN_MAIL));
		}

		@Override
		protected Object getValue(Object element) {
			return ((TableElement) element).problems;
		}

		@Override
		protected void setValue(Object element, Object value) {
			TableElement te = (TableElement) element;
			te.problems = (Boolean) value;
			te.user.setManageProblems((Boolean) value);
			DAOUser.getDAO().modifyManageProblemsPermission(te.mail,
					(Boolean) value);
			viewer.update(element, null);
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(2, false);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setLayout(layout);

		TableViewer viewer = new TableViewer(container, SWT.MULTI
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		Table table = viewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(new ViewContentProvider());
		
		TableViewerColumn tvc = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn tc = tvc.getColumn();
		tc.setText(Messages.AdminPanelDialog_User);
		tc.setImage(AbstractUIPlugin.imageDescriptorFromPlugin(
				"flintstones.gathering.cloud", "/icons/mail_20.png") //$NON-NLS-1$ //$NON-NLS-2$
				.createImage());
		tvc.setLabelProvider(new MailLabelProvider());

		tvc = new TableViewerColumn(viewer, SWT.NONE);
		tc = tvc.getColumn();
		tc.setText(Messages.AdminPanelDialog_Admin);
		tc.setImage(AbstractUIPlugin.imageDescriptorFromPlugin(
				"flintstones.gathering.cloud", "/icons/admin_20.png") //$NON-NLS-1$ //$NON-NLS-2$
				.createImage());
		tvc.setLabelProvider(new AdminLabelProvider());
		tvc.setEditingSupport(new AdminEditingSupport(viewer));

		tvc = new TableViewerColumn(viewer, SWT.NONE);
		tc = tvc.getColumn();
		tc.setText(Messages.AdminPanelDialog_Manage_problems);
		tc.setImage(PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_OBJ_ELEMENT));
		tvc.setLabelProvider(new ManageProblemsLabelProvider());
		tvc.setEditingSupport(new ManageProblemsEditingSupport(viewer));

		Label deployLabel = new Label(container, SWT.NONE);
		deployLabel.setText(Messages.AdminPanelDialog_Deploy_application);

		Button deployButton = new Button(container, SWT.NONE);
		deployButton.setToolTipText(Messages.AdminPanelDialog_Do_deploy_application);
		deployButton.setText(Messages.AdminPanelDialog_Deploy);
		deployButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (MessageDialog
						.openConfirm(
								Display.getCurrent().getActiveShell(),
								Messages.AdminPanelDialog_Do_deploy_application,
								Messages.AdminPanelDialog_This_action_can_not_be_undone)) {
					Deploy.make();
					PlatformUI.getWorkbench().close();
				}
			}
		});
		
		viewer.setInput(model);
		packColumns(viewer);
		
		return area;
	}

	private void packColumns(TableViewer viewer) {
		for (TableColumn column : viewer.getTable().getColumns()) {
			column.pack();
			column.setWidth(column.getWidth() + 40);
		}
	}

	@Override
	protected boolean isResizable() {
		return true;
	}
}