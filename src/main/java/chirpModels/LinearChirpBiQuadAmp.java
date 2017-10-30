package chirpModels;

public class LinearChirpBiQuadAmp implements ChirpFitFunction {

	public double val(double t, double[] a, int totaltime, int timeindex, int degree) {

		double Sinusoid = (a[0] * t * t * t * t + a[1] * t * t * t + a[2] * t * t + a[3] * t + a[4] ) * Math.cos(Math.toRadians(a[5] * t + (a[6] - a[5])* t * t / (2 * totaltime) 
				+ a[7])) + a[8];
		
		return Sinusoid;
	}

	/*
	 * Gradient function
	 * 
	 */
	public double grad(double t, double[] a, int totaltime, int k, int timeindex, int degree) {

		if (k == 0) {

		
			double Sinusoid = t * t * t * t * Math.cos(Math.toRadians(a[5] * t
					+ (a[6] - a[5]) * t * t / (2* totaltime)  + a[7]));
			return Sinusoid;

		}
		
		else if (k == 1) {

			double Sinusoid =  t * t * t * Math.cos(Math.toRadians(a[5] * t
					+ (a[6] - a[5]) * t * t / (2* totaltime)  + a[7]));
			return Sinusoid;

		}
		
		else if (k == 2) {

			double Sinusoid =   t * t * Math.cos(Math.toRadians(a[5] * t
					+ (a[6] - a[5]) * t * t / (2* totaltime)  + a[7]));
			return Sinusoid;

		}
		
		else if (k == 3) {

			double Sinusoid =    t * Math.cos(Math.toRadians(a[5] * t
					+ (a[6] - a[5]) * t * t / (2* totaltime)  + a[7]));
			return Sinusoid;

		}
		else if (k == 4) {

			double Sinusoid =    Math.cos(Math.toRadians(a[5] * t
					+ (a[6] - a[5]) * t * t / (2* totaltime)  + a[7]));
			return Sinusoid;

		}

		else if (k == 5) {

			double Sinusoid = -(a[0] * t * t * t * t + a[1] * t * t * t + a[2] * t * t + a[3] * t + a[4] )  * Math.sin(Math.toRadians(a[5] * t
					+ (a[6] - a[5]) * t * t / (2 * totaltime)  + a[7])) * ( t -  t * t / (2 * totaltime) );

			return Sinusoid;

		}

		else if (k == 6) {

			double Sinusoid = -(a[0] * t * t * t * t + a[1] * t * t * t + a[2] * t * t + a[3] * t + a[4] )  * Math.sin(Math.toRadians(a[5] * t
					+ (a[6] - a[5]) * t * t / (2 * totaltime)  + a[7])) * t * t
					/ (2 * totaltime) ;

			return Sinusoid;

		}

		

		else if (k == 7) {

			double Sinusoid = -(a[0] * t * t * t * t + a[1] * t * t * t + a[2] * t * t + a[3] * t + a[4] )  * Math.sin(Math.toRadians(a[5] * t
					+ (a[6] - a[5]) * t * t / (2 * totaltime)  + a[7])) ;

			return Sinusoid;

		}
		
		else if (k == 8){
			
			return 1;
		}
		
		
		else return 0;
	}

}
