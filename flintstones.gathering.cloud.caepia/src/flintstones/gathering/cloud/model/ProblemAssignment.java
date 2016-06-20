package flintstones.gathering.cloud.model;

public class ProblemAssignment {

	private String _id;
	private User _user;
	private Valuations _valuations;
	private boolean _make;
	
	private ProblemAssignment() {
		_id = null;
		_user = null;
		_valuations = null;
		_make = false;
	}
	
	public ProblemAssignment(String id, User user, Valuations valuations) {
		this();
		setId(id);
		setUser(user);
		setValuations(valuations);
	}
	
	public ProblemAssignment(String id, User user) {
		this(id, user, new Valuations());
	}
	
	public void setId(String id) {
		_id = id;
	}
	
	public void setUser(User user) {
		_user = user;
	}
	
	public void setValuations(Valuations valuations) {
		_valuations = valuations;
	}
	
	public String getId() {
		return _id;
	}
	
	public User getUser() {
		return _user;
	}
	
	public Valuations getValuations() {
		return _valuations;
	}
	
	public void setMake(boolean make) {
		_make = make;
	}
	
	public boolean getMake() {
		return _make;
	}
}
