package chirpModels;

public class LinearChirpPolyAmp implements ChirpFitFunction {

	public double val(double t, double[] a, int totaltime, int timeindex, int degree) {

		double Sinusoid = polyfunc(t, a, degree) * Math.cos(Math.toRadians(a[degree + 1] * t + (a[degree + 2] - a[degree + 1])* t * t / (2 * totaltime) 
				+ a[degree + 3])) + a[degree + 4];
		return Sinusoid;
	}
	
	
	public double polyfunc(double t, double[] a, int degree){
		
		double poly = 0;
		
		for (int j = degree; j >= 0; --j)
			poly+= a[j] * Math.pow(t, j);
		
		return poly;
	}
	
	public double derivpolyfunc(double t, double[] a, int k){
		
		double poly = Math.pow(t, k);
		
		return poly;
	}
	
	
	

	/*
	 * Gradient function
	 * 
	 */
	public double grad(double t, double[] a, int totaltime, int k, int timeindex, int degree) {
		
		double poly = polyfunc(t, a, degree) ;
		if (k <= degree) {

		
			double Sinusoid = derivpolyfunc(t, a, k) * Math.cos(Math.toRadians(a[degree + 1] * t
					+ (a[degree + 2] - a[degree + 1]) * t * t / (2* totaltime)  + a[degree + 3]));
			return Sinusoid;

		}

		else if (k == degree + 1) {

			double Sinusoid = -poly * Math.sin(Math.toRadians(a[degree + 1] * t
					+ (a[degree + 2] - a[degree + 1]) * t * t / (2 * totaltime)  + a[degree + 3])) * ( t -  t * t / (2 * totaltime) );

			return Sinusoid;

		}

		else if (k == degree + 2) {

			double Sinusoid = -poly * Math.sin(Math.toRadians(a[degree + 1] * t
					+ (a[degree + 2] - a[degree + 1]) * t * t / (2 * totaltime)  + a[degree + 3])) * t * t
					/ (2 * totaltime) ;

			return Sinusoid;

		}

		

		else if (k == degree + 3) {

			double Sinusoid = -poly * Math.sin(Math.toRadians(a[degree + 1] * t
					+ (a[degree + 2] - a[degree + 1]) * t * t / (2 * totaltime) + a[degree + 3])) ;

			return Sinusoid;

		}
		
		else if (k == degree + 4){
			
			return 1;
		}
		
		
		else return 0;
	}

}
