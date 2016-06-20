package mcdacw.valuation.domain.fuzzyset;

/**
 * Clase activadora de los dominios difusos que permite acceder a una instancia de
 * 'LabelFactory' mediante la cual crear las etiquetas.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 * 
 */
public class Activator {

	/**
	 * Instancia del activador para singleton.
	 */
	private static Activator _activator = new Activator();

	/**
	 * Factoría de etiquetas gestionada por el activador.
	 */
	private LabelFactory _labelFactory;

	/**
	 * Constructor privado para singleton.
	 */
	private Activator() {
		_labelFactory = new LabelFactory();
	}

	/**
	 * Acceso a la instancia única del activador.
	 * 
	 * @return Instancia única del activador.
	 */
	public static Activator getActivator() {
		return _activator;
	}

	/**
	 * Devuelve la factoría de etiquetas gestionada por el activador.
	 * 
	 * @return Factoría de etiquetas gestionada por el activador.
	 */
	public LabelFactory getLabelFactory() {
		return _labelFactory;
	}
}
