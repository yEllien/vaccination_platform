package View;

public class AdminUserPage extends UserPage{

	AdminUserPage(VaccinationPlatformGUI frame) {
		super(frame);
		loadAccountInfo();
		loadOptions();
	}
	
	void loadAccountInfo () {
		//TODO get name
		leftPanel.accountName.setText("Admin username");
	}
	
	void loadOptions() {
		leftPanel.options.addOption("Medical Center Information");
		leftPanel.options.addOption("Appointment Information");
		leftPanel.options.selectOption(0);
	}

	@Override
	void setUp() {
		// TODO Auto-generated method stub
		
	}
}
