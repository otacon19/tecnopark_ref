package mcdacw.valuation.domain;

import org.eclipse.swt.graphics.Image;
import org.jfree.chart.JFreeChart;

public abstract class DomainChart {
	
	protected Domain _domain;
	protected JFreeChart _chart;
	
	public Domain getDomain() {
		return _domain;
	}
	
	public void setDomain(Domain domain) {
		_domain = domain;
		refreshChart();
	}
	
	public abstract void refreshChart();
	
	public abstract void setSelection(Object selection);
	
	public abstract void initialize(Domain domain, int width, int height, int style);
	
	public abstract Image createImage();
}

