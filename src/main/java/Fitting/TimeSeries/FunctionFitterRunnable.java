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
import chirpModels.LinearChirpBiQuadAmp;
import chirpModels.LinearChirpConstAmp;
import chirpModels.LinearChirpCubeAmp;
import chirpModels.LinearChirpLinearAmp;
import chirpModels.LinearChirpQuadAmp;
import chirpModels.LinearChirpSixthOrderAmp;
import chirpModels.UserChirpModel;
import chirpModels.UserChirpModel.UserModel;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;

public class FunctionFitterRunnable implements Runnable {
	
	final InteractiveChirpFit parent;
	final ArrayList<Pair<Double, Double>> timeseries;
	private final UserModel model;
	public int maxiter = 50000;
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
	
	public FunctionFitterRunnable(final InteractiveChirpFit parent, final ArrayList<Pair<Double, Double>> timeseries, 
			UserModel model, final int fileindex,
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
		
	
		
		
		ChirpFitFunction UserChoiceFunction = null;
		if (model == UserModel.Linear){
			
			UserChoiceFunction = new LinearChirp();
			
		}
		
		 if (model == UserModel.LinearConstAmp){
				
				UserChoiceFunction = new LinearChirpConstAmp();
				
			}
		 if (model == UserModel.LinearLinearAmp){
				
				UserChoiceFunction = new LinearChirpLinearAmp();
				
			}
		 
		 if (model == UserModel.LinearQuadraticAmp){
				
				UserChoiceFunction = new LinearChirpQuadAmp();
				
			}
		 if (model == UserModel.LinearCubeAmp){
				
				UserChoiceFunction = new LinearChirpCubeAmp();
				
			}
		 if (model == UserModel.LinearBiquadAmp){
				
				UserChoiceFunction = new LinearChirpBiQuadAmp();
				
			}
		  if (model == UserModel.LinearSixthOrderAmp){
	   			
	   			UserChoiceFunction = new LinearChirpSixthOrderAmp();
	   			
	   		}

		LMparam = ExtractSeries.initialguess(timeseries, timeseries.size(), Lowfrequency, Highfrequency, model);
		
		try {
			
			LevenbergMarquardtSolverChirp LMsolver = new LevenbergMarquardtSolverChirp(parent, parent.jpb);
			
			LMsolver.solve(T,timeseries, LMparam, timeseries.size(), I, UserChoiceFunction, lambda,
					termepsilon, maxiter, fileindex, totalfiles, model);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		int totaltime = timeseries.size();
	
		if (model == UserModel.Linear){
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
		
			if (parent.dataset!=null)
				parent.dataset.removeAllSeries();
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

		
		if (model == UserModel.LinearConstAmp){
			System.out.println("Frequency (hrs):" + 6.28/((LMparam[1]) * 60));
			System.out.println("Chirp Frequ  (hrs):" + 6.28/((LMparam[2]) * 60));
			System.out.println("Phase:" + ((LMparam[3])));
			System.out.println("Back:" + ((LMparam[4])));


			System.out.println("Frequency :" + LMparam[1]);
			System.out.println("Chirp Frequ  :" + LMparam[2]);
			System.out.println("Phase:" + ((LMparam[3])));
			System.out.println("Back:" + ((LMparam[4])));
			
			parent.rtAll.incrementCounter();
			parent.rtAll.addValue("Low Frequency (hrs):" , 6.28/((LMparam[1]) * 60));
			parent.rtAll.addValue("High Frequency  (hrs):" , 6.28/((LMparam[2]) * 60));
			parent.rtAll.show("Frequency by Chirp Model Fits");
		
			if (parent.dataset!=null)
				parent.dataset.removeAllSeries();
			parent.frequchirphist.add(new ValuePair<Double, Double> (6.28/((LMparam[1]) * 60),6.28/((LMparam[2]) * 60)   ));
			
			double poly;
			final ArrayList<Pair<Double, Double>> fitpoly = new ArrayList<Pair<Double, Double>>();
			for (int i = 0; i < timeseries.size(); ++i) {

				Double time = timeseries.get(i).getA();

				poly = LMparam[0]
						* Math.cos(Math.toRadians(LMparam[1] * time
								+ (LMparam[2] -LMparam[1]) * time * time
										/ (2 * totaltime)
								+ LMparam[3])) + LMparam[4] ;
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
		if (model == UserModel.LinearLinearAmp){
			System.out.println("Frequency (hrs):" + 6.28/((LMparam[2]) * 60));
			System.out.println("Chirp Frequ  (hrs):" + 6.28/((LMparam[3]) * 60));
			System.out.println("Phase:" + ((LMparam[4])));
			System.out.println("Back:" + ((LMparam[5])));


			System.out.println("Frequency :" + LMparam[2]);
			System.out.println("Chirp Frequ  :" + LMparam[3]);
			System.out.println("Phase:" + ((LMparam[4])));
			System.out.println("Back:" + ((LMparam[5])));
			
			parent.rtAll.incrementCounter();
			parent.rtAll.addValue("Low Frequency (hrs):" , 6.28/((LMparam[2]) * 60));
			parent.rtAll.addValue("High Frequency  (hrs):" , 6.28/((LMparam[3]) * 60));
			parent.rtAll.show("Frequency by Chirp Model Fits");
		
			if (parent.dataset!=null)
				parent.dataset.removeAllSeries();
			parent.frequchirphist.add(new ValuePair<Double, Double> (6.28/((LMparam[2]) * 60),6.28/((LMparam[3]) * 60)   ));
			
			double poly;
			final ArrayList<Pair<Double, Double>> fitpoly = new ArrayList<Pair<Double, Double>>();
			for (int i = 0; i < timeseries.size(); ++i) {

				Double time = timeseries.get(i).getA();

				poly = (LMparam[0]*time + LMparam[1])
						* Math.cos(Math.toRadians(LMparam[2] * time
								+ (LMparam[3] -LMparam[2]) * time * time
										/ (2 * totaltime)
								+ LMparam[4])) + LMparam[5] ;
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
		
		if (model == UserModel.LinearQuadraticAmp){
			System.out.println("Frequency (hrs):" + 6.28/((LMparam[3]) * 60));
			System.out.println("Chirp Frequ  (hrs):" + 6.28/((LMparam[4]) * 60));
			System.out.println("Phase:" + ((LMparam[5])));
			System.out.println("Back:" + ((LMparam[6])));


			System.out.println("Frequency :" + LMparam[3]);
			System.out.println("Chirp Frequ  :" + LMparam[4]);
			System.out.println("Phase:" + ((LMparam[5])));
			System.out.println("Back:" + ((LMparam[6])));
			
			parent.rtAll.incrementCounter();
			parent.rtAll.addValue("Low Frequency (hrs):" , 6.28/((LMparam[3]) * 60));
			parent.rtAll.addValue("High Frequency  (hrs):" , 6.28/((LMparam[4]) * 60));
			parent.rtAll.show("Frequency by Chirp Model Fits");
		
			if (parent.dataset!=null)
				parent.dataset.removeAllSeries();
			parent.frequchirphist.add(new ValuePair<Double, Double> (6.28/((LMparam[3]) * 60),6.28/((LMparam[4]) * 60)   ));
			
			double poly;
			final ArrayList<Pair<Double, Double>> fitpoly = new ArrayList<Pair<Double, Double>>();
			for (int i = 0; i < timeseries.size(); ++i) {

				Double time = timeseries.get(i).getA();

				poly = (LMparam[0]*time * time + LMparam[1] * time + LMparam[2])
						* Math.cos(Math.toRadians(LMparam[3] * time
								+ (LMparam[4] -LMparam[3]) * time * time
										/ (2 * totaltime)
								+ LMparam[5])) + LMparam[6] ;
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
		
		
		if (model == UserModel.LinearCubeAmp){
			System.out.println("Frequency (hrs):" + 6.28/((LMparam[4]) * 60));
			System.out.println("Chirp Frequ  (hrs):" + 6.28/((LMparam[5]) * 60));
			System.out.println("Phase:" + ((LMparam[6])));
			System.out.println("Back:" + ((LMparam[7])));


			System.out.println("Frequency :" + LMparam[4]);
			System.out.println("Chirp Frequ  :" + LMparam[3]);
			System.out.println("Phase:" + ((LMparam[6])));
			System.out.println("Back:" + ((LMparam[7])));
			
			parent.rtAll.incrementCounter();
			parent.rtAll.addValue("Low Frequency (hrs):" , 6.28/((LMparam[4]) * 60));
			parent.rtAll.addValue("High Frequency  (hrs):" , 6.28/((LMparam[5]) * 60));
			parent.rtAll.show("Frequency by Chirp Model Fits");
		
			if (parent.dataset!=null)
				parent.dataset.removeAllSeries();
			parent.frequchirphist.add(new ValuePair<Double, Double> (6.28/((LMparam[4]) * 60),6.28/((LMparam[5]) * 60)   ));
			
			double poly;
			final ArrayList<Pair<Double, Double>> fitpoly = new ArrayList<Pair<Double, Double>>();
			for (int i = 0; i < timeseries.size(); ++i) {

				Double time = timeseries.get(i).getA();

				poly = (LMparam[0]*time * time * time + LMparam[1] * time * time + LMparam[2] * time + LMparam[3])
						* Math.cos(Math.toRadians(LMparam[4] * time
								+ (LMparam[5] -LMparam[4]) * time * time
										/ (2 * totaltime)
								+ LMparam[6])) + LMparam[7] ;
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
		
		
		if (model == UserModel.LinearBiquadAmp){
			System.out.println("Frequency (hrs):" + 6.28/((LMparam[5]) * 60));
			System.out.println("Chirp Frequ  (hrs):" + 6.28/((LMparam[6]) * 60));
			System.out.println("Phase:" + ((LMparam[7])));
			System.out.println("Back:" + ((LMparam[8])));


			System.out.println("Frequency :" + LMparam[5]);
			System.out.println("Chirp Frequ  :" + LMparam[6]);
			System.out.println("Phase:" + ((LMparam[7])));
			System.out.println("Back:" + ((LMparam[8])));
			
			parent.rtAll.incrementCounter();
			parent.rtAll.addValue("Low Frequency (hrs):" , 6.28/((LMparam[5]) * 60));
			parent.rtAll.addValue("High Frequency  (hrs):" , 6.28/((LMparam[6]) * 60));
			parent.rtAll.show("Frequency by Chirp Model Fits");
		
			if (parent.dataset!=null)
				parent.dataset.removeAllSeries();
			parent.frequchirphist.add(new ValuePair<Double, Double> (6.28/((LMparam[5]) * 60),6.28/((LMparam[6]) * 60)   ));
			
			double poly;
			final ArrayList<Pair<Double, Double>> fitpoly = new ArrayList<Pair<Double, Double>>();
			for (int i = 0; i < timeseries.size(); ++i) {

				Double time = timeseries.get(i).getA();

				poly = (LMparam[0]*time * time * time * time + LMparam[1] * time * time * time + LMparam[2] * time * time + LMparam[3] * time + LMparam[4])
						* Math.cos(Math.toRadians(LMparam[5] * time
								+ (LMparam[6] -LMparam[5]) * time * time
										/ (2 * totaltime)
								+ LMparam[7])) + LMparam[8] ;
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
		
		if (model == UserModel.LinearSixthOrderAmp){
			System.out.println("Frequency (hrs):" + 6.28/((LMparam[7]) * 60));
			System.out.println("Chirp Frequ  (hrs):" + 6.28/((LMparam[8]) * 60));
			System.out.println("Phase:" + ((LMparam[9])));
			System.out.println("Back:" + ((LMparam[10])));


			System.out.println("Frequency :" + LMparam[7]);
			System.out.println("Chirp Frequ  :" + LMparam[8]);
			System.out.println("Phase:" + ((LMparam[9])));
			System.out.println("Back:" + ((LMparam[10])));
			
			parent.rtAll.incrementCounter();
			parent.rtAll.addValue("Low Frequency (hrs):" , 6.28/((LMparam[7]) * 60));
			parent.rtAll.addValue("High Frequency  (hrs):" , 6.28/((LMparam[8]) * 60));
			parent.rtAll.show("Frequency by Chirp Model Fits");
		
			if (parent.dataset!=null)
				parent.dataset.removeAllSeries();
			parent.frequchirphist.add(new ValuePair<Double, Double> (6.28/((LMparam[7]) * 60),6.28/((LMparam[8]) * 60)   ));
			
			double poly;
			final ArrayList<Pair<Double, Double>> fitpoly = new ArrayList<Pair<Double, Double>>();
			for (int i = 0; i < timeseries.size(); ++i) {

				Double time = timeseries.get(i).getA();

				poly = (LMparam[0]*time * time * time * time * time * time + LMparam[1] * time * time * time * time * time + LMparam[2] * time * time* time * time 
						+ LMparam[3] * time * time * time + LMparam[4] * time * time + LMparam[5] * time + LMparam[6])
						* Math.cos(Math.toRadians(LMparam[7] * time
								+ (LMparam[8] -LMparam[7]) * time * time
										/ (2 * totaltime)
								+ LMparam[9])) + LMparam[10] ;
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
		
	}

	
	
	public double[] result(){
		
		return LMparam;
	}
		

	

	
	
	
	

}
