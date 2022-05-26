package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.KeyPair;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Controller.Appointment;
import Controller.MedicalCenter;
import Controller.db;
import View.Calendar.TimeslotColumn;
import View.Calendar.TimeslotEntry;
import View.GraphicsComponents.CustomButton;
import View.GraphicsComponents.TextField;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class AppointmentEntry extends JPanel {
	
	Color backgroundColor;
	
	MedicalCenter [] medicalCenterPairs;
	JComboBox<MedicalCenter> medicalCenters;
	
	//ArrayList<Timeslot> timeslotList;
	//JPanel timeslots;
	JPanel calendarWrapper;
	Calendar calendar;

	Appointment appointment;
	CitizenUser citizen;
	
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
	JPanel appointmentConfirmationPanel;
	
	CustomButton confirmAppointmentButton;
	
	ActionListener reloadAppointments;
	
	AppointmentEntry (
			CitizenUser citizen,
			Appointment appointment,
			String postalCode,
			int width,
			Color color,
			ActionListener l) {
		System.out.println("AppointmentEntry constructor");
		this.citizen = citizen;
		/*
			String SSN, 
			String medicalCenterID, 
			String vaccineName,
			String doseNumber,
			String medicalCenterName,
			String date,
			String time
		*/
		System.out.println("AppointmentEntry constructor: added citizen");
		this.appointment = appointment;

		if(this.appointment != null)
		System.out.println("AppointmentEntry: adding appointments to appointmentTabContent");
		
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
		dose.setText("Dose " + Integer.toString(this.appointment.getDoseNumber()));
		
		dose.setForeground(backgroundColor);
		dose.setHorizontalAlignment(JLabel.CENTER);
		dose.setFont(new Font(getFont().getFontName(), Font.BOLD, getFont().getSize()));
		doseWrapper.add(dose);	
		
		statusPanel = new JPanel(); //(Integer.toString(statusInt));
		statusPanel.setBackground(backgroundColor);
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusPanel.setPreferredSize(new Dimension(maxWidth/24, maxHeight));
		statusPanel.setMinimumSize(statusPanel.getPreferredSize());
		
		makeAppointment();
		yaxisContent.add(content);
		
		border.add(yaxisContent);
		this.add(border);
		
		this.reloadAppointments = l;
		
		confirmAppointmentButton = new CustomButton("Confirm", CustomColors.dark_blue, Color.white);
	}
	
	void makeAppointment() {
		
		switch (appointment.getStatus()) {
		case -1 :
			makeNoAppointment();
			break;
		
		case 0:
			makeBookedAppointment();
			break;
			
		case 1:
			makeCompletedAppointment();
			break;
			
		default:
			makeNoAppointment();
		}
	}
	
	void addAppointmentComps() {
		switch (appointment.getStatus()) {
		case -1 :
			addNoAppointmentComps();
			break;
		
		case 0:
			addBookedAppointmentComps();
			break;
			
		case 1:
			addCompletedAppointmentComps();
			break;
			
		default:
			makeNoAppointment();
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
		
		final JPanel cancellationFailurePanel = new JPanel();
		cancellationFailurePanel.setLayout(
				new BoxLayout(
						cancellationFailurePanel, BoxLayout.X_AXIS));
		cancellationFailurePanel.setPreferredSize(
				new Dimension(
							dataPanel.getPreferredSize().width
							+statusPanel.getPreferredSize().width, 30)
						);
		
		JLabel cancellationFailureLabel = new JLabel();
		CustomButton okButton = new CustomButton("OK", CustomColors.dark_blue, Color.white);
		
		cancellationFailurePanel.add(Box.createGlue());
		cancellationFailurePanel.add(cancellationFailureLabel);
		cancellationFailurePanel.add(Box.createGlue());
		cancellationFailurePanel.add(okButton);
		cancellationFailurePanel.add(Box.createGlue());
		
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
				//TODO request appointment cancellation from Database
			
				content.remove(dataPanel);
				content.remove(statusPanel);
				content.add(confirmationPanel);
				invalidate();
				revalidate();
				repaint();
			}
			
		});
		
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				content.remove(cancellationFailurePanel);
				addAppointmentComps();
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
				/*
				 */
				content.remove(confirmationPanel);
				content.add(dataPanel);
				content.add(statusPanel);
				//addAppointmentComps();
				revalidate();
				repaint();
			}
			
		});
		
		yesButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean cancellationSuccess = true;

				clear();
				//if (cancellationSuccess) {
					
					
					try {
						int doseno = appointment.getDoseNumber();
						appointment = new Appointment(
								appointment.getSSN(),
								doseno);
						
						db database = new db();
						database.init();
						
						database.CancelAppointment(citizen.citizen.getSSN(), doseno);
						
						database.con.close();
						
						makeAppointment();
						revalidate();
						repaint();
						
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						JOptionPane.showMessageDialog(new JFrame(), e1.toString(), "Error", JOptionPane.WARNING_MESSAGE);
						addAppointmentComps();
					}
					
				//}
			//	else {
					
			//	}
				
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
				try {
					makeMedicalCenterCombo();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
	
	void makeMedicalCenterCombo () throws SQLException {
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
				if (calendar!=null)
					yaxisContent.remove(calendarWrapper);
				
				calendarWrapper = new JPanel();
				calendarWrapper.setBackground(backgroundColor);
				calendarWrapper.setLayout(new BoxLayout(calendarWrapper, BoxLayout.Y_AXIS));
				
				makeCalendar(selected); 
				calendar.setWidth(maxWidth-100);

				calendarWrapper.add(calendar);
				yaxisContent.add(calendarWrapper);
				
				revalidate();
				repaint();
			}
			
		});
		
		medicalCenters.setSelectedIndex(0);
	}
	
	void loadMedicalCenters () throws SQLException {
		
		//TODO request medical centers by postal code
/*		medicalCenterPairs = new MedicalCenter[] {
				new MedicalCenter("-1", "-- Select medical center", " vaccine"),
				new MedicalCenter("12", "Medical Center of Chania", "Moderna"),
				new MedicalCenter("21", "Princeton Plainsboro Hospital", "Pfizer")
		};
*/		System.out.println("Load Medical Centers");
		db database = new db();
		database.init();
		String postalCode = this.citizen.citizen.getPostalCode();
		System.out.println("Hospitals near: " + postalCode);
		
		ArrayList<String> hospitals = database.getNearbyHospitals(postalCode);
		System.out.println("Found " + hospitals.size());
		if(hospitals.size() > 0) this.medicalCenterPairs = new MedicalCenter[hospitals.size()];
	    for (int i = 0; i < hospitals.size(); i++){ 		      
	    	  String medicalCenterName = hospitals.get(i);
	    	  String medicalCenterId = database.getHospitalID(medicalCenterName);
	    	  String vaccine = database.getHospitalVaccine(medicalCenterId);
	    	  
	    	  this.medicalCenterPairs[i] = new MedicalCenter(medicalCenterId, medicalCenterName, vaccine);
	    	  System.out.println("Added " + medicalCenterName + " " + medicalCenterId + " " + vaccine);
	      
	      }
		
		database.con.close();
	}
	
	void makeCalendar (MedicalCenter selected) {
		calendar = new Calendar(selected, backgroundColor);
		
		final JPanel selectedPanel = new JPanel();
		selectedPanel.setBackground(backgroundColor);
		selectedPanel.setLayout(new BoxLayout(
				selectedPanel, BoxLayout.X_AXIS));
		selectedPanel.setBorder(
				BorderFactory.createLineBorder(backgroundColor, 10));
		
		final JLabel selectedLabel = new JLabel();
		
		String med;
		String vacc;
		String date;
		String day;
				
		selectedPanel.add(Box.createGlue());
		selectedPanel.add(selectedLabel);
		selectedPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		selectedPanel.add(confirmAppointmentButton);
		selectedPanel.add(Box.createGlue());
		
		confirmAppointmentButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				clear();
				content.add(doseWrapper);
				content.add(Box.createGlue());
				makeAppointmentConfirmation();
				yaxisContent.add(Box.createRigidArea(
							new Dimension(maxWidth, 10)
						));
				yaxisContent.add(appointmentConfirmationPanel);
				revalidate();
				repaint();
				
				// TODO update database
				//database.bookappointment(this.appointment);
			}
			
		});
		//confirmAppointmentButton.addActionListener(reloadAppointments);
		
		for (final TimeslotColumn column : calendar.timeslotColumn) {
			if (column.timeslots == null) {continue;}
			for (final TimeslotEntry entry : column.timeslots) {
				entry.addActionListener(new ActionListener () {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
						MedicalCenter selectedCenter = (MedicalCenter) medicalCenters.getSelectedItem();
						String day = calendar.dayPanel[column.id].day.getText();
						String date = calendar.dayPanel[column.id].date.getText();
						String time = entry.getText();
						/*
								String SSN, 
								String medicalCenterID, 
								String vaccineName,
								String doseNumber,
								String medicalCenterName,
								String date,
								String time
						*/
						
						String vaccinationState = "";
						int status = -1;
						
						try {
							db database = new db();
							database.init();
							vaccinationState = database.getVaccinationState(citizen.citizen.getSSN());
							
							System.out.println("SSN: " + citizen.citizen.getSSN() + "   STATE: " + vaccinationState);
							
							database.con.close();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						if(vaccinationState == "fully vaccinated") {
							status = 1;
						} System.out.println("       STATUS: " + status);
							
						
						appointment = new Appointment(
									appointment.getSSN(),
									selectedCenter.getID().toString(),
									selectedCenter.getName(),
									selectedCenter.getVaccine(),
									appointment.getDoseNumber(),
									//"Medical center", //TODO get medical center name from id
									date,
									time,
									status
								);
						
						selectedLabel.setText(
									selectedCenter.getName() + ", "+
									day+", "+
									date+", "+
									time
								);
						
						calendarWrapper.add(selectedPanel);
						revalidate();
						repaint();
					}
					
				});
			}
		}		
		
	}
	
	void makeAppointmentConfirmation () {
		
		appointmentConfirmationPanel = new JPanel();
		appointmentConfirmationPanel.setBackground(backgroundColor);
		appointmentConfirmationPanel.setLayout(
				new BoxLayout(appointmentConfirmationPanel, BoxLayout.Y_AXIS));
		appointmentConfirmationPanel.setPreferredSize(
				new Dimension(maxHeight, 100));
		
		final JLabel timerLabel = new JLabel ();
		timerLabel.setBackground(backgroundColor);
		timerLabel.setBorder(BorderFactory.createLineBorder(CustomColors.red, 3));
		timerLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		JLabel instructions = new JLabel ();
		instructions.setText("Enter the 4-digit code you received by e-mail (or a pop-up)");
		instructions.setAlignmentX(CENTER_ALIGNMENT);
		
		final TextField codeField = new TextField();
		codeField.setPlaceholder("x x x x");
		codeField.setPreferredSize(new Dimension(70, 30));
		codeField.setBorder(BorderFactory.createLineBorder(CustomColors.dark_blue, 3));
		codeField.setMaximumSize(codeField.getPreferredSize());
		codeField.setHorizontalAlignment(SwingConstants.CENTER);
		codeField.setAlignmentX(CENTER_ALIGNMENT);

		final String code = String.format("%04d", new Random().nextInt(10000));
		
		final Timer timer = new Timer ("Timer");
		
		TimerTask countdown = new TimerTask() {
			
			int minutes = 2;
			int seconds = 0;
			DecimalFormat dFormat = new DecimalFormat("00");
			
			public void run () {
				timerLabel.setText(getMinutes()+":"+getSeconds());
				
				if(seconds==0) {
					if (minutes==0) {
						codeField.setEditable(false);
						for (FocusListener l : codeField.getFocusListeners())
							codeField.removeFocusListener(l);
						cancel();
						timer.cancel(); 
						timer.purge();
						
						makeConfirmationFailure();
						
						return;
					}
					minutes--;
					seconds=59;
					return;
				}
				seconds--;
			}
			
			String getMinutes() {
				return dFormat.format(minutes);
			}
			
			String getSeconds() {
				return dFormat.format(seconds);
			}
		};
		
		codeField.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
				  checkInput();
			  }
			  public void removeUpdate(DocumentEvent e) {
				  checkInput();
			  }
			  public void insertUpdate(DocumentEvent e) {
				  checkInput();
			  }
			  
			  public void checkInput () {
				  if (codeField.getText().equals(code)) {
					  //TODO
					  //Confirmation successful!
					  //update database with the appointment this.appointment
					  
					  try {
						 db database = new db();
						 database.init();
						 
						 database.bookAppointment(appointment.getDoseNumber(), appointment.getSSN(), 
								 appointment.getMedicalCenterID(), appointment.getDate(), appointment.getTime(), appointment.getVaccineName());
						 
						 database.con.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					  
					  System.out.println("okay");
					  citizen.reloadAppointments();
					  return;
				  }
			  }
		});
		
		appointmentConfirmationPanel.add(Box.createGlue());
		appointmentConfirmationPanel.add(timerLabel);
		appointmentConfirmationPanel.add(Box.createGlue());
		appointmentConfirmationPanel.add(instructions);
		appointmentConfirmationPanel.add(Box.createGlue());
		appointmentConfirmationPanel.add(codeField);
		appointmentConfirmationPanel.add(Box.createGlue());
		
		long delay = 1L;
		timer.scheduleAtFixedRate(countdown, 0, 1000L);
		
		revalidate();
		repaint();
		
		JDialog dialog = new JOptionPane(code).createDialog("Confirmation Code");
		//dialog.add(new JLabel(code));
		dialog.setModal(false);
		dialog.show();
	}
	
	void makeConfirmationFailure () {
		
		JLabel failureLabel = new JLabel();
		failureLabel.setText("The appointment was not confirmed and the timeslot has been released.");
		failureLabel.setAlignmentX(CENTER_ALIGNMENT);
		failureLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		CustomButton okButton = new CustomButton("OK", CustomColors.dark_blue, Color.white);
		okButton.setAlignmentX(CENTER_ALIGNMENT);
		
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clear();
				citizen.reloadAppointments();
				makeAppointment();
			}
			
		});
		
		yaxisContent.add(failureLabel);
		yaxisContent.add(okButton);
		yaxisContent.add(Box.createRigidArea(
				new Dimension(0,20)));
		
		revalidate();
		repaint();
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
		JLabel medicalCenter = new JLabel(appointment.getMedicalCenterID());
		medicalCenter.setBackground(backgroundColor);
		medicalCenter.setHorizontalAlignment(SwingConstants.CENTER);
		//tw = rmw/3;
		tw = rmw/3;
		medicalCenter.setPreferredSize(new Dimension(tw, maxHeight));
		
		dataPanel.add(medicalCenter);//content.add(medicalCenter);
		dataPanel.add(new Separator());//content.add(new Separator());
		
		JLabel vaccineName = new JLabel(appointment.getVaccineName());
		vaccineName.setBackground(backgroundColor);
		vaccineName.setHorizontalAlignment(SwingConstants.CENTER);
		tw = rmw/4;
		vaccineName.setPreferredSize(new Dimension(tw, maxHeight));
		
		dataPanel.add(vaccineName); //content.add(vaccineName);
		dataPanel.add(new Separator());//content.add(new Separator());
		
		//Date
		JLabel date = new JLabel (appointment.getDate()+", "+appointment.getTime());
		date.setBackground(backgroundColor);
		date.setHorizontalAlignment(SwingConstants.CENTER);
		//tw = rmw/3;
		tw = 5*rmw/12;
		date.setPreferredSize(new Dimension(tw, maxHeight));
		date.setMaximumSize(date.getPreferredSize());
		date.setMinimumSize(date.getPreferredSize());
		
		dataPanel.add(date); //content.add(date);
		dataPanel.repaint();
	}
	
	public void confirmAppointmentBooking () {
		
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
