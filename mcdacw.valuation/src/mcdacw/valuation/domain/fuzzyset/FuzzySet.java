package mcdacw.valuation.domain.fuzzyset;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import mcdacw.paremetervalidator.ParameterValidator;
import mcdacw.valuation.domain.IDomain;
import mcdacw.valuation.domain.numeric.NumericDomain;

/**
 * Conjunto de etiquetas difuso.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public class FuzzySet extends LabelSet implements IDomain, Cloneable {

	/**
	 * Medidas de las etiquetas del conjunto de etiquetas.
	 */
	protected List<Double> _measures;
	
	/**
	 * Información no balanceada
	 */
	protected UnbalancedInfo _unbalancedInfo;

	/**
	 * Constructor por defecto de un conjunto difuso.
	 */
	public FuzzySet() {
		super();
		_measures = new LinkedList<Double>();
		_unbalancedInfo = null;
	}

	/**
	 * Construye un conjunto difuso indicado las etiquetas que conforman el
	 * mismo.
	 * <p>
	 * Establecerá todos los pesos a 0.
	 * 
	 * @param labels
	 *            Etiquetas del conjunto difuso.
	 * @throws IllegalArgumentException
	 *             Si labels es nulo o,
	 *             <p>
	 *             si alguna etiqueta es nula o
	 *             <p>
	 *             si el nombre de alguna etiqueta está duplicado.
	 */
	public FuzzySet(List<Label> labels) {
		this();
		setLabels(labels);
	}

	/**
	 * Construye un conjunto difuso indicado las etiquetas que conforman el
	 * mismo y las medidas asociadas.
	 * 
	 * @param labels
	 *            Etiquetas del conjunto difuso.
	 * @param measures
	 *            Medidas asociadas a las etiquetas.
	 * @throws IllegalArgumentException
	 *             Si labels es nulo o,
	 *             <p>
	 *             si alguna etiqueta es nula o,
	 *             <p>
	 *             si el nombre de alguna etiqueta está duplicado o,
	 *             <p>
	 *             si measures es nulo o,
	 *             <p>
	 *             si alguna medidas es nula o
	 *             <p>
	 *             si alguna medida no está en el rango [0, 1] o
	 *             <p>
	 *             si el número de medidas no coincide con el de etiquetas.
	 */
	public FuzzySet(List<Label> labels, List<Double> measures) {
		this(labels);
		setMeasures(measures);
	}

	/**
	 * Establece las etiquetas que conforman el conjunto.
	 * <p>
	 * Establecerá todas las medidas a 0.
	 * 
	 * @param labels
	 *            Conjunto de etiquetas.
	 * @throws IllegalArgumentException
	 *             Si labels es nulo o,
	 *             <p>
	 *             alguna de las etiquetas contenidas lo es o,
	 *             <p>
	 *             el nombre de alguna etiqueta está duplicado.
	 */
	@Override
	public void setLabels(List<Label> labels) {
		super.setLabels(labels);
		List<Double> measures = new LinkedList<Double>();
		for (int element = 0; element < _labels.size(); element++) {
			measures.add(0d);
		}
		_measures = measures;
	}

	/**
	 * Establece las medidas de las etiquetas.
	 * <p>
	 * Deben indicarse tantas medidas como etiquetas tenga el conjunto difuso.
	 * 
	 * @param measures
	 *            Medidas de las etiquetas.
	 * @throws IllegalArgumentException
	 *             Si medidas es nulo o,
	 *             <p>
	 *             si alguna medida es nula o
	 *             <p>
	 *             si alguna medida no está en el rango [0, 1]
	 *             <p>
	 *             si el número de medidas no coincide con el de etiquetas.
	 */
	public void setMeasures(List<Double> measures) {
		ParameterValidator.notNull(measures, "measures");
		int cardinality = cardinality();
		ParameterValidator.notInvalidSize(measures.size(), cardinality,
				cardinality, "measures");
		for (Double measure : measures) {
			ParameterValidator.notNull(measure, "measure");
			ParameterValidator.notInvalidSize(measure, 0.0, 1.0, "measure");
		}
		_measures = measures;
	}

	/**
	 * Devuelve las medidas de las etiquetas.
	 * 
	 * @return Medidas de las etiquetas.
	 */
	public List<Double> getMeasures() {
		return _measures;
	}
	
	public UnbalancedInfo getUnbalancedInfo() {
		return _unbalancedInfo;
	}
	
	public void setUnbalancedInfo(UnbalancedInfo unbalancedInfo) {
		_unbalancedInfo = unbalancedInfo;
	}

	/**
	 * Añade una nueva etiqueta al conjunto difuso.
	 * <p>
	 * Establece la medida de la etiqueta a 0.
	 * 
	 * @param label
	 *            Etiqueta a añadir.
	 * 
	 * @throws IllegalArgumentException
	 *             Si label es null o
	 *             <p>
	 *             si el nombre de la etiqueta está duplicado.
	 */
	@Override
	public void addLabel(Label label) {
		addLabel(cardinality(), label);
	}

	/**
	 * Añade una nueva etiqueta y la medida de la misma al conjunto difuso.
	 * 
	 * @param label
	 *            Etiqueta a añadir.
	 * @param measure
	 *            Medida de la etiqueta.
	 * @throws IllegalArgumentException
	 *             Si label es null o
	 *             <p>
	 *             si el nombre de la etiqueta está duplicado o
	 *             <p>
	 *             si measure no está contenido en el intervalo [0, 1].
	 */
	public void addLabel(Label label, double measure) {
		addLabel(cardinality(), label, measure);
	}

	/**
	 * Añade una etiqueta en la posición indicada.
	 * <p>
	 * Establece la medida de la etiqueta a 0.
	 * 
	 * @param pos
	 *            Posición de inserción.
	 * @param label
	 *            Etiqueta a añadir.
	 * 
	 * @throws IllegalArgumentException
	 *             Si la etiqueta es nula o,
	 *             <p>
	 *             si la posición es inválida o,
	 *             <p>
	 *             si el nombre de la etiqueta ya existe.
	 */
	@Override
	public void addLabel(int pos, Label label) {
		super.addLabel(pos, label);
		_measures.add(pos, 0d);
	}

	/**
	 * Añade una etiqueta en la posición indicada así como la medida de esta.
	 * 
	 * @param pos
	 *            Posición de inserción.
	 * @param label
	 *            Etiqueta a añadir.
	 * @param measure
	 *            Medida de la etiqueta.
	 * 
	 * @throws IllegalArgumentException
	 *             Si la etiqueta es nula o,
	 *             <p>
	 *             si la posición es inválida o,
	 *             <p>
	 *             si el nombre de la etiqueta ya existe o
	 *             <p>
	 *             si measure no está contenido en el rango [0, 1].
	 */
	public void addLabel(int pos, Label label, double measure) {
		super.addLabel(pos, label);

		ParameterValidator.notNull(measure, "measure");
		ParameterValidator.notInvalidSize(measure, 0.0, 1.0, "measure");

		_measures.add(pos, measure);
	}

	/**
	 * Elimina una etiqueta dada su posición.
	 * <p>
	 * Elimina también la medida asociada.
	 * 
	 * @param pos
	 *            Posición de la etiqueta a eliminar.
	 * 
	 * @throws IllegalArgumentException
	 *             Si se índica una posición vacía o
	 *             <p>
	 *             si la cardinalidad es vacía.
	 */
	@Override
	public void rmLabel(int pos) {

		ParameterValidator.notEmpty(_labels.toArray(), "_labels");
		ParameterValidator.notInvalidSize(pos, 0, cardinality() - 1, "pos");

		_labels.remove(pos);
		_measures.remove(pos);
	}

	/**
	 * Elimina una etiqueta dado su nombre.
	 * <p>
	 * Elimina también la medida asociada.
	 * 
	 * @param name
	 *            Nombre de la etiqueta a eliminar.
	 */
	@Override
	public void rmLabel(String name) {

		if (name == null) {
			return;
		}

		int pos = getPos(name);

		if (pos != -1) {
			_labels.remove(pos);
			_measures.remove(pos);
		}
	}

	/**
	 * Elimina una etiqueta.
	 * <p>
	 * Elimina también la medida asociada.
	 * 
	 * @param label
	 *            Etiqueta a eliminar.
	 */
	@Override
	public void rmLabel(Label label) {

		if (label == null) {
			return;
		}

		int pos = getPos(label);

		if (pos != -1) {
			_labels.remove(pos);
			_measures.remove(pos);
		}
	}

	/**
	 * Establece la medida de una etiqueta concreta dada su posición.
	 * 
	 * @param pos
	 *            Posición de la etiqueta.
	 * @param measure
	 *            Medida de la etiqueta.
	 * @throws IllegalArgumentException
	 *             Si la posición es inválida o,
	 *             <p>
	 *             si measure es null o
	 *             <p>
	 *             si measure no está contenido en el intervalo [0, 1].
	 */
	public void setMeasure(int pos, double measure) {

		ParameterValidator.notEmpty(_labels.toArray(), "_labels");
		ParameterValidator.notInvalidSize(pos, 0, cardinality() - 1, "pos");

		ParameterValidator.notNull(measure, "measure");
		ParameterValidator.notInvalidSize(measure, 0.0, 1.0, "measure");

		_measures.set(pos, measure);

	}

	/**
	 * Establece la medida de una etiqueta concreta dado su nombre.
	 * 
	 * @param name
	 *            Nombre de la etiqueta.
	 * @param measure
	 *            Medida de la etiqueta.
	 * @throws IllegalArgumentException
	 *             Si el nombre es nulo o,
	 *             <p>
	 *             si el nombre no corresponde a ninguna etiqueta o,
	 *             <p>
	 *             si measure es null o
	 *             <p>
	 *             si measure no está contenido en el intervalo [0, 1].
	 */
	public void setMeasure(String name, double measure) {

		ParameterValidator.notNull(name, "name");
		ParameterValidator.notNull(measure, "measure");
		ParameterValidator.notInvalidSize(measure, 0.0, 1.0, "measure");

		int pos = getPos(name);

		if (pos != -1) {
			_measures.set(pos, measure);
		} else {
			throw new IllegalArgumentException("Inexistent element");
		}

	}

	/**
	 * Establece la medida de una etiqueta.
	 * 
	 * @param label
	 *            Etiqueta.
	 * @param measure
	 *            Medida de la etiqueta.
	 * @throws IllegalArgumentException
	 *             Si la etiqueta es nula o,
	 *             <p>
	 *             si measure es null o
	 *             <p>
	 *             si measure no está contenido en el intervalo [0, 1].
	 */
	public void setMeasure(Label label, double measure) {

		ParameterValidator.notNull(label, "label");
		ParameterValidator.notNull(measure, "measure");
		ParameterValidator.notInvalidSize(measure, 0.0, 1.0, "measure");

		int pos = getPos(label);

		if (pos != -1) {
			_measures.set(pos, measure);
		} else {
			throw new IllegalArgumentException("Inexistent element");
		}

	}

	/**
	 * Devuelve la medida de una etiqueta concreta dada su posición.
	 * 
	 * @param pos
	 *            Posición de la etiqueta.
	 * @return La medida de la etiqueta.
	 * 
	 * @throws IllegalArgumentException
	 *             Si indica una posición erronea.
	 */
	public Double getMeasure(int pos) {

		ParameterValidator.notEmpty(_labels.toArray(), "_labels");
		ParameterValidator.notInvalidSize(pos, 0, cardinality() - 1, "pos");

		return _measures.get(pos);

	}

	/**
	 * Devuelve la medida de una etiqueta concreta dado su nombre.
	 * 
	 * @param name
	 *            Nombre de la etiqueta.
	 * 
	 * @return La medida de la etiqueta.
	 *         <p>
	 *         null si no existe una etiqueta con este nombre.
	 */
	public Double getMeasure(String name) {

		int pos = getPos(name);

		if (pos != -1) {
			return _measures.get(pos);
		} else {
			return null;
		}

	}

	/**
	 * Devuelve la medida de una etiqueta concreta.
	 * 
	 * @param label
	 *            Etiqueta.
	 * 
	 * @return La medida de la etiqueta.
	 *         <p>
	 *         null si no existe la etiqueta.
	 */
	public Double getMeasure(Label label) {

		int pos = getPos(label);

		if (pos != -1) {
			return _measures.get(pos);
		} else {
			return null;
		}

	}

	/**
	 * Calcula un valor que resume el conjunto difuso.
	 * 
	 * @return Valor que resume el conjunto difuso.
	 */
	public double chi() {
		double result, measuresSum, sum;
		int pos;

		if (cardinality() == 0) {
			return 0;
		}

		sum = measuresSum = pos = 0;

		for (Double measure : _measures) {
			sum += measure * pos++;
			measuresSum += measure;
		}

		result = sum / measuresSum;

		return result;
	}

	/**
	 * Comprueba si el FuzzySet es una partición difusa.
	 * 
	 * @return True si el FuzzySet es una partición difusa.
	 *         <p>
	 *         False si el FuzzySet no es una partición difusa.
	 */
	public boolean isFuzzyPartition() {

		int cardinality = cardinality();

		// Cardinalidad no vacía
		if (cardinality == 0) {
			return false;
		}

		// Pertenencia 1 entre 0 y 1
		PiecewiseDefinedFunction piecewiseDefinedFunction = new PiecewiseDefinedFunction();
		PiecewiseDefinedFunction semantic;
		Map<NumericDomain, IPieceFunction> pieces;
		for (Label label : _labels) {
			semantic = label.getSemantic().toPiecewiseDefinedFunction();
			pieces = semantic.getPieces();
			for (NumericDomain domain : pieces.keySet()) {
				piecewiseDefinedFunction.addPiece(domain, pieces.get(domain));
				piecewiseDefinedFunction.simplify();
			}
		}

		pieces = piecewiseDefinedFunction.getPieces();
		if (pieces.size() == 1) {
			NumericDomain domain = new NumericDomain(0, 1);
			LinearPieceFunction piece = (LinearPieceFunction) pieces
					.get(domain);
			if (piece != null) {
				if ((piece.getSlope() == 0) && (piece.getYIntercept() == 1)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Comprueba si la cardinalidad es impar.
	 * 
	 * @return True si la cardinalidad es impar.
	 *         <p>
	 *         False si la cardinalidad es par.
	 */
	public boolean isOdd() {
		return (cardinality() % 2 != 0);
	}

	/**
	 * Comprueba si un FuzzySet está conformado por etiquetas triangulares.
	 * 
	 * @return True si el FuzzySet está conformado por etiquetas triangulares.
	 *         <p>
	 *         False si el FuzzySet no está conformado por etiquetas
	 *         triangulares.
	 */
	public boolean isTriangular() {

		if (cardinality() == 0) {
			return false;
		}

		IMembershipFunction semantic;
		for (Label label : _labels) {
			semantic = label.getSemantic();

			if (semantic instanceof TrapezoidalMembershipFunction) {
				if (!((TrapezoidalMembershipFunction) semantic).isTriangular()) {
					return false;
				}

			} else {
				return false;
			}
		}

		return true;
	}

	/**
	 * Comprueba si un FuzzySet es un TOR (Triangular Odd Ruspini).
	 * 
	 * @return True si el FuzzySet es un TOR.
	 *         <p>
	 *         False si sel FuzzySet no es un TOR.
	 */
	public boolean isTOR() {

		if (isFuzzyPartition()) {
			if (isOdd()) {
				return isTriangular();
			}
		}

		return false;
	}

	/**
	 * Comprueba si el FuzzySet es simétrico.
	 * 
	 * @return True si el FuzzySet es simétrico.
	 *         <p>
	 *         False si el FuzzySet no es simétrico.
	 */
	public boolean isSymmetrical() {

		if (cardinality() == 0) {
			return true;
		}

		Label label1;
		Label label2;
		double center;
		int centralPos = cardinality() / 2;

		// Controlamos etiqueta central
		if (isOdd()) {
			label1 = _labels.get(centralPos);
			IMembershipFunction semantic = label1.getSemantic();
			if (!semantic.isSymmetrical()) {
				return false;
			} else {
				center = semantic.resumeValue();
			}

		} else {

			// Calculamos el centro
			label1 = _labels.get(centralPos - 1);
			label2 = _labels.get(centralPos);

			center = (label2.getSemantic().resumeValue() + label1.getSemantic()
					.resumeValue()) / 2d;
		}

		// Vemos si las etiquetas son simétricas
		for (int i = 0; i < centralPos; i++) {
			label1 = _labels.get(i);
			label2 = _labels.get((cardinality() - 1) - i);
			if (!(label1.getSemantic().isSymmetrical(label2.getSemantic(),
					center))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Comprueba si el FuzzySet es uniforme.
	 * 
	 * @return True si el FuzzySet es uniforme.
	 *         <p>
	 *         False si el FuzzySet no es uniforme.
	 */
	public boolean isUniform() {

		int cardinality = cardinality();
		
		if (cardinality <= 1) {
			return true;
		}

		// Calculamos la distancia entre el centro de las dos primeras etiquetas
		Label label1 = _labels.get(0);
		Label label2 = _labels.get(1);

		double center1 = label1.getSemantic().getCenter().resumeValue();
		double center2 = label2.getSemantic().getCenter().resumeValue();
		
		double distance = center2 - center1;
		double error = (1 / Math.pow(10, Double.toString(distance).length() - 2)) * 1.5;
		double distanceLower = distance - error;
		double distanceUpper = distance + error;

		// Comprobamos si la distancia entre todas las etiquetas es igual
		for (int i = 2; i < cardinality; i++) {
			label1 = (Label) label2.clone();
			label2 = _labels.get(i);
			
			center1 = center2;
			center2 = label2.getSemantic().getCenter().resumeValue();
			distance = center2 - center1;
			if (!((distanceLower <= distance) && (distance <= distanceUpper))) {
				return false;
			}
			
		}

		return true;
	}

	/**
	 * Comprueba si el FuzzySet es un BLTS (Basic Linguistic Term Set).
	 * 
	 * @return True si el FuzzySet es un BLTS.
	 *         <p>
	 *         False si el FuzzySet no es un BLTS.
	 */
	public boolean isBLTS() {

		if (isTOR()) {
			if (isSymmetrical()) {
				return isUniform();
			}
		}

		return false;
	}
	
	/**
	 * Devuelve el sumario del Fuzzy Set.
	 * 
	 * @return Sumario del FuzzySet.
	 */
	public String summary() {
		
		StringBuilder result = new StringBuilder();
		int cardinality;
		
		if ((cardinality = cardinality()) != 0) {
			result.append("(");
			for (int i = 0; i < cardinality - 1; i++) {
				result.append(_labels.get(i).getName() + ",");
			}
			result.append(_labels.get(cardinality - 1).getName() + ")");
		}
		return result.toString();
	}

	/**
	 * Devuelve una descripción del conjunto difuso.
	 * 
	 * @return Descripción del conjunto difuso.
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
				result.append("[");
				result.append(_labels.get(pos));
				result.append(";");
				result.append(_measures.get(pos));
				result.append("]");
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

		final FuzzySet other = (FuzzySet) object;
		return new EqualsBuilder().append(_labels, other._labels)
				.append(_measures, other._measures).isEquals();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(_labels).append(_measures)
				.toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {

		Object result;

		result = super.clone();

		List<Double> measures = new LinkedList<Double>();
		for (Double measure : _measures) {
			measures.add(new Double(measure));
		}

		((FuzzySet) result)._measures = measures;

		return result;
	}
	
}
