package flintstones.gathering.cloud.xml;

import mcdacw.valuation.domain.fuzzyset.function.types.TrapezoidalFunction;

public class XMLValues {

	public static final String NUMERIC_INTEGER_DOMAIN = "flintstones.domain.numeric.integer";
	public static final String NUMERIC_REAL_DOMAIN = "flintstones.domain.numeric.real";
	public static final String FUZZY_SET = "flintstones.domain.linguistic";
	public static final String LINGUISTIC = "flintstones.valuation.linguistic";
	public static final String HESITANT = "flintstones.valuation.hesitant";
	public static final String INTEGER = "flintstones.valuation.integer";
	public static final String REAL = "flintstones.valuation.real";
	public static final String INTEGER_INTERVAL = "flintstones.valuation.integer.interval";
	public static final String REAL_INTERVAL = "flintstones.valuation.real.interval";
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
	
	public static final String RESOLUTION_SCHEME = "flintstones.resolutionscheme.dm";
	public static final String RESOLUTION_PHASE_FRAMEWORK = "flintstones.resolutionphase.framework";
	public static final String RESOLUTION_PHASE_FRAMEWORK_STRUCTURING = "flintstones.resolutionphase.frameworkstructuring";
	public static final String RESOLUTION_PHASE_GATHERING = "flintstones.resolutionphase.gathering";
	public static final String RESOLUTION_PHASE_RATING = "flintstones.resolutionphase.rating";
	public static final String RESOLUTION_PHASE_SENSITIVITY_ANALYSIS = "flintstones.resolutionphase.sensitivityanalysis";
	
	private XMLValues() {
		
	}
}
