package mcdacw.valuation.domain.type;

import mcdacw.valuation.domain.Domain;

public abstract class Numeric extends Domain {
	
	protected boolean _inRange;
	
	public Numeric() {
		super();
		_inRange = true;
	}
	
	public void setInRange(Boolean inRange) {
		_inRange = inRange;
	}
	
	public boolean getInRange() {
		return _inRange;
	}
	
	@Override
	public Object clone() {
		Numeric result = null;
		result = (Numeric) super.clone();
		
		return result;
	}

}
