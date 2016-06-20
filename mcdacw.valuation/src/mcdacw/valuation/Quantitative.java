package mcdacw.valuation;

import java.util.List;

import mcdacw.paremetervalidator.ParameterValidator;

/**
 * Abstracción de una valoración cuantitativa
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public abstract class Quantitative extends Valuation {

	/**
	 * Cálculo del mínimo de un conjunto de valoraciones.
	 */
	private static final int MIN = -1;

	/**
	 * Cálculo del máximo de un conjunto de valoraciones.
	 */
	private static final int MAX = 1;

	/**
	 * Devuelve la valoración normalizada.
	 * 
	 * @return Valoración normalizada.
	 */
	public abstract Valuation getNormalized();

	/**
	 * Calcula el mínimo o máximo de una serie de valoraciones cuantitativas.
	 * 
	 * @param valuations
	 * @param type
	 *            -1 para el mínimo
	 *            <p>
	 *            1 para el máximo
	 * @return Mímino o máximo de las valoraciones indicadas.
	 * @throws IllegalArgumentException
	 *             Si valuations es nulo.
	 */
	private static Valuation getLimit(List<Quantitative> valuations, int type) {

		Quantitative result;

		ParameterValidator.notNull(valuations, "valuations");
		
		if (valuations.isEmpty()) {
			return null;
		}

		result = valuations.get(0);
		for (Quantitative valuation : valuations) {
			if (type == valuation.compareTo(result)) {
				result = valuation;
			}
		}

		return result;

	}

	/**
	 * Calcula el mínimo de una serie de valoraciones cuantitativas.
	 * 
	 * @param valuations
	 *            Valoraciones de las que calcular el mímino.
	 * @return Mímino de las valoraciones indicadas.
	 * @throws IllegalArgumentException
	 *             Si valuations es nulo.
	 */
	public static Valuation min(List<Quantitative> valuations) {
		return getLimit(valuations, MIN);
	}

	/**
	 * Calcula el máximo de una serie de valoraciones cuantitativas.
	 * 
	 * @param valuations
	 *            Valoraciones de las que calcular el máximo.
	 * @return Máximo de las valoraciones indicadas.
	 * @throws IllegalArgumentException
	 *             Si valuations es nulo.
	 */
	public static Valuation max(List<Quantitative> valuations) {
		return getLimit(valuations, MAX);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mcdacw.valuation.Valuation#clone()
	 */
	@Override
	public Object clone() {
		return super.clone();
	}

}
