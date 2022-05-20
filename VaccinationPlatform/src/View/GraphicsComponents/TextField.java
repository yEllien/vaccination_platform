package View.GraphicsComponents;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

import View.CustomColors;

public class TextField extends JTextField {
	
	String placeholder;
	boolean active;
	
	public TextField() {
		super();
		placeholder = "";
		active = false;
		
		setForeground(CustomColors.gray);
		
		Unclicked();
		addFocusListener(new FocusListener() {
			
			public void focusGained (FocusEvent e) {
				Clicked();
			}
			
			public void focusLost (FocusEvent e) {
				Unclicked();
			}
			
		});

	}
	
	public void setPlaceholder (String placeholder) {
		this.placeholder = placeholder;
		setText(placeholder);
	}
	
	void Clicked () {
		if (getText().equals(placeholder)) {
			setText("");
			setForeground(Color.black);
		}
	}
	
	void Unclicked () {
		if (getText().isEmpty()) {
			setForeground(CustomColors.gray);
			setText(placeholder);
		}
	}
}
