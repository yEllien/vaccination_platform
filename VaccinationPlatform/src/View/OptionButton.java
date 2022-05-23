package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import View.GraphicsComponents.RoundedLayeredPanel;

public class OptionButton extends JPanel {
	
	RoundedLayeredPanel roundedPanel;
	
	JPanel buttonLayer = new JPanel();
	JButton button = new JButton();
	
	public OptionButton(Dimension d, Color fill, boolean filled, Color line, boolean lined, Color foreground) {

		this.setBackground(Color.yellow);
		
		buttonLayer.setLayout(new BoxLayout(buttonLayer, BoxLayout.Y_AXIS));
		buttonLayer.setBackground(Color.MAGENTA);

		if (filled)
			button.setBackground(fill);
		if (lined)
			button.setBackground(line);
		button.setForeground(foreground);
		button.setPreferredSize(d);
		button.setMinimumSize(d);
		button.setMaximumSize(d);
		
		buttonLayer.add(button);
		
		
		this.setPreferredSize(button.getPreferredSize());
		this.setMinimumSize(button.getPreferredSize());
		
		
		roundedPanel = new RoundedLayeredPanel(
				this, 
				button.getPreferredSize(),
				fill, filled,
				line, lined
				);
		
		roundedPanel.createLayer(buttonLayer);
		this.add(roundedPanel);
	}

	void addActionListener (ActionListener l) {
		button.addActionListener(l);
		button.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				clicked();
			}
			
		});
	}
	
	public void setForegroundColor (Color color) {
		button.setForeground(color);
	}
	
	void setText (String text) {
		button.setText(text);
	}
	
	void clicked () {
		roundedPanel.setFill(CustomColors.faded_blue, true);
		button.setForeground(Color.white);
	}
	
	void unclicked () {
		roundedPanel.setFill(CustomColors.faded_blue, false);
		button.setForeground(CustomColors.faded_blue);
	}
}

