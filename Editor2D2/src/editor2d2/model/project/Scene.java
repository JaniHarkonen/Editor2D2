package editor2d2.model.project;

import java.util.ArrayList;

import editor2d2.model.project.layers.Layer;
import editor2d2.model.project.scene.Camera;

public class Scene {
	
		// Name of the scene
	private String name;

		// Width of the Scene area in pixels
	private int width;
	
		// Height of the Scene area in pixels
	private int height;
	
		// Layers of placeables that the Scene consists of
	private final ArrayList<Layer> layers;
	
		// Camera that is used to render the Scene view at its location
	private Camera camera;
	
	
	public Scene() {
		this.name = null;
		this.layers = new ArrayList<Layer>();
		this.camera = null;
	}
	
	public Scene(String name) {
		this();
		
		this.name = name;
	}
	
	
		// GETTERS/SETTERS
	
		// Returns the name of the Scene
	public String getName() {
		return this.name;
	}
	
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
	public ArrayList<Layer> getLayers() {
		return this.layers;
	}
	
		// Returns a reference to a layer with given name
	public Layer getLayerByName(String name) {
		for( Layer l : this.layers )
		if( l.getName().equals(name) )
		return l;
		
		return null;
	}
	
		// Sets the name of the Scene
	public void setName(String name) {
		if( name == null )
		return;
		
		this.name = name;
	}

		// Sets the dimensions of the Scene
	public void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
		// Sets the camera instance viewing the Scene
	public void setCamera(Camera camera) {
		camera.setScene(this);
		this.camera = camera;
	}
	
		// Adds a layer to the list of layers
	public void addLayer(Layer layer) {
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
	
		// Removes a given layer from the list of layers and returns
		// true if the removal was successful, false if not
	public boolean removeLayer(Layer layer) {
		for( int i = 0; i < this.layers.size(); i++ )
		{
			if( this.layers.get(i) == layer )
			{
				this.layers.remove(i);
				return true;
			}
		}
		
		return false;
	}
}
