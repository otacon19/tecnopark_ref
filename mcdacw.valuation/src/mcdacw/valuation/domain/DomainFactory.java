package mcdacw.valuation.domain;

import java.util.LinkedList;
import java.util.List;

import mcdacw.valuation.domain.fuzzyset.FuzzySet;
import mcdacw.valuation.domain.fuzzyset.HesitantFuzzySet;
import mcdacw.valuation.domain.fuzzyset.Label;
import mcdacw.valuation.domain.fuzzyset.UnbalancedInfo;
import mcdacw.valuation.domain.numeric.IntervalNumericDomain;
import mcdacw.valuation.domain.numeric.NumericDomain;

/**
 * Factoría de dominios.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public class DomainFactory {

	/**
	 * Construye un dominio difuso.
	 * 
	 * @return Un dominio formado por un conjunto difuso.
	 */
	public FuzzySet buildFuzzySetDomain() {
		return new FuzzySet();
	}

	/**
	 * Construye un dominio difuso hesitant.
	 * 
	 * @return Un dominio formado por un conjunto difuso hesitant.
	 */
	public HesitantFuzzySet buildHesitantFuzzySetDomain() {
		return new HesitantFuzzySet();
	}

	/**
	 * Construye un FuzzySet con las etiquetas indicadas.
	 * 
	 * @param labels
	 *            Nombres de las etiquetas.
	 * 
	 * @return FuzzySet con las etiquetas indicadas.
	 * 
	 * @throws IllegalArgumentException
	 *             Si alguna etiqueta es nula.
	 */
	public FuzzySet buildFuzzySetDomain(String[] labels) {

		// Controlamos etiquetas nulas
		if (labels == null) {
			return null;
		}

		// Controlamos etiquetas vacías
		if (labels.length == 0) {
			return null;
		}

		FuzzySet result = new FuzzySet();
		mcdacw.valuation.domain.fuzzyset.Activator activator = mcdacw.valuation.domain.fuzzyset.Activator
				.getActivator();
		mcdacw.valuation.domain.fuzzyset.LabelFactory labelFactory = activator
				.getLabelFactory();
		Label label;

		int numLabels = labels.length;
		double increment = 1d / (numLabels - 1);

		if (numLabels == 1) {
			label = labelFactory.buildTrapezoidalLabel(labels[0], new double[] {
					0, 0, 1, 1 });
			result.addLabel(label);

		} else {
			double lower;
			double central;
			double upper;
			for (int i = 0; i < numLabels; i++) {
				lower = (i == 0) ? 0 : increment * (i - 1);
				central = increment * i;
				upper = (i == (numLabels - 1)) ? 1 : increment * (i + 1);

				lower = roundTo(lower, 5);
				central = roundTo(central, 5);
				upper = roundTo(upper, 5);

				label = labelFactory.buildTrapezoidalLabel(labels[i],
						new double[] { lower, central, upper });

				result.addLabel(label);
			}
		}

		return result;

	}

	/**
	 * Construye un FuzzySet no balanceado con las características indicadas.
	 * 
	 * @param labels
	 *            Nombres de las etiquetas.
	 * @param sl
	 *            Cardinalidad a la izquierda.
	 * @param sr
	 *            Cardinalidad a la derecha.
	 * @param sldensity
	 *            Densidad a la izquierda.
	 *            <p>
	 *            0 Extreme, 1 Center
	 * @param srdensity
	 *            Densidad a la derecha.
	 *            <p>
	 *            0 Extreme, 1 Center.
	 * @param initialDomain
	 *            Dominio inicial de la jerarquía empleada
	 * 
	 * @return FuzzySet con las características indicadas.
	 * 
	 * @throws IllegalArgumentException
	 *             Si alguna etiqueta es nula o
	 *             <p>
	 *             Si sl o sr son negativos o
	 *             <p>
	 *             si la cardinalidad es par o
	 *             <p>
	 *             si sl y sr no concuerda con labels o
	 *             <p>
	 *             si no se puede representar el fuzzy set con la dominio
	 *             inicial especificado.
	 */
	public FuzzySet buildUnbalancedDomain(String[] labels, int sr, int sl,
			int sldensity, int srdensity, int initialDomain) {

		// Controlamos etiquetas nulas
		if (labels == null) {
			return null;
		}

		// Controlamos etiquetas vacías
		if (labels.length == 0) {
			return null;
		}

		// Controlamos cardinalidad inválida
		if (sl <= 0) {
			throw new IllegalArgumentException("Invalid sl");
		}

		if (sr <= 0) {
			throw new IllegalArgumentException("Invalid sr");
		}

		if ((sl + sr + 1) != labels.length) {
			throw new IllegalArgumentException("Invalid cardinality");
		}

		if ((labels.length % 2) == 0) {
			throw new IllegalArgumentException("Pair cardinality");
		}

		// Controlamos densidades inválidas
		if ((sldensity < 0) || (sldensity > 1)) {
			throw new IllegalArgumentException("Invalid sl density");
		}

		if ((srdensity < 0) || (srdensity > 1)) {
			throw new IllegalArgumentException("Invalid sr density");
		}

		// Controlamos dominio incial
		if ((initialDomain % 2) == 0) {
			throw new IllegalArgumentException("Pair initial domain");
		}

		if (initialDomain < 3) {
			throw new IllegalArgumentException("Invalid initial domain");
		}

		int aux = (initialDomain - 1) / 2;
		if ((aux > sl) || (aux > sr)) {
			throw new IllegalArgumentException("Invalid initial domain");
		}

		FuzzySet result = new FuzzySet();
		mcdacw.valuation.domain.fuzzyset.Activator activator = mcdacw.valuation.domain.fuzzyset.Activator
				.getActivator();
		mcdacw.valuation.domain.fuzzyset.LabelFactory labelFactory = activator
				.getLabelFactory();
		Label leftCenterLabel;
		Label rightCenterLabel;
		Label centerLabel;

		UnbalancedInfo unbalancedInfo = new UnbalancedInfo(labels.length);
		unbalancedInfo.setSl(sl);
		unbalancedInfo.setSr(sr);
		unbalancedInfo.setSlDensity(sldensity);
		unbalancedInfo.setSrDensity(srdensity);
		
		List<Integer> lh = new LinkedList<Integer>();
		lh.add(initialDomain);

		int Lab_t;
		int Lab_t1;
		boolean directly;
		int sideCardinality = ((initialDomain - 1) / 2);

		// Representación a la izquierda
		directly = false;
		aux = sideCardinality;
		while (aux < sl) {
			aux *= 2;
			lh.add((aux * 2) + 1);
		}

		if (aux == sl) {
			directly = true;
		}

		// Construimos el dominio completo y nos quedamos con las etiquetas que
		// nos interesan
		if (directly) {
			int leftCardinality = (sl * 2) + 1;
			String leftLabels[] = new String[leftCardinality];

			for (int i = 0; i < leftCardinality; i++) {
				if (i < sl) {
					leftLabels[i] = labels[i];
				} else {
					leftLabels[i] = "dirty label " + i;
				}
			}
			FuzzySet left = buildFuzzySetDomain(leftLabels);
			for (int i = 0; i < sl; i++) {
				unbalancedInfo.setLabelInDomain(i, leftCardinality, i);
				result.addLabel(left.getLabel(i));
			}

			unbalancedInfo.setLabelInDomain(sl, leftCardinality, sl);
			leftCenterLabel = left.getLabel(sl);
		} else {
			Label leftBrid;
			Label rightBrid;
			Label brid;
			Lab_t = aux - sl;
			Lab_t1 = sl - Lab_t;
			int sleCardinality, slcCardinality;
			String sleLabels[], slcLabels[];
			FuzzySet sleFuzzySet, slcFuzzySet;

			if (sldensity == 0) {
				sleCardinality = (aux * 2) + 1;
				sleLabels = new String[sleCardinality];

				for (int i = 0; i < sleCardinality; i++) {
					if (i < Lab_t1) {
						sleLabels[i] = labels[i];
					} else {
						sleLabels[i] = "dirty label " + i;
					}
				}
				sleFuzzySet = buildFuzzySetDomain(sleLabels);
				for (int i = 0; i < Lab_t1; i++) {
					unbalancedInfo.setLabelInDomain(i, sleCardinality, i);
					result.addLabel(sleFuzzySet.getLabel(i));
				}

				unbalancedInfo.setLabelInDomain(Lab_t1, sleCardinality, Lab_t1);
				leftBrid = sleFuzzySet.getLabel(Lab_t1);

				slcCardinality = aux + 1;
				slcLabels = new String[slcCardinality];

				int alreadyUsed = Lab_t1 / 2;
				int j = 0;
				for (int i = 0; i < slcCardinality; i++) {
					if ((i >= alreadyUsed) && (i < (Lab_t + alreadyUsed))) {
						slcLabels[i] = labels[Lab_t1 + j++];
					} else {
						slcLabels[i] = "dirty label " + i;
					}
				}
				slcFuzzySet = buildFuzzySetDomain(slcLabels);
				for (int i = (alreadyUsed + 1); i < (Lab_t + alreadyUsed); i++) {
					unbalancedInfo.setLabelInDomain(result.cardinality() + 1, slcCardinality, i);
					result.addLabel(slcFuzzySet.getLabel(i));
				}

				unbalancedInfo.setLabelInDomain(Lab_t1, slcCardinality, alreadyUsed);
				rightBrid = slcFuzzySet.getLabel(alreadyUsed);

				double a = leftBrid.getSemantic().getCoverage().getMin();
				double b = rightBrid.getSemantic().getCenter().getMin();
				double d = rightBrid.getSemantic().getCoverage().getMax();
				brid = labelFactory.buildTrapezoidalLabel(rightBrid.getName(),
						new double[] { a, b, b, d });
				result.addLabel(Lab_t1, brid);

				unbalancedInfo.setLabelInDomain(sl, slcCardinality, Lab_t + alreadyUsed);
				leftCenterLabel = slcFuzzySet.getLabel(Lab_t + alreadyUsed);

			} else {
				sleCardinality = aux + 1;
				sleLabels = new String[sleCardinality];

				for (int i = 0; i < sleCardinality; i++) {
					if (i < Lab_t) {
						sleLabels[i] = labels[i];
					} else {
						sleLabels[i] = "dirty label " + i;
					}
				}

				sleFuzzySet = buildFuzzySetDomain(sleLabels);
				for (int i = 0; i < Lab_t; i++) {
					unbalancedInfo.setLabelInDomain(i, sleCardinality, i);
					result.addLabel(sleFuzzySet.getLabel(i));
				}

				unbalancedInfo.setLabelInDomain(Lab_t, sleCardinality, Lab_t);
				leftBrid = sleFuzzySet.getLabel(Lab_t);

				slcCardinality = (aux * 2) + 1;
				slcLabels = new String[slcCardinality];

				int alreadyUsed = Lab_t * 2;
				int j = 0;
				for (int i = 0; i < slcCardinality; i++) {
					if ((i >= alreadyUsed) && (i < (Lab_t1 + alreadyUsed))) {
						slcLabels[i] = labels[Lab_t + j++];
					} else {
						slcLabels[i] = "dirty label " + i;
					}
				}
				slcFuzzySet = buildFuzzySetDomain(slcLabels);
				for (int i = (alreadyUsed + 1); i < (Lab_t1 + alreadyUsed); i++) {
					unbalancedInfo.setLabelInDomain(result.cardinality() + 1, slcCardinality, i);
					result.addLabel(slcFuzzySet.getLabel(i));
				}

				unbalancedInfo.setLabelInDomain(Lab_t, slcCardinality, alreadyUsed);
				rightBrid = slcFuzzySet.getLabel(alreadyUsed);

				double a = leftBrid.getSemantic().getCoverage().getMin();
				double b = rightBrid.getSemantic().getCenter().getMin();
				double d = rightBrid.getSemantic().getCoverage().getMax();
				brid = labelFactory.buildTrapezoidalLabel(rightBrid.getName(),
						new double[] { a, b, b, d });
				result.addLabel(Lab_t, brid);

				unbalancedInfo.setLabelInDomain(sl, slcCardinality, Lab_t1 + alreadyUsed);
				leftCenterLabel = slcFuzzySet.getLabel(Lab_t1 + alreadyUsed);
			}
		}

		// Representación a la derecha
		directly = false;
		aux = sideCardinality;
		int bigger = lh.get(lh.size() - 1);
		while (aux < sr) {
			aux *= 2;
			if (((aux * 2) + 1) > bigger) {
				lh.add((aux * 2) + 1);
			}
		}

		if (aux == sr) {
			directly = true;
		}

		// Construimos el dominio completo y nos quedamos con las etiquetas que
		// nos interesan
		if (directly) {
			int rightCardinality = (sr * 2) + 1;
			String rightLabels[] = new String[rightCardinality];

			for (int i = 0; i < rightCardinality; i++) {
				if (i >= (rightCardinality - sr)) {
					rightLabels[i] = labels[labels.length
							- (rightCardinality - i)];
				} else {
					rightLabels[i] = "dirty label " + i;
				}
			}
			FuzzySet right = buildFuzzySetDomain(rightLabels);
			for (int i = (rightCardinality - sr); i < rightCardinality; i++) {
				unbalancedInfo.setLabelInDomain(result.cardinality() + 1, rightCardinality, i);
				result.addLabel(right.getLabel(i));
			}

			rightCenterLabel = right.getLabel(rightCardinality - sr - 1);

			double a = leftCenterLabel.getSemantic().getCoverage().getMin();
			double b = rightCenterLabel.getSemantic().getCenter().getMin();
			double d = rightCenterLabel.getSemantic().getCoverage().getMax();
			centerLabel = labelFactory.buildTrapezoidalLabel(labels[sl],
					new double[] { a, b, b, d });
			unbalancedInfo.setLabelInDomain(sl, rightCardinality, rightCardinality - sr - 1);
			result.addLabel(sl, centerLabel);

		} else {
			Label leftBrid;
			Label rightBrid;
			Label brid;
			Lab_t = aux - sr;
			Lab_t1 = sr - Lab_t;
			int sreCardinality, srcCardinality;
			String sreLabels[], srcLabels[];
			FuzzySet sreFuzzySet, srcFuzzySet;

			if (srdensity == 0) {
				srcCardinality = aux + 1;
				srcLabels = new String[srcCardinality];

				int j = 0;
				for (int i = 0; i < srcCardinality; i++) {
					if ((i > (aux / 2)) && (i <= ((aux / 2) + Lab_t))) {
						srcLabels[i] = labels[sl + 1 + j++];
					} else {
						srcLabels[i] = "dirty label " + i;
					}
				}

				srcFuzzySet = buildFuzzySetDomain(srcLabels);
				for (int i = ((aux / 2) + 1); i < ((aux / 2) + Lab_t); i++) {
					unbalancedInfo.setLabelInDomain(result.cardinality() + 1, srcCardinality, i);
					result.addLabel(srcFuzzySet.getLabel(i));
				}
				unbalancedInfo.setLabelInDomain(result.cardinality() + 1, srcCardinality, (aux / 2) + Lab_t);
				leftBrid = srcFuzzySet.getLabel((aux / 2) + Lab_t);

				rightCenterLabel = srcFuzzySet.getLabel(aux / 2);

				double a = leftCenterLabel.getSemantic().getCoverage().getMin();
				double b = rightCenterLabel.getSemantic().getCenter().getMin();
				double d = rightCenterLabel.getSemantic().getCoverage()
						.getMax();
				centerLabel = labelFactory.buildTrapezoidalLabel(labels[sl],
						new double[] { a, b, b, d });
				
				unbalancedInfo.setLabelInDomain(sl, srcCardinality, aux / 2);
				result.addLabel(sl, centerLabel);

				sreCardinality = (aux * 2) + 1;
				sreLabels = new String[sreCardinality];

				for (int i = 0; i < sreCardinality; i++) {
					if (i >= (sreCardinality - Lab_t1)) {
						sreLabels[i] = labels[labels.length
								- (sreCardinality - i)];
					} else {
						sreLabels[i] = "dirty label " + i;
					}
				}

				sreFuzzySet = buildFuzzySetDomain(sreLabels);
				for (int i = (sreCardinality - Lab_t1); i < sreCardinality; i++) {
					unbalancedInfo.setLabelInDomain(result.cardinality() + 1, sreCardinality, i);
					result.addLabel(sreFuzzySet.getLabel(i));
				}

				unbalancedInfo.setLabelInDomain(sl + Lab_t, sreCardinality, sreCardinality - Lab_t1 - 1);
				rightBrid = sreFuzzySet.getLabel(sreCardinality - Lab_t1 - 1);

				a = leftBrid.getSemantic().getCoverage().getMin();
				b = rightBrid.getSemantic().getCenter().getMin();
				d = rightBrid.getSemantic().getCoverage().getMax();
				brid = labelFactory.buildTrapezoidalLabel(leftBrid.getName(),
						new double[] { a, b, b, d });
				result.addLabel(sl + Lab_t, brid);

			} else {
				srcCardinality = (aux * 2) + 1;
				srcLabels = new String[srcCardinality];

				int j = 0;
				for (int i = 0; i < srcCardinality; i++) {
					if ((i > aux) && i < (aux + Lab_t1)) {
						srcLabels[i] = labels[sl + 1 + j++];
					} else {
						srcLabels[i] = "dirty label " + i;
					}
				}

				srcFuzzySet = buildFuzzySetDomain(srcLabels);
				for (int i = (aux + 1); i < (aux + Lab_t1); i++) {
					unbalancedInfo.setLabelInDomain(result.cardinality() + 1, srcCardinality, i);
					result.addLabel(srcFuzzySet.getLabel(i));
				}

				leftBrid = srcFuzzySet.getLabel(aux + Lab_t1);

				rightCenterLabel = srcFuzzySet.getLabel(aux);

				double a = leftCenterLabel.getSemantic().getCoverage().getMin();
				double b = rightCenterLabel.getSemantic().getCenter().getMin();
				double d = rightCenterLabel.getSemantic().getCoverage()
						.getMax();
				centerLabel = labelFactory.buildTrapezoidalLabel(labels[sl],
						new double[] { a, b, b, d });
				unbalancedInfo.setLabelInDomain(sl, srcCardinality, aux);
				result.addLabel(sl, centerLabel);

				sreCardinality = aux + 1;
				sreLabels = new String[sreCardinality];

				for (int i = 0; i < sreCardinality; i++) {
					if (i >= (sreCardinality - Lab_t - 1)) {
						sreLabels[i] = labels[labels.length
								- (sreCardinality - i)];
					} else {
						sreLabels[i] = "dirty label " + i;
					}
				}

				sreFuzzySet = buildFuzzySetDomain(sreLabels);

				for (int i = (sreCardinality - Lab_t); i < sreCardinality; i++) {
					unbalancedInfo.setLabelInDomain(result.cardinality() + 1, sreCardinality, i);
					result.addLabel(sreFuzzySet.getLabel(i));
				}

				unbalancedInfo.setLabelInDomain(sl + Lab_t1, srcCardinality, aux + Lab_t1);
				unbalancedInfo.setLabelInDomain(sl + Lab_t1, sreCardinality, sreCardinality - Lab_t - 1);
				rightBrid = sreFuzzySet.getLabel(sreCardinality - Lab_t - 1);
				a = leftBrid.getSemantic().getCoverage().getMin();
				b = rightBrid.getSemantic().getCenter().getMin();
				d = rightBrid.getSemantic().getCoverage().getMax();

				brid = labelFactory.buildTrapezoidalLabel(rightBrid.getName(),
						new double[] { a, b, b, d });

				result.addLabel(sl + Lab_t1, brid);
			}
		}
		
		int lhArray[] = new int[lh.size()];
		for (int i = 0; i < lhArray.length; i++) {
			lhArray[i] = lh.get(i);
		}
		
		unbalancedInfo.setLH(lhArray);
		result.setUnbalancedInfo(unbalancedInfo);
		return result;

	}

	/**
	 * Construye un HesitantFuzzySet con las etiquetas indicadas.
	 * 
	 * @param labels
	 *            Nombres de las etiquetas.
	 * 
	 * @return HesitantFuzzySet con las etiquetas indicadas.
	 * 
	 * @throws IllegalArgumentException
	 *             Si alguna etiqueta es nula.
	 */
	public HesitantFuzzySet buildHesitantFuzzySetDomain(String[] labels) {

		// Controlamos etiquetas nulas
		if (labels == null) {
			return null;
		}

		// Controlamos etiquetas vacías
		if (labels.length == 0) {
			return null;
		}

		HesitantFuzzySet result = new HesitantFuzzySet();
		mcdacw.valuation.domain.fuzzyset.Activator activator = mcdacw.valuation.domain.fuzzyset.Activator
				.getActivator();
		mcdacw.valuation.domain.fuzzyset.LabelFactory labelFactory = activator
				.getLabelFactory();
		Label label;

		int numLabels = labels.length;
		double increment = 1d / (numLabels - 1);

		if (numLabels == 1) {
			label = labelFactory.buildTrapezoidalLabel(labels[0], new double[] {
					0, 0, 1, 1 });
			result.addLabel(label);

		} else {
			double lower;
			double central;
			double upper;
			for (int i = 0; i < numLabels; i++) {
				lower = (i == 0) ? 0 : increment * (i - 1);
				central = increment * i;
				upper = (i == (numLabels - 1)) ? 1 : increment * (i + 1);

				lower = roundTo(lower, 5);
				central = roundTo(central, 5);
				upper = roundTo(upper, 5);

				label = labelFactory.buildTrapezoidalLabel(labels[i],
						new double[] { lower, central, upper });

				result.addLabel(label);
			}
		}

		return result;

	}

	/**
	 * Redondea un valor al número de decimales indicado.
	 * 
	 * @param value
	 *            Valor a redondear.
	 * @param decimals
	 *            Número de decimales.
	 * @return Valor redondeado.
	 */
	private double roundTo(double value, int decimals) {
		long factor = (long) Math.pow(10, decimals);
		double aux;
		double result;
		if (value == 0) {
			return 0;
		} else {
			aux = (int) (value * factor);
			result = (aux / factor);
			aux = ((value - result) * factor);
			if (aux > 0.5) {
				aux = 1d / factor;
				result += aux;
			}
			if ((decimals + 2) < (Double.toString(result).length())) {
				return roundTo(result + (0.1d / factor), decimals);
			} else {
				return result;
			}
		}
	}

	/**
	 * Construye un dominio numérico.
	 * 
	 * @return Un dominio numérico.
	 */
	public NumericDomain buildNumericDomain() {
		return new NumericDomain();
	}

	/**
	 * Construye un dominio numérico intervalar.
	 * 
	 * @return Un dominio numérico intervalar.
	 */
	public IntervalNumericDomain buildIntervalNumericDomain() {
		return new IntervalNumericDomain();
	}

}
