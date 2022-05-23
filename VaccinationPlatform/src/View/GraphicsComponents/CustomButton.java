package View.GraphicsComponents;

import java.awt.Color;

import View.CustomColors;

public class CustomButton extends RoundedButton {

	public CustomButton (String label, Color background, Color foreground) {
		super(label);
		setBackground(background);
		setForeground(foreground);
	}
}
