package Suncalc;

import static Suncalc.JulianDay.JULIAN_CENTURY;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;
import java.util.*;

/**
 * Class of astronomy time. Includes sidereal Grinvich time, star time in user point and United Time
 * Use Julian days, Gregorian calendar from java.util and coordinates
 * 
 * @author Igor
 */
public class StarTime {
	//Variables
	private double siderealGrinvichTime;
	private double UTtime;
	private double starTime;
	
	//----------------------------------------------------------------------------------
	//Constructors
	/**
	 * Empty constructor, all time is default 0.0
	 */
	public StarTime() {
		this.siderealGrinvichTime = this.UTtime = this.starTime = 0.0;
	}
	
	/**
	 * Constructor of star time.
	 * 
	 * @param julianDay current Julian date
	 * @param calendar Gregorian calendar, includes user date, time and time zone
	 * @param coords coordinates of user point
	 */
	public StarTime(JulianDay julianDay, GregorianCalendar calendar, Coordinates coords, int timeZone) {
		this.siderealGrinvichTime = toSidereal(julianDay); // Get sidereal time
		this.UTtime = timeToUT(calendar, timeZone); // Get United time (in Grinvich)
		this.starTime = calculateStarTime(UTtime, siderealGrinvichTime, coords.getLongitude()); // Get star time
	}
	//----------------------------------------------------------------------------------
	// Methods
	/**
	 * Calculate sidereal Grinvich time
	 * 
	 * @param julianDay current Julian date
	 * @return calculated sidereal Grinvich time
	 */
	public double toSidereal(JulianDay julianDay) {
		// Constants
		final double A, B, C, D;
		A = 24110.54841;
		B = 8640184.812;
		C = 0.093104;
		D = 0.0000062;
		// Get part of Julian century (use constant from JulianDay = 36525). 50 years is 0.5, 100 years is 1
		double partInCentury = (julianDay.getDateInXXICentury())/JULIAN_CENTURY;
		
		return (A + B*partInCentury + C*Math.pow(partInCentury, 2) - D*Math.pow(partInCentury, 3));
	}
	
	public double toSidereal(double julianDay) {
		// Constants
		final double A, B, C, D;
		A = 24110.54841;
		B = 8640184.812;
		C = 0.093104;
		D = 0.0000062;
		// Get part of Julian century (use constant from JulianDay = 36525). 50 years is 0.5, 100 years is 1
		double partInCentury = (julianDay - JulianDay.MJD_POINT - JulianDay.XXI_CENTURY)/36525.0;
		
		return (A + B*partInCentury + C*Math.pow(partInCentury, 2) - D*Math.pow(partInCentury, 3));
	}
	
	/**
	 * Convert user time to United time (in Grinvich)
	 * 
	 * @param hour known hour
	 * @param minute known minute
	 * @param sec known seconds
	 * @param timeZone known time zone
	 * @return calculated United time
	 */
	public double timeToUT(GregorianCalendar calendar, int timeZone) {
		// Take parameters from Gregorian calendar
		int hour = calendar.get(HOUR_OF_DAY);
		int minute = calendar.get(MINUTE);
		int sec = calendar.get(SECOND);
		double time = hour - timeZone + minute/60.0 + sec/3600.0;
		// Time must be behind 0 and 24
		if(time >= 24)
			time -= 24;
		if(time < 0)
			time += 24;
		
		return time;
	}
	
	/**
	 * Calculate star time for user longitude
	 * 
	 * @param timeInUT taken United time (in Grinvich)
	 * @param grinvichTime sidereal Grinvich time
	 * @param longitude user longitude
	 * @return calculated star time
	 */
	public double calculateStarTime(double timeInUT, double grinvichTime, double longitude) {
		double starSec = timeInUT*3600*366.2422/365.2422; // Get United time in seconds and calculate star seconds
		double GMST = (grinvichTime + starSec)/3600*15; // Grinvich median star time in degrees
		double starTime = GMST + longitude; // Add user longitude
		// Time angle must be behind 0 and 360 degrees
		while(starTime >= 360)
			starTime -= 360;
		
		return starTime;
	}
	
	/**
	 * Convert star time from angle format to time format 00:00:00
	 * @return Clock object
	 */
	public Clock convertToTime() {
		Clock time = new Clock();
		time.timeFromAngle(this.starTime);
		
		return time;
	}
	
	public void recalculate(JulianDay julianDay, GregorianCalendar calendar, Coordinates coords, int timeZone) {
		this.siderealGrinvichTime = toSidereal(julianDay); // Get sidereal time
		this.UTtime = timeToUT(calendar, timeZone); // Get United time (in Grinvich)
		this.starTime = calculateStarTime(UTtime, siderealGrinvichTime, coords.getLongitude()); // Get star time
	}
	
	//----------------------------------------------------------------------------------
	// Get/set methods
	/**
	 * Get United time in seconds format
	 * 
	 * @return variable UTtime in seconds
	 */
	public double getUTtimeInSec() {
		return UTtime*3600;
	}
	
	/**
	 * Get sidereal Grinvich time
	 * 
	 * @return variable siderealGrinvichTime
	 */
	public double getSidereal() {
		return this.siderealGrinvichTime;
	}
	
	/**
	 * Get United time (in Grinvich)
	 * 
	 * @return variable UTtime
	 */
	public double getUTtime() {
		return this.UTtime;
	}
	
	/**
	 * Get star time angle
	 * 
	 * @return variable starTime
	 */
	public double getStarTime() {
		return this.starTime;
	}
		
	//----------------------------------------------------------------------------------
	/**
	 * Set sidereal Grinvich time
	 * 
	 * @param siderealGrinvichTime sidereal Grinvich time
	 */
	public void setSidereal(double siderealGrinvichTime) {
		this.siderealGrinvichTime = siderealGrinvichTime;
	}
	
	/**
	 * Set United time. Star time calculate automatically
	 * 
	 * @param UTtime known time
	 * @param longitude user point longitude
	 */
	public void setUtTime(double UTtime, double longitude) {
		this.UTtime = UTtime;
		starTime = calculateStarTime(siderealGrinvichTime, UTtime, longitude); // Calculate star time
	}
	
	/**
	 * Set only star time. Another variables not changeable
	 * 
	 * @param starTime star time
	 */
	public void setStarTime(double starTime) {
		this.starTime = starTime;
	}
}