package mcdacw.valuation.domain.fuzzyset;

import org.apache.commons.lang.builder.HashCodeBuilder;

import mcdacw.paremetervalidator.ParameterValidator;

/**
 * Trozo de función lineal.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public class LinearPieceFunction implements IPieceFunction {
	
	private final static double EPSILON = 0.00001;
	
	/*
	 * Pendiente de la función.
	 */
	private double _slope;

	/*
	 * Punto de corte de la función.
	 */
	private double _yIntercept;

	/**
	 * Constructor de LinearPieceFunction indicando pendiente y punto de corte.
	 * 
	 * @param slope
	 *            Pendiente de la función.
	 * @param yIntercept
	 *            Punto de corte de la función.
	 * 
	 */
	public LinearPieceFunction(double slope, double yIntercept) {
		_slope = slope;
		_yIntercept = yIntercept;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mcdacw.valuation.domain.fuzzyset.IPieceFunction#sum(mcdacw.valuation.
	 * domain.fuzzyset.IPieceFunction)
	 */
	public IPieceFunction sum(IPieceFunction other) {
		ParameterValidator.notNull(other, "other");
		ParameterValidator.notIllegalElementType(other, new String[] { this
				.getClass().toString() }, "other");

		return new LinearPieceFunction(_slope
				+ ((LinearPieceFunction) other)._slope, _yIntercept
				+ ((LinearPieceFunction) other)._yIntercept);
	}

	/**
	 * Devuelve la pendiente de la función.
	 * 
	 * @return Pendiente de la función.
	 */
	public double getSlope() {
		return _slope;
	}

	/**
	 * Devuelve el punto de corte de la función.
	 * 
	 * @return Punto de corte de la función.
	 */
	public double getYIntercept() {
		return _yIntercept;
	}

	/**
	 * Devuelve una descripción de la función
	 * 
	 * @return Descripción de la función.
	 */
	@Override
	public String toString() {
		return (_yIntercept < 0) ? (_slope + "x " + _yIntercept) : (_slope
				+ "x + " + _yIntercept);
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

		final LinearPieceFunction other = (LinearPieceFunction) object;
		
		if (Math.abs(_slope - other._slope) < EPSILON) {
			if (Math.abs(_yIntercept - other._yIntercept) < EPSILON) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(_slope).append(_yIntercept)
				.toHashCode();
	}
}
