package View;

public class AdminUserPage extends UserPage{

	DataPanel dataPanel;
	
	AdminUserPage(VaccinationPlatformGUI frame) {
		super(frame);
	}
	
	void loadAccountInfo () {
		//TODO get name
		leftPanel.accountName.setText("Admin username");
		dataPanel.loadData();
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
		dataPanel.init();
	}

	@Override
	void reload() {
		// TODO Auto-generated method stub
		loadAccountInfo();
	}
}
