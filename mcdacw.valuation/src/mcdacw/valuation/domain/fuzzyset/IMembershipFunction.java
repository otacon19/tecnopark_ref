package mcdacw.valuation.domain.fuzzyset;

import mcdacw.valuation.Interval;
import mcdacw.valuation.domain.numeric.NumericDomain;

/**
 * Interfaz que debe implementar una función de pertenencia.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public interface IMembershipFunction extends Cloneable,
		Comparable<IMembershipFunction> {

	/**
	 * Convierte una función de pertenencia en una función a trozos.
	 * 
	 * @return Función a trozos.
	 */
	public PiecewiseDefinedFunction toPiecewiseDefinedFunction();

	/**
	 * Comprueba si una función de pertenencia es simétrica.
	 * 
	 * @return True si es simétrica.
	 *         <p>
	 *         False en caso contrario.
	 */
	public boolean isSymmetrical();

	/**
	 * Comprueba si una función de pertenencia es simétrica respecto a otra dado
	 * el centro de la distribución.
	 * 
	 * @param other
	 *            Etiqueta a comparar si es simétrica respecto a la actual.
	 * @param center
	 *            Centro de la distribución.
	 * @return True si son simétricas.
	 *         <p>
	 *         False si no son simétricas.
	 */
	public boolean isSymmetrical(IMembershipFunction other, double center);

	/**
	 * Devuelve el centro de la etiqueta.
	 * 
	 * @return Centro de la etiqueta.
	 */
	public NumericDomain getCenter();

	/**
	 * Devuelve la cobertura de la etiqueta.
	 * 
	 * @return Cobertura de la etiqueta.
	 */
	public NumericDomain getCoverage();

	/**
	 * Devuelve el valor de pertenencia de un valor dado.
	 * 
	 * @param x
	 *            Valor para el que calcularemos el valor de pertenencia.
	 * @return Valor de pertenencia.
	 */
	public double getMembershipValue(double x);

	/**
	 * Devuelve un valor que resume la función de pertenencia.
	 * <p>
	 * Es empleado para la comparación.
	 * 
	 * @return Valor de resumen de la función de pertenencia.
	 */
	public double resumeValue();

	/**
	 * Valor máximo de la función en un intervalo.
	 * 
	 * @param Intervalo
	 *            en el que obtener el máximo.
	 * 
	 * @return Valor máximo de la función en el intervalo.
	 * 
	 * @throws IllegalArgumentException
	 *             Si interval es null.
	 */
	public double maxMin(Interval interval);

	/**
	 * Devuelve el máximo valor del mínimo entre la función actual y otra
	 * función.
	 * 
	 * @param function
	 *            Función a combinar con la actual, mediante el mínimo, para
	 *            obtener el máximo valor.
	 * 
	 * @return Máximo valor del mínimo entre la función actual y function.
	 * 
	 * @throws IllegalArgumentException
	 *             Si function es null.
	 */
	public double maxMin(IMembershipFunction function);

	/**
	 * Devuelve un duplicado del objeto.
	 * 
	 * @return Duplicado del objeto.
	 */
	public Object clone();

}
