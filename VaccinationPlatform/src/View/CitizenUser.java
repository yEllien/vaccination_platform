package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Controller.Appointment;
import Controller.Citizen;
import Controller.Vaccine;
import View.GraphicsComponents.CustomButton;
import View.GraphicsComponents.RoundedPanel;

public class CitizenUser extends User{

	Citizen citizen;
	
	JPanel appointmentTabContent;
	DataPanel dataPanel;	
	
	CitizenUser(VaccinationPlatformGUI frame, String SSN) throws SQLException {
		super(frame);
		citizen = new Citizen(SSN);
	}
	
	void setUp () {		
		try {
			citizen.loadData();
			createAccountTab();
			createAppointmentTab();
			updateAccountInfo();
			updateAppointments();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	void createAccountTab () {
		createOptionAndPage("Account");
		selectOption(0);
		dataPanel = new DataPanel(
				new String[]{
					"First name",
					"Last name",
					"SSN",
					//"TIN",
					"Date of birth", 
					"gender",
					"Phone number",
					"e-mail"
				},
				panelColor, backgroundColor);
		mainPanel.addToContentPanel(0, dataPanel);
	}
	
	void createAppointmentTab () {
		
		createOptionAndPage("Appointments");
		
		appointmentTabContent = new JPanel();
		appointmentTabContent.setBackground(backgroundColor);
		appointmentTabContent.setLayout(new BoxLayout(appointmentTabContent, BoxLayout.Y_AXIS));
		
		mainPanel.addToContentPanel(1, appointmentTabContent);
	}	
	
	public void updateAccountInfo () {
		setAccountName(citizen.getFirstName(), citizen.getLastName());
		dataPanel.updateData(citizen.getArray());
	}
	
	public void updateAppointments () {
		
		boolean complete = true;
		
		System.out.println("CitizenUser: updateAppointments()");
		for (Appointment appointment : citizen.getAppointments()) {
			System.out.println("CitizenUser: adding appointments");
			appointmentTabContent.add(
					new AppointmentEntry(
							this,
							appointment,
							citizen.getPostalCode(),
							mainPanel.getSize().width-150,
							backgroundColor, 
							new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {
									reloadAppointments();
								}
								
							}
							));
			if(appointment.getStatus()!=1)
				complete = false;
		}
		
		int appointmentCount = citizen.getAppointments().length;
		if (appointmentCount >= 1) {
			String vname = citizen.getAppointments()[0].getVaccineName();
			if (Vaccine.getVaccineDoses(vname) > appointmentCount)
				complete = false;
		}
		
		if (complete) {
			CustomButton getButton = new CustomButton(
					"Get Certificate", 
					CustomColors.dark_blue, 
					Color.white);
			
			JPanel buttonWrapper = new JPanel();
			buttonWrapper.setBackground(backgroundColor);
			buttonWrapper.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			
			buttonWrapper.add(getButton);
			
			getButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO generate certificate
				}
			});
			
			appointmentTabContent.add(buttonWrapper);
		}
		
	}
	
	
	public void reload () {
		try {
			citizen.loadData();
			appointmentTabContent.removeAll();
			updateAccountInfo();
			updateAppointments();
			System.out.println("CitizenUser: reload()");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void reloadAppointments() {
		System.out.println("reloading appointments");
		try {
			citizen.loadData();
			System.out.println("loaded data");
			appointmentTabContent.removeAll();
			System.out.println("removed all");
			updateAppointments();
			System.out.println("updated appointments");
			revalidate();
			repaint();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
