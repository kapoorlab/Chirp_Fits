package chirpModels;

public class LinearChirpLinearAmp implements ChirpFitFunction {

	public double val(double t, double[] a, int totaltime, int timeindex, int degree) {

		double Sinusoid = (a[0] * t + a[1]) * Math.cos(Math.toRadians(a[2] * t + (a[3] - a[2])* t * t / (2 * totaltime) 
				+ a[4])) + a[5];
		
		return Sinusoid;
	}

	/*
	 * Gradient function
	 * 
	 */
	public double grad(double t, double[] a, int totaltime, int k, int timeindex, int degree) {

		if (k == 0) {

		
			double Sinusoid = t * Math.cos(Math.toRadians(a[2] * t
					+ (a[3] - a[2]) * t * t / (2* totaltime)  + a[4]));
			return Sinusoid;

		}
		
		else if (k == 1) {

			double Sinusoid =  Math.cos(Math.toRadians(a[2] * t
					+ (a[3] - a[2]) * t * t / (2* totaltime)  + a[4]));
			return Sinusoid;

		}

		else if (k == 2) {

			double Sinusoid = -(a[0] * t + a[1]) * Math.sin(Math.toRadians(a[2] * t
					+ (a[3] - a[2]) * t * t / (2 * totaltime)  + a[4])) * ( t -  t * t / (2 * totaltime) );

			return Sinusoid;

		}

		else if (k == 3) {

			double Sinusoid = -(a[0] * t + a[1]) * Math.sin(Math.toRadians(a[2] * t
					+ (a[3] - a[2]) * t * t / (2 * totaltime)  + a[4])) * t * t
					/ (2 * totaltime) ;

			return Sinusoid;

		}

		

		else if (k == 4) {

			double Sinusoid = -(a[0] * t + a[1]) * Math.sin(Math.toRadians(a[2] * t
					+ (a[3] - a[2]) * t * t / (2 * totaltime)  + a[4])) ;

			return Sinusoid;

		}
		
		else if (k == 5){
			
			return 1;
		}
		
		
		else return 0;
	}

}
