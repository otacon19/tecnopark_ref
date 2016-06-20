package mcdacw.valuation.domain.numeric;

/**
 * Interfaz que debe implementar un dominio numérico.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public interface INumericDomain extends Cloneable, Comparable<INumericDomain> {

	/**
	 * Establece el límite inferior del dominio.
	 * 
	 * @param min
	 *            Límite inferior del dominio.
	 * 
	 * @throws IllegalArgumentException
	 *             Si min > max.
	 */
	public void setMin(double min);

	/**
	 * Devuelve el límite inferior del dominio.
	 * 
	 * @return Límite inferior del dominio.
	 */
	public double getMin();

	/**
	 * Establece el límite superior del dominio.
	 * 
	 * @param max
	 *            Límite superior del dominio.
	 * 
	 * @throws IllegalArgumentException
	 *             Si min > max.
	 */
	public void setMax(double max);

	/**
	 * Devuelve el límite superior del dominio.
	 * 
	 * @return Límite superior del dominio.
	 */
	public double getMax();

	/**
	 * Establece los límites inferior y superior del dominio.
	 * 
	 * @param min
	 *            Límite inferior del dominio.
	 * @param max
	 *            Límite superior del dominio.
	 * 
	 * @throws IllegalArgumentException
	 *             Si min > max.
	 */
	public void setMinMax(double min, double max);

	/**
	 * Devuelve un valor que resume el dominio numérico.
	 * <p>
	 * Es empleado para las comprobaciones.
	 * 
	 * @return Valor que resume el dominio numérico.
	 */
	public double resumeValue();

}
