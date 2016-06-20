package mcdacw.valuation.domain.fuzzyset;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import mcdacw.paremetervalidator.ParameterValidator;
import mcdacw.valuation.domain.numeric.NumericDomain;

/**
 * Función a trozos.
 * 
 * @author Estrella Liébana, Francisco Javier
 * @version 1.0
 */
public class PiecewiseDefinedFunction {

	Map<NumericDomain, IPieceFunction> _pieces;

	/**
	 * Constructor por defecto de PiecewiseDefinedFunctino
	 */
	public PiecewiseDefinedFunction() {
		_pieces = new HashMap<NumericDomain, IPieceFunction>();
	}

	/**
	 * Añade un nuevo trozo a la función.
	 * 
	 * @param domain
	 *            Dominio en el que se define el trozo.
	 * @param piece
	 *            Trozo a añadir
	 * 
	 * @throws IllegalArgumentException
	 *             si domain es null o,
	 *             <p>
	 *             si piece es null o
	 *             <p>
	 *             si piece no es del tipo 'LinearPieceFunction'.
	 */
	public void addPiece(NumericDomain domain, IPieceFunction piece) {
		ParameterValidator.notNull(domain, "domain");
		ParameterValidator.notNull(piece, "piece");

		// NOTA: Actualmente solo trozos de funciones lineales están soportados
		ParameterValidator.notIllegalElementType(piece,
				new String[] { LinearPieceFunction.class.toString() }, "piece");

		// Controlar dominios vacíos.
		double otherMin = domain.getMin();
		double otherMax = domain.getMax();
		if (otherMin == otherMax) {
			return;
		}

		// Controlar cruces entre trozos
		double thisMin;
		double thisMax;
		NumericDomain lower;
		NumericDomain central;
		NumericDomain upper;
		IPieceFunction thisPiece;

		Set<NumericDomain> domains = _pieces.keySet();

		for (NumericDomain thisDomain : domains) {

			thisMin = thisDomain.getMin();
			thisMax = thisDomain.getMax();
			thisPiece = _pieces.get(thisDomain);

			// Comparamos el límite inferior
			switch (Double.compare(thisMin, otherMin)) {

			// thisMin a la izquierda de otherMin
			case -1:

				// Comparamos el máximo de this con el mínimo de other
				if (thisMax > otherMin) {

					// Comparamos los valores máximos
					switch (Double.compare(thisMax, otherMax)) {

					// Se cruzan
					// (TTTT )
					// ( OOOO)
					case -1:
						lower = new NumericDomain(thisMin, otherMin);
						central = new NumericDomain(otherMin, thisMax);
						upper = new NumericDomain(thisMax, otherMax);

						_pieces.remove(thisDomain);
						_pieces.put(lower, thisPiece);
						_pieces.put(central, piece.sum(thisPiece));
						addPiece(upper, piece);
						break;

					// this contiene a other y el máximo es igual
					// (TTTTT)
					// ( OOOO)
					case 0:
						lower = new NumericDomain(thisMin, otherMin);
						upper = new NumericDomain(otherMin, otherMax);

						_pieces.remove(thisDomain);
						_pieces.put(lower, thisPiece);
						_pieces.put(upper, piece.sum(thisPiece));
						break;

					// this contiene a other y el máximo es mayor
					// (TTTTT)
					// ( OOO )
					case 1:
						lower = new NumericDomain(thisMin, otherMin);
						central = new NumericDomain(otherMin, otherMax);
						upper = new NumericDomain(otherMax, thisMax);

						_pieces.remove(thisDomain);
						_pieces.put(lower, thisPiece);
						_pieces.put(central, piece.sum(thisPiece));
						_pieces.put(upper, thisPiece);
						break;
					}

					// Cualquier nuevo cruce se encontrará de forma recursiva
					// por lo que salimos
					return;
				}

				break;

			// Los mínimos son iguales
			case 0:

				switch (Double.compare(thisMax, otherMax)) {

				// other contiene a this
				// (TTTT )
				// (OOOOO)
				case -1:
					lower = new NumericDomain(otherMin, thisMax);
					upper = new NumericDomain(thisMax, otherMax);

					_pieces.remove(thisDomain);
					_pieces.put(lower, piece.sum(thisPiece));
					addPiece(upper, piece);
					break;

				// Son iguales
				// (TTTTT)
				// (OOOOO)
				case 0:
					_pieces.remove(thisDomain);
					_pieces.put(domain, piece.sum(thisPiece));
					break;

				// this contiene a other
				// (TTTTT)
				// (OOOO )
				case 1:
					lower = new NumericDomain(thisMin, otherMax);
					upper = new NumericDomain(otherMax, thisMax);

					_pieces.remove(thisDomain);
					_pieces.put(lower, piece.sum(thisPiece));
					_pieces.put(upper, thisPiece);
					break;
				}

				// Cualquier nuevo cruce se encontrará de forma recursiva
				// por lo que salimos
				return;

				// thisMin a la derecha de otherMin
			case 1:

				// Comparamos el máximo de other con el mínimo de this
				if (otherMax > thisMin) {

					// Comparamos los máximos
					switch (Double.compare(otherMax, thisMax)) {

					// Se cruzan
					// ( TTTT)
					// (OOOO )
					case -1:
						lower = new NumericDomain(otherMin, thisMin);
						central = new NumericDomain(thisMin, otherMax);
						upper = new NumericDomain(otherMax, thisMax);

						_pieces.remove(thisDomain);
						_pieces.put(central, piece.sum(thisPiece));
						_pieces.put(upper, thisPiece);
						addPiece(lower, piece);
						break;

					// other contiene a this y los máximos son iguales
					// ( TTTT)
					// (OOOOO)
					case 0:
						lower = new NumericDomain(otherMin, thisMin);
						upper = new NumericDomain(thisMin, otherMax);

						_pieces.remove(thisDomain);
						_pieces.put(upper, piece.sum(thisPiece));
						addPiece(lower, piece);
						break;

					// other contiene a this y los máximos son diferentes
					// ( TTT )
					// (OOOOO)
					case 1:
						lower = new NumericDomain(otherMin, thisMin);
						;
						central = new NumericDomain(thisMin, thisMax);
						upper = new NumericDomain(thisMax, otherMax);

						_pieces.remove(thisDomain);
						_pieces.put(central, piece.sum(thisPiece));
						addPiece(lower, piece);
						addPiece(upper, piece);
						break;

					}

					// Cualquier nuevo cruce se encontrará de forma recursiva
					// por lo que salimos
					return;
				}

				break;

			}
		}

		// Llegamos aquí únicamente si no se ha encontrado ningún cruce y, en
		// tal caso, añadimos la trozo.
		_pieces.put(domain, piece);
	}

	/**
	 * Fusiona los trozos iguales en caso de que el dominio se toque en un
	 * extremo.
	 */
	public void simplify() {

		Map<NumericDomain, IPieceFunction> pieces2 = new HashMap<NumericDomain, IPieceFunction>();

		LinearPieceFunction piece1;
		LinearPieceFunction piece2;
		NumericDomain domain;

		for (NumericDomain domain1 : _pieces.keySet()) {
			piece1 = (LinearPieceFunction) _pieces.get(domain1);

			for (NumericDomain domain2 : pieces2.keySet()) {
				piece2 = (LinearPieceFunction) pieces2.get(domain2);

				if (domain1.getMin() == domain2.getMax()) {
					if (piece1.equals(piece2)) {
						domain = new NumericDomain(domain2.getMin(),
								domain1.getMax());
						_pieces.remove(domain1);
						_pieces.remove(domain2);
						_pieces.put(domain, piece1);
						simplify();
						return;
					}

				} else if (domain1.getMax() == domain2.getMin()) {
					if (piece1.equals(piece2)) {
						domain = new NumericDomain(domain1.getMin(),
								domain2.getMax());
						_pieces.remove(domain1);
						_pieces.remove(domain2);
						_pieces.put(domain, piece1);
						simplify();
						return;
					}
				}
			}

			// Si se llega aquí añadimos el trozo
			pieces2.put(domain1, piece1);
		}

		_pieces = pieces2;

	}

	/**
	 * Devuelve los trozos de la función.
	 * 
	 * @return Trozos de la función.
	 */
	public Map<NumericDomain, IPieceFunction> getPieces() {
		return _pieces;
	}
}
