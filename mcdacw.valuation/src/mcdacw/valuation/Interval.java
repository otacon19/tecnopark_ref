package mcdacw.valuation;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import mcdacw.paremetervalidator.ParameterValidator;
import mcdacw.valuation.domain.IDomain;
import mcdacw.valuation.domain.fuzzyset.FuzzySet;
import mcdacw.valuation.domain.fuzzyset.IMembershipFunction;
import mcdacw.valuation.domain.numeric.IntervalNumericDomain;

/**
 * Valoración intervalar.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public class Interval extends Quantitative {

	/**
	 * Límite inferior del intervalo.
	 */
	private double _min;

	/**
	 * Límite superior del intervalo.
	 */
	private double _max;

	/**
	 * Constructor por defecto.
	 * <p>
	 * Establece los límites y el intervalo a [0, 0].
	 */
	public Interval() {
		super();
		setDomain(new IntervalNumericDomain(0d, 0d));
	}

	/**
	 * Constructor de intervalo indicado dominio.
	 * <p>
	 * Establece los valores del intervalo iguales a los del dominio.
	 * 
	 * @param domain
	 *            Dominio del intervalo.
	 * 
	 * @throws IllegalArgumentException
	 *             Si domain es null.
	 */
	public Interval(IntervalNumericDomain domain) {
		super();
		setDomain(domain);
	}

	/**
	 * Constructor de intervalo indicado dominio y límites del intervalo.
	 * 
	 * @param domain
	 *            Dominio del intervalo.
	 * @param min
	 *            Límite inferior del intervalo.
	 * @param max
	 *            Límite superior del intervalo.
	 * 
	 * 
	 * @throws IllegalArgumentException
	 *             Si domain es null o,
	 *             <p>
	 *             Si min > max o,
	 *             <p>
	 *             si min < Mínimo del dominio o
	 *             <p>
	 *             si max > Máximo del dominio.
	 */
	public Interval(IntervalNumericDomain domain, double min, double max) {
		this(domain);
		setMinMax(min, max);
	}

	/**
	 * Establece el límite inferior del intervalo.
	 * 
	 * @param min
	 *            Límite inferior del intervalo.
	 * 
	 * @throws IllegalArgumentException
	 *             Si min > max o
	 *             <p>
	 *             Si min < Mínimo del dominio.
	 */
	public void setMin(double min) {
		setMinMax(min, _max);
	}

	/**
	 * Devuelve el límite inferior del intervalo.
	 * 
	 * @return Límite inferior del intervalo.
	 */
	public double getMin() {
		return _min;
	}

	/**
	 * Establece el límite inferior del intervalo.
	 * 
	 * @param min
	 *            Límite inferior del intervalo.
	 * 
	 * @throws IllegalArgumentException
	 *             Si max < min o
	 *             <p>
	 *             Si max > Máximo del dominio.
	 */
	public void setMax(double max) {
		setMinMax(_min, max);
	}

	/**
	 * Devuelve el límite superior del intervalo.
	 * 
	 * @return Límite superior del intervalo.
	 */
	public double getMax() {
		return _max;
	}

	/**
	 * Establece los límites inferior y superior del intervalo.
	 * 
	 * @param min
	 *            Límite inferior del intervalo.
	 * @param max
	 *            Límite superior del intervalo.
	 * 
	 * @throws IllegalArgumentException
	 *             Si min > max o,
	 *             <p>
	 *             si min < Mínimo del dominio o
	 *             <p>
	 *             si max > Máximo del dominio.
	 */
	public void setMinMax(double min, double max) {

		ParameterValidator.notDisorder(new double[] { min, max },
				"min and max", false);

		ParameterValidator.notDisorder(
				new double[] { ((IntervalNumericDomain) _domain).getMin(), min },
				"min", false);

		ParameterValidator.notDisorder(new double[] { max,
				((IntervalNumericDomain) _domain).getMax() }, "max", false);

		_min = min;
		_max = max;
	}

	/**
	 * Establece el dominio.
	 * <p>
	 * Setea los valores máximo y mínimo del intervalo a los límites del
	 * dominio.
	 * 
	 * @throws IllegalArgumentException
	 *             Si domain es null o
	 *             <p>
	 *             si domain no es un IntervalNumericDomain.
	 */
	@Override
	public void setDomain(IDomain domain) {

		ParameterValidator.notNull(domain, "domain");
		ParameterValidator.notIllegalElementType(domain,
				new String[] { IntervalNumericDomain.class.toString() }, "domain");

		_domain = domain;
		_min = ((IntervalNumericDomain) _domain).getMin();
		_max = ((IntervalNumericDomain) _domain).getMax();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mcdacw.valuation.Quantitative#getNormalized()
	 */
	@Override
	public Valuation getNormalized() {

		Interval result;
		double min, max, intervalSize;

		min = ((IntervalNumericDomain) _domain).getMin();
		max = ((IntervalNumericDomain) _domain).getMax();
		intervalSize = max - min;

		max = ((double) (_max - min)) / intervalSize;
		min = ((double) (_min - min)) / intervalSize;

		result = new Interval(new IntervalNumericDomain(0, 1), min, max);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mcdacw.valuation.Valuation#neg()
	 */
	@Override
	public Valuation neg() {

		Interval result = (Interval) clone();

		double aux = ((IntervalNumericDomain) _domain).getMin()
				+ ((IntervalNumericDomain) _domain).getMax();
		result.setMinMax(aux - _max, aux - _min);

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
			throw new IllegalArgumentException("Not BLTS fuzzy set.");
		}
		
		int cardinality;
		Interval normalized;
		IMembershipFunction function;

		FuzzySet result = (FuzzySet) fuzzySet.clone();
		cardinality = fuzzySet.cardinality();
		normalized = (Interval) getNormalized();

		for (int i = 0; i < cardinality; i++) {
			function = result.getLabel(i).getSemantic();
			result.setMeasure(i, function.maxMin(normalized));
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
		return ("Interval([" + _min + ", " + _max + "]) in " + _domain
				.toString());
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

		final Interval other = (Interval) object;
		return new EqualsBuilder().append(_domain, other._domain)
				.append(_min, other._min).append(_max, other._max).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(_domain).append(_min)
				.append(_max).toHashCode();
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
				new String[] { Interval.class.toString() }, "other");

		if (_domain.equals(other.getDomain())) {
			double thisMiddle = (_max + _min) / 2d;
			double otherMiddle = (((Interval) other)._max + ((Interval) other)._min) / 2d;
			return Double.compare(thisMiddle, otherMiddle);
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
