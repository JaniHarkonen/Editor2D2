package editor2d2.model.project.scene;

public class Camera {

		// X-coordinate of the camera in the scene
	private double x;

		// Y-coordinate of the camera in the scene
	private double y;
	
		// Z-coordinate of the camera in the scene
	private double z;
	
		// Width of the view port in pixels
	private int portWidth;
	
		// Height of the view port in pixels
	private int portHeight;
	
	
		// Returns the X-coordinate of the camera
	public double getX() {
		return this.x;
	}
	
		// Returns the Y-coordinate of the camera
	public double getY() {
		return this.y;
	}
	
		// Returns the Z-coordinate (height) of the camera
	public double getZ() {
		return this.z;
	}
	
		// Returns the view port width of the camera
	public int getPortWidth() {
		return this.portWidth;
	}
	
		// Returns the view port height of the camera
	public int getPortHeight() {
		return this.portHeight;
	}
	
		// Sets the position of the camera
	public void setPosition(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
		// Sets the view port dimensions of the camera
	public void setPortDimensions(int portWidth, int portHeight) {
		this.portWidth = portWidth;
		this.portHeight = portHeight;
	}
}
