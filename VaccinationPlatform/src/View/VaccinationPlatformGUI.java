package View;
import View.LoginPage;
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
		setBackground(CustomColors.gray);
		setForeground(CustomColors.orange);
		//setBorder(new RoundedBorder(15));
	}
}

class UserPage extends JPanel {
	
	VaccinationPlatformGUI frame;
	
	JPanel leftPanel;
	JPanel mainPanel;
	
	UserPage (VaccinationPlatformGUI frame) {
		
		this.frame = frame;
		setBackground(Color.white);
		
		fillLeft();
		fillMain();
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		add(leftPanel);
		add(mainPanel);
	}
	
	void fillLeft () {
		leftPanel = new LeftPanel();
	}
	
	void fillMain () {
		mainPanel = new MainPanel();
	}
	
	class LeftPanel extends RoundedComponent{
		
		LeftPanel () {
			super(new Dimension(200, frame.getHeight()-60), CustomColors.lighter_blue, true, CustomColors.lighter_blue, true);
		}
	}
	
	class MainPanel extends JPanel{
		
		MainPanel () {
			setPreferredSize(new Dimension(frame.getWidth()-leftPanel.getWidth(), frame.getHeight()));
			setBackground(new Color(0,0,0,0));
		}
	}
}


public class VaccinationPlatformGUI extends JFrame {
	
	JPanel container = new JPanel();
	
	LandingPage landingPage = new LandingPage(this);
	
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
 		this.setPreferredSize(new Dimension(1300, 1000));
 		
 		setContentPane(landingPage);
 		//citizenLogin = new CitizenLogin();
 		//setContentPane(citizenLogin);
 		//add(lp);
 		setVisible(true);
 		pack();
 	}
}
