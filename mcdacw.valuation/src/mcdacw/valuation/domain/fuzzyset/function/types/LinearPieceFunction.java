package mcdacw.valuation.domain.fuzzyset.function.types;

import org.apache.commons.lang.builder.HashCodeBuilder;

import mcdacw.paremetervalidator.ParameterValidator;
import mcdacw.valuation.domain.fuzzyset.function.IFragmentFunction;


public class LinearPieceFunction implements IFragmentFunction {
	
	private final static double EPSILON = 0.00001;
	
	private double _slope;
	private double _cutOffY;
	
	public LinearPieceFunction(Double slope, Double cutOff) {
		_slope = slope;
		_cutOffY = cutOff;
	}
	
	public double getSlope() {
		return _slope;
	}
	
	public double getCutOffY() {
		return _cutOffY;
	}
	
	@Override
	public IFragmentFunction sumFunctions(IFragmentFunction other) {
		ParameterValidator.notNull(other, "other"); //$NON-NLS-1$
		ParameterValidator.notIllegalElementType(other, new String[] { this.
				getClass().toString() }, "other"); //$NON-NLS-1$
		
		return new LinearPieceFunction(_slope + ((LinearPieceFunction) other)._slope, 
				_cutOffY + ((LinearPieceFunction) other)._cutOffY);
	}
	
	@Override
	public String toString() {
		return (_cutOffY < 0) ? (_slope + "x " + _cutOffY) : (_slope + "x + " + _cutOffY); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(this == obj) {
			return true;
		}
		
		if(obj == null) {
			return false;
		}
		
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		
		final LinearPieceFunction other = (LinearPieceFunction) obj;
		
		if(Math.abs(_slope - other._slope) < EPSILON) {
			if(Math.abs(_cutOffY - other._cutOffY) < EPSILON) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder(17, 31);
		hcb.append(_cutOffY);
		hcb.append(_slope);
		return hcb.hashCode();
		
	}

}
