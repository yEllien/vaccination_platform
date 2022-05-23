package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

enum Availability {
	UNAVAILABLE,
	LIMITED,
	AVAILABLE
}

class Timeslot {
	int id;
	String value;
	int capacity;
	int appointments;
	
	Timeslot (int id, String value, int capacity, int appointments) {
		this.id = id;
		this.value = value;
		this.capacity = capacity;
		this.appointments = appointments;
	}
	
	Availability getAvailability () {
		
		if (capacity == 0)
			return Availability.UNAVAILABLE;
		
		float ratio = (float) appointments/capacity;
		System.out.println(ratio);
				
		if (ratio == 1)
			return Availability.UNAVAILABLE;
		
		if (ratio > 0.8)
			return Availability.LIMITED;
		
		return Availability.AVAILABLE;
	}
}

public class Calendar extends JPanel{
	
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

	
	Calendar (Color background) {
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
		}
	}
	
	
	void loadData () {
		dayPanel = new DayPanel [] {
				new DayPanel("Monday", "22-02-2022"),
				new DayPanel("Tuesday", "23-02-2022"),
				new DayPanel("Wednesday", "24-02-2022"),
				new DayPanel("Thursday", "25-02-2022"),
				new DayPanel("Friday", "26-02-2022"),
				new DayPanel("Saturday", "27-02-2022"),
				new DayPanel("Sunday", "28-02-2022")
		};
		
		timeslotColumn = new TimeslotColumn[] {
				new TimeslotColumn(0),
				new TimeslotColumn(1),
				new TimeslotColumn(2),
				new TimeslotColumn(3),
				new TimeslotColumn(4),
				new TimeslotColumn(5),
				new TimeslotColumn(6)
		};
		
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
			addActionListener(l);
		}
	}
	
	class TimeslotColumn extends JPanel {
		
		TimeslotEntry[] timeslots; 
		int id;
		
		TimeslotColumn (int id) {
			this.setBackground(background);
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.id = id;
			loadData();
		}
		
		void loadData () {
			//removeAll();
			timeslots = new TimeslotEntry [] {
				new TimeslotEntry (new Timeslot(0, "08:00-10:00", 10, 2)),
				new TimeslotEntry (new Timeslot(1, "10:00-12:00", 20, 10)),
				new TimeslotEntry (new Timeslot(2, "12:00-14:00", 10, 9)),
				new TimeslotEntry (new Timeslot(3, "14:00-16:00", 10, 10)),
				new TimeslotEntry (new Timeslot(4, "16:00-18:00", 0, 10))
			};
			
			this.add(Box.createGlue());
			for (TimeslotEntry entry : timeslots) {
				this.add(entry);
				this.add(Box.createGlue());
			}
		}
		
		void addActionListener (ActionListener l) {
			for (TimeslotEntry entry : timeslots) {
				entry.addActionListener(l);
			}
		}
		
		void updateData () {
			
		}
	}
}

