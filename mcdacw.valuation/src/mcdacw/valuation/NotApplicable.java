package mcdacw.valuation;

import mcdacw.valuation.domain.IDomain;
import mcdacw.valuation.domain.fuzzyset.FuzzySet;

/**
 * Valoración no aplicable.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public class NotApplicable extends Valuation {

	/**
	 * Constructor por defecto.
	 * <p>
	 * Privado para impedir su instanciación de este modo.
	 */
	private NotApplicable() {
	}

	/**
	 * Constructor indicando dominio.
	 * 
	 * @param domain
	 *            Dominio de la valoración.
	 */
	public NotApplicable(IDomain domain) {
		this();
		_domain = domain;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Valuation o) {

		if (o == null) {
			return -1;
		}

		if (o instanceof NotApplicable) {
			return 0;
		} else {
			return -1;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mcdacw.valuation.Valuation#setDomain(mcdacw.valuation.domain.IDomain)
	 */
	@Override
	public void setDomain(IDomain domain) {
		_domain = domain;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mcdacw.valuation.Valuation#neg()
	 */
	@Override
	public Valuation neg() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mcdacw.valuation.Valuation#unification(mcdacw.valuation.domain.fuzzyset
	 * .FuzzySet)
	 */
	@Override
	public FuzzySet unification(FuzzySet fuzzySet) {
		return null;
	}

}
