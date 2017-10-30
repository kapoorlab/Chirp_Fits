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
				
				parent.model = UserModel.LinearPolyAmp;
				parent.setdegreeenabled(true);
			}
			
			
			if (selectedindex == 1){
				
				parent.model = UserModel.Linear;
				parent.setdegreeenabled(false);
				
			}
			
			
			
			
			
		}
	
}
