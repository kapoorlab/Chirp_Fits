package listeners;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.TextComponent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

import Fitting.TimeSeries.InteractiveChirpFit;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;


public class DegreeListener implements TextListener{
	
	final InteractiveChirpFit parent;
	protected double min, max;
	
	public DegreeListener(final InteractiveChirpFit parent){
		
		this.parent = parent;
	}
	
	@Override
	public void textValueChanged(TextEvent e) {
		final TextComponent tc = (TextComponent)e.getSource();
	    
			    	String s = tc.getText();
			    	
			    
	                 if (s.length() > 0)		    		
					 parent.degree = (int)Float.parseFloat(s);
					
		 
		
	}
	
	

}
