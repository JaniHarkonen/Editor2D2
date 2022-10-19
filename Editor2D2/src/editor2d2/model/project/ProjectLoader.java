package editor2d2.model.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.Scene;
import johnnyutils.johnparser.Parser;
import johnnyutils.johnparser.parser.ParsedCommand;

public class ProjectLoader {

	public static final int ANY = 0;
	public static final int ASSET = 1;
	public static final int LAYER = 2;
	public static final int PLACEABLE = 3;
	
	public static final int PARSE_SUCCESSFUL = 1;
	public static final int PARSE_FAILED = 2;
	
		// Type of line expected
	private int expectedLineType;
	
		// Reference to the Project that has been created as
		// a result of the loading process
	private Project targetProject;
	
		// Reference to the Scene that is currently being
		// loaded
	private Scene targetScene;
	
		// Reference to the Layer that is currently being
		// loaded
	private Layer targetLayer;
	
	public ProjectLoader() {
		this.targetProject = null;
		this.targetScene = null;
		this.targetLayer = null;
		this.expectedLineType = ANY;
	}
	

		// Loads a Project from a given file and returns it
	public Project loadProject(String path) {
		File projectFile = new File(path);
		
			// Invalid file -> fail
		if( projectFile.exists() || projectFile.isDirectory() )
		return null;
		
		this.targetProject = new Project();
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(projectFile));
			Parser parser = new Parser();
			String line;
			
			this.expectedLineType = ANY;
			
				// Parse project file lines
			while( (line = br.readLine()) != null )
			interpret(parser.parse(line));
			
			br.close();
			
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
		
		return this.targetProject;
	}
	
	
		// Handles the interpretation of parsed project
		// file lines
	private int interpret(ParsedCommand pc) {
		boolean failed = false;
		
		switch( pc.getCommand() )
		{
			case "assets":
				if( this.expectedLineType == ANY )
				this.expectedLineType = ASSET;
				else
				failed = true;
				break;
			
			case "/assets":
				if( this.expectedLineType == ASSET )
				this.expectedLineType = ANY;
				else
				failed = true;
				break;
			
			case "scene":
				if( this.expectedLineType == ANY )
				{
					String name = pc.getString(0);
					int width = (int) pc.getNumeral(1);
					int height = (int) pc.getNumeral(2);
					
					createScene(name, width, height);
					this.expectedLineType = LAYER;
				}
				else
				failed = true;
				break;
			
			case "/scene":
				if( this.expectedLineType == LAYER )
				{
					this.targetScene = null;
					this.expectedLineType = ANY;
				}
				else
				failed = true;
				break;
				
			case "layer":
				if( this.expectedLineType == LAYER )
				{
					//createLayer();
					this.expectedLineType = PLACEABLE;
				}
				else
				failed = true;
				break;
				
			case "/layer":
				if( this.expectedLineType == PLACEABLE )
				{
					this.targetLayer = null;
					this.expectedLineType = LAYER;
				}
				else
				failed = true;
				break;
			
			default: {
				switch( this.expectedLineType )
				{
					case ASSET:
						break;
					
					case PLACEABLE:
						break;
					
					default:
						failed = true;
						break;
				}
			}
		}
		
		if( failed )
		return PARSE_FAILED;
		
		return PARSE_SUCCESSFUL;
	}
	
		// Creates a new Scene and adds it to the currently
		// loaded Project
	private void createScene(String name, int width, int height) {
		this.targetScene = new Scene(name);
		this.targetScene.setDimensions(width, height);
	}
	
		// Creates a new Layer and adds it to the currently
		// loaded Scene
	private void createLayer(String assetClass, String name, int cw, int ch, double opacity) {
		//this.targetLayer = GUIFactory.create;
		this.targetLayer.setOpacity(opacity);
	}
	
		// Creates a new Asset and adds it to the currently
		// loaded Project
	private void createAsset(String name, String identifier) {
		//this.targetProject.addAsset(asset);
	}
	
		// Creates a new Placeable and puts it into the
		// currently loaded Layer
	private void createPlaceable(String identifier) {
		Asset asset = this.targetProject.getAsset(identifier);
		//this.targetLayer.place(x, y, asset.createPlaceable());
	}
}
