package editor2d2.model.project.scene.placeables;

public class Instance extends Placeable {

		// X-coordinate of the object in the scene
	private double x;
	
		// Y-coordinate of the object in the scene
	private double y;
	
	
		// GETTERS/SETTERS
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
}
