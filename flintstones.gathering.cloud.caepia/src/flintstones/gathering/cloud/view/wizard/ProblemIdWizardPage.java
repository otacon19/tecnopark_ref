package flintstones.gathering.cloud.view.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

import flintstones.gathering.cloud.dao.DAOProblem;
import flintstones.gathering.cloud.nls.Messages;

@SuppressWarnings("serial")
public class ProblemIdWizardPage extends WizardPage {
	
	private DAOProblem daoProblem = DAOProblem.getDAO();
	private Text problemIdText;

	private String id = ""; //$NON-NLS-1$
	private boolean pageComplete = false;
	
	/**
	 * Create the wizard.
	 */
	public ProblemIdWizardPage() {
		super("wizardPage"); //$NON-NLS-1$
		setTitle(Messages.ProblemIdWizardPage_Set_problem_id);
		setDescription(null);
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		
		container.setLayout(new GridLayout(2, false));		
		Label problemIdLabel = new Label(container, SWT.NONE);
		problemIdLabel.setText(Messages.ProblemIdWizardPage_Problem_id);
		
		problemIdText = new Text(container, SWT.BORDER);
		problemIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		problemIdText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent event) {
				id = problemIdText.getText();
				validate();
			}
		});
		
		validate();
		
	}
	
	public void validate() {
		if (!id.isEmpty()) {
			if (daoProblem.getProblem(id) == null) {
				setErrorMessage(null);
				pageComplete = true;
			} else {
				pageComplete = false;
				setErrorMessage(Messages.ProblemIdWizardPage_Duplicated_id);
			}
		} else {
			pageComplete = false;
			setErrorMessage(Messages.ProblemIdWizardPage_Empty_id);
		}
		
		setPageComplete(pageComplete);
	}
	
	public String getId() {
		return id;
	}
	
}
