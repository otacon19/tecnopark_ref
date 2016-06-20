package flintstones.gathering.cloud.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.ICommandIds;

/**
 * When run, this action will open another instance of a view.
 */
@SuppressWarnings("serial")
public class ExportProblemActionMenu extends Action {

	@SuppressWarnings("unused")
	private final IWorkbenchWindow window;

	public ExportProblemActionMenu(IWorkbenchWindow window) {
		super("Exportar problema seleccionado");
		this.window = window;
		setToolTipText("Exportar problema seleccionado");
		setText("Exportar problema seleccionado");
		setId(ICommandIds.CMD_EXPORT_PROBLEM_MENU);
		setActionDefinitionId(ICommandIds.CMD_EXPORT_PROBLEM_MENU);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"flintstones.gathering.cloud", "/icons/export.png"));
	}

	public void run() {
	}
}
