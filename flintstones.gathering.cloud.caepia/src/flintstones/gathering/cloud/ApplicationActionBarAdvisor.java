package flintstones.gathering.cloud;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.controller.ExportProblemAction;
import flintstones.gathering.cloud.controller.ExportProblemActionMenu;
import flintstones.gathering.cloud.controller.ImportProblemAction;
import flintstones.gathering.cloud.controller.ImportProblemActionMenu;
import flintstones.gathering.cloud.controller.OpenFrameworkStructuringPerspective;
import flintstones.gathering.cloud.controller.OpenGatheringPerspective;
import flintstones.gathering.cloud.controller.OpenProblemsPerspective;
import flintstones.gathering.cloud.controller.RefreshAction;
import flintstones.gathering.cloud.controller.UserAccountAction;
import flintstones.gathering.cloud.model.User;
import flintstones.gathering.cloud.nls.Messages;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	private IWorkbenchAction logoutAction;
	private IAction aboutAction;
	private RefreshAction refreshAction;
	private OpenProblemsPerspective openProblemsPerspective;
	private OpenGatheringPerspective openGatheringPerspective;
	private OpenFrameworkStructuringPerspective openFrameworkStructuringPerspective;
	
	private UserAccountAction userAccountAction;

	private ImportProblemAction importProblemAction;
	private ImportProblemActionMenu importProblemActionMenu;

	private ExportProblemAction exportProblemAction;
	private ExportProblemActionMenu exportProblemActionMenu;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(final IWorkbenchWindow window) {

		importProblemAction = new ImportProblemAction(window);
		register(importProblemAction);
		
		importProblemActionMenu = new ImportProblemActionMenu(window);
		register(importProblemActionMenu);

		exportProblemAction = new ExportProblemAction(window);
		exportProblemAction.setEnabled(false);
		register(exportProblemAction);
		
		exportProblemActionMenu = new ExportProblemActionMenu(window);
		exportProblemActionMenu.setEnabled(false);
		register(exportProblemActionMenu);		

		userAccountAction = new UserAccountAction(window);
		register(userAccountAction);
		
		logoutAction = ActionFactory.QUIT.create(window);
		logoutAction.setToolTipText(Messages.ApplicationActionBarAdvisor_Exit0);
		logoutAction.setText(""); //$NON-NLS-1$
		logoutAction.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/exit.png")); //$NON-NLS-1$ //$NON-NLS-2$
		register(logoutAction);

		refreshAction = new RefreshAction(window);
		register(refreshAction);

		openProblemsPerspective = new OpenProblemsPerspective();
		register(openProblemsPerspective);
		openProblemsPerspective.setEnabled(false);

		openFrameworkStructuringPerspective = new OpenFrameworkStructuringPerspective();
		register(openFrameworkStructuringPerspective);
		openFrameworkStructuringPerspective.setEnabled(false);
		
		openGatheringPerspective = new OpenGatheringPerspective();
		register(openGatheringPerspective);

		aboutAction = new AboutAction(window);
		aboutAction.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("flintstones.gathering.cloud", "/icons/about.png")); //$NON-NLS-1$ //$NON-NLS-2$
		register(aboutAction);
	}

	protected void fillMenuBar(IMenuManager menuBar) {

		User user = (User) RWT.getUISession().getAttribute("user"); //$NON-NLS-1$
		boolean canManageProblems = user.getManageProblems();
		MenuManager problemMenu = null;
		if (canManageProblems) {
			problemMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_ExitProblems, IWorkbenchActionConstants.M_FILE);
		}
		MenuManager helpMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_ExitHelp, IWorkbenchActionConstants.M_HELP);

		if (canManageProblems) {
			menuBar.add(problemMenu);
			menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		}
		menuBar.add(helpMenu);

		// Problem
		if (canManageProblems) {
			problemMenu.add(importProblemActionMenu);
			problemMenu.add(exportProblemActionMenu);
			problemMenu.add(refreshAction);
		}

		// Help
		helpMenu.add(aboutAction);
	}

	protected void fillCoolBar(ICoolBarManager coolBar) {
		IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
		coolBar.add(new ToolBarContributionItem(toolbar, "main")); //$NON-NLS-1$

		User user = (User) RWT.getUISession().getAttribute("user"); //$NON-NLS-1$
		boolean canManageProblems = user.getManageProblems();
		if (canManageProblems) {
			toolbar.add(importProblemAction);
			toolbar.add(exportProblemAction);
			toolbar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
			toolbar.add(openProblemsPerspective);
			toolbar.add(openGatheringPerspective);
			toolbar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		} else {
			toolbar.add(openFrameworkStructuringPerspective);
			toolbar.add(openGatheringPerspective);
		}
		toolbar.add(userAccountAction);
		toolbar.add(logoutAction);
	}
}
