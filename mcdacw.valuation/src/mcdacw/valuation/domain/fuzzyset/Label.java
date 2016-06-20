package mcdacw.valuation.domain.fuzzyset;

import mcdacw.paremetervalidator.ParameterValidator;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Etiqueta linguística.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public class Label implements Cloneable, Comparable<Label> {

	/**
	 * Nombre de la etiqueta.
	 */
	private String _name;

	/**
	 * Semántica de la etiqueta.
	 */
	private IMembershipFunction _semantic;

	/**
	 * Constructor por defecto de Label.
	 * <p>
	 * Privado para impedir su instanciación de este modo.
	 */
	private Label() {
	};

	/**
	 * Construye un Label asignando el nombre y la función de pertenencia.
	 * 
	 * @param name
	 *            Nombre del Label.
	 * @param semantic
	 *            Función de pertenencia del Label.
	 * @throws IllegalArgumentException
	 *             Si el nombre es nulo o vacío o si la semántica es nula.
	 */
	public Label(String name, IMembershipFunction semantic) {
		this();
		
		ParameterValidator.notEmpty(name, "name");
		ParameterValidator.notNull(semantic, "semantic");

		_name = name;
		_semantic = semantic;
	}

	/**
	 * Devuele el nombre de la etiqueta.
	 * 
	 * @return Nombre de la etiqueta.
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Devuelve la semántica de la etiqueta.
	 * 
	 * @return Semántica de la etiqueta.
	 */
	public IMembershipFunction getSemantic() {
		return _semantic;
	}

	/**
	 * Devuelve una descripción de la etiqueta
	 * 
	 * @return Descripción de la etiqueta.
	 */
	@Override
	public String toString() {
		return (_name + "::" + _semantic.toString());
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

		final Label other = (Label) object;
		return new EqualsBuilder().append(_name, other._name)
				.append(_semantic, other._semantic).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(_name).append(_semantic)
				.toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		Object result = null;

		try {
			result = super.clone();
		} catch (CloneNotSupportedException cnse) {
			// No debería ocurrir
		}
		
		((Label) result)._name = new String(_name);
		((Label) result)._semantic = (IMembershipFunction) _semantic.clone();
		
		return result;
	}

	/**
	 * Compara dos etiquetas a través de su función de pertenencia.
	 * 
	 * @param other
	 *            Etiqueta a comparar.
	 * @return this < other --> <0.
	 *         <p>
	 *         this = other --> 0
	 *         <p>
	 *         this > other --> >0
	 * @throws IllegalArgumentException
	 *             Si other es null.
	 */
	@Override
	public int compareTo(Label other) {
		ParameterValidator.notNull(other, "other");
		return _semantic.compareTo(other._semantic);
	}
}
