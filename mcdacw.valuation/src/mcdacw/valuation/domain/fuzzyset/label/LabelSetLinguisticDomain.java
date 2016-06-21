package mcdacw.valuation.domain.fuzzyset.label;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import mcdacw.paremetervalidator.ParameterValidator;


public class LabelSetLinguisticDomain implements Cloneable {

	protected List<LabelLinguisticDomain> _labels;
	
	public LabelSetLinguisticDomain() {
		_labels = new LinkedList<LabelLinguisticDomain>();
	}
	
	public LabelSetLinguisticDomain(List<LabelLinguisticDomain> labels) {
		this();
		setLabels(labels);
	}
	
	public void setLabels(List<LabelLinguisticDomain> labels) {
		ParameterValidator.notNull(labels, "labels");
		
		for(LabelLinguisticDomain label: labels) {
			addLabel(label);
		}
		
		_labels = labels;
	}
	
	public List<LabelLinguisticDomain> getLabels() {
		return _labels;
	}
	
	public void addLabel(LabelLinguisticDomain label) {
		addLabel(getCardinality(), label);
	}
	
	public void addLabel(int pos, LabelLinguisticDomain label) {
		ParameterValidator.notNull(label, "label");
		ParameterValidator.notInvalidSize(pos, 0, getCardinality(), "cardinality");
		
		if(containsLabel(label.getName())) {
			throw new IllegalArgumentException("Duplicate label name");
		}
		
		_labels.add(pos, label);
	}
	
	public void removeLabel(int pos) {
		ParameterValidator.notEmpty(_labels.toArray(), "labels");
		ParameterValidator.notInvalidSize(pos, 0, getCardinality() - 1, "cardinality");
		
		_labels.remove(pos);
	}
	
	public void removeLabel(LabelLinguisticDomain label) {
		
		if(label == null) {
			return;
		}
		
		int pos = getPos(label);
		
		if(pos != -1) {
			removeLabel(pos);
		}
	}
	
	public boolean containsLabel(String name) {
		return (getPos(name) != -1);
	}
	
	public boolean containsLabel(LabelLinguisticDomain label) {
		return _labels.contains(label);
	}
	
	public int getCardinality() {
		return _labels.size();
	}
	
	public LabelLinguisticDomain getLabel(int pos) {
		ParameterValidator.notEmpty(_labels.toArray(), "labels");
		ParameterValidator.notInvalidSize(pos, 0, getCardinality() - 1, "cardinality");
		
		return _labels.get(pos);
	}
	
	public LabelLinguisticDomain getLabel(String name) {
		int pos = getPos(name);
		
		if(pos != -1) {
			return getLabel(pos);
		} else {
			return null;
		}
	}
	
	public int getPos(String name) {
		
		if(name == null) {
			return -1;
		}
		
		if(ParameterValidator.isEmpty(name)) {
			return -1;
		}
		
		for(LabelLinguisticDomain auxLabel: _labels) {
			if(auxLabel.getName().equals(name)) {
				return _labels.indexOf(auxLabel);
			}
		}
		
		return -1;
		
	}
	
	public int getPos(LabelLinguisticDomain label) {
		return _labels.indexOf(label);
	}
	
	@Override
	public String toString() {
		String result = ""; //$NON-NLS-1$
		int cardinality = getCardinality();
		
		if(cardinality > 0) {
			for(int pos = 0; pos < cardinality; ++pos) {
				if(pos > 0) {
					result += ", "; //$NON-NLS-1$
				}
				result += _labels.get(pos);
			}
		}
		
		return "{" + result + "}"; //$NON-NLS-1$ //$NON-NLS-2$
		
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(this.equals(obj)) {
			return true;
		}
		
		if(obj == null) {
			return false;
		}
		
		if(this.getClass() != obj.getClass()) {
			return false;
		}
		
		final LabelSetLinguisticDomain other = (LabelSetLinguisticDomain) obj;
		
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(_labels, other._labels);
		
		return eb.isEquals();
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder(17, 31);
		hcb.append(_labels);
		return hcb.hashCode();
	}
	
	@Override
	public Object clone() {
		Object result = null;
		
		try {
			result = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		List<LabelLinguisticDomain> resultLabels = new LinkedList<LabelLinguisticDomain>();
		for(LabelLinguisticDomain label: _labels) {
			resultLabels.add((LabelLinguisticDomain)label.clone()); 
		}
		
		((LabelSetLinguisticDomain) result)._labels = resultLabels;
		
		return result;
	}
	
}
