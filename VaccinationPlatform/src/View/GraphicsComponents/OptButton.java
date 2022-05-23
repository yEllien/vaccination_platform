package View.GraphicsComponents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class OptButton extends RoundedLayeredPanel {
	
	JPanel buttonLayer;
	JButton button;
	
	public boolean active;
	public int id;
	
	boolean filled;
	boolean lined;
	
	Color fill;
	Color line;
	Color text;
	
	JPanel parent;
	
	public OptButton(JPanel parent, Dimension d, Color fill, boolean filled, Color line, boolean lined) {

		super(parent, new Dimension(d.width-10, d.height-20), fill, filled, line, lined);
		
		this.parent = parent;
		
		this.setPreferredSize(d);
		this.setMinimumSize(d);
		this.setMaximumSize(d);
		
		this.filled = filled;
		this.fill = fill;
		this.lined = lined;
		this.line = line;
		
		button = new JButton();
		button.setBorder(new EmptyBorder(0,0,0,0));
		/*
		if (filled)
			button.setBackground(fill);
		if (lined)
			button.setBackground(line);
		*/
		buttonLayer = new JPanel();
		buttonLayer.setLayout(new BoxLayout(buttonLayer, BoxLayout.X_AXIS));
		//buttonLayer.setBackground(Color.magenta);
		buttonLayer.setBackground(parent.getBackground());
		buttonLayer.add(Box.createGlue());
		buttonLayer.add(button);
		buttonLayer.add(Box.createGlue());
		
/*
		button.setPreferredSize(d);
		button.setMinimumSize(d);
		button.setMaximumSize(d);
	*/	
		createLayer(buttonLayer);
		
		unclicked();
		
		// TODO Auto-generated constructor stub
	}
	
	
	public void addActionListener (ActionListener l) {
		button.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				clicked();
			}
			
		});
		button.addActionListener(l);
	}
	
	public void setForegroundColor (Color color) {
		button.setForeground(color);
	}
	
	public void setText (String text) {
		button.setText(text);
	}
	
	public void clicked () {
		setFill(fill, !filled);
		setLine(line, !lined);
		active = true;
		
		if (filled)	{
			//button.setBackground(Color.orange);
			button.setBackground(parent.getBackground());
			buttonLayer.setBackground(parent.getBackground());
			button.setForeground(fill);			
		}
		else {
			button.setBackground(Color.orange);
			button.setBackground(fill);
			buttonLayer.setBackground(fill);
			button.setForeground(Color.white);
		}
	}
	
	public void unclicked () {
		setFill(fill, filled);
		setLine(line, lined);
		active = false;
		
		if (filled) {
			button.setBackground(fill);
			buttonLayer.setBackground(fill);
			button.setForeground(Color.white);			
		}
		else {
			button.setBackground(parent.getBackground());
			buttonLayer.setBackground(parent.getBackground());
			button.setForeground(fill);
		}
	}
	
}
