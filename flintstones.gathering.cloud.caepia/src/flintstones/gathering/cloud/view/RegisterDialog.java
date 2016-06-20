package flintstones.gathering.cloud.view;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
public class RegisterDialog extends TitleAreaDialog {

	private User _user = null;
	
	private Text _mailText;
	private Text _passText;
	private Text _confirmPassText;
	
	private Label _validMailLabel;
	private Label _validPassLabel;
	private Label _validConfirmPassLabel;

	private String _mail = "";
	private String _pass = "";
	private String _confirmPass = "";
	
	private boolean _validMail;
	private boolean _validPass;
	private boolean _validConfirmPass;
	
	private static final Color GREEN = SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN);
	private static final Color RED = SWTResourceManager.getColor(SWT.COLOR_DARK_RED);
	private static final Font FONT = SWTResourceManager.getFont("Cantarell", 10, SWT.NORMAL);

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private static final String PASS_PATTERN = "^[a-zA-Z0-9]*$";
	public RegisterDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.PRIMARY_MODAL);
	}

	@Override
	public void create() {
		super.create();
		setTitle("Registro");
		setMessage("Insertar datos de usuario", IMessageProvider.INFORMATION);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
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

		_mailText = new Text(container, SWT.BORDER);
		_mailText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		new Label(container, SWT.NONE);
		
		_validMailLabel = new Label(container, SWT.NONE);
		_validMailLabel.setFont(FONT);

		Label passLabel = new Label(container, SWT.NONE);
		passLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		passLabel.setText("contraseña");

		_passText = new Text(container, SWT.BORDER | SWT.PASSWORD);
		_passText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		new Label(container, SWT.NONE);
		
		_validPassLabel = new Label(container, SWT.NONE);
		_validPassLabel.setFont(FONT);

		Label confirmPassLabel = new Label(container, SWT.NONE);
		confirmPassLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		confirmPassLabel.setText("confirmar");

		_confirmPassText = new Text(container, SWT.BORDER | SWT.PASSWORD);
		_confirmPassText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		new Label(container, SWT.NONE);
		
		_validConfirmPassLabel = new Label(container, SWT.NONE);
		_validConfirmPassLabel.setFont(FONT);

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
		_user = new User(_mail, _pass, false, false);
		DAOUser.getDAO().createUser(_user);
		super.okPressed();
	}

	private void hookListeners() {
		_mailText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent event) {
				_mail = _mailText.getText();
				_validMail = validateMail();
				validate();
			}
		});
		_passText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent event) {
				_pass = _passText.getText();
				_validPass = validatePass();
				_validConfirmPass = validateConfirmPass();
				validate();
			}
		});
		_confirmPassText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent event) {
				_confirmPass = _confirmPassText.getText();
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
	
	private boolean validateMail() {
		boolean result = false;
		
		String text = null;
		
		if (!_mail.isEmpty()) {
			if (_mail.matches(EMAIL_PATTERN)) {
				if (DAOUser.getDAO().getUser(_mail) == null) {
					text = "OK";
					result = true;
				} else {
					text = "mail ya registrado";
				}
			} else {
				text = "mail inválido";
			}
		} else {
			text = "Insertar mail";
		}
		
		setDecorator(_validMailLabel, text, result);
		return result;
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
		
		setDecorator(_validPassLabel, text, result);
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
			text = "Contraseña inválida";
		}
		
		setDecorator(_validConfirmPassLabel, text, result);
		return result;
	}

	private void validate() {
		getButton(IDialogConstants.OK_ID).setEnabled(_validMail && _validPass && _validConfirmPass);
	}

	private void validateFull() {
		_validMail = validateMail();
		_validPass = validatePass();
		_validConfirmPass = validateConfirmPass();

	}
	
	public User getUser() {
		return _user;
	}
}