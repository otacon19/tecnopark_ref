package flintstones.gathering.cloud.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Problem {

	private String _id;
	private User _admin;
	private List<String> _criteria;
	private List<String> _alternatives;
	private List<String> _experts;
	private Map<String, Domain> _domains;
	private Map<Key, String> _domainAssignments;
	private Map<String, ProblemAssignment> _assignments;

	private Problem() {
		_id = null;
		_admin = null;
		_criteria = null;
		_alternatives = null;
		_experts = null;
		_domains = null;
		_domainAssignments = null;
		_assignments = null;
	}

	public Problem(String id, User admin, List<String> criteria, List<String> alternatives, List<String> experts) {
		this();
		setId(id);
		setAdmin(admin);
		setCriteria(criteria);
		setAlternatives(alternatives);
		setExperts(experts);
		initAssignments();
	}
	
	public Problem(String id, User admin, List<String> criteria, List<String> alternatives, List<String> experts, Map<String, Domain> domains, Map<Key, String> domainAssignments, Map<String, ProblemAssignment> assignments) {
		this();
		setId(id);
		setAdmin(admin);
		setCriteria(criteria);
		setAlternatives(alternatives);
		setExperts(experts);
		setDomains(domains);
		setDomainAssignments(domainAssignments);
		setAssignments(assignments);
	}
	
	private void initAssignments() {
		_assignments = new HashMap<String, ProblemAssignment>();
		for (String expert : _experts) {
			_assignments.put(expert, null);
		}
	}

	public void setId(String id) {
		_id = id;
	}

	public String getId() {
		return _id;
	}

	public void setAdmin(User admin) {
		_admin = admin;
	}

	public User getAdmin() {
		return _admin;
	}
	
	public void setCriteria(List<String> criteria) {
		_criteria = criteria;
	}
	
	public List<String> getCriteria() {
		return _criteria;
	}
	
	public void setAlternatives(List<String> alternatives) {
		_alternatives = alternatives;
	}
	
	public List<String> getAlternatives() {
		return _alternatives;
	}
	
	public void setExperts(List<String> experts) {
		_experts = experts;
	}
	
	public List<String> getExperts() {
		return _experts;
	}
	

	public void setDomains(Map<String, Domain> domains) {
		_domains = domains;
	}
	
	public Map<String, Domain> getDomains() {
		return _domains;
	}
	
	public void setDomainAssignments(Map<Key, String> domainAssignments) {
		_domainAssignments = domainAssignments;
	}
	
	public Map<Key, String> getDomainAssignments() {
		return _domainAssignments;
	}
	
	public void setAssignments(Map<String, ProblemAssignment> assignments) {
		_assignments = assignments;
	}

	public Map<String, ProblemAssignment> getAssignments() {
		return _assignments;
	}
	
	public void setAssignment(ProblemAssignment assignment) {
		_assignments.put(assignment.getId(), assignment);
	}
	
	public ProblemAssignment getAssignment(String id) {
		return _assignments.get(id);
	}

	@Override
	public String toString() {
		return _id;
	}
}
