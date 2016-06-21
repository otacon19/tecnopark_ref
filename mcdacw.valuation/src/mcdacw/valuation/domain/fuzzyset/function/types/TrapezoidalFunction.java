package mcdacw.valuation.domain.fuzzyset.function.types;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.apache.commons.lang.builder.HashCodeBuilder;

import mcdacw.paremetervalidator.ParameterValidator;
import mcdacw.valuation.domain.fuzzyset.function.FragmentFunction;
import mcdacw.valuation.domain.fuzzyset.semantic.IMembershipFunction;
import mcdacw.valuation.domain.numeric.NumericRealDomain;

public class TrapezoidalFunction implements IMembershipFunction {
	
	private final static double EPSILON = 0.000001;
	
	private double _a;
	private double _b;
	private double _c;
	private double _d;
	
	public TrapezoidalFunction() {
		_a = _b = _c = _d = 0d;
	}
	
	public TrapezoidalFunction(double[] limits) {
		this();
		
		ParameterValidator.notNull(limits, "limits");
		ParameterValidator.notInvalidSize(limits.length, 3, 4, "limits");
		
		for(double limit: limits) {
			ParameterValidator.notNegative(limit, "limit");
		}
		
		if(limits.length == 3) {
			_a = limits[0];
			_b = _c = limits[1];
			_d = limits[2];
		} else if(limits.length == 4) {
			_a = limits[0];
			_b = limits[1];
			_c = limits[2];
			_d = limits[3];
		}
	}

	@Override
	public FragmentFunction toFragmentFunction() {
		FragmentFunction result = new FragmentFunction();
		LinearPieceFunction piece;
		NumericRealDomain domain;
		double slope, cutoffY;
		
		if(_a != _b) {
			slope = 1d / (_b - _a);
			cutoffY = -(slope * _a);
			piece = new LinearPieceFunction(slope, cutoffY);
			domain = new NumericRealDomain();
			domain.setMinMax(_a, _b);
			result.addPiece(domain, piece);
		}
		
		if(_b != _c) {
			slope = 0;
			cutoffY = 1;
			piece = new LinearPieceFunction(slope, cutoffY);
			domain = new NumericRealDomain();
			domain.setMinMax(_b, _c);
			result.addPiece(domain, piece);
		}
		
		if(_c != _d) {
			slope = 1d / (_c - _d);
			cutoffY = -(slope * _d);
			piece = new LinearPieceFunction(slope, cutoffY);
			domain = new NumericRealDomain();
			domain.setMinMax(_c, _d);
			result.addPiece(domain, piece);
		}
		
		return result;
	}
	
	@Override
	public boolean isSymmetrical() {
		return (Math.abs((_b - _a) - (_d - _c)) < EPSILON);
	}
	
	@Override
	public boolean isSymmetrical(IMembershipFunction other, double center) {
		double displacement = (center - _d) * 2;
		TrapezoidalFunction clone = (TrapezoidalFunction) clone();
		
		clone._a = _d;
		clone._b = clone._a + (_d - _c);
		clone._c = clone._b + (_c - _b);
		clone._d = clone._c + (_b - _a);
		
		clone._a += displacement;
		clone._b += displacement;
		clone._c += displacement;
		clone._d += displacement;
		
		return clone.equals(other);
	}
	
	@Override
	public NumericRealDomain getCenter() {
		NumericRealDomain result = new NumericRealDomain();
		result.setMinMax(_b, _c);
		
		return result;
	}
	
	@Override
	public NumericRealDomain getCoverage() {
		NumericRealDomain result = new NumericRealDomain();
		result.setMinMax(_a, _d);
		
		return result;
	}
	
	@Override
	public double getMembershipValue(double x) {
		double result;
		
		if(x >= _b && x <= _c) {
			result = 1d;
		} else if(x <= _a || x >= _d) {
			result = 0d;
		} else if(x < _b) {
			result = (x - _a) / (_b - _a);
		} else {
			result = (x - _d) / (_c - _d);
		}
		
		return result;
	}
	
	public boolean isTriangular() {
		return (_b == _c);
	}
	
	@Override
	public double centroid() {
		double centroidLeft, centroidCenter, centroidRight, areaLeft, areaCenter, areaRight,
		areaSum, result;
	
		centroidLeft = (_a + ( 2 * _b)) / 3.;
		centroidCenter = (_b + _c) / 2.;
		centroidRight = ((2 * _c) + _d) / 3.;
		
		areaLeft = (_b - _a) / 2.;
		areaCenter = (_c - _b);
		areaRight = (_d - _c) / 2.;
		areaSum = areaLeft + areaCenter + areaRight;
		
		result = ((centroidLeft * areaLeft) + (centroidCenter * areaCenter) + (centroidRight * areaRight)) / areaSum;
		
		return result;
	}
	

	@Override
	public double maxMin(double min, double max) {
		ParameterValidator.notDisorder(new double[] { min, max }, "min-max", false);
		
		if(( max >= _b) && (min <= _c)) {
			return 1d;
		} else if(max < _b) {
			return getMembershipValue(max);
		} else {
			return getMembershipValue(min);
		}
	}
	
	@Override
	public double maxMin(IMembershipFunction function) {
		TrapezoidalFunction tmf;

		ParameterValidator.notNull(function, "function");
		if (function instanceof TrapezoidalFunction) {
			tmf = (TrapezoidalFunction) function;
		} else {
			throw new IllegalArgumentException("Invalid element type");
		}

		double values[] = new double[5];
		double result;
		double slopeThisAB, slopeFunctionAB, slopeThisCD, slopeFunctionCD;
		
		values[0] = maxMin(tmf._b, tmf._c);
		
		
		if (values[0] == 1) {
			return 1d;
		}

		if (_b == _a) {
			values[1] = values[2] = tmf.getMembershipValue(_a);
		} else {
			slopeThisAB = 1d / (_b - _a);

			if (tmf._a == tmf._b) {
				values[1] = getMembershipValue(tmf._a);
			} else {
				slopeFunctionAB = 1d / (tmf._b - tmf._a);

				if (slopeThisAB == slopeFunctionAB) {
					values[1] = 0d;

				} else {
					values[1] = slopeFunctionAB * slopeThisAB * (_a - tmf._a) / (slopeThisAB - slopeFunctionAB);
				}
			}

			if (tmf._c == tmf._d) {
				values[2] = getMembershipValue(tmf._c);
			} else {
				slopeFunctionCD = 1d / (tmf._c - tmf._d);
				values[2] = slopeFunctionCD * slopeThisAB * (_a - tmf._d) / (slopeThisAB - slopeFunctionCD);
			}
		}
		
		if (_c == _d) {
			values[3] = values[4] = tmf.getMembershipValue(_c);
		} else {
			slopeThisCD = 1d / (_c - _d);

			if (tmf._a == tmf._b) {
				values[3] = getMembershipValue(tmf._a);
			} else {
				slopeFunctionAB = 1d / (tmf._b - tmf._a);
				values[3] = slopeFunctionAB * slopeThisCD * (_d - tmf._a) / (slopeThisCD - slopeFunctionAB);
			}

			if (tmf._c == tmf._d) {
				values[4] = getMembershipValue(tmf._c);

			} else {
				slopeFunctionCD = 1d / (tmf._c - tmf._d);

				if (slopeThisCD == slopeFunctionCD) {
					values[4] = 0d;

				} else {
					values[4] = slopeFunctionCD * slopeThisCD * (_d - tmf._d) / (slopeThisCD - slopeFunctionCD);
				}
			}
		}
		
		for (int i = 1; i < values.length; i++) {
			if (values[i] > 1) {
				values[i] = 0d;
			}
		}
		
		result = Math.max(values[0], Math.max(values[1], Math.max(values[2], Math.max(values[3], values[4]))));
	
		return result;
	}
	
	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("#.##"); //$NON-NLS-1$
		df.setRoundingMode(RoundingMode.CEILING);
		
		if(_b == _c) {
			return ("Trapezoidal(" + df.format(_a) + ", " + df.format(_b) + ", " + df.format(_d) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		} else {
			return ("Trapezoidal(" + df.format(_a) + ", " + df.format(_b) + ", " + df.format(_c) + ", " + df.format(_d) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(this == obj) {
			return true;
		}
		
		if(obj == null) {
			return false;
		}
		
		if(this.getClass() != obj.getClass()) {
			return false;
		}
		
		final TrapezoidalFunction other = (TrapezoidalFunction) obj;
		
		double aRound = Math.abs(new BigDecimal(_a).setScale(3, RoundingMode.HALF_UP).doubleValue());
		double bRound = Math.abs(new BigDecimal(_b).setScale(3, RoundingMode.HALF_UP).doubleValue());
		double cRound = Math.abs(new BigDecimal(_c).setScale(3, RoundingMode.HALF_UP).doubleValue());
		double dRound = Math.abs(new BigDecimal(_d).setScale(3, RoundingMode.HALF_UP).doubleValue());
		double aOtherRound = Math.abs(new BigDecimal(other._a).setScale(3, RoundingMode.HALF_UP).doubleValue());
		double bOtherRound = Math.abs(new BigDecimal(other._b).setScale(3, RoundingMode.HALF_UP).doubleValue());
		double cOtherRound = Math.abs(new BigDecimal(other._c).setScale(3, RoundingMode.HALF_UP).doubleValue());
		double dOtherRound = Math.abs(new BigDecimal(other._d).setScale(3, RoundingMode.HALF_UP).doubleValue());
		
		if(aRound == aOtherRound) {
			if(bRound == bOtherRound) {
				if(cRound == cOtherRound) {
					if(dRound == dOtherRound) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder(17, 31);
		hcb.append(_a);
		hcb.append(_b);
		hcb.append(_c);
		hcb.append(_d);
		return hcb.hashCode();
	}
	
	@Override
	public Object clone() {
		Object result = null;
		
		try {
			result = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public int compareTo(IMembershipFunction other) {
		ParameterValidator.notNull(other, "other");
		
		return Double.compare(this.centroid(), other.centroid());
	}
	
}
