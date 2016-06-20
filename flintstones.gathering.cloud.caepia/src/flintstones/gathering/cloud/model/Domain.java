package flintstones.gathering.cloud.model;

import flintstones.gathering.cloud.xml.XMLValues;
import mcdacw.valuation.domain.IDomain;
import mcdacw.valuation.domain.fuzzyset.HesitantFuzzySet;
import mcdacw.valuation.domain.numeric.IntervalNumericDomain;
import mcdacw.valuation.domain.numeric.NumericDomain;

public class Domain {

	private String _id;
	private String _type;
	private IDomain _domain;

	private Domain() {
		_id = null;
		_type = null;
		_domain = null;
	}

	public Domain(String id, String type, IDomain domain) {
		this();
		setId(id);
		setType(type);
		setDomain(domain);
	}

	public void setId(String id) {
		_id = id;
	}

	public String getId() {
		return _id;
	}

	public void setType(String type) {
		_type = type;
	}

	public String getType() {
		return _type;
	}
	
	public void setDomain(IDomain domain) {
		_domain = domain;
	}
	
	public IDomain getDomain() {
		return _domain;
	}
	
	public static IDomain buildDomain(String type, String value) {
		IDomain result = null;
		if (type.equals(XMLValues.NUMERIC_DOMAIN)) {
			result = NumericDomain.fromString(value);
		} else if (type.equals(XMLValues.HESITANT_FUZZY_SET)) {
			result = HesitantFuzzySet.fromString(value);
		} else if (type.equals(XMLValues.INTERVAL_NUMERIC_DOMAIN)) {
			result = IntervalNumericDomain.fromString(value);
		}
		return result;
	}
	
	@Override
	public String toString() {
		return _id;
	}
}
