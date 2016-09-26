package flintstones.gathering.cloud.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.ICommandIds;
import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.nls.Messages;
import flintstones.gathering.cloud.view.ProblemsView;
import flintstones.gathering.cloud.view.wizard.ImportWizard;
import flintstones.gathering.cloud.xml.ReadXML;

/**
 * When run, this action will open another instance of a view.
 */
@SuppressWarnings("serial")
public class ImportProblemAction extends Action {

	private final IWorkbenchWindow window;

	public ImportProblemAction(IWorkbenchWindow window) {
		super(Messages.ImportProblemAction_Import_problem);
		this.window = window;
		setToolTipText(Messages.ImportProblemAction_Import_problem);
		setText(""); //$NON-NLS-1$
		setId(ICommandIds.CMD_IMPORT_PROBLEM);
		setActionDefinitionId(ICommandIds.CMD_IMPORT_PROBLEM);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/import.png")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void run() {
		FileDialog fileDialog = new FileDialog(window.getShell(), SWT.TITLE);
		fileDialog.setText(Messages.ImportProblemAction_Import_problem);
		fileDialog.open();
		String[] fileNames = fileDialog.getFileNames();

		if (fileNames.length == 1) {
			String file = fileNames[0];
			if (!file.endsWith("flintstones")) { //$NON-NLS-1$
				MessageDialog.openError(window.getShell(), Messages.ImportProblemAction_Invalid_file, Messages.ImportProblemAction_Invalid_file);
			} else {
				try {
					ReadXML readXML = new ReadXML(file);
					Problem problem = readXML.getProblem();
					ImportWizard iw = new ImportWizard(problem);
					WizardDialog wizard = new WizardDialog(window.getShell(), iw);
					if (wizard.open() == Window.OK) {
						ProblemsView pv = (ProblemsView) RWT.getUISession().getAttribute(ProblemsView.ID);
						pv.refreshModel();
					}
				} catch (Exception e) {
					MessageDialog.openError(window.getShell(), Messages.ImportProblemAction_Invalid_file, Messages.ImportProblemAction_Invalid_file);
				}
			}
		}
	}
}
