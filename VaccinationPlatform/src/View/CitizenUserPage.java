package View;

public class CitizenUserPage extends UserPage{

	CitizenUserPage(VaccinationPlatformGUI frame) {
		super(frame);
		loadAccountInfo();
		loadOptions();
	}
	
	void loadAccountInfo () {
		//TODO get name
		leftPanel.accountName.setText("Citizen name");
	}
	
	void loadOptions() {
		leftPanel.options.addOption("Account");
		leftPanel.options.addOption("Appointments");
		leftPanel.options.selectOption(0);
		leftPanel.revalidate();
	}

}
