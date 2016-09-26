package flintstones.gathering.cloud;

/**
 * Interface defining the application's command IDs.
 * Key bindings can be defined for specific commands.
 * To associate an action with a command, use IAction.setActionDefinitionId(commandId).
 *
 * @see org.eclipse.jface.action.IAction#setActionDefinitionId(String)
 */
public interface ICommandIds {

	public static final String CMD_OPEN = "flintstones.gathering.cloud.open"; //$NON-NLS-1$
	public static final String CMD_CLOSE = "flintstones.gathering.cloud.close"; //$NON-NLS-1$
	public static final String CMD_REFRESH = "flintstones.gathering.cloud.refreshAction"; //$NON-NLS-1$
	public static final String CMD_LOGIN_ACTION = "flintstones.gathering.cloud.loginAction"; //$NON-NLS-1$
	public static final String CMD_REGISTER_ACTION = "flintstones.gathering.cloud.registerAction"; //$NON-NLS-1$
	public static final String CMD_USER_ACCOUNT = "flintstones.gathering.cloud.userAccount"; //$NON-NLS-1$
	public static final String CMD_IMPORT_PROBLEM = "flintstones.gathering.cloud.importProblem"; //$NON-NLS-1$
	public static final String CMD_IMPORT_PROBLEM_MENU = "flintstones.gathering.cloud.importProblemMenu"; //$NON-NLS-1$
	public static final String CMD_EXPORT_PROBLEM = "flintstones.gathering.cloud.exportProblem"; //$NON-NLS-1$
	public static final String CMD_EXPORT_PROBLEM_MENU = "flintstones.gathering.cloud.exportProblemMenu"; //$NON-NLS-1$
	public static final String CMD_OPEN_GATHERING_PERSPECTIVE = "flintstones.gathering.cloud.openGatheringPerspective"; //$NON-NLS-1$
	public static final String CMD_OPEN_FRAMEWORK_STRUCTURING_PERSPECTIVE = "flintstones.gathering.cloud.openFrameworkStructuringPerspective"; //$NON-NLS-1$
	public static final String CMD_OPEN_PROBLEMS_PERSPECTIVE = "flintstones.gathering.cloud.openProblemsPerspective"; //$NON-NLS-1$

}
