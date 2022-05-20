package View;

public class CitizenUserPage extends UserPage{

	CitizenUserPage(VaccinationPlatformGUI frame) {
		super(frame);
		loadAccountInfo();
		//loadOptions();
	}
	
	void loadAccountInfo () {
		//TODO get name
		leftPanel.accountName.setText("Citizen name");
	}
	
	void setUp () {
		createOptionAndPage("Account");
		
		createOptionAndPage("Appointments");
		
		
		selectOption(0);
	}

}
