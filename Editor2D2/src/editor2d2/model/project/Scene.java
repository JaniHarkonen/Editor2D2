package editor2d2.model.project;

import java.util.ArrayList;

import editor2d2.model.project.layers.Layer;
import editor2d2.model.project.scene.Camera;
import editor2d2.model.project.scene.placeables.Placeable;

public class Scene {

		// Width of the Scene area in pixels
	private int width;
	
		// Height of the Scene area in pixels
	private int height;
	
		// Layers of placeables that the Scene consists of
	private final ArrayList<Layer<Placeable>> layers;
	
		// Camera that is used to render the Scene view at its location
	private Camera camera;
	
	
	public Scene() {
		this.layers = new ArrayList<Layer<Placeable>>();
	}
	
	
		// GETTERS/SETTERS
	
		// Returns the width of the Scene in pixels
	public int getWidth() {
		return this.width;
	}
	
		// Returns the height of the Scene in pixels
	public int getHeight() {
		return this.height;
	}
	
		// Returns a reference to the instance viewing the Scene
	public Camera getCamera() {
		return this.camera;
	}
	
		// Returns a reference to the list of layers that the Scene consists of
	public ArrayList<Layer<Placeable>> getLayers() {
		return this.layers;
	}

		// Sets the dimensions of the Scene
	public void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
		// Sets the camera instance viewing the Scene
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
		// Adds a layer to the list of layers
	public void addLayer(Layer<Placeable> layer) {
		this.layers.add(layer);
	}
	
		// Removes a layer of a given name from the list of layers
		// and returns true if the removal was successful, false if not
	public boolean removeLayer(String name) {
		for( int i = 0; i < this.layers.size(); i++ )
		{
			if( this.layers.get(i).getName() == name )
			{
				this.layers.remove(i);
				return true;
			}
		}
		
		return false;
	}
}
