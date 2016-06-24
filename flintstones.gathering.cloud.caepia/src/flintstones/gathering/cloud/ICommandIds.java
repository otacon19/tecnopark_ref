package flintstones.gathering.cloud;

/**
 * Interface defining the application's command IDs.
 * Key bindings can be defined for specific commands.
 * To associate an action with a command, use IAction.setActionDefinitionId(commandId).
 *
 * @see org.eclipse.jface.action.IAction#setActionDefinitionId(String)
 */
public interface ICommandIds {

	public static final String CMD_OPEN = "flintstones.gathering.cloud.open";
	public static final String CMD_CLOSE = "flintstones.gathering.cloud.close";
	public static final String CMD_REFRESH = "flintstones.gathering.cloud.refreshAction";
	public static final String CMD_LOGIN_ACTION = "flintstones.gathering.cloud.loginAction";
	public static final String CMD_REGISTER_ACTION = "flintstones.gathering.cloud.registerAction";
	public static final String CMD_USER_ACCOUNT = "flintstones.gathering.cloud.userAccount";
	public static final String CMD_IMPORT_PROBLEM = "flintstones.gathering.cloud.importProblem";
	public static final String CMD_IMPORT_PROBLEM_MENU = "flintstones.gathering.cloud.importProblemMenu";
	public static final String CMD_EXPORT_PROBLEM = "flintstones.gathering.cloud.exportProblem";
	public static final String CMD_EXPORT_PROBLEM_MENU = "flintstones.gathering.cloud.exportProblemMenu";
	public static final String CMD_OPEN_GATHERING_PERSPECTIVE = "flintstones.gathering.cloud.openGatheringPerspective";
	public static final String CMD_OPEN_FRAMEWORK_STRUCTURING_PERSPECTIVE = "flintstones.gathering.cloud.openFrameworkStructuringPerspective";
	public static final String CMD_OPEN_PROBLEMS_PERSPECTIVE = "flintstones.gathering.cloud.openProblemsPerspective";

}
