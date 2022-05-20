package View;

public class MedicalUserPage extends UserPage {

	MedicalUserPage(VaccinationPlatformGUI frame) {
		super(frame);
		loadAccountInfo();
		loadOptions();
	}
	
	void loadAccountInfo () {
		//TODO get name
		leftPanel.accountName.setText("Employee name");
	}
	
	
	void loadOptions() {
		leftPanel.options.addOption("Account");
		leftPanel.options.addOption("Appointments");
		leftPanel.options.selectOption(0);
	}

}
