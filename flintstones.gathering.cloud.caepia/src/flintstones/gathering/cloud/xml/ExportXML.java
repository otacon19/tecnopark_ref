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
		_problem = (Problem) RWT.getUISession().getAttribute("problem"); //$NON-NLS-1$

		_outputFactory = XMLOutputFactory.newInstance();

		createFlintstonesFile();
	}

	private void createFlintstonesFile() throws Exception {
		_writer = _outputFactory.createXMLStreamWriter(new FileWriter(_file));
		_writer.writeStartDocument();

		_writer.writeStartElement("resolution.scheme"); //$NON-NLS-1$
		_writer.writeAttribute("id", XMLValues.RESOLUTION_SCHEME); //$NON-NLS-1$

		_writer.writeStartElement("resolution.phase"); //$NON-NLS-1$
		_writer.writeAttribute("id", XMLValues.RESOLUTION_PHASE_FRAMEWORK); //$NON-NLS-1$

		createElements();
		createDomains();
		createDomainsValuations();

		_writer.writeEndElement();

		_writer.writeStartElement("resolution.phase"); //$NON-NLS-1$
		_writer.writeAttribute("id", XMLValues.RESOLUTION_PHASE_FRAMEWORK_STRUCTURING); //$NON-NLS-1$

		createDomainAssignments();

		_writer.writeEndElement();

		_writer.writeStartElement("resolution.phase"); //$NON-NLS-1$
		_writer.writeAttribute("id", XMLValues.RESOLUTION_PHASE_GATHERING); //$NON-NLS-1$

		createValuations();

		_writer.writeEndElement();

		_writer.writeStartElement("resolution.phase"); //$NON-NLS-1$
		_writer.writeAttribute("id", XMLValues.RESOLUTION_PHASE_RATING); //$NON-NLS-1$
		_writer.writeEndElement();

		_writer.writeStartElement("resolution.phase"); //$NON-NLS-1$
		_writer.writeAttribute("id", XMLValues.RESOLUTION_PHASE_SENSITIVITY_ANALYSIS); //$NON-NLS-1$
		_writer.writeEndElement();

		_writer.writeEndElement();

		_writer.writeEndDocument();
		_writer.flush();
		_writer.close();
	}

	private void createElements() throws Exception {
		_writer.writeStartElement("elements"); //$NON-NLS-1$

		List<String> experts = DAOProblemExperts.getDAO().getProblemExperts(_problem);
		_writer.writeStartElement("experts"); //$NON-NLS-1$
		for (String experto : experts) {
			_writer.writeStartElement("expert"); //$NON-NLS-1$
			_writer.writeAttribute("id", experto); //$NON-NLS-1$
			_writer.writeEndElement();
		}
		_writer.writeEndElement();

		List<String> alternatives = DAOProblemAlternatives.getDAO().getProblemAlternatives(_problem);
		_writer.writeStartElement("alternatives"); //$NON-NLS-1$
		for (String alternative : alternatives) {
			_writer.writeStartElement("alternative"); //$NON-NLS-1$
			_writer.writeAttribute("id", alternative); //$NON-NLS-1$
			_writer.writeEndElement();
		}
		_writer.writeEndElement();

		List<String> criteria = DAOProblemCriteria.getDAO().getProblemCriteria(_problem);
		_writer.writeStartElement("criteria"); //$NON-NLS-1$
		for (String criterion : criteria) {
			_writer.writeStartElement("criterion"); //$NON-NLS-1$
			_writer.writeAttribute("id", criterion); //$NON-NLS-1$
			_writer.writeAttribute("cost", "false"); //$NON-NLS-1$ //$NON-NLS-2$
			_writer.writeEndElement();
		}
		_writer.writeEndElement();

		_writer.writeEndElement();
	}

	private void createDomains() throws Exception {
		_writer.writeStartElement("domain-set"); //$NON-NLS-1$

		String domainType = null;
		Map<String, Domain> domains = DAOProblemDomains.getDAO().getProblemDomains(_problem);
		for (String domainId : domains.keySet()) {

			Domain domain = domains.get(domainId);

			if (domain instanceof FuzzySet) {
				domainType = "flintstones.domain.linguistic"; //$NON-NLS-1$
			} else if (domain instanceof NumericIntegerDomain) {
				domainType = "flintstones.domain.numeric.integer"; //$NON-NLS-1$
			} else if (domain instanceof NumericRealDomain) {
				domainType = "flintstones.domain.numeric.real"; //$NON-NLS-1$
			}

			_writer.writeStartElement(domainType);
			_writer.writeAttribute("id", domainId); //$NON-NLS-1$
			domain.save(_writer);
			_writer.writeEndElement();
		}
		
		_writer.writeEndElement();
	}

	private void createDomainsValuations() throws Exception {
		_writer.writeStartElement("domain-valuation"); //$NON-NLS-1$

		Map<String, String> domainValuations = DAOProblemValuations.getDAO().getDomainValuations(_problem.getId());
		for (String domain : domainValuations.keySet()) {
			_writer.writeStartElement("domain-id"); //$NON-NLS-1$
			_writer.writeAttribute("id", domain); //$NON-NLS-1$
			_writer.writeEndElement();

			_writer.writeStartElement("valuation-id"); //$NON-NLS-1$
			_writer.writeAttribute("id", domainValuations.get(domain)); //$NON-NLS-1$
			_writer.writeEndElement();
		}
		
		_writer.writeStartElement("domain-id"); //$NON-NLS-1$
		_writer.writeAttribute("id", "auto_generated_importance"); //$NON-NLS-1$ //$NON-NLS-2$
		_writer.writeEndElement();
		
		_writer.writeStartElement("valuation-id"); //$NON-NLS-1$
		_writer.writeAttribute("id", "flintstones.valuation.hesitant"); //$NON-NLS-1$ //$NON-NLS-2$
		_writer.writeEndElement();
		
		_writer.writeStartElement("domain-id"); //$NON-NLS-1$
		_writer.writeAttribute("id", "auto_generated_knowledge"); //$NON-NLS-1$ //$NON-NLS-2$
		_writer.writeEndElement();

		_writer.writeStartElement("valuation-id"); //$NON-NLS-1$
		_writer.writeAttribute("id", "flintstones.valuation.linguistic"); //$NON-NLS-1$ //$NON-NLS-2$
		_writer.writeEndElement();
		
		
		_writer.writeEndElement();
	}

	private void createDomainAssignments() throws Exception {
		_writer.writeStartElement("domain-assignments"); //$NON-NLS-1$

		Map<KeyDomainAssignment, String> domainAssignments = DAOProblemDomainAssignments.getDAO().getProblemDomainAssignments(_problem);
		for (KeyDomainAssignment key : domainAssignments.keySet()) {
			_writer.writeStartElement("assignment"); //$NON-NLS-1$

			_writer.writeAttribute("expert", key.getExpert()); //$NON-NLS-1$
			_writer.writeAttribute("alternative", key.getAlternative()); //$NON-NLS-1$
			_writer.writeAttribute("criterion", key.getCriterion()); //$NON-NLS-1$
			_writer.writeAttribute("domain", domainAssignments.get(key)); //$NON-NLS-1$

			_writer.writeEndElement();
		}

		_writer.writeEndElement();
	}

	private void createValuations() throws Exception {
		List<String> experts = DAOProblemExperts.getDAO().getProblemExperts(_problem);

		_writer.writeStartElement("valuations"); //$NON-NLS-1$

		for (String expert : experts) {
			Valuations v = DAOValuations.getDAO().getValuations(_problem.getId(), _problem.getAssignment(expert), _problem.getDomains());

			Map<KeyDomainAssignment, Valuation> valuations = v.getValuations();
			String valuationType = null;
			for (KeyDomainAssignment key : valuations.keySet()) {
				Valuation valuation = valuations.get(key);
				
				if (valuation instanceof HesitantValuation) {
					valuationType = "flintstones.valuation.hesitant"; //$NON-NLS-1$
				} else if (valuation instanceof IntegerValuation) {
					valuationType = "flintstones.valuation.integer"; //$NON-NLS-1$
				} else if (valuation instanceof IntegerIntervalValuation) {
					valuationType = "flintstones.valuation.integer.interval"; //$NON-NLS-1$
				} else if (valuation instanceof LinguisticValuation) {
					valuationType = "flintstones.valuation.linguistic"; //$NON-NLS-1$
				} else if (valuation instanceof RealValuation) {
					valuationType = "flintstones.valuation.real"; //$NON-NLS-1$
				} else if (valuation instanceof RealIntervalValuation) {
					valuationType = "flintstones.valuation.real.interval"; //$NON-NLS-1$
				}

				_writer.writeStartElement(valuationType);

				_writer.writeAttribute("domain-id", valuation.getDomain().getId()); //$NON-NLS-1$
				_writer.writeAttribute("expert", key.getExpert()); //$NON-NLS-1$
				_writer.writeAttribute("alternative", key.getAlternative()); //$NON-NLS-1$
				_writer.writeAttribute("criterion", key.getCriterion()); //$NON-NLS-1$

				valuation.save(_writer);

				_writer.writeEndElement();
			}
		}
		
		_writer.writeEndElement();
	}
}
