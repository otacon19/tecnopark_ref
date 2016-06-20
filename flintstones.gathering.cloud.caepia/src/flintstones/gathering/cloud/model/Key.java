package flintstones.gathering.cloud.model;

public class Key {
	private String _alternative;
	private String _criterion;
	
	private Key() {
		_alternative = null;
		_criterion = null;
	}
	
	public Key(String alternative, String criterion) {
		this();
		setAlternative(alternative);
		setCriterion(criterion);
	}
	
	public void setAlternative(String alternative) {
		_alternative = alternative;
	}
	
	public void setCriterion(String criterion) {
		_criterion = criterion;
	}
	
	public String getAlternative() {
		return _alternative;
	}
	
	public String getCriterion() {
		return _criterion;
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
		} else if (!(obj instanceof Key)) {
			return false;
		}
		
		Key o = (Key) obj;
		
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
		
		return true;
	}
}
