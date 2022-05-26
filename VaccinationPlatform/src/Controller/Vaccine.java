package Controller;

public class Vaccine {
	
	String name;
	String manufacturer;
	int dosesRequired;
	
	Vaccine (String name, String manufacturer, int doses) {
		this.name = name;
		this.manufacturer = manufacturer;
		this.dosesRequired = doses;
	}
	
	public static Vaccine[] vaccines = new Vaccine[] {
			new Vaccine ("Novaxovid", "Novavax", 2),
			new Vaccine ("Spikevax", "Moderna", 2),
			new Vaccine ("Cormirnaty", "Pfizer", 2),
			new Vaccine ("AD26.COV2.S", "Johnson", 1),
			new Vaccine ("Vaxzevria", "AstraZeneca", 2),
		
			//from db
			new Vaccine("Johnson", "Johnson & Johnson", 1),
			new Vaccine("Moderna", "Moderna Biotech", 2),
			new Vaccine("Pfizer", "Pfizer - BioNTech", 1)
			};
	
	public static int getVaccineDoses (String id) {
		
		switch (id) {
		
		case "Novaxovid":
		case "Novavax":
			return vaccines[0].dosesRequired;
		case "Spikevax":
		case "Moderna":
			return vaccines[1].dosesRequired;
		case "Cormirnaty":
		case "Pfizer":
			return vaccines[2].dosesRequired;
		case "AD26.COV2.S":
		case "Johnson":
			return vaccines[3].dosesRequired;
		case "Vaxzevria":
		case "AstraZeneca":
			return vaccines[4].dosesRequired;
		default:
			return -1;
		}
	}
	
}