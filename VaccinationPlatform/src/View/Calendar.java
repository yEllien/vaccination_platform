package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import Controller.MedicalCenter;
import Controller.db;

enum Availability {
	UNAVAILABLE,
	LIMITED,
	AVAILABLE
}

class Timeslot {
	//int id;
	String date;
	String value;
	int capacity;
	//int appointments;
	
	Timeslot (/*int id,*/String date, String value, int capacity /*, int appointments*/) {
		//this.id = id;
		this.date = date;
		this.value = value;
		this.capacity = capacity;
		//this.appointments = appointments;
	}
	
	Availability getAvailability () {
		
		if (capacity == 0)
			return Availability.UNAVAILABLE;
		
		//float ratio = (float) appointments/capacity;
		float ratio = (float) (1-capacity)/6;
		//System.out.println(ratio);
		
		if (ratio == 1)
			return Availability.UNAVAILABLE;
		
		if (ratio > 0.8)
			return Availability.LIMITED;
		
		return Availability.AVAILABLE;
	}
}

public class Calendar extends JPanel{
	
	MedicalCenter medicalCenter;
	
	JPanel datePanel;
	JPanel timeslotPanel;
	
	DayPanel[] dayPanel;
	TimeslotColumn[] timeslotColumn;
	
	Color borderColor = CustomColors.gray;
	Color background;
	

	Color[] availabilityColors = new Color [] {
			CustomColors.light_gray,
			CustomColors.red,
			CustomColors.green
	};

	
	Calendar (MedicalCenter medicalCenter, Color background) {
		
		this.medicalCenter = medicalCenter;
		
		this.background = background;
		this.setBackground(background);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		Border border = BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(borderColor),
				BorderFactory.createEmptyBorder(10,10,10,10)
			);
		
		datePanel = new JPanel();
		datePanel.setBackground(background);
		datePanel.setBorder(border);
		datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.X_AXIS));
		this.add(datePanel);
		
		timeslotPanel = new JPanel();
		timeslotPanel.setBackground(background);
		timeslotPanel.setBorder(border);
		timeslotPanel.setLayout(new BoxLayout(timeslotPanel, BoxLayout.X_AXIS));
		this.add(timeslotPanel);
		
		loadData();
	}
	
	void setWidth (int width) {
		
		this.setMaximumSize(new Dimension (
				width,
				2000
				));
		this.setMinimumSize(new Dimension (
				width,
				200));
	}
	
	void addActionListener(ActionListener l) {
		for (TimeslotColumn column : timeslotColumn) {
			column.addActionListener(l);
			column.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					System.out.println("TEST");
				}
				
			});
		}
	}
	
	
	void loadData () {
		
		try {
			db database = new db();
			database.init();

			//Get timeslot in string array form
			
			ArrayList <String[]> timeslots_str = database.getDateAndTimeSlotsAvailability(
						medicalCenter.getID()
					);
			
			//Make timeslots from string data
			ArrayList <Timeslot> timeslots = new ArrayList<Timeslot>();
			
			for (int i=1; i<timeslots_str.size(); i++) {
				String[] timeslot_str = timeslots_str.get(i); 
				
				timeslots.add( new Timeslot(
								timeslot_str[0], 
								timeslot_str[1], 
								Integer.parseInt(timeslot_str[2])
							)
						);
				
				System.out.println("Created timeslot "+timeslot_str);
				System.out.println("Timeslot "+timeslots.get(i-1).date+" "+timeslots.get(i-1).value+" "+timeslots.get(i-1).capacity);
			}
			
			//Find date and number of timeslots for each day
			String[] dates = new String[7];
			Timeslot[][] timeslotCols = new Timeslot[7][3];
			//int[] slots = {0,0,0,0,0,0,0};
			
			int i=-1;
			int j=2;
			
			for(Timeslot timeslot: timeslots) {
				
				if (j==2) {
					j=0;
					i++;
				}
				else {
					j++;
				}
				
				if (i>=7) break;
				
				if (j==0) {
					timeslotCols[i] = new Timeslot[3];
					dates[i] = timeslot.date;
					System.out.println("Date "+i+" : "+timeslot.date);
				}
				
				timeslotCols[i][j] = timeslot;

				System.out.print(timeslot.date+" "+timeslot.value+" "+timeslot.capacity+" -> ");
				System.out.println(timeslotCols[i][j].date+" "+timeslotCols[i][j].value+" "+timeslotCols[i][j].capacity);
				System.out.println(j);
				
			}
			
				/*
				if(timeslot.date != tmp) {
					System.out.println("Initializing data for timeslot column "+i);
					timeslotCols [i] = new Timeslot[3];
					j=0;
					tmp = timeslot.date;
					dates[i] = tmp;
					timeslotCols [i][j] = timeslot;
					System.out.println("Added timeslot ("+i+","+j+") : "+timeslotCols[i][j].date+", "+timeslotCols[i][j].value+", "+timeslotCols[i][j].capacity);
					
					i++;
				}
				else {
					System.out.println("Adding new entry "+j+" to date "+dates[i]);
					timeslotCols [i][j] = timeslot;
					System.out.println("Added timeslot ("+i+","+j+") : "+timeslotCols[i][j].date+", "+timeslotCols[i][j].value+", "+timeslotCols[i][j].capacity);
					
				}
				*/
			
			for (String date : dates) {
				System.out.println(date);
			}
			
			for (Timeslot[] timec : timeslotCols) {
				for (Timeslot t : timec) {
					System.out.println(t);
				}
			}
			
			//Create day panel with the appropriate dates
			dayPanel = new DayPanel [] {
					new DayPanel("Monday", dates[0]),
					new DayPanel("Tuesday", dates[1]),
					new DayPanel("Wednesday", dates[2]),
					new DayPanel("Thursday", dates[3]),
					new DayPanel("Friday", dates[4]),
					new DayPanel("Saturday", dates[5]),
					new DayPanel("Sunday", dates[6])
			};
			
			/*
			dayPanel = new DayPanel [] {
					new DayPanel("Monday", "22-02-2022"),
					new DayPanel("Tuesday", "23-02-2022"),
					new DayPanel("Wednesday", "24-02-2022"),
					new DayPanel("Thursday", "25-02-2022"),
					new DayPanel("Friday", "26-02-2022"),
					new DayPanel("Saturday", "27-02-2022"),
					new DayPanel("Sunday", "28-02-2022")
			};
			*/
						
			//Create timeslot Columns
			timeslotColumn = new TimeslotColumn[] {
					new TimeslotColumn(0, timeslotCols[0]),
					new TimeslotColumn(1, timeslotCols[1]),
					new TimeslotColumn(2, timeslotCols[2]),
					new TimeslotColumn(3, timeslotCols[3]),
					new TimeslotColumn(4, timeslotCols[4]),
					new TimeslotColumn(5, timeslotCols[5]),
					new TimeslotColumn(6, timeslotCols[6])
			};
			
			for (TimeslotColumn tsc : timeslotColumn) {
				if (tsc == null) {
					System.out.println("~~~NULL COL");
				}
			}
			/*
			timeslotColumn = new TimeslotColumn[] {
					new TimeslotColumn(0),
					new TimeslotColumn(1),
					new TimeslotColumn(2),
					new TimeslotColumn(3),
					new TimeslotColumn(4),
					new TimeslotColumn(5),
					new TimeslotColumn(6)
			};
			*/
			
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(datePanel, e.getMessage());
			e.printStackTrace();
		}
		
		
		datePanel.add(Box.createGlue());
		for (DayPanel day : dayPanel) {
			datePanel.add(day);
			datePanel.add(Box.createGlue());
		}
		
		timeslotPanel.add(Box.createGlue());
		for (TimeslotColumn column : timeslotColumn) {
			timeslotPanel.add(column);
			timeslotPanel.add(Box.createGlue());
		}
		
		revalidate();
		repaint();
	}
	
	class DayPanel extends JPanel {
		JLabel day;
		JLabel date;
		
		DayPanel (String day, String date) {
			this.day = new JLabel(day);
			this.date = new JLabel(date);
			
			this.setBackground(background);
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			this.add(this.day);
			this.add(this.date);
		}
	}
	
	class TimeslotEntry extends JButton{
		Timeslot t;
		
		Color color;
		
		TimeslotEntry (Timeslot t) {
			super (t.value);
			this.t = t;
			
			this.setText(t.value);
			this.color = availabilityColors[t.getAvailability().ordinal()];
			this.setMargin(new Insets(5,5,5,5));
			this.setBorder( BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(
							background,
							5),
					BorderFactory.createCompoundBorder(
							BorderFactory.createLineBorder(
									color,
									2), 							
							BorderFactory.createEmptyBorder(
									5,5,5,5)
							)
					)
			);
			
			if (t.getAvailability() == Availability.UNAVAILABLE) {
				setForeground(color);
				setBackground(background);
				setEnabled(false);
				return;
			}
			
			setBackground(color);
		}
		
		public void addNewActionListener (ActionListener l) {
			System.out.println("ADDED");
			addActionListener(l);
		}
	}
	
	class TimeslotColumn extends JPanel {
		
		TimeslotEntry[] timeslotEntry = new TimeslotEntry[3]; 
		int id;
		
		TimeslotColumn (int id, Timeslot[] timeslots) {
			this.setBackground(background);
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.id = id;
			loadData(timeslots);
		}
		
		void loadData (Timeslot[] timeslots) {
			//removeAll();
			
			this.add(Box.createGlue());
			int i=0;
			for (Timeslot timeslot : timeslots) {
					
					if (timeslot == null)
					{
						timeslotEntry[i] = new TimeslotEntry(new Timeslot("   -   ", "   -   ", 0));
					}
					else {
						timeslotEntry[i] = new TimeslotEntry(timeslot);
					}
					this.add(timeslotEntry[i]);						
					this.add(Box.createGlue());
					i++;
			}
				
			/*
			timeslots = new TimeslotEntry [] {
				new TimeslotEntry (new Timeslot(0, "08:00-10:00", 10, 2)),
				new TimeslotEntry (new Timeslot(1, "10:00-12:00", 20, 10)),
				new TimeslotEntry (new Timeslot(2, "12:00-14:00", 10, 9)),
				new TimeslotEntry (new Timeslot(3, "14:00-16:00", 10, 10)),
				new TimeslotEntry (new Timeslot(4, "16:00-18:00", 0, 10))
			};
			
			 */
		}
		
		void addActionListener (ActionListener l) {
			for (TimeslotEntry entry : timeslotEntry) {
				entry.addActionListener(l);
			}
		}
		
		void updateData () {
			
		}
	}
}

