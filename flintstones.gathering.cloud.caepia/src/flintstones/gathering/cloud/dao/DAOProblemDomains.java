package flintstones.gathering.cloud.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import sinbad2.database.Database;
import sinbad2.database.DatabaseManager;
import flintstones.gathering.cloud.model.Problem;
import mcdacw.valuation.domain.Domain;
import mcdacw.valuation.domain.fuzzyset.FuzzySet;
import mcdacw.valuation.domain.fuzzyset.function.types.TrapezoidalFunction;
import mcdacw.valuation.domain.fuzzyset.label.LabelLinguisticDomain;
import mcdacw.valuation.domain.fuzzyset.label.LabelSetLinguisticDomain;
import mcdacw.valuation.domain.numeric.NumericIntegerDomain;
import mcdacw.valuation.domain.numeric.NumericRealDomain;

public class DAOProblemDomains {

	public static final String TABLE = "domains"; //$NON-NLS-1$
	public static final String ID = "id"; //$NON-NLS-1$
	public static final String PROBLEM = "problem"; //$NON-NLS-1$
	public static final String TYPE = "type"; //$NON-NLS-1$
	public static final String DOMAIN = "domain"; //$NON-NLS-1$

	private static DAOProblemDomains _dao = null;

	private DAOProblemDomains() {
	}

	public static DAOProblemDomains getDAO() {

		if (_dao == null) {
			_dao = new DAOProblemDomains();
		}

		return _dao;
	}

	public static String getCreationTableSql() {
		String result = null;

		result = "create table " + TABLE + "(" + ID //$NON-NLS-1$ //$NON-NLS-2$
				+ " VARCHAR(50) NOT NULL, " + PROBLEM //$NON-NLS-1$
				+ " VARCHAR(50) NOT NULL, " + TYPE + " VARCHAR(255) NOT NULL, " + DOMAIN + " TEXT NOT NULL, PRIMARY KEY(" + ID + "," //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				+ PROBLEM + "));"; //$NON-NLS-1$

		return result;
	}

	public Connection getConnection() {
		Connection result = null;
		
		Database db = DatabaseManager.getInstance().getApplicationDatabase();

		if (db != null) {
			result = db.getConnection();
		}
		return result;
	}

	public void createProblemDomains(Problem problem) {
		boolean importanceDomain = false, knowledgeDomain = false;
		
		try {
			Connection c = getConnection();
			Statement st = c.createStatement();
			String problemId = problem.getId();
			Map<String, Domain> domains = problem.getDomains();
			Domain domain = null;
			for (String id : domains.keySet()) {
				
				if(id.equals("auto_generated_importance")) { //$NON-NLS-1$
					importanceDomain = true;
				}
				
				if(id.equals("auto_generated_knowledge")) { //$NON-NLS-1$
					knowledgeDomain = true;
				}
				
				domain = domains.get(id);
				st.executeUpdate("insert into " + TABLE + " values ('" + id + "','" + problemId + "','" + domain.getType() + "','" + domain.toString() + "')"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			}
			
			if(!importanceDomain) {
				
				List<LabelLinguisticDomain> labels = new LinkedList<LabelLinguisticDomain>();
				LabelLinguisticDomain nothing = new LabelLinguisticDomain("Absolutely_low_importance", new TrapezoidalFunction(new double[]{0, 0, 0, 0.17})); //$NON-NLS-1$
				labels.add(nothing);
				LabelLinguisticDomain veryBad = new LabelLinguisticDomain("Very_low_importance", new TrapezoidalFunction(new double[]{0, 0.17, 0.17, 0.33})); //$NON-NLS-1$
				labels.add(veryBad);
				LabelLinguisticDomain bad = new LabelLinguisticDomain("Low_importance", new TrapezoidalFunction(new double[]{0.17, 0.33, 0.33, 0.5})); //$NON-NLS-1$
				labels.add(bad);
				LabelLinguisticDomain medium = new LabelLinguisticDomain("Medium_importance", new TrapezoidalFunction(new double[]{0.33, 0.5, 0.5, 0.67})); //$NON-NLS-1$
				labels.add(medium);
				LabelLinguisticDomain good = new LabelLinguisticDomain("High_importance", new TrapezoidalFunction(new double[]{0.5, 0.67, 0.67, 0.83})); //$NON-NLS-1$
				labels.add(good);
				LabelLinguisticDomain veryGood = new LabelLinguisticDomain("Very_high_importance", new TrapezoidalFunction(new double[]{0.67, 0.83, 0.83, 1})); //$NON-NLS-1$
				labels.add(veryGood);
				LabelLinguisticDomain perfect = new LabelLinguisticDomain("Absolutely_high_importance", new TrapezoidalFunction(new double[]{0.83, 1, 1, 1})); //$NON-NLS-1$
				labels.add(perfect);
			
				FuzzySet importance = new FuzzySet(labels);
				importance.setId("auto_generated_importance"); //$NON-NLS-1$
				importance.setType("Lingüístico"); //$NON-NLS-1$
				
				st.executeUpdate("insert into " + TABLE + " values ('" + importance.getId() + "','" + problemId + "','" + importance.getType() + "','" + importance.toString() + "')"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			}
			
			if(!knowledgeDomain) {
				
				List<LabelLinguisticDomain> labels = new LinkedList<LabelLinguisticDomain>();
				LabelLinguisticDomain nothing = new LabelLinguisticDomain("None", new TrapezoidalFunction(new double[]{0, 0, 0, 0.17})); //$NON-NLS-1$
				labels.add(nothing);
				LabelLinguisticDomain veryBad = new LabelLinguisticDomain("Very_unsure", new TrapezoidalFunction(new double[]{0, 0.17, 0.17, 0.33})); //$NON-NLS-1$
				labels.add(veryBad);
				LabelLinguisticDomain bad = new LabelLinguisticDomain("Unsure", new TrapezoidalFunction(new double[]{0.17, 0.33, 0.33, 0.5})); //$NON-NLS-1$
				labels.add(bad);
				LabelLinguisticDomain medium = new LabelLinguisticDomain("Medium", new TrapezoidalFunction(new double[]{0.33, 0.5, 0.5, 0.67})); //$NON-NLS-1$
				labels.add(medium);
				LabelLinguisticDomain good = new LabelLinguisticDomain("Sure", new TrapezoidalFunction(new double[]{0.5, 0.67, 0.67, 0.83})); //$NON-NLS-1$
				labels.add(good);
				LabelLinguisticDomain veryGood = new LabelLinguisticDomain("Very_sure", new TrapezoidalFunction(new double[]{0.67, 0.83, 0.83, 1})); //$NON-NLS-1$
				labels.add(veryGood);
				LabelLinguisticDomain perfect = new LabelLinguisticDomain("Absolutely_sure", new TrapezoidalFunction(new double[]{0.83, 1, 1, 1})); //$NON-NLS-1$
				labels.add(perfect);
			
				FuzzySet knowledge = new FuzzySet(labels);
				knowledge.setId("auto_generated_knowledge"); //$NON-NLS-1$
				knowledge.setType("Lingüístico"); //$NON-NLS-1$
				
				st.executeUpdate("insert into " + TABLE + " values ('" + knowledge.getId() + "','" + problemId + "','" + knowledge.getType() + "','" + knowledge.toString() + "')"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
				
			}
			
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeProblemDomains(String problem) {
		try {
			Connection c = getConnection();
			PreparedStatement pst = c.prepareStatement("delete from " + TABLE //$NON-NLS-1$
					+ " where " + PROBLEM + " = ?"); //$NON-NLS-1$ //$NON-NLS-2$
			pst.setString(1, problem);
			pst.executeUpdate();
			pst.close();
		} catch (Exception e) {
		}
	}

	public void removeProblemDomains(Problem problem) {
		removeProblemDomains(problem.getId());
	}
	
	public Map<String, Domain> getProblemDomains(String problem) {
		Map<String, Domain> result = new HashMap<String, Domain>();
		
		try {
			Connection c = getConnection();
			Statement st = c.createStatement();

			String select = "select * from " + TABLE + " where " + PROBLEM + "='" + problem + "';"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			ResultSet rs = st.executeQuery(select);
			Domain domain = null;
			String id;
			String type;
			String value;
			while (rs.next()) {
				
				id = rs.getString(ID);
				type = rs.getString(TYPE);
				value = rs.getString(DOMAIN);
	
				if(type.equals("Integer")) { //$NON-NLS-1$
	
					try {
						String[] tokens = value.split(","); //$NON-NLS-1$
						int min = Integer.parseInt(tokens[0].substring(1));
						int max = Integer.parseInt(tokens[1].substring(1, tokens[1].length() - 1));
						domain = new NumericIntegerDomain();
						domain.setId(id);
						domain.setName(id);
						domain.setType(type);
						((NumericIntegerDomain) domain).setMinMax(min, max);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				} else if(type.equals("Real")) { //$NON-NLS-1$
					
					try {
						String[] tokens = value.split(","); //$NON-NLS-1$
						double min = Double.parseDouble(tokens[0].substring(1));
						double max = Double.parseDouble(tokens[1].substring(1, tokens[1].length() - 1));
						domain = new NumericRealDomain();
						domain.setId(id);
						domain.setName(id);
						domain.setType(type);
						((NumericRealDomain) domain).setMinMax(min, max);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				} else if(type.equals("Linguistic")) { //$NON-NLS-1$
					
					try {
						value = value.replace("{", ""); //$NON-NLS-1$ //$NON-NLS-2$
						value = value.replace("}", ""); //$NON-NLS-1$ //$NON-NLS-2$
						value = value.replace("[", ""); //$NON-NLS-1$ //$NON-NLS-2$
						value = value.replace(" ", ""); //$NON-NLS-1$ //$NON-NLS-2$
						String[] tokens = value.split("]"); //$NON-NLS-1$
	
						List<Double> values = new LinkedList<Double>();
						LabelSetLinguisticDomain labelSet = new LabelSetLinguisticDomain();
						for(int i = 0; i < tokens.length; ++i) {
							LabelLinguisticDomain label = new LabelLinguisticDomain();
							String labelInfo = tokens[i];
							
							String[] info = labelInfo.split(";"); //$NON-NLS-1$
							if(info[0].equals(",")) { //$NON-NLS-1$
								info = Arrays.copyOfRange(info, 1, info.length);
							}
							
							label.setName(info[0].replace(",", "")); //$NON-NLS-1$ //$NON-NLS-2$
							String semanticFunction = info[1];
							String limits = semanticFunction.substring(semanticFunction.indexOf("(") + 1, semanticFunction.indexOf(")")); //$NON-NLS-1$ //$NON-NLS-2$
							String[] limitsNumbers = limits.split(","); //$NON-NLS-1$
							
							double[] limitsV = new double[limitsNumbers.length];
							for(int j = 0; j < limitsNumbers.length; ++j) {
								limitsV[j] = Double.parseDouble(limitsNumbers[j]);
							}
	
							TrapezoidalFunction semantic = new TrapezoidalFunction(limitsV);
							label.setSemantic(semantic);
							labelSet.addLabel(label);
							values.add(Double.parseDouble(info[2]));
						}			
						
						domain = new FuzzySet();
						domain.setId(id);
						domain.setName(id);
						domain.setType(type);
						((FuzzySet) domain).setLabelSet(labelSet);
						((FuzzySet) domain).setValues(values);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				result.put(id, domain);
			}
		} catch (Exception e) {
			
		}
		
		return result;
	}
	
	public Map<String, Domain> getProblemDomains(Problem problem) {
		return getProblemDomains(problem.getId());
	}

}
