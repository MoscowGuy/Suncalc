package Suncalc;

import java.util.GregorianCalendar;
import static java.util.GregorianCalendar.HOUR_OF_DAY;
import static java.util.GregorianCalendar.MINUTE;
import static java.util.GregorianCalendar.SECOND;
import static java.util.GregorianCalendar.YEAR;
import static java.util.GregorianCalendar.MONTH;
import static java.util.GregorianCalendar.DATE;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.TimeZone;

public class Data {
	private Place place;
	private GregorianCalendar calendar, calendar12h;
	private Clock currentTime;
	private JulianDay julianDay;
	private StarTime starTime, starTime12h;
	private SunTimer sun, sun12h;
	
	public Data(Place place) {
		this.place = place;
		this.calendar = new GregorianCalendar();
		this.calendar12h = new GregorianCalendar();
		this.currentTime = new Clock();
		timeToCalendar(12, 0, 0, this.calendar12h);
		getTimeFromCalendar();
		this.julianDay = new JulianDay(calendar);
		this.starTime = new StarTime(julianDay, calendar, place.getCoordinates(), place.getTimeZone());
		this.starTime12h = new StarTime(julianDay, calendar12h, place.getCoordinates(), place.getTimeZone());
		this.sun = new SunTimer(julianDay, place.getCoordinates(), starTime, place.getTimeZone());
		this.sun12h = new SunTimer(julianDay, place.getCoordinates(), starTime12h, place.getTimeZone());
	}
	
	private void getTimeFromCalendar() {
		int h = this.calendar.get(HOUR_OF_DAY);
		int m = this.calendar.get(MINUTE);
		int s = this.calendar.get(SECOND);
		this.currentTime.setTime(h, m, s);
	}
	
	public void timeToCalendar(int hour, int minute, int sec, GregorianCalendar calendar) {
		calendar.set(HOUR_OF_DAY, hour);
		calendar.set(MINUTE, minute);
		calendar.set(SECOND, sec);
	}
	
	public void timeToCalendar() {
		int h = this.currentTime.get(Clock.HOUR);
		int m = this.currentTime.get(Clock.MINUTE);
		int s = this.currentTime.get(Clock.SECOND);
		this.calendar.set(HOUR_OF_DAY, h);
		this.calendar.set(MINUTE, m);
		this.calendar.set(SECOND, s);
	}
	
	public void recalculate() {
		synhronizeCalendar(this.calendar12h, this.calendar);
		timeToCalendar(12, 0, 0, this.calendar12h);
		this.julianDay.setCalendar(calendar);
		this.starTime.recalculate(julianDay, calendar, place.getCoordinates(), place.getTimeZone());
		this.starTime12h.recalculate(julianDay, calendar12h, place.getCoordinates(), place.getTimeZone());
		this.sun.recalculate(julianDay, place.getCoordinates(), starTime, place.getTimeZone());
		this.sun12h.recalculate(julianDay, place.getCoordinates(), starTime12h, place.getTimeZone());
	}
	
	private double round(double value, int fraction) {
		BigDecimal bd = new BigDecimal(value).setScale(fraction, RoundingMode.DOWN);
		double number = bd.doubleValue();	
		return number;
	}
	
	public void synhronizeCalendar(GregorianCalendar a, GregorianCalendar b) {
		int year = b.get(YEAR);
		int month = b.get(MONTH);
		int day = b.get(DATE);
		int hour = b.get(HOUR_OF_DAY);
		int minute = b.get(MINUTE);
		int second = b.get(SECOND);
		TimeZone timeZone = b.getTimeZone();
		a.set(year, month, day, hour, minute, second);
		a.setTimeZone(timeZone);
	}
	
	public Place getPlace() {
		return this.place;
	}
	
	public Coordinates getPlaceCoordinates() {
		return this.place.getCoordinates();
	}
	
	public GregorianCalendar getCalendar() {
		return this.calendar;
	}
	
	public int getTimeZone() {
		return (this.calendar.getTimeZone().getRawOffset()/3600000);
	}
	
	public Clock getTime() {
		return this.currentTime;
	}
	
	public JulianDay getJulianDay() {
		return this.julianDay;
	}
	
	public double getJulianDay(GregorianCalendar calendar, int roundFraction) {
		double value = this.julianDay.toJulian(calendar, true);
		return round(value, roundFraction);
	}
	
	public double getModifiedJulianDay(GregorianCalendar calendar, int roundFraction) {
		double value = this.julianDay.toJulian(calendar, true);
		value = this.julianDay.convertTo(JulianDay.MJD, value);
		return round(value, roundFraction);
	}
	
	public StarTime getStarTime() {
		return this.starTime;
	}
	
	public Clock getStarTimeClock() {
		return this.starTime.convertToTime();
	}
	
	public double getSiderialTime(GregorianCalendar calendar, int roundFraction) {
		double julian = this.julianDay.toJulian(calendar, true);
		double value = this.starTime.toSidereal(julian);
		return round(value, roundFraction);
	}
	
	public SunTimer getSun() {
		return this.sun12h;
	}
	
	public Clock getSunTime(int type) {
		return this.sun12h.getTime(type);
	}
	
	public double getSunParameter(int type, int roundFraction) {
		double value = this.sun.get(type);
		return round(value, roundFraction);
	}
	
	public double getMaxSunAltitude(int roundFraction) {
		double value = 90 - this.place.getCoordinates().getLatitude() + this.sun12h.get(Sun.DECLINATION);
		if(value > 90)
			value = 180 - value;
		return round(value, roundFraction);
	}
}