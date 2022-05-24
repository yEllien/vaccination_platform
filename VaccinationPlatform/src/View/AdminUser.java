package View;

public class AdminUser extends User{

	DataPanel dataPanel;
	
	AdminUser(VaccinationPlatformGUI frame) {
		super(frame);
	}
	
	void loadAccountInfo () {
		//TODO get name
		/*
		setAccountName();
		dataPanel.updateData();
	*/
	}
	
	void loadOptions() {
		leftPanel.options.addOption("Medical Center Information");
		leftPanel.options.addOption("Appointment Information");
		leftPanel.options.selectOption(0);
	}

	@Override
	void setUp() {
		createAccountTab();
		createOptionAndPage("Appointment Settings");
	}
	
	void createAccountTab () {
		createOptionAndPage("Vaccination Center Information");
		selectOption(0);
		dataPanel = new DataPanel(
				new String[]{
					"Center Name",
					"Center ID",
					"Address", 
					"TIN",
					"e-mail",
					"Phone number"
				},
				panelColor, backgroundColor);
		mainPanel.addToContentPanel(0, dataPanel);
	}

	@Override
	void reload() {
		// TODO Auto-generated method stub
		loadAccountInfo();
	}
}
