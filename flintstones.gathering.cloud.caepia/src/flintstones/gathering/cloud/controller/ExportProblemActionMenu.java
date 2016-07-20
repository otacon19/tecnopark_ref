package flintstones.gathering.cloud.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.ICommandIds;
import flintstones.gathering.cloud.download.DownloadService;
import flintstones.gathering.cloud.xml.ExportXML;

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
		File file = new File("flintstones_problem.flintstones");
		ExportXML exportXML = new ExportXML(file.getName());
		try {
			exportXML.createExportFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Path path = Paths.get(file.getAbsolutePath());
		byte[] data = null;
		try {
			data = Files.readAllBytes(path);
			DownloadService.sendDownload(data, file.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
