package Suncalc;

/**
 * The class describes the <b>place</b> of observation chosen by the user,
 * which he chooses either from the list of cities or by entering the coordinates of the desired place.
 * 
 * @author Ibra_san
 */
public class Place {
	// Variables
	private Coordinates coords;
	private int timeZone;
	
	//----------------------------------------------------------------------------------
	// Constructors
	/**
	 * Empty constructor. Coordinates are set as {0.0N, 0.0E}, it is not a city, 
	 * the name of the city is empty.
	 */
	public Place() {
		this.coords = new Coordinates();
		this.timeZone = 0;
	}
	
	/**
	 * Creates an object according to the entered coordinates and time zone.
	 * <p>
	 * @param coords user-entered coordinates (Suncalc.Coordinates object)
	 * @param timeZone location time zone
	 */
	public Place(Coordinates coords, int timeZone) {
		this.coords = coords;
		this.timeZone = timeZone;
	}
	//----------------------------------------------------------------------------------
	// Get/set methods
	/**
	 * Returns coordinates of the place.
	 * <p>
	 * @return <b>Coordinates</b> <i>coords</i> object
	 * @see Suncalc.Coordinates
	 */
	public Coordinates getCoordinates() {
		return this.coords;
	}
	
	/**
	 * Return time zone of the place
	 * <p>
	 * @return <b>double</b> <i>timeZone</i>
	 */
	public int getTimeZone() {
		return this.timeZone;
	}
	//----------------------------------------------------------------------------------
	/**
	 * Entering location coordinates. Other parameters are not changed.
	 * <p>
	 * @param coords user-entered coordinates (Suncalc.Coordinates object)
	 */
	public void setCoordinates(Coordinates coords) {
		this.coords = coords;
	}
	
	/**
	 * Entering location time zone.
	 * <p>
	 * @param timeZone user-entered time zone
	 */
	public void setTimeZone(int timeZone) {
		this.timeZone = timeZone;
	}
	
	/**
	 * Copy parameters from <b>place</b> to <b>place</b>.
	 * <p>
	 * @param place object, that you want to copy
	 */
	public void copyFrom(Place place) {
		this.coords.setLatitude(place.getCoordinates().getLatitude());
		this.coords.setLongitude(place.getCoordinates().getLongitude());
		this.timeZone = place.timeZone;
	}
	
	//----------------------------------------------------------------------------------
	// Get string
	/**
	 * Displays a <b>string</b> of location data.
	 * <p>
	 * {55.67N 37.47E, 3 UTC}
	 */
	@Override
	public String toString() {
		String place = "{" + this.coords + ", " + this.timeZone + " UTC}";
		return place;
	}
}