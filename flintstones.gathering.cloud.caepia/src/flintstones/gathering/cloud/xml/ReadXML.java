package flintstones.gathering.cloud.xml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import mcdacw.valuation.domain.IDomain;
import mcdacw.valuation.domain.fuzzyset.HesitantFuzzySet;
import mcdacw.valuation.domain.fuzzyset.Label;
import mcdacw.valuation.domain.numeric.IntervalNumericDomain;
import mcdacw.valuation.domain.numeric.NumericDomain;

import org.eclipse.rap.rwt.RWT;

import flintstones.gathering.cloud.model.Domain;
import flintstones.gathering.cloud.model.Key;
import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.model.User;

public class ReadXML {

	private String _file;
	private XMLInputFactory _inputFactory;
	
	private Problem _problem;

	private XMLEventReader _eventReader;
	private XMLEvent _event;
	
	private Map<String, Domain> domains;
	private Map<Key, String> domainAssignments;

	private ReadXML() {
		User user = (User) RWT.getUISession().getAttribute("user");
		_problem = new Problem("", user, new LinkedList<String>(), new LinkedList<String>(), new LinkedList<String>());

	}

	public ReadXML(String file) throws Exception {
		this();
		
		_file = file;
		readFile();
	}

	private void readFile() throws Exception {
		_inputFactory = XMLInputFactory.newInstance();
		InputStream in = new FileInputStream(_file);
		_eventReader = _inputFactory.createXMLEventReader(in);
		
		readProblemElements();
	}

	private void readProblemElements() throws Exception {

		readExperts();
		readAlternatives();
		readCriteria();
		
		System.out.println(_problem.getExperts());
		System.out.println(_problem.getCriteria());
		System.out.println(_problem.getAlternatives());
	}
	
	public void readExperts() throws XMLStreamException {
		goToStartElement("experts"); //$NON-NLS-1$

		XMLEvent event;
		String id;
		boolean end = false;
		while (hasNext() && !end) {
			event = next();

			if (event.isStartElement()) {
				if ("expert".equals(getStartElementLocalPart())) { //$NON-NLS-1$
					id = getStartElementAttribute("id"); //$NON-NLS-1$
					_problem.getExperts().add(id);
				}
			} else if (event.isEndElement()) {
				if ("experts".equals(getEndElementLocalPart())) { //$NON-NLS-1$
					end = true;
					Collections.sort(_problem.getExperts());
				}
			}
		}
	}
	
	//TODO cost?
	public void readCriteria() throws XMLStreamException {
		goToStartElement("criteria"); //$NON-NLS-1$

		XMLEvent event;
		String id;
		//Boolean cost;
		boolean end = false;
		while (hasNext() && !end) {
			event = next();

			if (event.isStartElement()) {
				if ("criterion".equals(getStartElementLocalPart())) { //$NON-NLS-1$
					id = getStartElementAttribute("id"); //$NON-NLS-1$
					//cost = Boolean.parseBoolean(getStartElementAttribute("cost")); //$NON-NLS-1$
					_problem.getCriteria().add(id);
				}
			} else if (event.isEndElement()) {
				if ("criteria".equals(getEndElementLocalPart())) { //$NON-NLS-1$
					end = true;
					Collections.sort(_problem.getCriteria());
				}
			}
		}
	}
	
	public void readAlternatives() throws XMLStreamException {
		goToStartElement("alternatives"); //$NON-NLS-1$

		XMLEvent event;
		String id;
		boolean end = false;
		while (hasNext() && !end) {
			event = next();

			if (event.isStartElement()) {
				if ("alternative".equals(getStartElementLocalPart())) { //$NON-NLS-1$
					id = getStartElementAttribute("id");
					_problem.getAlternatives().add(id); //$NON-NLS-1$
				}
			} else if (event.isEndElement()) {
				if ("alternatives".equals(getEndElementLocalPart())) { //$NON-NLS-1$
					end = true;
					Collections.sort(_problem.getAlternatives());
				}
			}
		}
	}
	

	private void readDomains() throws Exception {

		XMLEvent event;
		boolean exit = false;

		domains = new HashMap<String, Domain>();

		do {
			event = _eventReader.nextEvent();
			if (event.isStartElement()) {
				readDomain();
			} else if (event.isEndElement()) {
				exit = true;
			}
		} while (!exit);

		_problem.setDomains(domains);

	}

	private void readDomain() throws Exception {

		XMLEvent event;
		StartElement startElement;
		EndElement endElement;
		String localPart;
		String content;
		boolean exit = false;
		boolean inName = false;
		boolean inType = false;
		IDomain domain = null;
		String name = null;
		String type = null;

		do {
			event = _eventReader.nextEvent();
			if (event.isStartElement()) {
				startElement = event.asStartElement();
				localPart = startElement.getName().getLocalPart();
				if (XMLValues.NAME.equals(localPart)) {
					inName = true;
				} else if (XMLValues.TYPE.equals(localPart)) {
					inType = true;
				}

			} else if (event.isEndElement()) {
				endElement = event.asEndElement();
				localPart = endElement.getName().getLocalPart();
				if (XMLValues.DOMAIN.equals(localPart)) {
					exit = true;
				} else if (inName) {
					inName = false;
				} else if (inType) {
					inType = false;
					if (XMLValues.NUMERIC_DOMAIN.equals(type)) {
						domain = readNumericDomain();
					} else if (XMLValues.HESITANT_FUZZY_SET.equals(type)) {
						domain = readHesitantFuzzySet();
					} else if (XMLValues.INTERVAL_NUMERIC_DOMAIN.equals(type)) {
						domain = readIntervalNumericDomain();
					}
					domains.put(name, new Domain(name, type, domain));
				}
			} else {
				content = event.asCharacters().getData();
				if ((content.indexOf(XMLValues.END) == -1)
						&& (content.indexOf(XMLValues.TAB) == -1)) {
					if (inName) {
						name = content;
					} else if (inType) {
						type = content;
					}
				}
			}
		} while (!exit);
	}

	private NumericDomain readNumericDomain() throws Exception {

		NumericDomain result = null;

		XMLEvent event;
		StartElement startElement;
		EndElement endElement;
		String localPart;
		String content;
		boolean exit = false;
		boolean inMin = false;
		boolean inMax = false;
		boolean inValues = false;
		boolean inRange = false;
		Double min = null;
		Double max = null;
		Integer type = null;
		Boolean range = null;

		do {
			event = _eventReader.nextEvent();
			if (event.isStartElement()) {
				startElement = event.asStartElement();
				localPart = startElement.getName().getLocalPart();
				if (XMLValues.MIN.equals(localPart)) {
					inMin = true;
				} else if (XMLValues.MAX.equals(localPart)) {
					inMax = true;
				} else if (XMLValues.VALUES.equals(localPart)) {
					inValues = true;
				} else if (XMLValues.RANGE.equals(localPart)) {
					inRange = true;
				}
			} else if (event.isEndElement()) {
				endElement = event.asEndElement();
				localPart = endElement.getName().getLocalPart();
				if (XMLValues.MIN.equals(localPart)) {
					inMin = false;
				} else if (XMLValues.MAX.equals(localPart)) {
					inMax = false;
				} else if (XMLValues.VALUES.equals(localPart)) {
					inValues = false;
				} else if (XMLValues.RANGE.equals(localPart)) {
					inRange = false;
					exit = true;
				}
			} else {
				content = event.asCharacters().getData();
				if ((content.indexOf(XMLValues.END) == -1)
						&& (content.indexOf(XMLValues.TAB) == -1)) {
					if (inMin) {
						min = Double.parseDouble(content);
					} else if (inMax) {
						max = Double.parseDouble(content);
					} else if (inValues) {
						type = Integer.parseInt(content);
					} else if (inRange) {
						range = Boolean.parseBoolean(content);
					}
				}
			}
		} while (!exit);

		result = new NumericDomain(min, max);
		result.setType(type);
		result.setInRange(range);

		return result;
	}

	private IntervalNumericDomain readIntervalNumericDomain() throws Exception {

		IntervalNumericDomain result = null;

		XMLEvent event;
		StartElement startElement;
		EndElement endElement;
		String localPart;
		String content;
		boolean exit = false;
		boolean inMin = false;
		boolean inMax = false;
		boolean inValues = false;
		boolean inRange = false;
		Double min = null;
		Double max = null;
		Integer type = null;
		Boolean range = null;

		do {
			event = _eventReader.nextEvent();
			if (event.isStartElement()) {
				startElement = event.asStartElement();
				localPart = startElement.getName().getLocalPart();
				if (XMLValues.MIN.equals(localPart)) {
					inMin = true;
				} else if (XMLValues.MAX.equals(localPart)) {
					inMax = true;
				} else if (XMLValues.VALUES.equals(localPart)) {
					inValues = true;
				} else if (XMLValues.RANGE.equals(localPart)) {
					inRange = true;
				}
			} else if (event.isEndElement()) {
				endElement = event.asEndElement();
				localPart = endElement.getName().getLocalPart();
				if (XMLValues.MIN.equals(localPart)) {
					inMin = false;
				} else if (XMLValues.MAX.equals(localPart)) {
					inMax = false;
				} else if (XMLValues.VALUES.equals(localPart)) {
					inValues = false;
				} else if (XMLValues.RANGE.equals(localPart)) {
					inRange = false;
					exit = true;
				}
			} else {
				content = event.asCharacters().getData();
				if ((content.indexOf(XMLValues.END) == -1)
						&& (content.indexOf(XMLValues.TAB) == -1)) {
					if (inMin) {
						min = Double.parseDouble(content);
					} else if (inMax) {
						max = Double.parseDouble(content);
					} else if (inValues) {
						type = Integer.parseInt(content);
					} else if (inRange) {
						range = Boolean.parseBoolean(content);
					}
				}
			}
		} while (!exit);

		result = new IntervalNumericDomain(min, max);
		result.setType(type);
		result.setInRange(range);

		return result;
	}

	private HesitantFuzzySet readHesitantFuzzySet() throws Exception {

		HesitantFuzzySet result = null;

		boolean exit = false;

		XMLEvent event;
		StartElement startElement;
		EndElement endElement;
		String localPart;
		String content;
		boolean inName = false;
		boolean inSemantic = false;
		boolean inType = false;
		boolean inA = false;
		boolean inB = false;
		boolean inC = false;
		boolean inD = false;
		boolean inMeasure = false;
		Label label = null;
		String name = null;
		String type = null;
		Double a = null;
		Double b = null;
		Double c = null;
		Double d = null;
		Double measure = null;

		List<Label> labels = new LinkedList<Label>();
		List<Double> measures = new LinkedList<Double>();

		do {
			event = _eventReader.nextEvent();
			if (event.isStartElement()) {
				startElement = event.asStartElement();
				localPart = startElement.getName().getLocalPart();
				if (XMLValues.NAME.equals(localPart)) {
					inName = true;
				} else if (XMLValues.SEMANTIC.equals(localPart)) {
					inSemantic = true;
				} else if (XMLValues.TYPE.equals(localPart)) {
					inType = true;
				} else if (XMLValues.A.equals(localPart)) {
					inA = true;
				} else if (XMLValues.B.equals(localPart)) {
					inB = true;
				} else if (XMLValues.C.equals(localPart)) {
					inC = true;
				} else if (XMLValues.D.equals(localPart)) {
					inD = true;
				} else if (XMLValues.MEASURE.equals(localPart)) {
					inMeasure = true;
				}
			} else if (event.isEndElement()) {
				endElement = event.asEndElement();
				localPart = endElement.getName().getLocalPart();
				if (XMLValues.LABELS.equals(localPart)) {
					exit = true;
				} else if (XMLValues.NAME.equals(localPart)) {
					inName = false;
				} else if (XMLValues.SEMANTIC.equals(localPart)) {
					inSemantic = false;
				} else if (XMLValues.TYPE.equals(localPart)) {
					inType = false;
				} else if (XMLValues.A.equals(localPart)) {
					inA = false;
				} else if (XMLValues.B.equals(localPart)) {
					inB = false;
				} else if (XMLValues.C.equals(localPart)) {
					inC = false;
				} else if (XMLValues.D.equals(localPart)) {
					inD = false;
					if (inSemantic) {
						if (XMLValues.TRAPEZOIDAL_MEMBERSHIP_FUNCTION
								.equals(type)) {
							label = mcdacw.valuation.domain.fuzzyset.Activator
									.getActivator()
									.getLabelFactory()
									.buildTrapezoidalLabel(name,
											new double[] { a, b, c, d });
							labels.add(label);
						}
					}
				} else if (XMLValues.MEASURE.equals(localPart)) {
					inMeasure = false;
					measures.add(measure);
				}
			} else {
				content = event.asCharacters().getData();
				if ((content.indexOf(XMLValues.END) == -1)
						&& (content.indexOf(XMLValues.TAB) == -1)) {
					if (inName) {
						name = content;
					} else if (inType) {
						type = content;
					} else if (inA) {
						a = Double.parseDouble(content);
					} else if (inB) {
						b = Double.parseDouble(content);
					} else if (inC) {
						c = Double.parseDouble(content);
					} else if (inD) {
						d = Double.parseDouble(content);
					} else if (inMeasure) {
						measure = Double.parseDouble(content);
					}
				}
			}
		} while (!exit);

		result = new HesitantFuzzySet(labels, measures);
		return result;
	}

	private void readDomainsAssignments() throws Exception {
		XMLEvent event;
		boolean exit = false;

		domainAssignments = new HashMap<Key, String>();

		do {
			event = _eventReader.nextEvent();
			if (event.isStartElement()) {
				readDomainAssignment();
			} else if (event.isEndElement()) {
				exit = true;
			}
		} while (!exit);

		_problem.setDomainAssignments(domainAssignments);
	}

	@SuppressWarnings("unused")
	private void readDomainAssignment() throws Exception {

		XMLEvent event;
		StartElement startElement;
		EndElement endElement;
		String localPart;
		String content;
		boolean exit = false;
		boolean inExpert = false;
		boolean inAlternative = false;
		boolean inCriterion = false;
		boolean inDomain = false;
		boolean inBlock = false;
		String expert = null;
		String alternative = null;
		String criterion = null;
		String domain = null;
		Boolean blockDomain = null;

		do {
			event = _eventReader.nextEvent();
			if (event.isStartElement()) {
				startElement = event.asStartElement();
				localPart = startElement.getName().getLocalPart();
				if (XMLValues.EXPERT.equals(localPart)) {
					inExpert = true;
				} else if (XMLValues.ALTERNATIVE.equals(localPart)) {
					inAlternative = true;
				} else if (XMLValues.CRITERION.equals(localPart)) {
					inCriterion = true;
				} else if (XMLValues.DOMAIN.equals(localPart)) {
					inDomain = true;
				} else if (XMLValues.BLOCK.equals(localPart)) {
					inBlock = true;
				}
			} else if (event.isEndElement()) {
				endElement = event.asEndElement();
				localPart = endElement.getName().getLocalPart();
				if (XMLValues.EXPERT.equals(localPart)) {
					inExpert = false;
				} else if (XMLValues.ALTERNATIVE.equals(localPart)) {
					inAlternative = false;
				} else if (XMLValues.CRITERION.equals(localPart)) {
					inCriterion = false;
				} else if (XMLValues.DOMAIN.equals(localPart)) {
					inDomain = false;
				} else if (XMLValues.BLOCK.equals(localPart)) {
					inBlock = false;
				} else if (XMLValues.ASSIGNMENT.equals(localPart)) {
					exit = true;
				}
			} else {
				content = event.asCharacters().getData();
				if ((content.indexOf(XMLValues.END) == -1)
						&& (content.indexOf(XMLValues.TAB) == -1)) {
					if (inExpert) {
						expert = content;
					} else if (inAlternative) {
						alternative = content;
					} else if (inCriterion) {
						criterion = content;
					} else if (inDomain) {
						domain = content;
					} else if (inBlock) {
						blockDomain = Boolean.parseBoolean(content);
					}
				}
			}
		} while (!exit);

		if (alternative.toLowerCase().equals("importancia")) {
			for (String c : _problem.getCriteria()) {
				domainAssignments.put(new Key(alternative, c), domain);
			}
		} else {
			List<String> criteria = null;
			if (criterion.equals("null")) {
				criteria = _problem.getCriteria();
			} else {
				criteria = new LinkedList<String>();
				criteria.add(criterion);
			}

			for (String c : criteria) {
				for (String a : _problem.getAlternatives()) {
					if (!a.toLowerCase().equals("importancia")) {
						domainAssignments.put(new Key(a, c), domain);
					}
				}
			}
		}
	}
	
	public String getStartElementLocalPart() {
		StartElement startElement = _event.asStartElement();
		return startElement.getName().getLocalPart();
	}

	public String getEndElementLocalPart() {
		EndElement endElement = _event.asEndElement();
		return endElement.getName().getLocalPart();
	}

	public void goToStartElement(String name) throws XMLStreamException {
		boolean find = false;
		while (hasNext() && !find) {
			if (next().isStartElement()) {
				if (name.equals(getStartElementLocalPart())) {
					find = true;
				}
			}
		}
	}

	public void goToEndElement(String name) throws XMLStreamException {
		boolean find = false;
		while (hasNext() && !find) {
			if (next().isEndElement()) {
				if (name.equals(getEndElementLocalPart())) {
					find = true;
				}
			}
		}
	}

	public String getStartElementAttribute(String name) {
		return _event.asStartElement().getAttributeByName(new QName(name))
				.getValue();
	}
	
	public boolean hasNext() {
		return _eventReader.hasNext();
	}

	public XMLEvent next() throws XMLStreamException {
		try {
			_event = _eventReader.nextEvent();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return _event;
	}

	public Problem getProblem() {
		return _problem;
	}
}
