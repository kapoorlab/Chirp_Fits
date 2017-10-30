package chirpModels;

public class UserChirpModel {

	
	
	public static enum UserModel{
		
		Linear, LinearConstAmp, LinearLinearAmp, LinearQuadraticAmp, LinearCubeAmp, LinearBiquadAmp, LinearSixthOrderAmp, LinearPolyAmp;
		
	}
	
	
	protected static UserModel Linear;
	protected static UserModel Quadratic;
	protected static UserModel LinearConstAmp;
	protected static UserModel LinearLinearAmp;
	protected static UserModel LinearQuadraticAmp;
	protected static UserModel LinearCubeAmp;
	protected static UserModel LinearBiquadAmp;
	protected static UserModel LinearSixthOrderAmp;
	protected static UserModel LinearPolyAmp;
}
