package flintstones.gathering.cloud;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import flintstones.gathering.cloud.controller.LoginAction;
import flintstones.gathering.cloud.model.User;
import sinbad2.database.DatabaseManager;

/**
 * Configures the initial size and appearance of a workbench window.
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	
	public ApplicationWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		
		configureDatabase();
		
		IWorkbenchWindow window = getWindowConfigurer().getWindow();
		LoginAction action = new LoginAction("Login", window); //$NON-NLS-1$
		action.run();
		User user = action.getUser();

		if (user != null) {
			RWT.getUISession().setAttribute("user", user); //$NON-NLS-1$
			IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
			configurer.setInitialSize(new Point(600, 400));
			configurer.setShowCoolBar(true);
			configurer.setShowStatusLine(false);
			configurer.setTitle(APP.APP_NAME);
		} else {
			preWindowOpen();
		}
	}

	private void configureDatabase() {
		DatabaseManager.getInstance().getApplicationDatabase();
	}
}
