package editor2d2.model.project.scene.placeables;

public class Instance extends Placeable {

		// X-coordinate of the instance in the scene
	private double x;
	
		// Y-coordinate of the instance in the scene
	private double y;
	
	
	@Override
	public void draw(RenderContext rctxt) {
		
	}
	
		// GETTERS/SETTERS
	
		// Sets the X- and Y-coordinates of the instance
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
		// Returns the X-coordinate of the instance
	public double getX() {
		return this.x;
	}
	
		// Returns the Y-coordinate of the instance
	public double getY() {
		return this.y;
	}
}
