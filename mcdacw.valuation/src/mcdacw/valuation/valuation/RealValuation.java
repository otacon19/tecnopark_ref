package mcdacw.valuation.valuation;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import mcdacw.paremetervalidator.ParameterValidator;
import mcdacw.valuation.domain.Domain;
import mcdacw.valuation.domain.fuzzyset.FuzzySet;
import mcdacw.valuation.domain.fuzzyset.semantic.IMembershipFunction;
import mcdacw.valuation.domain.numeric.NumericRealDomain;

public class RealValuation extends Valuation {
	
	public static final String ID = "flintstones.valuation.real"; //$NON-NLS-1$
	
	public double _value;
	
	
	public RealValuation() {
		super();
		_value = 0;
	}
	
	public RealValuation(NumericRealDomain domain, double value) {
		_domain = domain;
		_value = value;
	}
	
	public void setValue(Double value) {
		ParameterValidator.notNull(_domain, "domain"); //$NON-NLS-1$
		
		if(((NumericRealDomain)_domain).getInRange()) {
			ParameterValidator.inRange(value, ((NumericRealDomain)_domain).getMin(), ((NumericRealDomain)_domain).getMax());
			_value = value;
		} else {
			_value = value;
		}
	}
	
	public double getValue() {
		return _value;
	}
	
	public Valuation normalized() {
		RealValuation result = (RealValuation) clone();
		double min, max, intervalSize;
		
		min = ((NumericRealDomain)_domain).getMin();
		max = ((NumericRealDomain)_domain).getMax();
		intervalSize = max - min;
		
		((NumericRealDomain) result._domain).setMinMax(0d, 1d);
		result._value = (_value - min) / intervalSize;
		
		return result;
		
	}
	
	@Override
	public Valuation negateValuation() {
		RealValuation result = (RealValuation) clone();
		double aux = ((NumericRealDomain)_domain).getMin() + ((NumericRealDomain)_domain).getMax();
		
		result.setValue(aux - _value);
		
		return result;
	}
	
	@Override
	public FuzzySet unification(Domain fuzzySet) {

		ParameterValidator.notNull(fuzzySet, "fuzzyset"); //$NON-NLS-1$
		
		if (!((FuzzySet) fuzzySet).isBLTS()) {
			throw new IllegalArgumentException("Not BLTS fuzzy set"); //$NON-NLS-1$
		}

		int cardinality;
		RealValuation normalized;
		IMembershipFunction function;

		FuzzySet result = (FuzzySet) fuzzySet.clone();
		cardinality =((FuzzySet) fuzzySet).getLabelSet().getCardinality();
		normalized = (RealValuation) normalized();

		for(int i = 0; i < cardinality; i++) {
			function = result.getLabelSet().getLabel(i).getSemantic();
			result.setValue(i, function.getMembershipValue(normalized.getValue()));
		}

		return result;
	}

	
	@Override
	public String toString() {
		return "(Real(" + _value + ") in " + _domain.toString(); //$NON-NLS-1$ //$NON-NLS-2$
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
		
		final RealValuation other = (RealValuation) obj;
		
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
		ParameterValidator.notIllegalElementType(other, new String[] { Integer.class.toString() }, "other"); //$NON-NLS-1$
		
		if(_domain.equals(other.getDomain())) {
			return Double.compare(_value, ((RealValuation) other)._value);
		}
				
		return 0;
	}
	
	@Override
	public Object clone() {
		RealValuation result = null;
		result = (RealValuation) super.clone();
		result._value = new Double(_value);
		
		return result;
	}
	
	@Override
	public String changeFormatValuationToString() {
		return Double.toString(_value);
	}
	
	@Override
	public void save(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeAttribute("value", Double.toString(_value));	 //$NON-NLS-1$
	}

}
