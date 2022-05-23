package View.GraphicsComponents;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class RoundedLayeredPanel extends JLayeredPane {

	protected RoundedComponent rounded;
	
	ArrayList<JPanel> layers;
	
	public RoundedLayeredPanel (JPanel parent, Dimension d, Color fill, boolean filled, Color line, boolean lined) {

		this(parent.getBounds().x, parent.getBounds().y, d,
				fill, filled, line, lined,
				parent.getBackground());
	}
	
	public RoundedLayeredPanel(int x, int y, Dimension d, Color fill, boolean filled, Color line, boolean lined, Color c) {

		layers = new ArrayList<JPanel>();

		this.setBounds(x, y, d.width, d.height);
		
		rounded = new RoundedComponent(d, fill, filled, line, lined, c);
		
		
		JPanel layer = new JPanel();
		layer.setBounds(this.getBounds());
		layer.setBackground(Color.green);
		layer.setLayout(new BoxLayout(layer, BoxLayout.Y_AXIS));
		layer.add(rounded);
		
		layers.add(layer);
		this.add(layer, new Integer(1));
	}
		
	void addComponent (Component c, int l) {
		if (l<layers.size()-1) {
			System.out.println("Layer does not exist");
			return;
		}
		
		layers.get(l).add(c);
	}
	
	public int createLayer (JPanel layer) {
		JPanel l0 = layers.get(0);
		
		layer.setBounds(
				l0.getBounds().x+5,
				l0.getBounds().y+5,
				l0.getBounds().width-10,
				l0.getBounds().height-10);
		layers.add(layer);
		this.add(layer, new Integer(layers.size()));
		return layers.size();
	}
	
	public void setFill (Color fill, boolean filled) {
		rounded.setFill(fill, filled);
	}
	
	public void setLine (Color line, boolean lined) {
		rounded.setLine(line, lined);
	}
}
