package mcdacw.valuation.domain.numeric.jfreechart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.DefaultIntervalXYDataset;
import org.jfree.data.xy.IntervalXYDataset;

import mcdacw.valuation.domain.Domain;
import mcdacw.valuation.domain.DomainChart;
import mcdacw.valuation.domain.numeric.NumericRealDomain;
import mcdacw.valuation.nls.Messages;


public class NumericRealDomainChart extends DomainChart {
	
	private NumericRealDomain _domain;
	
	private int _width;
	private int _height;
	
	public static final Color[] colors = {Color.RED, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.LIGHT_GRAY, Color.ORANGE, Color.ORANGE,
		Color.MAGENTA, Color.PINK, Color.WHITE, Color.YELLOW};
	
	private ValueMarker _numMarker; 
	private IntervalMarker _intervalMarker; 

	public NumericRealDomainChart() {
		super();
		_domain = null;
		_chart = null;
		_numMarker = null;
		_intervalMarker = null;
	}
	
	@Override
	public void initialize(Domain domain, int width, int height, int style) {
		_width = width;
		_height = height;
		
		setDomain(domain);
	}
	
	@Override
	public void refreshChart() {
		
		if(_chart == null) {
			_chart = createChart(createIntervalDataset());
		} else {
			_chart.getXYPlot().setDataset(createIntervalDataset());
		}
		
		double upperLimit = _domain.getMax(), lowerLimit = _domain.getMin();
		boolean inRange = _domain.getInRange();
		
		double rangeSize = upperLimit - lowerLimit;
		double margin = (rangeSize > 0) ? rangeSize / 4d : 1;
		
		_chart.getXYPlot().getRangeAxis().setRange(lowerLimit - margin, upperLimit + margin);
		_chart.getXYPlot().getRangeAxis().setTickLabelsVisible(inRange);
		
		if(inRange) {
			_chart.getXYPlot().getRangeAxis().setLabel(Messages.getString("NumericRealDomainChart.Domain")); //$NON-NLS-1$
		} else {
			_chart.getXYPlot().getRangeAxis().setLabel(Double.toString(Double.NEGATIVE_INFINITY) + 
					"                                         " +  //$NON-NLS-1$
					Double.toString(Double.POSITIVE_INFINITY));
		}
		
	}
	
	@Override
	public void setDomain(Domain domain) {
		_domain = (NumericRealDomain) domain;
		refreshChart();
	}

	@Override
	public void setSelection(Object selection) {

		if(_numMarker != null) {
			_chart.getXYPlot().removeRangeMarker(_numMarker);
		}
		
		if(_intervalMarker != null) {
			_chart.getXYPlot().removeRangeMarker(_intervalMarker);
		}
		
		if(selection instanceof LinkedList<?>) {
			_intervalMarker = new IntervalMarker((Double)((LinkedList<?>) selection).getFirst(), (Double)((LinkedList<?>) selection).getLast());
			_intervalMarker.setAlpha(0.5f);
			_intervalMarker.setPaint(Color.RED);
			_chart.getXYPlot().addRangeMarker(_intervalMarker);
		} else {	
			_numMarker = new ValueMarker((Double) selection);
			_numMarker.setPaint(Color.RED);
			_numMarker.setStroke(new BasicStroke(4));
			_chart.getXYPlot().addRangeMarker(_numMarker);
		}
		
	}
	
	private JFreeChart createChart(IntervalXYDataset intervalXYDataset) {
		JFreeChart result = ChartFactory.createXYBarChart("", "X", false, "", intervalXYDataset,  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				PlotOrientation.HORIZONTAL, false, false, false);
		
		result.setBackgroundPaint(Color.WHITE);
		XYPlot xyplot = (XYPlot) result.getPlot();
		
		XYBarRenderer xyBarRenderer = (XYBarRenderer) xyplot.getRenderer();
		xyBarRenderer.setSeriesPaint(0, Color.BLUE);
		xyBarRenderer.setUseYInterval(true);
		xyBarRenderer.setBarPainter(new StandardXYBarPainter());
		
		xyplot.setBackgroundPaint(Color.WHITE);
		xyplot.setDomainGridlinePaint(Color.LIGHT_GRAY);
		xyplot.setRangeGridlinePaint(Color.LIGHT_GRAY);
		xyplot.getDomainAxis().setVisible(false);
		
		return result;
	}
	
	private IntervalXYDataset createIntervalDataset() {
		DefaultIntervalXYDataset result = new DefaultIntervalXYDataset();
		
		double upperLimit = _domain.getMax(), lowerLimit = _domain.getMin();
		
		double[] x = new double[] {1};
		double[] xStart = new double[] {0.1};
		double[] xEnd = new double[] {0.9};
		double[] y = new double[] {upperLimit - lowerLimit};
		double[] yStart = new double[] {lowerLimit};
		double[] yEnd = new double[] {upperLimit};
		double[][] data = new double[][] {x, xStart, xEnd, y, yStart, yEnd};
		result.addSeries(Messages.getString("NumericRealDomainChart.Range"), data); //$NON-NLS-1$
		
		return result;
	}
	
	@Override
	public Image createImage() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		Image image = null;
		try {
			ChartUtilities.writeChartAsPNG(bos, _chart, _width, _height);
			image = new Image(Display.getCurrent(), new ByteArrayInputStream(bos.toByteArray()));
		} catch (IOException e) {
			System.err.println("Creation image failed"); //$NON-NLS-1$
		}
		
		return image;
	}
}
