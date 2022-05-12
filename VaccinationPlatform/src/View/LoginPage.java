package View;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class LoginPage extends JPanel {
	
	VaccinationPlatformGUI frame;
	JPanel inputPanel;
	JPanel cancelPanel;
	
	JLabel loginLabel;
	JTextField usernameField;
	JPasswordField passwordField;
	OrangeButton loginButton;
	JButton forgotButton;
	JButton xButton;
	
	JLabel cancel;
	
	LoginPage(VaccinationPlatformGUI frame) {
		
		this.frame = frame;
		
		setBackground(CustomColors.lighter_blue);
		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(10,10,10,10));
		
		inputPanel = new JPanel();
		inputPanel.setBackground(Color.WHITE);
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
		
		cancelPanel = new JPanel();
		cancelPanel.setBackground(Color.WHITE);
		cancelPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		//cancelPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		try {
			BufferedImage cancelbuf = ImageIO.read(new File(System.getProperty("user.dir")+"/images/cancel_gray.png"));
			cancel = new JLabel(new ImageIcon(cancelbuf));			
			cancel.setAlignmentX(RIGHT_ALIGNMENT);
			
			cancel.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println("Clicked");
					back();
				}
				
			});
			//cancelPanel.add(Box.createRigidArea(new Dimension(10, 30)));
			cancelPanel.add(cancel);
			
			Dimension d = new Dimension(cancelbuf.getWidth()*2, cancelbuf.getHeight()*2);
			cancelPanel.setPreferredSize(d);
			cancelPanel.setMinimumSize(d);
			cancelPanel.setMaximumSize(d);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		
		loginLabel = new JLabel("");
		loginLabel.setBackground(CustomColors.darker_light_gray);
		loginLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		usernameField = new JTextField();
		usernameField.setText("Username");
		//usernameField.setPreferredSize(new Dimension(200, 30));
		usernameField.setMinimumSize(new Dimension(200, 30));
		usernameField.setMaximumSize(new Dimension(200, 30));
		usernameField.setBorder(new EmptyBorder(0, 0, 0, 0));
		usernameField.setAlignmentY(CENTER_ALIGNMENT);
		
		RoundedComponent usernameBorder = new RoundedComponent(usernameField.getMinimumSize(), CustomColors.light_gray, false, CustomColors.light_gray, true);
		//usernameBorder.setBackground(Color.decode("#DDDDDD"));
		usernameBorder.add(usernameField);
		usernameBorder.setAlignmentX(CENTER_ALIGNMENT);
		
		passwordField = new JPasswordField();
		passwordField.setText("Password");
		passwordField.setMinimumSize(new Dimension(200, 30));
		passwordField.setMaximumSize(new Dimension(200, 30));
		passwordField.setBorder(new EmptyBorder(0, 0, 0, 0));
		passwordField.setAlignmentY(CENTER_ALIGNMENT);
		
		RoundedComponent passwordBorder = new RoundedComponent(passwordField.getMinimumSize(), CustomColors.light_gray, false, CustomColors.light_gray, true);
		//passwordBorder.setBackground(Color.decode("#DDDDDD"));
		passwordBorder.add(passwordField);
		passwordBorder.setAlignmentX(CENTER_ALIGNMENT);
		
		loginButton = new OrangeButton("Log In");
		loginButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.userPage = new UserPage(frame);
				frame.setContentPane(frame.userPage);
				frame.revalidate();
			}
			
		});
		
		forgotButton = new JButton();
		forgotButton.setText("Forgot password?");
		forgotButton.setAlignmentX(CENTER_ALIGNMENT);
	
		xButton = new JButton();
		xButton.setText("X");
		
		inputPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		inputPanel.add(cancelPanel);
		inputPanel.add(loginLabel);
		inputPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		//inputPanel.add(usernameField);
		inputPanel.add(usernameBorder);
		inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		inputPanel.add(passwordBorder);
		inputPanel.add(passwordField);
		inputPanel.add(loginButton);
		inputPanel.add(forgotButton);
		
		inputPanel.setSize(new Dimension(10, 10));
		inputPanel.setPreferredSize(new Dimension(300, 300));
		
		add(inputPanel);
	}
	
	void back () {
		frame.setContentPane(frame.landingPage);
		frame.revalidate();
	}
}


class CitizenLogin extends LoginPage {
	
	CitizenLogin (VaccinationPlatformGUI frame) {
		super(frame);
		loginLabel.setText("Citizen Log In");
	}
}

class MedicalLogin extends LoginPage {
	
	MedicalLogin (VaccinationPlatformGUI frame) {
		super(frame);
		loginLabel.setText("Medical Staff Log In");
	}
}

class AdminLogin extends LoginPage {
	
	AdminLogin (VaccinationPlatformGUI frame) {
		super(frame);
		loginLabel.setText("Admin Staff Log In");
	}
}
