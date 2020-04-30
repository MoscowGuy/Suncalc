package Suncalc;

import java.awt.AWTError;

public class SunTimer extends Sun {
	private static final int FROM_DECIMAL = 0;
	private static final int FROM_NUMBER = 1;
	public static final int SOLAR_NOON = 2;
	public static final int DAY_LONG = 3;
	public static final int SUNRISE = 4;
	public static final int SUNSET = 5;
	public static final int DAWN = 6;
	public static final int NAUTICAL_DAWN = 7;
	public static final int CIVIL_DAWN = 8;
	public static final int DUSK = 9;
	public static final int NAUTICAL_DUSK = 10;
	public static final int CIVIL_DUSK = 11;
	
	private Clock solarNoon;
	private Clock dayLong;
	private Clock sunrise;
	private Clock sunset;
	private Clock dawn;
	private Clock dusk;
	private Clock nauticalDawn;
	private Clock nauticalDusk;
	private Clock civilDawn;
	private Clock civilDusk;
	private boolean polarDay;
	private boolean polarNight;
	private boolean astronomicalTwilight;
	private boolean nauticalTwilight;
	private boolean civilTwilight;
	
	public SunTimer(JulianDay julianDay, Coordinates coords, StarTime starTime, int timeZone) {
		super(julianDay, coords, starTime, timeZone);
		this.polarDay = this.polarNight = this.astronomicalTwilight 
				= this.nauticalTwilight = this.civilTwilight = false;
		calculate();
	}
	
	private void calculate() {
		solarNoonTime();
		dayLong();
		sunriseSunset();
		
		if(super.get(HOUR_ANGLE) >= 12)
			this.polarDay = true;
		if(super.get(HOUR_ANGLE) == 0)
			this.polarNight = true;
		if(super.get(ASTRO_TWILIGHT) >= 12 || super.get(ASTRO_TWILIGHT) <= 0)
			this.astronomicalTwilight = true;
		if(super.get(NAUTICAL_TWILIGHT) >= 12 || super.get(NAUTICAL_TWILIGHT) <= 0)
			this.nauticalTwilight = true;
		if(super.get(CIVIL_TWILIGHT) >= 12 || super.get(CIVIL_TWILIGHT) <= 0)
			this.civilTwilight = true;
	}
	
	public void recalculate(JulianDay julianDay, Coordinates coords, StarTime starTime, int timeZone) {
		super.calculate(julianDay, coords, starTime, timeZone);
		this.polarDay = this.polarNight = this.astronomicalTwilight 
				= this.nauticalTwilight = this.civilTwilight = false;
		calculate();
	}
	
	private Clock toTime(int type, double value) {
		Clock time = new Clock();
		switch(type) {
		case FROM_DECIMAL:
			time.timeFromDecimalPart(value);
			break;
		case FROM_NUMBER:
			time.timeFromNumber(value);
			break;
		default:
			throw new AWTError("Invalid type!!"); // If you enter invalid type
		}
		
		return time;
	}
	
	private void solarNoonTime() {
		double decimalPart = super.get(SOLAR_NOON)%(int)super.get(SOLAR_NOON);
		this.solarNoon = toTime(FROM_DECIMAL, decimalPart);
	}
	
	private void dayLong() {
		double dayLong = super.get(HOUR_ANGLE)*2;
		this.dayLong = toTime(FROM_NUMBER, dayLong);
	}
	
	private void sunriseSunset() {
		double noon = (super.get(SOLAR_NOON)%(int)super.get(SOLAR_NOON))*24;
		double sunrise = noon - super.get(HOUR_ANGLE);
		double sunset = noon + super.get(HOUR_ANGLE);
		double dawn = noon - super.get(ASTRO_TWILIGHT);
		double dusk = noon + super.get(ASTRO_TWILIGHT);
		double nauticalDawn = noon - super.get(NAUTICAL_TWILIGHT);
		double nauticalDusk = noon + super.get(NAUTICAL_TWILIGHT);
		double civilDawn = noon - super.get(CIVIL_TWILIGHT);
		double civilDusk = noon + super.get(CIVIL_TWILIGHT);
		
		this.sunrise = toTime(FROM_NUMBER, sunrise);
		this.sunset = toTime(FROM_NUMBER, sunset);
		this.dawn = toTime(FROM_NUMBER, dawn);
		this.dusk = toTime(FROM_NUMBER, dusk);
		this.nauticalDawn = toTime(FROM_NUMBER, nauticalDawn);
		this.nauticalDusk = toTime(FROM_NUMBER, nauticalDusk);
		this.civilDawn = toTime(FROM_NUMBER, civilDawn);
		this.civilDusk = toTime(FROM_NUMBER, civilDusk);
	}
	
	public Clock getTime(int type) {
		switch(type) {
		case SOLAR_NOON:
			return this.solarNoon;
		case DAY_LONG:
			return this.dayLong;
		case SUNRISE:
			return this.sunrise;
		case SUNSET:
			return this.sunset;
		case DAWN:
			return this.dawn;
		case NAUTICAL_DAWN:
			return this.nauticalDawn;
		case CIVIL_DAWN:
			return this.civilDawn;
		case DUSK:
			return this.dusk;
		case NAUTICAL_DUSK:
			return this.nauticalDusk;
		case CIVIL_DUSK:
			return this.civilDusk;
		default:
			throw new AWTError("Invalid type!!"); // If you enter invalid type
		}
	}
	
	public boolean isPolarDay() {
		return this.polarDay;
	}
	
	public boolean isPolarNight() {
		return this.polarNight;
	}
	
	public boolean isAstronomicalTwilight() {
		return this.astronomicalTwilight;
	}
	
	public boolean isNauticalTwilight() {
		return this.nauticalTwilight;
	}
	
	public boolean isCivilTwilight() {
		return this.civilTwilight;
	}
}