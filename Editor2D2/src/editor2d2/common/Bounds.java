package editor2d2.common;

/**
 * A container class that represents the left, top, right and
 * bottom edges of an area. This class uses Doubles to
 * represent the coordinates of the edges of the area.
 * @author User
 *
 */
public class Bounds {

	/**
	 * X-coordinate of the left edge of the area.
	 */
	public double left;
	
	/**
	 * Y-coordinate of the top edge of the area.
	 */
	public double top;
	
	/**
	 * X-coordinate of the right edge of the area.
	 */
	public double right;
	
	/**
	 * Y-coordinate of the bottom edge of the area.
	 */
	public double bottom;
	
	
	/**
	 * Constructs the Bounds for an area with given edges.
	 * @param left X-coordinate of the left edge of the area.
	 * @param top Y-coordinate of the top edge of the area.
	 * @param right X-coordinate of the right edge of the area.
	 * @param bottom Y-coordinate of the bottom edge of the
	 * area.
	 */
	public Bounds(double left, double top, double right, double bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	
	/**
	 * Constructs the Bounds for an unspecified area.<br />
	 * By default the edges will be set at (0, 0, 0, 0).
	 */
	public Bounds() {
		this(0, 0, 0, 0);
	}
	
	@Override
	public String toString() {
        return "Bounds(" + this.left + ", " + this.top + ", " + this.right + ", " + this.bottom + ")";
    }
}
