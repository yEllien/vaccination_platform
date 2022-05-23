package View;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Table extends JPanel{

	int columnCount;
	int rowCount;
	int width; 
	int borderWidth = 2;
	
	Color backgroundColor;
	Color borderColor;
	Color secondaryBorderColor;
	Color fontColor;
	
	ArrayList <Row> rows;
	
	Table (int columns, int rows, int width, 
			Color backgroundColor, Color borderColor, 
			Color secondaryBorderColor, Color fontColor) {
		this.columnCount = columns;
		this.rowCount = rows;
		
		this.backgroundColor = backgroundColor;
		this.borderColor = borderColor;
		this.secondaryBorderColor = secondaryBorderColor;
		this.fontColor = fontColor;
		
		this.setBorder(BorderFactory.createLineBorder(borderColor, borderWidth));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(Box.createGlue());
	}
	
	void addTitles () {
		JPanel titles = new JPanel();
		titles.setBackground(borderColor);
		titles.setLayout(new BoxLayout(titles, BoxLayout.X_AXIS));
		titles.setBorder(BorderFactory.createLineBorder(borderColor, borderWidth));
		
		JLabel tmp = new JLabel("SSN");
		
	}
	
	void addRow (String SSN, String name, String date, String time, boolean confirm) {
		
		rows.add(new Row(SSN, name, date, time, confirm));
		this.add(Box.createGlue());
	}
	
	class Row extends JPanel {
		
		JLabel ssnLabel;
		JLabel nameLabel;
		JLabel dateLabel;
		JLabel timeLabel;
		JCheckBox confirmBox;
		
		Row (String SSN, String name, String date, String time, boolean confirm) {			
			this.setBackground(backgroundColor);
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.setBorder(BorderFactory.createLineBorder(borderColor, borderWidth));
			
			ssnLabel = new JLabel(SSN);
			nameLabel = new JLabel(name);
			dateLabel = new JLabel(date);
			timeLabel = new JLabel(time);
			confirmBox = new JCheckBox();
			confirmBox.setSelected(confirm);
			
			this.add(Box.createGlue());
			this.add(ssnLabel);
			this.add(Box.createGlue());
			this.add(nameLabel);
			this.add(Box.createGlue());
			this.add(dateLabel);
			this.add(Box.createGlue());
			this.add(timeLabel);
			this.add(Box.createGlue());
			this.add(confirmBox);
			this.add(Box.createGlue());
		}
	}
	
}
