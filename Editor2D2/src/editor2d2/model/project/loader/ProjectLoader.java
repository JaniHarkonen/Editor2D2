package editor2d2.model.project.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import editor2d2.model.project.Asset;
import editor2d2.model.project.Project;
import editor2d2.model.project.scene.Camera;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.Scene;
import editor2d2.modules.FactoryService;
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
	
		// Reference to the loader that is currently being
		// used to load Placeable
	private AbstractLoader currentLoader;
	
	public ProjectLoader() {
		this.targetProject = null;
		this.targetScene = null;
		this.targetLayer = null;
		this.currentLoader = null;
		this.expectedLineType = ANY;
	}
	

		// Loads a Project from a given file and returns it
	public Project loadProject(File file) {
		File projectFile = file;
		
			// Invalid file -> fail
		if( !projectFile.exists() || projectFile.isDirectory() )
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
			{
				ParsedCommand pc = parser.parse(line);
				
				if( pc == null )
				continue;
				
				interpret(pc);
			}
			
			br.close();
			
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
		
		return this.targetProject;
	}
	
	public Project loadProject(String path) {
		return loadProject(new File(path));
	}
	
	
		// Returns the last Project that was loaded using this ProjectLoader
	public Project getLastProject() {
		return this.targetProject;
	}
	
	
		// Handles the interpretation of parsed project
		// file lines
	private int interpret(ParsedCommand pc) {
		
		switch( pc.getCommand() )
		{
				// Beginning of the Assets-block
			case "assets": return setExpectedLineTypeOrFail(ANY, ASSET);
			
				// Ending of the Assets-block
			case "/assets": return setExpectedLineTypeOrFail(ASSET, ANY);
			
				// Beginning of a Scene
			case "scene": {
				
				if( checkSuccessful(setExpectedLineTypeOrFail(ANY, LAYER)) )
				{
					String name = pc.getString(0);
					int width = (int) pc.getNumeral(1);
					int height = (int) pc.getNumeral(2);
					
					createScene(name, width, height);
					
					return PARSE_SUCCESSFUL;
				}
			}
				
				return PARSE_FAILED;
			
				// Ending of a Scene
			case "/scene": return setExpectedLineTypeOrFail(LAYER, ANY);
				
				// Beginning of a Layer
			case "layer": {
				
				if( checkSuccessful(setExpectedLineTypeOrFail(LAYER, PLACEABLE)) )
				{
					String assetClass = pc.getReference(0);
					String name = pc.getString(1);
					int cw = (int) pc.getNumeral(2);
					int ch = (int) pc.getNumeral(3);
					double opacity = pc.getNumeral(4);
					
					createLayer(assetClass, name, cw, ch, opacity);
					this.currentLoader = FactoryService.getFactories(assetClass).createLoader();
					
					return PARSE_SUCCESSFUL;
				}
				
				return PARSE_FAILED;
			}
					
				
				// Ending of a Layer
			case "/layer": {
				if( checkSuccessful(setExpectedLineTypeOrFail(PLACEABLE, LAYER)) )
				{
					this.currentLoader = null;
					return PARSE_SUCCESSFUL;
				}
				
				return PARSE_FAILED;
			}
			
			default: {
				switch( this.expectedLineType )
				{
						// Load an Asset
					case ASSET:
						Asset loadedAsset = FactoryService.getFactories(pc.getCommand()).createLoader().loadAsset(pc);
						this.targetProject.addAsset(loadedAsset);
						break;
					
						// Load a Placeable
					case PLACEABLE:
						Asset sourceAsset = this.targetProject.getAsset(pc.getCommand());
						this.currentLoader.loadPlaceable(pc, sourceAsset, this.targetLayer);
						break;
					
					default: return PARSE_FAILED;
				}
			}
		}
		
		return PARSE_SUCCESSFUL;
	}
	
	
		// Sets the expected line type if the current one meets the 
		// given expectation
	private int setExpectedLineTypeOrFail(int expectation, int set) {
		if( this.expectedLineType == expectation )
		this.expectedLineType = set;
		else
		return PARSE_FAILED;
		
		return PARSE_SUCCESSFUL;
	}
	
		// Returns whether a parse was successful
		// (de facto converts int-typed outcomes to booleans)
	private boolean checkSuccessful(int outcome) {
		return (outcome == PARSE_SUCCESSFUL);
	}
	
		// Creates a new Scene and adds it to the currently
		// loaded Project
	private void createScene(String name, int width, int height) {
		this.targetScene = new Scene(name);
		this.targetScene.setDimensions(width, height);
		
		Camera cam = new Camera();
		cam.setPortDimensions(358, 210);
		this.targetScene.setCamera(cam);
		
		this.targetProject.addScene(this.targetScene);
	}
	
		// Creates a new Layer and adds it to the currently
		// loaded Scene
	private void createLayer(String assetClass, String name, int cw, int ch, double opacity) {
		this.targetLayer = FactoryService.getFactories(assetClass).createLayer(this.targetScene, cw, ch);
		this.targetLayer.setName(name);
		this.targetLayer.setOpacity(opacity);
		
		this.targetScene.addLayer(this.targetLayer);
	}
}
