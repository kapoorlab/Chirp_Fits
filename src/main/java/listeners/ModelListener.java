package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import Fitting.TimeSeries.InteractiveChirpFit;
import chirpModels.UserChirpModel.UserModel;



	
	
	public class ModelListener implements ActionListener {

		
		
		final InteractiveChirpFit parent;
		final JComboBox<String> choice;
		
		public ModelListener(final InteractiveChirpFit parent, final JComboBox<String> choice){
			
			this.parent = parent;
			this.choice = choice;
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			
			int selectedindex = choice.getSelectedIndex();
			
			if (selectedindex == 0){
				
				parent.model = UserModel.LinearLinearAmp;
				
			}
			
			
			if (selectedindex == 1){
				
				parent.model = UserModel.LinearBiquadAmp;
				
			}
			
			if (selectedindex == 2){
				
				parent.model = UserModel.LinearSixthOrderAmp;
				
			}
			
            if (selectedindex == 3){
				
				parent.model = UserModel.Linear;
				
			}
			
			
			
		}
	
}
