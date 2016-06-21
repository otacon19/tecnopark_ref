package mcdacw.valuation.domain.type;

import mcdacw.valuation.domain.Domain;

public abstract class Linguistic extends Domain {
	
	public Linguistic() {
		super();
	}
	
	@Override
	public Object clone() {
		Linguistic result = null;
		result = (Linguistic) super.clone();
		
		return result;
	}
	

}
