package View;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import View.GraphicsComponents.RoundPanel;
import View.GraphicsComponents.RoundedBorder;
import View.GraphicsComponents.RoundedComponent;

public class LandingPage extends JPanel {
	
	VaccinationPlatformGUI frame;
	
	Color backgroundColor = CustomColors.light_blue;
	Color panelColor = CustomColors.very_light_gray;
	
	JPanel mainPanel;
	JPanel leftPanel;
	JPanel headerPanel;
	JPanel optionPanel;
	
	OrangeButton citizen = new OrangeButton("Citizen Log In");
	OrangeButton medical = new OrangeButton("Medical Staff Log In");
	GrayButton admin = new GrayButton("Admin Log In");
	
	JLabel image;
	
	LandingPage (VaccinationPlatformGUI frame) {
		super();
		
		this.frame = frame;
		setBackground(backgroundColor);
		setLayout(new GridBagLayout());

		mainPanel = new JPanel();
		mainPanel.setBackground(panelColor);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));		
		
		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		leftPanel.setBackground(panelColor);
		
		RoundedBorder border = new RoundedBorder(5, new Color(0,0,0,0));
		
		
		headerPanel = new JPanel();
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
		headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		headerPanel.setBackground(panelColor);
		headerPanel.setBorder(new RoundedBorder(5, new Color(0,0,0,0)));
		
		optionPanel = new JPanel();		
		optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
		optionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		optionPanel.setBackground(panelColor);
		optionPanel.setBorder(new EmptyBorder(0,100,0,0));
		
		JLabel header = new JLabel();
		header.setText("Covid-19 Vaccination");
		header.setForeground(CustomColors.dark_blue);
		
		JLabel subheader = new JLabel();
		subheader.setText("View or book your Covid-19 vaccine appointment");
		subheader.setForeground(CustomColors.gray);
		
		headerPanel.add(Box.createRigidArea(new Dimension(0, 40)));
		headerPanel.add(header);
		headerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		headerPanel.add(subheader);
		headerPanel.add(Box.createRigidArea(new Dimension(0, 40)));
		
		citizen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				citizenLogin();
			}
		});
		
		medical.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				medicalLogin();
			}
		});
		
		admin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adminLogin();
			}
		});
		
		optionPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		optionPanel.add(citizen);
		optionPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		optionPanel.add(medical);
		optionPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		optionPanel.add(admin);
		
		leftPanel.add(headerPanel);
		leftPanel.add(optionPanel);
		mainPanel.add(leftPanel);
		
		try {
			System.out.println(System.getProperty("user.dir"));
			image = new JLabel(new ImageIcon(ImageIO.read(new File(System.getProperty("user.dir")+"/images/image.png"))));			
			mainPanel.add(image);
		}
		catch(Exception x) {
			System.out.println("Could not load image");
		}
		
		
		this.add(mainPanel);
		
		setVisible(true);
	}

	void citizenLogin() {
		frame.citizenLogin = new CitizenLogin(frame);
		//frame.getContentPane().removeAll();
		frame.setContentPane(frame.citizenLogin);
		
		frame.revalidate();
	}
	
	void medicalLogin() {
		frame.medicalLogin = new MedicalLogin(frame);
		//frame.getContentPane().removeAll();
		frame.setContentPane(frame.medicalLogin);
		
		frame.revalidate();
	}
	
	void adminLogin() {
		frame.adminLogin = new AdminLogin(frame);
		//frame.getContentPane().removeAll();
		frame.setContentPane(frame.adminLogin);
		
		frame.revalidate();
	}
}