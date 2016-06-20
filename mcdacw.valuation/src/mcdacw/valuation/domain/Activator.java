package mcdacw.valuation.domain;

/**
 * Clase activadora de los dominios que permite acceder a una instancia de
 * 'DomainFactory' mediante la cual crear los dominios.
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
	 * Factoría de dominios gestionada por el activador.
	 */
	private DomainFactory _domainFactory;

	/**
	 * Constructor privado para singleton.
	 */
	private Activator() {
		_domainFactory = new DomainFactory();
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
	 * Devuelve la factoría de dominios gestionada por el activador.
	 * 
	 * @return Factoría de dominios gestionada por el activador.
	 */
	public DomainFactory getDomainFactory() {
		return _domainFactory;
	}
}
