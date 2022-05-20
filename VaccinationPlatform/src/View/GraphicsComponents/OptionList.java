package View.GraphicsComponents;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OptionList extends JPanel {

	public ArrayList<OptButton> options = new ArrayList <OptButton> ();
	
	JPanel parent;

	Dimension optionSize;
	
	boolean filled;
	boolean lined;
	
	Color fill;
	Color line;
	
	Color transparent;

	Component topGlue, bottomGlue;
	
	public OptionList (JPanel parent, Dimension optionSize, Color fill, boolean filled, Color line, boolean lined) {
		this.parent = parent;
		transparent = parent.getBackground();
		this.setBackground(parent.getBackground());
		
		this.optionSize = optionSize;
		
		this.fill = fill;
		this.line = line;
		this.filled = filled;
		this.lined = lined;
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		topGlue = Box.createGlue();
		bottomGlue = Box.createGlue();
		
		//this.add(topGlue);
		//this.add(bottomGlue);
	}
	
	public void addOption (String optionName) {
		OptButton newOption = new OptButton(
				parent, 
				optionSize, 
				fill, filled, 
				line, lined);
		
		newOption.setText(optionName);
		newOption.addActionListener(new OptionListener());
		
		newOption.id = options.size();
		options.add(newOption);
		
		//this.remove(bottomGlue);
		this.add(newOption);
		//this.add(new JLabel("test"));
		this.add(bottomGlue);
	}
	
	class OptionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			for (OptButton option : options) {
				option.unclicked();
			}
		}
	}
	
	public void selectOption (int i) {
		options.get(i).clicked();
	}
	
}
