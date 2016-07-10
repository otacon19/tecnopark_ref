package flintstones.gathering.cloud.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.ICommandIds;
import flintstones.gathering.cloud.xml.ExportXML;

/**
 * When run, this action will open another instance of a view.
 */
@SuppressWarnings("serial")
public class ExportProblemAction extends Action {
	
	private final IWorkbenchWindow window;

	public ExportProblemAction(IWorkbenchWindow window) {
		super("Exportar problema seleccionado");
		this.window = window;
		setToolTipText("Exportar problema seleccionado");
		setText("");
		setId(ICommandIds.CMD_EXPORT_PROBLEM);
		setActionDefinitionId(ICommandIds.CMD_EXPORT_PROBLEM);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/export.png"));
	}

	public void run() {
		FileDialog fileDialog = new FileDialog(window.getShell(), SWT.SAVE);
		fileDialog.setText("Exportar problema");
		fileDialog.open();
		String[] fileNames = fileDialog.getFileNames();
		
		if (fileNames.length == 1) {
			String file = fileNames[0];
			ExportXML exportXML = new ExportXML(file);
			try {
				exportXML.createExportFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
