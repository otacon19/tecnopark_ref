package mcdacw.paremetervalidator;

/**
 * Validador de parámetros.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public class ParameterValidator {

	/**
	 * Cadena para mostrar si no se aceptan valores nulos.
	 */
	private final static String NOT_NULL = "Cannot pass null value.";

	/**
	 * Cadena para mostrar si no se aceptan cadenas vacías.
	 */
	private final static String NOT_EMPTY_STRING = "Cannot pass empty string.";

	/**
	 * Cadena para mostrar si no se aceptan arrays vacíos.
	 */
	private final static String NOT_EMPTY_ARRAY = "Cannot pass empty array.";

	/**
	 * Cadena para mostrar si el tamaño es inválido.
	 */
	private final static String INVALID_SIZE = "Invalid size.";

	/**
	 * Cadena para mostrar si el rango es inválido.
	 */
	private final static String INVALID_RANGE = "Invalid range.";

	/**
	 * Cadena para mostrar que no se permiten valores negativos.
	 */
	private final static String NOT_NEGATIVE = "Cannot pass negative value.";

	/**
	 * Cadena para mostrar que no se permiten valores negativos.
	 */
	private final static String NOT_DISORDER = "Cannot pass disorder values.";

	/**
	 * Cadena para mostrar que no se permiten tipos ilegales.
	 */
	private final static String NOT_ILLEGAL_TYPE = "Cannot pass illegal objects.";

	/**
	 * Comprueba que un valor esté en el rango especificado.
	 * 
	 * @param value
	 *            Valor a comprobar.
	 * @param min
	 *            Límite inferior del rango.
	 * @param max
	 *            Límite superior del rango.
	 * @return True si el valor está dentro del rango establecido, False en caso
	 *         contrario.
	 * @throws IllegalArgumentException
	 *             Si el rango no es válido.
	 */
	public static boolean inRange(int value, int min, int max) {
		if (min > max) {
			throw new IllegalArgumentException(INVALID_RANGE);
		}
		return ((min <= value) && (value <= max));
	}

	/**
	 * Comprueba que un valor esté en el rango especificado.
	 * 
	 * @param value
	 *            Valor a comprobar.
	 * @param min
	 *            Límite inferior del rango.
	 * @param max
	 *            Límite superior del rango.
	 * @return True si el valor está dentro del rango establecido, False en caso
	 *         contrario.
	 * @throws IllegalArgumentException
	 *             Si el rango no es válido.
	 */
	public static boolean inRange(double value, double min, double max) {
		if (min > max) {
			throw new IllegalArgumentException(INVALID_RANGE);
		}
		return ((min <= value) && (value <= max));
	}

	/**
	 * Comprueba que un valor esté en el rango especificado.
	 * 
	 * @param value
	 *            Valor a comprobar.
	 * @param min
	 *            Límite inferior del rango.
	 * @param max
	 *            Límite superior del rango.
	 * @param includeStartpoint
	 *            Considerar el valor inicial dentro del rango.
	 * @param includeEndpoing
	 *            Considerar el valor final dentro del rango.
	 * 
	 * @return True si el valor está dentro del rango establecido, False en caso
	 *         contrario.
	 * @throws IllegalArgumentException
	 *             Si el rango no es válido.
	 */
	public static boolean inRange(int value, int min, int max,
			boolean includeStartpoint, boolean includeEndpoint) {
		if (min > max) {
			throw new IllegalArgumentException(INVALID_RANGE);
		}
		if (includeStartpoint) {
			if (includeEndpoint) {
				return ((min <= value) && (value <= max));
			} else {
				return ((min <= value) && (value < max));
			}
		} else {
			if (includeEndpoint) {
				return ((min < value) && (value <= max));
			} else {
				return ((min < value) && (value < max));
			}
		}
	}

	/**
	 * Comprueba que un valor esté en el rango especificado.
	 * 
	 * @param value
	 *            Valor a comprobar.
	 * @param min
	 *            Límite inferior del rango.
	 * @param max
	 *            Límite superior del rango.
	 * @param includeStartpoint
	 *            Considerar el valor inicial dentro del rango.
	 * @param includeEndpoing
	 *            Considerar el valor final dentro del rango.
	 * 
	 * @return True si el valor está dentro del rango establecido, False en caso
	 *         contrario.
	 * @throws IllegalArgumentException
	 *             Si el rango no es válido.
	 */
	public static boolean inRange(double value, double min, double max,
			boolean includeStartpoint, boolean includeEndpoint) {
		if (min > max) {
			throw new IllegalArgumentException(INVALID_RANGE);
		}
		if (includeStartpoint) {
			if (includeEndpoint) {
				return ((min <= value) && (value <= max));
			} else {
				return ((min <= value) && (value < max));
			}
		} else {
			if (includeEndpoint) {
				return ((min < value) && (value <= max));
			} else {
				return ((min < value) && (value < max));
			}
		}
	}

	/**
	 * Comprueba si una parámetro es nulo.
	 * 
	 * @param parameter
	 *            Parámetro a comprobar.
	 * @return True si es nulo, False en caso contrario.
	 */
	public static boolean isNull(Object parameter) {
		return (parameter == null);
	}

	/**
	 * Comprueba si una cadena está vacía.
	 * 
	 * @param parameter
	 *            Parámetro a comprobar.
	 * @return True si es la cadena vacía, False en caso contrario.
	 * @throws IllegalArgumentException
	 *             Si el parámetro es nulo.
	 */
	public static boolean isEmpty(String parameter) {
		notNull(parameter, "parameter");
		return (parameter.trim().isEmpty());
	}

	/**
	 * Comprueba si un array está vacío.
	 * 
	 * @param parameter
	 *            Parámetro a comprobar.
	 * @return True si el array está vacío, False en caso contrario.
	 * 
	 * @throws IllegalArgumentException
	 *             Si el parámetro es nulo.
	 */
	public static boolean isEmpty(Object[] parameter) {
		notNull(parameter, "parameter");
		return (parameter.length == 0);
	}

	/**
	 * Comprueba si un valor es negativo.
	 * 
	 * @param value
	 *            Valor a comprobar.
	 *            
	 * @return True si el valor es negativo, False en caso contrario.
	 */
	public static boolean isNegative(double value) {
		return (value < 0);
	}

	/**
	 * Comprueba que un conjunto de valores esté desordenado
	 * 
	 * @param values
	 *            Valores a comprobar.
	 *            
	 * @return True si los valores están en orden, False en caso contrario.
	 * 
	 * @throws IllegalArgumentException
	 *             Si los valores son nulos.
	 */
	public static boolean isOrdered(double[] values) {

		int length;

		notNull(values, "values");

		length = values.length;
		if (length < 2) {
			return true;
		} else {
			for (int i = 0; i < length; i++) {
				for (int j = 0; j < i; j++) {
					if (values[j] > values[i]) {
						return false;
					}
				}
				for (int j = i + 1; j < length; j++) {
					if (values[j] < values[i]) {
						return false;
					}
				}
			}
			return true;
		}
	}

	/**
	 * Comprueba que un conjunto de valores esté ordenado de forma estricta.
	 * 
	 * @param values
	 *            Valores a comprobar.
	 * @return True si los valores están en orden extricto, False en caso
	 *         contrario.
	 * @throws IllegalArgumentException
	 *             Si los valores son nulos.
	 */
	public static boolean isStrictlyOrdered(double[] values) {

		int length;

		notNull(values, "values");

		length = values.length;
		if (length < 2) {
			return true;
		} else {
			for (int i = 0; i < length; i++) {
				for (int j = 0; j < i; j++) {
					if (values[j] >= values[i]) {
						return false;
					}
				}
				for (int j = i + 1; j < length; j++) {
					if (values[j] <= values[i]) {
						return false;
					}
				}
			}
			return true;
		}
	}

	/**
	 * Comprueba que un elemento sea de alguno de los tipos indicados.
	 * 
	 * @param object
	 *            Objeto que debe ser del tipo indicado.
	 * @param types
	 *            Tipos permitidos.
	 * @return True si el elemento es de alguno de los tipos indicados, False en
	 *         caso contrario.
	 * @throw IllegalArgumentException Si object es null o,
	 *        <p>
	 *        si types está vacío o
	 *        <p>
	 *        si types es null.
	 */
	public static boolean validElementType(Object object, String[] types) {

		notEmpty(types, "types");
		notNull(object, "object");

		String objectClass = object.getClass().toString();

		for (String type : types) {
			if (objectClass.equals(type)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Comprueba si una cadena es nula o está vacía.
	 * 
	 * @param parameter
	 *            Parámetro a comprobar.
	 * @return True si es la cadena es nula o está vacía, False en caso
	 *         contrario.
	 */
	private static boolean isNullOrEmpty(String parameter) {
		return (parameter == null) ? true : parameter.trim().isEmpty();
	}

	/**
	 * Construye el prefijo para una excepción.
	 * 
	 * @param parameterName
	 * @return La cadena "parameterName + ": "' si esta no es nula ni vacía.
	 *         <p>
	 *         La cadena vacía en caso contrario.
	 */
	private static String exceptionPrefix(String parameterName) {
		return isNullOrEmpty(parameterName) ? "" : parameterName + ": ";
	}

	/**
	 * Lanza una excepción de argumento inválido.
	 * 
	 * @param parameterName
	 *            Nombre del argumento inválido.
	 * @param msg
	 *            Mensaje de la excepción.
	 * @throw IllegalArgumentExceptión Siempre que es invocada.
	 */
	private static void throwIllegalArgumentException(String parameterName,
			String msg) {
		String prefix;

		prefix = exceptionPrefix(parameterName);
		throw new IllegalArgumentException(prefix + msg);
	}

	/**
	 * Comprueba que un parametro no sea nulo.
	 * 
	 * @param parameter
	 *            Parametro a comprobar.
	 * @param parameterName
	 *            Nombre del parámetro.
	 * @throws IllegalArgumentException
	 *             Si el parámetro es nulo.
	 */
	public static void notNull(Object parameter, String parameterName) {
		if (isNull(parameter)) {
			throwIllegalArgumentException(parameterName, NOT_NULL);
		}
	}

	/**
	 * Comprueba que una cadena no sea vacía.
	 * 
	 * @param parameter
	 *            Parametro a comprobar.
	 * @param parameterName
	 *            Nombre del parámetro.
	 * @throws IllegalArgumentException
	 *             Si la cadena está vacía o es nula.
	 */
	public static void notEmpty(String parameter, String parameterName) {
		if (isEmpty(parameter)) {
			throwIllegalArgumentException(parameterName, NOT_EMPTY_STRING);
		}
	}

	/**
	 * Comprueba que un array no esté vacío
	 * 
	 * @param parameter
	 *            Array a comprobar.
	 * @param parameterName
	 *            Nombre del array.
	 * @throws IllegalArgumentException
	 *             Si el array está vacía o es nulo.
	 */
	public static void notEmpty(Object[] parameter, String parameterName) {
		if (isEmpty(parameter)) {
			throwIllegalArgumentException(parameterName, NOT_EMPTY_ARRAY);
		}
	}

	/**
	 * Comprueba que el tamaño de un elemento esté dentro de los valores
	 * permitidos.
	 * 
	 * @param size
	 *            Tamaño del elemento.
	 * @param minValidSize
	 *            Tamaño mínimo permitido.
	 * @param maxValidSize
	 *            Tamaño máximo permitido.
	 * @param parameterName
	 *            Nombre del parámetro.
	 * @throws IllegalArgumentException
	 *             Si el tamaño no está dentro del rango permitido o si el rango
	 *             es inválido.
	 */
	public static void notInvalidSize(int size, int minValidSize,
			int maxValidSize, String parameterName) {
		if (!inRange(size, minValidSize, maxValidSize)) {
			throwIllegalArgumentException(parameterName, INVALID_SIZE
					+ " Value must be in range [" + minValidSize + ", "
					+ maxValidSize + "].");
		}
	}

	/**
	 * Comprueba que el tamaño de un elemento esté dentro de los valores
	 * permitidos.
	 * 
	 * @param size
	 *            Tamaño del elemento.
	 * @param minValidSize
	 *            Tamaño mínimo permitido.
	 * @param maxValidSize
	 *            Tamaño máximo permitido.
	 * @param parameterName
	 *            Nombre del parámetro.
	 * @throws IllegalArgumentException
	 *             Si el tamaño no está dentro del rango permitido o si el rango
	 *             es inválido.
	 */
	public static void notInvalidSize(double size, double minValidSize,
			double maxValidSize, String parameterName) {
		if (!inRange(size, minValidSize, maxValidSize)) {
			throwIllegalArgumentException(parameterName, INVALID_SIZE
					+ " Value must be in range [" + minValidSize + ", "
					+ maxValidSize + "].");
		}
	}

	/**
	 * Comprueba que el tamaño de un elemento esté dentro de los valores
	 * permitidos.
	 * 
	 * @param size
	 *            Tamaño del elemento.
	 * @param minValidSize
	 *            Tamaño mínimo permitido.
	 * @param maxValidSize
	 *            Tamaño máximo permitido.
	 * @param includeStartpoint
	 *            Incluir el valor inicial dentro del rango.
	 * @param maxValidSize
	 *            Incluier el valor final dentro del rango.
	 * @param parameterName
	 *            Nombre del parámetro.
	 * @throws IllegalArgumentException
	 *             Si el tamaño no está dentro del rango permitido o si el rango
	 *             es inválido.
	 */
	public static void notInvalidSize(int size, int minValidSize,
			int maxValidSize, boolean includeStartpoint,
			boolean includeEndpoint, String parameterName) {
		if (!inRange(size, minValidSize, maxValidSize, includeStartpoint,
				includeEndpoint)) {
			String open = (includeEndpoint) ? "[" : "(";
			String close = (includeEndpoint) ? "]" : ")";
			throwIllegalArgumentException(parameterName, INVALID_SIZE
					+ " Value must be in range " + open + minValidSize + ", "
					+ maxValidSize + close + ".");
		}

	}

	/**
	 * Comprueba que el tamaño de un elemento esté dentro de los valores
	 * permitidos.
	 * 
	 * @param size
	 *            Tamaño del elemento.
	 * @param minValidSize
	 *            Tamaño mínimo permitido.
	 * @param maxValidSize
	 *            Tamaño máximo permitido.
	 * @param includeStartpoint
	 *            Incluir el valor inicial dentro del rango.
	 * @param maxValidSize
	 *            Incluier el valor final dentro del rango.
	 * @param parameterName
	 *            Nombre del parámetro.
	 * @throws IllegalArgumentException
	 *             Si el tamaño no está dentro del rango permitido o si el rango
	 *             es inválido.
	 */
	public static void notInvalidSize(double size, double minValidSize,
			double maxValidSize, boolean includeStartpoint,
			boolean includeEndpoint, String parameterName) {
		if (!inRange(size, minValidSize, maxValidSize, includeStartpoint,
				includeEndpoint)) {
			String open = (includeEndpoint) ? "[" : "(";
			String close = (includeEndpoint) ? "]" : ")";
			throwIllegalArgumentException(parameterName, INVALID_SIZE
					+ " Value must be in range " + open + minValidSize + ", "
					+ maxValidSize + close + ".");
		}

	}

	/**
	 * Comprueba que un valor no sea negativo.
	 * 
	 * @param value
	 *            Valor a comprobar.
	 * @param parameterName
	 *            Nombre del parámetro.
	 * @throw IllegalArgumentException Si el parámetro es negativo.
	 */
	public static void notNegative(double value, String parameterName) {
		if (isNegative(value)) {
			throwIllegalArgumentException(parameterName, NOT_NEGATIVE);
		}
	}

	/**
	 * Comprueba que un conjunto de valores no estén desordenados.
	 * 
	 * @param values
	 *            Valores a comprobar.
	 * @param parameterName
	 *            Nombre del parámetro.
	 * @param strictlyOrder
	 *            Orden estricto.
	 * @throw IllegalArgumentException Si los valores son nulos o están
	 *        desordenados.
	 */
	public static void notDisorder(double[] values, String parameterName,
			boolean strictlyOrder) {

		boolean disorder;

		if (strictlyOrder) {
			disorder = !isStrictlyOrdered(values);
		} else {
			disorder = !isOrdered(values);
		}

		if (disorder) {
			throwIllegalArgumentException(parameterName, NOT_DISORDER);
		}
	}

	/**
	 * Comprueba que un elemento sea de alguno de los tipos indicados.
	 * 
	 * @param object
	 *            Objeto que debe ser del tipo indicado.
	 * @param types
	 *            Tipos permitidos.
	 * @param parameterName
	 *            Nombre del parámetro.
	 * @throw IllegalArgumentException Si el objeto no es de alguno de los tipos
	 *        indicados o,
	 *        <p>
	 *        si object es null o,
	 *        <p>
	 *        si types está vacío o
	 *        <p>
	 *        si types es null.
	 */
	public static void notIllegalElementType(Object object, String[] types,
			String parameterName) {
		if (!validElementType(object, types)) {
			throwIllegalArgumentException(parameterName, NOT_ILLEGAL_TYPE);
		}
	}

}
