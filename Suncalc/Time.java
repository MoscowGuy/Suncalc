package Suncalc;

import java.awt.*;

/**
 * Class of time. You can take it in string format
 * Includes hours, minutes and seconds
 * 
 * @author Igor
 */
public class Time {
	// Type constants
	/**
	 * Type of hours
	 */
	public static final int HOUR = 0;
	
	/**
	 * Type of minutes
	 */
	public static final int MINUTE = 1;
	
	/**
	 * Type of seconds
	 */
	public static final int SECOND = 2;
	
	//----------------------------------------------------------------------------------
	// Variables
	private int hour;
	private int minute;
	private int second;
	
	//----------------------------------------------------------------------------------
	// Constructors
	/**
	 * Empty constructor. Default midnight. 00:00:00
	 */
	public Time() {
		this.hour = this.minute = this.second = 0;
	}
	
	/**
	 * Time constructor
	 * 
	 * @param hour known hours 
	 * @param minute known minutes
	 * @param second known seconds
	 */
	public Time(int hour, int minute, int second) {
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}
	
	/**
	 * Take time from decimal part of number (0.5 to 24 hour format (12:00:00))
	 * 
	 * @param value value that you want to transform into time format
	 */
	public void timeFromDecimalPart(double value) {
		// Get hour *24
		double h = value*24;
		// Hours couldn't be below 0
		while(h < 0)
			h += 24;
		this.hour = (int)h;
		
		getMinutesSeconds(h); // Get minutes and seconds from decimal part
	}
	
	/**
	 * Take time from number (16.50 to 16:30:00)
	 * 
	 * @param value value that you want to transform into time format
	 */
	public void timeFromNumber(double value) {
		// Hours couldn't be below 0
		while(value < 0)
			value += 24;
		this.hour = (int)value;
		
		getMinutesSeconds(value); // Get minutes and seconds from decimal part
	}
	
	/**
	 * Take time from angle (90 degrees to 06:00:00)
	 * 
	 * @param angle angle that you want to transform into time format
	 */
	public void timeFromAngle(double angle) {
		double h = angle/(360/24); // Get how much degrees in 1 hour, 24h = 360 degrees
		// Hours couldn't be below 0
		while(h < 0)
			h += 24;
		this.hour = (int)h;
		
		getMinutesSeconds(h); // Get minutes and seconds from decimal part
	}
	
	/**
	 * Take time from Julian day (decimal part (17334.0 is 12:00:00))
	 * 
	 * @param day known Julian date
	 */
	public void timeFromJulianDay(double date) {
		double time = date%(int)date;
		// Get hours, Julian day starts at 12:00:00 (0.5 is 00:00:00)
		double h = time*24 + 12; // Add 12 hours
		// Hours couldn't be over 24
		while(h >= 24)
			h -= 24;
		this.hour = (int)h;
		
		getMinutesSeconds(h); // Get minutes and seconds from decimal part
	}
	
	/**
	 * Take time from seconds format
	 * 
	 * @param time known time in seconds
	 */
	public void timeFromSeconds(int time) {
		double h = time/3600.0;
		this.hour = (int)h;
		
		getMinutesSeconds(h); // Get minutes and seconds from decimal part
	}
	
	/**
	 * Part of calculations minutes and seconds
	 * 
	 * @param h known hours with decimal part
	 */
	private void getMinutesSeconds(double h) {
		// Part after . *60 and get minutes
		double m;
		if(this.hour > 0)
			m = (h%this.hour)*60;
		else
			m = h*60;
		this.minute = (int)m;
						
		// Another part after . *60 and get seconds
		double s;
		if(this.minute > 0)
			s = (m%this.minute)*60;
		else
			s = m*60;
		this.second = (int)s;
	}
	
	//----------------------------------------------------------------------------------
	// Get/set methods
	/**
	 * Get parameter by type
	 * 
	 * @param type HOUR, MINUTE or SECOND
	 * @return variable hour, minute or second
	 */
	public int get(int type) {
		switch(type) {
		case HOUR:
			return this.hour;
		case MINUTE:
			return this.minute;
		case SECOND:
			return this.second;
		default:
			throw new AWTError("Invalid type!!"); // If you enter invalid type		
		}
	}
	
	/**
	 * Get time in decimal number format
	 * 
	 * @return time
	 */
	public double getTimeInHours() {
		double time = this.hour + this.minute/60.0 + this.second/3600.0;
		return time;	
	}
	
	/**
	 * Get time in number (seconds format 1:00:00 is 3600 sec)
	 * 
	 * @return time
	 */
	public int getTimeInSeconds() {
		int time = this.second + this.minute*60 + this.hour*3600;
		return time;
	}
	
	//----------------------------------------------------------------------------------
	/**
	 * Set time parameter by type
	 * 
	 * @param type HOUR, MINUTE or SECOND
	 * @param time known time parameter
	 */
	public void set(int type, int time) {
		switch(type) {
		case HOUR:
			this.hour = time;
			break;
		case MINUTE:
			this.minute = time;
			break;
		case SECOND:
			this.second = time;
			break;
		default:
			throw new AWTError("Invalid type!!"); // If you enter invalid type
		}
	}
	
	/**
	 * Set time
	 * 
	 * @param hour user hour
	 * @param minute user minute
	 * @param second user second
	 */
	public void setTime(int hour, int minute, int second) {
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}
	
	//----------------------------------------------------------------------------------
	// To string format
	/**
	 * Get time in format 00:00:00
	 */
	@Override
	public String toString() {
		String time = "";
		// If parameters are below 10, add 0 before
		if(hour < 10)
			time += "0";
		time += this.hour + ":";
		if(minute < 10)
			time += "0";
		time += this.minute + ":";
		if(second < 10)
			time += "0";
		time += this.second;
		
		return time;
	}
}