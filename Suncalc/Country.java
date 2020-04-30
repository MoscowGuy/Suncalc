package Suncalc;

import java.util.ArrayList;

public class Country {
	private int id;
	private String name;
	private City capital;
	private ArrayList<City> cities;
	
	public Country() {
		this.id = 0;
		this.name = "";
		this.capital = null;
		this.cities = new ArrayList<City>();
	}
	
	public Country(int id, String name, City capital) {
		this.id = id;
		this.name = name;
		this.capital = capital;
		this.cities = new ArrayList<City>();
	}
	
	public Country(int id, String name, City capital, ArrayList<City> cities) {
		this.id = id;
		this.name = name;
		this.capital = capital;
		this.cities = cities;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public City getCapital() {
		return this.capital;
	}
	
	public ArrayList<City> getCitiesList(){
		return this.cities;
	}
	
	public int citiesCount() {
		if(this.capital != null)
			return this.cities.size() + 1;
		else
			return this.cities.size();
	}
	
	public City getCity(int id) {
		return this.cities.get(id);
	}
	
	public void setCity(City city) {
		this.cities.add(city);
	}
	
	public String toString() {
		String country = "{ " + this.id + ", " + this.name;
		if(this.capital != null)
			country += ", capital: " + this.capital.getName();
		country += ", cities: " + citiesCount() + " }";
		
		return country;
	}
}