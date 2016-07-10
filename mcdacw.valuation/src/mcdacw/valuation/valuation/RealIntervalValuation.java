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

public class RealIntervalValuation extends Valuation {
	
	public static final String ID = "flintstones.valuation.real.interval"; //$NON-NLS-1$

	public double _min;
	public double _max;
	
	
	public RealIntervalValuation() {
		super();
		_min = 0;
		_max = 0;
	}
	
	public RealIntervalValuation(NumericRealDomain domain, double min, double max) {
		super();
		_domain = domain;
		_min = min;
		_max = max;
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
		ParameterValidator.notNull(_domain, "domain");
		ParameterValidator.notDisorder(new double[] { min, max }, "min-max", false);
		
		_min = min;
		_max = max;
	}
	
	public Valuation normalized() {
		RealIntervalValuation result = (RealIntervalValuation) clone();
		double min, max, intervalSize;
		
		min = ((NumericRealDomain) _domain).getMin();
		max = ((NumericRealDomain) _domain).getMax();
		intervalSize = max - min;
		
		max = ((double) (_max - min)) / intervalSize;
		min = ((double) (_min - min)) / intervalSize;
		
		((NumericRealDomain) result._domain).setMinMax(0d, 1d);
		result.setMinMax(min, max);
		
		return result;
	}
	
	@Override
	public Valuation negateValuation() {
		RealIntervalValuation result = (RealIntervalValuation) clone();
		
		double aux = ((NumericRealDomain) _domain).getMin() + ((NumericRealDomain) _domain).getMax();
		result.setMinMax(aux - _max, aux - _min);
		
		return result;
	}
	
	@Override
	public FuzzySet unification(Domain fuzzySet) {

		ParameterValidator.notNull(fuzzySet, "fuzzyset");

		if (!((FuzzySet) fuzzySet).isBLTS()) {
			throw new IllegalArgumentException("Not BLTS fuzzy set");
		}
		
		int cardinality;
		RealIntervalValuation normalized;
		IMembershipFunction function;

		FuzzySet result = (FuzzySet) fuzzySet.clone();
		cardinality = ((FuzzySet) fuzzySet).getLabelSet().getCardinality();
		normalized = (RealIntervalValuation) normalized();

		for (int i = 0; i < cardinality; i++) {
			function = result.getLabelSet().getLabel(i).getSemantic();
			result.setValue(i, function.maxMin(normalized.getMax(), normalized.getMin()));
		}

		return result;
	}
	
	@Override
	public String toString() {
		return ("Real interval[" + _min + "," + _max + "] in" + _domain.toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
		
		final RealIntervalValuation other = (RealIntervalValuation) obj;
		
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
			double otherMidle = (((RealIntervalValuation) other)._max + ((RealIntervalValuation) other)._min) / 2d;
			return Double.compare(middle, otherMidle);	
		} else {
			throw new IllegalArgumentException("Different domains");
		}
	}
	
	@Override
	public Object clone() {
		RealIntervalValuation result = null;
		result = (RealIntervalValuation) super.clone();
		result._min = new Double(_min);
		result._max = new Double(_max);
		
		return result;
	}

	@Override
	public String changeFormatValuationToString() {
		return "[" + Double.toString(_min) + ", " + Double.toString(_max) + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	@Override
	public void save(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeAttribute("min", Double.toString(_min)); //$NON-NLS-1$
		writer.writeAttribute("max", Double.toString(_max));	 //$NON-NLS-1$
	}
}
