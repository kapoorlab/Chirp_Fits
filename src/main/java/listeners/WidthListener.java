package listeners;

import java.awt.TextComponent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

import Fitting.TimeSeries.InteractiveChirpFit;


public class WidthListener implements TextListener{
	
	final InteractiveChirpFit parent;
	
	public WidthListener(final InteractiveChirpFit parent){
		
		this.parent = parent;
	}
	
	@Override
	public void textValueChanged(TextEvent e) {
		final TextComponent tc = (TextComponent)e.getSource();
		 
		
			    	String s = tc.getText();
			    	
						if (s.length() > 0)
					parent.Highfrequ = parent.Lowfrequ - Float.parseFloat(s) ;
					
			 
		 
		 
		 
		 
		
	}

}
