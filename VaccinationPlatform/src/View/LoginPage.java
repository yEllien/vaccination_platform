package View;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
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

import View.GraphicsComponents.RoundedComponent;
import View.GraphicsComponents.TextField;

abstract public class LoginPage extends JPanel {
	
	VaccinationPlatformGUI frame;
	
	Color backgroundColor = CustomColors.light_blue; //CustomColors.lighter_blue;
	Color panelColor = Color.white;//CustomColors.very_light_gray;
	Color borderColor = CustomColors.light_gray;
	Color darkFontColor = CustomColors.dark_gray;
	Color regularFontColor = CustomColors.gray;
	Color lightFontColor = CustomColors.darker_light_gray;
	
	JPanel labelPanel;
	JPanel inputPanel;
	JPanel cancelPanel;
	JPanel fieldsPanel;
	
	RoundedComponent centerBorder;
	RoundedComponent usernameBorder;
	RoundedComponent passwordBorder;
	
	JLabel taxisLogo;
	JLabel loginLabel;
	
	TextField centerField;
	TextField usernameField;
	JPasswordField passwordField;
	
	OrangeButton loginButton;
	JButton forgotButton;
	JButton xButton;
	
	JLabel cancel;
	
	String login;
	String password;
	
	LoginPage(VaccinationPlatformGUI frame) {
		
		this.frame = frame;
		
		setBackground(backgroundColor);
		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(10,10,10,10));
		
		inputPanel = new JPanel();
		inputPanel.setBackground(panelColor);
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
		inputPanel.setSize(new Dimension(300, 300));
		inputPanel.setPreferredSize(new Dimension(300, 300));
		
		fieldsPanel = new JPanel();
		fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
		fieldsPanel.setBackground(panelColor);
		//fieldsPanel.setLayout(new BorderLayout());
		
		cancelPanel = new JPanel();
		cancelPanel.setBackground(panelColor);
		cancelPanel.setLayout(new BoxLayout(cancelPanel, BoxLayout.X_AXIS));
		
		//cancelPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		try {
			int scaledSize = 15;
			BufferedImage cancelbuf = ImageIO.read(new File(System.getProperty("user.dir")+"/images/cancel_gray.png"));
			Image scaled = cancelbuf.getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
			cancel = new JLabel(new ImageIcon(scaled));			
			cancel.setAlignmentX(RIGHT_ALIGNMENT);
			
			cancel.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					back();
				}
				
			});
			cancelPanel.add(Box.createHorizontalGlue());
			cancelPanel.add(cancel);
			cancelPanel.add(Box.createRigidArea(new Dimension(10, 30)));
			
			Dimension d = new Dimension(inputPanel.getSize().width, scaledSize);
			cancelPanel.setPreferredSize(d);
			cancelPanel.setMinimumSize(d);
			cancelPanel.setMaximumSize(d);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

		loginLabel = new JLabel("");
		loginLabel.setForeground(CustomColors.gray);
		loginLabel.setFont(new Font(loginLabel.getFont().getName(), Font.PLAIN, 20));
		loginLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));

		try {
			int scaledSize = 40;
			BufferedImage cancelbuf = ImageIO.read(new File(System.getProperty("user.dir")+"/images/taxis_logo.png"));
			Image scaled = cancelbuf.getScaledInstance(scaledSize, scaledSize, Image.SCALE_SMOOTH);
			taxisLogo = new JLabel(new ImageIcon(scaled));			
			taxisLogo.setAlignmentX(LEFT_ALIGNMENT);
			
			labelPanel.add(Box.createHorizontalGlue());
			labelPanel.add(taxisLogo);
			taxisLogo.setVisible(false);
			labelPanel.add(Box.createRigidArea(new Dimension(10, 0)));
			labelPanel.add(loginLabel);
			labelPanel.add(Box.createHorizontalGlue());
			labelPanel.setBackground(panelColor);
			
			Dimension d = new Dimension(inputPanel.getSize().width, scaledSize);
			labelPanel.setPreferredSize(d);
			labelPanel.setMinimumSize(d);
			labelPanel.setMaximumSize(d);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		
		centerField = new TextField();
		centerField.setPlaceholder("Placeholder");
		centerField.setName("centerField");
		centerField.setMinimumSize(new Dimension(200, 30));
		centerField.setMaximumSize(new Dimension(200, 30));
		centerField.setBorder(new EmptyBorder(0, 0, 0, 0));
		centerField.setAlignmentY(CENTER_ALIGNMENT);
		centerField.setBackground(panelColor);
		
		Dimension d = new Dimension (centerField.getMinimumSize());
		d.width+=10;
		centerBorder = new RoundedComponent(
				d, 
				borderColor, false, 
				borderColor, true,
				panelColor);
		centerBorder.add(centerField);
		centerBorder.setAlignmentX(CENTER_ALIGNMENT);
		
		
		usernameField = new TextField();
		usernameField.setPlaceholder("Placeholder");
		usernameField.setName("usernameField");
		usernameField.setMinimumSize(new Dimension(200, 30));
		usernameField.setMaximumSize(new Dimension(200, 30));
		usernameField.setBorder(new EmptyBorder(0, 0, 0, 0));
		usernameField.setAlignmentY(CENTER_ALIGNMENT);
		usernameField.setBackground(panelColor);
		
		d = new Dimension (usernameField.getMinimumSize());
		d.width+=10;
		usernameBorder = new RoundedComponent(
				d, 
				borderColor, false, 
				borderColor, true,
				panelColor);
		usernameBorder.add(usernameField);
		usernameBorder.setAlignmentX(CENTER_ALIGNMENT);
		
		
		passwordField = new JPasswordField();
		passwordField.setText("Password");
		passwordField.setMinimumSize(new Dimension(200, 30));
		passwordField.setMaximumSize(new Dimension(200, 30));
		passwordField.setBorder(new EmptyBorder(0, 0, 0, 0));
		passwordField.setAlignmentY(CENTER_ALIGNMENT);
		passwordField.setBackground(panelColor);
		
		d = new Dimension (passwordField.getMinimumSize());
		d.width+=10;
		passwordBorder = new RoundedComponent(
				d, 
				borderColor, false, 
				borderColor, true,
				panelColor);
		passwordBorder.add(passwordField);
		passwordBorder.setAlignmentX(CENTER_ALIGNMENT);
		
		
		fieldsPanel.setPreferredSize(new Dimension(d.width, 3*d.height + 20));
		fieldsPanel.setMinimumSize(new Dimension(d.width, 3*d.height + 20));
		fieldsPanel.setMaximumSize(new Dimension(d.width, 3*d.height + 20));
		
		loginButton = new OrangeButton("Log In");
		loginButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
			
		});
		
		forgotButton = new JButton();
		forgotButton.setText("Forgot password?");
		forgotButton.setAlignmentX(CENTER_ALIGNMENT);
		forgotButton.setBackground(panelColor);
		forgotButton.setForeground(CustomColors.darker_light_gray);
		forgotButton.setBorder(new EmptyBorder(0,0,0,0));
	
		xButton = new JButton();
		xButton.setText("X");
		
		inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		inputPanel.add(cancelPanel);
		
		inputPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		inputPanel.add(labelPanel);
		inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		fieldsPanel.add(Box.createVerticalGlue());
		centerBorder.setAlignmentY(CENTER_ALIGNMENT);
		fieldsPanel.add(centerBorder);
		fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		usernameBorder.setAlignmentY(CENTER_ALIGNMENT);
		fieldsPanel.add(usernameBorder);
		fieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		passwordBorder.setAlignmentY(CENTER_ALIGNMENT);
		fieldsPanel.add(passwordBorder);
		fieldsPanel.add(Box.createVerticalGlue());
		
		inputPanel.add(fieldsPanel);
		
		inputPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		inputPanel.add(loginButton);
		inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		inputPanel.add(forgotButton);
		
		taxisLogo.setVisible(false);
		centerBorder.setVisible(false);
		
		add(inputPanel);		
	}
	
	void makeXPanel () {
		
	}
		
	abstract void login () ;
	
	void back () {
		frame.setContentPane(frame.landingPage);
		frame.revalidate();
	}
}


class CitizenLogin extends LoginPage {
	
	CitizenLogin (VaccinationPlatformGUI frame) {
		super(frame);
		taxisLogo.setVisible(true);
		loginLabel.setText("TaxisNet");
		usernameField.setPlaceholder("SSN");
		revalidate();
	}
	
	@Override
	void login() {
		//TODO ask for user with ssn from usernameField
		String SSN = usernameField.getText();
		String password = passwordField.getText();
		
		frame.userPage = new CitizenUser(frame, SSN);
		frame.setContentPane(frame.userPage);
		frame.userPage.setUp();
		frame.userPage.reload();
		frame.revalidate();
	}
	
	
}

class MedicalLogin extends LoginPage {
	
	MedicalLogin (VaccinationPlatformGUI frame) {
		super(frame);
		centerBorder.setVisible(true);
		loginLabel.setText("Medical Staff Log In");
		usernameField.setPlaceholder("Employee ID");
		revalidate();
	}


	@Override
	void login() {
		//TODO ask for user with employee id from usernameField
		//and employer id from center fied
		String employerID = centerField.getText();
		String employeeID = usernameField.getText();
		String password = passwordField.getText();
		
		frame.userPage = new MedicalUser(frame, employeeID, employerID);
		frame.setContentPane(frame.userPage);
		frame.userPage.setUp();
		frame.userPage.reload();
		frame.revalidate();
	}
}

class AdminLogin extends LoginPage {
	
	AdminLogin (VaccinationPlatformGUI frame) {
		super(frame);
		loginLabel.setText("Administrator Log In");
		revalidate();
	}

	@Override
	void login() {
		frame.userPage = new AdminUser(frame);
		frame.setContentPane(frame.userPage);
		frame.userPage.setUp();
		frame.userPage.reload();
		frame.revalidate();
	}

}
