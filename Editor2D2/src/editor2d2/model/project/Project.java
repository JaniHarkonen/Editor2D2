package editor2d2.model.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import editor2d2.model.project.scene.Scene;

public class Project {

		// Name of the map project
	private String name;
	
		// Scenes the project consists of
	private final Map<String, Scene> scenes;
	
		// List of external assets imported to the project
	private final ArrayList<Asset> assets;
	
	
	public Project() {
		this.scenes = new HashMap<String, Scene>();
		this.assets = new ArrayList<Asset>();
	}
	
	
		// Adds a new Scene to the project
	public void addScene(Scene scene) {
		if( scene == null )
		return;
		
		this.scenes.put(scene.getName(), scene);
	}
	
		// Adds an asset into the project
	public void addAsset(Asset asset) {
		if( asset == null )
		return;
		
		this.assets.add(asset);
	}
	
	
		// Returns the name of the map project
	public String getName() {
		return this.name;
	}
	
		// Returns a reference to a Scene of a given name
	public Scene getScene(String name) {
		return this.scenes.get(name);
	}
	
		// Returns a reference to the mapping of Scenes to the names of the Scenes
	public Map<String, Scene> getAllScenes() {
		return this.scenes;
	}
	
		// Returns a reference to the list of Assets in the project
	public ArrayList<Asset> getAllAssets() {
		return this.assets;
	}
	
		// Sets the name of the project
	public void setName(String name) {
		this.name = name;
	}
}
