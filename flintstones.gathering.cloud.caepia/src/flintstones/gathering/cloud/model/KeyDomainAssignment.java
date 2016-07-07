package flintstones.gathering.cloud.model;

public class KeyDomainAssignment {
	private String _alternative;
	private String _criterion;
	private String _expert;
	
	private KeyDomainAssignment() {
		_alternative = null;
		_criterion = null;
	}
	
	public KeyDomainAssignment(String alternative, String criterion, String expert) {
		this();
		setAlternative(alternative);
		setCriterion(criterion);
		setExpert(expert);
	}
	
	public void setAlternative(String alternative) {
		_alternative = alternative;
	}
	
	public void setCriterion(String criterion) {
		_criterion = criterion;
	}
	
	public void setExpert(String expert) {
		_expert = expert;
	}
	
	public String getAlternative() {
		return _alternative;
	}
	
	public String getCriterion() {
		return _criterion;
	}
	
	public String getExpert() {
		return _expert;
	}
	
	@Override
	public int hashCode() {
		if ((_alternative == null) && (_criterion == null)) {
			return super.hashCode();
		} else if (_alternative == null) {
			return _criterion.hashCode();
		} else if (_criterion == null) {
			return _alternative.hashCode();
		} else {
			return new String(_alternative + _criterion).hashCode();
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null) {
			return false;
		} else if (!(obj instanceof KeyDomainAssignment)) {
			return false;
		}
		
		KeyDomainAssignment o = (KeyDomainAssignment) obj;
		
		if (_criterion == null) {
			if (o._criterion != null) {
				return false;
			}
		} else {
			if (o._criterion == null) {
				return false;
			} else {
				if (!_criterion.equals(o._criterion)) {
					return false;
				}
			}
		}
		
		if (_alternative == null) {
			if (o._alternative != null) {
				return false;
			}
		} else {
			if (o._alternative == null) {
				return false;
			} else {
				if (!_alternative.equals(o._alternative)) {
					return false;
				}
			}
		}
		
		if (_expert == null) {
			if (o._expert != null) {
				return false;
			}
		} else {
			if (o._expert == null) {
				return false;
			} else {
				if (!_expert.equals(o._expert)) {
					return false;
				}
			}
		}
		
		return true;
	}
}
