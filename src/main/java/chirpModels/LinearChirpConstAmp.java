package chirpModels;

public class LinearChirpConstAmp implements ChirpFitFunction {

	public double val(double t, double[] a, int totaltime, int timeindex, int degree) {

		double Sinusoid = a[0] * Math.cos(Math.toRadians(a[1] * t + (a[2] - a[1])* t * t / (2 * totaltime) 
				+ a[3])) + a[4];
		
		return Sinusoid;
	}

	/*
	 * Gradient function
	 * 
	 */
	public double grad(double t, double[] a, int totaltime, int k, int timeindex, int degree) {

		if (k == 0) {

		
			double Sinusoid = Math.cos(Math.toRadians(a[1] * t
					+ (a[2] - a[1]) * t * t / (2* totaltime)  + a[3]));
			return Sinusoid;

		}

		else if (k == 1) {

			double Sinusoid = -a[0] * Math.sin(Math.toRadians(a[1] * t
					+ (a[2] - a[1]) * t * t / (2 * totaltime)  + a[3])) * ( t -  t * t / (2 * totaltime) );

			return Sinusoid;

		}

		else if (k == 2) {

			double Sinusoid = -a[0] * Math.sin(Math.toRadians(a[1] * t
					+ (a[2] - a[1]) * t * t / (2 * totaltime)  + a[3])) * t * t
					/ (2 * totaltime) ;

			return Sinusoid;

		}

		

		else if (k == 3) {

			double Sinusoid = -a[0] * Math.sin(Math.toRadians(a[1] * t
					+ (a[2] - a[1]) * t * t / (2 * totaltime) + a[3])) ;

			return Sinusoid;

		}
		
		else if (k == 4){
			
			return 1;
		}
		
		
		else return 0;
	}

}
