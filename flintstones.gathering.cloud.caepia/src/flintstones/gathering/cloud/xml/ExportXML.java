package flintstones.gathering.cloud.xml;

import java.io.FileWriter;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.eclipse.rap.rwt.RWT;

import flintstones.gathering.cloud.dao.DAOProblemAlternatives;
import flintstones.gathering.cloud.dao.DAOProblemCriteria;
import flintstones.gathering.cloud.dao.DAOProblemDomainAssignments;
import flintstones.gathering.cloud.dao.DAOProblemDomains;
import flintstones.gathering.cloud.dao.DAOProblemExperts;
import flintstones.gathering.cloud.dao.DAOProblemValuations;
import flintstones.gathering.cloud.dao.DAOValuations;
import flintstones.gathering.cloud.model.KeyDomainAssignment;
import flintstones.gathering.cloud.model.Problem;
import flintstones.gathering.cloud.model.Valuations;
import mcdacw.valuation.domain.Domain;
import mcdacw.valuation.domain.fuzzyset.FuzzySet;
import mcdacw.valuation.domain.numeric.NumericIntegerDomain;
import mcdacw.valuation.domain.numeric.NumericRealDomain;
import mcdacw.valuation.valuation.IntegerIntervalValuation;
import mcdacw.valuation.valuation.IntegerValuation;
import mcdacw.valuation.valuation.LinguisticValuation;
import mcdacw.valuation.valuation.RealIntervalValuation;
import mcdacw.valuation.valuation.RealValuation;
import mcdacw.valuation.valuation.Valuation;
import mcdacw.valuation.valuation.hesitant.HesitantValuation;

public class ExportXML {
	
	private XMLOutputFactory _outputFactory;
	private XMLStreamWriter _writer;
	
	private String _file;
	
	private Problem _problem;

	public ExportXML(String file) {
		_file = file;
	}
	
	public void createExportFile() throws Exception {
		_problem = (Problem) RWT.getUISession().getAttribute("problem");
		
		_outputFactory = XMLOutputFactory.newInstance();
		
		createFlintstonesFile();
	}

	private void createFlintstonesFile() throws Exception {
		_writer = _outputFactory.createXMLStreamWriter(new FileWriter(_file));
		_writer.writeStartDocument();
		
		_writer.writeStartElement("resolution.scheme");
		_writer.writeAttribute("id", XMLValues.RESOLUTION_SCHEME);
		
		_writer.writeStartElement("resolution.phase");
		_writer.writeAttribute("id", XMLValues.RESOLUTION_PHASE_FRAMEWORK);
		
		createElements();		
		createDomains();
		createDomainsValuations();
		
		_writer.writeEndElement();
		
		_writer.writeStartElement("resolution.phase");
		_writer.writeAttribute("id", XMLValues.RESOLUTION_PHASE_FRAMEWORK_STRUCTURING);
		
		createDomainAssignments();
		
		_writer.writeEndElement();
		
		_writer.writeStartElement("resolution.phase");
		_writer.writeAttribute("id", XMLValues.RESOLUTION_PHASE_GATHERING);
		
		createValuations();
		
		_writer.writeEndElement();
		
		_writer.writeStartElement("resolution.phase");
		_writer.writeAttribute("id", XMLValues.RESOLUTION_PHASE_RATING);
		_writer.writeEndElement();
		
		_writer.writeStartElement("resolution.phase");
		_writer.writeAttribute("id", XMLValues.RESOLUTION_PHASE_SENSITIVITY_ANALYSIS);
		_writer.writeEndElement();
		
		_writer.writeEndElement();
		
		_writer.writeEndDocument();
		_writer.flush();
		_writer.close();
	}

	private void createElements() throws Exception {
		_writer.writeStartElement("elements");
		
		List<String> experts = DAOProblemExperts.getDAO().getProblemExperts(_problem);
		_writer.writeStartElement("experts");
		for(String experto: experts) {
			_writer.writeStartElement("expert");
			_writer.writeAttribute("id", experto);
			_writer.writeEndElement();
		}
		_writer.writeEndElement();
		
		List<String> alternatives = DAOProblemAlternatives.getDAO().getProblemAlternatives(_problem);
		_writer.writeStartElement("alternatives");
		for(String alternative: alternatives) {
			_writer.writeStartElement("alternative");
			_writer.writeAttribute("id", alternative);
			_writer.writeEndElement();
		}
		_writer.writeEndElement();
		
		List<String> criteria = DAOProblemCriteria.getDAO().getProblemCriteria(_problem);
		_writer.writeStartElement("criteria");
		for(String criterion: criteria) {
			_writer.writeStartElement("criterion");
			_writer.writeAttribute("id", criterion);
			_writer.writeAttribute("cost", "false");
			_writer.writeEndElement();
		}
		_writer.writeEndElement();
		
		_writer.writeEndElement();
	}
	
	private void createDomains() throws Exception {
		_writer.writeStartElement("domain-set");
		
		String domainType = null;
		Map<String, Domain> domains = DAOProblemDomains.getDAO().getProblemDomains(_problem);
		for(String domainId: domains.keySet()) {
			
			Domain domain = domains.get(domainId);
			
			if(domain instanceof FuzzySet) {
				domainType = "flintstones.domain.linguistic";
			} else if(domain instanceof NumericIntegerDomain) {
				domainType = "flintstones.domain.numeric.integer";
			} else if(domain instanceof NumericRealDomain) {
				domainType = "flintstones.domain.numeric.real";
			}
			
			_writer.writeStartElement(domainType);
			_writer.writeAttribute("id", domainId);
			domain.save(_writer);
			_writer.writeEndElement();
			
		}
		_writer.writeEndElement();
	}
	
	private void createDomainsValuations() throws Exception {
		_writer.writeStartElement("domain-valuation");
		
		Map<String, String> domainValuations = DAOProblemValuations.getDAO().getDomainValuations(_problem.getId());
		for(String domain: domainValuations.keySet()) {
			_writer.writeStartElement("domain-id");
			_writer.writeAttribute("id", domain);
			_writer.writeEndElement();
			
			_writer.writeStartElement("valuation-id");
			_writer.writeAttribute("id", domainValuations.get(domain));
			_writer.writeEndElement();
		}
		_writer.writeEndElement();
	}
	
	private void createDomainAssignments() throws Exception {
		_writer.writeStartElement("domain-assignments");
		
		Map<KeyDomainAssignment, String> domainAssignments = DAOProblemDomainAssignments.getDAO().getProblemDomainAssignments(_problem);
		for(KeyDomainAssignment key: domainAssignments.keySet()) {
			_writer.writeStartElement("assignment");
			
			_writer.writeAttribute("expert", key.getExpert());
			_writer.writeAttribute("alternative", key.getAlternative());
			_writer.writeAttribute("criterion", key.getCriterion());
			_writer.writeAttribute("domain", domainAssignments.get(key));
			
			_writer.writeEndElement();
		}
		
		_writer.writeEndElement();
	}
	
	private void createValuations() throws Exception {
		List<String> experts = DAOProblemExperts.getDAO().getProblemExperts(_problem);
	
		_writer.writeStartElement("valuations");
		
		for(String expert: experts) {
			Valuations v = DAOValuations.getDAO().getValuations(_problem.getId(), _problem.getAssignment(expert), _problem.getDomains());
			
			Map<KeyDomainAssignment, Valuation> valuations = v.getValuations();
			String valuationType = null;
			for(KeyDomainAssignment key: valuations.keySet()) {
				Valuation valuation = valuations.get(key);
				
				if(valuation instanceof HesitantValuation) {
					valuationType = "flintstones.valuation.hesitant";
				} else if(valuation instanceof IntegerValuation) {
					valuationType = "flintstones.valuation.integer";
				} else if(valuation instanceof IntegerIntervalValuation) {
					valuationType = "flintstones.valuation.integer.interval";
				} else if(valuation instanceof LinguisticValuation) {
					valuationType = "flintstones.valuation.linguistic";
				} else if(valuation instanceof RealValuation) {
					valuationType = "flintstones.valuation.real";
				} else if(valuation instanceof RealIntervalValuation) {
					valuationType = "flintstones.valuation.real.interval";
				} 
				
				_writer.writeStartElement(valuationType);
				
				_writer.writeAttribute("domain-id", valuation.getDomain().getId());
				_writer.writeAttribute("expert", key.getExpert());
				_writer.writeAttribute("alternative", key.getAlternative());
				_writer.writeAttribute("criterion", key.getCriterion());
				
				valuation.save(_writer);
				
				_writer.writeEndElement();
			}
		}
		
		_writer.writeEndElement();
	}
}
