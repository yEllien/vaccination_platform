package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import View.GraphicsComponents.OptButton;
import View.GraphicsComponents.OptionList;
import View.GraphicsComponents.RoundedComponent;
import View.GraphicsComponents.RoundedLayeredPanel;
import View.User.LeftPanel;
import View.User.MainPanel;

abstract class User extends JPanel {
	
	VaccinationPlatformGUI frame;
	
	Color panelColor = CustomColors.light_blue;
	Color backgroundColor = Color.white;//CustomColors.very_light_gray;
	
	Color panelFontColor = CustomColors.darker_faded_blue;
	Color panelOptionColor = CustomColors.faded_blue;
	
	LeftPanel leftPanel;
	MainPanel mainPanel;
	
	int active;
		
	User (VaccinationPlatformGUI frame) {
		
		this.frame = frame;
		
		setBackground(Color.green);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		leftPanel = new LeftPanel();
		mainPanel = new MainPanel();		
		
		add(leftPanel);
		add(mainPanel);
		
		frame.revalidate();
	}
	
	void setAccountName (String accountFirstName, String accountLastName) {
		this.leftPanel.accountFirstName.setText(accountFirstName);
		this.leftPanel.accountLastName.setText(accountLastName);
	}
	
	void setAccountIcon (String icon_name) {
		try {
			ImageIcon img = new ImageIcon(ImageIO.read(new File(System.getProperty("user.dir")+"/images/"+icon_name)));			
			this.leftPanel.accountIcon = new JLabel(img);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	void selectOption (int i) {
		leftPanel.options.selectOption(i);
		mainPanel.set(i);
	}
	
	void createOptionAndPage (String optionName) {
		mainPanel.addContentPanel();
		mainPanel.content.get(mainPanel.content.size()-1).contentTitle.setText(optionName);
		leftPanel.addOption(optionName);
	}
	
	abstract void setUp ();
	abstract void reload();
	
	class LeftPanel extends JPanel {
		
		RoundedLayeredPanel base;
		
		JPanel componentsPanel;
		JPanel accountInfo;
		
		JLabel accountIcon = new JLabel();
		JLabel accountFirstName = new JLabel();
		JLabel accountLastName = new JLabel();
		
		JLabel logoutLabel;
		WhiteButton logoutButton;
		WhiteButton yesLogoutButton;
		FadedBlueButton noLogoutButton; 
		
		int activeOption = 0;
		
		OptionList options;
		
		LeftPanel () {
						
			this.setBounds(5,10, frame.getWidth()/4, frame.getHeight()-60);
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.setBackground(backgroundColor);

			base = new RoundedLayeredPanel (
					this, 
					new Dimension(getBounds().width-25, getBounds().height),
					panelColor, true, 
					panelColor, true
					);
			
			componentsPanel = new JPanel();
			componentsPanel.setBackground(panelColor);
			componentsPanel.setLayout(new BoxLayout(componentsPanel, BoxLayout.Y_AXIS));
			
			int i = base.createLayer(componentsPanel);
			this.add(base);
			

			JPanel accountInfoPanel = createAccountInfo();
			JPanel optionsPanel = createOptions();
			JPanel logoutPanel = createLogout();
			
			componentsPanel.add(Box.createGlue());
			componentsPanel.add(accountInfoPanel);
			componentsPanel.add(Box.createGlue());
			componentsPanel.add(optionsPanel);
			componentsPanel.add(Box.createGlue());
			componentsPanel.add(logoutPanel);
			componentsPanel.add(Box.createGlue());
				
		}
		
		JPanel createAccountInfo () {
			
			JPanel accountInfoPanelWrapper = new JPanel();
			accountInfoPanelWrapper.setBackground(panelColor);
			
			JPanel accountInfoPanel = new JPanel();
			accountInfoPanel.setLayout(new BoxLayout(accountInfoPanel, BoxLayout.Y_AXIS));
			accountInfoPanel.setBackground(panelColor);
			
			accountFirstName.setPreferredSize(new Dimension (
							componentsPanel.getSize().width,40));
			accountFirstName.setMaximumSize(accountFirstName.getPreferredSize());
			/*
			 */
			accountFirstName.setHorizontalAlignment(SwingConstants.CENTER);			
			accountFirstName.setForeground(panelFontColor);
			accountFirstName.setText("First Name");
			
			accountLastName.setPreferredSize(new Dimension (
					componentsPanel.getSize().width,40));
			accountLastName.setMaximumSize(accountLastName.getPreferredSize());
			accountLastName.setHorizontalAlignment(SwingConstants.CENTER);			
			accountLastName.setForeground(panelFontColor);
			accountLastName.setText("Last Name");
			
			accountInfoPanel.add(Box.createGlue());
			accountInfoPanel.add(accountFirstName);
			accountInfoPanel.add(accountLastName);
			accountInfoPanel.add(Box.createGlue());
			accountInfoPanel.setPreferredSize(new Dimension(
					accountFirstName.getPreferredSize().width, 40
					));
			
			accountInfoPanelWrapper.add(accountInfoPanel);
			accountInfoPanel.revalidate();
			
			return accountInfoPanelWrapper;
		}
		
		JPanel createOptions () {
			
			JPanel optionsPanel = new JPanel();
			optionsPanel.setBackground(panelColor);
			
			options = new OptionList (
					componentsPanel,
					new Dimension(componentsPanel.getWidth(), 50),
					panelOptionColor, false,
					panelOptionColor, false					
					);
			
			optionsPanel.add(options);
			
			return optionsPanel;
		}
		
		void addOption (String optionName) {
			
			options.addOption(optionName);
			int i = options.options.size()-1;
			options.options.get(i).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					mainPanel.set(i);
				}
				
			});
		}
		
		
		JPanel createLogout () {
			
			JPanel logoutPanel = new JPanel();
			logoutPanel.setLayout(new BoxLayout(logoutPanel, BoxLayout.Y_AXIS));
			logoutPanel.setPreferredSize(new Dimension (
					getWidth(), 100));
			logoutPanel.setMinimumSize(logoutPanel.getPreferredSize());
			logoutPanel.setMaximumSize(logoutPanel.getPreferredSize());
			logoutPanel.setBackground(panelColor);
			
			JPanel logoutConfirmPanel = new JPanel();
			logoutConfirmPanel.setLayout(new BoxLayout(logoutConfirmPanel, BoxLayout.X_AXIS));
			logoutConfirmPanel.setPreferredSize(new Dimension (
					getWidth(), 50));
			logoutConfirmPanel.setMinimumSize(logoutConfirmPanel.getPreferredSize());
			logoutConfirmPanel.setMaximumSize(logoutConfirmPanel.getPreferredSize());
			logoutConfirmPanel.setBackground(panelColor);
			
			JPanel logoutWrapper = new JPanel();
			logoutWrapper.setBackground(panelColor);
			
			JPanel logoutButtonWrapper = new JPanel();
			logoutButtonWrapper.setPreferredSize(new Dimension (
					getWidth(), 50));
			logoutButtonWrapper.setLayout(new BoxLayout(logoutButtonWrapper, BoxLayout.X_AXIS));
			logoutButtonWrapper.setBackground(panelColor);
			
			logoutLabel = new JLabel("Are you sure you want to log out?");
			logoutLabel.setPreferredSize(new Dimension (
					componentsPanel.getWidth(), 60));
			logoutLabel.setHorizontalAlignment(SwingConstants.CENTER);
			//logoutLabel.setBackground(Color.DARK_GRAY);
			logoutLabel.setVisible(false);
			
			logoutButton = new WhiteButton("Log out");
			yesLogoutButton = new WhiteButton("Yes");
			noLogoutButton = new FadedBlueButton("No");
			
			logoutConfirmPanel.add(Box.createGlue());
			logoutConfirmPanel.add(yesLogoutButton);
			logoutConfirmPanel.add(Box.createRigidArea(new Dimension(10, 0)));
			logoutConfirmPanel.add(noLogoutButton);
			logoutConfirmPanel.add(Box.createGlue());
			
			logoutConfirmPanel.setVisible(false);
			
			logoutButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					logoutLabel.setVisible(true);
					logoutButtonWrapper.setVisible(false);
					logoutConfirmPanel.setVisible(true);
				}
				
			});
			
			noLogoutButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					logoutLabel.setVisible(false);;
					logoutButtonWrapper.setVisible(true);
					logoutConfirmPanel.setVisible(false);
				}
				
			});
			
			yesLogoutButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					frame.setContentPane(frame.landingPage);
				}
				
			});
			
			logoutWrapper.add(logoutLabel);

			logoutButtonWrapper.add(Box.createGlue());
			logoutButtonWrapper.add(logoutButton);
			logoutButtonWrapper.add(Box.createGlue());
			
			logoutPanel.add(logoutWrapper);
			logoutPanel.add(logoutButtonWrapper);
			logoutPanel.add(logoutConfirmPanel);
			
			return logoutPanel;
		}
	}
	
	class MainPanel extends JPanel{
		
		ArrayList<ContentPanel> content;
		
		MainPanel () {
			
			setPreferredSize(new Dimension(frame.getWidth()-leftPanel.getWidth(), frame.getHeight()));
			setMinimumSize(getPreferredSize());
			setMaximumSize(getPreferredSize());
			setBackground(backgroundColor);
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			content = new ArrayList<ContentPanel>();
			active = -1;

		}
		
		void addContentPanel () {
			ContentPanel c = new ContentPanel();
			addContentPanel(c);
		}
		
		void addContentPanel (ContentPanel c) {
			c.id = content.size();
			content.add(c);
		}
		
		void set(int i) {
			
			this.removeAll();
			this.repaint();
			this.add(content.get(i));
			active = i;
		}
		
		void addToContentPanel (int i, Component c) {
			content.get(i).contentPanel.add(c);
		}
		
		class ContentPanel extends JPanel {
			
			int id;
			
			JLabel contentTitle;
			
			RoundedLayeredPanel basePanel;
			
			JPanel contentPanel;
			
			int titleHeight;
			
			ContentPanel () {
				
				titleHeight = 100;
				
				this.setBackground(backgroundColor);
				this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				this.setPreferredSize( new Dimension (
						mainPanel.getSize().width,frame.getSize().height));

				JPanel borderWrapper = makeBorder();
				JPanel titleWrapper = makeTitle();

				
				basePanel.createLayer(titleWrapper);
				borderWrapper.add(basePanel);
				
				contentPanel = makeContent();

				this.add(Box.createGlue());
				this.add(borderWrapper);
				this.add(contentPanel);
				this.add(Box.createGlue());
				
			}
			
			JPanel makeTitle () {
				JPanel titleWrapper = new JPanel();
				titleWrapper.setLayout(new GridBagLayout());
				
				titleWrapper.setBackground(panelColor);
				titleWrapper.setPreferredSize(new Dimension(
						mainPanel.getSize().width-titleHeight, titleHeight));
				titleWrapper.setMinimumSize(titleWrapper.getPreferredSize());
				titleWrapper.setMaximumSize(titleWrapper.getPreferredSize());
				
				contentTitle = new JLabel("Title");
				contentTitle.setBackground(Color.green);
				contentTitle.setHorizontalAlignment(SwingConstants.CENTER);
				
				titleWrapper.add(contentTitle);
				
				return titleWrapper;
			}
			
			JPanel makeBorder () {
				
				basePanel = new RoundedLayeredPanel(
						mainPanel.getBounds().x, 25, 
						new Dimension(mainPanel.getSize().width-100, titleHeight),
						panelColor, true, 
						panelColor, true,
						mainPanel.getBackground()
						);
				
				JPanel borderWrapper = new JPanel();
				borderWrapper.setLayout(new BoxLayout(borderWrapper, BoxLayout.X_AXIS));
				borderWrapper.setBackground(backgroundColor);
				borderWrapper.setPreferredSize(new Dimension (
						mainPanel.getSize().width-100, titleHeight+50));
				borderWrapper.setMinimumSize(borderWrapper.getPreferredSize());
				borderWrapper.setMaximumSize(borderWrapper.getPreferredSize());
			
				return borderWrapper;
			}
			
			JPanel makeContent () {
				JPanel contentPanel = new JPanel();
				contentPanel.setBackground(backgroundColor);
				contentPanel.setPreferredSize(new Dimension(
						mainPanel.getSize().width-100, frame.getSize().height-titleHeight-50));
				contentPanel.setMinimumSize(contentPanel.getPreferredSize());
				contentPanel.setMaximumSize(contentPanel.getPreferredSize());
				
				return contentPanel;
			}
		}
	}
}