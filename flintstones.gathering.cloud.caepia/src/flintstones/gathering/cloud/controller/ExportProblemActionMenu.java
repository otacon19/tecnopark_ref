package flintstones.gathering.cloud.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jface.action.Action;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.ICommandIds;
import flintstones.gathering.cloud.download.DownloadService;
import flintstones.gathering.cloud.nls.Messages;
import flintstones.gathering.cloud.xml.ExportXML;

/**
 * When run, this action will open another instance of a view.
 */
@SuppressWarnings("serial")
public class ExportProblemActionMenu extends Action {

	@SuppressWarnings("unused")
	private final IWorkbenchWindow window;

	public ExportProblemActionMenu(IWorkbenchWindow window) {
		super(Messages.ExportProblemActionMenu_Export_problem_selected0);
		this.window = window;
		setToolTipText(Messages.ExportProblemActionMenu_Export_problem_selectedExport_problem_selected);
		setText(Messages.ExportProblemActionMenu_Export_problem_selectedExport_problem_selected);
		setId(ICommandIds.CMD_EXPORT_PROBLEM_MENU);
		setActionDefinitionId(ICommandIds.CMD_EXPORT_PROBLEM_MENU);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"flintstones.gathering.cloud", "/icons/export.png")); //$NON-NLS-1$ //$NON-NLS-2$
		RWT.getUISession().setAttribute("export-action-menu", this); //$NON-NLS-1$
	}

	public void run() {
		File file = new File("flintstones_problem.flintstones"); //$NON-NLS-1$
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
