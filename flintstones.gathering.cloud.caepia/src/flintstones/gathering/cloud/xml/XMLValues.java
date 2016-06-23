package flintstones.gathering.cloud.xml;

import mcdacw.valuation.domain.fuzzyset.Unbalanced;
import mcdacw.valuation.domain.fuzzyset.function.types.TrapezoidalFunction;
import mcdacw.valuation.valuation.IntegerIntervalValuation;
import mcdacw.valuation.valuation.IntegerValuation;
import mcdacw.valuation.valuation.LinguisticValuation;
import mcdacw.valuation.valuation.RealIntervalValuation;
import mcdacw.valuation.valuation.RealValuation;
import mcdacw.valuation.valuation.TwoTuple;
import mcdacw.valuation.valuation.hesitant.HesitantValuation;

public class XMLValues {

	public static final String NUMERIC_INTEGER_DOMAIN = "flintstones.domain.numeric.integer";
	public static final String NUMERIC_REAL_DOMAIN = "flintstones.domain.numeric.real";
	public static final String FUZZY_SET = "flintstones.domain.linguistic";
	public static final String UNBALANCED = Unbalanced.class.getName();
	public static final String LINGUISTIC = LinguisticValuation.class.getName();
	public static final String TWO_TUPLE = TwoTuple.class.getName();
	public static final String HESITANT = HesitantValuation.class.getName();
	public static final String INTEGER = IntegerValuation.class.getName();
	public static final String REAL = RealValuation.class.getName();
	public static final String INTEGER_INTERVAL = IntegerIntervalValuation.class.getName();
	public static final String REAL_INTERVAL = RealIntervalValuation.class.getName();
	public static final String TRAPEZOIDAL_MEMBERSHIP_FUNCTION = TrapezoidalFunction.class.getName();
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
