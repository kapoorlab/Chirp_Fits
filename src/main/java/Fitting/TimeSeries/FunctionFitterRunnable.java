package Fitting.TimeSeries;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.title.Title;
import org.jfree.ui.RectangleEdge;

import chirpModels.ChirpFitFunction;
import chirpModels.LinearChirp;
import chirpModels.UserChirpModel;

import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;

public class FunctionFitterRunnable implements Runnable {
	
	final InteractiveChirpFit parent;
	final ArrayList<Pair<Double, Double>> timeseries;
	private final UserChirpModel model;
	public int maxiter = 1500;
	public double lambda = 1e-3;
	public double termepsilon = 1e-4;
	double[] LMparam;
	public double Lowfrequency = 0.02;
	public double Highfrequency = 0.03;
	public final int fileindex;
	public final int totalfiles;
	
	
	
	public void setMaxiter(int maxiter) {
		this.maxiter = maxiter;
	}

	public int getMaxiter() {
		return maxiter;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

	public double getLambda() {
		return lambda;
	}

	public void setTermepsilon(double termepsilon) {
		this.termepsilon = termepsilon;
	}

	public double getTermepsilon() {
		return termepsilon;
	}
	
	public void setLowfrequency(double Lowfrequency) {

		this.Lowfrequency = Lowfrequency;

	}

	public double getLowfrequency() {

		return Lowfrequency;
	}
	
	public void setHighfrequency(double Highfrequency) {

		this.Highfrequency = Highfrequency;

	}

	public double getHighfrequency() {

		return Highfrequency;
	}
	
	
	/**
	 * 
	 * @param timeseries input the time series
	 * deltat = spacing in time between succeding points
	 */
	
	public FunctionFitterRunnable(final InteractiveChirpFit parent, final ArrayList<Pair<Double, Double>> timeseries, UserChirpModel model, final int fileindex,
			final int totalfiles){
		
		this.parent = parent;
		this.timeseries = timeseries;
		this.model = model;
		this.fileindex = fileindex;
		this.totalfiles = totalfiles;
		
	}
	
	
	public boolean checkInput() {
		
		if (timeseries.size() == 0)
		return false;
		
		return true;
	}

	@Override
	public void run() {
		
		// Run the gradient descent using Chirp function fit
		double[] T = new double[timeseries.size()];
		double[] I = new double[timeseries.size()];

		
		
		System.out.println(Lowfrequency + " " + Highfrequency);
		
		for (int i = 0; i < timeseries.size(); ++i){
			
			T[i] = timeseries.get(i).getA();
			I[i] = timeseries.get(i).getB(); 
		}
		
	
		LMparam = ExtractSeries.initialguess(timeseries, timeseries.size(), Lowfrequency, Highfrequency);
		
		ChirpFitFunction UserChoiceFunction = null;
		if (model == UserChirpModel.Linear){
			
			UserChoiceFunction = new LinearChirp();
			
		}
		
		try {
			
			LevenbergMarquardtSolverChirp LMsolver = new LevenbergMarquardtSolverChirp(parent, parent.jpb);
			
			LMsolver.solve(T,timeseries, LMparam, timeseries.size(), I, UserChoiceFunction, lambda,
					termepsilon, maxiter, fileindex, totalfiles);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		int totaltime = timeseries.size();
	
			System.out.println("Frequency (hrs):" + 6.28/((LMparam[totaltime]) * 60));
			System.out.println("Chirp Frequ  (hrs):" + 6.28/((LMparam[totaltime + 1]) * 60));
			System.out.println("Phase:" + ((LMparam[totaltime + 2])));
			System.out.println("Back:" + ((LMparam[totaltime + 3])));


			System.out.println("Frequency :" + LMparam[totaltime]);
			System.out.println("Chirp Frequ  :" + LMparam[totaltime + 1]);
			System.out.println("Phase:" + ((LMparam[totaltime + 2])));
			System.out.println("Back:" + ((LMparam[totaltime + 3])));
			
			parent.rtAll.incrementCounter();
			parent.rtAll.addValue("Low Frequency (hrs):" , 6.28/((LMparam[totaltime]) * 60));
			parent.rtAll.addValue("High Frequency  (hrs):" , 6.28/((LMparam[totaltime + 1]) * 60));
			parent.rtAll.show("Frequency by Chirp Model Fits");
			
			parent.frequchirphist.add(new ValuePair<Double, Double> (6.28/((LMparam[totaltime]) * 60),6.28/((LMparam[totaltime + 1]) * 60)   ));
			
			double poly;
			final ArrayList<Pair<Double, Double>> fitpoly = new ArrayList<Pair<Double, Double>>();
			for (int i = 0; i < timeseries.size(); ++i) {

				Double time = timeseries.get(i).getA();

				poly = LMparam[i]
						* Math.cos(Math.toRadians(LMparam[totaltime] * time
								+ (LMparam[totaltime + 1] -LMparam[totaltime]) * time * time
										/ (2 * totaltime)
								+ LMparam[totaltime + 2])) + LMparam[totaltime + 3] ;
				fitpoly.add(new ValuePair<Double, Double>(time, poly));
			}
			parent.dataset.addSeries(Mainpeakfitter.drawPoints(timeseries));
			parent.dataset.addSeries(Mainpeakfitter.drawPoints(fitpoly, "Fits"));
			Mainpeakfitter.setColor(parent.chart, 1, new Color(255, 255, 64));
			Mainpeakfitter.setStroke(parent.chart, 1, 2f);
			  Mainpeakfitter.setColor(parent.chart, 0, new Color(64, 64, 64));
		       Mainpeakfitter.setStroke(parent.chart, 0, 2f);
		       Mainpeakfitter.setDisplayType(parent.chart, 0, false, true);
		       Mainpeakfitter.setSmallUpTriangleShape(parent.chart, 0);
		      
		
	}

	
	
	public double[] result(){
		
		return LMparam;
	}
		

	

	
	
	
	

}
