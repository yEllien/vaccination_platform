package View.GraphicsComponents;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class RoundedPanel extends JLayeredPane {

	protected RoundedComponent rounded;
	
	JPanel layers[] = new JPanel[2];
	
	Color fill, line, background;
	boolean filled, lined;
	
	public RoundedPanel(Color fill, boolean filled, Color line, boolean lined, Color c) {

		this.fill = fill;
		this.line = line;
		this.filled = filled;
		this.lined = lined;
		this.background = c;
			
	}
		
	public void addComponent (Component c) {
		layers[1].add(c);
	}
	
	public void setLayer (JPanel layer) {
		
		layers[1] = layer;
		this.add(layer, new Integer(1));
	}
	
	public void draw () {
		
		rounded = new RoundedComponent(
				new Dimension(
						layers[1].getSize().width+100,
						layers[1].getSize().height+100), 
				fill, filled, line, lined, background);
		
		JPanel layer = new JPanel();
		layer.setBounds(this.getBounds());
		layer.setBackground(Color.blue);
		layer.setLayout(new BoxLayout(layer, BoxLayout.Y_AXIS));
		layer.add(rounded);
		
		layers[0] = layer;
		
		this.add(layer, new Integer(1));
		rounded.repaint();
	}
	
	public void setFill (Color fill, boolean filled) {
		rounded.setFill(fill, filled);
	}
	
	public void setLine (Color line, boolean lined) {
		rounded.setLine(line, lined);
	}
}
