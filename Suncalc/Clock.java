package Suncalc;

import java.awt.*;
import java.util.GregorianCalendar;

/**
 * Class of time. You can choose 24h or 12h format.
 * Unlike Time class, the format of time checked every time methods are called.
 * 
 * @author Ibra_san
 */
public class Clock extends Time{
	// Variables
	private boolean shortTimeFormat; // 12h format if true
	private boolean am; // am or pm, work if shortTimeFormat is true
	
	//----------------------------------------------------------------------------------
	// Constructors
	/**
	 * Empty clock constructor. Default time 00:00:00
	 */
	public Clock() {
		super();
		this.shortTimeFormat = this.am = false;
		checkFormat(); // Check 24h time format
	}
	
	/**
	 * Clock constructor with format choice. Default time 00:00:00 
	 * 
	 * @param is12hFormat choose format (12h if its true)
	 */
	public Clock(boolean is12hFormat) {
		super();
		this.shortTimeFormat = is12hFormat;
		this.am = false;
		checkFormat(); // Check 24h or 12h time format
	}
	
	/**
	 * Clock constructor
	 * 
	 * @param hour known hours
	 * @param minute known minutes
	 * @param second known seconds
	 */
	public Clock(int hour, int minute, int second) {
		super(hour, minute, second);
		this.shortTimeFormat = this.am = false;
		checkFormat(); // Check 24h time format
	}
	
	/**
	 * Clock constructor with format choice.
	 * 
	 * @param hour known hours
	 * @param minute known minutes
	 * @param second known seconds
	 * @param is12hFormat choose format (12h if its true)
	 */
	public Clock(int hour, int minute, int second, boolean is12hFormat) {
		super(hour, minute, second);
		this.shortTimeFormat = is12hFormat;
		this.am = false;
		checkFormat(); // Check 24h or 12h time format
	}
	
	//----------------------------------------------------------------------------------
	// Methods
	/**
	 * Checking format of time (12h or 24h). Convert to am/pm and back
	 */
	private void checkFormat() {
		int hour = super.get(HOUR);
		while(hour >= 24)
			hour -= 24;
		// If > 12:00:00 its p.m
		if(this.shortTimeFormat & hour >= 12) {
			hour -= 12;
			this.am = false;
		}
		// If < 12:00:00 its a.m
		else if(this.shortTimeFormat & hour < 12) {
			this.am = true;
		}
		else if(!this.shortTimeFormat && this.am) {
			hour += 12;
			this.am = false;
		}
		
		super.set(HOUR, hour);
	}
	
	/**
	 * Take time from decimal part of number (0.5 to 24 hour format (12:00:00))
	 * 
	 * @param value value that you want to transform into time format
	 */
	@Override
	public void timeFromDecimalPart(double value) {
		super.timeFromDecimalPart(value);
		checkFormat();
	}
	
	/**
	 * Take time from number (16.50 to 16:30:00)
	 * 
	 * @param value value that you want to transform into time format
	 */
	@Override
	public void timeFromNumber(double value) {
		super.timeFromNumber(value);
		checkFormat();
	}
	
	/**
	 * Take time from angle (90 degrees to 06:00:00)
	 * 
	 * @param angle angle that you want to transform into time format
	 */
	@Override
	public void timeFromAngle(double angle) {
		super.timeFromAngle(angle);
		checkFormat();
	}
	
	/**
	 * Take time from Julian day (decimal part (17334.0 is 12:00:00))
	 * 
	 * @param day known Julian date
	 */
	@Override
	public void timeFromJulianDay(double date) {
		super.timeFromJulianDay(date);
		checkFormat();
	}
	
	/**
	 * Take time from seconds format
	 * 
	 * @param time known time in seconds
	 */
	@Override
	public void timeFromSeconds(int time) {
		super.timeFromSeconds(time);
		checkFormat();
	}
	
	//----------------------------------------------------------------------------------
	// Get/set methods
	/**
	 * Get parameter by type and choose time format
	 * 
	 * @param type HOUR, MINUTE or SECOND
	 * @param shortTimeFormat choose format (12h if its true)
	 * @return variable hour, minute or second
	 */
	public int get(int type, boolean shortTimeFormat) {
		switch(type) {
		case HOUR:
			if(!this.shortTimeFormat && !this.am)
				return super.get(HOUR) + 12;
			if(shortTimeFormat && super.get(HOUR) >= 12)
				return super.get(HOUR) - 12;
			return super.get(HOUR);
		case MINUTE:
			return super.get(MINUTE);
		case SECOND:
			return super.get(SECOND);
		default:
			throw new AWTError("Invalid type!!"); // If you enter invalid type	
		}
	}
	
	/**
	 * Get boolean of time format
	 * 
	 * @return true if its 12h format
	 */
	public boolean isShortTimeFormat() {
		return this.shortTimeFormat;
	}
	
	/**
	 * Get boolean of morning or evening time
	 * 
	 * @return true if its morning time
	 */
	public boolean isAm() {
		return this.am;
	}
	
	//----------------------------------------------------------------------------------
	/**
	 * Set format of time, check and convert time to format
	 * 
	 * @param isShortTimeFormat true if you want 12h format
	 */
	public void setShortTimeFormat(boolean isShortTimeFormat) {
		this.shortTimeFormat = isShortTimeFormat;
		checkFormat(); // Check 24h or 12h time format
	}
	
	/**
	 * Set type of time
	 * 
	 * @param isAm true if you want to convert time into morning time
	 */
	public void setAm(boolean isAm) {
		this.am = isAm;
		checkFormat(); // Check 24h or 12h time format
	}
	
	/**
	 * Set time parameter by type
	 * 
	 * @param type HOUR, MINUTE or SECOND
	 * @param time known time parameter
	 */
	@Override
	public void set(int type, int time) {
		super.set(type, time);
		checkFormat(); // Check 24h or 12h time format
	}
	
	/**
	 * Set time
	 * 
	 * @param hour user hour
	 * @param minute user minute
	 * @param second user second
	 */
	@Override
	public void setTime(int hour, int minute, int second) {
		super.setTime(hour, minute, second);
		checkFormat(); // Check 24h or 12h time format
	}
	
	public void setTime(GregorianCalendar calendar) {
		int h = calendar.get(GregorianCalendar.HOUR_OF_DAY);
		int m = calendar.get(GregorianCalendar.MINUTE);
		int s = calendar.get(GregorianCalendar.SECOND);
		super.setTime(h, m, s);
		checkFormat(); // Check 24h or 12h time format
	}
	
	//----------------------------------------------------------------------------------
	// To string format
	/**
	 * Get time in format 00:00:00 or 00:00:00 a.m (p.m)
	 */
	@Override
	public String toString() {
		String time = super.toString();
		if(this.shortTimeFormat) {
			if(this.am)
				time += " a.m";
			else
				time += " p.m";
		}
		
		return time;
	}
}