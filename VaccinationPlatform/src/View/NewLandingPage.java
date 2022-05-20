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
import View.GraphicsComponents.RoundedLayeredPanel;

public class NewLandingPage extends JPanel {
	
	VaccinationPlatformGUI frame;
	
	Color backgroundColor = CustomColors.light_blue;
	Color panelColor = CustomColors.very_light_gray;
	
	JPanel mainPanel;
	RoundedLayeredPanel basePanel;
	JPanel leftPanel;
	JPanel headerPanel;
	JPanel optionPanel;
	
	OrangeButton citizen = new OrangeButton("Citizen Log In");
	OrangeButton medical = new OrangeButton("Medical Staff Log In");
	GrayButton admin = new GrayButton("Admin Log In");
	
	JLabel image;
	
	NewLandingPage (VaccinationPlatformGUI frame) {
		super();
		
		this.frame = frame;
		setBackground(backgroundColor);
		//this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLayout(new GridBagLayout());
		/*
		GridBagConstraints gbc = new GridBagConstraints();
		*/
		
		JPanel wrapper = new JPanel ();
		wrapper.setPreferredSize(frame.d);
		wrapper.setMinimumSize(wrapper.getPreferredSize());
		wrapper.setMaximumSize(wrapper.getPreferredSize());
		wrapper.setBackground(backgroundColor);
		wrapper.setBounds(0,10, frame.getWidth()-100, frame.getHeight()-100);
		//wrapper.setLayout(new GridBagLayout());
		this.add(wrapper);
		
		System.out.println("Wrapper size : "+wrapper.getBounds());
		
		basePanel = new RoundedLayeredPanel(
				wrapper, 
				new Dimension(
						wrapper.getPreferredSize().width-50,
						wrapper.getPreferredSize().height-50),
				panelColor, true,
				panelColor, true);
		
		mainPanel = new JPanel();
		mainPanel.setBackground(panelColor);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		
		basePanel.createLayer(mainPanel);
		wrapper.add(basePanel);
		this.revalidate();
		
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
		//optionPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		optionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		optionPanel.setBackground(panelColor);
		optionPanel.setBorder(new EmptyBorder(0,100,0,0));
		//optionPanel.setBorder(new RoundedBorder(5, new Color(0,0,0,0)));
		
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
		
		
		/*
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 4;
		gbc.gridwidth = 4;
		*/
		//this.add(mainPanel);
		
		//mainPanel.setPreferredSize(new Dimension(frame.getHeight()-300, frame.getWidth()-300));

		//LineBorder line = new LineBorder(Color.blue, 2, true);
		//setBorder(line);
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