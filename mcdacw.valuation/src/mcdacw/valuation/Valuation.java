package mcdacw.valuation;

import mcdacw.paremetervalidator.ParameterValidator;
import mcdacw.valuation.domain.IDomain;
import mcdacw.valuation.domain.fuzzyset.FuzzySet;

/**
 * Abstracción de una evaluación.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public abstract class Valuation implements Cloneable, Comparable<Valuation> {

	/**
	 * Dominio de la valoración.
	 */
	protected IDomain _domain;

	/**
	 * Establece el dominio de la valoración.
	 * <p>
	 * Selecciona como valoración la primera del conjunto.
	 * 
	 * @param domain
	 *            Dominio de la valoración.
	 */
	public abstract void setDomain(IDomain domain);

	/**
	 * Devuelve el dominio de la valoración.
	 * 
	 * @return Dominio de la valoración.
	 */
	public IDomain getDomain() {
		return _domain;
	}

	/**
	 * Devuelve la negación de la evaluación.
	 * 
	 * @return Negación de la evaluación.
	 */
	public abstract Valuation neg();

	/**
	 * Unifica la valoración en un conjunto difuso.
	 * 
	 * @param fuzzySet
	 *            Conjunto difuso empleado para realizar la unificación.
	 * 
	 * @return Unificación de la valoración en el conjunto difuso indicado.
	 * 
	 * @throws IllegalArgumentException
	 *             si fuzzySet es null o
	 *             <p>
	 *             si fuzzySet no es un BLTS.
	 */
	public abstract FuzzySet unification(FuzzySet fuzzySet);

	/**
	 * Desunifica un fuzzySet a una valoración 2-tuplas.
	 * 
	 * @param fuzzySet
	 *            FuzzySet a desunificar.
	 * @return Valoración dos tuplas desunificada.
	 * 
	 * @trhows IllegalArgumentException si fuzzySet es null.
	 */
	public static TwoTuple disunification(FuzzySet fuzzySet) {

		ParameterValidator.notNull(fuzzySet, "fuzzySet");

		TwoTuple result = new TwoTuple((FuzzySet) fuzzySet.clone());

		int size = fuzzySet.cardinality();
		double numerator = 0;
		double denominator = 0;
		double measure;
		double beta = 0;
		for (int i = 0; i < size; i++) {
			((FuzzySet) result.getDomain()).setMeasure(i, 0);
			measure = fuzzySet.getMeasure(i);
			numerator += measure * i;
			denominator += measure;
		}

		if (denominator != 0) {
			beta = numerator / denominator;
		}
		
		result.delta(beta);

		return result;
	}

	/**
	 * Devuelve un duplicado del objeto.
	 * 
	 * @return Duplicado del objeto.
	 */
	public Object clone() {

		Object result = null;

		try {
			result = super.clone();
		} catch (CloneNotSupportedException e) {
			// No debe fallar
		}

		((Valuation) result)._domain = (IDomain) _domain.clone();

		return result;
	}
}
