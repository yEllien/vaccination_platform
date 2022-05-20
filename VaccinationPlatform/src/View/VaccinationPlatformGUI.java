package View;
import View.LoginPage;
import View.GraphicsComponents.RoundedButton;
import View.GraphicsComponents.RoundedComponent;
import View.GraphicsComponents.RoundedLayeredPanel;
import View.LandingPage;

import javax.swing.border.LineBorder;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.*;

import javax.imageio.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import javax.swing.border.EmptyBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class OrangeButton extends RoundedButton {
	
	OrangeButton (String label) {
		super(label);
		setBackground(CustomColors.orange);
		setForeground(Color.white);
		//setBorder(new RoundedBorder(15));
	}
}

class GrayButton extends RoundedButton {
	
	GrayButton (String label) {
		super(label);
		setBackground(CustomColors.light_gray);
		setForeground(CustomColors.orange);
		//setBorder(new RoundedBorder(15));
	}
}

class FadedBlueButton extends RoundedButton {
	
	FadedBlueButton (String label) {
		super(label);
		setBackground(CustomColors.darker_faded_blue);
		setForeground(Color.white);
		//setBorder(new RoundedBorder(15));
	}
}

class WhiteButton extends RoundedButton {
	
	WhiteButton (String label) {
		super(label);
		setBackground(Color.white);
		setForeground(CustomColors.darker_faded_blue);
		//setBorder(new RoundedBorder(15));
	}
}

class OptionButton extends JPanel {
	
	RoundedLayeredPanel roundedPanel;
	
	JPanel buttonLayer = new JPanel();
	JButton button = new JButton();
	
	public OptionButton(Dimension d, Color fill, boolean filled, Color line, boolean lined, Color foreground) {
		System.out.println("Dimnension given : "+d);
		//super(parent, d, fill, filled, line, lined);
		
		//this.setBackground(new Color(0,0,0,0));
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
		
		
		System.out.println("BUTTON LAYER SIZE pref : "+buttonLayer.getPreferredSize());
		roundedPanel = new RoundedLayeredPanel(
				this, 
				button.getPreferredSize(),
				fill, filled,
				line, lined
				);
		
		roundedPanel.createLayer(buttonLayer);
		System.out.println("BUTTON LAYER SIZE : "+buttonLayer.getSize());
		this.add(roundedPanel);
		System.out.println("Yellow panel : "+ this);
	}
	
	/*
	public OptionButton (Dimension dimension, Color fill, boolean filled, Color line, boolean lined, Color foreground) {
		
		border = new RoundedComponent(dimension, fill, filled, line, lined);
		button = new JButton();
		
		
		if (filled)
			button.setBackground(fill);
		if (lined)
			button.setBackground(line);
		button.setForeground(foreground);
	}
	*/

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


public class VaccinationPlatformGUI extends JFrame {
	
	Dimension d = new Dimension(1280, 800);
	
	JPanel container = new JPanel();
	
	LandingPage landingPage = new LandingPage(this);
	
	NewLandingPage newLandingPage;
	
	CitizenLogin citizenLogin;
	MedicalLogin medicalLogin;
	AdminLogin adminLogin;
	
	UserPage userPage;
	
 	VaccinationPlatformGUI(){
 		/*
 		frame.setUndecorated(true);
 		frame.setShape(new RoundRectangle2D.Double(50,50,1500,1000,50,50));
 	    frame.setLocationByPlatform(true);
 	*/
 		//container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
 		
 		setSize(1300, 1000);
 		this.setPreferredSize(d);
 		
 		newLandingPage = new NewLandingPage(this);
 		
 		setContentPane(landingPage);
 		//setContentPane(newLandingPage);
 		
 		//citizenLogin = new CitizenLogin();
 		//setContentPane(citizenLogin);
 		//add(lp);
 		
 		this.addWindowListener(new WindowAdapter() {

 	        public void windowClosing(WindowEvent evt) {
 	            System.exit(0);
 	        }

 		}); 
 		
 		setVisible(true);
 		pack();
 	}
}
