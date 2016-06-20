package mcdacw.valuation.domain.fuzzyset;

import java.util.HashMap;
import java.util.Map;

public class UnbalancedInfo {

	/**
	 * Jerarquía empleada
	 */
	protected int[] _lh;

	/**
	 * Cardinalidad de sl
	 */
	protected int _sl;

	/**
	 * Cardinalidad de sr
	 */
	protected int _sr;

	/**
	 * Densidad a la derecha
	 */
	protected int _slDensity;

	/**
	 * Densidad a la izquierda
	 */
	protected int _srDensity;

	/**
	 * Cardinalidad
	 */
	protected int _cardinality;

	/**
	 * Correspondencia de etiquetas en la jerarquía
	 */
	protected Map<Integer, Map<Integer, Integer>> _labels;

	public UnbalancedInfo(String info) {
		String[] parts = info.split(";");

		String[] lhLevels = parts[0].split(",");
		_lh = new int[lhLevels.length];
		for (int i = 0; i < lhLevels.length; i++) {
			_lh[i] = Integer.parseInt(lhLevels[i]);
		}

		String format[] = parts[1].split(",");
		_sl = Integer.parseInt(format[0]);
		_slDensity = Integer.parseInt(format[1]);
		_sr = Integer.parseInt(format[2]);
		_srDensity = Integer.parseInt(format[3]);

		_cardinality = _sl + _sr + 1;

		_labels = new HashMap<Integer, Map<Integer, Integer>>();

		String labels[] = parts[2].split(",");
		for (int i = 0; i < labels.length; i++) {
			_labels.put(i, new HashMap<Integer, Integer>());
			String[] aux = labels[i].split("=");
			for (String j : aux) {
				String[] value = j.split(":");
				_labels.get(i).put(Integer.parseInt(value[0]),
						Integer.parseInt(value[1]));
			}
		}
	}

	public UnbalancedInfo(int cardinality) {
		_cardinality = cardinality;
		_labels = new HashMap<Integer, Map<Integer, Integer>>();
	}

	public void setLH(int[] lh) {
		_lh = lh;
	}

	public void setSl(int sl) {
		_sl = sl;
	}

	public void setSr(int sr) {
		_sr = sr;
	}

	public void setSlDensity(int slDensity) {
		_slDensity = slDensity;
	}

	public void setSrDensity(int srDensity) {
		_srDensity = srDensity;
	}

	public void setLabels(Map<Integer, Map<Integer, Integer>> labels) {
		_labels = labels;
	}

	public void setCardinality(int cardinality) {
		_cardinality = cardinality;
	}

	public int[] getLH() {
		return _lh;
	}

	public int getSl() {
		return _sl;
	}

	public int getSr() {
		return _sr;
	}

	public int getSlDensity() {
		return _slDensity;
	}

	public int getSrDensity() {
		return _srDensity;
	}

	public Map<Integer, Map<Integer, Integer>> getLabels() {
		return _labels;
	}

	public int getCardinality() {
		return _cardinality;
	}

	public boolean isBrid(int pos) {
		return (_labels.get(pos).size() > 1);
	}

	public void setLabel(int pos, Map<Integer, Integer> label) {
		_labels.put(pos, label);
	}

	public Map<Integer, Integer> getLabel(int pos) {
		return _labels.get(pos);
	}

	public void setLabelInDomain(int pos, int domain, int label) {
		if (!_labels.containsKey(pos)) {
			_labels.put(pos, new HashMap<Integer, Integer>());
		}

		_labels.get(pos).put(domain, label);
	}
	
	public Integer labelPos(int domain, int label) {
		Integer aux;
		for (Integer pos : _labels.keySet()) {
			aux = getLabelInDomain(pos, domain);
			if (aux != null) {
				if (aux == label) {
					return pos;
				}
			}
		}
		
		return null;
	}

	public Integer getLabelInDomain(int pos, int domain) {
		if (_labels.containsKey(pos)) {
			return _labels.get(pos).get(domain);
		} else {
			return null;
		}
	}

	public String toString() {
		StringBuilder result = new StringBuilder("");
		for (Integer label : _labels.keySet()) {
			result.append("Label " + label + "\n");
			for (Integer domain : _labels.get(label).keySet()) {
				result.append("\tDomain: " + domain + ":"
						+ _labels.get(label).get(domain) + "\n");
			}
		}

		return result.toString();
	}

	public String getInfo() {
		StringBuilder result = new StringBuilder("");

		StringBuilder lh = new StringBuilder("");
		for (int l : _lh) {
			lh.append(l + ",");
		}
		lh.replace(lh.length() - 1, lh.length(), "");
		result.append(lh);

		StringBuilder format = new StringBuilder(";");
		format.append(_sl + "," + _slDensity + "," + _sr + "," + _srDensity);
		result.append(format);

		StringBuilder labels = new StringBuilder(";");
		for (Integer label : _labels.keySet()) {
			for (Integer domain : _labels.get(label).keySet()) {
				labels.append(domain + ":" + _labels.get(label).get(domain)
						+ "=");
			}
			labels.replace(labels.length() - 1, labels.length(), ",");
		}
		labels.replace(labels.length() - 1, labels.length(), "");
		result.append(labels);

		return result.toString();
		
	}

}
