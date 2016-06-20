package mcdacw.valuation;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import mcdacw.paremetervalidator.ParameterValidator;
import mcdacw.valuation.domain.IDomain;
import mcdacw.valuation.domain.fuzzyset.FuzzySet;
import mcdacw.valuation.domain.fuzzyset.HesitantFuzzySet;
import mcdacw.valuation.domain.fuzzyset.Label;

/**
 * Valoración hesitant.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public class Hesitant extends Qualitative {

	private UnaryRelationType _unaryRelation;
	private Label _term;
	private Label _lowerTerm;
	private Label _upperTerm;

	/**
	 * Enumerado para los tipos de relaciones unarias.
	 * 
	 * @author Estrella Liébana, Francisco Javier
	 * @version 1.0
	 */
	public enum UnaryRelationType {
		LowerThan("lower than"), GreaterThan("greather than"), AtLeast(
				"at least"), AtMost("at most");

		/*
		 * Valor del tipo
		 */
		private String _text;

		/**
		 * Constructor del tipo a partir del texto de la enumeración.
		 * 
		 * @param text
		 *            Texto de la enumeración
		 */
		private UnaryRelationType(String text) {
			_text = text;
		}
		
		public String getText() {
			return _text;
		}
	}

	/**
	 * Constructor por defecto.
	 * <p>
	 * Privado para impedir su uso de este modo.
	 */
	private Hesitant() {
		super();
		_unaryRelation = null;
		_label = null;
		_term = null;
		_lowerTerm = null;
		_upperTerm = null;
	}

	/**
	 * Constructor de Hesitant indicando el dominio.
	 * 
	 * @param domain
	 *            Dominio de la valoración.
	 * @throw IllegalArgumentException Si el dominio está vacío o,
	 *        <p>
	 *        si el dominio es nulo o
	 *        <p>
	 *        si el dominio no es un HessitantFuzzySet.
	 */
	public Hesitant(HesitantFuzzySet domain) {
		this();
		setDomain(domain);
	}

	/**
	 * Establece el dominio de la valoración.
	 * 
	 * @param domain
	 *            Dominio de la valoración.
	 * @throw IllegalArgumentException Si el dominio está vacío o,
	 *        <p>
	 *        si el dominio es nulo o
	 *        <p>
	 *        si el dominio no es un HessitantFuzzySet.
	 */
	@Override
	public void setDomain(IDomain domain) {

		ParameterValidator.notNull(domain, "domain");
		ParameterValidator.notIllegalElementType(domain,
				new String[] { HesitantFuzzySet.class.toString() }, "domain");
		ParameterValidator.notEmpty(((HesitantFuzzySet) domain).getLabels()
				.toArray(), "domain");

		_domain = (HesitantFuzzySet) domain;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mcdacw.valuation.Qualitative#setLabel(int)
	 */
	@Override
	public void setLabel(int pos) {
		Label newLabel = ((HesitantFuzzySet) _domain).getLabel(pos);
		ParameterValidator.notNull(newLabel, "newLabel");

		_label = newLabel;
		disableComposite();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mcdacw.valuation.Qualitative#setLabel(java.lang.String)
	 */
	@Override
	public void setLabel(String name) {
		Label newLabel = ((HesitantFuzzySet) _domain).getLabel(name);
		ParameterValidator.notNull(newLabel, "newLabel");

		_label = newLabel;
		disableComposite();
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

		if (((HesitantFuzzySet) _domain).contains(label)) {
			_label = (Label) label;
			disableComposite();
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

	/**
	 * Establece los valores para una relación unaria.
	 * 
	 * @param unaryRelation
	 *            Relación unaria.
	 * 
	 * @param term
	 *            Etiqueta de la relación.
	 * 
	 * @throws IllegalArgumentException
	 *             Si unaryRelation es null o
	 *             <p>
	 *             si term es null o
	 *             <p>
	 *             si label no está contenido en el conjunto hesitant.
	 */
	public void setUnaryRelation(UnaryRelationType unaryRelation, Label term) {
		ParameterValidator.notNull(unaryRelation, "unaryRelation");
		ParameterValidator.notNull(term, "term");

		if (!((HesitantFuzzySet) _domain).contains(term)) {
			throw new IllegalArgumentException("Label not contains in domain.");
		}

		_unaryRelation = unaryRelation;
		_term = term;

		disablePrimary();
		disableBinary();
	}

	/**
	 * Establece los valores para una relación unaria.
	 * 
	 * @param unaryRelation
	 *            Relación unaria.
	 * 
	 * @param termPos
	 *            Posición de la etiqueta de la relación.
	 * 
	 * @throws IllegalArgumentException
	 *             Si unaryRelation es null o
	 *             <p>
	 *             si termPos es inválido.
	 */
	public void setUnaryRelation(UnaryRelationType unaryRelation, int termPos) {
		ParameterValidator.notNull(unaryRelation, "unaryRelation");
		Label term = ((HesitantFuzzySet) _domain).getLabel(termPos);
		ParameterValidator.notNull(term, "term");

		_unaryRelation = unaryRelation;
		_term = term;

		disablePrimary();
		disableBinary();
	}

	/**
	 * Establece los valores para una relación unaria.
	 * 
	 * @param unaryRelation
	 *            Relación unaria.
	 * 
	 * @param termName
	 *            Nombre de la etiqueta de la relación.
	 * 
	 * @throws IllegalArgumentException
	 *             Si unaryRelation es null o
	 *             <p>
	 *             si termName es inválido.
	 */
	public void setUnaryRelation(UnaryRelationType unaryRelation,
			String termName) {
		ParameterValidator.notNull(unaryRelation, "unaryRelation");
		Label term = ((HesitantFuzzySet) _domain).getLabel(termName);
		ParameterValidator.notNull(term, "term");

		_unaryRelation = unaryRelation;
		_term = term;

		disablePrimary();
		disableBinary();

	}

	/**
	 * Devuelve el tipo de relación unaria.
	 * 
	 * @return Tipo de relación unaria.
	 */
	public UnaryRelationType getUnaryRelation() {
		return _unaryRelation;
	}

	/**
	 * Devuelve el término de una relación unaria.
	 * 
	 * @return Término de una relación unaria.
	 */
	public Label getTerm() {
		return _term;
	}

	/**
	 * Establece los valores para una relación binaria.
	 * 
	 * @param lowerTerm
	 *            Etiqueta inferior de la relación.
	 * @param upperTerm
	 *            Etiqueta superior de la relación.
	 * 
	 * @throws IllegalArgumentException
	 *             Si lowerTerm es null o
	 *             <p>
	 *             si lowerTerm no está contenido en el conjunto hesitant o
	 *             <p>
	 *             si upperTerm es null o
	 *             <p>
	 *             si upperTerm no está contenido en el conjunto hesitant o
	 *             <p>
	 *             si upperTerm es inferior o igual a lowerTerm.
	 */
	public void setBinaryRelation(Label lowerTerm, Label upperTerm) {
		ParameterValidator.notNull(lowerTerm, "lowerTerm");
		ParameterValidator.notNull(upperTerm, "upperTerm");

		if (!((HesitantFuzzySet) _domain).contains(lowerTerm)) {
			throw new IllegalArgumentException(
					"Lower term not contains in domain.");
		}
		if (!((HesitantFuzzySet) _domain).contains(upperTerm)) {
			throw new IllegalArgumentException(
					"Upper term not contains in domain.");
		}
		if (upperTerm.compareTo(lowerTerm) <= 0) {
			throw new IllegalArgumentException(
					"Upper term is bigger than lower term.");
		}

		_lowerTerm = lowerTerm;
		_upperTerm = upperTerm;

		disablePrimary();
		disableUnary();

	}

	/**
	 * Establece los valores para una relación binaria.
	 * 
	 * @param lowerTermPos
	 *            Posición de la etiqueta inferior de la relación.
	 * @param upperTermPos
	 *            Posición de la etiqueta superior de la relación.
	 * 
	 * @throws IllegalArgumentException
	 *             Si lowerTermPos es inválido o
	 *             <p>
	 *             si upperTermPos es inválido o
	 *             <p>
	 *             si upperTermPos es inferior o igual a lowerTermPos.
	 */
	public void setBinaryRelation(int lowerTermPos, int upperTermPos) {

		Label lowerTerm = ((HesitantFuzzySet) _domain).getLabel(lowerTermPos);
		ParameterValidator.notNull(lowerTerm, "lowerTerm");
		Label upperTerm = ((HesitantFuzzySet) _domain).getLabel(upperTermPos);
		ParameterValidator.notNull(upperTerm, "lowerTerm");

		if (upperTermPos <= lowerTermPos) {
			throw new IllegalArgumentException(
					"Upper term is bigger than lower term.");
		}

		_lowerTerm = lowerTerm;
		_upperTerm = upperTerm;

		disablePrimary();
		disableUnary();

	}

	/**
	 * Establece los valores para una relación binaria.
	 * 
	 * @param lowerTermName
	 *            Nombre de la etiqueta inferior de la relación.
	 * @param upperTermName
	 *            Nombre de la etiqueta superior de la relación.
	 * 
	 * @throws IllegalArgumentException
	 *             Si lowerTermName es inválido o
	 *             <p>
	 *             si upperTermName es inválido o
	 *             <p>
	 *             si la etiqueta de upperTermName es inferior o igual a la
	 *             etiqueta de lowerTermName.
	 */
	public void setBinaryRelation(String lowerTermName, String upperTermName) {

		Label lowerTerm = ((HesitantFuzzySet) _domain).getLabel(lowerTermName);
		ParameterValidator.notNull(lowerTerm, "lowerTerm");
		Label upperTerm = ((HesitantFuzzySet) _domain).getLabel(upperTermName);
		ParameterValidator.notNull(upperTerm, "lowerTerm");

		if (upperTerm.compareTo(lowerTerm) <= 0) {
			throw new IllegalArgumentException(
					"Upper term is bigger than lower term.");
		}

		_lowerTerm = lowerTerm;
		_upperTerm = upperTerm;

		disablePrimary();
		disableUnary();
	}

	/**
	 * Devuelve el término inferior de una relación binaria.
	 * 
	 * @return Término inferior de una relación binaria.
	 */
	public Label getLowerTerm() {
		return _lowerTerm;
	}

	/**
	 * Devuelve el término superior de una relación binaria.
	 * 
	 * @return Término superior de una relación binaria.
	 */
	public Label getUpperTerm() {
		return _upperTerm;
	}

	/**
	 * Indica si el hesitant es primario.
	 * 
	 * @return True si el hesitant es primario
	 *         <p>
	 *         False si el hesitant es compuesto.
	 */
	public boolean isPrimary() {
		return (_label != null);
	}

	/**
	 * Indica si el hesitant es compuesto.
	 * 
	 * @return True si el hesitant es compuesto
	 *         <p>
	 *         False si el hesitant es primario.
	 */
	public boolean isComposite() {
		return (isUnary() || isBinary());
	}

	/**
	 * Indica si el hesitant es unario.
	 * 
	 * @return True si el hesitant es unario
	 *         <p>
	 *         False si el hesitant no es unario.
	 */
	public boolean isUnary() {
		return ((_unaryRelation != null) && (_term != null));
	}

	/**
	 * Indica si el hesitant es binario.
	 * 
	 * @return True si el hesitant es binario
	 *         <p>
	 *         False si el hesitant no es binario.
	 */
	public boolean isBinary() {
		return ((_lowerTerm != null) && (_upperTerm != null));
	}

	/**
	 * Elimina los valores de los hesitant primarios
	 */
	private void disablePrimary() {
		_label = null;
	}

	/**
	 * Elimina los valores de los hesitant compuestos
	 */
	private void disableComposite() {
		disableUnary();
		disableBinary();
	}

	/**
	 * Elimina los valores de los hesitant unarios.
	 */
	private void disableUnary() {
		_unaryRelation = null;
		_term = null;
	}

	/**
	 * Elimina los valores de los hesitant binarios.
	 */
	private void disableBinary() {
		_lowerTerm = null;
		_upperTerm = null;
	}

	/**
	 * Devuelve la envoltura de la valoración.
	 * 
	 * @return Envoltura de la valoración.
	 */
	public Label[] getEnvelope() {

		Label[] result = new Label[2];

		if (isPrimary()) {
			result[0] = _label;
			result[1] = _label;
		} else if (isUnary()) {
			if (UnaryRelationType.LowerThan.equals(_unaryRelation)) {
				int pos = ((HesitantFuzzySet) _domain).getPos(_term) - 1;
				result[0] = ((HesitantFuzzySet) _domain).getLabel(0);
				result[1] = ((HesitantFuzzySet) _domain).getLabel(pos);
			} else if (UnaryRelationType.GreaterThan.equals(_unaryRelation)) {
				int cardinality = ((HesitantFuzzySet) _domain).cardinality();
				int pos = ((HesitantFuzzySet) _domain).getPos(_term) + 1;
				result[0] = ((HesitantFuzzySet) _domain).getLabel(pos);
				result[1] = ((HesitantFuzzySet) _domain)
						.getLabel(cardinality - 1);
			} else if (UnaryRelationType.AtLeast.equals(_unaryRelation)) {
				int cardinality = ((HesitantFuzzySet) _domain).cardinality();
				result[0] = _term;
				result[1] = ((HesitantFuzzySet) _domain)
						.getLabel(cardinality - 1);
			} else if (UnaryRelationType.AtMost.equals(_unaryRelation)) {
				result[0] = ((HesitantFuzzySet) _domain).getLabel(0);
				result[1] = _term;
			} else {
				result = null;
			}
		} else if (isBinary()) {
			result[0] = _lowerTerm;
			result[1] = _upperTerm;
		} else {
			result = null;
		}

		return result;
	}

	/**
	 * Devuelve los índices de la envoltura de la valoración.
	 * 
	 * @return Índices de la envoltura de la valoración.
	 */
	public int[] getEnvelopeIndex() {

		int[] result = null;
		Label[] envelope = getEnvelope();

		if (envelope != null) {
			result = new int[2];
			result[0] = ((HesitantFuzzySet) _domain).getPos(envelope[0]);
			result[1] = ((HesitantFuzzySet) _domain).getPos(envelope[1]);
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (isPrimary()) {
			return (_label + " in " + _domain);
		} else if (isUnary()) {
			return (_unaryRelation.toString() + " " + _term + " in " + _domain);
		} else if (isBinary()) {
			return ("between " + _lowerTerm + " and " + _upperTerm + " in " + _domain);
		} else {
			return "";
		}
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
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (_unaryRelation != null) {
			return new HashCodeBuilder(17, 31).append(_label).append(_domain)
					.append(_unaryRelation.toString()).append(_term)
					.append(_lowerTerm).append(_upperTerm).toHashCode();
		} else {
			return new HashCodeBuilder(17, 31).append(_label).append(_domain)
					.append("").append(_term).append(_lowerTerm)
					.append(_upperTerm).toHashCode();
		}
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
				new String[] { Hesitant.class.toString() }, "other");

		if (_domain.equals(other.getDomain())) {
			int[] tei = getEnvelopeIndex();
			int[] oei = ((Hesitant) other).getEnvelopeIndex();
			double tc = (double) (tei[1] + tei[0]) / 2d;
			double tw = (double) (tei[1] - tei[0]) / 2d;
			double oc = (double) (oei[1] + oei[0]) / 2d;
			double ow = (double) (oei[1] - oei[0]) / 2d;

			double acceptability;
			if ((tw + ow) == 0) {
				if (tc == oc) {
					return 0;
				} else if (tc > oc) {
					return 1;
				} else {
					return -1;
				}
			}
			acceptability = (tc - oc) / (tw + ow);
			double limit = 0.25;
			if ((acceptability <= limit) && (acceptability >= -limit)) {
				return 0;
			} else if (acceptability > limit) {
				return 1;
			} else {
				return -1;
			}
		} else {
			throw new IllegalArgumentException("Different domains");
		}
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

		final Hesitant other = (Hesitant) object;
		return new EqualsBuilder().append(_label, other._label)
				.append(_domain, other._domain)
				.append(_unaryRelation, other._unaryRelation)
				.append(_term, other._term)
				.append(_lowerTerm, other._lowerTerm)
				.append(_upperTerm, other._upperTerm).isEquals();

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
