package mcdacw.valuation.domain.fuzzyset;

/**
 * Interfaz a implementar por un trozo de función.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public interface IPieceFunction {

	/**
	 * Devuelve la función suma de las dos funciones.
	 * 
	 * @param other
	 *            Función a sumar con la actual.
	 * 
	 * @return Función suma de las dos funciones.
	 * 
	 * @throws IllegalArgumentException
	 *             Si other es null o
	 *             <p>
	 *             si other no es del tipo apropiado.
	 */
	public IPieceFunction sum(IPieceFunction other);
}
