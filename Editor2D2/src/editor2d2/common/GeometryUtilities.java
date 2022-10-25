package editor2d2.common;

import java.awt.Point;

public final class GeometryUtilities {
	
		// Do not instantiate
	private GeometryUtilities() { }
	
		// Converts degrees to radians
	public static double degToRad(double ang) {
		return ang / (180 / Math.PI);
	}
	
		// Converts radians to degrees
	public static double radToDeg(double rad) {
		return rad * (180 / Math.PI);
	}
	
		// Clamps an angle between 0 and 359 degrees
	public static double angleWrap(double ang) {
		if( ang >= 0 && ang <= 359 ) return ang;
		if( ang > -360 ) return ang + 360;
		if( ang == 360 ) return 0;
		
		return (ang < 0.0d) ? ang % 360 + 360 : ang % 360; 
	}
	
		// Calculates the distance between two points using Pythagora's
		// theorem
	public static double distanceBetweenPoints(double x1, double y1, double x2, double y2) {
		double dx = x2 - x1;
		double dy = y2 - y1;
		
		return Math.sqrt(dx*dx + dy*dy);
	}

		// Calculates the X- and Y-coordinates of a point that is a given distance
		// away from a given point at a given direction
	public static Point.Double pointAtDistance(double x, double y, double dist, double ang) {
		ang = angleWrap(ang);
		return new Point.Double(x + Math.cos(degToRad(ang)) * dist, y + Math.sin(degToRad(ang)) * dist);
	}
	
		// Calculates the angle (in degrees) between two points
	public static double angleBetweenPoints(double x1, double y1, double x2, double y2) {
		return angleWrap(radToDeg(Math.atan2(y2 - y1, x2 - x1)));
	}
}
