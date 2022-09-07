package editor2d2.model.project;

import java.util.ArrayList;

import editor2d2.model.project.layers.Layer;
import editor2d2.model.project.scene.Camera;
import editor2d2.model.project.scene.placeables.Placeable;

public class Scene {

		// Width of the map area in pixels
	private int width;
	
		// Height of the map area in pixels
	private int height;
	
		// Layers of placeables that the map consists of
	private ArrayList<Layer<Placeable>> layers;
	
		// Camera that is used to render the map view at its location
	private Camera camera;
	
	
		// GETTERS/SETTERS
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public Camera getCamera() {
		return this.camera;
	}
}
