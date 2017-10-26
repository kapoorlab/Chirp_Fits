package Fitting.TimeSeries;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import chirpModels.ChirpFitFunction;
import chirpModels.UserChirpModel;
import chirpModels.UserChirpModel.UserModel;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;

public class ExtractSeries {

	
	public static ArrayList< Pair< Double, Double > > gatherdata( final File file )
	{
		final ArrayList< Pair< Double, Double > > points = new ArrayList< Pair< Double, Double > >();

		try
		{
			BufferedReader in = Util.openFileRead( file );

			while( in.ready() )
			{
				String line = in.readLine().trim();

				while ( line.contains( "\t\t" ) )
					line = line.replaceAll( "\t\t", "\t" );

				if ( line.length() >= 3 && line.matches( "[0-9].*" ) )
				{
					final String[] split = line.trim().split( "\t" );

					final double timepoint = Double.parseDouble( split[ 0 ] );
					final double value = Double.parseDouble( split[ 1 ] );

					points.add( new ValuePair< Double, Double >( timepoint, value ) );
				}
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			return null;
		}

		Collections.sort( points, new Comparator< Pair< Double, Double > >()
		{
			
			public int compare( final Pair< Double, Double > o1, final Pair< Double, Double > o2 )
			{
				return o1.getA().compareTo( o2.getA() );
			}
		} );

		return points;
	}
	
	
	public static ArrayList< Pair< Double, Double > > Normalize (ArrayList< Pair< Double, Double > > points){
		
		
		Pair<Double, Double> minmax = minmax(points);
		final ArrayList< Pair< Double, Double > > Normpoints = new ArrayList< Pair< Double, Double > >();

		
		
		for (final Pair< Double, Double > p : points){
			
			Normpoints.add(new ValuePair<Double, Double>(p.getA(), p.getB() / (minmax.getB() - minmax.getA())));
			
		}
		
		return Normpoints;
	}
	
	public static Pair<Double, Double> minmax (ArrayList< Pair< Double, Double > > points){
		
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		
		
		for (final Pair< Double, Double > p : points){
			
			min = Math.min(min, p.getB());
			max = Math.max(max, p.getB());
		}
		
		Pair<Double, Double> minmax = new ValuePair<Double,Double>(min, max);
		
		return minmax;
	}

	public static double[] initialguess(ArrayList< Pair< Double, Double > > points,
			final int totaltime, double Lowfrequency, double Highfrequency, UserModel model){
		
	
		if (model == UserModel.Linear){
		double[] initialparameters = new double[totaltime + 4];
		
		
		
		
		double Frequency = Lowfrequency;
		double endChirp =  Highfrequency ;
		
		double phase = 0;
		
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		
		
		for (final Pair< Double, Double > p : points){
			
			min = Math.min(min, p.getB());
			max = Math.max(max, p.getB());
		}
		
		System.out.println("ni");
			
			
		for (int index = 0; index < points.size(); ++index){
			initialparameters[index] = points.get(index).getB();
		}
		initialparameters[totaltime] = Frequency;
		initialparameters[totaltime + 1] = endChirp;
		initialparameters[totaltime + 2] = phase;
		initialparameters[totaltime + 3] = min;
		
		return initialparameters;
		}
		
			
		
		if (model == UserModel.LinearConstAmp){
			
			
			
			System.out.println("hi");
			double[] initialparameters = new double[5];
			
			double Frequency = Lowfrequency;
			double endChirp =  Highfrequency ;
			
			double phase = 0;
			
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;
			
			
			for (final Pair< Double, Double > p : points){
				
				min = Math.min(min, p.getB());
				max = Math.max(max, p.getB());
			}
			
			initialparameters[0] = (min + max) * 0.5;
			initialparameters[1] = Frequency;
			initialparameters[2] = endChirp;
			initialparameters[3] = phase;
			initialparameters[4] = min;
			
			return initialparameters;
		}

		if (model == UserModel.LinearLinearAmp){
			
			
			
			System.out.println("hiH");
			double[] initialparameters = new double[6];
			
			double Frequency = Lowfrequency;
			double endChirp =  Highfrequency ;
			
			double phase = 0;
			
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;
			
			
			for (final Pair< Double, Double > p : points){
				
				min = Math.min(min, p.getB());
				max = Math.max(max, p.getB());
			}
			
			initialparameters[0] = max;
			initialparameters[1] = min;
			initialparameters[2] = Frequency;
			initialparameters[3] = endChirp;
			initialparameters[4] = phase;
			initialparameters[5] = min;
			
			return initialparameters;
		}
		
	if (model == UserModel.LinearQuadraticAmp){
			
			
			
			System.out.println("hiHQ");
			double[] initialparameters = new double[7];
			
			double Frequency = Lowfrequency;
			double endChirp =  Highfrequency ;
			
			double phase = 0;
			
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;
			
			
			for (final Pair< Double, Double > p : points){
				
				min = Math.min(min, p.getB());
				max = Math.max(max, p.getB());
			}
			
			initialparameters[0] = 0;
			initialparameters[1] = 0;
			initialparameters[2] = (max + min)/2;
			initialparameters[3] = Frequency;
			initialparameters[4] = endChirp;
			initialparameters[5] = phase;
			initialparameters[6] = min;
			
			return initialparameters;
		}
	if (model == UserModel.LinearCubeAmp){
		
		
		
		System.out.println("hiHC");
		double[] initialparameters = new double[8];
		
		double Frequency = Lowfrequency;
		double endChirp =  Highfrequency ;
		
		double phase = 0;
		
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		
		
		for (final Pair< Double, Double > p : points){
			
			min = Math.min(min, p.getB());
			max = Math.max(max, p.getB());
		}
		
		initialparameters[0] = 0;
		initialparameters[1] = 0;
		initialparameters[2] = 0;
		initialparameters[3] = (max + min)/2;
		initialparameters[4] = Frequency;
		initialparameters[5] = endChirp;
		initialparameters[6] = phase;
		initialparameters[7] = min;
		
		return initialparameters;
	}
	
    if (model == UserModel.LinearBiquadAmp){
		
		
		
		System.out.println("hiHCB");
		double[] initialparameters = new double[9];
		
		double Frequency = Lowfrequency;
		double endChirp =  Highfrequency ;
		
		double phase = 0;
		
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		
		
		for (final Pair< Double, Double > p : points){
			
			min = Math.min(min, p.getB());
			max = Math.max(max, p.getB());
		}
		
		initialparameters[0] = 0;
		initialparameters[1] = 0;
		initialparameters[2] = 0;
		initialparameters[3] = 0;
		
		initialparameters[4] = (max + min)/2;
		initialparameters[5] = Frequency;
		initialparameters[6] = endChirp;
		initialparameters[7] = phase;
		initialparameters[8] = min;
		
		return initialparameters;
	}
    if (model == UserModel.LinearSixthOrderAmp){
		
		
		
  		System.out.println("hiHCB6");
  		double[] initialparameters = new double[11];
  		
  		double Frequency = Lowfrequency;
  		double endChirp =  Highfrequency ;
  		
  		double phase = 0;
  		
  		double min = Double.MAX_VALUE;
  		double max = Double.MIN_VALUE;
  		
  		
  		for (final Pair< Double, Double > p : points){
  			
  			min = Math.min(min, p.getB());
  			max = Math.max(max, p.getB());
  		}
  		
  		initialparameters[0] = 0;
  		initialparameters[1] = 0;
  		initialparameters[2] = 0;
  		initialparameters[3] = 0;
  		initialparameters[4] = 0;
  		initialparameters[5] = 0;
  		initialparameters[6] = (max + min)/2;
  		initialparameters[7] = Frequency;
  		initialparameters[8] = endChirp;
  		initialparameters[9] = phase;
  		initialparameters[10] = min;
  		
  		return initialparameters;
  	}
	
		else return null;

		
		
	}
	
	
}
