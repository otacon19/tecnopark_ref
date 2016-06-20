package flintstones.gathering.cloud.model;

public class Valuation {
	private String _value;
	
	private Valuation() {
		_value = null;
	}
	
	public Valuation(String value) {
		this();
		setValue(value);
	}
	
	public void setValue(String value) {
		_value = value;
	}
	
	public String getValue() {
		return _value;
	}
}
