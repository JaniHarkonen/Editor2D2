package editor2d2.model.project;

import java.util.ArrayList;
import java.util.Map;

import editor2d2.model.project.assets.Asset;

public class Project {

		// Name of the map project
	private String name;
	
		// Maps the project consists of
	private Map<String, Scene> maps;
	
		// List of external assets imported to the project
	private ArrayList<Asset> assets;
	
}
