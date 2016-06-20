package mcdacw.valuation.domain.fuzzyset;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import mcdacw.paremetervalidator.ParameterValidator;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Conjunto de etiquetas.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public class LabelSet implements Cloneable {

	/**
	 * Lista de etiquetas que conforma el conjunto de etiquetas.
	 */
	protected List<Label> _labels;

	/**
	 * Constructor por defecto de LabelSet.
	 */
	public LabelSet() {
		_labels = new LinkedList<Label>();
	}

	/**
	 * Constructor de LabelSet a partir de las etiquetas del mimso.
	 * 
	 * @param labels
	 *            Etiquetas que conforman el labelSet.
	 * @throws IllegalArgumentException
	 *             Si labels es null o,
	 *             <p>
	 *             alguna de las etiquetas contenidas lo es o
	 *             <p>
	 *             el nombre de alguna etiqueta está duplicado.
	 */
	public LabelSet(List<Label> labels) {
		this();
		setLabels(labels);
	}
	
	/**
	 * Establece las etiquetas que conforman el conjunto de etiquetas.
	 * 
	 * @param labels
	 *            Etiquetas que conformarán el dominio de etiquetas.
	 * @throws IllegalArgumentException
	 *             Si labels es nulo o,
	 *             <p>
	 *             alguna de las etiquetas contenidas lo es o,
	 *             <p>
	 *             el nombre de alguna etiqueta está duplicado.
	 */
	public void setLabels(List<Label> labels) {
		ParameterValidator.notNull(labels, "labels");
		for (Label label : labels) {
			addLabel(label);
		}
		// Si todo fue bien, establecemos las etiquetas pasadas
		_labels = labels;
	}

	/**
	 * Devuelve las etiquetas que conforman el conjunto de etiquetas.
	 * 
	 * @return Etiquetas que conforman el conjunto de etiquetas.
	 */
	public List<Label> getLabels() {
		return _labels;
	}

	/**
	 * Añade una etiqueta al conjunto de etiquetas.
	 * 
	 * @param label
	 *            Etiqueta a añadir al conjunto de etiquetas.
	 * @throws IllegalArgumentException
	 *             Si label es null o
	 *             <p>
	 *             si el nombre de la etiqueta está duplicado.
	 */
	public void addLabel(Label label) {
		addLabel(cardinality(), label);
	}

	/**
	 * Inserta una etiqueta en una determinada posición.
	 * 
	 * @param pos
	 *            Posición de inserción.
	 * @param label
	 *            Etiqueta a insertar.
	 * 
	 * @throws IllegalArgumentException
	 *             Si la etiqueta es nula o,
	 *             <p>
	 *             si la posición es inválida o,
	 *             <p>
	 *             si el nombre de la etiqueta ya existe.
	 */
	public void addLabel(int pos, Label label) {
		
		ParameterValidator.notNull(label, "label");
		ParameterValidator.notInvalidSize(pos, 0, cardinality(), "pos");
		if (contains(label.getName())) {
			throw new IllegalArgumentException("Duplicate label name.");
		}
		
		_labels.add(pos, label);
	}

	/**
	 * Elimina una etiqueta dada su posición.
	 * 
	 * @param pos
	 *            Posición de la etiqueta a eliminar.
	 * 
	 * @throws IllegalArgumentException
	 *             Si se índica una posición vacía o
	 *             <p>
	 *             si la cardinalidad es vacía.
	 */
	public void rmLabel(int pos) {

		ParameterValidator.notEmpty(_labels.toArray(), "_labels");
		ParameterValidator.notInvalidSize(pos, 0, cardinality() - 1, "pos");

		_labels.remove(pos);
	}

	/**
	 * Elimina una etiqueta dado su nombre.
	 * 
	 * @param name
	 *            Nombre de la etiqueta a eliminar.
	 */
	public void rmLabel(String name) {

		if (name == null) {
			return;
		}

		int pos = getPos(name);
		if (pos != -1) {
			_labels.remove(pos);
		}
	}

	/**
	 * Elimina una etiqueta.
	 * 
	 * @param label
	 *            Etiqueta a eliminar.
	 */
	public void rmLabel(Label label) {

		if (label == null) {
			return;
		}

		int pos = getPos(label);
		if (pos != -1) {
			_labels.remove(pos);
		}
	}

	/**
	 * Comprueba si existe una etiqueta con el nombre indicado.
	 * 
	 * @param name
	 *            Nombre de la etiqueta.
	 */
	public boolean contains(String name) {
		return (getPos(name) != -1);
	}

	/**
	 * Comprueba si la etiqueta indicada está contenida.
	 * 
	 * @param label
	 *            Etiqueta a comprobar.
	 */
	public boolean contains(Label label) {
		return _labels.contains(label);
	}

	/**
	 * Indica la cardinalidad del conjunto de etiquetas.
	 * 
	 * @return Cardinalidad del conjunto de etiquetas.
	 */
	public int cardinality() {
		return _labels.size();
	}

	/**
	 * Devuelve una etiqueta dada su posición.
	 * 
	 * @param pos
	 *            Posición de la etiqueta.
	 * @return Etiqueta que se encuentra en la posición indicada.
	 * 
	 * @throws IllegalArgumentException
	 *             Si el objeto no se encuentra en los tipos soportados o
	 *             <p>
	 *             en caso de tratarse de una posición, esta es inválida.
	 */
	public Label getLabel(int pos) {

		ParameterValidator.notEmpty(_labels.toArray(), "_labels");
		ParameterValidator.notInvalidSize(pos, 0, cardinality() - 1, "pos");

		return _labels.get(pos);

	}

	/**
	 * Devuelve una etiqueta dado su nombre.
	 * 
	 * @param name
	 *            Nombre de la etiqueta.
	 * @return Etiqueta que se corresponde con el nombre indicado.
	 *         <p>
	 *         Null si no se corresponde con ninguna etiqueta.
	 */
	public Label getLabel(String name) {

		int pos = getPos(name);

		if (pos != -1) {
			return _labels.get(pos);
		} else {
			return null;
		}
	}

	/**
	 * Devuelve la posición de una etiqueta en el conjunto de etiquetas dado su
	 * nombre.
	 * 
	 * @param name
	 *            Nombre de la etiqueta.
	 * @return Posición de la etiqueta.
	 *         <p>
	 *         -1 si no está contenida.
	 */
	public int getPos(String name) {

		if (name == null) {
			return -1;
		}

		if (ParameterValidator.isEmpty(name)) {
			return -1;
		}

		Iterator<Label> iterator;
		Label auxLabel;

		iterator = _labels.iterator();
		while (iterator.hasNext()) {
			auxLabel = iterator.next();
			if (auxLabel.getName().equals(name)) {
				return _labels.indexOf(auxLabel);
			}

		}
		return -1;
	}

	/**
	 * Devuelve la posición de una etiqueta en el conjunto de etiquetas.
	 * 
	 * @param label
	 *            Etiqueta de la cual queremos conocer su posición.
	 *            
	 * @return Posición de la etiqueta.
	 *         <p>
	 *         -1 si no está contenida.
	 */
	public int getPos(Label label) {
		return _labels.indexOf(label);
	}

	/**
	 * Devuelve una descripción del conjunto de etiquetas.
	 * 
	 * @return Descripción del conjunto de etiquetas.
	 */
	@Override
	public String toString() {

		StringBuilder result = new StringBuilder();

		int cardinality = cardinality();
		if (cardinality > 0) {
			for (int pos = 0; pos < cardinality; pos++) {
				if (pos > 0) {
					result.append(", ");
				}
				result.append(_labels.get(pos));
			}
		}

		return "{" + result + "}";
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

		final LabelSet other = (LabelSet) object;
		return new EqualsBuilder().append(_labels, other._labels).isEquals();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(_labels).toHashCode();
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

		List<Label> cloneLabels = new LinkedList<Label>();
		for (Label label : _labels) {
			cloneLabels.add((Label) label.clone());
		}

		((LabelSet) result)._labels = cloneLabels;

		return result;
	}
}
