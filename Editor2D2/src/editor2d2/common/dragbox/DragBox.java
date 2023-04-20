package editor2d2.common.dragbox;

/**
 * This class contains the functionalities required to create
 * an element that can be dragged via mouse. DragBox calculates
 * the change in position since the dragging began which the
 * user can then utilize when updating the position of the element,
 * for example, on screen.
 * @author User
 *
 */
public class DragBox {

	/**
	 * X-coordinate of the draggable element.
	 */
	private double x;
	
	/**
	 * Y-coordinate of the draggable element.
	 */
	private double y;
	
	/**
	 * X-coordinate of the element when it was last polled.
	 */
	private double previousX;
	
	/**
	 * Y-coordinate of the element when it was last polled.
	 */
	private double previousY;
	
	/**
	 * Width of the draggable element.
	 */
	private double width;
	
	/**
	 * Height of the draggable element.
	 */
	private double height;
	
	/**
	 * X-coordinate of the point on the element that was clicked when
	 * the dragging began.
	 */
	private double anchorX;
	
	/**
	 * Y-coordinate of the point on the element that was clicked when
	 * the dragging began.
	 */
	private double anchorY;
	
	/**
	 * Whether the element is being dragged currently.
	 */
	private boolean isDragged;
	
	/**
	 * Constructs a DragBox with a given position and dimensions.
	 * @param x X-coordinate of the draggable element.
	 * @param y Y-coordinate of the draggable element.
	 * @param width Width of the draggable element.
	 * @param height of the draggable element.
	 */
	public DragBox(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.anchorX = 0;
		this.anchorY = 0;
		this.previousX = 0;
		this.previousY = 0;
		this.isDragged = false;
	}
	
	/********************************************************************************/
	
	/**
	 * Starts dragging the element if the given pointer coordinates lie within the
	 * element. A factor will be used to apply a transform (use 1.0 for no transform).
	 * @param px X-coordinate of the pointer.
	 * @param py Y-coordinate of the pointer.
	 * @param factor Transform factor.
	 */
	public void startDragging(double px, double py, double factor) {
		if( this.isDragged == true ) return;
		
		double f_x = this.x * factor;
		double f_y = this.y * factor;
		double f_w = this.width * factor;
		double f_h = this.height * factor;
		
		if( px < f_x || py < f_y || px > f_x + f_w || py > f_y + f_h ) return;
		
		this.anchorX = (px - f_x) / factor;
		this.anchorY = (py - f_y) / factor;
		
		this.previousX = x;
		this.previousY = y;
		
		this.isDragged = true;
	}
	
	/**
	 * Polls the DragBox and returns the current position change from the starting
	 * point of the drag. A factor will be used to apply a transform (use 1.0 for
	 * no transform).
	 * @param px Current X-coordinate of the pointer dragging the element.
	 * @param py Current Y-coordindate of the pointer dragging the element.
	 * @param factor Transform factor.
	 * 
	 * @return Returns a reference to the DragBoxPoll-instance that contains the
	 * drag result.
	 */
	public DragBoxPoll poll(double px, double py, double factor) {
		if( isDragged == false ) return null;
		
		x = px - anchorX;
		y = py - anchorY;
		
		DragBoxPoll dbp = new DragBoxPoll(x - previousX, y - previousY);
		
		previousX = x;
		previousY = y;
		
		return dbp;
	}
	
	/**
	 * Stops dragging the draggable element.
	 */
	public void stopDragging() {
		isDragged = false;
	}
	
	/***************************** GETTERS & SETTERS ********************************/
	
	/**
	 * Returns the X-coordinate of the draggable element.
	 * @return X-coordinate of the element.
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Returns the Y-coordinate of the draggable element.
	 * @return Y-coordinate of the element.
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Sets the position of the draggable element.
	 * @param x New X-coordinate of the element.
	 * @param y New Y-coordinate of the element.
	 */
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the current width of the draggable element.
	 * @return Width of the element.
	 */
	public double getWidth() {
		return width;
	}
	
	/**
	 * Returns the current height of the draggable element.
	 * @return Height of the element.
	 */
	public double getHeight() {
		return height;
	}
	
	/**
	 * Sets the dimensions of the draggable element.
	 * @param width New width of the element.
	 * @param height New height of the element.
	 */
	public void setDimensions(double width, double height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Returns the X-coordinate of the anchor point the draggable
	 * element was dragged from last.
	 * 
	 * (Anchor point is the point on the draggable element that was
	 * clicked by the pointer to initiate a drag.)
	 * 
	 * @return X-coordinate of the anchor point.
	 */
	public double getXAnchor() {
		return anchorX;
	}
	
	/**
	 * Returns the Y-coordinate of the anchor point the draggable
	 * element was dragged from last.
	 * 
	 * (Anchor point is the point on the draggable element that was
	 * clicked by the pointer to initiate a drag.)
	 * 
	 * @return Y-coordinate of the anchor point.
	 */
	public double getYAnchor() {
		return anchorY;
	}
	
	/**
	 * Returns whether the draggable element is being dragged.
	 * @return Whether the element is being dragged.
	 */
	public boolean checkDragging() {
		return isDragged;
	}
}
