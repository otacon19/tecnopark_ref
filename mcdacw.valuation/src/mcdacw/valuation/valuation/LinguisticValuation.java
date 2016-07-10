package mcdacw.valuation.valuation;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import mcdacw.paremetervalidator.ParameterValidator;
import mcdacw.valuation.domain.Domain;
import mcdacw.valuation.domain.fuzzyset.FuzzySet;
import mcdacw.valuation.domain.fuzzyset.label.LabelLinguisticDomain;
import mcdacw.valuation.domain.fuzzyset.semantic.IMembershipFunction;


public class LinguisticValuation extends Valuation {
	
	public static final String ID = "flintstones.valuation.linguistic"; //$NON-NLS-1$
	
	public LabelLinguisticDomain _label;
	
	public LinguisticValuation() {
		super();
		
		_label = new LabelLinguisticDomain();
	}
	
	public void setLabel(int pos) {
		LabelLinguisticDomain label = ((FuzzySet) _domain).getLabelSet().getLabel(pos);
		ParameterValidator.notNull(label, "label");
		
		_label = label;
	}
	
	public void setLabel(String name) {
		LabelLinguisticDomain label = ((FuzzySet) _domain).getLabelSet().getLabel(name);
		ParameterValidator.notNull(label, "label");
		
		_label = (LabelLinguisticDomain) label;
	}
	
	public void setLabel(LabelLinguisticDomain label) {
		ParameterValidator.notNull(label, "label");
		
		if(((FuzzySet) _domain).getLabelSet().containsLabel(label)) {
			_label = (LabelLinguisticDomain) label;
		} else {
			throw new IllegalArgumentException("Not contains in domain");
		}
	}
	
	public LabelLinguisticDomain getLabel() {
		return _label;
	}

	@Override
	public Valuation negateValuation() {
		LinguisticValuation result = (LinguisticValuation) clone();
		
		FuzzySet domain = (FuzzySet) _domain;
		if(domain.getLabelSet().getCardinality() > 1) {
			int negPos = (domain.getLabelSet().getCardinality() - 1) -  domain.getLabelSet().getPos(_label);
			result.setLabel(negPos);
		}
		
		return result;
	}
	
	@Override
	public String changeFormatValuationToString() {
		return _label.getName();
	}
	
	@Override
	public FuzzySet unification(Domain fuzzySet) {
		ParameterValidator.notNull(fuzzySet, "fuzzyset");

		if(!((FuzzySet) fuzzySet).isBLTS()) {
			throw new IllegalArgumentException("Not BLTS fuzzy set");
		}

		int cardinality;
		IMembershipFunction function;
		FuzzySet result;

		result = (FuzzySet) ((FuzzySet) fuzzySet).clone();
		cardinality = ((FuzzySet) fuzzySet).getLabelSet().getCardinality();

		for(int i = 0; i < cardinality; i++) {
			function = result.getLabelSet().getLabel(i).getSemantic();
			result.setValue(i, function.maxMin(_label.getSemantic()));
		}
		return result;
	}

	@Override
	public String toString() {
		return (_label + " in " + _domain.toString()); //$NON-NLS-1$
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(this == obj) {
			return true;
		}
		
		if(obj ==  null) {
			return false;
		}
		
		if(this.getClass() != obj.getClass()) {
			return false;
		}
		
		LinguisticValuation other = (LinguisticValuation) obj;
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(_domain, other._domain);
		eb.append(_label, other._label);
		
		return eb.isEquals();
	}
	
	@Override
	public int compareTo(Valuation other) {
		ParameterValidator.notNull(other, "other");
		ParameterValidator.notIllegalElementType(other, new String[] { LinguisticValuation.class.toString() }, "other");
		
		if(_domain.equals(other.getDomain())) {
			return _label.compareTo(((LinguisticValuation) other)._label);
		} else {
			throw new IllegalArgumentException("Different domains");
		}
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder(17, 31);
		hcb.append(_domain);
		hcb.append(_label);
		return hcb.toHashCode();
	}
	
	@Override
	public Object clone() {
		Object result = null;
		
		result = super.clone();
		
		return result;
	}
	
	@Override
	public void save(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeStartElement("labelvaluation"); //$NON-NLS-1$
		
		writer.writeStartElement("labelv"); //$NON-NLS-1$
		writer.writeAttribute("label", _label.getName()); //$NON-NLS-1$
		_label.save(writer);
		writer.writeEndElement();
		
		writer.writeEndElement();
	}
}
