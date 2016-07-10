package mcdacw.valuation.domain.fuzzyset.semantic;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import mcdacw.valuation.domain.fuzzyset.function.FragmentFunction;
import mcdacw.valuation.domain.numeric.NumericRealDomain;

public interface IMembershipFunction extends Cloneable, Comparable<IMembershipFunction> {
	
	public FragmentFunction toFragmentFunction();
	
	public boolean isSymmetrical();
	
	public boolean isSymmetrical(IMembershipFunction other, double center);
	
	public NumericRealDomain getCenter();
	
	public NumericRealDomain getCoverage();
	
	public double getMembershipValue(double x);
	
	public double centroid();
	
	public double maxMin(double max, double min);
	
	public double maxMin(IMembershipFunction function);
	
	public Object clone();
	
	public void save(XMLStreamWriter writer) throws XMLStreamException;

}
