package flintstones.gathering.cloud.view;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flintstones.gathering.cloud.dao.DAOProblemAssignments;
import flintstones.gathering.cloud.dao.DAOValuations;
import flintstones.gathering.cloud.model.Domain;
import flintstones.gathering.cloud.model.Key;
import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.model.ProblemAssignment;
import flintstones.gathering.cloud.model.Valuation;
import flintstones.gathering.cloud.model.Valuations;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.wb.swt.SWTResourceManager;

public class SurveyView extends ViewPart {

	public static final String ID = "flintstones.gathering.cloud.view.surveyView";

	public static final Image CHECKED = AbstractUIPlugin
			.imageDescriptorFromPlugin("flintstones.gathering.cloud",
					"icons/checked.gif").createImage();
	public static final Image UNCHECKED = AbstractUIPlugin
			.imageDescriptorFromPlugin("flintstones.gathering.cloud",
					"icons/unchecked.gif").createImage();

	public static final String ALL = "* (todas las alternativas)";

	public static final String QSINGLE = "Single label";
	public static final String QATLEAST = "At least";
	public static final String QATMOST = "At most";
	public static final String QGREATERTHAN = "Greater than";
	public static final String QLOWERTHAN = "Lower than";
	public static final String QBETWEEN = "Between";

	public String[] empty = new String[] {};
	public String[] quantifiers = new String[] { QSINGLE, QATLEAST, QATMOST,
			QGREATERTHAN, QLOWERTHAN, QBETWEEN };
	public String[] labels_assessment = null;
	public String[] labels_greater_than_assessment = null;
	public String[] labels_lower_than_assessment = null;
	public String[] labels_greater_than_importance = null;
	public String[] labels_lower_than_importance = null;
	public String[] between_start_assessment = null;
	public String[] between_end_assessment = null;
	public String[] labels_importance = null;
	public String[] between_start_importance = null;
	public String[] between_end_importance = null;

	private int[][] domainPolygons_assessment = null;
	private int[][] domainPolygons_importance = null;
	private String value_first_assessment;
	private String value_second_assessment;
	private String value_third_assessment;
	private String value_first_importance;
	private String value_second_importance;
	private String value_third_importance;

	private Problem problem;
	private ProblemAssignment problemAssignment;
	private Button performButton;
	private TableElement[] elements_assessment;
	private TableElement[] elements_importance;
	private Button saveButton_linguistic_assessment;
	private Button saveButton_importance;
	private List<String> alternatives;

	private Composite parent;

	private ListViewer listViewer_first_assessment,
			listViewer_second_assessment, listViewer_third_assessment;
	private ListViewer listViewer_first_importance,
			listViewer_second_importance, listViewer_third_importance;

	private ISelectionChangedListener listViewer_first_listener_assessment,
			listViewer_second_listener_assessment,
			listViewer_third_listener_assessment;

	private ISelectionChangedListener listViewer_first_listener_importance,
			listViewer_second_listener_importance,
			listViewer_third_listener_importance;

	private int number_of_labels_assessment;
	private int number_of_labels_importance;

	private TableViewer viewer_assessment;
	private TableViewer viewer_importance;
	private Text text_assessment;
	private Text text_importance;

	private HesitantFuzzySet domain_assessment;
	private HesitantFuzzySet domain_importance;
	private Canvas canvas_assessment;
	private Canvas canvas_importance;

	private int domainWidth = 340;
	private int domainHeight = 178;

	private int[] selection_assessment;
	private int[] selection_importance;

	private GC gc_assessment = null;
	private GC gc_importance = null;

	private Composite booleanAssessmentPanel = null;
	private Composite linguisticAssessmentPanel = null;
	private Composite importanceContainer;
	private Composite assessmentsContainer;

	private Label booleanLabel;

	private String booleanValue;

	private Button saveButton_boolean_assessment;

	private Composite numericalAssessmentPanel = null;
	private Composite intervalNumericalAssessmentPanel = null;

	private String numericalValue;
	private String intervalNumericalValue;

	private Button saveButton_numerical_assessment;
	private Button saveButton_intervalNumerical_assessment;

	private Spinner numericalSpinner;
	private Spinner minNumericalSpinner;
	private Spinner maxNumericalSpinner;

	private Composite percentAssessmentPanel = null;

	private String percentValue;

	private Spinner percentSpinner;

	private Button saveButton_percent_assessment;

	private Combo projectFilterCombo;

	private FilterByProject assessmentsFilter;

	public SurveyView() {
	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		RWT.getUISession().setAttribute(ID, this);
		problem = (Problem) RWT.getUISession().getAttribute("valuation-problem");
		problemAssignment = (ProblemAssignment) RWT.getUISession().getAttribute("valuation-problem-assignment");
		setPartName((problem != null) ? "Valoraciones para el problema: " + problem.getId() : "Problema no seleccionado");

		setLabels();
		selection_assessment = new int[] { -1, -1 };
		selection_importance = new int[] { -1, -1 };
	}

	private void setLabels() {

		domain_importance = (HesitantFuzzySet) problem.getDomains().get("Importancia").getDomain();
		number_of_labels_importance = domain_importance.getLabels().size();
		labels_importance = new String[number_of_labels_importance];
		between_start_importance = new String[number_of_labels_importance - 1];
		labels_lower_than_importance = new String[number_of_labels_importance - 1];
		labels_greater_than_importance = new String[number_of_labels_importance - 1];
		for (int i = 0; i < number_of_labels_importance; i++) {
			labels_importance[i] = domain_importance.getLabel(i).getName();
			if (i < (number_of_labels_importance - 1)) {
				between_start_importance[i] = labels_importance[i];
				labels_greater_than_importance[i] = labels_importance[i];
			}
			if (i > 0) {
				labels_lower_than_importance[i - 1] = labels_importance[i];
			}
		}

		domain_assessment = (HesitantFuzzySet) problem.getDomains().get("Acuerdo").getDomain();
		number_of_labels_assessment = domain_assessment.getLabels().size();
		labels_assessment = new String[number_of_labels_assessment];
		between_start_assessment = new String[number_of_labels_assessment - 1];
		labels_lower_than_assessment = new String[number_of_labels_assessment - 1];
		labels_greater_than_assessment = new String[number_of_labels_assessment - 1];
		for (int i = 0; i < number_of_labels_assessment; i++) {
			labels_assessment[i] = domain_assessment.getLabel(i).getName();
			if (i < (number_of_labels_assessment - 1)) {
				between_start_assessment[i] = labels_assessment[i];
				labels_greater_than_assessment[i] = labels_assessment[i];
			}
			if (i > 0) {
				labels_lower_than_assessment[i - 1] = labels_assessment[i];
			}
		}
	}

	private void computeBetweenEndLabelsAssessment(String value) {
		boolean find = false;
		int pos = 0;
		do {
			if (!labels_assessment[pos].equals(value)) {
				pos++;
			} else {
				find = true;
			}
		} while (!find);

		between_end_assessment = new String[number_of_labels_assessment - pos
				- 1];
		for (int i = 0; i < between_end_assessment.length; i++) {
			between_end_assessment[i] = domain_assessment.getLabel(i + pos + 1)
					.getName();
		}
	}

	private void computeBetweenEndLabelsImportance(String value) {
		boolean find = false;
		int pos = 0;
		do {
			if (!labels_importance[pos].equals(value)) {
				pos++;
			} else {
				find = true;
			}
		} while (!find);

		between_end_importance = new String[number_of_labels_importance - pos
				- 1];
		for (int i = 0; i < between_end_importance.length; i++) {
			between_end_importance[i] = domain_importance.getLabel(i + pos + 1)
					.getName();
		}
	}

	class TableElement implements Comparable<TableElement> {
		Key key = null;
		Valuation valuation = null;

		public TableElement(Key key, Valuation valuation) {
			this.key = key;
			this.valuation = valuation;
		}

		public Key getKey() {
			return key;
		}

		public Valuation getValuation() {
			return valuation;
		}

		@Override
		public int compareTo(TableElement o) {
			int result = 0;
			if ((key != null) && (o != null)) {
				result = key.getAlternative().compareTo(
						o.getKey().getAlternative());
				if (result == 0) {
					result = key.getCriterion().compareTo(
							o.getKey().getCriterion());
				}
			}

			return result;
		}
	}

	@SuppressWarnings("serial")
	class ViewContentProviderAssessment implements IStructuredContentProvider {

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			List<TableElement> result = new LinkedList<TableElement>();
			ProblemAssignment pa = (ProblemAssignment) RWT.getUISession()
					.getAttribute("valuation-problem-assignment");
			Valuations valuations = pa.getValuations();
			Map<Key, Valuation> valuationsMap = valuations.getValuations();

			Set<String> a = new HashSet<String>();
			for (Key k : valuationsMap.keySet()) {
				if (!k.getAlternative().toLowerCase().equals("importancia")) {
					if (k.getCriterion().contains(":")) {
						result.add(new TableElement(k, valuationsMap.get(k)));
						if (k.getAlternative().contains("[")) {
							a.add(k.getAlternative().split("\\[")[0]);
						} else {
							a.add(k.getAlternative());
						}
					}
				}
			}

			alternatives = new LinkedList<>(a);
			Collections.sort(alternatives);
			alternatives.add(0, ALL);

			Collections.sort(result);
			elements_assessment = result.toArray(new TableElement[0]);
			validate();
			return elements_assessment;
		}
	}

	@SuppressWarnings("serial")
	class ViewContentProviderImportance implements IStructuredContentProvider {

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			List<TableElement> result = new LinkedList<TableElement>();
			ProblemAssignment pa = (ProblemAssignment) RWT.getUISession()
					.getAttribute("valuation-problem-assignment");
			Valuations valuations = pa.getValuations();
			Map<Key, Valuation> valuationsMap = valuations.getValuations();

			for (Key k : valuationsMap.keySet()) {
				if (k.getAlternative().toLowerCase().equals("importancia")) {
					if (!k.getCriterion().startsWith("*")) {
						result.add(new TableElement(k, valuationsMap.get(k)));
					}
				}
			}

			Collections.sort(result);
			elements_importance = result.toArray(new TableElement[0]);
			validate();
			return elements_importance;
		}
	}

	@SuppressWarnings("serial")
	class AlternativeLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			String result = ((TableElement) obj).key.getAlternative();
			if (result.contains("[")) {
				result = result.split("\\[")[0];
			}
			return result;
		}

		@Override
		public String getToolTipText(Object obj) {

			String result = null;

			String main = null;
			String des = null;

			String alternative = ((TableElement) obj).key.getAlternative();
			if (alternative.contains("[")) {
				String[] tokens = alternative.split("\\[");
				main = tokens[0];
				des = tokens[1].substring(0, tokens[1].length() - 1);
			} else {
				main = alternative;
			}

			result = "Project '" + main + "'";

			if (des != null) {
				StringBuilder d = new StringBuilder(des);
				int pos = 105;
				int newPos;
				while (pos < d.length()) {
					newPos = d.indexOf(" ", pos);
					if (newPos == -1) {
						pos = d.length();
					} else {
						pos = newPos;
						d.insert(newPos, "\n");
						pos += 105;
					}
				}
				result += "\n\n" + d.toString();
			}
			return result;
		}

		@Override
		public Point getToolTipShift(Object object) {
			return new Point(5, 5);
		}

		@Override
		public int getToolTipDisplayDelayTime(Object object) {
			return 1000;
		}

		@Override
		public int getToolTipTimeDisplayed(Object object) {
			return 5000;
		}
	}

	@SuppressWarnings("serial")
	class CriterionLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {
			String result = ((TableElement) obj).key.getCriterion();
			if (result.contains("[")) {
				result = result.split("\\[")[0];
			}
			if (result.contains(":")) {
				String[] tokens = result.split(":");
				result = tokens[0].substring(0, 2) + "." + tokens[1];
			}
			if (result != null) {
				result = result.replace("R and D", "R&D");
			}
			return result;
		}

		@Override
		public String getToolTipText(Object obj) {

			String result = null;

			String main = null;
			String sub = null;
			String des = null;

			String criterion = ((TableElement) obj).key.getCriterion();
			if (criterion.contains("[")) {
				String[] tokens = criterion.split("\\[");
				main = tokens[0];
				des = tokens[1].substring(0, tokens[1].length() - 1);
			} else {
				main = criterion;
			}

			if (main.contains(":")) {
				String[] tokens = main.split(":");
				main = tokens[0];
				sub = tokens[1];
				result = "Criterios principales " + main + " - Sub-criterio " + sub;
			} else {
				result = "Criterios principales " + main;
			}

			if (des != null) {
				result += "\n\n" + des;
			}

			if (result != null) {
				result = result.replace("R and D", "R&D");
			}
			return result;
		}

		@Override
		public Point getToolTipShift(Object object) {
			return new Point(5, 5);
		}

		@Override
		public int getToolTipDisplayDelayTime(Object object) {
			return 1000;
		}

		@Override
		public int getToolTipTimeDisplayed(Object object) {
			return 5000;
		}
	}

	@SuppressWarnings("serial")
	class ValuationLabelProvider extends ColumnLabelProvider {
		public String getText(Object obj) {

			Key key = ((TableElement) obj).key;
			if (!key.getAlternative().toLowerCase().equals("importancia")) {
				String d = problem.getDomainAssignments().get(key);
				if (d != null) {
					Domain domain = problem.getDomains().get(d);
					if (domain.getDomain() instanceof NumericDomain) {
						NumericDomain numericDomain = (NumericDomain) domain.getDomain();
						String stringValue = ((TableElement) obj).valuation.getValue();
						if (stringValue == null) {
							return "";
						} else if (stringValue.isEmpty()) {
							return "";
						}
						
						Integer value = Integer.parseInt(stringValue);
						
						// FLOAT
						if (numericDomain.getType() == 0) {
							return Float.toString((new Float(value)) / 100f);
						} else {
							return Integer.toString(value);
						}
					} else if (domain.getDomain() instanceof IntervalNumericDomain) {
						IntervalNumericDomain intervalNumericDomain = (IntervalNumericDomain) domain.getDomain();
						String stringValue = ((TableElement) obj).valuation.getValue();
						if (stringValue == null) {
							return "";
						} else if (stringValue.isEmpty()) {
							return "";
						}
						
						String[] tokens = stringValue.split(", ");
						Integer value1 = Integer.parseInt(tokens[0].substring(1));
						Integer value2 = Integer.parseInt(tokens[1].substring(0, tokens[1].length() - 1));
						
						// FLOAT
						if (intervalNumericDomain.getType() == 0) {
							return "[" + Float.toString((new Float(value1)) / 100f) + ", " + Float.toString((new Float(value2)) / 100f) + "]";
						} else {
							return "[" + Integer.toString(value1) + ", " + Integer.toString(value2) + "]";
						}
					}
					if (d.equals("boolean alternative assessment")) {
						if (((TableElement) obj).valuation.getValue().equals(
								"1")) {
							return "yes";
						} else {
							if (((TableElement) obj).valuation.getValue()
									.isEmpty()) {
								return "";
							} else {
								return "no";
							}
						}
					} else if (d.equals("percent alternative assessment")) {
						if (!((TableElement) obj).valuation.getValue()
								.isEmpty()) {
							return ((TableElement) obj).valuation.getValue()
									+ "%";
						}
					}
				}
			}

			return ((TableElement) obj).valuation.getValue();
		}

		@Override
		public Image getImage(Object obj) {
			Key key = ((TableElement) obj).key;
			if (!key.getAlternative().toLowerCase().equals("importancia")) {
				String d = problem.getDomainAssignments().get(key);
				if (d != null) {
					if (d.equals("boolean alternative assessment")) {
						if (((TableElement) obj).valuation.getValue().equals(
								"1")) {
							// return CHECKED;
							return null;
						} else {
							if (((TableElement) obj).valuation.getValue()
									.isEmpty()) {
								return null;
							} else {
								// return UNCHECKED;
								return null;
							}
						}
					}
				}
			}

			return null;
		}
	}

	@SuppressWarnings("serial")
	public class FilterByProject extends ViewerFilter {

		private String searchString;

		public void setSearchText(String s) {
			searchString = s;
		}

		@Override
		public boolean select(Viewer viewer, Object parentElement,
				Object element) {
			if (searchString == null || searchString.length() == 0) {
				return true;
			}

			TableElement te = (TableElement) element;
			if (ALL.equals(searchString)) {
				return true;
			} else if (te.getKey().getAlternative().startsWith(searchString)) {
				return true;
			} else {
				return false;
			}
		}
	}

	private void createImportanceTable(Composite container) {
		viewer_importance = new TableViewer(container, SWT.SINGLE
				| SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		viewer_importance
				.setContentProvider(new ViewContentProviderImportance());

		Table table = viewer_importance.getTable();
		GridData gd = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd.heightHint = 170;
		table.setLayoutData(gd);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableViewerColumn tvc = new TableViewerColumn(viewer_importance,
				SWT.NONE);
		TableColumn tc = tvc.getColumn();
		tc.setText("Criterio");
		tc.setImage(AbstractUIPlugin.imageDescriptorFromPlugin(
				"flintstones.gathering.cloud", "/icons/criterion_20.png")
				.createImage());
		tvc.setLabelProvider(new CriterionLabelProvider());
		ColumnViewerToolTipSupport.enableFor(viewer_importance);

		tvc = new TableViewerColumn(viewer_importance, SWT.NONE);
		tc = tvc.getColumn();
		tc.setText("Importancia");
		tc.setImage(AbstractUIPlugin.imageDescriptorFromPlugin(
				"flintstones.gathering.cloud", "/icons/assessment_20.png")
				.createImage());
		tvc.setLabelProvider(new ValuationLabelProvider());
	}

	@SuppressWarnings("serial")
	private void createAssessmentsTable(Composite container) {
		projectFilterCombo = new Combo(container, SWT.BORDER);
		projectFilterCombo
				.setToolTipText("Seleccionar una alternativa para filtrar el contenido de la tabla");

		GridData gd = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		projectFilterCombo.setLayoutData(gd);
		projectFilterCombo
				.setText("Seleccionar una alternativa para filtrar el contenido de la tabla");

		viewer_assessment = new TableViewer(container, SWT.SINGLE
				| SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		viewer_assessment
				.setContentProvider(new ViewContentProviderAssessment());

		Table table = viewer_assessment.getTable();
		gd = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd.heightHint = 145;
		table.setLayoutData(gd);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableViewerColumn tvc = new TableViewerColumn(viewer_assessment,
				SWT.NONE);
		TableColumn tc = tvc.getColumn();
		tc.setText("Alternativa");
		tc.setImage(AbstractUIPlugin.imageDescriptorFromPlugin(
				"flintstones.gathering.cloud", "/icons/alternative_20.png")
				.createImage());
		tvc.setLabelProvider(new AlternativeLabelProvider());

		tvc = new TableViewerColumn(viewer_assessment, SWT.NONE);
		tc = tvc.getColumn();
		tc.setText("Criterio");
		tc.setImage(AbstractUIPlugin.imageDescriptorFromPlugin(
				"flintstones.gathering.cloud", "/icons/criterion_20.png")
				.createImage());
		tvc.setLabelProvider(new CriterionLabelProvider());
		ColumnViewerToolTipSupport.enableFor(viewer_assessment);

		tvc = new TableViewerColumn(viewer_assessment, SWT.NONE);
		tc = tvc.getColumn();
		tc.setText("Valoración");
		tc.setImage(AbstractUIPlugin.imageDescriptorFromPlugin(
				"flintstones.gathering.cloud", "/icons/assessment_20.png")
				.createImage());
		tvc.setLabelProvider(new ValuationLabelProvider());

		projectFilterCombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				assessmentsFilter.setSearchText(projectFilterCombo.getText());
				viewer_assessment.refresh();
			}
		});
		assessmentsFilter = new FilterByProject();
		viewer_assessment.addFilter(assessmentsFilter);
	}

	@SuppressWarnings("serial")
	private void createImportancePanel(Composite parent) {

		importanceContainer = new Composite(parent, SWT.NONE);
		GridLayout gl_importanceContainer = new GridLayout(2, true);
		gl_importanceContainer.marginWidth = 0;
		gl_importanceContainer.marginRight = 0;
		gl_importanceContainer.marginLeft = 0;
		gl_importanceContainer.marginTop = 7;
		importanceContainer.setLayout(gl_importanceContainer);
		importanceContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1));

		Composite valuationComposite = new Composite(importanceContainer,
				SWT.NONE);
		valuationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		valuationComposite.setLayout(new GridLayout(3, true));

		text_importance = new Text(valuationComposite, SWT.BORDER);
		text_importance.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 3, 1));
		text_importance.setEditable(false);

		listViewer_first_importance = new ListViewer(valuationComposite,
				SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		org.eclipse.swt.widgets.List list = listViewer_first_importance
				.getList();
		list.setFont(SWTResourceManager.getFont("Cantarell", 11, SWT.NORMAL));
		GridData gd_list = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_list.heightHint = 147;
		list.setLayoutData(gd_list);
		listViewer_first_importance
				.setContentProvider(new ArrayContentProvider());

		listViewer_second_importance = new ListViewer(valuationComposite,
				SWT.BORDER | SWT.V_SCROLL);
		org.eclipse.swt.widgets.List list_1 = listViewer_second_importance
				.getList();
		list_1.setFont(SWTResourceManager.getFont("Cantarell", 11, SWT.NORMAL));
		GridData gd_list_1 = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_list_1.heightHint = 147;
		list_1.setLayoutData(gd_list_1);
		listViewer_second_importance
				.setContentProvider(new ArrayContentProvider());

		listViewer_third_importance = new ListViewer(valuationComposite,
				SWT.BORDER | SWT.V_SCROLL);
		org.eclipse.swt.widgets.List list_2 = listViewer_third_importance
				.getList();
		list_2.setFont(SWTResourceManager.getFont("Cantarell", 11, SWT.NORMAL));
		GridData gd_list_2 = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_list_2.heightHint = 147;
		list_2.setLayoutData(gd_list_2);
		listViewer_third_importance
				.setContentProvider(new ArrayContentProvider());

		saveButton_importance = new Button(valuationComposite, SWT.PUSH
				| SWT.BORDER);
		saveButton_importance.setLayoutData(new GridData(SWT.CENTER, SWT.TOP,
				true, false, 3, 1));
		saveButton_importance.setImage(AbstractUIPlugin
				.imageDescriptorFromPlugin("flintstones.gathering.cloud",
						"/icons/save_20.png").createImage());
		saveButton_importance.setEnabled(false);
		saveButton_importance.pack();
		saveButton_importance.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				StructuredSelection selection = (StructuredSelection) viewer_importance.getSelection();
				TableElement te = (TableElement) selection.getFirstElement();
				te.valuation.setValue(text_importance.getText());
				viewer_importance.update(te, null);
				packColumnsImportance();

				DAOValuations.getDAO().setValuation(problem, problemAssignment,
						te.key, te.valuation);

				validate();

			}
		});

		Control[] c = new Control[] { list, list_1, list_2,
				saveButton_importance };
		valuationComposite.setTabList(c);

		canvas_importance = new Canvas(importanceContainer, SWT.NONE);
		GridData gd = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		gd.heightHint = domainHeight;
		gd.widthHint = domainWidth;
		canvas_importance.setLayoutData(gd);
		canvas_importance.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				gc_importance = e.gc;
				paintSelectionImportance();
			}
		});
	}

	@SuppressWarnings("serial")
	private void createLinguisticAssessmentPanel(Composite parent) {

		linguisticAssessmentPanel = new Composite(parent, SWT.NONE);
		GridLayout gl_assessmentContainer = new GridLayout(2, true);
		gl_assessmentContainer.marginWidth = 0;
		gl_assessmentContainer.marginRight = 0;
		gl_assessmentContainer.marginLeft = 0;
		gl_assessmentContainer.marginTop = 7;
		linguisticAssessmentPanel.setLayout(gl_assessmentContainer);
		linguisticAssessmentPanel.setLayoutData(new GridData(SWT.FILL,
				SWT.FILL, true, true, 1, 1));

		Composite valuationComposite = new Composite(linguisticAssessmentPanel,
				SWT.NONE);
		valuationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		valuationComposite.setLayout(new GridLayout(3, true));

		text_assessment = new Text(valuationComposite, SWT.BORDER);
		text_assessment.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 3, 1));
		text_assessment.setEditable(false);

		listViewer_first_assessment = new ListViewer(valuationComposite,
				SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		org.eclipse.swt.widgets.List list = listViewer_first_assessment
				.getList();
		list.setFont(SWTResourceManager.getFont("Cantarell", 11, SWT.NORMAL));
		GridData gd_list = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_list.heightHint = 147;
		list.setLayoutData(gd_list);
		listViewer_first_assessment
				.setContentProvider(new ArrayContentProvider());

		listViewer_second_assessment = new ListViewer(valuationComposite,
				SWT.BORDER | SWT.V_SCROLL);
		org.eclipse.swt.widgets.List list_1 = listViewer_second_assessment
				.getList();
		list_1.setFont(SWTResourceManager.getFont("Cantarell", 11, SWT.NORMAL));
		GridData gd_list_1 = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_list_1.heightHint = 147;
		list_1.setLayoutData(gd_list_1);
		listViewer_second_assessment
				.setContentProvider(new ArrayContentProvider());

		listViewer_third_assessment = new ListViewer(valuationComposite,
				SWT.BORDER | SWT.V_SCROLL);
		org.eclipse.swt.widgets.List list_2 = listViewer_third_assessment
				.getList();
		list_2.setFont(SWTResourceManager.getFont("Cantarell", 11, SWT.NORMAL));
		GridData gd_list_2 = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_list_2.heightHint = 147;
		list_2.setLayoutData(gd_list_2);
		listViewer_third_assessment
				.setContentProvider(new ArrayContentProvider());

		saveButton_linguistic_assessment = new Button(valuationComposite,
				SWT.PUSH | SWT.BORDER);
		saveButton_linguistic_assessment.setLayoutData(new GridData(SWT.CENTER,
				SWT.TOP, true, false, 3, 1));
		saveButton_linguistic_assessment.setImage(AbstractUIPlugin
				.imageDescriptorFromPlugin("flintstones.gathering.cloud",
						"/icons/save_20.png").createImage());
		saveButton_linguistic_assessment.setEnabled(false);
		saveButton_linguistic_assessment.pack();
		saveButton_linguistic_assessment
				.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						StructuredSelection selection = (StructuredSelection) viewer_assessment.getSelection();
						TableElement te = (TableElement) selection.getFirstElement();
						te.valuation.setValue(text_assessment.getText());
						viewer_assessment.update(te, null);
						packColumnsAssessment();

						DAOValuations.getDAO().setValuation(problem,
								problemAssignment, te.key, te.valuation);

						validate();

					}
				});

		Control[] c = new Control[] { list, list_1, list_2,
				saveButton_linguistic_assessment };
		valuationComposite.setTabList(c);

		canvas_assessment = new Canvas(linguisticAssessmentPanel, SWT.NONE);
		GridData gd = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		gd.heightHint = domainHeight;
		gd.widthHint = domainWidth;
		canvas_assessment.setLayoutData(gd);
		canvas_assessment.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				gc_assessment = e.gc;
				paintSelectionAssessment();
			}
		});

		listViewer_first_assessment
				.addSelectionChangedListener(listViewer_first_listener_assessment);
		listViewer_second_assessment
				.addSelectionChangedListener(listViewer_second_listener_assessment);
		listViewer_third_assessment
				.addSelectionChangedListener(listViewer_third_listener_assessment);
	}

	@SuppressWarnings("serial")
	private void createBooleanAssessmentPanel(Composite parent) {

		booleanAssessmentPanel = new Composite(parent, SWT.NONE);
		GridLayout gl_booleanAssessmentPanel = new GridLayout(1, true);
		gl_booleanAssessmentPanel.marginWidth = 0;
		gl_booleanAssessmentPanel.marginRight = 0;
		gl_booleanAssessmentPanel.marginLeft = 0;
		gl_booleanAssessmentPanel.marginTop = 7;
		booleanAssessmentPanel.setLayout(gl_booleanAssessmentPanel);
		booleanAssessmentPanel.setLayoutData(new GridData(SWT.CENTER,
				SWT.CENTER, true, true, 1, 1));

		StructuredSelection selection = (StructuredSelection) viewer_assessment.getSelection();
		TableElement te = (TableElement) selection.getFirstElement();
		booleanValue = te.getValuation().getValue();

		if (booleanValue == null) {
			booleanValue = "0";
		} else if (booleanValue.isEmpty()) {
			booleanValue = "0";
		}

		booleanLabel = new Label(booleanAssessmentPanel, SWT.NONE);
		booleanLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				false, 1, 1));
		if (booleanValue.equals("1")) {
			booleanLabel.setImage(AbstractUIPlugin.imageDescriptorFromPlugin(
					"flintstones.gathering.cloud", "/icons/yes_30.png")
					.createImage());
		} else {
			booleanLabel.setImage(AbstractUIPlugin.imageDescriptorFromPlugin(
					"flintstones.gathering.cloud", "/icons/no_30.png")
					.createImage());
		}
		booleanLabel.setCursor(new org.eclipse.swt.graphics.Cursor(booleanLabel
				.getDisplay(), SWT.CURSOR_HAND));
		booleanLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {

				if (booleanValue.equals("1")) {
					booleanValue = "0";
					booleanLabel.setImage(AbstractUIPlugin
							.imageDescriptorFromPlugin(
									"flintstones.gathering.cloud",
									"/icons/no_30.png").createImage());
				} else {
					booleanValue = "1";
					booleanLabel.setImage(AbstractUIPlugin
							.imageDescriptorFromPlugin(
									"flintstones.gathering.cloud",
									"/icons/yes_30.png").createImage());
				}
			}
		});

		saveButton_boolean_assessment = new Button(booleanAssessmentPanel,
				SWT.PUSH | SWT.BORDER);
		saveButton_boolean_assessment.setLayoutData(new GridData(SWT.CENTER,
				SWT.TOP, true, false, 1, 1));
		saveButton_boolean_assessment.setImage(AbstractUIPlugin
				.imageDescriptorFromPlugin("flintstones.gathering.cloud",
						"/icons/save_20.png").createImage());
		saveButton_boolean_assessment.setEnabled(true);
		saveButton_boolean_assessment.pack();
		saveButton_boolean_assessment
				.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						StructuredSelection selection = (StructuredSelection) viewer_assessment.getSelection();
						TableElement te = (TableElement) selection.getFirstElement();
						te.valuation.setValue(booleanValue);
						viewer_assessment.update(te, null);
						packColumnsAssessment();

						DAOValuations.getDAO().setValuation(problem,
								problemAssignment, te.key, te.valuation);

						validate();

					}
				});
	}

	@SuppressWarnings("serial")
	private void createNumericalAssessmentPanel(NumericDomain domain, Composite parent) {

		numericalAssessmentPanel = new Composite(parent, SWT.NONE);
		GridLayout gl_numericalAssessmentPanel = new GridLayout(1, true);
		gl_numericalAssessmentPanel.marginWidth = 0;
		gl_numericalAssessmentPanel.marginRight = 0;
		gl_numericalAssessmentPanel.marginLeft = 0;
		gl_numericalAssessmentPanel.marginTop = 7;
		numericalAssessmentPanel.setLayout(gl_numericalAssessmentPanel);
		numericalAssessmentPanel.setLayoutData(new GridData(SWT.CENTER,
				SWT.CENTER, true, true, 1, 1));

		StructuredSelection selection = (StructuredSelection) viewer_assessment.getSelection();
		TableElement te = (TableElement) selection.getFirstElement();
		numericalValue = te.getValuation().getValue();

		numericalSpinner = new Spinner(numericalAssessmentPanel, SWT.BORDER);
		GridData gd = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd.widthHint = 50;
		numericalSpinner.setLayoutData(gd);
		Double min = domain.getMin();
		Double max = domain.getMax();

		// FLOAT
		if (domain.getType() == 0) {
			if (numericalValue == null) {
				numericalValue = Integer.toString(min.intValue() * 100);
			} else if (numericalValue.isEmpty()) {
				numericalValue = Integer.toString(min.intValue() * 100);
			}
			
			numericalSpinner.setValues(Integer.parseInt(numericalValue), min.intValue() * 100, max.intValue() * 100, 2,
					1, 1);
			
		// INTEGER
		} else {
			if (numericalValue == null) {
				numericalValue = Integer.toString(min.intValue());
			} else if (numericalValue.isEmpty()) {
				numericalValue = Integer.toString(min.intValue());
			}
			
			numericalSpinner.setValues(Integer.parseInt(numericalValue), min.intValue(), max.intValue(), 0,
					1, 1);
		}
		
		numericalSpinner.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent event) {
				if (numericalSpinner.getDigits() == 0) {
					numericalValue = numericalSpinner.getText();
				} else {
					numericalValue = Integer.toString(new Float(Float.parseFloat(numericalSpinner.getText().replace(",",".")) * 100).intValue());
				}
			}
		});

		saveButton_numerical_assessment = new Button(numericalAssessmentPanel,
				SWT.PUSH | SWT.BORDER);
		saveButton_numerical_assessment.setLayoutData(new GridData(SWT.CENTER,
				SWT.TOP, true, false, 1, 1));
		saveButton_numerical_assessment.setImage(AbstractUIPlugin
				.imageDescriptorFromPlugin("flintstones.gathering.cloud",
						"/icons/save_20.png").createImage());
		saveButton_numerical_assessment.setEnabled(true);
		saveButton_numerical_assessment.pack();
		saveButton_numerical_assessment
				.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						StructuredSelection selection = (StructuredSelection) viewer_assessment.getSelection();
						TableElement te = (TableElement) selection.getFirstElement();
						te.valuation.setValue(numericalValue);
						viewer_assessment.update(te, null);
						packColumnsAssessment();

						DAOValuations.getDAO().setValuation(problem,
								problemAssignment, te.key, te.valuation);

						validate();

					}
				});
	}

	@SuppressWarnings("serial")
	private void createIntervalNumericalAssessmentPanel(IntervalNumericDomain domain, Composite parent) {

		intervalNumericalAssessmentPanel = new Composite(parent, SWT.NONE);
		GridLayout gl_intervalNumericalAssessmentPanel = new GridLayout(1, true);
		gl_intervalNumericalAssessmentPanel.marginWidth = 0;
		gl_intervalNumericalAssessmentPanel.marginRight = 0;
		gl_intervalNumericalAssessmentPanel.marginLeft = 0;
		gl_intervalNumericalAssessmentPanel.marginTop = 7;
		intervalNumericalAssessmentPanel.setLayout(gl_intervalNumericalAssessmentPanel);
		intervalNumericalAssessmentPanel.setLayoutData(new GridData(SWT.CENTER,
				SWT.CENTER, true, true, 1, 1));

		StructuredSelection selection = (StructuredSelection) viewer_assessment.getSelection();
		TableElement te = (TableElement) selection.getFirstElement();
		intervalNumericalValue = te.getValuation().getValue();

		minNumericalSpinner = new Spinner(intervalNumericalAssessmentPanel, SWT.BORDER);
		GridData gd = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd.widthHint = 50;
		minNumericalSpinner.setLayoutData(gd);
		maxNumericalSpinner = new Spinner(intervalNumericalAssessmentPanel, SWT.BORDER);
		gd = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd.widthHint = 50;
		maxNumericalSpinner.setLayoutData(gd);
		Double min = domain.getMin();
		Double max = domain.getMax();

		// FLOAT
		if (domain.getType() == 0) {
			String lv = null;
			String uv = null;
			if (intervalNumericalValue == null) {
				lv = Integer.toString(min.intValue() * 100);
				uv = Integer.toString(min.intValue() * 100);
			} else if (intervalNumericalValue.isEmpty()) {
				lv = Integer.toString(min.intValue() * 100);
				uv = Integer.toString(min.intValue() * 100);
			} else {
				String[] tokens = intervalNumericalValue.split(", ");
				lv = tokens[0].substring(1);
				uv = tokens[1].substring(0, tokens[1].length() - 1);
			}
			
			intervalNumericalValue = "[" + lv + ", " + uv + "]";
						
			minNumericalSpinner.setValues(Integer.parseInt(lv), min.intValue() * 100, Integer.parseInt(uv), 2,
					1, 1);
			maxNumericalSpinner.setValues(Integer.parseInt(uv), Integer.parseInt(lv), max.intValue() * 100, 2,
					1, 1);
			
		// INTEGER
		} else {
			if (intervalNumericalValue == null) {
				intervalNumericalValue = Integer.toString(min.intValue());
			} else if (intervalNumericalValue.isEmpty()) {
				intervalNumericalValue = Integer.toString(min.intValue());
			}
			
			minNumericalSpinner.setValues(Integer.parseInt(intervalNumericalValue), min.intValue(), max.intValue(), 0,
					1, 1);
			
			String lv = null;
			String uv = null;
			if (intervalNumericalValue == null) {
				lv = Integer.toString(min.intValue());
				uv = Integer.toString(min.intValue());
			} else if (intervalNumericalValue.isEmpty()) {
				lv = Integer.toString(min.intValue());
				uv = Integer.toString(min.intValue());
			} else {
				String[] tokens = intervalNumericalValue.split(", ");
				lv = tokens[0].substring(1);
				uv = tokens[1].substring(0, tokens[1].length() - 1);
			}
			
			intervalNumericalValue = "[" + lv + ", " + uv + "]";
						
			minNumericalSpinner.setValues(Integer.parseInt(lv), min.intValue(), Integer.parseInt(uv), 2,
					1, 1);
			maxNumericalSpinner.setValues(Integer.parseInt(uv), Integer.parseInt(lv), max.intValue(), 2,
					1, 1);
		}
		
		minNumericalSpinner.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent event) {
				String lv = null;
				String uv = null;
				if (minNumericalSpinner.getDigits() == 0) {
					lv = minNumericalSpinner.getText();
				} else {
					lv = Integer.toString(new Float(Float.parseFloat(minNumericalSpinner.getText().replace(",",".")) * 100).intValue());
				}
				if (maxNumericalSpinner.getDigits() == 0) {
					uv = maxNumericalSpinner.getText();
				} else {
					uv = Integer.toString(new Float(Float.parseFloat(maxNumericalSpinner.getText().replace(",",".")) * 100).intValue());
				}
				intervalNumericalValue = "[" + lv + ", " + uv + "]";
				maxNumericalSpinner.setMinimum(Integer.parseInt(lv));
			}
		});
		
		maxNumericalSpinner.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent event) {
				String lv = null;
				String uv = null;
				if (minNumericalSpinner.getDigits() == 0) {
					lv = minNumericalSpinner.getText();
				} else {
					lv = Integer.toString(new Float(Float.parseFloat(minNumericalSpinner.getText().replace(",",".")) * 100).intValue());
				}
				if (maxNumericalSpinner.getDigits() == 0) {
					uv = maxNumericalSpinner.getText();
				} else {
					uv = Integer.toString(new Float(Float.parseFloat(maxNumericalSpinner.getText().replace(",",".")) * 100).intValue());
				}
				intervalNumericalValue = "[" + lv + ", " + uv + "]";
				minNumericalSpinner.setMaximum(Integer.parseInt(uv));
			}
		});

		saveButton_intervalNumerical_assessment = new Button(intervalNumericalAssessmentPanel,
				SWT.PUSH | SWT.BORDER);
		saveButton_intervalNumerical_assessment.setLayoutData(new GridData(SWT.CENTER,
				SWT.TOP, true, false, 1, 1));
		saveButton_intervalNumerical_assessment.setImage(AbstractUIPlugin
				.imageDescriptorFromPlugin("flintstones.gathering.cloud",
						"/icons/save_20.png").createImage());
		saveButton_intervalNumerical_assessment.setEnabled(true);
		saveButton_intervalNumerical_assessment.pack();
		saveButton_intervalNumerical_assessment
				.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						StructuredSelection selection = (StructuredSelection) viewer_assessment.getSelection();
						TableElement te = (TableElement) selection.getFirstElement();
						te.valuation.setValue(intervalNumericalValue);
						viewer_assessment.update(te, null);
						packColumnsAssessment();

						DAOValuations.getDAO().setValuation(problem,
								problemAssignment, te.key, te.valuation);

						validate();

					}
				});
	}

	@SuppressWarnings("serial")
	private void createPercentAssessmentPanel(Composite parent) {

		percentAssessmentPanel = new Composite(parent, SWT.NONE);
		GridLayout gl_percentAssessmentPanel = new GridLayout(2, true);
		gl_percentAssessmentPanel.marginWidth = 0;
		gl_percentAssessmentPanel.marginRight = 0;
		gl_percentAssessmentPanel.marginLeft = 0;
		gl_percentAssessmentPanel.marginTop = 7;
		percentAssessmentPanel.setLayout(gl_percentAssessmentPanel);
		percentAssessmentPanel.setLayoutData(new GridData(SWT.CENTER,
				SWT.CENTER, true, true, 1, 1));

		StructuredSelection selection = (StructuredSelection) viewer_assessment.getSelection();
		TableElement te = (TableElement) selection.getFirstElement();
		percentValue = te.getValuation().getValue();

		if (percentValue == null) {
			percentValue = "0";
		} else if (percentValue.isEmpty()) {
			percentValue = "0";
		}
		percentValue = percentValue.replace(",", ".");

		percentSpinner = new Spinner(percentAssessmentPanel, SWT.BORDER);
		GridData gd = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd.widthHint = 50;
		percentSpinner.setLayoutData(gd);
		percentSpinner.setValues((int) (Float.parseFloat(percentValue) * 100f),
				0, 10000, 2, 1, 1);
		percentSpinner.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent event) {
				percentValue = percentSpinner.getText().replace(",", ".");
			}
		});

		Label label = new Label(percentAssessmentPanel, SWT.NONE);
		label.setText("%");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1,
				1));

		saveButton_percent_assessment = new Button(percentAssessmentPanel,
				SWT.PUSH | SWT.BORDER);
		saveButton_percent_assessment.setLayoutData(new GridData(SWT.CENTER,
				SWT.TOP, true, false, 2, 1));
		saveButton_percent_assessment.setImage(AbstractUIPlugin
				.imageDescriptorFromPlugin("flintstones.gathering.cloud",
						"/icons/save_20.png").createImage());
		saveButton_percent_assessment.setEnabled(true);
		saveButton_percent_assessment.pack();
		saveButton_percent_assessment
				.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						StructuredSelection selection = (StructuredSelection) viewer_assessment.getSelection();
						TableElement te = (TableElement) selection.getFirstElement();
						te.valuation.setValue(percentValue.replace(".", ","));
						viewer_assessment.update(te, null);
						packColumnsAssessment();

						DAOValuations.getDAO().setValuation(problem,
								problemAssignment, te.key, te.valuation);

						validate();

					}
				});
	}

	private void createAssessmentTab(TabFolder folder) {
		TabItem assessmentTab = new TabItem(folder, SWT.NONE);
		assessmentTab.setText("Valoraciones");

		assessmentsContainer = new Composite(folder, SWT.NONE);
		GridLayout gl_assessmentsContainer = new GridLayout(1, false);
		gl_assessmentsContainer.marginWidth = 0;
		gl_assessmentsContainer.marginHeight = 0;
		assessmentsContainer.setLayout(gl_assessmentsContainer);

		createAssessmentsTable(assessmentsContainer);
		assessmentTab.setControl(assessmentsContainer);
	}

	private void createImportanceTab(TabFolder folder) {
		TabItem importanceTab = new TabItem(folder, SWT.NONE);
		importanceTab.setText("Importancias");

		Composite importanceContainter = new Composite(folder, SWT.NONE);
		GridLayout gl_importanceContainer = new GridLayout(1, false);
		gl_importanceContainer.marginWidth = 0;
		gl_importanceContainer.marginHeight = 0;
		importanceContainter.setLayout(gl_importanceContainer);

		createImportanceTable(importanceContainter);
		createImportancePanel(importanceContainter);

		importanceTab.setControl(importanceContainter);
	}

	private void createTabs() {
		TabFolder folder = new TabFolder(parent, SWT.NONE);
		folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		createImportanceTab(folder);
		createAssessmentTab(folder);
	}

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);

		createTabs();

		performButton = new Button(parent, SWT.NONE);
		performButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				true, 1, 1));
		performButton
				.setImage(AbstractUIPlugin.imageDescriptorFromPlugin(
						"flintstones.gathering.cloud", "/icons/vote.png")
						.createImage());
		performButton.setEnabled(false);

		hookListeners();
		refresh();
	}

	@Override
	public void setFocus() {
		parent.setFocus();
	}

	private void packColumnsImportance() {
		for (TableColumn column : viewer_importance.getTable().getColumns()) {
			column.pack();
			column.setWidth(column.getWidth() + 10);
		}
	}

	private void packColumnsAssessment() {
		for (TableColumn column : viewer_assessment.getTable().getColumns()) {
			column.pack();
			column.setWidth(column.getWidth() + 10);
		}
	}

	@SuppressWarnings("serial")
	private void hookListeners() {
		viewer_importance
				.addSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						listViewer_first_importance.setInput(empty);
						listViewer_second_importance.setInput(empty);
						listViewer_third_importance.setInput(empty);
						value_first_importance = value_second_importance = value_third_importance = null;
						setTextImportance();

						IStructuredSelection selection = (IStructuredSelection) event
								.getSelection();

						boolean visible = false;
						if (!selection.isEmpty()) {
							visible = true;
							StructuredSelection s = (StructuredSelection) viewer_importance.getSelection();
							TableElement te = (TableElement) s.getFirstElement();
							String value = te.valuation.getValue();
							extractTextImportance(value);
						}
						importanceContainer.setVisible(visible);

					}
				});

		viewer_assessment
				.addSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						if (linguisticAssessmentPanel != null) {
							listViewer_first_assessment.setInput(empty);
							listViewer_second_assessment.setInput(empty);
							listViewer_third_assessment.setInput(empty);
						}
						value_first_assessment = value_second_assessment = value_third_assessment = null;

						if (linguisticAssessmentPanel != null) {
							setTextAssessment();
						}

						IStructuredSelection selection = (IStructuredSelection) event
								.getSelection();

						boolean isBoolean = false;
						boolean isNumerical = false;
						boolean isPercent = false;
						boolean isLinguistic = false;
						boolean isInterval = false;
						String value = null;
						Domain domain = null;
						if (!selection.isEmpty()) {
							StructuredSelection s = (StructuredSelection) viewer_assessment.getSelection();
							TableElement te = (TableElement) s.getFirstElement();

							if (!te.key.getAlternative().toLowerCase().equals("importancia")) {
								String d = problem.getDomainAssignments().get(
										te.key);
								domain = problem.getDomains().get(d);
								if (domain != null) {
									IDomain iDomain = domain.getDomain();
									if (iDomain instanceof NumericDomain) {
										isNumerical = true;
									} else if (iDomain instanceof IntervalNumericDomain) {
										isInterval = true;
									} else {
										isLinguistic = true;
									}
								}
							}

							value = te.valuation.getValue();
						}

						if (!isBoolean) {
							if (booleanAssessmentPanel != null) {
								booleanAssessmentPanel.dispose();
								booleanAssessmentPanel = null;
							}
						}
						
						if (!isNumerical) {
							if (numericalAssessmentPanel != null) {
								numericalAssessmentPanel.dispose();
								numericalAssessmentPanel = null;
							}
						}
						
						if (!isPercent) {
							if (percentAssessmentPanel != null) {
								percentAssessmentPanel.dispose();
								percentAssessmentPanel = null;
							}
						}
						
						if (!isInterval) {
							if (intervalNumericalAssessmentPanel != null) {
								intervalNumericalAssessmentPanel.dispose();
								intervalNumericalAssessmentPanel = null;
							}
						}
						
						if (!isLinguistic) {
							if (linguisticAssessmentPanel != null) {
								listViewer_first_assessment
										.removeSelectionChangedListener(listViewer_first_listener_assessment);
								listViewer_second_assessment
										.removeSelectionChangedListener(listViewer_second_listener_assessment);
								listViewer_third_assessment
										.removeSelectionChangedListener(listViewer_third_listener_assessment);
								linguisticAssessmentPanel.dispose();
								linguisticAssessmentPanel = null;
							}
						}
						
						if (isBoolean) {
							if (booleanAssessmentPanel == null) {
								createBooleanAssessmentPanel(assessmentsContainer);
								assessmentsContainer.layout();
							}
							extractBooleanAssessment(value);
						}

						if (isNumerical) {
							if (numericalAssessmentPanel == null) {
								createNumericalAssessmentPanel((NumericDomain) domain.getDomain(), assessmentsContainer);
								assessmentsContainer.layout();
							}
							extractNumericalAssessment((NumericDomain) domain.getDomain(), value);

						}

						if (isInterval) {
							if (intervalNumericalAssessmentPanel == null) {
								createIntervalNumericalAssessmentPanel((IntervalNumericDomain) domain.getDomain(), assessmentsContainer);
								assessmentsContainer.layout();
							}
							extractIntervalNumericalAssessment((IntervalNumericDomain) domain.getDomain(), value);

						}

						if (isPercent) {
							if (percentAssessmentPanel == null) {
								createPercentAssessmentPanel(assessmentsContainer);
								assessmentsContainer.layout();
							}
							extractPercentAssessment(value);

						}

						if (isLinguistic) {
							if (linguisticAssessmentPanel == null) {
								createLinguisticAssessmentPanel(assessmentsContainer);
								assessmentsContainer.layout();
							}
							extractLinguisticAssessment(value);
						}
					}
				});

		listViewer_first_listener_importance = new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();

				String new_value;
				if (selection.isEmpty()) {
					new_value = null;
				} else {
					new_value = (String) selection.getFirstElement();
				}

				boolean doit = false;
				if (new_value == null) {
					if (value_first_importance != null) {
						doit = true;
					}
				} else if (!new_value.equals(value_first_importance)) {
					doit = true;
				}

				if (doit) {
					value_first_importance = new_value;

					value_second_importance = null;
					value_third_importance = null;

					if (value_first_importance != null) {
						if (value_first_importance.equals(QBETWEEN)) {
							listViewer_second_importance
									.setInput(between_start_importance);
						} else if (value_first_importance.equals(QLOWERTHAN)) {
							listViewer_second_importance
									.setInput(labels_lower_than_importance);
						} else if (value_first_importance.equals(QGREATERTHAN)) {
							listViewer_second_importance
									.setInput(labels_greater_than_importance);
						} else {
							listViewer_second_importance
									.setInput(labels_importance);
						}
						setTextImportance();
					} else {
						listViewer_second_importance.setInput(empty);
						text_importance.setText("");
					}

					listViewer_third_importance.setInput(empty);
				}
			}
		};

		listViewer_first_listener_assessment = new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();

				String new_value;
				if (selection.isEmpty()) {
					new_value = null;
				} else {
					new_value = (String) selection.getFirstElement();
				}

				boolean doit = false;
				if (new_value == null) {
					if (value_first_assessment != null) {
						doit = true;
					}
				} else if (!new_value.equals(value_first_assessment)) {
					doit = true;
				}

				if (doit) {
					value_first_assessment = new_value;

					value_second_assessment = null;
					value_third_assessment = null;

					if (value_first_assessment != null) {
						if (value_first_assessment.equals(QBETWEEN)) {
							listViewer_second_assessment
									.setInput(between_start_assessment);
						} else if (value_first_assessment.equals(QLOWERTHAN)) {
							listViewer_second_assessment
									.setInput(labels_lower_than_assessment);
						} else if (value_first_assessment.equals(QGREATERTHAN)) {
							listViewer_second_assessment
									.setInput(labels_greater_than_assessment);
						} else {
							listViewer_second_assessment
									.setInput(labels_assessment);
						}
						setTextAssessment();
					} else {
						listViewer_second_assessment.setInput(empty);
						text_assessment.setText("");
					}

					listViewer_third_assessment.setInput(empty);
				}
			}
		};

		listViewer_second_listener_importance = new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();

				String new_value;
				if (selection.isEmpty()) {
					new_value = null;
				} else {
					new_value = (String) selection.getFirstElement();
				}

				boolean doit = false;
				if (new_value == null) {
					if (value_second_importance != null) {
						doit = true;
					}
				} else if (!new_value.equals(value_second_importance)) {
					doit = true;
				}

				if (doit) {
					value_second_importance = new_value;

					value_third_importance = null;

					if (value_second_importance != null) {
						if (value_first_importance.equals(QBETWEEN)) {
							computeBetweenEndLabelsImportance(value_second_importance);
							listViewer_third_importance
									.setInput(between_end_importance);
						} else {
							listViewer_third_importance.setInput(empty);
						}
						setTextImportance();

					}
				}
			}
		};

		listViewer_second_listener_assessment = new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();

				String new_value;
				if (selection.isEmpty()) {
					new_value = null;
				} else {
					new_value = (String) selection.getFirstElement();
				}

				boolean doit = false;
				if (new_value == null) {
					if (value_second_assessment != null) {
						doit = true;
					}
				} else if (!new_value.equals(value_second_assessment)) {
					doit = true;
				}

				if (doit) {
					value_second_assessment = new_value;

					value_third_assessment = null;

					if (value_second_assessment != null) {
						if (value_first_assessment.equals(QBETWEEN)) {
							computeBetweenEndLabelsAssessment(value_second_assessment);
							listViewer_third_assessment
									.setInput(between_end_assessment);
						} else {
							listViewer_third_assessment.setInput(empty);
						}
						setTextAssessment();

					}
				}
			}
		};

		listViewer_third_listener_importance = new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();

				String new_value;
				if (selection.isEmpty()) {
					new_value = null;
				} else {
					new_value = (String) selection.getFirstElement();
				}

				boolean doit = false;
				if (new_value == null) {
					if (value_third_importance != null) {
						doit = true;
					}
				} else if (!new_value.equals(value_third_importance)) {
					doit = true;
				}

				if (doit) {
					value_third_importance = new_value;

					if (value_third_importance != null) {
						setTextImportance();
					}
				}
			}
		};

		listViewer_third_listener_assessment = new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();

				String new_value;
				if (selection.isEmpty()) {
					new_value = null;
				} else {
					new_value = (String) selection.getFirstElement();
				}

				boolean doit = false;
				if (new_value == null) {
					if (value_third_assessment != null) {
						doit = true;
					}
				} else if (!new_value.equals(value_third_assessment)) {
					doit = true;
				}

				if (doit) {
					value_third_assessment = new_value;

					if (value_third_assessment != null) {
						setTextAssessment();
					}
				}
			}
		};

		listViewer_first_importance
				.addSelectionChangedListener(listViewer_first_listener_importance);
		listViewer_second_importance
				.addSelectionChangedListener(listViewer_second_listener_importance);
		listViewer_third_importance
				.addSelectionChangedListener(listViewer_third_listener_importance);

		performButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (MessageDialog.openConfirm(parent.getShell(), "Enviar",
						"¿Enviar valoraciones?")) {
					problemAssignment.setMake(true);
					DAOProblemAssignments.getDAO().makeAssignment(problem,
							problemAssignment);
				}
			}
		});

	}

	private void extractTextImportance(String value) {

		String value_first_copy = "";
		String value_second_copy = null;
		String value_third_copy = null;

		listViewer_first_importance.setInput(quantifiers);

		if (value.isEmpty()) {
			listViewer_first_importance.setSelection(new StructuredSelection(),
					true);

		} else if (value.startsWith(QBETWEEN)) {
			value_first_copy = QBETWEEN;
			listViewer_first_importance.setSelection(new StructuredSelection(
					value_first_copy), true);
			if (value.length() > value_first_copy.length()) {
				value_second_copy = value
						.substring(value_first_copy.length() + 1);
				if (value_second_copy.contains(" and ")) {
					String[] tokens = value_second_copy.split(" and ");
					value_second_copy = tokens[0];
					value_third_copy = tokens[1];
					listViewer_second_importance.setSelection(
							new StructuredSelection(value_second_copy), true);
				}
				if (value_third_copy != null) {
					listViewer_third_importance.setSelection(
							new StructuredSelection(value_third_copy), true);
				}
			}

		} else if (value.startsWith(QATLEAST) || value.startsWith(QATMOST)
				|| value.startsWith(QGREATERTHAN)
				|| value.startsWith(QLOWERTHAN)) {
			if (value.startsWith(QATLEAST)) {
				value_first_copy = QATLEAST;
			} else if (value.startsWith(QATMOST)) {
				value_first_copy = QATMOST;
			} else if (value.startsWith(QGREATERTHAN)) {
				value_first_copy = QGREATERTHAN;
			} else {
				value_first_copy = QLOWERTHAN;
			}
			listViewer_first_importance.setSelection(new StructuredSelection(
					value_first_copy), true);
			if (value.length() > value_first_copy.length()) {
				value_second_copy = value
						.substring(value_first_copy.length() + 1);
				listViewer_second_importance.setSelection(
						new StructuredSelection(value_second_copy), true);
			}
		} else {
			listViewer_first_importance.setSelection(new StructuredSelection(
					QSINGLE), true);
			value_second_copy = value;
			listViewer_second_importance.setSelection(new StructuredSelection(
					value_second_copy), true);
		}
	}

	private void extractBooleanAssessment(String value) {

		booleanValue = value;

		if (booleanValue == null) {
			booleanValue = "0";
		} else if (booleanValue.isEmpty()) {
			booleanValue = "0";
		}

		if (booleanValue.equals("1")) {
			booleanLabel.setImage(AbstractUIPlugin.imageDescriptorFromPlugin(
					"flintstones.gathering.cloud", "/icons/yes_30.png")
					.createImage());
		} else {
			booleanLabel.setImage(AbstractUIPlugin.imageDescriptorFromPlugin(
					"flintstones.gathering.cloud", "/icons/no_30.png")
					.createImage());
		}
	}

	private void extractNumericalAssessment(NumericDomain domain, String value) {

		numericalValue = value;

		Double min = domain.getMin();
		Double max = domain.getMax();
		
		// FLOAT
		if (domain.getType() == 0) {
			if (numericalValue == null) {
				numericalValue = Integer.toString(min.intValue() * 100);
			} else if (numericalValue.isEmpty()) {
				numericalValue = Integer.toString(min.intValue() * 100);
			}
			
			numericalSpinner.setValues(Integer.parseInt(numericalValue), min.intValue() * 100, max.intValue() * 100, 2,
					1, 1);
			
		// INTEGER
		} else {
			if (numericalValue == null) {
				numericalValue = Integer.toString(min.intValue());
			} else if (numericalValue.isEmpty()) {
				numericalValue = Integer.toString(min.intValue());
			}
			
			numericalSpinner.setValues(Integer.parseInt(numericalValue), min.intValue(), max.intValue(), 0,
					1, 1);
		}
		
		numericalSpinner.setSelection(Integer.parseInt(numericalValue));

	}

	private void extractIntervalNumericalAssessment(IntervalNumericDomain domain, String value) {

		intervalNumericalValue = value;

		Double min = domain.getMin();
		Double max = domain.getMax();
		
		// FLOAT
		if (domain.getType() == 0) {

			String lv = null;
			String uv = null;
			if (intervalNumericalValue == null) {
				lv = Integer.toString(min.intValue() * 100);
				uv = Integer.toString(min.intValue() * 100);
			} else if (intervalNumericalValue.isEmpty()) {
				lv = Integer.toString(min.intValue() * 100);
				uv = Integer.toString(min.intValue() * 100);
			} else {
				String[] tokens = intervalNumericalValue.split(", ");
				lv = tokens[0].substring(1);
				uv = tokens[1].substring(0, tokens[1].length() - 1);
			}
						
			minNumericalSpinner.setValues(Integer.parseInt(lv), min.intValue() * 100, Integer.parseInt(uv), 2,
					1, 1);
			maxNumericalSpinner.setValues(Integer.parseInt(uv), Integer.parseInt(lv), max.intValue() * 100, 2,
					1, 1);
			
		// INTEGER
		} else {
			if (intervalNumericalValue == null) {

				String lv = null;
				String uv = null;
				if (intervalNumericalValue == null) {
					lv = Integer.toString(min.intValue());
					uv = Integer.toString(min.intValue());
				} else if (intervalNumericalValue.isEmpty()) {
					lv = Integer.toString(min.intValue());
					uv = Integer.toString(min.intValue());
				} else {
					String[] tokens = intervalNumericalValue.split(", ");
					lv = tokens[0].substring(1);
					uv = tokens[1].substring(0, tokens[1].length() - 1);
				}
							
				minNumericalSpinner.setValues(Integer.parseInt(lv), min.intValue(), Integer.parseInt(uv), 2,
						1, 1);
				maxNumericalSpinner.setValues(Integer.parseInt(uv), Integer.parseInt(lv), max.intValue(), 2,
						1, 1);
			}
		}

	}

	private void extractPercentAssessment(String value) {

		percentValue = value;

		if (percentValue == null) {
			percentValue = "0";
		} else if (percentValue.isEmpty()) {
			percentValue = "0";
		}
		percentValue = percentValue.replace(",", ".");

		percentSpinner
				.setSelection((int) (Float.parseFloat(percentValue) * 100f));

	}

	private void extractLinguisticAssessment(String value) {

		String value_first_copy = "";
		String value_second_copy = null;
		String value_third_copy = null;

		listViewer_first_assessment.setInput(quantifiers);

		if (value.isEmpty()) {
			listViewer_first_assessment.setSelection(new StructuredSelection(),
					true);

		} else if (value.startsWith(QBETWEEN)) {
			value_first_copy = QBETWEEN;
			listViewer_first_assessment.setSelection(new StructuredSelection(
					value_first_copy), true);
			if (value.length() > value_first_copy.length()) {
				value_second_copy = value
						.substring(value_first_copy.length() + 1);
				if (value_second_copy.contains(" and ")) {
					String[] tokens = value_second_copy.split(" and ");
					value_second_copy = tokens[0];
					value_third_copy = tokens[1];
					listViewer_second_assessment.setSelection(
							new StructuredSelection(value_second_copy), true);
				}
				if (value_third_copy != null) {
					listViewer_third_assessment.setSelection(
							new StructuredSelection(value_third_copy), true);
				}
			}

		} else if (value.startsWith(QATLEAST) || value.startsWith(QATMOST)
				|| value.startsWith(QGREATERTHAN)
				|| value.startsWith(QLOWERTHAN)) {
			if (value.startsWith(QATLEAST)) {
				value_first_copy = QATLEAST;
			} else if (value.startsWith(QATMOST)) {
				value_first_copy = QATMOST;
			} else if (value.startsWith(QGREATERTHAN)) {
				value_first_copy = QGREATERTHAN;
			} else {
				value_first_copy = QLOWERTHAN;
			}
			listViewer_first_assessment.setSelection(new StructuredSelection(
					value_first_copy), true);
			if (value.length() > value_first_copy.length()) {
				value_second_copy = value
						.substring(value_first_copy.length() + 1);
				listViewer_second_assessment.setSelection(
						new StructuredSelection(value_second_copy), true);
			}
		} else {
			listViewer_first_assessment.setSelection(new StructuredSelection(
					QSINGLE), true);
			value_second_copy = value;
			listViewer_second_assessment.setSelection(new StructuredSelection(
					value_second_copy), true);
		}
	}

	private void computeSelectedLabelsImportance() {

		int init_pos = -1;
		int end_pos = -1;

		int[] new_selection = new int[2];

		String t = text_importance.getText();

		if (!t.isEmpty()) {
			if (t.startsWith(QBETWEEN)) {
				t = t.substring(QBETWEEN.length() + 1);
				if (t.contains(" and ")) {
					String[] tokens = t.split(" and ");
					if (tokens.length > 1) {
						int pos = 0;
						boolean find = false;
						do {
							if (labels_importance[pos].equals(tokens[0])) {
								find = true;
							} else {
								pos++;
							}
						} while ((!find) && (pos < number_of_labels_importance));
						if (find) {
							init_pos = pos;

							find = false;
							do {
								if (labels_importance[pos].equals(tokens[1])) {
									find = true;
								} else {
									pos++;
								}
							} while ((!find)
									&& (pos < number_of_labels_importance));
							if (find) {
								end_pos = pos;
							} else {
								init_pos = -1;
							}
						}
					}
				}

			} else if (t.startsWith(QATLEAST)) {
				t = t.substring(QATLEAST.length() + 1);
				int pos = 0;
				boolean find = false;
				do {
					if (labels_importance[pos].equals(t)) {
						find = true;
					} else {
						pos++;
					}
				} while ((!find) && (pos < number_of_labels_importance));
				if (find) {
					init_pos = pos;
					end_pos = labels_importance.length - 1;
				}

			} else if (t.startsWith(QATMOST)) {
				t = t.substring(QATMOST.length() + 1);
				int pos = 0;
				boolean find = false;
				do {
					if (labels_importance[pos].equals(t)) {
						find = true;
					} else {
						pos++;
					}
				} while ((!find) && (pos < number_of_labels_importance));
				if (find) {
					init_pos = 0;
					end_pos = pos;
				}
			} else if (t.startsWith(QLOWERTHAN)) {
				t = t.substring(QLOWERTHAN.length() + 1);
				int pos = 0;
				boolean find = false;
				do {
					if (labels_importance[pos].equals(t)) {
						find = true;
					} else {
						pos++;
					}
				} while ((!find) && (pos < number_of_labels_importance));
				if (find) {
					init_pos = 0;
					end_pos = pos - 1;
				}

			} else if (t.startsWith(QGREATERTHAN)) {
				t = t.substring(QGREATERTHAN.length() + 1);
				int pos = 0;
				boolean find = false;
				do {
					if (labels_importance[pos].equals(t)) {
						find = true;
					} else {
						pos++;
					}
				} while ((!find) && (pos < number_of_labels_importance));
				if (find) {
					init_pos = pos + 1;
					end_pos = labels_importance.length - 1;
				}

			} else {
				int pos = 0;
				boolean find = false;
				do {
					if (labels_importance[pos].equals(t)) {
						find = true;
					} else {
						pos++;
					}
				} while (!find);
				init_pos = end_pos = pos;
			}

		}

		new_selection[0] = init_pos;
		new_selection[1] = end_pos;

		if ((selection_importance[0] != new_selection[0])
				|| (selection_importance[1] != new_selection[1])) {
			selection_importance = new_selection;
			canvas_importance.layout();
			canvas_importance.redraw();
		}
	}

	private void computeSelectedLabelsAssessment() {

		int init_pos = -1;
		int end_pos = -1;

		int[] new_selection = new int[2];

		String t = text_assessment.getText();

		if (!t.isEmpty()) {
			if (t.startsWith(QBETWEEN)) {
				t = t.substring(QBETWEEN.length() + 1);
				if (t.contains(" and ")) {
					String[] tokens = t.split(" and ");
					if (tokens.length > 1) {
						int pos = 0;
						boolean find = false;
						do {
							if (labels_assessment[pos].equals(tokens[0])) {
								find = true;
							} else {
								pos++;
							}
						} while ((!find) && (pos < number_of_labels_assessment));
						if (find) {
							init_pos = pos;

							find = false;
							do {
								if (labels_assessment[pos].equals(tokens[1])) {
									find = true;
								} else {
									pos++;
								}
							} while ((!find)
									&& (pos < number_of_labels_assessment));
							if (find) {
								end_pos = pos;
							} else {
								init_pos = -1;
							}
						}
					}
				}

			} else if (t.startsWith(QATLEAST)) {
				t = t.substring(QATLEAST.length() + 1);
				int pos = 0;
				boolean find = false;
				do {
					if (labels_assessment[pos].equals(t)) {
						find = true;
					} else {
						pos++;
					}
				} while ((!find) && (pos < number_of_labels_assessment));
				if (find) {
					init_pos = pos;
					end_pos = labels_assessment.length - 1;
				}

			} else if (t.startsWith(QATMOST)) {
				t = t.substring(QATMOST.length() + 1);
				int pos = 0;
				boolean find = false;
				do {
					if (labels_assessment[pos].equals(t)) {
						find = true;
					} else {
						pos++;
					}
				} while ((!find) && (pos < number_of_labels_assessment));
				if (find) {
					init_pos = 0;
					end_pos = pos;
				}

			} else if (t.startsWith(QLOWERTHAN)) {
				t = t.substring(QLOWERTHAN.length() + 1);
				int pos = 0;
				boolean find = false;
				do {
					if (labels_assessment[pos].equals(t)) {
						find = true;
					} else {
						pos++;
					}
				} while ((!find) && (pos < number_of_labels_assessment));
				if (find) {
					init_pos = 0;
					end_pos = pos - 1;
				}

			} else if (t.startsWith(QGREATERTHAN)) {
				t = t.substring(QGREATERTHAN.length() + 1);
				int pos = 0;
				boolean find = false;
				do {
					if (labels_assessment[pos].equals(t)) {
						find = true;
					} else {
						pos++;
					}
				} while ((!find) && (pos < number_of_labels_assessment));
				if (find) {
					init_pos = pos + 1;
					end_pos = labels_assessment.length - 1;
				}

			} else {
				int pos = 0;
				boolean find = false;
				do {
					if (labels_assessment[pos].equals(t)) {
						find = true;
					} else {
						pos++;
					}
				} while (!find);
				init_pos = end_pos = pos;
			}

		}

		new_selection[0] = init_pos;
		new_selection[1] = end_pos;

		if ((selection_assessment[0] != new_selection[0])
				|| (selection_assessment[1] != new_selection[1])) {
			selection_assessment = new_selection;
			canvas_assessment.layout();
			canvas_assessment.redraw();
		}
	}

	private void paintSelectionImportance() {
		double min_lower;
		double max_lower;
		double min_upper;
		double max_upper;
		NumericDomain coverage;
		NumericDomain center;
		domainPolygons_importance = new int[number_of_labels_importance][10];
		int pos = 0;
		for (mcdacw.valuation.domain.fuzzyset.Label label : domain_importance
				.getLabels()) {
			coverage = label.getSemantic().getCoverage();
			min_lower = coverage.getMin();
			max_lower = coverage.getMax();
			center = label.getSemantic().getCenter();
			min_upper = center.getMin();
			max_upper = center.getMax();

			domainPolygons_importance[pos][0] = (int) (min_lower * (domainWidth - 3));
			domainPolygons_importance[pos][1] = domainHeight - 1;
			domainPolygons_importance[pos][2] = (int) (min_upper * (domainWidth - 3));
			domainPolygons_importance[pos][3] = 20;
			domainPolygons_importance[pos][4] = (int) (max_upper * (domainWidth - 3));
			domainPolygons_importance[pos][5] = 20;
			domainPolygons_importance[pos][6] = (int) (max_lower * (domainWidth - 3));
			domainPolygons_importance[pos][7] = domainHeight - 1;
			domainPolygons_importance[pos][8] = (int) (min_lower * (domainWidth - 3));
			domainPolygons_importance[pos][9] = domainHeight - 1;
			pos++;
		}

		gc_importance.setLineWidth(1);
		int c;
		String[] words;
		String label;
		for (int i = 0; i < number_of_labels_importance; i++) {
			if (i == selection_importance[0]) {
				gc_importance.setLineWidth(3);
			}

			if (i == 0) {
				c = domainPolygons_importance[i][2];
			} else if (i == (number_of_labels_importance - 1)) {
				c = domainPolygons_importance[i][4] - 15;
			} else {
				c = ((domainPolygons_importance[i][4] - domainPolygons_importance[i][2]) / 2)
						+ domainPolygons_importance[i][2] - 7;
			}

			words = labels_importance[i].split(" ");
			label = "";
			for (String w : words) {
				label += w.toCharArray()[0];
			}
			gc_importance.drawText(label, c, 0);
			gc_importance.drawPolygon(domainPolygons_importance[i]);
			if (i == selection_importance[1]) {
				gc_importance.setLineWidth(1);
			}
		}

	}

	private void paintSelectionAssessment() {
		double min_lower;
		double max_lower;
		double min_upper;
		double max_upper;
		NumericDomain coverage;
		NumericDomain center;
		domainPolygons_assessment = new int[number_of_labels_assessment][10];
		int pos = 0;
		for (mcdacw.valuation.domain.fuzzyset.Label label : domain_assessment
				.getLabels()) {
			coverage = label.getSemantic().getCoverage();
			min_lower = coverage.getMin();
			max_lower = coverage.getMax();
			center = label.getSemantic().getCenter();
			min_upper = center.getMin();
			max_upper = center.getMax();

			domainPolygons_assessment[pos][0] = (int) (min_lower * (domainWidth - 3));
			domainPolygons_assessment[pos][1] = domainHeight - 1;
			domainPolygons_assessment[pos][2] = (int) (min_upper * (domainWidth - 3));
			domainPolygons_assessment[pos][3] = 20;
			domainPolygons_assessment[pos][4] = (int) (max_upper * (domainWidth - 3));
			domainPolygons_assessment[pos][5] = 20;
			domainPolygons_assessment[pos][6] = (int) (max_lower * (domainWidth - 3));
			domainPolygons_assessment[pos][7] = domainHeight - 1;
			domainPolygons_assessment[pos][8] = (int) (min_lower * (domainWidth - 3));
			domainPolygons_assessment[pos][9] = domainHeight - 1;
			pos++;
		}

		gc_assessment.setLineWidth(1);
		int c;
		String[] words;
		String label;
		for (int i = 0; i < number_of_labels_assessment; i++) {
			if (i == selection_assessment[0]) {
				gc_assessment.setLineWidth(3);
			}

			if (i == 0) {
				c = domainPolygons_assessment[i][2];
			} else if (i == (number_of_labels_assessment - 1)) {
				c = domainPolygons_assessment[i][4] - 20;
			} else {
				c = ((domainPolygons_assessment[i][4] - domainPolygons_assessment[i][2]) / 2)
						+ domainPolygons_assessment[i][2] - 7;
			}

			words = labels_assessment[i].split(" ");
			label = "";
			for (String w : words) {
				label += w.toCharArray()[0];
			}

			gc_assessment.drawText(label, c, 0);
			gc_assessment.drawPolygon(domainPolygons_assessment[i]);
			if (i == selection_assessment[1]) {
				gc_assessment.setLineWidth(1);
			}
		}
	}

	private void setTextImportance() {

		boolean enabled = false;
		String value = "";
		if (value_first_importance != null) {
			if (!value_first_importance.equals(QSINGLE)) {
				value += value_first_importance;
			}
			if (!value.isEmpty()) {
				value += " ";
			}
			if (value_second_importance != null) {
				value += value_second_importance;
				if (value_first_importance.equals(QBETWEEN)) {
					value += " and ";
					if (value_third_importance != null) {
						value += value_third_importance;
						enabled = true;
					}
				} else {
					enabled = true;
				}
			}
		}
		saveButton_importance.setEnabled(enabled);

		text_importance.setText(value);

		computeSelectedLabelsImportance();
	}

	private void setTextAssessment() {

		boolean enabled = false;
		String value = "";
		if (value_first_assessment != null) {
			if (!value_first_assessment.equals(QSINGLE)) {
				value += value_first_assessment;
			}
			if (!value.isEmpty()) {
				value += " ";
			}
			if (value_second_assessment != null) {
				value += value_second_assessment;
				if (value_first_assessment.equals(QBETWEEN)) {
					value += " and ";
					if (value_third_assessment != null) {
						value += value_third_assessment;
						enabled = true;
					}
				} else {
					enabled = true;
				}
			}
		}
		saveButton_linguistic_assessment.setEnabled(enabled);

		text_assessment.setText(value);

		computeSelectedLabelsAssessment();
	}

	public void refresh() {
		problem = (Problem) RWT.getUISession()
				.getAttribute("valuation-problem");
		problemAssignment = (ProblemAssignment) RWT.getUISession()
				.getAttribute("valuation-problem-assignment");
		if (problem != null) {
			parent.setVisible(true);
			viewer_importance.setInput(problemAssignment);
			viewer_assessment.setInput(problemAssignment);
			projectFilterCombo.setItems(alternatives.toArray(new String[0]));
			packColumnsImportance();
			packColumnsAssessment();
		} else {
			parent.setVisible(false);
		}
		setPartName((problem != null) ? "Valoraciones para el problema: "
				+ problem.getId() : "Problema no seleccionado");
	}

	private boolean validateImportance() {
		boolean valid = true;
		if (elements_importance == null) {
			return false;
		}
		for (TableElement element : elements_importance) {
			if (element.getValuation().getValue() == null) {
				valid = false;
			} else if (element.getValuation().getValue().isEmpty()) {
				valid = false;
			}
		}
		return valid;
	}

	private boolean validateAssessment() {
		boolean valid = true;
		if (elements_assessment == null) {
			return false;
		}
		for (TableElement element : elements_assessment) {
			if (element.getValuation().getValue() == null) {
				valid = false;
			} else if (element.getValuation().getValue().isEmpty()) {
				valid = false;
			}
		}
		return valid;
	}

	private void validate() {
		boolean valid = validateImportance() && validateAssessment();
		performButton.setEnabled(valid);
	}

}
