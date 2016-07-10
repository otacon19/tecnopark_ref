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
import flintstones.gathering.cloud.model.KeyDomainAssignment;
import flintstones.gathering.cloud.model.Problem;
import mcdacw.valuation.domain.Domain;

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
			_writer.writeAttribute("id", experto);
			_writer.writeEndElement();
		}
		
		_writer.writeStartElement("alternatives");
		List<String> alternatives = DAOProblemAlternatives.getDAO().getProblemAlternatives(_problem);
		for(String alternative: alternatives) {
			_writer.writeAttribute("id", alternative);
			_writer.writeEndElement();
		}
		
		_writer.writeStartElement("criteria");
		List<String> criteria = DAOProblemCriteria.getDAO().getProblemCriteria(_problem);
		for(String criterion: criteria) {
			_writer.writeAttribute("id", criterion);
			_writer.writeAttribute("cost", "false");
			_writer.writeEndElement();
		}
		
		_writer.writeEndElement();
	}
	
	private void createDomains() throws Exception {
		_writer.writeStartElement("domain-set");
		
		Map<String, Domain> domains = DAOProblemDomains.getDAO().getProblemDomains(_problem);
		for(String domainId: domains.keySet()) {
			Domain domain = domains.get(domainId);
			domain.save(_writer);
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
	
	private void createValuations() {
		// TODO Auto-generated method stub
		
	}
}
