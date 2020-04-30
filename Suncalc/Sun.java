package Suncalc;

import static Suncalc.Vector3.X;
import static Suncalc.Vector3.Y;
import static Suncalc.Vector3.Z;
import static Suncalc.JulianDay.JULIAN_CENTURY;
import java.awt.AWTError;

public class Sun {
	public static final int MEAN_ANOMALY = 0;
	public static final int ECLIPTIC_LONGITUDE = 1;
	public static final int SOLAR_NOON = 2;
	public static final int DECLINATION = 3;
	public static final int ASCENSION = 4;
	public static final int HOUR_ANGLE = 5;
	public static final int ASTRO_TWILIGHT = 6;
	public static final int NAUTICAL_TWILIGHT = 7;
	public static final int CIVIL_TWILIGHT = 8;
	public static final int ALTITUDE = 9;
	
	public static final double EQUATION_EARTH = 1.9148;
	public static final double PERIHELION = 102.9372;
	public static final double EARTH_OBLIQUITY = 23.439281;
	public static final double SUN_REFRACTION = -0.83;
	public static final double TWILIGHT_ANGLE = -18.0;
	
	private double meanAnomaly;
	private double eclipticLongitude;
	private double solarNoonDate;
	private Vector3 solarCoords;
	private Vector3 equatorCoords;
	private double declination;
	private double rightAscension;
	private double hourAngle;
	private double astroTwilight;
	private double nauticalTwilight;
	private double civilTwilight;
	private double altitude;
	
	public Sun(JulianDay julianDay, Coordinates coords, StarTime starTime, int timeZone) {
		this.solarCoords = new Vector3();
		this.equatorCoords = new Vector3();
		calculate(julianDay, coords, starTime, timeZone);
	}
	
	public void calculate(JulianDay julianDay, Coordinates coords, StarTime starTime, int timeZone) {
		// Time zone get in milliseconds, 3 600 000 in hour
		double UTtime = starTime.getUTtime();
		eclipticCoordinates(julianDay.getDateInXXICentury(), coords.getLongitude(), timeZone, UTtime);
		angleToVector();
		calculateEquatorCoordinates();
		this.rightAscension = getRightAscension(this.equatorCoords);
		this.declination = getDeclination(this.equatorCoords);
		calculateSunAngles(coords.getLatitude(), starTime.getStarTime());
	}
	
	private void eclipticCoordinates(double dayInXXICentury, double longitude, int timeZone, double UTtime) {
		double A = 357.5291;
		double B = 35999.05;
		double C = 0.04107;
		double D = 0.0048;
		double E = 0.02;
		double F = 36000.772;
		double G = 0.0053;
		double H = 0.0069;
		
		double julian = dayInXXICentury/JULIAN_CENTURY;
		double meanSolarNoon = dayInXXICentury - longitude/360.0;
		double anomaly = A + B*julian + C*UTtime;
		while(anomaly >=360)
			anomaly -= 360;
		this.meanAnomaly = anomaly;
		
		double center = (EQUATION_EARTH - D*julian)*Math.sin(Math.toRadians(meanAnomaly)) +
				E*Math.sin(Math.toRadians(meanAnomaly*2));
		double lyambda = 177.52 + PERIHELION + C*UTtime + F*julian + center;
		while(lyambda >= 360)
			lyambda -= 360;
		this.eclipticLongitude = lyambda;
		
		this.solarNoonDate = 2451545.5 + meanSolarNoon + G*Math.sin(Math.toRadians(meanAnomaly)) -
				H*Math.sin(2*Math.toRadians(eclipticLongitude)) + timeZone/24.0;
	}
	
	private void angleToVector() {
		double angle = this.eclipticLongitude;
		this.solarCoords.setVector(Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle)), 0);
	}
	
	private void calculateEquatorCoordinates() {
		double x = this.solarCoords.get(X);
		double y = this.solarCoords.get(Y);
		double z = this.solarCoords.get(Z);
		this.equatorCoords.setVector(x,
						y*Math.cos(Math.toRadians(EARTH_OBLIQUITY) - z*Math.sin(Math.toRadians(EARTH_OBLIQUITY))),
						y*Math.sin(Math.toRadians(EARTH_OBLIQUITY) + z*Math.cos(Math.toRadians(EARTH_OBLIQUITY))));
	}
	
	private double angle(double constant, double latitude) {
		double angle = (Math.sin(Math.toRadians(constant))-(Math.sin(Math.toRadians(latitude)))
				*Math.sin(Math.toRadians(this.declination)))/(Math.cos(Math.toRadians(latitude))
				*Math.cos(Math.toRadians(this.declination)));
		double speedAngle = 15;
		if(angle >= -1 && angle <= 1)
			return (Math.acos(angle)*1/Math.toRadians(speedAngle));
		else if(angle < -1)
			return 12.0;
		else
			return 0.0;
	}
	
	private void calculateSunAngles(double latitude, double starTime) {
		this.hourAngle = angle(SUN_REFRACTION, latitude);
		this.astroTwilight = angle(TWILIGHT_ANGLE, latitude);
		this.nauticalTwilight = angle(TWILIGHT_ANGLE + 6.0, latitude);
		this.civilTwilight = angle(TWILIGHT_ANGLE + 12.0, latitude);
		
		double th = starTime - this.rightAscension;
		double H = Math.acos(Math.sin(Math.toRadians(latitude))*Math.sin(Math.toRadians(this.declination)) +
				Math.cos(Math.toRadians(latitude))*Math.cos(Math.toRadians(this.declination))*
				Math.cos(Math.toRadians(th)))*180/Math.PI;
		this.altitude = 90 - H;
	}
	
	public double getRightAscension(Vector3 equatorCoords) {
		return Math.atan2(equatorCoords.get(Y), equatorCoords.get(X))*180/Math.PI;
	}
	
	public double getDeclination(Vector3 equatorCoords) {
		return Math.atan2(equatorCoords.get(Z),
				Math.sqrt(Math.pow(equatorCoords.get(X), 2) + Math.pow(equatorCoords.get(Y), 2)))*180/Math.PI;
	}
	
	public double get(int type) {
		switch(type) {
		case MEAN_ANOMALY:
			return this.meanAnomaly;
		case ECLIPTIC_LONGITUDE:
			return this.eclipticLongitude;
		case SOLAR_NOON:
			return this.solarNoonDate;
		case DECLINATION:
			return this.declination;
		case ASCENSION:
			return this.rightAscension;
		case HOUR_ANGLE:
			return this.hourAngle;
		case ASTRO_TWILIGHT:
			return this.astroTwilight;
		case NAUTICAL_TWILIGHT:
			return this.nauticalTwilight;
		case CIVIL_TWILIGHT:
			return this.civilTwilight;
		case ALTITUDE:
			return this.altitude;
		default:
			throw new AWTError("Invalid type!!"); // If you enter invalid type
		}
	}
	
	public double getAzimuthAnomaly() {
		return (this.solarNoonDate%(int)this.solarNoonDate - 0.5);
	}
	
	public Vector3 getSolarCoordinates() {
		return this.solarCoords;
	}
	
	public Vector3 getEquatorCoordinates() {
		return this.equatorCoords;
	}
}