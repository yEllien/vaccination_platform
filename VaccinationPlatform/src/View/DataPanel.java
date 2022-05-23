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
	ArrayList <TextField> dataFields;
	
	JPanel titlePanel;
	JPanel dataPanel;
	JPanel wrapperPanel;
	
	JPanel parent;
	
	JPanel confirmPanel;
	JPanel textModeButtonPanel;
	JPanel editModeButtonPanel;
	
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
		dataFields = new ArrayList<TextField> ();
		
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
		setData();
		
		this.add(wrapperPanel);
		
		editButton = new CustomButton("Edit", CustomColors.dark_blue, Color.white);
		saveButton = new CustomButton("Save", CustomColors.dark_blue, Color.white);
		cancelButton = new CustomButton("Cancel", Color.white, CustomColors.dark_blue);
		yesButton = new CustomButton ("Yes", Color.white, CustomColors.dark_blue);
		noButton = new CustomButton("No", CustomColors.dark_blue, Color.white);
		
		
		textModeButtonPanel = new JPanel();
		textModeButtonPanel.setBorder(new EmptyBorder(10,10,10,10));
		textModeButtonPanel.setPreferredSize(new Dimension(400, 70));
		textModeButtonPanel.setBackground(panelColor);
		textModeButtonPanel.setLayout(new BoxLayout(textModeButtonPanel, BoxLayout.X_AXIS));
		textModeButtonPanel.add(Box.createGlue());
		textModeButtonPanel.add(editButton);
		textModeButtonPanel.add(Box.createGlue());
		
		editModeButtonPanel = new JPanel();
		editModeButtonPanel.setBorder(new EmptyBorder(10,10,10,10));
		editModeButtonPanel.setPreferredSize(new Dimension(400, 70));
		editModeButtonPanel.setBackground(panelColor);
		editModeButtonPanel.setLayout(new BoxLayout(editModeButtonPanel, BoxLayout.X_AXIS));
		editModeButtonPanel.add(Box.createGlue());
		editModeButtonPanel.add(cancelButton);
		editModeButtonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		editModeButtonPanel.add(saveButton);
		editModeButtonPanel.add(Box.createGlue());
		
		JPanel confirmButtonPanel = new JPanel();
		confirmButtonPanel.setBackground(panelColor);
		confirmButtonPanel.setLayout(new BoxLayout(confirmButtonPanel, BoxLayout.X_AXIS));
		confirmButtonPanel.add(Box.createGlue());
		confirmButtonPanel.add(noButton);
		confirmButtonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		confirmButtonPanel.add(yesButton);
		confirmButtonPanel.add(Box.createGlue());
		
		JLabel confirmationMessage = new JLabel ("Are you sure you want to cancel your changes?");

		confirmPanel = new JPanel();
		//confirmPanel.setBorder(new EmptyBorder(10,10,10,10));
		confirmPanel.setBackground(panelColor);
		confirmPanel.setPreferredSize(new Dimension(400, 70));
		confirmPanel.setLayout(new BoxLayout(confirmPanel, BoxLayout.Y_AXIS));
		confirmPanel.add(Box.createGlue());
		confirmPanel.add(confirmationMessage);
		confirmPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		confirmPanel.add(confirmButtonPanel);
		confirmPanel.add(Box.createGlue());
		
		this.add(textModeButtonPanel);
		
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editMode();
				remove(textModeButtonPanel);
				add(editModeButtonPanel);
				revalidate();
				repaint();
			}
		});
		
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
				textMode();
				remove(editModeButtonPanel);
				add(textModeButtonPanel);
				revalidate();
				repaint();
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				remove(editModeButtonPanel);
				add(confirmPanel);
				revalidate();
				repaint();
			}
		});

		noButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				add(editModeButtonPanel);
				remove(confirmPanel);
				revalidate();
				repaint();
			}
		});
		
		yesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textMode();
				remove(confirmPanel);
				add(textModeButtonPanel);
				revalidate();
				repaint();
			}
		});
		
	
		setPreferredSize(new Dimension (400,360));
		//setMinimumSize(new Dimension(400,400));
		//setMaximumSize(new Dimension(400,400));
		
		revalidate();
	
	}	
	
	public void init () {
		
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
	
	TextField dataField () {
		TextField field = new TextField();
		field.setHorizontalAlignment(SwingConstants.CENTER);
		field.setBorder(BorderFactory.createLineBorder(panelColor, 5));
		field.setPreferredSize(new Dimension(200, 30));
		field.setMinimumSize(new Dimension (200, 30));
		field.setMaximumSize(new Dimension(200, 100));
		return field;
	}
	
	void setTitles () {
		JLabel tmp;
		
		for (String name : dataTitlesStr) {
			tmp = titleLabel(name);
			dataTitles.add(tmp);
			titlePanel.add(tmp);
		}
	}
	
	void setData () {
		JLabel tmpl;
		TextField tmpf;
		
		for (int i=0; i<dataTitlesStr.length; i++) {
			tmpl = dataLabel("");
			tmpf = dataField();
			
			dataTexts.add(tmpl);
			dataFields.add(tmpf);
			
			dataPanel.add(tmpl);
		}
	}
	
	void editMode () {
		for (int i=0; i<dataTexts.size(); i++) {
			dataPanel.remove(dataTexts.get(i));
			dataPanel.add(dataFields.get(i));
		}
	}
	
	void save() {
		for (int i=0; i<dataTexts.size(); i++) {
			String newText = dataFields.get(i).getText();
			dataFields.get(i).setPlaceholder(newText);
			dataTexts.get(i).setText(newText);
		}
	}
	
	void textMode () {
		for (int i=0; i<dataTexts.size(); i++) {
			dataPanel.remove(dataFields.get(i));
			dataPanel.add(dataTexts.get(i));
		}
	}
	
	
	
	void loadData() {
		
		String[] loadedArray = new String[] {
				"Ellie", 
				"Psilou", 
				"17-09-2001", 
				"17090112345",
				"123456789",
				"elittsa@gmail.com",
				"6987948735"
			};
		
		JLabel tmp;
		int i=0;
		for (String item : loadedArray) {
			if (i==dataTexts.size()) break;
			dataTexts.get(i).setText(item);
			dataFields.get(i).setPlaceholder(item);
			i++;
		}
	}
}
