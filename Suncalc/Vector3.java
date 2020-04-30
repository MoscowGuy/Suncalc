package Suncalc;

import java.awt.*;

/**
 * Class of coordinates vector in 3 dimensions (x, y, z)
 * 
 * @author Igor
 */
public class Vector3 {
	// Type constants
	/**
	 * Type of x dimension
	 */
	public static final int X = 0;
	
	/**
	 * Type of y dimension
	 */
	public static final int Y = 1;
	
	/**
	 * Type of z dimension
	 */
	public static final int Z = 2;
	
	//----------------------------------------------------------------------------------
	// Variables
	private double x;
	private double y;
	private double z;
	
	//----------------------------------------------------------------------------------
	// Constructors
	/**
	 * Empty constructor, all parameters are 0
	 */
	public Vector3() {
		this.x = this.y = this.z = 0.0;
	}
	
	/**
	 * Constructor of coordinates vector
	 * 
	 * @param x known x coordinate
	 * @param y known y coordinate
	 * @param z known z coordinate
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	//----------------------------------------------------------------------------------
	// Get/set methods
	/**
	 * Get coordinate by type
	 * 
	 * @param type X, Y or Z dimensions
	 * @return variables x, y or z
	 */
	public double get(int type) {
		switch(type) {
		case X:
			return this.x;
		case Y:
			return this.y;
		case Z:
			return this.z;
		default:
			throw new AWTError("Invalid type!!"); // If you enter invalid type
		}
	}
	
	//----------------------------------------------------------------------------------
	/**
	 * Set coordinate by type, unchangeable another dimensions
	 * 
	 * @param type X, Y or Z
	 * @param var known coordinate
	 */
	public void set(int type, double var) {
		switch(type) {
		case X:
			this.x = var;
			break;
		case Y:
			this.y = var;
			break;
		case Z:
			this.z = var;
			break;
		default:
			throw new AWTError("Invalid type!!"); // If you enter invalid type
		}
	}
	
	/**
	 * Set all known coordinates in vector
	 * 
	 * @param x known x coordinate
	 * @param y known y coordinate
	 * @param z known z coordinate
	 */
	public void setVector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	//----------------------------------------------------------------------------------
	// To string format
	/**
	 * Get vector in format { x , y , z }
	 */
	@Override
	public String toString() {
		String coords = "{ " + this.x + " , " + this.y + " , " + this.z + " }";
		return coords;
	}
}