package mcdacw.valuation.domain.numeric;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import mcdacw.paremetervalidator.ParameterValidator;
import mcdacw.valuation.domain.IDomain;

/**
 * Dominio numérico intervalar.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public class IntervalNumericDomain implements INumericDomain, IDomain {

	public static final int INTEGER = 0;
	public static final int REAL = 1;
	
	/**
	 * Límite inferior del dominio.
	 */
	private double _min;

	/**
	 * Límite superior del dominio.
	 */
	private double _max;
	
	private int _type;
	
	private boolean _inRange;

	/**
	 * Constructor por defecto de IntervalNumericDomain.
	 * <p>
	 * Asigna los límites inferior y superior a 0.
	 * <p>
	 * El dominio será de tipo REAL.
	 */
	public IntervalNumericDomain() {
		_type = REAL;
		_min = 0;
		_max = 0;
		_inRange = true;
	}

	/**
	 * Constructor de IntervalNumericDomain indicando los límites inferior y superior.
	 * <p>
	 * El dominio será de tipo REAL.
	 * 
	 * @param min
	 *            Límite inferior.
	 * @param max
	 *            Límite superior.
	 * 
	 * @throws IllegalArgumentException
	 *             Si min > max.
	 */
	public IntervalNumericDomain(double min, double max) {
		_type = REAL;
		_inRange = true;
		setMinMax(min, max);
	}

	/**
	 * Establece el tipo de dominio.
	 * 
	 * @param type
	 *            INTEGER para dominio entero.
	 *            <p>
	 *            REAL para dominio real.
	 * @trhows IllegalArgumentException si type es inválido.
	 */
	public void setType(int type) {
		ParameterValidator.notInvalidSize(type, INTEGER, REAL, "type");
		_type = type;
	}

	public void setInRange(boolean inRange) {
		_inRange = inRange;
	}

	/**
	 * Devuelve el tipo del dominio.
	 * 
	 * @return INTEGER si el dominio es de tipo entero.
	 *         <p>
	 *         REAL si el dominio es de tipo real.
	 */
	public int getType() {
		return _type;
	}

	public boolean getInRange() {
		return _inRange;
	}

	/*
	 * (non-Javadoc)
	 * @see mcdacw.valuation.domain.numeric.INumericDomain#setMin(double)
	 */
	@Override
	public void setMin(double min) {
		setMinMax(min, _max);
	}

	/*
	 * (non-Javadoc)
	 * @see mcdacw.valuation.domain.numeric.INumericDomain#getMin()
	 */
	@Override
	public double getMin() {
		return _min;
	}

	/*
	 * (non-Javadoc)
	 * @see mcdacw.valuation.domain.numeric.INumericDomain#setMax(double)
	 */
	@Override
	public void setMax(double max) {
		setMinMax(_min, max);
	}

	/*
	 * (non-Javadoc)
	 * @see mcdacw.valuation.domain.numeric.INumericDomain#getMax()
	 */
	@Override
	public double getMax() {
		return _max;
	}

	/*
	 * (non-Javadoc)
	 * @see mcdacw.valuation.domain.numeric.INumericDomain#setMinMax(double, double)
	 */
	@Override
	public void setMinMax(double min, double max) {
		ParameterValidator.notDisorder(new double[] { min, max }, "limits",
				false);
		_min = min;
		_max = max;
	}

	/*
	 * (non-Javadoc)
	 * @see mcdacw.valuation.domain.numeric.INumericDomain#resumeValue()
	 */
	@Override
	public double resumeValue() {
		return ((_max + _min) / 2.);
	}

	/**
	 * Devuelve una descripción del dominio numérico.
	 * 
	 * @return Descripción del dominio numérico.
	 */
	@Override
	public String toString() {
		if (_type == INTEGER) {
			return "[" + (int) _min + ", " + (int) _max + "]";
		} else {
			return "[" + _min + ", " + _max + "]";
		}
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

		final IntervalNumericDomain other = (IntervalNumericDomain) object;
		return new EqualsBuilder().append(_min, other._min)
				.append(_max, other._max).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(_min).append(_max)
				.toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		Object result = null;

		try {
			result = super.clone();
		} catch (CloneNotSupportedException cnse) {
			// No debería ocurrir
		}

		return result;
	}

	/**
	 * Compara el dominio actual con otro dominio.
	 * 
	 * @param other
	 *            Dominio a comparar con el actual.
	 * @throws IllegalArgumentException
	 *             Si other es null.
	 */
	@Override
	public int compareTo(INumericDomain other) {
		ParameterValidator.notNull(other, "other");
		return Double.compare(this.resumeValue(), other.resumeValue());
	}
	
	public static IntervalNumericDomain fromString(String value) {
		IntervalNumericDomain result = null;
		
		try {
			String[] tokens = value.split(",");
			double min = Double.parseDouble(tokens[0].substring(1));
			double max = Double.parseDouble(tokens[1].substring(0, tokens[1].length() - 1));
			result = new IntervalNumericDomain(min, max);
			if (tokens[0].contains(".")) {
				result.setType(0);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
