package flintstones.gathering.cloud.model;

import java.util.HashMap;
import java.util.Map;

public class Valuations {

	private Map<Key, Valuation> _valuations;
	
	public Valuations() {
		_valuations = new HashMap<Key, Valuation>();
	}
	
	public void setValuations(Map<Key, Valuation> valuations) {
		_valuations = valuations;
	}
	
	public Map<Key, Valuation> getValuations() {
		return _valuations;
	}
	 
	public void setValuation(Key key, Valuation valuation) {
		_valuations.put(key, valuation);
	}
	
	public Valuation getValuation(Key key) {
		return _valuations.get(key);
	}
	
	public void removeValuation(Key key) {
		_valuations.remove(key);
	}
}
