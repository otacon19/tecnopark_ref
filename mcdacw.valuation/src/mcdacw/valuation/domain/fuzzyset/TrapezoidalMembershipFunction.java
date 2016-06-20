package mcdacw.valuation.domain.fuzzyset;

import mcdacw.paremetervalidator.ParameterValidator;
import mcdacw.valuation.Interval;
import mcdacw.valuation.domain.numeric.IntervalNumericDomain;
import mcdacw.valuation.domain.numeric.NumericDomain;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Función de pertenencia trapezoidal.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public class TrapezoidalMembershipFunction implements IMembershipFunction {

	private final static double EPSILON = 0.000001;

	/**
	 * Límite inferior de pertenencia.
	 */
	private double _a;

	/**
	 * Límite central inferior de pertenencia.
	 */
	private double _b;

	/**
	 * Límite central superior de pertenencia.
	 */
	private double _c;

	/**
	 * Límite superior de pertenencia.
	 */
	private double _d;

	/**
	 * Constructor por defecto de una función de pertenencia trapezoidal.
	 * <p>
	 * Privado para impedir su instanciación de esta forma.
	 */
	private TrapezoidalMembershipFunction() {
		_a = _b = _c = _d = 0d;
	}

	/**
	 * Construye una función de pertenencia trapezoidal a partir de un vector de
	 * límites.
	 * 
	 * @param limits
	 *            Límites de la función de pertenencia.
	 * @throws IllegalArgumentException
	 *             Si el vector es nulo o,
	 *             <p>
	 *             no tiene todos los elementos necesarios o
	 *             <p>
	 *             los valores de estos no son correctos.
	 */
	public TrapezoidalMembershipFunction(double[] limits) {
		this();

		ParameterValidator.notNull(limits, "limits");
		ParameterValidator.notInvalidSize(limits.length, 3, 4, "limits");
		for (double limit : limits) {
			ParameterValidator.notNegative(limit, "limit");
		}
		ParameterValidator.notDisorder(limits, "limits", false);
		if (limits.length == 3) {
			_a = limits[0];
			_b = _c = limits[1];
			_d = limits[2];
		} else if (limits.length == 4) {
			_a = limits[0];
			_b = limits[1];
			_c = limits[2];
			_d = limits[3];
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mcdacw.valuation.domain.fuzzyset.IMembershipFunction#
	 * toPiecewiseDefinedFunction()
	 */
	@Override
	public PiecewiseDefinedFunction toPiecewiseDefinedFunction() {
		PiecewiseDefinedFunction result = new PiecewiseDefinedFunction();
		LinearPieceFunction piece;
		NumericDomain domain;
		double slope;
		double yIntercept;

		// Pendiente inicial
		if (_a != _b) {
			slope = 1d / (_b - _a);
			yIntercept = -(slope * _a);
			piece = new LinearPieceFunction(slope, yIntercept);
			domain = new NumericDomain(_a, _b);
			result.addPiece(domain, piece);
		}

		// Función trapezoidal
		if (_b != _c) {
			slope = 0;
			yIntercept = 1;
			piece = new LinearPieceFunction(slope, yIntercept);
			domain = new NumericDomain(_b, _c);
			result.addPiece(domain, piece);
		}

		// Pendiente final
		if (_c != _d) {
			slope = 1d / (_c - _d);
			yIntercept = -(slope * _d);
			piece = new LinearPieceFunction(slope, yIntercept);
			domain = new NumericDomain(_c, _d);
			result.addPiece(domain, piece);
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mcdacw.valuation.domain.fuzzyset.IMembershipFunction#isSymmetrical()
	 */
	@Override
	public boolean isSymmetrical() {
		return (Math.abs((_b - _a) - (_d - _c)) < EPSILON);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mcdacw.valuation.domain.fuzzyset.IMembershipFunction#isSymmetrical(mcdacw
	 * .valuation.domain.fuzzyset.IMembershipFunction, double)
	 */
	@Override
	public boolean isSymmetrical(IMembershipFunction other, double center) {

		// Calculamos el desplazamiento
		double displacement = (center - _d) * 2;

		// Clonamos la función actual
		TrapezoidalMembershipFunction thisClone = (TrapezoidalMembershipFunction) clone();

		// Invertimos
		thisClone._a = _d;
		thisClone._b = thisClone._a + (_d - _c);
		thisClone._c = thisClone._b + (_c - _b);
		thisClone._d = thisClone._c + (_b - _a);

		// Desplazamos
		thisClone._a += displacement;
		thisClone._b += displacement;
		thisClone._c += displacement;
		thisClone._d += displacement;

		// Son simétricas si son iguales respecto a la desplazada
		return thisClone.equals(other);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mcdacw.valuation.domain.fuzzyset.IMembershipFunction#getCenter()
	 */
	@Override
	public NumericDomain getCenter() {

		mcdacw.valuation.domain.Activator activator = mcdacw.valuation.domain.Activator
				.getActivator();
		mcdacw.valuation.domain.DomainFactory domainFactory = activator
				.getDomainFactory();

		NumericDomain result = domainFactory.buildNumericDomain();
		result.setMinMax(_b, _c);

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mcdacw.valuation.domain.fuzzyset.IMembershipFunction#getCoverage()
	 */
	@Override
	public NumericDomain getCoverage() {

		mcdacw.valuation.domain.Activator activator = mcdacw.valuation.domain.Activator
				.getActivator();
		mcdacw.valuation.domain.DomainFactory domainFactory = activator
				.getDomainFactory();

		NumericDomain result = domainFactory.buildNumericDomain();
		result.setMinMax(_a, _d);

		return result;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mcdacw.valuation.domain.fuzzyset.IMembershipFunction#getMembershipValue
	 * (double)
	 */
	@Override
	public double getMembershipValue(double x) {

		double result;

		/* Si el punto está entre b y c, la función tiene valor 1. */
		if (x >= _b && x <= _c) {
			result = 1d;

			/* Si el punto es menor a a o mayor a d, la funcion tiene valor 0. */
		} else if (x <= _a || x >= _d) {
			result = 0d;

			/*
			 * Si no, si el punto es menor a b (y mayor a a), calcular el valor
			 * de la recta que pasa por (a, 0) y (b, 1) en el punto
			 */
		} else if (x < _b) {
			result = (x - _a) / (_b - _a);

			/*
			 * Si no, el punto es mayor a c y menor a d, calcular el valor de la
			 * recta que pasa por (c, 1) y (d, 0) en el punto
			 */
		} else {
			result = (x - _d) / (_c - _d);
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mcdacw.valuation.domain.fuzzyset.IMembershipFunction#maxMin(mcdacw.valuation
	 * .Interval)
	 */
	@Override
	public double maxMin(Interval interval) {

		ParameterValidator.notNull(interval, "interval");

		Interval normalized;
		double min, max;

		normalized = (Interval) interval.getNormalized();
		min = normalized.getMin();
		max = normalized.getMax();

		// Si el intervalo está entre b y c, el mayor valor es 1
		if ((max >= _b) && (min <= _c)) {
			return 1d;
		}

		// Si los valores que cubre el intervalor son menores a b, el mayor
		// valor de la función será el obtenido en el valor máximo del
		// intervalo.
		else if (max < _b) {
			return (getMembershipValue(max));
		}

		// Si los valores que cubre el intervalo son mayores a c, el mayor valor
		// de la función será el obtenido en el valor mínimo del intervalo.
		else {
			return (getMembershipValue(min));
		}

	}

	/**
	 * Devuelve el máximo valor del mínimo entre la función actual y otra
	 * función.
	 * 
	 * @param function
	 *            Función a combinar con la actual, mediante el mínimo, para
	 *            obtener el máximo valor.
	 * 
	 * @return Máximo valor del mínimo entre la función actual y function.
	 * 
	 * @throws IllegalArgumentException
	 *             Si function es null o
	 *             <p>
	 *             si function no es una función de pertenencia trapezoidal.
	 */
	@Override
	public double maxMin(IMembershipFunction function) {

		TrapezoidalMembershipFunction tmf;

		// Validación de parámetros
		ParameterValidator.notNull(function, "function");
		if (function instanceof TrapezoidalMembershipFunction) {
			tmf = (TrapezoidalMembershipFunction) function;
		} else {
			throw new IllegalArgumentException("Invalid element type.");
		}

		double values[] = new double[5];
		double result;
		double slopeThisAB, slopeFunctionAB, slopeThisCD, slopeFunctionCD;

		// Si en el intervalo [b, c] toma el valor máximo devolvemos 1.
		values[0] = maxMin(new Interval(new IntervalNumericDomain(0, 1),
				tmf._b, tmf._c));
		if (values[0] == 1) {
			return 1d;
		}

		// Calcular la intersección entre las rectas:
		// (a,0),(b,1) y (funcion.a,0),(funcion.b,1)
		// (a,0),(b,1) y (funcion.c,1),(funcion.d,0)
		// (c,1),(d,0) y (funcion.a,0),(funcion.b,1)
		// (c,1),(d,0) y (funcion.c,1),(funcion.d,0)

		// controlar pendiente infinita
		if (_b == _a) {
			values[1] = values[2] = tmf.getMembershipValue(_a);
		} else {
			slopeThisAB = 1d / (_b - _a);

			// controlar pendiente infinita
			if (tmf._a == tmf._b) {
				values[1] = getMembershipValue(tmf._a);
			} else {
				slopeFunctionAB = 1d / (tmf._b - tmf._a);

				// controlar rectas paralelas
				if (slopeThisAB == slopeFunctionAB) {
					values[1] = 0d;

				} else {
					values[1] = slopeFunctionAB * slopeThisAB * (_a - tmf._a)
							/ (slopeThisAB - slopeFunctionAB);
				}
			}

			// controlar pendiente infinita
			if (tmf._c == tmf._d) {
				values[2] = getMembershipValue(tmf._c);
			} else {
				slopeFunctionCD = 1d / (tmf._c - tmf._d);
				values[2] = slopeFunctionCD * slopeThisAB * (_a - tmf._d)
						/ (slopeThisAB - slopeFunctionCD);
			}
		}

		// controlar pendiente infinita
		if (_c == _d) {
			values[3] = values[4] = tmf.getMembershipValue(_c);
		} else {
			slopeThisCD = 1d / (_c - _d);

			// controlar pendiente infinita
			if (tmf._a == tmf._b) {
				values[3] = getMembershipValue(tmf._a);
			} else {
				slopeFunctionAB = 1d / (tmf._b - tmf._a);
				values[3] = slopeFunctionAB * slopeThisCD * (_d - tmf._a)
						/ (slopeThisCD - slopeFunctionAB);
			}

			// controlar pendiente infinita
			if (tmf._c == tmf._d) {
				values[4] = getMembershipValue(tmf._c);

			} else {
				slopeFunctionCD = 1d / (tmf._c - tmf._d);

				// controlar rectas paralelas
				if (slopeThisCD == slopeFunctionCD) {
					values[4] = 0d;

				} else {
					values[4] = slopeFunctionCD * slopeThisCD * (_d - tmf._d)
							/ (slopeThisCD - slopeFunctionCD);
				}
			}
		}

		// Controlar que el valor de corte no sea mayor a 1.
		for (int i = 1; i < values.length; i++) {
			if (values[i] > 1) {
				values[i] = 0d;
			}
		}

		// Devolver el mayor valor entre los 5
		result = Math.max(
				values[0],
				Math.max(values[1],
						Math.max(values[2], Math.max(values[3], values[4]))));
		return result;
	}

	/**
	 * Calcula el centroide de la función de pertenencia trapezoidal.
	 * 
	 * @return Centroide de la función de pertenencia.
	 */
	public double centroid() {

		double centroidLeft;
		double centroidCenter;
		double centroidRight;
		double areaLeft, areaCenter, areaRight, areaSum;
		double result;

		centroidLeft = (_a + (2 * _b)) / 3.;
		centroidCenter = (_b + _c) / 2.;
		centroidRight = ((2 * _c) + _d) / 3.;

		areaLeft = (_b - _a) / 2.;
		areaCenter = (_c - _b);
		areaRight = (_d - _c) / 2.;
		areaSum = areaLeft + areaCenter + areaRight;

		result = ((centroidLeft * areaLeft) + (centroidCenter * areaCenter) + (centroidRight * areaRight))
				/ areaSum;

		return result;
	}

	/**
	 * Indica si la etiqueta es triangular.
	 * 
	 * @return True si la etiqueta es triangular.
	 *         <p>
	 *         False si la etiqueta es trapezoidal.
	 */
	public boolean isTriangular() {
		return (_b == _c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mcdacw.valuation.domain.fuzzyset.IMembershipFunction#resumeValue()
	 */
	@Override
	public double resumeValue() {
		return centroid();
	}

	/**
	 * Devuelve una descripción de la función de pertenencia.
	 * 
	 * @return Descripción de la función de pertenencia.
	 */
	@Override
	public String toString() {
		if (_b == _c) {
			return ("Trapeze(" + _a + ", " + _b + ", " + _d + ")");
		} else {
			return ("Trapeze(" + _a + ", " + _b + ", " + _c + ", " + _d + ")");
		}
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

		final TrapezoidalMembershipFunction other = (TrapezoidalMembershipFunction) object;

		if (Math.abs(_a - other._a) < EPSILON) {
			if (Math.abs(_b - other._b) < EPSILON) {
				if (Math.abs(_c - other._c) < EPSILON) {
					if (Math.abs(_d - other._d) < EPSILON) {
						return true;
					}
				}
			}
		}
		
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(_a).append(_b).append(_c)
				.append(_d).toHashCode();
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

		return result;
	}

	/**
	 * Compara la función de pertenencia actual con otra función de pertenencia
	 * indicada haciendo uso del centroide de ambas.
	 * 
	 * @param other
	 *            Función de pertenencia a comparar.
	 * @return this < other --> <0.
	 *         <p>
	 *         this = other --> 0
	 *         <p>
	 *         this > other --> >0
	 * @throws IllegalArgumentException
	 *             Si other es null.
	 */
	@Override
	public int compareTo(IMembershipFunction other) {
		ParameterValidator.notNull(other, "other");
		return Double.compare(this.resumeValue(), other.resumeValue());
	}

}
