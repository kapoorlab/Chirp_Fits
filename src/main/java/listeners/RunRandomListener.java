package listeners;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import Fitting.TimeSeries.InteractiveChirpFit;
import chirpModels.UserChirpModel.UserModel;




public class RunRandomListener implements ItemListener {

	
	public InteractiveChirpFit parent;
	
	public RunRandomListener(InteractiveChirpFit parent) {
		this.parent = parent;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		
		parent.model = UserModel.Linear;
		parent.setdegreeenabled(false);
		
	}

}
