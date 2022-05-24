package View;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import View.GraphicsComponents.CustomButton;
import View.GraphicsComponents.RoundedLayeredPanel;
import View.GraphicsComponents.RoundedPanel;
import View.GraphicsComponents.TextField;

public class DataPanel extends JPanel{
	
	Color panelColor;
	Color backgroundColor;
	
	String dataTitlesStr[];
	
	ArrayList <JLabel> dataTitles;
	ArrayList <JLabel> dataTexts;
	//ArrayList <TextField> dataFields;
	
	JPanel titlePanel;
	JPanel dataPanel;
	JPanel wrapperPanel;
	
	JPanel parent;
	
	JPanel confirmPanel;
	JPanel textModeButtonPanel;
	//JPanel editModeButtonPanel;
	
	CustomButton editButton;
	CustomButton saveButton;
	CustomButton cancelButton;
	CustomButton yesButton;
	CustomButton noButton;
	
	DataPanel (String[] dataTitlesStr, Color panelColor, Color backgroundColor) {
		
		this.dataTitlesStr = dataTitlesStr;
		
		this.panelColor = panelColor;
		this.backgroundColor = backgroundColor;
		
		dataTitles = new ArrayList<JLabel> ();
		dataTexts = new ArrayList<JLabel> ();
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(panelColor);
		this.setBorder(new EmptyBorder(10,10,10,10));
	
		titlePanel = new JPanel ();
		titlePanel.setBackground(panelColor);
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
		titlePanel.setBorder(new EmptyBorder(10,10,10,10));
		
		dataPanel = new JPanel ();
		dataPanel.setBackground(panelColor);
		dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
		dataPanel.setBorder(new EmptyBorder(10,10,10,10));
		
		wrapperPanel = new JPanel ();
		wrapperPanel.setBackground(panelColor);
		wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.X_AXIS));
		
		wrapperPanel.add(Box.createGlue());
		wrapperPanel.add(titlePanel);
		wrapperPanel.add(dataPanel);
		wrapperPanel.add(Box.createGlue());
		
		setTitles();
		addDataLabels();
		
		this.add(wrapperPanel);
		
		setPreferredSize(new Dimension (400,360));
		
		revalidate();
	
	}	
	
	JLabel titleLabel (String name) {
		JLabel title = new JLabel(name);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBorder(new EmptyBorder(10,10,10,10));
		title.setMinimumSize(new Dimension(100, 30));
		title.setMaximumSize(new Dimension(200, 100));
		title.setFont(new Font(
				getFont().getFontName(), 
				Font.BOLD,
				getFont().getSize()));
		return title;
	}
	
	JLabel dataLabel (String val) {
		JLabel data = new JLabel (val);
		data.setHorizontalAlignment(SwingConstants.CENTER);
		data.setBorder(new EmptyBorder(15,15,15,15));
		data.setPreferredSize(new Dimension(200, 30));
		data.setMinimumSize(new Dimension (200, 30));
		data.setMaximumSize(new Dimension(200, 100));
		return data;
	}
	
	void setTitles () {
		JLabel tmp;
		
		for (String name : dataTitlesStr) {
			tmp = titleLabel(name);
			dataTitles.add(tmp);
			titlePanel.add(tmp);
		}
	}
	
	void addDataLabels () {
		JLabel tmpl;
		
		for (int i=0; i<dataTitlesStr.length; i++) {
			tmpl = dataLabel("");
			
			dataTexts.add(tmpl);
			dataPanel.add(tmpl);
		}
	}
	
	void updateData(String[] loadedArray) {
		
		int i=0;
		for (String item : loadedArray) {
			if (i==dataTexts.size()) break;
			dataTexts.get(i).setText(item);
			i++;
		}
	}
}
