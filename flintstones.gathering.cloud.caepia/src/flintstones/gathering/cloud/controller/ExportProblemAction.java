package flintstones.gathering.cloud.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.ICommandIds;

/**
 * When run, this action will open another instance of a view.
 */
@SuppressWarnings("serial")
public class ExportProblemAction extends Action {
	
	@SuppressWarnings("unused")
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
	}
}
