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
import flintstones.gathering.cloud.view.ProblemsView;
import flintstones.gathering.cloud.view.wizard.ImportWizard;
import flintstones.gathering.cloud.xml.ReadXML;

/**
 * When run, this action will open another instance of a view.
 */
@SuppressWarnings("serial")
public class ImportProblemActionMenu extends Action {

	private final IWorkbenchWindow window;

	public ImportProblemActionMenu(IWorkbenchWindow window) {
		super("Importar problema");
		this.window = window;
		setToolTipText("Importar problema");
		setText("Importar problema");
		setId(ICommandIds.CMD_IMPORT_PROBLEM_MENU);
		setActionDefinitionId(ICommandIds.CMD_IMPORT_PROBLEM_MENU);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"flintstones.gathering.cloud", "/icons/import.png"));
	}

	public void run() {
		FileDialog fileDialog = new FileDialog(window.getShell(), SWT.TITLE);
		fileDialog.setText("Importar problema");
		fileDialog.open();
		String[] fileNames = fileDialog.getFileNames();

		if (fileNames.length == 1) {
			String file = fileNames[0];
			if (!file.endsWith("flintstones")) {
				MessageDialog.openError(window.getShell(), "Archivo inválido",
						"Archivo inválido");
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
					MessageDialog.openError(window.getShell(), "Archivo inválido",
							"Archivo inválido");
				}
			}
		}
	}
}
