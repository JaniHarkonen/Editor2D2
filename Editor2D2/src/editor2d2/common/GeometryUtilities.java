package editor2d2.common;

import java.awt.Point;

/**
 * This is a utility class that provides some basic 
 * geometry utility methods mostly to do with 
 * trigonometry.
 * 
 * This is a final class and is not to be extended 
 * as it only contains - and should only contain - 
 * static methods.
 * 
 * @author User
 *
 */
public final class GeometryUtilities {
	
		// Do not instantiate
	private GeometryUtilities() { }
	
	/**
	 * Converts degrees to radians.
	 * 
	 * @param ang Degrees to convert.
	 * 
	 * @return Returns radians.
	 */
	public static double degToRad(double ang) {
		return ang / (180.0d / Math.PI);
	}
	
	/**
	 * Converts radians to degrees.
	 * 
	 * @param rad Radians to convert.
	 * 
	 * @return Returns degrees.
	 */
	public static double radToDeg(double rad) {
		return rad * (180.0d / Math.PI);
	}
	
	/**
	 * Wraps an angle of any size (in degrees), 
	 * positive or negative, to be betweeen 0 and 
	 * 359 degrees.
	 * 
	 * @param ang Angle (in degrees) to wrap.
	 * 
	 * @return Returns an angle (in degrees) 
	 * between 0 and 359.
	 */
	public static double angleWrap(double ang) {
		if( ang >= 0 && ang <= 359 ) return ang;
		if( ang > -360 ) return ang + 360;
		if( ang == 360 ) return 0;
		
		return (ang < 0.0d) ? ang % 360 + 360 : ang % 360; 
	}
	
	/**
	 * Calculates the distance between two points 
	 * using Pythagora's theorem.
	 * 
	 * @param x1 X-coorindate of the first point.
	 * @param y1 Y-coordinate of the first point.
	 * @param x2 X-coordinate of the second point. 
	 * @param y2 Y-coordinate of the second point.
	 * 
	 * @return Returns the distance between the two 
	 * points.
	 */
	public static double distanceBetweenPoints(double x1, double y1, double x2, double y2) {
		double dx = x2 - x1;
		double dy = y2 - y1;
		
		return Math.sqrt(dx*dx + dy*dy);
	}

	/**
	 * Returns a Point representing the point that is 
	 * a given distance away from a starting point at 
	 * a given point.
	 * 
	 * This method returns a Point.Double instance.
	 * 
	 * @param x X-coordinate of a starting point.
	 * @param y Y-coordinate of a starting point.
	 * @param dist Distance of the end point from the 
	 * starting point.
	 * @param ang Angle at which the end point is from 
	 * the starting point.
	 * 
	 * @return Returns the end point.
	 */
	public static Point.Double pointAtDistance(double x, double y, double dist, double ang) {
		ang = angleWrap(ang);
		return new Point.Double(x + Math.cos(degToRad(ang)) * dist, y + Math.sin(degToRad(ang)) * dist);
	}
	
	/**
	 * Returns the angle (in degrees) between two points.
	 * 
	 * @param x1 X-coordinate of the first point.
	 * @param y1 Y-coordinate of the first point.
	 * @param x2 X-coordinate of the second point.
	 * @param y2 Y-coordinate of the second point.
	 * 
	 * @return Returns the angle (in degrees) between 
	 * the two points.
	 */
	public static double angleBetweenPoints(double x1, double y1, double x2, double y2) {
		return angleWrap(radToDeg(Math.atan2(y2 - y1, x2 - x1)));
	}
}
