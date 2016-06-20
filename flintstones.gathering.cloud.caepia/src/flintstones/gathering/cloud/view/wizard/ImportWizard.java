package flintstones.gathering.cloud.view.wizard;

import java.util.Map;

import org.eclipse.jface.wizard.Wizard;

import flintstones.gathering.cloud.dao.DAOProblem;
import flintstones.gathering.cloud.dao.DAOUser;
import flintstones.gathering.cloud.mail.ProblemMailGenerator;
import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.model.ProblemAssignment;
import flintstones.gathering.cloud.model.User;

public class ImportWizard extends Wizard {

	private ProblemIdWizardPage idWP = new ProblemIdWizardPage();
	//private AlternativesWizardPage aWP = new AlternativesWizardPage();
	//private CriteriaWizardPage cWP = new CriteriaWizardPage();
	private ExpertsWizardPage eWP = new ExpertsWizardPage();
	private Problem _problem;
	
	public ImportWizard(Problem problem) {
		setWindowTitle("Asistente para importar problema");
		
		_problem = problem;
	}

	@Override
	public void addPages() {
		addPage(idWP);
		//addPage(aWP);
		//addPage(cWP);
		addPage(eWP);
	}

	@Override
	public boolean performFinish() {
		_problem.setId(idWP.getId());
		performAssignments();
		return true;
	}
	
	public Problem getProblem() {
		return _problem;
	}
	
	private void performAssignments() {
		Map<String, String> mails = eWP.getMails();
		DAOUser daoUser = DAOUser.getDAO();
		DAOProblem daoProblem = DAOProblem.getDAO();
		ProblemAssignment assignment;
		String mail;
		User user;
		for (String id : mails.keySet()) {
			mail = mails.get(id);
			user = daoUser.getUser(mail);
			if (user == null) {
				user = daoUser.createAccount(mail);
			}
			
			assignment = new ProblemAssignment(id, user);
			_problem.setAssignment(assignment);
			
			ProblemMailGenerator.buildAndSendInvitationProblemMail(user, _problem.getId());
		}
		daoProblem.createProblem(_problem);
	}

}
