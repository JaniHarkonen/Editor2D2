package editor2d2.common.dragbox;

/**
 * Container class for a result of a DragBox poll.
 * @author User
 *
 */
public class DragBoxPoll {

	/**
	 * X-coordinate of the draggable element at the time of the poll.
	 */
	public double x;
	
	/**
	 * Y-coordinate of the draggable element at the time of the poll.
	 */
	public double y;
	
	/**
	 * Constructs a DragBox poll result.
	 * @param x X-coordinate of the draggable element.
	 * @param y Y-coordinate of the draggable element.
	 */
	public DragBoxPoll(double x, double y) {
		this.x = x;
		this.y = y;
	}
}
