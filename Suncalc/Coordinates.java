package Suncalc;

/**
 * Class of Coordinates. Includes latitude and longitude
 * You can set or get this coordinates from object
 * Use java.math for rounding
 * 
 * @author Igor
 */
public class Coordinates {
	// Variables
	private double latitude;
	private double longitude;
	
	//----------------------------------------------------------------------------------
	// Constructors
	/**
	 * Empty coordinates constructor. Default coordinates 0.0 0.0
	 */
	public Coordinates() {
		this.latitude = this.longitude = 0.0;
	}
	
	/**
	 * Coordinates constructor
	 * 
	 * @param latitude known latitude in format 0.0, south latitude is negative
	 * @param longitude known longitude in format 0.0, east longitude is negative
	 */
	public Coordinates(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	//----------------------------------------------------------------------------------
	// Get/set methods
	/**
	 * Get latitude coordinate
	 * 
	 * @return variable latitude
	 */
	public double getLatitude() {
		return this.latitude;
	}
	
	/**
	 * Get longitude coordinate
	 * 
	 * @return variable longitude
	 */
	public double getLongitude() {
		return this.longitude;
	}
	
	//----------------------------------------------------------------------------------
	/**
	 * Set known coordinates
	 * 
	 * @param latitude known latitude
	 * @param longitude known longitude
	 */
	public void setCoordinates(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	/**
	 * Set only known latitude, longitude is unchangeable
	 * 
	 * @param latitude known latitude
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * Set only known longitude, latitude is unchangeable
	 * 
	 * @param longitude known longitude
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	//----------------------------------------------------------------------------------
	// To string format
	/**
	 * Get coordinates in string format 0.0N 0.0E
	 */
	public String toString() {
		String coords = "" + Math.abs(this.latitude);
		// Below 0 latitude is south hemisphere
		if(this.latitude < 0)
			coords += "S";
		else
			coords += "N";
		coords += " " + Math.abs(this.longitude);
		// Below 0 longitude is west hemisphere
		if(this.longitude < 0)
			coords += "W";
		else
			coords += "E";
		
		return coords;
	}
}