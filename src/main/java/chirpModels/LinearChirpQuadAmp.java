package chirpModels;

public class LinearChirpQuadAmp implements ChirpFitFunction {

	public double val(double t, double[] a, int totaltime, int timeindex) {

		double Sinusoid = (a[0] * t * t + a[1] * t + a[2]) * Math.cos(Math.toRadians(a[3] * t + (a[4] - a[3])* t * t / (2 * totaltime) 
				+ a[5])) + a[6];
		
		return Sinusoid;
	}

	/*
	 * Gradient function
	 * 
	 */
	public double grad(double t, double[] a, int totaltime, int k, int timeindex) {

		if (k == 0) {

		
			double Sinusoid = t * t * Math.cos(Math.toRadians(a[3] * t
					+ (a[4] - a[3]) * t * t / (2* totaltime)  + a[5]));
			return Sinusoid;

		}
		
		else if (k == 1) {

			double Sinusoid =  t * Math.cos(Math.toRadians(a[3] * t
					+ (a[4] - a[3]) * t * t / (2* totaltime)  + a[5]));
			return Sinusoid;

		}
		
		else if (k == 2) {

			double Sinusoid =   Math.cos(Math.toRadians(a[3] * t
					+ (a[4] - a[3]) * t * t / (2* totaltime)  + a[5]));
			return Sinusoid;

		}

		else if (k == 3) {

			double Sinusoid = -(a[0] * t * t + a[1] * t + a[2]) * Math.sin(Math.toRadians(a[3] * t
					+ (a[4] - a[3]) * t * t / (2 * totaltime)  + a[5])) * ( t -  t * t / (2 * totaltime) );

			return Sinusoid;

		}

		else if (k == 4) {

			double Sinusoid = -(a[0] * t * t + a[1] * t + a[2]) * Math.sin(Math.toRadians(a[3] * t
					+ (a[4] - a[3]) * t * t / (2 * totaltime)  + a[5])) * t * t
					/ (2 * totaltime) ;

			return Sinusoid;

		}

		

		else if (k == 5) {

			double Sinusoid = -(a[0] * t * t + a[1] * t + a[2])  * Math.sin(Math.toRadians(a[2] * t
					+ (a[3] - a[2]) * t * t / (2 * totaltime)  + a[4])) ;

			return Sinusoid;

		}
		
		else if (k == 6){
			
			return 1;
		}
		
		
		else return 0;
	}

}
