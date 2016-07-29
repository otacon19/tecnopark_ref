package mcdacw.valuation.valuation;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import mcdacw.paremetervalidator.ParameterValidator;
import mcdacw.valuation.domain.Domain;
import mcdacw.valuation.domain.fuzzyset.FuzzySet;
import mcdacw.valuation.domain.numeric.NumericIntegerDomain;

public class IntegerIntervalValuation extends Valuation {

	public static final String ID = "flintstones.valuation.integer.interval"; //$NON-NLS-1$
	
	public double _min;
	public double _max;
	
	public IntegerIntervalValuation() {
		super();
		_min = 0;
		_max = 0;
	}
	
	public IntegerIntervalValuation(NumericIntegerDomain domain, long min, long max) {
		super();
		_domain = domain;
		_min = min;
		_max = max;
	}
	
	public void setMin(Long min) {
		_min = min;
	}
	
	public double getMin() {
		return _min;
	}
	
	public void setMax(Long max) {
		_max = max;
	}
	
	public double getMax() {
		return _max;
	}
	
	public void setMinMax(Double min, Double max) {
		ParameterValidator.notNull(_domain, "min-max");
		ParameterValidator.notDisorder(new double[] {min,  max}, "min-max", false);
		
		_min = min;
		_max = max;
	}
	
	@Override
	public Valuation negateValuation() {
		IntegerIntervalValuation result = (IntegerIntervalValuation) clone();
		
		long aux = Math.round(((NumericIntegerDomain) _domain).getMin()) + Math.round(((NumericIntegerDomain) _domain).getMax());
		result.setMinMax(aux - _max, aux - _min);
		
		return result;
	}
	
	@Override
	public FuzzySet unification(Domain fuzzySet) {
		return null;
	}
	
	@Override
	public String toString() {
		return ("Integer interval[" + _min + "," + _max + "] in " + _domain.toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(this == obj) {
			return true;
		}
		
		if(obj == null) {
			return false;
		}
		
		if(obj.getClass() != this.getClass()) {
			return false;
		}
		
		final IntegerIntervalValuation other = (IntegerIntervalValuation) obj;
		
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(_max, other._max);
		eb.append(_min, other._min);
		eb.append(_domain, other._domain);
		
		return eb.isEquals();
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder(17, 31);
		hcb.append(_max);
		hcb.append(_min);
		hcb.append(_domain);
		return hcb.toHashCode();
	}
	
	@Override
	public int compareTo(Valuation other) {
		ParameterValidator.notNull(other, "other");
		ParameterValidator.notIllegalElementType(other, new String[] {Integer.class.toString()}, "other");
		
		if(_domain.equals(other.getDomain())) {
			double middle = (_max + _min) / 2l;
			double otherMidle = (((IntegerIntervalValuation) other)._max + ((IntegerIntervalValuation) other)._min) / 2l;
			return Double.valueOf(middle).compareTo(Double.valueOf(otherMidle));
		} else {
			throw new IllegalArgumentException("Different domains");
		}
	}
	
	@Override
	public Object clone() {
		IntegerIntervalValuation result = null;
		result = (IntegerIntervalValuation) super.clone();
		result._min = new Double(_min);
		result._max = new Double(_max);
		
		return result;
	}

	@Override
	public String changeFormatValuationToString() {
		return "[" + Long.toString((long) _min) + ", " + Long.toString((long) _max) + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	@Override
	public void save(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeAttribute("min", Long.toString((long) _min)); //$NON-NLS-1$
		writer.writeAttribute("max", Long.toString((long) _max));	 //$NON-NLS-1$
	}
}
