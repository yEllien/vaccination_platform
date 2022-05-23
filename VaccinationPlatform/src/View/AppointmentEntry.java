package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.KeyPair;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import View.Calendar.TimeslotColumn;
import View.Calendar.TimeslotEntry;
import View.GraphicsComponents.CustomButton;


class MedicalCenter {
	
	int id;
	String name;
	String vaccine;
	
	MedicalCenter (int id, String name, String vaccine) {
		this.id = id;
		this.name = name;
		this.vaccine = vaccine;
	}
	
	public String toString () {
		return name+", "+vaccine;
	}
}

public class AppointmentEntry extends JPanel {
	
	Color backgroundColor;
	
	MedicalCenter [] medicalCenterPairs;
	JComboBox<MedicalCenter> medicalCenters;
	
	//ArrayList<Timeslot> timeslotList;
	//JPanel timeslots;
	Calendar calendar;
	
	String doseNoStr;
	String medicalCenterStr;
	String vaccineNameStr;
	String dateStr;
	int statusInt;
	
	JPanel border;
	
	int maxWidth;
	int maxHeight = 35;
	int rmw;
	int q;
	
	JPanel yaxisContent;
	JPanel content;
	JPanel doseWrapper;
	JPanel dataPanel;
	JPanel statusPanel;
	JPanel confirmationPanel;
	JPanel abortConfirmationPanel;
	
	AppointmentEntry (
			String doseNoStr, 
			String medicalCenterStr, 
			String vaccineNameStr,
			String dateStr,
			int statusInt,
			int width,
			Color color) {
		
		this.doseNoStr = doseNoStr;
		this.medicalCenterStr = medicalCenterStr;
		this.vaccineNameStr = vaccineNameStr;
		this.dateStr = dateStr;
		this.statusInt = statusInt;
		this.maxWidth = width;
		this.backgroundColor = color;
		
		setBackground(backgroundColor);

		border = new JPanel();
		border.setBackground(CustomColors.faded_lighter_blue);
		border.setBorder(new EmptyBorder(2,2,2,2));
		border.setLayout(new BoxLayout(border, BoxLayout.Y_AXIS));
		
		yaxisContent = new JPanel();
		yaxisContent.setBackground(backgroundColor);
		yaxisContent.setLayout(new BoxLayout(yaxisContent, BoxLayout.Y_AXIS));
		
		content = new JPanel();
		content.setBackground(backgroundColor);
		content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
		content.setBorder(new EmptyBorder(4,4,4,4));
		
		doseWrapper = new JPanel();
		doseWrapper.setBackground(CustomColors.orange);
		int tw = maxWidth/8;
		doseWrapper.setLayout(new GridBagLayout());
		doseWrapper.setMaximumSize(new Dimension(tw, maxHeight));
		doseWrapper.setMinimumSize(new Dimension(tw, maxHeight));
		doseWrapper.setPreferredSize(new Dimension(tw, maxHeight));
		
		JLabel dose = new JLabel();
		dose.setText("Dose "+doseNoStr);
		dose.setForeground(backgroundColor);
		dose.setHorizontalAlignment(JLabel.CENTER);
		dose.setFont(new Font(getFont().getFontName(), Font.BOLD, getFont().getSize()));
		doseWrapper.add(dose);	
		
		statusPanel = new JPanel(); //(Integer.toString(statusInt));
		statusPanel.setBackground(backgroundColor);
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusPanel.setPreferredSize(new Dimension(maxWidth/24, maxHeight));
		statusPanel.setMinimumSize(statusPanel.getPreferredSize());
		
		yaxisContent.add(content);
		
		border.add(yaxisContent);
		this.add(border);
		
		if (statusInt == -1) {
			makeNoAppointment();
		}
		
		else if (statusInt == 0) {
			makeBookedAppointment();
		}
		
		else if (statusInt == 1) {
			makeCompletedAppointment();
		}
	}
	
	void addNoAppointmentComps() {
		content.add(doseWrapper);
		content.add(statusPanel);
	}
	
	void addBookedAppointmentComps() {
		content.add(doseWrapper);
		content.add(dataPanel);
		content.add(statusPanel);
	}
	
	void addCompletedAppointmentComps() {
		content.add(doseWrapper);
		content.add(dataPanel);
		content.add(statusPanel);
	}
	
	void makeNoAppointment () {
		makeAddOption();
		addNoAppointmentComps();
	}
	
	void makeBookedAppointment () {
		setAppointmentData();
		addBookedAppointmentComps();
		makeCancelOption();
	}
	
	void makeCompletedAppointment () {
		setAppointmentData();
		makeCompletedStatus();
		addCompletedAppointmentComps();
	}
	
	void makeCancelOption () {
		
		Icon icon = new ImageIcon(System.getProperty("user.dir")+"/images/cancel.png");
		JButton status = new JButton (icon);
		status.setBorder(new EmptyBorder(0,0,0,0));
		status.setPreferredSize(new Dimension(
				statusPanel.getPreferredSize().width-4,
				statusPanel.getPreferredSize().height));
		status.setBackground(backgroundColor);
		status.setForeground(CustomColors.dark_blue);
		status.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				content.remove(dataPanel);
				content.remove(statusPanel);
				content.add(confirmationPanel);
				invalidate();
				revalidate();
				repaint();
			}
			
		});
		
		statusPanel.add(Box.createGlue());
		statusPanel.add(new Separator());
		
		statusPanel.add(status);
		
		confirmationPanel = new JPanel ();
		confirmationPanel.setBackground(backgroundColor);
		confirmationPanel.setPreferredSize(new Dimension(
				dataPanel.getPreferredSize().width
				+statusPanel.getPreferredSize().width, 30));
		
		
		confirmationPanel.add(Box.createGlue());
		
		JLabel confirmationMessage = new JLabel("Are you sure you want to cancel your appointment?");
		confirmationPanel.add(confirmationMessage);
		
		CustomButton yesButton = new CustomButton("Yes", Color.white, CustomColors.orange);
		CustomButton noButton = new CustomButton("No", CustomColors.orange, Color.white);
		
		confirmationPanel.add(noButton);
		confirmationPanel.add(yesButton);
		confirmationPanel.add(Box.createGlue());
		
		noButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				content.remove(confirmationPanel);
				content.add(dataPanel);
				content.add(statusPanel);
				revalidate();
				repaint();
			}
			
		});
		
		yesButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clear();
				statusInt = -1;
				makeAddOption();
				addNoAppointmentComps();
				revalidate();
				repaint();
				
			}
			
		});
		
	}
	
	void makeCompletedStatus() {
		
		statusPanel.add(Box.createGlue());
		content.add(statusPanel);

		Icon icon = new ImageIcon(System.getProperty("user.dir")+"/images/tic.png");
		JLabel status = new JLabel (icon);
		status.setBorder(new EmptyBorder(0,0,0,0));
		status.setHorizontalAlignment(JLabel.CENTER);
		status.setPreferredSize(statusPanel.getPreferredSize());
		statusPanel.add(Box.createGlue());
		statusPanel.add(new Separator());
		statusPanel.add(status);
	}
	
	void makeAddOption () {
		
		Icon icon = new ImageIcon(System.getProperty("user.dir")+"/images/plus.png");
		JButton status = new JButton (icon);
		status.setBorder(new EmptyBorder(0,0,0,0));
		status.setBackground(backgroundColor);
		status.setPreferredSize(new Dimension(
				statusPanel.getPreferredSize().width-4,
				statusPanel.getPreferredSize().height));
		status.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				makeMedicalCenterCombo();
				statusPanel.removeAll();
				makeAbortStatus();
				yaxisContent.add(Box.createRigidArea(new Dimension(
						maxWidth, 5)));
				yaxisContent.add(medicalCenters);
				yaxisContent.add(Box.createRigidArea(new Dimension(
						maxWidth, 10)));
				revalidate();
				repaint();
			}
		});
		
		statusPanel.add(Box.createGlue());
		statusPanel.add(new Separator());
		statusPanel.add(status);
	}
	
	void makeAbortConfirmationPanel () {
		abortConfirmationPanel = new JPanel();
		abortConfirmationPanel.setBackground(backgroundColor);
		abortConfirmationPanel.setLayout(
				new BoxLayout(abortConfirmationPanel, BoxLayout.X_AXIS));
		
		JLabel abortConfirmationMessage = new JLabel(
				"Delete choices?");
		CustomButton yesButton = new CustomButton("yes", backgroundColor, CustomColors.orange);
		CustomButton noButton = new CustomButton("no", CustomColors.orange, backgroundColor);
		
		noButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				content.remove(abortConfirmationPanel);
				content.add(statusPanel);
				
				revalidate();
				repaint();
			}
			
		});
		
		yesButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				yaxisContent.removeAll();
				yaxisContent.add(content);
				
				content.removeAll();
				statusPanel.removeAll();


				makeNoAppointment();
				revalidate();
				repaint();
			}
			
		});
		
		abortConfirmationPanel.setPreferredSize(new Dimension(
				maxWidth - doseWrapper.getWidth(),
				maxHeight));
		
		abortConfirmationPanel.add(Box.createGlue());
		abortConfirmationPanel.add(abortConfirmationMessage);
		abortConfirmationPanel.add(Box.createRigidArea(new Dimension(10,0)));
		abortConfirmationPanel.add(noButton);
		abortConfirmationPanel.add(Box.createRigidArea(new Dimension(10,0)));
		abortConfirmationPanel.add(yesButton);
		abortConfirmationPanel.add(Box.createGlue());
	}
	
	void makeAbortStatus () {
		
		makeAbortConfirmationPanel();
		
		statusPanel.add(Box.createGlue());
		statusPanel.add(new Separator());
		
		Icon icon = new ImageIcon(System.getProperty("user.dir")+"/images/x.png");
		JButton status = new JButton(icon);
		status.setBorder(new EmptyBorder(0,0,0,0));
		status.setBackground(backgroundColor);
		status.setPreferredSize(new Dimension(
				statusPanel.getPreferredSize().width-4,
				statusPanel.getPreferredSize().height));
		status.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				content.remove(statusPanel);
				content.add(Box.createGlue());
				content.add(abortConfirmationPanel);
				content.add(Box.createGlue());
				revalidate();
				repaint();
			}
			
		});
		
		statusPanel.add(status);
	}
	
	void makeMedicalCenterCombo () {
		loadMedicalCenters();
		medicalCenters = new JComboBox<>();
		medicalCenters.setBorder(BorderFactory.createLineBorder(backgroundColor, 5));
		medicalCenters.setModel(
				new DefaultComboBoxModel<MedicalCenter>(medicalCenterPairs));
		medicalCenters.setPreferredSize(new Dimension(
				maxWidth/2, maxHeight+10));
		medicalCenters.setMaximumSize(medicalCenters.getPreferredSize());
		medicalCenters.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() != ItemEvent.SELECTED)
					return;
				
				MedicalCenter selected = (MedicalCenter) medicalCenters.getSelectedItem();
				//request medical center's data
				makeCalendar(); 
				calendar.setWidth(maxWidth-100);
				yaxisContent.add(calendar);
				revalidate();
				repaint();
			}
			
		});
		
		medicalCenters.setSelectedIndex(0);
	}
	
	void loadMedicalCenters () {
		medicalCenterPairs = new MedicalCenter[] {
				new MedicalCenter(-1, "-- Select medical center", " vaccine"),
				new MedicalCenter(0, "Medical Center of Chania", "Moderna"),
				new MedicalCenter(1, "Princeton Plainsboro Hospital", "Pfizer")
		};
	}
	
	void makeCalendar () {
		calendar = new Calendar(backgroundColor);
		
		JPanel selectedPanel = new JPanel();
		selectedPanel.setBackground(backgroundColor);
		selectedPanel.setLayout(new BoxLayout(
				selectedPanel, BoxLayout.X_AXIS));
		selectedPanel.setBorder(
				BorderFactory.createLineBorder(backgroundColor, 10));
		
		JLabel selectedLabel = new JLabel();
		
		String med;
		String vacc;
		String date;
		String day;
		
		CustomButton confirm = new CustomButton("Confirm", CustomColors.dark_blue, Color.white);
		
		confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//update database
				clear();
				makeBookedAppointment();
				revalidate();
				repaint();
			}
			
		});
		
		selectedPanel.add(Box.createGlue());
		selectedPanel.add(selectedLabel);
		selectedPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		selectedPanel.add(confirm);
		selectedPanel.add(Box.createGlue());
		
		
		for (TimeslotColumn column : calendar.timeslotColumn) {
			for (TimeslotEntry entry : column.timeslots) {
				entry.addActionListener(new ActionListener () {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
						MedicalCenter selectedCenter = (MedicalCenter) medicalCenters.getSelectedItem();
						
						medicalCenterStr = selectedCenter.name; 
						vaccineNameStr = selectedCenter.vaccine;
						dateStr = 
								calendar.dayPanel[column.id].day.getText() + ", "+
								calendar.dayPanel[column.id].date.getText();						
						
						selectedLabel.setText(
								medicalCenterStr + 
								", "+
								dateStr+
								", "+
								entry.getText()
								);
						
						yaxisContent.add(selectedPanel);
						revalidate();
						repaint();
					}
					
				});
			}
		}		
		
	}
	
	void setAppointmentData () {
		int rmw = maxWidth;
		int rw = rmw;
		int tw;
		
		rw-=8;
		
		rw -= 2*3;
		rmw = rw;
		
		
		//tw = rmw/8;
		rw -= doseWrapper.getPreferredSize().width;
		rw -= statusPanel.getPreferredSize().width;
		rmw = rw;
		
		dataPanel = new JPanel();
		dataPanel.setBackground(backgroundColor);
		dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.X_AXIS));
		dataPanel.setPreferredSize(new Dimension (rw, maxHeight));
		//rw -=
		
		//dataPanel.add(Box.createGlue());
		
		//nedical center
		JLabel medicalCenter = new JLabel(medicalCenterStr);
		medicalCenter.setBackground(backgroundColor);
		medicalCenter.setHorizontalAlignment(SwingConstants.CENTER);
		//tw = rmw/3;
		tw = rmw/3;
		medicalCenter.setPreferredSize(new Dimension(tw, maxHeight));
		
		dataPanel.add(medicalCenter);//content.add(medicalCenter);
		dataPanel.add(new Separator());//content.add(new Separator());
		
		JLabel vaccineName = new JLabel(vaccineNameStr);
		vaccineName.setBackground(backgroundColor);
		vaccineName.setHorizontalAlignment(SwingConstants.CENTER);
		tw = rmw/4;
		vaccineName.setPreferredSize(new Dimension(tw, maxHeight));
		
		dataPanel.add(vaccineName); //content.add(vaccineName);
		dataPanel.add(new Separator());//content.add(new Separator());
		
		//Date
		JLabel date = new JLabel (dateStr);
		date.setBackground(backgroundColor);
		date.setHorizontalAlignment(SwingConstants.CENTER);
		//tw = rmw/3;
		tw = 5*rmw/12;
		date.setPreferredSize(new Dimension(tw, maxHeight));
		date.setMaximumSize(date.getPreferredSize());
		date.setMinimumSize(date.getPreferredSize());
		
		dataPanel.add(date); //content.add(date);
		
	}
	
	void clear () {
		yaxisContent.removeAll();
		yaxisContent.add(content);
		content.removeAll();
		statusPanel.removeAll();
	}
	
	class Separator extends JLabel {
		
		Separator () {
			super(new ImageIcon(System.getProperty("user.dir")+"/images/separator.png"));
			this.setFont(new Font (getFont().getFontName(), Font.PLAIN, 18));
			this.setVerticalAlignment(JLabel.TOP);
		}
	}
	
	
}
