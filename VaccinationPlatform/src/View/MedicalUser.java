package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import Controller.MedicalStaff;
import Controller.db;
import View.GraphicsComponents.TextField;


public class MedicalUser extends User {

	MedicalStaff medicalStaff;
	
	AccountTab accountTab;
	AppointmentTab appointmentTab;

	MedicalUser (
			VaccinationPlatformGUI frame, 
			String employeeID, 
			String employerID) 
	{
		super(frame);
		medicalStaff = new MedicalStaff(employeeID, employerID);
		//loadAccountInfo();
		//loadOptions();
	}
	
	void updateAccountInfo () {
		//TODO get info
		setAccountName(medicalStaff.getFirstName(), medicalStaff.getLastName());
		accountTab.dataPanel.updateData(medicalStaff.getArray());
	}

	void setUp() {
		try {
			medicalStaff.loadData();
			accountTab = new AccountTab();
			appointmentTab = new AppointmentTab();
			updateAccountInfo();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	void reload() {
		try {
			medicalStaff.loadData();
			updateAccountInfo();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	class AccountTab {
		DataPanel dataPanel;
		
		String[] dataTitles = new String[] {
				"Medical Center ID",
				"Employee ID",
				"First Name",
				"Last name"
		};
		
		AccountTab () {
			createOptionAndPage("Account");
			selectOption(0);
			dataPanel = new DataPanel (
					dataTitles,
					panelColor, 
					backgroundColor);
			mainPanel.addToContentPanel(0, dataPanel);
		}
	}
	
	class AppointmentTab {
		String lastSearch;
		
		JPanel searchPanel;
		DataTable table;
		
		AppointmentTab () {
			createOptionAndPage("Appointments");
			
			createSearchBar();
			createTable();
			
			JPanel tmp = new JPanel();
			tmp.setBackground(backgroundColor);
			tmp.setLayout(new BoxLayout(tmp, BoxLayout.Y_AXIS));
			tmp.add(searchPanel);
			tmp.add(table);
			
			mainPanel.addToContentPanel(1, tmp);
			table.updateViewportSize();
			
			mainPanel.revalidate();
			mainPanel.repaint();
		}
		
		JPanel createSearchBar() {
			lastSearch = "";
			
			searchPanel = new JPanel();
			searchPanel.setBackground(panelColor);
			searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
			
			JLabel label = new JLabel();
			label.setText("Search by ");
			label.setBorder(BorderFactory.createEmptyBorder(0,20,0,10));
			
			String [] searchModes = {"SSN", "Date"};
			final JComboBox <String> searchMode = new JComboBox <String> (new DefaultComboBoxModel<String>(searchModes));
			searchMode.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
			
			
			final TextField search = new TextField();
			search.setPlaceholder("");
			search.setBorder(
					BorderFactory.createCompoundBorder(
							BorderFactory.createMatteBorder(0, 30, 0, 0, panelColor),
							BorderFactory.createMatteBorder(2,6,2,6, backgroundColor)
							));
			
			
			final JButton go = new JButton("go");
			go.setBackground(backgroundColor);
			
			final JButton cancel = new JButton ("cancel");
			cancel.setBackground(backgroundColor);
			
			go.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
			cancel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
			searchPanel.setBorder(BorderFactory.createEmptyBorder(15,0,10,30));
			
			
			search.getDocument().addDocumentListener(new DocumentListener() {
				  public void changedUpdate(DocumentEvent e) {
					  updateButtons();
				  }
				  public void removeUpdate(DocumentEvent e) {
					  updateButtons();
				  }
				  public void insertUpdate(DocumentEvent e) {
					  updateButtons();
				  }
				  
				  public void updateButtons () {
					  if (search.getText().equals("")) {
						  go.setVisible(false);
						  return;
					  }
					  
					  if (!lastSearch.equals("")) {
							cancel.setVisible(true); 
					  }
					  
					  if (lastSearch.equals(search.getText())) {
						  go.setVisible(false);
					  }
					  else go.setVisible(true);
				  }
				});
			
			searchMode.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String s = (String) searchMode.getSelectedItem();
					
					switch(s) {
					case "SSN" :
						search.setPlaceholder("e.g. 18010112345");
						break;
					case "Date" :
						search.setPlaceholder("e.g. 18-01-2001");
						break;
					}
				}
				
			});
			
			go.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					
					String s = (String) searchMode.getSelectedItem();
					String target = search.getText();
					cancel.setVisible(true);
					go.setVisible(false);
					lastSearch = target;
					Object[][] appointment = null;
					
					switch(s) {
					case "SSN":
						System.out.println("Searching for appointment with SSN: " + target);
						String ssn = target;
						//lookup database by ssn
						
						try {
							db database = new db();
							database.init();
							
							ArrayList<String[]> appointmentsBySSN = database.GetAppointmentsBySSN(medicalStaff.getMedicalCenterID(), ssn);

							if(appointmentsBySSN.size()>0)
							db.PrintArrayList(appointmentsBySSN);
							
							appointment = new Object[appointmentsBySSN.size()-1][6];
							for(int i=1 ; i<appointmentsBySSN.size() ; i++) {
								String [] appt = appointmentsBySSN.get(i);
								
								appointment[i-1][0] = appt[0];
								appointment[i-1][1] = appt[3];
								appointment[i-1][2] = appt[1] + " " + appt[2];
								appointment[i-1][3] = appt[4];
								appointment[i-1][4] = appt[5];
								appointment[i-1][5] = appt[7];
								
								//Object[] appointment = {appt[0], appt[3], appt[1] + " " + appt[2], appt[4], appt[5], false};
								//table.addRow(appointment);
								
							}
							
							table.loadArray(appointment);
							table.updateViewportSize();
							table.revalidate();
							table.repaint();
							
							database.con.close();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						break;
					case "Date":
						//lookup database by date
						
						System.out.println("Searching for appointment with SSN: " + target);
						String date = target;
					
						try {
							db database = new db();
							database.init();
							
							ArrayList<String[]> appointmentsByDate = database.GetDailyAppointments(medicalStaff.getMedicalCenterID(), date);

							if(appointmentsByDate.size()>0)
							db.PrintArrayList(appointmentsByDate);
							
							appointment = new Object[appointmentsByDate.size()-1][6];
							for(int i=1 ; i<appointmentsByDate.size() ; i++) {
								String [] appt = appointmentsByDate.get(i);
								
								appointment[i-1][0] = appt[0];
								appointment[i-1][1] = appt[3];
								appointment[i-1][2] = appt[1] + " " + appt[2];
								appointment[i-1][3] = appt[4];
								appointment[i-1][4] = appt[5];
								appointment[i-1][5] = false;
								
							}
							
							table.loadArray(appointment);
							table.updateViewportSize();
							table.revalidate();
							table.repaint();
							
							database.con.close();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						break;
					}
				}
				
			});
			
			cancel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					//reload all data, undo search
					cancel.setVisible(false);
					search.setText("");
					lastSearch = "";
				}
				
			});
			
			
			searchPanel.add(Box.createGlue());
			searchPanel.add(label);
			searchPanel.add(searchMode);
			searchPanel.add(Box.createGlue());
			searchPanel.add(search);
			searchPanel.add(cancel);
			searchPanel.add(go);
			searchPanel.add(Box.createGlue());
			
			go.setVisible(false);
			cancel.setVisible(false);
			
			searchMode.setSelectedIndex(0);
			
			return searchPanel;
		}
		
		DataTable createTable() {
			//JTable table = new JTable(model);
			table = new DataTable(backgroundColor, panelColor);
			
			//request all appointments of the medical center
			
			try {
				db database = new db();
				database.init();
				
				ArrayList<String[]> dailyAppointments = database.GetDailyAppointments(medicalStaff.getMedicalCenterID(), "2022/05/25");
				System.out.println("Appointments for " + medicalStaff.getMedicalCenterID() + "(" + dailyAppointments.size() + ")");
				
				if(dailyAppointments.size()>0)
				db.PrintArrayList(dailyAppointments);
				
				Object[][] appointment = new Object[dailyAppointments.size()-1][6];
				
				for(int i=1 ; i<dailyAppointments.size() ; i++) {
						String [] appt = dailyAppointments.get(i);
						
						appointment[i-1][0] = appt[0];
						appointment[i-1][1] = appt[3];
						appointment[i-1][2] = appt[1] + " " + appt[2];
						appointment[i-1][3] = appt[4];
						appointment[i-1][4] = appt[5];
						appointment[i-1][5] = appt[7];
						
				}
					table.loadArray(appointment);
					table.updateViewportSize();
					table.revalidate();
					table.repaint();
				
				database.con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return table;
		}
	}
}
