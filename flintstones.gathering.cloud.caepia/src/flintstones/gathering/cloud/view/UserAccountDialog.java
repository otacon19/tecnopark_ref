package flintstones.gathering.cloud.view;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import flintstones.gathering.cloud.dao.DAOUser;
import flintstones.gathering.cloud.model.User;

@SuppressWarnings("serial")
public class UserAccountDialog extends TitleAreaDialog {

	private Shell shell;
	private User _user = null;
	
	private Text _mailText;
	private Text _newPassText;
	private Text _confirmNewPassText;
	
	private Label _validNewPassLabel;
	private Label _validConfirmNewPassLabel;

	private String _pass = "";
	private String _confirmPass = "";
	
	private boolean _validNewPass;
	private boolean _validConfirmPass;
	
	private static final Color GREEN = SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN);
	private static final Color RED = SWTResourceManager.getColor(SWT.COLOR_DARK_RED);
	private static final Font FONT = SWTResourceManager.getFont("Cantarell", 10, SWT.NORMAL);
	
	private static final String PASS_PATTERN = "^[a-zA-Z0-9]*$";
	public UserAccountDialog(Shell parentShell) {
		super(parentShell);
		shell = parentShell;
		setShellStyle(SWT.PRIMARY_MODAL);
		_user = (User) RWT.getUISession().getAttribute("user");
	}

	@Override
	public void create() {
		super.create();
		
		setTitle("Cuenta de usuario");
		setMessage("Modificar cuenta de usuario", IMessageProvider.INFORMATION);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		
		if (_user.getAdmin()) {
			createButton(parent, IDialogConstants.ABORT_ID, "Panel de administración",
					false);
			getButton(IDialogConstants.ABORT_ID).addSelectionListener(new SelectionAdapter() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					AdminPanelDialog apd = new AdminPanelDialog(shell);
					apd.open();
				}
			});
		}
		createButton(parent, IDialogConstants.OK_ID, "Modificar",
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.get().CANCEL_LABEL, false);
		
		getButton(IDialogConstants.OK_ID).setEnabled(false);
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

		_mailText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		_mailText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		_mailText.setText(_user.getMail());
		
		Label newPassLabel = new Label(container, SWT.NONE);
		newPassLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		newPassLabel.setText("contraseña");

		_newPassText = new Text(container, SWT.BORDER | SWT.PASSWORD);
		_newPassText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		new Label(container, SWT.NONE);
		
		_validNewPassLabel = new Label(container, SWT.NONE);
		_validNewPassLabel.setFont(FONT);

		Label confirmNewPassLabel = new Label(container, SWT.NONE);
		confirmNewPassLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		confirmNewPassLabel.setText("confirmar");

		_confirmNewPassText = new Text(container, SWT.BORDER | SWT.PASSWORD);
		_confirmNewPassText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		new Label(container, SWT.NONE);
		
		_validConfirmNewPassLabel = new Label(container, SWT.NONE);
		_validConfirmNewPassLabel.setFont(FONT);

		Composite composite = new Composite(container, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.horizontalSpacing = 1;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2,
				1));

		hookListeners();
		validateFull();

		return area;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void okPressed() {
		DAOUser.getDAO().changePass(_user, _pass);
		_user.setPass(_pass);
		RWT.getUISession().setAttribute("user", _user);
		super.okPressed();
	}

	private void hookListeners() {
		_newPassText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent event) {
				_pass = _newPassText.getText();
				_validNewPass = validatePass();
				_validConfirmPass = validateConfirmPass();
				validate();
			}
		});
		_confirmNewPassText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent event) {
				_confirmPass = _confirmNewPassText.getText();
				_validConfirmPass = validateConfirmPass();
				validate();
			}
		});

	}
	
	private void setDecorator(Label label, String text, boolean result) {
		label.setText(text);
		label.pack();
		if (result) {
			label.setForeground(GREEN);
		} else {
			label.setForeground(RED);
		}
	}
	
	private boolean validatePass() {
		boolean result = false;
		
		String text = null;
		
		if (!_pass.isEmpty()) {
			if (_pass.length() == 8) {
				if (_pass.matches(PASS_PATTERN)) {
					text = "OK";
					result = true;
				} else {
					text = "La contraseña debe estar formada por 8 carácteres alfanuméricos";
				}
			} else {
				text = "La contraseña debe estar formada por 8 carácteres alfanuméricos";
			}
		} else {
			text = "Insertar contraseña";
		}
		
		setDecorator(_validNewPassLabel, text, result);
		return result;
	}
	
	private boolean validateConfirmPass() {
		boolean result = false;
		
		String text = null;
		
		if (!_confirmPass.isEmpty()) {
			if (_pass.equals(_confirmPass)) {
				text = "OK";
				result = true;
			} else {
				text = "Contraseña inválida";
			}
		} else {
			text = "Insertar contraseña";
		}
		
		setDecorator(_validConfirmNewPassLabel, text, result);
		return result;
	}

	private void validate() {
		getButton(IDialogConstants.OK_ID).setEnabled(_validNewPass && _validConfirmPass);
	}

	private void validateFull() {
		_validNewPass = validatePass();
		_validConfirmPass = validateConfirmPass();
	}
}