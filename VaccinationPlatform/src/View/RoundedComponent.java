package View;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagLayout;

public class RoundedComponent extends JPanel {
	
	Dimension d;
	Color fill, line;
	boolean filled, lined;
	
	RoundedComponent(Dimension d, Color fill, boolean filled, Color line, boolean lined)
	{
		this.fill = fill;
		this.line = line;
		
		this.filled = filled;
		this.lined = lined; 
		
		this.d = new Dimension(d.width, d.height);
		this.setPreferredSize(d);
		this.setMinimumSize(d);
		this.setMaximumSize(d);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	public void paint(Graphics g) {
		
		if(filled)
		{
			g.setColor(fill);
			g.fillRoundRect(0, 0, (int)d.getWidth() - 1, (int)d.getHeight() - 1, 20, 20);			
		}
		
		// draw the perimeter of the button
		//g.setColor(getBackground().darker().darker().darker());
		
		if (lined)
		{
			g.setColor(line);
			g.drawRoundRect(0, 0, (int)d.getWidth() - 1, (int)d.getHeight() - 1, 20, 20);			
		}
	}

}
