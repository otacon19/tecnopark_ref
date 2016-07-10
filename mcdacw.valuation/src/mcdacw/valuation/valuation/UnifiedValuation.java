package mcdacw.valuation.valuation;

import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import mcdacw.paremetervalidator.ParameterValidator;
import mcdacw.valuation.domain.Domain;
import mcdacw.valuation.domain.fuzzyset.FuzzySet;
import mcdacw.valuation.domain.fuzzyset.label.LabelLinguisticDomain;

public class UnifiedValuation extends Valuation {

	private UnifiedValuation() {}
	
	public UnifiedValuation(FuzzySet domain) {
		this();
		_domain = domain;
	}
	
	@Override
	public int compareTo(Valuation other) {
		ParameterValidator.notNull(other, "other");
		ParameterValidator.notIllegalElementType(other, new String[] { UnifiedValuation.class.toString() }, "other");

		FuzzySet thisDomain = (FuzzySet) _domain;
		FuzzySet otherDomain = (FuzzySet) other.getDomain();
		for (int i = 0; i < thisDomain.getLabelSet().getCardinality(); i++) {
			if (!thisDomain.getLabelSet().getLabel(i).equals(otherDomain.getLabelSet().getLabel(i))) {
				throw new IllegalArgumentException("Different domains");
			}
		}

		TwoTuple thisDisunification = disunification(thisDomain);
		TwoTuple otherDisunification = disunification(otherDomain);

		return thisDisunification.compareTo(otherDisunification);
	}
	
	public TwoTuple disunification(FuzzySet fuzzySet) {
		ParameterValidator.notNull(fuzzySet, "fuzzySet");

		TwoTuple result = new TwoTuple((FuzzySet) fuzzySet.clone());

		int size = fuzzySet.getLabelSet().getCardinality();
		double numerator = 0, denominator = 0, measure, beta = 0;
		for (int i = 0; i < size; i++) {
			((FuzzySet) result.getDomain()).setValue(i, 0d);
			measure = fuzzySet.getValue(i);
			numerator += measure * i;
			denominator += measure;
		}

		if (denominator != 0) {
			beta = numerator / denominator;
		}
		
		result.calculateDelta(beta);

		return result;
	}

	@Override
	public Valuation negateValuation() {
		return null;
	}

	@Override
	public String changeFormatValuationToString() {
		FuzzySet domain = (FuzzySet) _domain;
		StringBuilder result = new StringBuilder("{"); //$NON-NLS-1$
		List<LabelLinguisticDomain> labels = domain.getLabelSet().getLabels();
		String name, measure;
		
		for (int i = 0; i < labels.size(); i++) {
			name = labels.get(i).getName();
			measure = domain.getValue(i).toString();
			if (measure.length() > 4) {
				measure = measure.substring(0, 4);
			}
			result.append("(" + name + "," + measure + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if ((i + 1) != labels.size()) {
				result.append(","); //$NON-NLS-1$
			}
		}
		result.append("}"); //$NON-NLS-1$
		
		return result.toString();
	}
	
	@Override
	public Domain unification(Domain fuzzySet) {
		return null;
	}

	@Override
	public void save(XMLStreamWriter writer) throws XMLStreamException {}
}
