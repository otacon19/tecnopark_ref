package mcdacw.valuation.valuation;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.builder.HashCodeBuilder;

import mcdacw.paremetervalidator.ParameterValidator;
import mcdacw.valuation.domain.Domain;

public abstract class Valuation implements Cloneable, Comparable<Valuation> {

	protected Domain _domain;
	private String _domainExtensionId;
	private String _id;
	private String _name;
	
	public Valuation() {
		_domain = null;
		_id = null;
		_name = null;
	}
	
	public void setDomainExtensionId(String domainExtensionId) {
		_domainExtensionId = domainExtensionId;
	}
	
	public String getDomainExtensionId() {
		return _domainExtensionId;
	}
	
	public void setId(String id) {
		_id = id;
	}
	
	public String getId() {
		return _id;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public String getName() {
		return _name;
	}
	
	public void setDomain(Domain domain) {
		ParameterValidator.notNull(domain, "domain");
		
		_domain = domain;
	}
	
	public Domain getDomain() {
		return _domain;
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder(17, 31);
		hcb.append(_id);
		hcb.append(_name);
		hcb.append(_domain);
		return hcb.toHashCode();
	}
	
	public Object clone() {
		Valuation result = null;
		
		try {
			result = (Valuation) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
		
		if(_domain != null) {
			result._domain = (Domain) _domain.clone();
		}
		
		return result;
		
	}
	
	public abstract Valuation negateValuation();
	
	public abstract Domain unification(Domain fuzzySet);
	
	public abstract String changeFormatValuationToString();
	
	public abstract void save(XMLStreamWriter writer) throws XMLStreamException;
	
}
