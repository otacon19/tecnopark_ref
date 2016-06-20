package mcdacw.valuation;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import mcdacw.paremetervalidator.ParameterValidator;
import mcdacw.valuation.domain.IDomain;
import mcdacw.valuation.domain.fuzzyset.FuzzySet;
import mcdacw.valuation.domain.fuzzyset.IMembershipFunction;
import mcdacw.valuation.domain.numeric.NumericDomain;

/**
 * Valoración numérica.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public class Numeric extends Quantitative {

	/**
	 * Medida de la valoración
	 */
	private double _measure;

	/**
	 * Constructor por defecto de Numeric.
	 * <p>
	 * Establece el dominio a [0,1] y la valoración a 0.5.
	 */
	public Numeric() {
		super();
		setDomain(new NumericDomain(0, 1));
	}

	/**
	 * Constructor de Numeric indicando dominio.
	 * <p>
	 * Establece el dominio al indicado y la medida al valor central del
	 * dominio.
	 * 
	 * @param domain
	 *            Dominio de valoración.
	 * 
	 * @throws IllegalArgumentException
	 *             Si domain es null.
	 */
	public Numeric(NumericDomain domain) {
		super();
		setDomain(domain);
	}

	/**
	 * Constructor de Numeric indicando dominio y medida.
	 * 
	 * @param domain
	 *            Dominio de valoración.
	 * @param measure
	 *            Medida de la valoración.
	 * 
	 * @throws IllegalArgumentException
	 *             Si domain es null o
	 *             <p>
	 *             si la medida no se encuentra dentro del dominio.
	 */
	public Numeric(NumericDomain domain, double measure) {
		super();
		setDomain(domain);
		setMeasure(measure);
	}

	/**
	 * Establece el dominio de la valoración.
	 * <p>
	 * Establece la medida al valor central del intervalo.
	 * 
	 * @param domain
	 *            Dominio de la valoración.
	 * @throws IllegalArgumentException
	 *             Si domain es null o
	 *             <p>
	 *             si domain no es un NumericDomain.
	 */
	@Override
	public void setDomain(IDomain domain) {
		ParameterValidator.notNull(domain, "domain");
		ParameterValidator.notIllegalElementType(domain,
				new String[] { NumericDomain.class.toString() }, "domain");

		_domain = domain;
		_measure = (((NumericDomain) _domain).getMin() + ((NumericDomain) _domain)
				.getMax()) / 2;
	}

	/**
	 * Establece la medida de la valoración.
	 * 
	 * @param measure
	 *            Medida de la valoración.
	 * @throws IllegalArgumentException
	 *             Si measure no está contenida en el dominio.
	 */
	public void setMeasure(double measure) {
		ParameterValidator.notDisorder(
				new double[] { ((NumericDomain) _domain).getMin(), measure,
						((NumericDomain) _domain).getMax() }, "measure", false);
		_measure = measure;
	}

	/**
	 * Devuelve la medida de la valoración.
	 * 
	 * @return Medida de la valoración.
	 */
	public double getMeasure() {
		return _measure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mcdacw.valuation.Quantitative#getNormalized()
	 */
	@Override
	public Valuation getNormalized() {

		Numeric result;
		double min, max, intervalSize;

		min = ((NumericDomain) _domain).getMin();
		max = ((NumericDomain) _domain).getMax();
		intervalSize = max - min;

		result = new Numeric(new NumericDomain(0, 1), (_measure - min)
				/ intervalSize);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mcdacw.valuation.Valuation#neg()
	 */
	@Override
	public Valuation neg() {

		Numeric result = (Numeric) clone();

		double aux = ((NumericDomain) _domain).getMin()
				+ ((NumericDomain) _domain).getMax();
		result.setMeasure(aux - _measure);

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mcdacw.valuation.Valuation#unification(mcdacw.valuation.domain.fuzzyset
	 * .FuzzySet)
	 */
	@Override
	public FuzzySet unification(FuzzySet fuzzySet) {

		ParameterValidator.notNull(fuzzySet, "fuzzySet");
		
		if (!fuzzySet.isBLTS()) {
			// TODO throw new IllegalArgumentException("Not BLTS fuzzy set.");
		}

		int cardinality;
		Numeric normalized;
		IMembershipFunction function;

		FuzzySet result = (FuzzySet) fuzzySet.clone();
		cardinality = fuzzySet.cardinality();
		normalized = (Numeric) getNormalized();

		for (int i = 0; i < cardinality; i++) {
			function = result.getLabel(i).getSemantic();
			result.setMeasure(i,
					function.getMembershipValue(normalized.getMeasure()));
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ("Numeric(" + _measure + ") in " + _domain.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {

		if (this == object) {
			return true;
		}

		if (object == null) {
			return false;
		}

		if (object.getClass() != this.getClass()) {
			return false;
		}

		final Numeric other = (Numeric) object;
		return new EqualsBuilder().append(_domain, other._domain)
				.append(_measure, other._measure).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(_domain).append(_measure)
				.toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Valuation other) {

		ParameterValidator.notNull(other, "other");
		ParameterValidator.notIllegalElementType(other,
				new String[] { Numeric.class.toString() }, "other");

		if (_domain.equals(other.getDomain())) {
			return Double.compare(_measure, ((Numeric) other)._measure);
		} else {
			throw new IllegalArgumentException("Different domains");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		Object result = null;

		result = super.clone();

		return result;
	}

}
