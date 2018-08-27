package flintstones.gathering.cloud.view.frameworkstructuring;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import flintstones.gathering.cloud.model.Problem;
import mcdacw.valuation.domain.Domain;
import mcdacw.valuation.domain.DomainChart;
import mcdacw.valuation.domain.fuzzyset.jfreechart.LinguisticDomainChart;
import mcdacw.valuation.domain.numeric.jfreechart.NumericIntegerDomainChart;
import mcdacw.valuation.domain.numeric.jfreechart.NumericRealDomainChart;

public class DomainView extends ViewPart implements ISelectedDomain {

	public static final String ID = "flintstones.gathering.cloud.view.domainview"; //$NON-NLS-1$
	
	private Problem _problem;
	private Map<String, Domain> _domains;
	
	private Composite _parent;
	
	private DomainChart _chart;
	
	@Override
	public void createPartControl(Composite parent) {
		_parent = parent;
		
		_problem = (Problem) RWT.getUISession().getAttribute("valuation-problem"); //$NON-NLS-1$
		
		if(_problem != null) {
			_domains = _problem.getDomains();
		} else {
			_domains = new HashMap<String, Domain>();
		}
		
		DomainIndexView domainIndexView = null;
		IViewReference viewReferences[] = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();
		for (int i = 0; i < viewReferences.length; i++) {
			if (DomainIndexView.ID.equals(viewReferences[i].getId())) {
				domainIndexView = (DomainIndexView) viewReferences[i].getView(false);
			}
		}
		
		domainIndexView.registerListener(this);
	}

	@Override
	public void notifySelectedDomain(String id) {
		Domain d = _domains.get(id);

		if(d.getType().equals("Linguistic")) { //$NON-NLS-1$
			_chart = new LinguisticDomainChart();
			_chart.initialize(d, _parent.getSize().x, _parent.getSize().y, SWT.NONE);
			
			Image chartImage = _chart.createImage();
			_parent.setBackgroundMode(SWT.INHERIT_NONE);
			_parent.setBackgroundImage(chartImage);
			
		} else if(d.getType().equals("Integer")) { //$NON-NLS-1$
			_chart = new NumericIntegerDomainChart();
			_chart.initialize(d, _parent.getSize().x, _parent.getSize().y, SWT.NONE);
			
			Image chartImage = _chart.createImage();
			_parent.setBackgroundMode(SWT.INHERIT_NONE);
			_parent.setBackgroundImage(chartImage);
			
		} else if(d.getType().equals("Real")) { //$NON-NLS-1$
			_chart = new NumericRealDomainChart();
			_chart.initialize(d, _parent.getSize().x, _parent.getSize().y, SWT.NONE);
			
			Image chartImage = _chart.createImage();
			_parent.setBackgroundMode(SWT.INHERIT_NONE);
			_parent.setBackgroundImage(chartImage);
		}
	}
	
	@Override
	public void setFocus() {}

}
