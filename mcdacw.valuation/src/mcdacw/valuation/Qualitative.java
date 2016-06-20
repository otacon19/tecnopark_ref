package mcdacw.valuation;

import mcdacw.valuation.domain.fuzzyset.Label;

/**
 * Abstracción de una valoración cualitativa.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public abstract class Qualitative extends Valuation {

	/**
	 * Etiqueta que corresponde con la valoración realizada.
	 */
	protected Label _label;

	/**
	 * Establece la etiqueta de la valoración a partir de su posición en el
	 * dominio.
	 * 
	 * @param pos
	 *            Posición de la valoración.
	 * @throws IllegalArgumentException
	 *             Si la posición es inválida.
	 */
	public abstract void setLabel(int pos);

	/**
	 * Establece la etiqueta de la valoración a partir del nombre de una
	 * etiqueta del dominio.
	 * 
	 * @param name
	 *            Nombre de la etiqueta.
	 *            
	 * @throws IllegalArgumentException
	 *             Si name es null o
	 *             <p>
	 *             si la etiqueta no está contenida en el dominio o.
	 */
	public abstract void setLabel(String name);



	/**
	 * Establece la etiqueta de la valoración.
	 * 
	 * @param label
	 *            Etiqueta del dominio.
	 *            
	 * @throws IllegalArgumentException
	 *             Si label es null o
	 *             <p>
	 *             si la etiqueta no está contenida en el dominio o.
	 */
	public abstract void setLabel(Label label);

	/*
	 * (non-Javadoc)
	 * 
	 * @see mcdacw.valuation.Valuation#clone()
	 */
	@Override
	public Object clone() {

		Object result = null;

		result = super.clone();

		if (_label != null) {
			((Qualitative) result)._label = (Label) _label.clone();
		} else {
			((Qualitative) result)._label = null;
		}

		return result;
	}
}
