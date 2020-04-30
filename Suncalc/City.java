package Suncalc;

public class City extends Place {
	private String name;
	private int id;
	private Country country;
	
	public City() {
		super();
		this.name = "";
		this.id = 0;
		this.country = null;
	}
	
	public City(Coordinates coords, int id, String name, int timeZone) {
		super(coords, timeZone);
		this.id = id;
		this.name = name;
		this.country = null;
	}
	
	public City(Coordinates coords, int id, String name, int timeZone, Country country) {
		super(coords, timeZone);
		this.id = id;
		this.name = name;
		this.country = country;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getId() {
		return this.id;
	}
	
	public Country getCountry() {
		return this.country;
	}
	
	public String toString() {
		return this.name + ", " + this.country.getName();
	}
	
	public void setCountry(Country country) {
		this.country = country;
	}
}