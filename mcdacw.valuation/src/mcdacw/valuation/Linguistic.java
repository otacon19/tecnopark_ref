package mcdacw.valuation;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import mcdacw.paremetervalidator.ParameterValidator;
import mcdacw.valuation.domain.IDomain;
import mcdacw.valuation.domain.fuzzyset.FuzzySet;
import mcdacw.valuation.domain.fuzzyset.IMembershipFunction;
import mcdacw.valuation.domain.fuzzyset.Label;

/**
 * Valoración lingüística.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public class Linguistic extends Qualitative {

	/**
	 * Constructor por defecto.
	 * <p>
	 * Privado para impedir la instanciación de este modo.
	 */
	private Linguistic() {
	}

	/**
	 * Constructor de la valoración indicando el dominio.
	 * <p>
	 * Establece la selección a la primera etiqueta del dominio.
	 * 
	 * @param domain
	 *            Dominio de valoración.
	 * @param label
	 *            Etiqueta correspondiente con la valoración.
	 * @throws IllegalArgumentException
	 *             Si el dominio es nulo o
	 *             <p>
	 *             si el dominio está vacio o.
	 */
	public Linguistic(FuzzySet domain) {
		this();
		setDomain(domain);
	}

	/**
	 * Constructor de la valoración.
	 * 
	 * @param domain
	 *            Dominio de valoración.
	 * @param label
	 *            Etiqueta correspondiente con la valoración.
	 * @throws IllegalArgumentException
	 *             Si el dominio es nulo o,
	 *             <p>
	 *             si el dominio está vacio o,
	 *             <p>
	 *             si la etiqueta es nula o,
	 *             <p>
	 *             si la etiqueta no está contenida en el dominio.
	 */
	public Linguistic(FuzzySet domain, Label label) {
		this(domain);
		setLabel(label);
	}

	/**
	 * Establece el dominio de la valoración.
	 * <p>
	 * Selecciona como valoración la primera del conjunto.
	 * 
	 * @param domain
	 *            Dominio de la valoración.
	 * @throw IllegalArgumentException Si el dominio está vacío o,
	 *        <p>
	 *        si el dominio es nulo o
	 *        <p>
	 *        si el dominio no es un FuzzySet.
	 */
	@Override
	public void setDomain(IDomain domain) {

		ParameterValidator.notNull(domain, "domain");
		ParameterValidator.notIllegalElementType(domain,
				new String[] { FuzzySet.class.toString() }, "domain");
		ParameterValidator.notEmpty(((FuzzySet) domain).getLabels().toArray(),
				"domain");

		_domain = domain;
		setLabel(((FuzzySet) _domain).getLabel(0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mcdacw.valuation.Qualitative#setLabel(int)
	 */
	@Override
	public void setLabel(int pos) {
		Label newLabel = ((FuzzySet) _domain).getLabel(pos);
		ParameterValidator.notNull(newLabel, "newLabel");

		_label = newLabel;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mcdacw.valuation.Qualitative#setLabel(java.lang.String)
	 */
	@Override
	public void setLabel(String name) {
		Label newLabel = ((FuzzySet) _domain).getLabel(name);
		ParameterValidator.notNull(newLabel, "newLabel");

		_label = newLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mcdacw.valuation.Qualitative#setLabel(mcdacw.valuation.domain.fuzzyset
	 * .Label)
	 */
	@Override
	public void setLabel(Label label) {
		ParameterValidator.notNull(label, "label");

		if (((FuzzySet) _domain).contains(label)) {
			_label = (Label) label;
		} else {
			throw new IllegalArgumentException("Label not contains in domain.");
		}

	}

	/**
	 * Devuelve la etiqueta de la evaluación.
	 * 
	 * @return Etiqueta de la evaluación.
	 */
	public Label getLabel() {
		return _label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mcdacw.valuation.Valuation#neg()
	 */
	@Override
	public Valuation neg() {
		Qualitative result = (Linguistic) clone();

		FuzzySet domain = (FuzzySet) _domain;
		if (domain.cardinality() > 1) {
			int negPos = (domain.cardinality() - 1) - domain.getPos(_label);
			result.setLabel(negPos);
		}

		return result;
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

		ParameterValidator.notNull(fuzzySet, "fuzzySet");

		if (!fuzzySet.isBLTS()) {
			throw new IllegalArgumentException("Not BLTS fuzzy set.");
		}

		int cardinality;
		IMembershipFunction function;
		FuzzySet result;

		result = (FuzzySet) ((FuzzySet) fuzzySet).clone();
		cardinality = ((FuzzySet) fuzzySet).cardinality();

		int thisCardinality = ((FuzzySet)_domain).cardinality();
		boolean thisBLTS = ((FuzzySet) _domain).isBLTS();
		
		if ((thisCardinality == cardinality) && (thisBLTS)) {
			int thisPos = ((FuzzySet) _domain).getPos(_label);
			int measure;
			for (int i = 0; i < cardinality; i++) {
				measure = (i == thisPos) ? 1 : 0;
				result.setMeasure(i, measure);
			}
		} else {
			for (int i = 0; i < cardinality; i++) {
				function = result.getLabel(i).getSemantic();
				result.setMeasure(i, function.maxMin(_label.getSemantic()));
			}
		}

		return result;
	}

	/**
	 * Transforma una etiqueta a la etiqueta correspondiente en otro dominio.
	 * 
	 * @param fuzzySet
	 *            Dominio en el que transformar la etiqueta.
	 * 
	 * @return Etiqueta dos tuplas transformada.
	 * 
	 * @throws IllegalArgumentException
	 *             Si fuzzySet es null o
	 *             <p>
	 *             Si fuzzySet no es un dominio BLTS.
	 */
	public TwoTuple transform(FuzzySet fuzzySet) {
		return (new TwoTuple((FuzzySet) _domain, _label)).transform(fuzzySet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (_label + " in " + _domain);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {

		if (this == object) {
			return true;
		}

		if (object == null) {
			return false;
		}

		if (object.getClass() != this.getClass()) {
			return false;
		}

		final Linguistic other = (Linguistic) object;
		return new EqualsBuilder().append(_label, other._label)
				.append(_domain, other._domain).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(_label).append(_domain)
				.toHashCode();
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
				new String[] { Linguistic.class.toString() }, "other");

		if (_domain.equals(other.getDomain())) {
			return _label.compareTo(((Linguistic) other)._label);
		} else {
			throw new IllegalArgumentException("Different domains");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		Object result = null;

		result = super.clone();

		return result;
	}

}
