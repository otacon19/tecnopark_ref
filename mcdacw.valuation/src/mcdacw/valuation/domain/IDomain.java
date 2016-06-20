package mcdacw.valuation.domain;

/**
 * Interfaz a implementar por un dominio.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public interface IDomain extends Cloneable {
	
	/**
	 * Devuelve un duplicado del objeto.
	 * 
	 * @return Duplicado del objeto.
	 */
	public Object clone();
}
