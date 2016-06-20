package mcdacw.valuation;

import java.util.List;

import mcdacw.paremetervalidator.ParameterValidator;
import mcdacw.valuation.Valuation;
import mcdacw.valuation.domain.IDomain;
import mcdacw.valuation.domain.fuzzyset.FuzzySet;
import mcdacw.valuation.domain.fuzzyset.Label;

/**
 * Valoración unificada.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public class UnifiedValuation extends Valuation {
	
	/**
	 * Constructor por defecto.
	 * <p>
	 * Privado para impedir su instanciación de este modo.
	 */
	private UnifiedValuation() {
	}

	/**
	 * Constructor indicando dominio.
	 * 
	 * @param domain
	 *            Dominio de la valoración.
	 */
	public UnifiedValuation(FuzzySet domain) {
		this();
		_domain = domain;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Valuation other) {

		ParameterValidator.notNull(other, "other");
		ParameterValidator.notIllegalElementType(other,
				new String[] { UnifiedValuation.class.toString() }, "other");

		FuzzySet thisDomain = (FuzzySet) _domain;
		FuzzySet otherDomain = (FuzzySet) other.getDomain();
		for (int i = 0; i < thisDomain.cardinality(); i++) {
			if (!thisDomain.getLabel(i).equals(otherDomain.getLabel(i))) {
				throw new IllegalArgumentException("Different domains");
			}
		}

		TwoTuple thisDisunification = Valuation.disunification(thisDomain);
		TwoTuple otherDisunification = Valuation.disunification(otherDomain);

		return thisDisunification.compareTo(otherDisunification);

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
		return null;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		FuzzySet domain = (FuzzySet) _domain;
		StringBuilder result = new StringBuilder("{");
		List<Label> labels = domain.getLabels();
		String name;
		String measure;
		for (int i = 0; i < labels.size(); i++) {
			name = labels.get(i).getName();
			measure = domain.getMeasure(i).toString();
			if (measure.length() > 4) {
				measure = measure.substring(0, 4);
			}
			result.append("(" + name + "," + measure + ")");
			if ((i + 1) != labels.size()) {
				result.append(",");
			}
		}
		result.append("}");
		return result.toString();
	}

}
