package mcdacw.valuation.domain.fuzzyset;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mcdacw.paremetervalidator.ParameterValidator;
import mcdacw.valuation.domain.fuzzyset.function.types.TrapezoidalFunction;
import mcdacw.valuation.domain.fuzzyset.label.LabelLinguisticDomain;

public class Unbalanced extends FuzzySet {
	
	public static final String ID = "flintstones.domain.linguistic.unbalanced"; //$NON-NLS-1$
	
	private int[] _lh;
	private int _sl;
	private int _sr;
	private int _slDensity;
	private int _srDensity;
	private int _cardinality;
	private Map<Integer, Map<Integer, Integer>> _labels;
	
	public Unbalanced() {
		super();
		_labels = new HashMap<Integer, Map<Integer,Integer>>();
	}
	
	public int[] getLh() {
		return _lh;
	}

	public void setLh(int[] lh) {
		_lh = lh;
	}

	public int getSl() {
		return _sl;
	}

	public void setSl(int sl) {
		_sl = sl;
	}

	public int getSr() {
		return _sr;
	}

	public void setSr(int sr) {
		_sr = sr;
	}

	public int getSlDensity() {
		return _slDensity;
	}

	public void setSlDensity(int slDensity) {
		_slDensity = slDensity;
	}

	public int getSrDensity() {
		return _srDensity;
	}

	public void setSrDensity(int srDensity) {
		_srDensity = srDensity;
	}

	public int getCardinality() {
		return _cardinality;
	}

	public void setCardinality(int cardinality) {
		_cardinality = cardinality;
	}

	public Map<Integer, Map<Integer, Integer>> getLabels() {
		return _labels;
	}

	public void setLabels(Map<Integer, Map<Integer, Integer>> labels) {
		_labels = labels;
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
		
		if(!_labels.containsKey(pos)) {
			_labels.put(pos, new HashMap<Integer, Integer>());
		}
		
		_labels.get(pos).put(domain, label);
		
	}
	
	public Integer labelPos(int domain, int label) {
		Integer aux;
		for(int pos: _labels.keySet()) {
			aux = getLabelInDomain(pos, domain);
			if(aux != null) {
				if(aux == label) {
					return pos;
				}
			}
		}
		
		return null;
	}
	
	public Integer getLabelInDomain(int pos, int domain) {
		
		if(_labels.containsKey(pos)) {
			return _labels.get(pos).get(domain);
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		String result = ""; //$NON-NLS-1$
		
		for(int label: _labels.keySet()) {
			result += "Label " + label + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
			for(int domain: _labels.get(label).keySet()) {
				result += "\tDomain: " + domain + ":" + _labels.get(label).get(domain) + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}
		
		return result;
	}
	
	public String getInfo() {
		String result = ""; //$NON-NLS-1$
		
		result = loadStringLh(result);
		result = loadStringFormat(result);
		return loadStringLabels(result);
	}
	
	public void createUnbalancedDomain(String[] labels, int sr, int sl, int sldensity, int srdensity, int initialDomain) {
		ParameterValidator.notNull(labels, "labels");
		ParameterValidator.notEmpty(labels, "labels");
		
		if(ParameterValidator.isNegative(sr)) {
			throw new IllegalArgumentException("Unbalanced invalid sr");
		}
		
		if(ParameterValidator.isNegative(sl)) {
			throw new IllegalArgumentException("Unbalanced invalid sl");
		}
		
		if(!ParameterValidator.isSameElement(sl + sr + 1, labels.length)) {
			throw new IllegalArgumentException("Unbalanced invalid cardinality");
		}
		
		if((labels.length % 2) == 0) {
			throw new IllegalArgumentException("Unbalanced pair cardinality");
		}
		
		if(!ParameterValidator.inRange(sldensity, 0, 1)) {
			throw new IllegalArgumentException("Unbalanced invalid sl density");
		}
		
		if(!ParameterValidator.inRange(srdensity, 0, 1)) {
			throw new IllegalArgumentException("Unbalanced invalid sr density");
		}
		
		if((initialDomain % 2) == 0) {
			throw new IllegalArgumentException("Unbalanced pair initial domain");
		}
		
		if(initialDomain < 3) {
			throw new IllegalArgumentException("Unbalanced invalid initial domain");
		}
		
		int aux = (initialDomain - 1) / 2;
		if((aux > sl) || (aux > sr)) {
			throw new IllegalArgumentException("Unbalanced invalid initial domain");
		}
		
		LabelLinguisticDomain leftCenterLabel, rightCenterLabel, centerLabel;

		setSl(sl);
		setSr(sr);
		setSlDensity(sldensity);
		setSrDensity(srdensity);
		setCardinality(labels.length);
		
		_labels = new HashMap<Integer, Map<Integer,Integer>>();
		
		clearFuzzySet();
		
		List<Integer> lh = new LinkedList<Integer>();
		lh.add(initialDomain);
		
		int Lab_t;
		int Lab_t1;
		boolean directly;
		int sideCardinality = ((initialDomain - 1) / 2);

		directly = false;
		aux = sideCardinality;
		while(aux < sl) {
			aux *= 2;
			lh.add((aux * 2) + 1);
		}

		if (aux == sl) {
			directly = true;
		}

		if(directly) {
			int leftCardinality = (sl * 2) + 1;
			String leftLabels[] = new String[leftCardinality];

			for(int i = 0; i < leftCardinality; i++) {
				if(i < sl) {
					leftLabels[i] = labels[i];
				} else {
					leftLabels[i] = "dirty label " + i; //$NON-NLS-1$
				}
			}
			FuzzySet left = new FuzzySet();
			left.createTrapezoidalFunction(leftLabels);
			for(int i = 0; i < sl; i++) {
				setLabelInDomain(i, leftCardinality, i);
				addLabel(left.getLabelSet().getLabel(i));
			}

			setLabelInDomain(sl, leftCardinality, sl);
			leftCenterLabel = left.getLabelSet().getLabel(sl);
		} else {
			LabelLinguisticDomain leftBrid, rightBrid, brid;
			Lab_t = aux - sl;
			Lab_t1 = sl - Lab_t;
			int sleCardinality, slcCardinality;
			String sleLabels[], slcLabels[];
			FuzzySet sleFuzzySet, slcFuzzySet;

			if(sldensity == 0) {
				sleCardinality = (aux * 2) + 1;
				sleLabels = new String[sleCardinality];

				for(int i = 0; i < sleCardinality; i++) {
					if(i < Lab_t1) {
						sleLabels[i] = labels[i];
					} else {
						sleLabels[i] = "dirty label " + i; //$NON-NLS-1$
					}
				}
				sleFuzzySet = new FuzzySet(); 
				sleFuzzySet.createTrapezoidalFunction(sleLabels);
				for(int i = 0; i < Lab_t1; i++) {
					setLabelInDomain(i, sleCardinality, i);
					addLabel(sleFuzzySet.getLabelSet().getLabel(i));
				}

				setLabelInDomain(Lab_t1, sleCardinality, Lab_t1);
				leftBrid = sleFuzzySet.getLabelSet().getLabel(Lab_t1);

				slcCardinality = aux + 1;
				slcLabels = new String[slcCardinality];

				int alreadyUsed = Lab_t1 / 2;
				int j = 0;
				for(int i = 0; i < slcCardinality; i++) {
					if((i >= alreadyUsed) && (i < (Lab_t + alreadyUsed))) {
						slcLabels[i] = labels[Lab_t1 + j++];
					} else {
						slcLabels[i] = "dirty label " + i; //$NON-NLS-1$
					}
				}
				slcFuzzySet = new FuzzySet(); 
				slcFuzzySet.createTrapezoidalFunction(slcLabels);
				for(int i = (alreadyUsed + 1); i < (Lab_t + alreadyUsed); i++) {
					setLabelInDomain(getLabelSet().getCardinality() + 1, slcCardinality, i);
					addLabel(slcFuzzySet.getLabelSet().getLabel(i));
				}

				setLabelInDomain(Lab_t1, slcCardinality, alreadyUsed);
				rightBrid = slcFuzzySet.getLabelSet().getLabel(alreadyUsed);

				double a = leftBrid.getSemantic().getCoverage().getMin();
				double b = rightBrid.getSemantic().getCenter().getMin();
				double d = rightBrid.getSemantic().getCoverage().getMax();
				double limits[] = {a, b, b, d};
				TrapezoidalFunction semantic = new TrapezoidalFunction(limits);
				brid = new LabelLinguisticDomain(rightBrid.getName(), semantic);
				addLabel(Lab_t1, brid);

				setLabelInDomain(sl, slcCardinality, Lab_t + alreadyUsed);
				leftCenterLabel = slcFuzzySet.getLabelSet().getLabel(Lab_t + alreadyUsed);

			} else {
				sleCardinality = aux + 1;
				sleLabels = new String[sleCardinality];

				for(int i = 0; i < sleCardinality; i++) {
					if(i < Lab_t) {
						sleLabels[i] = labels[i];
					} else {
						sleLabels[i] = "dirty label " + i; //$NON-NLS-1$
					}
				}

				sleFuzzySet = new FuzzySet();
				sleFuzzySet.createTrapezoidalFunction(sleLabels);
				for(int i = 0; i < Lab_t; i++) {
					setLabelInDomain(i, sleCardinality, i);
					addLabel(sleFuzzySet.getLabelSet().getLabel(i));
				}

				setLabelInDomain(Lab_t, sleCardinality, Lab_t);
				leftBrid = sleFuzzySet.getLabelSet().getLabel(Lab_t);

				slcCardinality = (aux * 2) + 1;
				slcLabels = new String[slcCardinality];

				int alreadyUsed = Lab_t * 2;
				int j = 0;
				for(int i = 0; i < slcCardinality; i++) {
					if((i >= alreadyUsed) && (i < (Lab_t1 + alreadyUsed))) {
						slcLabels[i] = labels[Lab_t + j++];
					} else {
						slcLabels[i] = "dirty label " + i; //$NON-NLS-1$
					}
				}
				slcFuzzySet = new FuzzySet();
				slcFuzzySet.createTrapezoidalFunction(slcLabels);
				for(int i = (alreadyUsed + 1); i < (Lab_t1 + alreadyUsed); i++) {
					setLabelInDomain(getLabelSet().getCardinality() + 1, slcCardinality, i);
					addLabel(slcFuzzySet.getLabelSet().getLabel(i));
				}

				setLabelInDomain(Lab_t, slcCardinality, alreadyUsed);
				rightBrid = slcFuzzySet.getLabelSet().getLabel(alreadyUsed);

				double a = leftBrid.getSemantic().getCoverage().getMin();
				double b = rightBrid.getSemantic().getCenter().getMin();
				double d = rightBrid.getSemantic().getCoverage().getMax();
				double limits[] = {a, b, b, d};
				TrapezoidalFunction semantic = new TrapezoidalFunction(limits);
				brid = new LabelLinguisticDomain(rightBrid.getName(), semantic);
				addLabel(Lab_t, brid);

				setLabelInDomain(sl, slcCardinality, Lab_t1 + alreadyUsed);
				leftCenterLabel = slcFuzzySet.getLabelSet().getLabel(Lab_t1 + alreadyUsed);
			}
		}
		
		directly = false;
		aux = sideCardinality;
		int bigger = lh.get(lh.size() - 1);
		while(aux < sr) {
			aux *= 2;
			if (((aux * 2) + 1) > bigger) {
				lh.add((aux * 2) + 1);
			}
		}

		if(aux == sr) {
			directly = true;
		}

		if(directly) {
			int rightCardinality = (sr * 2) + 1;
			String rightLabels[] = new String[rightCardinality];

			for(int i = 0; i < rightCardinality; i++) {
				if(i >= (rightCardinality - sr)) {
					rightLabels[i] = labels[labels.length - (rightCardinality - i)];
				} else {
					rightLabels[i] = "dirty label " + i; //$NON-NLS-1$
				}
			}
			FuzzySet right = new FuzzySet();
			right.createTrapezoidalFunction(rightLabels);
			for(int i = (rightCardinality - sr); i < rightCardinality; i++) {
				setLabelInDomain(getLabelSet().getCardinality() + 1, rightCardinality, i);
				addLabel(right.getLabelSet().getLabel(i));
			}

			rightCenterLabel = right.getLabelSet().getLabel(rightCardinality - sr - 1);

			double a = leftCenterLabel.getSemantic().getCoverage().getMin();
			double b = rightCenterLabel.getSemantic().getCenter().getMin();
			double d = rightCenterLabel.getSemantic().getCoverage().getMax();
			double limits[] = { a, b, b, d };
			TrapezoidalFunction semantic = new TrapezoidalFunction(limits);
			centerLabel = new LabelLinguisticDomain(labels[sl], semantic);
			setLabelInDomain(sl, rightCardinality, rightCardinality - sr - 1);
			addLabel(sl, centerLabel);

		} else {
			LabelLinguisticDomain leftBrid, rightBrid, brid;
			Lab_t = aux - sr;
			Lab_t1 = sr - Lab_t;
			int sreCardinality, srcCardinality;
			String sreLabels[], srcLabels[];
			FuzzySet sreFuzzySet, srcFuzzySet;

			if(srdensity == 0) {
				srcCardinality = aux + 1;
				srcLabels = new String[srcCardinality];

				int j = 0;
				for(int i = 0; i < srcCardinality; i++) {
					if((i > (aux / 2)) && (i <= ((aux / 2) + Lab_t))) {
						srcLabels[i] = labels[sl + 1 + j++];
					} else {
						srcLabels[i] = "dirty label " + i; //$NON-NLS-1$
					}
				}

				srcFuzzySet = new FuzzySet();
				srcFuzzySet.createTrapezoidalFunction(srcLabels);
				for(int i = ((aux / 2) + 1); i < ((aux / 2) + Lab_t); i++) {
					setLabelInDomain(getLabelSet().getCardinality() + 1, srcCardinality, i);
					addLabel(srcFuzzySet.getLabelSet().getLabel(i));
				}
				setLabelInDomain(getLabelSet().getCardinality() + 1, srcCardinality, (aux / 2) + Lab_t);
				leftBrid = srcFuzzySet.getLabelSet().getLabel((aux / 2) + Lab_t);

				rightCenterLabel = srcFuzzySet.getLabelSet().getLabel(aux / 2);

				double a = leftCenterLabel.getSemantic().getCoverage().getMin();
				double b = rightCenterLabel.getSemantic().getCenter().getMin();
				double d = rightCenterLabel.getSemantic().getCoverage().getMax();
				double limits[] = { a, b, b, d };
				TrapezoidalFunction semantic = new TrapezoidalFunction(limits);
				centerLabel = new LabelLinguisticDomain(labels[sl], semantic);
				
				setLabelInDomain(sl, srcCardinality, aux / 2);
				addLabel(sl, centerLabel);

				sreCardinality = (aux * 2) + 1;
				sreLabels = new String[sreCardinality];

				for(int i = 0; i < sreCardinality; i++) {
					if(i >= (sreCardinality - Lab_t1)) {
						sreLabels[i] = labels[labels.length - (sreCardinality - i)];
					} else {
						sreLabels[i] = "dirty label " + i; //$NON-NLS-1$
					}
				}

				sreFuzzySet = new FuzzySet();
				sreFuzzySet.createTrapezoidalFunction(sreLabels);
				for(int i = (sreCardinality - Lab_t1); i < sreCardinality; i++) {
					setLabelInDomain(getLabelSet().getCardinality() + 1, sreCardinality, i);
					addLabel(sreFuzzySet.getLabelSet().getLabel(i));
				}

				setLabelInDomain(sl + Lab_t, sreCardinality, sreCardinality - Lab_t1 - 1);
				rightBrid = sreFuzzySet.getLabelSet().getLabel(sreCardinality - Lab_t1 - 1);

				a = leftBrid.getSemantic().getCoverage().getMin();
				b = rightBrid.getSemantic().getCenter().getMin();
				d = rightBrid.getSemantic().getCoverage().getMax();
				double limits1[] = { a, b, b, d };
				TrapezoidalFunction semantic1 = new TrapezoidalFunction(limits1);
				brid = new LabelLinguisticDomain(leftBrid.getName(), semantic1);
				addLabel(sl + Lab_t, brid);

			} else {
				srcCardinality = (aux * 2) + 1;
				srcLabels = new String[srcCardinality];

				int j = 0;
				for(int i = 0; i < srcCardinality; i++) {
					if((i > aux) && i < (aux + Lab_t1)) {
						srcLabels[i] = labels[sl + 1 + j++];
					} else {
						srcLabels[i] = "dirty label " + i; //$NON-NLS-1$
					}
				}

				srcFuzzySet = new FuzzySet();
				srcFuzzySet.createTrapezoidalFunction(srcLabels);
				for(int i = (aux + 1); i < (aux + Lab_t1); i++) {
					setLabelInDomain(getLabelSet().getCardinality() + 1, srcCardinality, i);
					addLabel(srcFuzzySet.getLabelSet().getLabel(i));
				}

				leftBrid = srcFuzzySet.getLabelSet().getLabel(aux + Lab_t1);

				rightCenterLabel = srcFuzzySet.getLabelSet().getLabel(aux);

				double a = leftCenterLabel.getSemantic().getCoverage().getMin();
				double b = rightCenterLabel.getSemantic().getCenter().getMin();
				double d = rightCenterLabel.getSemantic().getCoverage().getMax();
				double limits[] = { a, b, b, d };
				TrapezoidalFunction semantic = new TrapezoidalFunction(limits);
				centerLabel = new LabelLinguisticDomain(labels[sl], semantic);
				setLabelInDomain(sl, srcCardinality, aux);
				addLabel(sl, centerLabel);

				sreCardinality = aux + 1;
				sreLabels = new String[sreCardinality];

				for(int i = 0; i < sreCardinality; i++) {
					if(i >= (sreCardinality - Lab_t - 1)) {
						sreLabels[i] = labels[labels.length - (sreCardinality - i)];
					} else {
						sreLabels[i] = "dirty label " + i; //$NON-NLS-1$
					}
				}

				sreFuzzySet = new FuzzySet();
				sreFuzzySet.createTrapezoidalFunction(sreLabels);

				for(int i = (sreCardinality - Lab_t); i < sreCardinality; i++) {
					setLabelInDomain(getLabelSet().getCardinality() + 1, sreCardinality, i);
					addLabel(sreFuzzySet.getLabelSet().getLabel(i));
				}

				setLabelInDomain(sl + Lab_t1, srcCardinality, aux + Lab_t1);
				setLabelInDomain(sl + Lab_t1, sreCardinality, sreCardinality - Lab_t - 1);
				rightBrid = sreFuzzySet.getLabelSet().getLabel(sreCardinality - Lab_t - 1);
				a = leftBrid.getSemantic().getCoverage().getMin();
				b = rightBrid.getSemantic().getCenter().getMin();
				d = rightBrid.getSemantic().getCoverage().getMax();

				double limits1[] = { a, b, b, d };
				TrapezoidalFunction semantic1 = new TrapezoidalFunction(limits1);
				brid = new LabelLinguisticDomain(rightBrid.getName(), semantic1);

				addLabel(sl + Lab_t1, brid);
			}
		}
		
		_lh = new int[lh.size()];
		for(int i = 0; i < _lh.length; i++) {
			_lh[i] = lh.get(i);
		}
	}

	private String loadStringLh(String result) {
		StringBuilder lh = new StringBuilder(""); //$NON-NLS-1$
		
		for(int l: _lh) {
			lh.append(l + ","); //$NON-NLS-1$
		}
		lh.replace(lh.length() - 1, lh.length(),  ""); //$NON-NLS-1$
		result += lh.toString();
		
		return result;
	}
	
	private String loadStringFormat(String result) {
		String format = _sl + "," + _slDensity + "," + _sr + "," + _srDensity; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		result += format;
		
		return result;
	}
	
	private String loadStringLabels(String result) {
		StringBuilder labels = new StringBuilder(";"); //$NON-NLS-1$
		
		for(int label: _labels.keySet()) {
			for(int domain: _labels.get(label).keySet()) {
				labels.append(domain + ":" + _labels.get(label).get(domain) + "="); //$NON-NLS-1$ //$NON-NLS-2$
			}
			labels.replace(labels.length() - 1, labels.length(), ""); //$NON-NLS-1$
		}
		labels.replace(labels.length() - 1, labels.length(), ""); //$NON-NLS-1$
		result += labels.toString();
		
		return result;
	}
}

