package flintstones.gathering.cloud.view;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import flintstones.gathering.cloud.APP;
import flintstones.gathering.cloud.dao.DAOUser;
import flintstones.gathering.cloud.model.User;

@SuppressWarnings("serial")
public class LoginDialog extends TitleAreaDialog {

	public static final int REGISTER_ID = 3000;
	private User _user = null;

	private Text _mailText;
	private Text _passText;

	public LoginDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.PRIMARY_MODAL);
	}

	@Override
	public void create() {
		super.create();
		setTitle("Login");
		setMessage("Insertar datos de usuario", IMessageProvider.INFORMATION);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, REGISTER_ID, "Registrar", false);
		createButton(parent, IDialogConstants.OK_ID, "Login", true);

		getButton(REGISTER_ID).addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				getShell().setVisible(false);
				RegisterDialog dialog = new RegisterDialog(getShell());
				if (dialog.open() == IDialogConstants.OK_ID) {
					_user = dialog.getUser();
					autoLogin();
				} else {
					getShell().setVisible(true);
				}
				e.doit = false;
			}
		});

		if (APP.DEVELOP) {
			createButton(parent, IDialogConstants.ABORT_ID, "Modo de desarrollo", false);
			getButton(IDialogConstants.ABORT_ID).addSelectionListener(
					new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							_user = DAOUser.getDAO().getUser(APP.ADMIN_MAIL);
							LoginDialog.super.okPressed();
						}
					});
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

		Label mailLabel = new Label(container, SWT.NONE);
		mailLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		mailLabel.setText("mail");

		_mailText = new Text(container, SWT.BORDER);
		_mailText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Label passLabel = new Label(container, SWT.NONE);
		passLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		passLabel.setText("contraseña");

		_passText = new Text(container, SWT.BORDER | SWT.PASSWORD);
		_passText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Composite composite = new Composite(container, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.horizontalSpacing = 1;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2,
				1));

		return area;
	}

	@Override
	protected void okPressed() {
		DAOUser dao = DAOUser.getDAO();
		_user = dao.getUser(_mailText.getText(), _passText.getText());
		if (_user == null) {
			MessageDialog.openError(getShell(), "Usuario inválido", "Usuario inválido");
		} else {
			super.okPressed();
		}
	}

	public void autoLogin() {
		super.okPressed();
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	public User getUser() {
		return _user;
	}
}