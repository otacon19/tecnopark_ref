package mcdacw.valuation;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import mcdacw.paremetervalidator.ParameterValidator;
import mcdacw.valuation.domain.fuzzyset.FuzzySet;
import mcdacw.valuation.domain.fuzzyset.Label;

/**
 * Valoración 2-tuplas.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public class TwoTuple extends Linguistic {

	/**
	 * Translación simbólica.
	 */
	private double _alpha;

	/**
	 * Constructor por defecto de la clase TwoTuple.
	 * <p>
	 * Privado para impedir su instanciación de este modo.
	 */
	private TwoTuple() {
		super(null);
	}

	/**
	 * Constructor de la clase TwoTuple indicando el dominio.
	 * 
	 * @param domain
	 *            Dominio de valoraciones.
	 */
	public TwoTuple(FuzzySet domain) {
		super(domain);
		_alpha = 0d;
	}

	/**
	 * Constructor de la clase TwoTuple indicando dominio y etiqueta.
	 * 
	 * @param domain
	 *            Dominio de valoraciones.
	 * @param label
	 *            Etiqueta asignada.
	 * @throws InvalidValueException
	 *             Si el valor de alpha no está dentro del rango permitido.
	 * @throws IllegalArgumentException
	 *             Si el objeto no se encuentra en los tipos soportados.
	 */
	public TwoTuple(FuzzySet domain, Label label) {
		super(domain, label);
	}

	/**
	 * Constructor de la clase TwoTuple indicando dominio, etiqueta y
	 * translación simbólica.
	 * 
	 * @param domain
	 *            Dominio de valoraciones.
	 * @param label
	 *            Etiqueta asignada.
	 * @param alpha
	 *            Translación simbólica
	 * @throws InvalidValueException
	 *             Si el valor de alpha no está dentro del rango permitido.
	 * @throws IllegalArgumentException
	 *             Si el objeto no se encuentra en los tipos soportados.
	 */
	public TwoTuple(FuzzySet domain, Label label, double alpha) {
		super(domain, label);
		setAlpha(alpha);
	}

	/**
	 * Establece la etiqueta de la valoración a partir de la posición
	 * <p>
	 * Establece la translación simbólica a 0.
	 * 
	 * @param pos
	 *            Posición de la etiqueta.
	 * 
	 * @throws IllegalArgumentException
	 *             Si la posición es inválida.
	 */
	@Override
	public void setLabel(int pos) {
		super.setLabel(pos);
		_alpha = 0;
	}

	/**
	 * Establece la etiqueta de la valoración a partir del nombre de una
	 * etiqueta.
	 * <p>
	 * Establece la translación simbólica a 0.
	 * 
	 * @param name
	 *            Nombre de la etiqueta.
	 * 
	 * @throws IllegalArgumentException
	 *             Si el nombre es nulo o
	 *             <p>
	 *             si no existe una etiqueta con dicho nombre.
	 */
	@Override
	public void setLabel(String name) {
		super.setLabel(name);
		_alpha = 0;
	}

	/**
	 * Establece la etiqueta de la valoración.
	 * <p>
	 * Establece la translación simbólica a 0.
	 * 
	 * @param label
	 *            Etiqueta de la valoración.
	 * 
	 * @throws IllegalArgumentException
	 *             Si la etiqueta es nula o,
	 *             <p>
	 *             si la etiqueta no está contenida en el dominio o.
	 */
	@Override
	public void setLabel(Label label) {
		super.setLabel(label);
		_alpha = 0;
	}

	/**
	 * Establece el valor de la translación simbólica.
	 * <p>
	 * El valor debe estar en el intervalor [-0.5,0.5)
	 * 
	 * @param alpha
	 *            Translación simbólica.
	 * @throws IllegalArgumentException
	 *             Si el valor de alpha no está dentro del rango permitido o
	 *             <p>
	 *             si es la primera etiqueta o la última y se realiza un
	 *             desplazamiento inválido.
	 */
	public void setAlpha(double alpha) {

		// Controlar valores de alpha inválidos
		ParameterValidator.notInvalidSize(alpha, -0.5, 0.5, true, false,
				"alpha");

		// Controlar translaciones inválidas
		int pos = ((FuzzySet) _domain).getPos(_label);

		// Controlar translación hacia la izquierda en la etiqueta inicial
		if ((pos == 0) && (alpha < 0)) {
			throw new IllegalArgumentException("Invalid alpha value");
		}

		// Controlar translación hacia la derecha en la etiqueta final
		if ((pos == ((FuzzySet) _domain).cardinality() - 1) && (alpha > 0)) {
			throw new IllegalArgumentException("Invalid alpha value");
		}

		_alpha = alpha;
	}

	/**
	 * Devuelve el valor de la translación simbólica.
	 * 
	 * @return Valor de la translación simbólica.
	 */
	public Double getAlpha() {
		return _alpha;
	}

	/**
	 * Calcula el valor 2-tuplas correspondiente a la medida de valoración beta.
	 * 
	 * @param beta
	 *            Medida de valoración beta.
	 * @throws IllegalArgumentException
	 *             Si beta es inválido.
	 */
	public void delta(double beta) {
		int labelIndex;
		double alpha;

		labelIndex = (int) Math.round(beta);
		setLabel(labelIndex);

		// Para que funcione igual en todos los equipos, restringimos a 5
		// decimales
		alpha = beta - labelIndex;
		alpha *= 100000;
		alpha = Math.round(alpha);
		alpha /= 100000;
		if (alpha == 0.5) {
			labelIndex++;
			alpha = -0.5;
		}
		setAlpha(alpha);

	}

	/**
	 * Calcula la medida de valoración beta correspondiente al valor 2-tuplas
	 * actuales.
	 * 
	 * @return Medida de valoración beta.
	 */
	public double inverseDelta() {
		return ((FuzzySet) _domain).getPos(_label) + _alpha;
	}
	
	/*
	 * (non-Javadoc)
	 * @see mcdacw.valuation.Linguistic#neg()
	 */
	@Override
	public Valuation neg() {
		TwoTuple result = (TwoTuple) clone();

		FuzzySet domain = (FuzzySet) _domain;
		if (domain.cardinality() > 1) {
			result.delta(((double)(domain.cardinality() - 1)) - inverseDelta());
		}

		return result;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mcdacw.valuation.Linguistic#transform(mcdacw.valuation.domain.fuzzyset
	 * .FuzzySet)
	 */
	@Override
	public TwoTuple transform(FuzzySet fuzzySet) {

		TwoTuple result = null;
		
		ParameterValidator.notNull(fuzzySet, "fuzzySet");
		
		if (!fuzzySet.isBLTS()) {
			throw new IllegalArgumentException("Not BLTS fuzzy set.");
		}
		
		int thisCardinality = ((FuzzySet)_domain).cardinality();
		int otherCardinality = fuzzySet.cardinality();
		
		double numerator = inverseDelta() * ((double) (otherCardinality - 1));
		double denominator = (double) (thisCardinality - 1);
		
		double beta = numerator / denominator;
		
		result = new TwoTuple(fuzzySet);
		result.delta(beta);
		return result;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ("[" + _label + ", " + _alpha + "]" + " in " + _domain);
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

		final TwoTuple other = (TwoTuple) object;
		return new EqualsBuilder().append(_label, other._label)
				.append(_domain, other._domain).append(_alpha, other._alpha)
				.isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(_label).append(_domain)
				.append(_alpha).toHashCode();
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
				new String[] { TwoTuple.class.toString() }, "other");

		if (_domain.equals(other.getDomain())) {
			Label otherLabel = ((TwoTuple) other)._label;
			double otherAlpha = ((TwoTuple) other)._alpha;

			int aux;
			if ((aux = _label.compareTo(otherLabel)) == 0) {
				return Double.compare(_alpha, otherAlpha);
			} else {
				return aux;
			}

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
