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


public class VaccinationPlatformGUI extends JFrame {
	
	Dimension d = new Dimension(1280, 800);
	
	JPanel container = new JPanel();
	
	LandingPage landingPage = new LandingPage(this);

	
	CitizenLogin citizenLogin;
	MedicalLogin medicalLogin;
	AdminLogin adminLogin;
	
	UserPage userPage;
	
 	VaccinationPlatformGUI(){
 		
 		setSize(1300, 1000);
 		this.setPreferredSize(d);
 		
 		setContentPane(landingPage);
 		
 		this.addWindowListener(new WindowAdapter() {

 	        public void windowClosing(WindowEvent evt) {
 	            System.exit(0);
 	        }

 		}); 
 		
 		setVisible(true);
 		pack();
 	}
}
