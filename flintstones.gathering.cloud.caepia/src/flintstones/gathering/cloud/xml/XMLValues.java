package flintstones.gathering.cloud.xml;

import mcdacw.valuation.Hesitant;
import mcdacw.valuation.Interval;
import mcdacw.valuation.Linguistic;
import mcdacw.valuation.NotApplicable;
import mcdacw.valuation.Numeric;
import mcdacw.valuation.TwoTuple;
import mcdacw.valuation.domain.fuzzyset.FuzzySet;
import mcdacw.valuation.domain.fuzzyset.HesitantFuzzySet;
import mcdacw.valuation.domain.fuzzyset.TrapezoidalMembershipFunction;
import mcdacw.valuation.domain.numeric.IntervalNumericDomain;
import mcdacw.valuation.domain.numeric.NumericDomain;

public class XMLValues {

	public static final String NUMERIC_DOMAIN = NumericDomain.class.getName();
	public static final String INTERVAL_NUMERIC_DOMAIN = IntervalNumericDomain.class.getName();
	public static final String FUZZY_SET = FuzzySet.class.getName();
	public static final String HESITANT_FUZZY_SET = HesitantFuzzySet.class.getName();
	public static final String NUMERIC = Numeric.class.getName();
	public static final String LINGUICTIC = Linguistic.class.getName();
	public static final String TWO_TUPLE = TwoTuple.class.getName();
	public static final String HESITANT = Hesitant.class.getName();
	public static final String NOT_APPLICABLE = NotApplicable.class.getName();
	public static final String INTERVAL = Interval.class.getName();
	public static final String TRAPEZOIDAL_MEMBERSHIP_FUNCTION = TrapezoidalMembershipFunction.class.getName();
	public static final String END = "\n";
	public static final String TAB = "\t";
	public static final String PROBLEM = "problem";
	public static final String PROBLEM_ELEMENTS = "problemElements";
	public static final String EXPERTS = "experts";
	public static final String EXPERT = "expert";
	public static final String ALTERNATIVES = "alternatives";
	public static final String ALTERNATIVE = "alternative";
	public static final String CRITERIA = "criteria";
	public static final String CRITERION = "criterion";
	public static final String NAME = "name";
	public static final String COST = "cost";
	public static final String DOMAINS = "domains";
	public static final String DOMAIN = "domain";
	public static final String TYPE = "type";
	public static final String MIN = "min";
	public static final String MAX = "max";
	public static final String VALUES = "values";
	public static final String RANGE = "range";
	public static final String LABELS = "labels";
	public static final String LABEL = "label";
	public static final String SEMANTIC = "semantic";
	public static final String A = "a";
	public static final String B = "b";
	public static final String C = "c";
	public static final String D = "d";
	public static final String MEASURE = "measure";
	public static final String UNBALANCED_INFO = "unbalancedInfo";
	public static final String CARDINALITY = "cardinality";
	public static final String SL = "sl";
	public static final String DENSITY = "density";
	public static final String SR = "sr";
	public static final String LH = "lh";
	public static final String POS = "pos";
	public static final String SIZE = "size";
	public static final String ASSIGNMENTS = "assignments";
	public static final String ASSIGNMENT = "assignment";
	public static final String DOMAIN_ASSIGNMENTS = "domainAssignments";
	public static final String NULL = "null";
	public static final String BLOCK = "block";
	public static final String EVALUATIONS = "evaluations";
	public static final String EVALUATION = "evaluation";
	public static final String VALUATION = "valuation";
	public static final String ALPHA = "alpha";
	public static final String HESITANT_TYPE = "hesitantType";
	public static final String PRIMARY = "primary";
	public static final String UNARY = "unary";
	public static final String RELATION = "relation";
	public static final String BINARY = "binary";
	public static final String TERM = "term";
	public static final String LOWER_TERM = "lowerTerm";
	public static final String UPPER_TERM = "upperTerm";
	public static final String EMPTY = "";
	private XMLValues() {
		
	}
}
