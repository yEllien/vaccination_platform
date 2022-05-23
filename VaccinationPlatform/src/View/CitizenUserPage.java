package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import View.GraphicsComponents.CustomButton;
import View.GraphicsComponents.RoundedPanel;

public class CitizenUserPage extends UserPage{

	DataPanel dataPanel;	
	JPanel appointmentTabContent;
	
	CitizenUserPage(VaccinationPlatformGUI frame) {
		super(frame);
		//loadOptions();
	}
	
	public void loadAccountInfo () {
		//TODO get name
		leftPanel.accountName.setText("Citizen name");
		dataPanel.loadData();
	}
	
	public void loadAppointments () {

		AppointmentEntry ae = new AppointmentEntry(
				"1",
				"Medical Center Name",
				"Vaccine name",
				"Friday, 09-01-2020, 11:00",
				1,
				 mainPanel.getSize().width-150,
				backgroundColor);
		appointmentTabContent.add(ae);
		appointmentTabContent.add(Box.createRigidArea(new Dimension(0, 20)));

		ae = new AppointmentEntry(
				"2",
				"Med",
				"Vac",
				"Fri-2020, 11:00",
				0,
				mainPanel.getSize().width-150,
				backgroundColor);
		appointmentTabContent.add(ae);
	}
	
	void setUp () {		
		
		createAccountTab();
		
		createAppointmentTab();
	}
	
	void createAccountTab () {
		createOptionAndPage("Account");
		selectOption(0);
		dataPanel = new DataPanel(
				new String[]{
					"First name",
					"Last name",
					"Date of birth", 
					"SSN",
					"TIN",
					"e-mail",
					"Phone number"
				},
				panelColor, backgroundColor);
		mainPanel.addToContentPanel(0, dataPanel);
		dataPanel.init();
	}
	
	void createAppointmentTab () {
		
		createOptionAndPage("Appointments");
		
		appointmentTabContent = new JPanel();
		appointmentTabContent.setBackground(backgroundColor);
		appointmentTabContent.setLayout(new BoxLayout(appointmentTabContent, BoxLayout.Y_AXIS));
		
		mainPanel.addToContentPanel(1, appointmentTabContent);
	}
	
	public void reload () {
		
		loadAccountInfo();
		loadAppointments();
		
	}

	
	
}
