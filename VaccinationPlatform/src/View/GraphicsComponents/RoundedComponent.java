package View.GraphicsComponents;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.geom.RoundRectangle2D;

public class RoundedComponent extends JPanel {
	
	Dimension d;
	Color fill, line;
	boolean filled, lined;
	RoundRectangle2D.Float roundedFill;
	RoundRectangle2D.Float roundedLine;
	
	int times;
	
	public RoundedComponent(Dimension d, Color fill, boolean filled, Color line, boolean lined, Color transparent)
	{
		times = 0;
		this.fill = fill;
		this.line = line;
		
		this.filled = filled;
		this.lined = lined; 
		
		this.d = new Dimension(d.width, d.height);
		this.setPreferredSize(d);
		this.setMinimumSize(d);
		this.setMaximumSize(d);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.setBackground(transparent);
		//this.setBackground(Color.CYAN);
		//this.setBackground(new Color(0,0,0,0));
		
		//roundedFill = new RoundRectangle2D.Float(0, 0, (int)d.getWidth() - 1, (int)d.getHeight() - 1, 20, 20);
		//roundedLine = new RoundRectangle2D.Float(0, 0, (int)d.getWidth() - 1, (int)d.getHeight() - 1, 20, 20);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		System.out.println(g);
		System.out.println(times);
		times++;
		
		if(filled)
		{
			System.out.println("Filled");
			//g.setColor(fill);
			g.setColor(fill);
			g.fillRoundRect(0, 0, (int)d.getWidth() - 1, (int)d.getHeight() - 1, 20, 20);			
		}
		
		// draw the perimeter of the button
		//g.setColor(getBackground().darker().darker().darker());
		
		if (lined)
		{
			System.out.println("Lined");
			g.setColor(line);
			g.drawRoundRect(0, 0, (int)d.getWidth() - 1, (int)d.getHeight() - 1, 20, 20);			
		}
	}

	public void setFill (Color fill, boolean filled) {
		this.filled = filled;
		this.fill = fill;
		//this.paint(getGraphics());
		this.repaint();
		
		this.revalidate();
	}
	
	public void setLine (Color line, boolean lined) {
		
		this.lined = lined;
		this.line = line;
		//this.paint(getGraphics());
		this.repaint();
		this.revalidate();
	}
	
}
