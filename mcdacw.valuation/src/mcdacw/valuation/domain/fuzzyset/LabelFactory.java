package mcdacw.valuation.domain.fuzzyset;

/**
 * Factoria de etiquetas.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public class LabelFactory {

	/**
	 * Crea una etiqueta con una función de pertenencia trapezoidal.
	 * 
	 * @param name
	 *            Nombre de la etiqueta.
	 * @param limits
	 *            Límites de la función de pertenencia.
	 * @return Etiqueta creada.
	 * 
	 * @throws IllegalArgumentException
	 *             Si el nombre es nulo o vacío o si el vector es nulo, no
	 *             tienes todos los elementos necesarios o los valores de estos
	 *             no son correctos.
	 */
	public Label buildTrapezoidalLabel(String name, double[] limits) {

		Label result;
		IMembershipFunction semantic;

		semantic = new TrapezoidalMembershipFunction(limits);
		result = new Label(name, semantic);

		return result;
	}
}
