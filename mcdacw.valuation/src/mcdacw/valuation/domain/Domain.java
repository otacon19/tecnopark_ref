package mcdacw.valuation.domain;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


public abstract class Domain implements Cloneable, Comparable<Domain> {
	
	private String _id;
	private String _name;
	private String _type;
	
	public Domain() {
		_id = null;
		_name = null;
		_type = null;
	}
	
	public String getId() {
		return _id;
	}
	
	public void setId(String id) {
		_id = id;
	}
	
	public String getName() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public String getType() {
		return _type;
	}
	
	public void setType(String type) {
		_type = type;
	}
	
	public abstract double midpoint();
	
	public abstract String formatDescriptionDomain();
	
	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder(17, 31);
		hcb.append(_id);
		hcb.append(_name);
		hcb.append(_type);
		return hcb.toHashCode();
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
		
		final Domain other = (Domain) obj;
		
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(_id, other._id);
		eb.append(_name, other._name);
		eb.append(_type, other._type);
		
		return eb.isEquals();
	}
	
	@Override
	public Object clone() {
		Domain result = null;
		
		try {
			result = (Domain) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public int compareTo(Domain domain) {
		
		if(_id == null) {
			return -1;
		}
		
		return _id.compareTo(domain.getId());
	}
	
	public abstract void save(XMLStreamWriter writer) throws XMLStreamException;
}
