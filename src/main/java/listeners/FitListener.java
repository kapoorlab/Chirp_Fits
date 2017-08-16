package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Fitting.TimeSeries.InteractiveChirpFit;
import Fitting.TimeSeries.Split;

public class FitListener  implements ActionListener {

	
final InteractiveChirpFit parent;
	public FitListener(final InteractiveChirpFit parent){
		
		this.parent = parent;
	}
	@Override
	public void actionPerformed(ActionEvent e) {

		parent.displayclicked(parent.row);
		
		
	}
	
	
	
}
