package mcdacw.valuation.domain.fuzzyset.label;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import mcdacw.paremetervalidator.ParameterValidator;
import mcdacw.valuation.domain.fuzzyset.semantic.IMembershipFunction;

public class LabelLinguisticDomain implements Cloneable, Comparable<LabelLinguisticDomain> {
	
	private String _name;
	private IMembershipFunction _semantic;
	
	public LabelLinguisticDomain(){}
	
	public LabelLinguisticDomain(String name, IMembershipFunction semantic) {
		ParameterValidator.notEmpty(name, "name"); //$NON-NLS-1$
		ParameterValidator.notNull(semantic, "semantic"); //$NON-NLS-1$
		
		_name = name;
		_semantic = semantic;
	}
	
	public String getName() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	
	public IMembershipFunction getSemantic() {
		return _semantic;
	}
	
	public void setSemantic(IMembershipFunction semantic) {
		_semantic = semantic;
	}
	
	@Override
	public String toString() {
		return _name + ";" + _semantic.toString(); //$NON-NLS-1$
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(this == obj) {
			return true;
		}
		
		if(obj == null) {
			return false;
		}
		
		if(this.getClass() != obj.getClass()) {
			return false;
		}
		
		final LabelLinguisticDomain other = (LabelLinguisticDomain) obj;
		
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(_name, other._name);
		eb.append(_semantic, other._semantic);
		
		return eb.isEquals();
		
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder(17, 31);
		hcb.append(_name);
		hcb.append(_semantic);
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
		
		((LabelLinguisticDomain) result)._name = new String(_name);
		((LabelLinguisticDomain) result)._semantic = (IMembershipFunction) _semantic.clone();
		
		return result;
		
	}
	
	@Override
	public int compareTo(LabelLinguisticDomain other) {
		ParameterValidator.notNull(other, "other"); //$NON-NLS-1$
		
		return _semantic.compareTo(other._semantic);
	}
	
	public void save(XMLStreamWriter writer) throws XMLStreamException {		
		writer.writeStartElement("semantic"); //$NON-NLS-1$
		writer.writeAttribute("type", "sinbad2.domain.linguistic.fuzzy.function.types.TrapezoidalFunction"); //$NON-NLS-1$ //$NON-NLS-2$
		_semantic.save(writer);
		writer.writeEndElement();
	}
}
