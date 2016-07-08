package flintstones.gathering.cloud.model;

import java.util.HashMap;
import java.util.Map;

import mcdacw.valuation.valuation.Valuation;

public class Valuations {

	private Map<KeyDomainAssignment, Valuation> _valuations;
	
	public Valuations() {
		_valuations = new HashMap<KeyDomainAssignment, Valuation>();
	}
	
	public void setValuations(Map<KeyDomainAssignment, Valuation> valuations) {
		_valuations = valuations;
	}
	
	public Map<KeyDomainAssignment, Valuation> getValuations() {
		return _valuations;
	}
	 
	public void setValuation(KeyDomainAssignment key, Valuation valuation) {
		_valuations.put(key, valuation);
	}
	
	public Valuation getValuation(KeyDomainAssignment key) {
		return _valuations.get(key);
	}
	
	public void removeValuation(KeyDomainAssignment key) {
		_valuations.remove(key);
	}
}
