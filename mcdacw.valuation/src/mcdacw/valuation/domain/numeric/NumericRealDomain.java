package mcdacw.valuation.domain.numeric;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import mcdacw.paremetervalidator.ParameterValidator;
import mcdacw.valuation.domain.type.Numeric;

public class NumericRealDomain extends Numeric {
	
public static final String ID = "flintstones.domain.numeric.real"; //$NON-NLS-1$
	
	private Double _min;
	private Double _max;
	
	public NumericRealDomain() {
		super();
		_min = 0d;
		_max = 0d;
	}
	
	public void setMin(Double min) {
		_min = min;
	}
	
	public double getMin() {
		return _min;
	}
	
	public void setMax(Double max) {
		_max = max;
	}
	
	public double getMax() {
		return _max;
	}

	public void setMinMax(Double min, Double max) {
		ParameterValidator.notDisorder(new double[] { min, max }, "min-max", false);
		_min = min;
		_max = max;
	}

	@Override
	public double midpoint() {
		return ((double) (_max + _min)) / 2d;
	}

	@Override
	public String formatDescriptionDomain() {
		String prefix = "(I) "; //$NON-NLS-1$
		
		if(!_inRange) {
			return prefix + "numeric real domain without range";
		} else {
			return prefix + toString();
		}
	}
	
	@Override
	public String toString() {
		return "[" + _min + ", " + _max + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(this == obj) {
			return true;
		}
		
		if(obj == null) {
			return false;
		}
		
		if((obj.getClass() != this.getClass()) || !super.equals(obj)) {
			return false;
		}
		
		final NumericRealDomain other = (NumericRealDomain) obj;
		
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(_inRange, other._inRange);
		eb.append(_max, other._max);
		eb.append(_min, other._min);
		
		return eb.isEquals();
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder(17, 31);
		hcb.append(super.hashCode());
		hcb.append(_inRange);
		hcb.append(_max);
		hcb.append(_min);
		return hcb.toHashCode();
	}
	
	@Override
	public Object clone() {
		NumericRealDomain result = null;
		
		result = (NumericRealDomain) super.clone();
		
		return result;
	}
		
	@Override
	public void save(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeAttribute("inRange", Boolean.toString(_inRange)); //$NON-NLS-1$
		writer.writeAttribute("min", _min.toString()); //$NON-NLS-1$
		writer.writeAttribute("max", _max.toString()); //$NON-NLS-1$
	}
}
