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
public class ExportProblemAction extends Action {
	
	@SuppressWarnings("unused")
	private final IWorkbenchWindow window;

	public ExportProblemAction(IWorkbenchWindow window) {
		super(Messages.ExportProblemAction_Export_selected_problem);
		this.window = window;
		setToolTipText(Messages.ExportProblemAction_Export_selected_problem);
		setText(""); //$NON-NLS-1$
		setId(ICommandIds.CMD_EXPORT_PROBLEM);
		setActionDefinitionId(ICommandIds.CMD_EXPORT_PROBLEM);
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/export.png")); //$NON-NLS-1$ //$NON-NLS-2$
		RWT.getUISession().setAttribute("export-action", this); //$NON-NLS-1$
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
