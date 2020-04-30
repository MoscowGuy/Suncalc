package Suncalc;

import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JComboBox;

public class CityList {
	private ArrayList<Country> countries;
	private ArrayList<City> cities;
	private JComboBox<City> cb;
	private BTree citiesTree;
	
	public CityList() {
		this.countries = new ArrayList<Country>();
		this.cb = new JComboBox<City>();
		cb.setEditable(true);
		cb.setFont(new Font("Tahoma", Font.PLAIN, 11));
		citiesTree = new BTree();
		cities = new ArrayList<City>();
	}
	
	public void createCityList() {
		if(this.countries.size() > 0) {
			for (Country country : countries) {
				cities.add(country.getCapital());
			}
			sortingCities(cities);
			createComboBox();
			createCityTree();
		}
	}
	
	public void createComboBox() {
		if(cb.getItemCount() > 0)
			cb.removeAllItems();
		for(City c : cities) {
			cb.addItem(c);
		}
	}
	
	public void sortingCities(ArrayList<City> citiesArray) {
		int left = 0;
		int right = citiesArray.size() - 1;
		do {
			for(int i = left; i < right; i++) {
				City city1 = citiesArray.get(i);
				City city2 = citiesArray.get(i+1);
				String name = city1.getName() + ", " + city1.getCountry().getName();
				String name2 = city2.getName() + ", " + city2.getCountry().getName();
				if(name.compareTo(name2) > 0) {
					citiesArray.set(i, city2);
					citiesArray.set(i+1, city1);
				}
			}
			right --;
			for(int i = right; i > left; i--) {
				City city1 = citiesArray.get(i);
				City city2 = citiesArray.get(i-1);
				String name = city1.getName() + ", " + city1.getCountry().getName();
				String name2 = city2.getName() + ", " + city2.getCountry().getName();
				if(name.compareTo(name2) < 0) {
					citiesArray.set(i, city2);
					citiesArray.set(i-1, city1);
				}
			}
			left++;
		} while(left < right);
	}
	
	public void createCityTree() {
		if(this.countries.size() > 0) {
			for(Country country : countries) {
				if(country.getCapital() != null)
					citiesTree.setCity(country.getCapital());
			}
		}
	}
	
	public boolean checkCountry(int id) {
		for (Country country : countries) {
			if(country.getId() == id)
				return true;
		}
		return false;
	}
	
	public void getCitiesFromNode(NodeTree node, ArrayList<City> citiesArray) {
		if(node.hasClidren()) {
			for(NodeTree child : node.getChildren()) {
				getCitiesFromNode(child, citiesArray);
			}
		}
		if(node.getCity() != null)
			citiesArray.add(node.getCity());
	}
	
	public Country getCountry(int id) {
		for (Country country : countries) {
			if(country.getId() == id)
				return country;
		}
		return null;
	}
	
	public JComboBox<City> getComboBox(){
		return this.cb;
	}
	
	public BTree getCitiesTree() {
		return this.citiesTree;
	}
	
	public void setCountry(Country country) {
		if(!checkCountry(country.getId()))
			this.countries.add(country);
	}
}