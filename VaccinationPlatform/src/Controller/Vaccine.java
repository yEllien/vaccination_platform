package Controller;

public class Vaccine {
	
	String name;
	String manufacturer;
	int dosesRequired;
	
	Vaccine (String name, String manufacturer, int doses) {
		this.name = name;
		this.manufacturer = manufacturer;
		this.dosesRequired = dosesRequired;
	}
	
	public static Vaccine[] vaccines = new Vaccine[] {
			new Vaccine ("Novaxovid", "Novavax", 2),
			new Vaccine ("Spikevax", "Moderna", 2),
			new Vaccine ("Cormirnaty", "Pfizer", 2),
			new Vaccine ("AD26.COV2.S", "Johnson & Johnson", 1),
			new Vaccine ("Vaxzevria", "AstraZeneca", 2)
		};
	
}