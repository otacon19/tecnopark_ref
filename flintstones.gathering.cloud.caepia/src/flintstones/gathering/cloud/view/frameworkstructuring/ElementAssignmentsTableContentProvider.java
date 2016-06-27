package flintstones.gathering.cloud.view.frameworkstructuring;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableNoScrollModel;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.renderers.FixedCellRenderer;
import de.kupzog.ktable.renderers.TextCellRenderer;
import flintstones.gathering.cloud.model.Key;
import flintstones.gathering.cloud.model.Problem;

public class ElementAssignmentsTableContentProvider extends KTableNoScrollModel {
	
	private List<String> _experts;
	private List<String> _alternatives;
	private List<String> _criteria;
	private List<String> _finalExperts;
	private List<String> _finalAlternatives;
	private List<String> _finalCriteria;
	private Map<String, String> _index;
	private String[][] _colHeaderMatrix;
	private String[][] _rowHeaderMatrix;
	private EElement _row;
	private EElement _col;
	private int _expertsDepth = -1;
	private int _alternativesDepth = -1;
	private int _criteriaDepth = -1;
	private int _fixedRows = -1;
	private int _fixedCols = -1;
	
	private Problem _problem;

	private String _element;

	private final FixedCellRenderer _fixedRenderer = new FixedCellRenderer(FixedCellRenderer.STYLE_FLAT | SWT.BOLD);

	private final FixedCellRenderer _fixedRenderersInTable = new FixedCellRenderer(FixedCellRenderer.STYLE_FLAT | TextCellRenderer.INDICATION_FOCUS);

	public ElementAssignmentsTableContentProvider(ElementAssignmentTable table, String element) {
		super(table);
		
		_problem = (Problem) RWT.getUISession().getAttribute("problem");

		_element = element;
		_experts = _problem.getExperts();
		_alternatives = _problem.getAlternatives();
		_criteria = _problem.getCriteria();

		computeOrder();
		initialize();

		_fixedRenderer.setAlignment(SWTX.ALIGN_HORIZONTAL_CENTER | SWTX.ALIGN_VERTICAL_CENTER);
		_fixedRenderersInTable.setAlignment(SWTX.ALIGN_HORIZONTAL_CENTER | SWTX.ALIGN_VERTICAL_CENTER);
		_fixedRenderersInTable.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	}

	private void computeOrder() {
		if (_problem.isAlternative(_element)) {
			_row = EElement.EXPERT;
			_col = EElement.CRITERION;
		} else if (_problem.isCriterion(_element)) {
			_row = EElement.ALTERNATIVE;
			_col = EElement.EXPERT;
		} else {
			_row = EElement.ALTERNATIVE;
			_col = EElement.CRITERION;
		}
	}

	@Override
	public void initialize() {
		computeDepths();
		computeFixedValues();
		extractElements();
		super.initialize();
	}

	private void computeDepths() {
		_expertsDepth = 1;
		_alternativesDepth = 1;
		_criteriaDepth = 1;
	}

	private void computeFixedValues() {

		if (_row.equals(EElement.EXPERT)) {
			_fixedCols = _expertsDepth;
		} else if (_row.equals(EElement.ALTERNATIVE)) {
			_fixedCols = _alternativesDepth;
		} else {
			_fixedCols = _criteriaDepth;
		}

		if (_col.equals(EElement.EXPERT)) {
			_fixedRows = _expertsDepth;
		} else if (_col.equals(EElement.ALTERNATIVE)) {
			_fixedRows = _alternativesDepth;
		} else {
			_fixedRows = _criteriaDepth;
		}

	}

	private List<String> extractExperts(String expert, int c) {

		List<String> result = new LinkedList<String>();

		String index = _index.get(expert);
		if (index == null) {
			index = "E" + c; //$NON-NLS-1$
		} else {
			index += "." + c; //$NON-NLS-1$
		}
		_index.put(expert, index);

		result.add(expert);

		return result;
	}

	private List<String> extractCriteria(String criterion, int c) {

		List<String> result = new LinkedList<String>();

		String index = _index.get(criterion);
		if (index == null) {
			index = "C" + c; //$NON-NLS-1$
		} else {
			index += "." + c; //$NON-NLS-1$
		}
		_index.put(criterion, index);

		result.add(criterion);
		
		return result;
	}

	private void extractElements() {
		_index = new HashMap<String, String>();

		_finalExperts = new LinkedList<String>();
		int counter = 1;
		for (String expert : _experts) {
			_finalExperts.addAll(extractExperts(expert, counter++));
		}

		_finalAlternatives = new LinkedList<String>();
		counter = 1;
		for (String alternative : _alternatives) {
			_index.put(alternative, "A" + counter++); //$NON-NLS-1$
			_finalAlternatives.add(alternative);
		}

		counter = 1;
		_finalCriteria = new LinkedList<String>();
		for (String criterion : _criteria) {
			_finalCriteria.addAll(extractCriteria(criterion, counter++));
		}

		String expert;
		String alternative;
		String criterion;
		int col;
		int row;

		if (_row.equals(EElement.EXPERT)) {
			_rowHeaderMatrix = new String[_fixedCols][_fixedRows + _finalExperts.size()];
			for (int i = 0; i < _finalExperts.size(); i++) {
				expert = _finalExperts.get(i);
				col = _fixedCols - 1;
				row = _fixedRows + i;
				_rowHeaderMatrix[col][row] = expert;
			}
		} else if (_row.equals(EElement.ALTERNATIVE)) {
			_rowHeaderMatrix = new String[_fixedCols][_fixedRows + _finalAlternatives.size()];
			for (int i = 0; i < _finalAlternatives.size(); i++) {
				alternative = _finalAlternatives.get(i);
				col = _fixedCols - 1;
				row = _fixedRows + i;
				_rowHeaderMatrix[col][row] = alternative;
			}
		} else {
			_rowHeaderMatrix = new String[_fixedCols][_fixedRows + _finalCriteria.size()];
			for (int i = 0; i < _finalCriteria.size(); i++) {
				criterion = _finalCriteria.get(i);
				col = _fixedCols - 1;
				row = _fixedRows + i;
				_rowHeaderMatrix[col][row] = criterion;
			}
		}

		if (_col.equals(EElement.EXPERT)) {
			_colHeaderMatrix = new String[_fixedCols + _finalExperts.size()][_fixedRows];
			for (int i = 0; i < _finalExperts.size(); i++) {
				expert = _finalExperts.get(i);
				col = _fixedCols + i;
				row = _fixedRows - 1;
				_colHeaderMatrix[col][row] = expert;
			}
		} else if (_col.equals(EElement.ALTERNATIVE)) {
			_colHeaderMatrix = new String[_fixedCols + _finalAlternatives.size()][_fixedRows];
			for (int i = 0; i < _finalAlternatives.size(); i++) {
				alternative = _finalAlternatives.get(i);
				col = _fixedCols + i;
				row = _fixedRows - 1;
				_colHeaderMatrix[col][row] = alternative;
			}

		} else {
			_colHeaderMatrix = new String[_fixedCols + _finalCriteria.size()][_fixedRows];
			for (int i = 0; i < _finalCriteria.size(); i++) {
				criterion = _finalCriteria.get(i);
				col = _fixedCols + i;
				row = _fixedRows - 1;
				_colHeaderMatrix[col][row] = criterion;
			}
		}

		for (int i = 0; i < _fixedCols; i++) {
			for (int j = 0; j < _fixedRows; j++) {
				_colHeaderMatrix[i][j] = null;
				_rowHeaderMatrix[i][j] = null;
			}
		}
	}

	@Override
	public Object doGetContentAt(int col, int row) {

		if ((col < _fixedCols) && (row < _fixedRows)) {
			return ""; //$NON-NLS-1$

		} else {
			Object erg = null;
			String expert = null;
			String alternative = null;
			String criterion = null;

			try {
				if (col < _fixedCols) {
					erg = (getColumnWidth(col) > 80) ? _rowHeaderMatrix[col][row]
							: _index.get(_rowHeaderMatrix[col][row]);

				} else if (row < _fixedRows) {
					erg = (getColumnWidth(col) > 80) ? _colHeaderMatrix[col][row]
							: _index.get(_colHeaderMatrix[col][row]);

				} else {
					col = col - _fixedCols;
					row = row - _fixedRows;
					switch (_col) {
					case EXPERT:
						expert = _finalExperts.get(col);
						break;

					case ALTERNATIVE:
						alternative = _finalAlternatives.get(col);
						break;

					case CRITERION:
						criterion = _finalCriteria.get(col);
						break;
					}

					switch (_row) {
					case EXPERT:
						expert = _finalExperts.get(row);
						break;

					case ALTERNATIVE:
						alternative = _finalAlternatives.get(row);
						break;

					case CRITERION:
						criterion = _finalCriteria.get(row);
						break;
					}

					if (expert == null) {
						expert = (String) _element;
					} else if (alternative == null) {
						alternative = (String) _element;
					} else {
						criterion = (String) _element;
					}
					
					Map<Key, String> domainAssignment = _problem.getDomainAssignments();

					erg = domainAssignment.get(new Key(alternative, criterion));

					if (erg != null) {
						//erg = (getColumnWidth(col) > 80) ? ((Domain) erg).getId() : _domainIndex.getIndex((Domain) erg);
					} else {
						erg = ""; //$NON-NLS-1$
					}
				}
			} catch (Exception e) {
				erg = null;
			}

			return erg;
		}
	}

	public KTableCellEditor doGetCellEditor(int col, int row) {
		return null;
	}

	@Override
	public void doSetContentAt(int col, int row, Object value) {
	}

	@Override
	public int doGetRowCount() {
		switch (_row) {
		case EXPERT:
			return _finalExperts.size() + getFixedRowCount();

		case ALTERNATIVE:
			return _finalAlternatives.size() + getFixedRowCount();

		case CRITERION:
			return _finalCriteria.size() + getFixedRowCount();

		default:
			return 0;
		}
	}

	@Override
	public int doGetColumnCount() {
		switch (_col) {
		case EXPERT:
			return _finalExperts.size() + getFixedColumnCount();

		case ALTERNATIVE:
			return _finalAlternatives.size() + getFixedColumnCount();

		case CRITERION:
			return _finalCriteria.size() + getFixedColumnCount();

		default:
			return 0;
		}
	}

	@Override
	public int getFixedHeaderRowCount() {
		return _fixedRows;
	}

	@Override
	public int getFixedHeaderColumnCount() {
		return _fixedCols;
	}

	public int getFixedRowCount() {
		return _fixedRows;
	}

	public int getFixedColumnCount() {
		return _fixedCols;
	}

	@Override
	public int getFixedSelectableRowCount() {
		return 0;
	}

	@Override
	public int getFixedSelectableColumnCount() {
		return 0;
	}

	@Override
	public boolean isColumnResizable(int col) {
		return false;
	}

	@Override
	public int getInitialFirstRowHeight() {
		return 25;
	}

	@Override
	public boolean isRowResizable(int row) {
		return false;
	}

	@Override
	public int getRowHeightMinimum() {
		return 60;
	}

	@Override
	public boolean isFixedCell(int col, int row) {
		return true;
	};

	@Override
	public boolean isHeaderCell(int col, int row) {
		return isFixedCell(col, row);
	}

	@Override
	public KTableCellRenderer doGetCellRenderer(int col, int row) {
		if ((col < getFixedColumnCount()) || (row < getFixedRowCount())) {
			return _fixedRenderer;
		} else {
			return _fixedRenderersInTable;
		}
	}

	@Override
	public Point doBelongsToCell(int col, int row) {

		if ((col < _fixedCols) || (row < _fixedRows)) {
			if (col < _fixedCols) {
				if (col > 0) {
					if (_rowHeaderMatrix[col][row] == _rowHeaderMatrix[col - 1][row]) {
						return new Point(col - 1, row);
					}
				}
				if (row > 0) {
					if (_rowHeaderMatrix[col][row] == _rowHeaderMatrix[col][row - 1]) {
						return new Point(col, row - 1);
					}
				}
			}
			if (row < _fixedRows) {
				if (col > 0) {
					if (_colHeaderMatrix[col][row] == _colHeaderMatrix[col - 1][row]) {
						return new Point(col - 1, row);
					}
				}
				if (row > 0) {
					if (_colHeaderMatrix[col][row] == _colHeaderMatrix[col][row - 1]) {
						return new Point(col, row - 1);
					}
				}
			}
		}
		return new Point(col, row);
	}

	@Override
	public int getInitialColumnWidth(int column) {
		return 60;
	}

	@Override
	public int getInitialRowHeight(int arg0) {
		return 25;
	}

	@Override
	public String doGetTooltipAt(int col, int row) {

		if ((col < _fixedCols) && (row < _fixedRows)) {
			return ""; //$NON-NLS-1$
		} else if (col < _fixedCols) {
			return _rowHeaderMatrix[col][row];
		} else if (row < _fixedRows) {
			return _colHeaderMatrix[col][row];
		} else {
			String value = null;
			col = col - _fixedCols;
			row = row - _fixedRows;
			String expert = null;
			String alternative = null;
			String criterion = null;
			String domain;
			switch (_row) {
			case EXPERT:
				expert = _finalExperts.get(row);
				value = expert;
				break;

			case ALTERNATIVE:
				alternative = _finalAlternatives.get(row);
				value = alternative;
				break;

			case CRITERION:
				criterion = _finalCriteria.get(row);
				value = criterion;
				break;
			}

			value += "/"; //$NON-NLS-1$

			switch (_col) {
			case EXPERT:
				expert = _finalExperts.get(col);
				value += expert;
				break;

			case ALTERNATIVE:
				alternative = _finalAlternatives.get(col);
				value += alternative;
				break;

			case CRITERION:
				criterion = _finalCriteria.get(col);
				value += criterion;
				break;
			}

			if (_problem.isExpert(_element)) {
				expert = _element;
			} else if (_problem.isAlternative(_element)) {
				alternative = _element;
			} else {
				criterion = _element;
			}
			
			Map<Key, String> domainAssignment = _problem.getDomainAssignments();

			domain = domainAssignment.get(new Key(alternative, criterion));
			if (domain == null) {
				return value;
			} else {
				return value += " - " + domain; //$NON-NLS-1$
			}
		}

	}

}
