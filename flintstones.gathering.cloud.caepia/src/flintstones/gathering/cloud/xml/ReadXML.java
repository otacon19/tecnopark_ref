package flintstones.gathering.cloud.xml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.eclipse.rap.rwt.RWT;

import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.model.User;
import mcdacw.valuation.domain.Domain;
import mcdacw.valuation.domain.fuzzyset.FuzzySet;
import mcdacw.valuation.domain.fuzzyset.function.types.TrapezoidalFunction;
import mcdacw.valuation.domain.fuzzyset.label.LabelLinguisticDomain;
import mcdacw.valuation.domain.fuzzyset.label.LabelSetLinguisticDomain;
import mcdacw.valuation.domain.numeric.NumericIntegerDomain;
import mcdacw.valuation.domain.numeric.NumericRealDomain;

public class ReadXML {

	private String _file;
	private XMLInputFactory _inputFactory;

	private XMLEventReader _eventReader;
	private XMLEvent _event;
	
	private Problem _problem;

	private ReadXML() throws Exception {
		User user = (User) RWT.getUISession().getAttribute("user");
		_problem = new Problem("", user, new LinkedList<String>(), new LinkedList<String>(), new LinkedList<String>(), new HashMap<String, Domain>());
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
		readDomains();
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
		String extensionId = null, endtag = null, id = null;
		boolean end = false;
		
		goToStartElement("domain-set"); //$NON-NLS-1$
		
		while (hasNext() && !end) {
			event = next();
			if (event.isStartElement()) {
				extensionId = getStartElementLocalPart();
				id = getStartElementAttribute("id"); //$NON-NLS-1$
				try {
					readDomain(id, extensionId);
				} catch (Exception e) {
					throw new XMLStreamException();
				}

			} else if (event.isEndElement()) {
				endtag = getEndElementLocalPart();
				if (endtag.equals("domain-set")) { //$NON-NLS-1$
					end = true;
				}
			}
		}
	}

	private void readDomain(String id, String extensionId) throws Exception {
		if(extensionId.equals(XMLValues.NUMERIC_INTEGER_DOMAIN)) {
			readNumericIntegerDomain(id);
		} else if(extensionId.equals(XMLValues.NUMERIC_REAL_DOMAIN)) {
			readNumericRealDomain(id);
		} else if(extensionId.equals(XMLValues.FUZZY_SET)) {
			readFuzzySetDomain(id);
		}
	}

	private void readNumericIntegerDomain(String id) {
		boolean inRange = Boolean.parseBoolean(getStartElementAttribute("inRange")); //$NON-NLS-1$
		int min = Integer.parseInt(getStartElementAttribute("min")); //$NON-NLS-1$
		int max = Integer.parseInt(getStartElementAttribute("max")); //$NON-NLS-1$
		
		NumericIntegerDomain d = new NumericIntegerDomain();
		d.setId(id);
		d.setName(id);
		d.setMinMax(min, max);
		d.setInRange(inRange);
		d.setType("Entero");
		
		_problem.getDomains().put(d.getId(), d);
	}
	
	private void readNumericRealDomain(String id) {
		boolean inRange = Boolean.parseBoolean(getStartElementAttribute("inRange")); //$NON-NLS-1$
		double min = Double.parseDouble(getStartElementAttribute("min")); //$NON-NLS-1$
		double max = Double.parseDouble(getStartElementAttribute("max")); //$NON-NLS-1$
		
		NumericRealDomain d = new NumericRealDomain();
		d.setId(id);
		d.setName(id);
		d.setMinMax(min, max);
		d.setInRange(inRange);
		d.setType("Real");
		
		_problem.getDomains().put(d.getId(), d);
	}

	private void readFuzzySetDomain(String id) throws Exception  {
		XMLEvent event;
		String v, endtag = null;
		Double value = null;
		boolean end = false;
		
		List<Double> values = new LinkedList<Double>();
		
		FuzzySet d = new FuzzySet();
		LabelSetLinguisticDomain labelSet = null;
		
		goToStartElement("values"); //$NON-NLS-1$
		
		while (hasNext() && !end) {
			event = next();
			if (event.isStartElement()) {
				if ("value".equals(getStartElementLocalPart())) { //$NON-NLS-1$
					v = getStartElementAttribute("v"); //$NON-NLS-1$
					value = new Double(v);
					values.add(value);
				} else {
					labelSet = new LabelSetLinguisticDomain();
					readLabelSet(labelSet);
				}
			} else if (event.isEndElement()) {
				endtag = getEndElementLocalPart();
				if (endtag.equals("labelSet")) { //$NON-NLS-1$
					d.setId(id);
					d.setName(id);
					d.setLabelSet(labelSet);
					d.setValues(values);
					d.setType("Lingüístico");
					end = true;
				}
			}
		}
		
		_problem.getDomains().put(d.getId(), d);
	}
	
	private void readLabelSet(LabelSetLinguisticDomain labelSet) throws Exception {
		XMLEvent event;
		String name = null, endtag = null, localPart = null;
		boolean end = false;
		
		LabelLinguisticDomain label = null;
		
		goToStartElement("labels"); //$NON-NLS-1$
		while (hasNext() && !end) {
			event = next();
			if (event.isStartElement()) {
				localPart = getStartElementLocalPart();
				name = getStartElementAttribute("label"); //$NON-NLS-1$
				try {
					label = new LabelLinguisticDomain();
					label.setName(name);
					readLabel(label);
				} catch (Exception e) {
					throw new XMLStreamException();
				}
			} else if (event.isEndElement()) {
				endtag = getEndElementLocalPart();
				if (endtag.equals(localPart)) {
					labelSet.addLabel(label);
				} else if (endtag.equals("labels")) { //$NON-NLS-1$
					end = true;
				}
			}
		}
		
	}

	private void readLabel(LabelLinguisticDomain label) throws Exception {
		goToStartElement("semantic"); //$NON-NLS-1$

		readTrapezoidalFunction(label);
	}

	private void readTrapezoidalFunction(LabelLinguisticDomain label) {
		double a = Double.parseDouble(getStartElementAttribute("a")); //$NON-NLS-1$
		double b = Double.parseDouble(getStartElementAttribute("b")); //$NON-NLS-1$
		double c = Double.parseDouble(getStartElementAttribute("c")); //$NON-NLS-1$
		double d = Double.parseDouble(getStartElementAttribute("d")); //$NON-NLS-1$
		double limits[] = new double[]{a, b, c ,d};
		
		TrapezoidalFunction semantic = new TrapezoidalFunction(limits);
		label.setSemantic(semantic);
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
