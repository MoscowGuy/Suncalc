package Suncalc;

import java.util.*;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;

import java.awt.*;

/**
 * Class of Julian days for astronomy. Convert data from/to Gregorian calendar. 
 * Includes typical or modified Julian days.
 * Use Gregorian calendar from java.util
 * 
 * @author Igor
 */
public class JulianDay {
	// Type constants
	/**
	 * Type of Julian day format. Starts since 1 jan 4713 bc 12:00
	 */
	public static final int JULIAN_DAY = 0;
	
	/**
	 * Type of modified Julian Day format. Starts since 17 nov 1858 0:00. Using in
	 * astronomy
	 */
	public static final int MJD = 1;
	
	//----------------------------------------------------------------------------------
	// Constants
	/**
	 * Constant for MJD, number of days from 1 jan -4713 12:00 to 17 nov 1858 0:00
	 */
	public static final double MJD_POINT = 2400000.5;
	
	/**
	 * Modified Julian date on 1 jan 2000
	 */
	public static final double XXI_CENTURY = 51544.5;
	
	/**
	 * Number of days in Julian century (Julian year is 365,25)
	 */
	public static final double JULIAN_CENTURY = 36525;

	//----------------------------------------------------------------------------------
	// Variables
	private double julianDay;
	private double modifiedJulianDay;

	//----------------------------------------------------------------------------------
	// Constructors
	/**
	 * Empty calendar constructor. Julian day is default 0.0, equals in Gregorian
	 * calendar 1 jan -4713 bc 12:00
	 */
	public JulianDay() {
		julianDay = 0.0;
		modifiedJulianDay = convertTo(MJD, 0.0); // Get modified Julian day
	}

	/**
	 * Calendar constructor
	 * 
	 * @param julianDay known data in Julian days
	 */
	public JulianDay(double julianDay) {
		this.julianDay = julianDay;
		modifiedJulianDay = convertTo(MJD, julianDay); // Get modified Julian day
	}

	/**
	 * Calendar constructor. You can choose type of enter data
	 * 
	 * @param type JULIAN_DAY or MJD
	 * @param day known data
	 */
	public JulianDay(int type, double day) {
		switch (type) {
		case JULIAN_DAY:
			julianDay = day;
			// If you enter data in Julian days, convert to MJD
			modifiedJulianDay = convertTo(MJD, day);
			break;
		case MJD:
			modifiedJulianDay = day;
			// If you enter data in modified Julian days, convert to JD
			julianDay = convertTo(JULIAN_DAY, day);
			break;
		default:
			throw new AWTError("Invalid type!!"); // If you enter invalid type
		}
	}
	
	/**
	 * Calendar constructor. You can enter Gregorian date
	 * 
	 * @param calendar Gregorian calendar
	 */
	public JulianDay(GregorianCalendar calendar) {
		julianDay = toJulian(calendar, false);
		modifiedJulianDay = convertTo(MJD, julianDay); // Get modified Julian day
	}
	
	//----------------------------------------------------------------------------------
	// Methods
	/**
	 * Convert to... You can choose type of data and enter days
	 * 
	 * @param toType JULIAN_DAY or MJD
	 * @param day known data
	 * @return converted days to chosen type
	 */
	public double convertTo(int toType, double day) {
		switch (toType) {
		case JULIAN_DAY:
			return day + MJD_POINT;
		case MJD:
			return day - MJD_POINT;
		default:
			throw new AWTError("Invalid type!!"); // If you enter invalid type
		}
	}
	
	/**
	 * Convertation from Gregorian calendar to Julian days
	 * 
	 * @param year year
	 * @param month month
	 * @param day day
	 * @return Julian days
	 */
	public double toJulian(GregorianCalendar calendar, boolean withTime) {
		// Take parameters from calendar
		int year = calendar.get(YEAR);
		int month = calendar.get(MONTH) + 1;
		int day = calendar.get(DATE);
		// Algorithm of convertation
		double A, B, C;
		A = 10000.0*year + 100.0*month + day; // Date to string format (10 oct 2020 = 20201010)
		// January and February go to 13/14 month of year
		if(month <= 2) {
			month += 12;
			year --;
		}
		
		// Before 5 oct 1582 - use only Julian calendar
		if(A <= 15821004)
			B = -2 + Math.floor((year + 4716)/4.0) - 1179; // Julian algorithm
		else
			B = Math.floor(year/400.0) - Math.floor(year/100.0) + Math.floor(year/4.0); // Gregorian algorithm
		C = 365*year + 1720996.5;
		
		double value = C + B + Math.floor(30.6001*(month + 1)) + day;
		
		if(!withTime)
			return value - 0.5;
		
		int hour = calendar.get(HOUR_OF_DAY);
		int minute = calendar.get(MINUTE);
		int second = calendar.get(SECOND);
		int time = second + minute*60 + hour*3600;
		double timeInDec = time/86400.0;
		
		return (value + timeInDec);
	}
	
	
	/**
	 * Convertation from Julian days to Gregorian calendar
	 * 
	 * @return Gregorian calendar
	 */
	public GregorianCalendar toGregorian() {
		// Algorithm of convertation (wikipedia.org)
		double A, B, C, D;
		A = this.julianDay + 1401 + (int)(((int)((4*this.julianDay + 274277)/146097)*3)/4) - 38;
		B = 4*A + 3;
		C = (int)((B%1461)/4);
		D = 5*C + 2;
		
		int day = (int)((D%153)/5) + 1;
		int month = (int)(((int)(D/153) + 2)%12 + 1);
		int year = (int)(B/1461) - 4716 + (int)((12 + 2 - month)/12);
		// Julian day starts at 12:00:00, default time
		GregorianCalendar calendar = new GregorianCalendar(year, month, day, 12, 0, 0);
		
		return calendar;
	}
	
	//----------------------------------------------------------------------------------
	// Get/set methods
	/**
	 * Get Julian days
	 * 
	 * @return variable julianDay
	 */
	public double getJulianDay() {
		return this.julianDay;
	}
	
	/**
	 * Get Julian days in modified format
	 * 
	 * @return variable modifiedJulianDay
	 */
	public double getMJD() {
		return this.modifiedJulianDay;
	}
	
	/**
	 * Get modified date since 1 jan 2020
	 * 
	 * @return modified Julian days in 21 century
	 */
	public double getDateInXXICentury() {
		return (modifiedJulianDay - XXI_CENTURY);
	}
	
	//----------------------------------------------------------------------------------
	/**
	 * Set Julian day
	 * 
	 * @param julianDay Julian day
	 */
	public void setJulianDay(double julianDay) {
		this.julianDay = julianDay;
		this.modifiedJulianDay = convertTo(MJD, julianDay); // Get modified Julian day
	}
	
	/**
	 * Set Julian day
	 * 
	 * @param modifiedJulianDay modified Julian day
	 */
	public void setMJD(double modifiedJulianDay) {
		this.modifiedJulianDay = modifiedJulianDay;
		this.julianDay = convertTo(JULIAN_DAY, modifiedJulianDay); // Get Julian day
	}
	
	/**
	 * Set Julian day by type
	 * 
	 * @param typeOfData JULIAN_DAY or MJD
	 * @param day known data
	 */
	public void set(int typeOfData, double day) {
		switch(typeOfData) {
		case JULIAN_DAY:
			julianDay = day;
			modifiedJulianDay = convertTo(MJD, day);
			break;
		case MJD:
			modifiedJulianDay = day;
			julianDay = convertTo(JULIAN_DAY, day);
			break;
		default:
			throw new AWTError("Invalid type!!"); // If you enter invalid type
		}
	}
	
	public void setCalendar(GregorianCalendar calendar) {
		this.julianDay = toJulian(calendar, false);
		modifiedJulianDay = convertTo(MJD, julianDay); // Get modified Julian day
	}
}
