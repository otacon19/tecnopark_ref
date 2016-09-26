package mcdacw.valuation.valuation;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import mcdacw.paremetervalidator.ParameterValidator;
import mcdacw.valuation.domain.Domain;
import mcdacw.valuation.domain.fuzzyset.FuzzySet;
import mcdacw.valuation.domain.numeric.NumericIntegerDomain;

public class IntegerValuation extends Valuation {
	
	public static final String ID = "flintstones.valuation.integer"; //$NON-NLS-1$
	
	public double _value;
	
	public IntegerValuation() {
		super();
		_value = 0;
	}
	
	public IntegerValuation(NumericIntegerDomain domain, double value) {
		_domain = domain;
		_value = value;
	}
	
	public void setValue(Double value) {
		ParameterValidator.notNull(_domain, "domain"); //$NON-NLS-1$
		
		if(((NumericIntegerDomain) _domain).getInRange()) {
			ParameterValidator.inRange(value, ((NumericIntegerDomain) _domain).getMin(), 
					((NumericIntegerDomain) _domain).getMax());
			_value = value;
		} else {
			_value = value;
		}
	}
	
	public double getValue() {
		return _value;
	}
	
	public Valuation negateValuation() {
		IntegerValuation result = (IntegerValuation) clone();
		
		long aux = Math.round(((NumericIntegerDomain) _domain).getMin()) + Math.round(((NumericIntegerDomain) _domain).getMax());
		result.setValue(aux - _value);
		
		return result;
	}
	
	@Override
	public FuzzySet unification(Domain fuzzySet) {
		return null;
	}

	@Override
	public String toString() {
		return ("Integer(" + _value + ") in" + _domain.toString()); //$NON-NLS-1$ //$NON-NLS-2$
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
		
		final IntegerValuation other = (IntegerValuation) obj;
		
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(_value, other._value);
		eb.append(_domain, other._domain);
		
		return eb.isEquals();
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder(17, 31);
		hcb.append(_value);
		hcb.append(_domain);
		return hcb.toHashCode();
	}
	
	@Override
	public int compareTo(Valuation other) {
		ParameterValidator.notNull(other, "other"); //$NON-NLS-1$
		ParameterValidator.notIllegalElementType(other, 
				new String[] { Integer.class.toString() }, "other"); //$NON-NLS-1$
		
		if(_domain.equals(other.getDomain())) {
			return Double.valueOf(_value).compareTo(Double.valueOf(((IntegerValuation) other)._value));
		}
		
		return 0;
	}
	
	@Override
	public Object clone() {
		IntegerValuation result = null;
		result = (IntegerValuation) super.clone();
		result._value = new Double(_value);
		
		return result;
	}

	@Override
	public String changeFormatValuationToString() {
		return Double.toString(_value);
	}
	
	@Override
	public void save(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeAttribute("value", Double.toString(_value)); //$NON-NLS-1$
	}
}
