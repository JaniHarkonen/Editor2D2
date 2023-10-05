package editor2d2.model.project.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;

import editor2d2.model.project.Asset;
import editor2d2.model.project.Folder;
import editor2d2.model.project.Project;
import editor2d2.model.project.scene.Camera;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.Scene;
import editor2d2.modules.FactoryService;
import johnnyutils.johnparser.Parser;
import johnnyutils.johnparser.parser.ParsedCommand;

/**
 * ProjectLoader instances are used to read project 
 * files. The files will be used to create a Project-
 * instance that will typically be opened in the 
 * editor. ProjectLoader iterates over the lines of a
 * text file representing a project and creates the 
 * Assets, Scenes, Layers and Placeables of the Project.
 * Loaders of specific Asset-types will be utilized 
 * for loading in Assets and Placeables.
 * 
 * <b>Notice: </b> This class utilizes an external 
 * module, JOHNParser from the JOHNNYUtilities library
 * v.1.1.0. The parser is used to split the read lines
 * into ParsedCommands consisting of the name of the 
 * command as well as its arguments.
 * 
 * See AbstractLoader for more information on loaders.
 * 
 * @author User
 *
 */
public class ProjectLoader {

	/**
	 * Expecting to read any line.
	 */
	public static final int ANY = 0;
	
	/**
	 * Expecting to parse a line containing an Asset declaration.
	 */
	public static final int ASSET = 1;
	
	/**
	 * Expecting to parse a line containing a Layer declaration.
	 */
	public static final int LAYER = 2;

	/**
	 * Expecting to parse a line containing a Placeable declaration.
	 */
	public static final int PLACEABLE = 3;
	
	/**
	 * Expecting to parse a compilation statement.
	 */
	public static final int COMPILATION_STATEMENT = 4;
	
	/**
	 * Whether a line was successfully parsed.
	 */
	public static final int PARSE_SUCCESSFUL = 1;
	
	/**
	 * Whether parsing a line failed.
	 */
	public static final int PARSE_FAILED = 2;
	
	/**
	 * Type of the next line that is expected to be parsed
	 * by the loadProject()-method.
	 */
	private int expectedLineType;
	
	/**
	 * Project-instance that resulted from the last loadProject()-call.
	 */
	private Project targetProject;
	
	/**
	 * Scene-instance that is currently being parsed.
	 */
	private Scene targetScene;
	
	/**
	 * Layer-instance that is currently being parsed.
	 */
	private Layer targetLayer;
	
	/**
	 * A dequeue (stack) keeping track of the Folder structure of 
	 * the Project.
	 */
	private ArrayDeque<Folder> folderStack;
	
	/**
	 * ArrayList of Assets that reference other Assets that have 
	 * not yet been loaded.
	 * 
	 * See UnresolvedAsset for more information on inter-Asset 
	 * dependencies during loading.
	 */
	private ArrayList<UnresolvedAsset> unresolvedAssets;
	
	/**
	 * Folder-instance whose contents are currently being read.
	 */
	private Folder targetFolder;
	
	/**
	 * Number of unread lines left in the compilation statement.
	 */
	private int compilationStatementLinesLeft;
	
	@SuppressWarnings("rawtypes")
	/**
	 * The loader currently being used to load a Placeable from the
	 * project file.
	 */
	private AbstractLoader currentLoader;
	
	/**
	 * Constructs a ProjectLoader instance with default settings.
	 * The ProjectLoader is now ready to be used for loading.
	 */
	public ProjectLoader() {
		this.targetProject = null;
		this.targetScene = null;
		this.targetLayer = null;
		this.currentLoader = null;
		this.expectedLineType = ANY;
		this.folderStack = new ArrayDeque<Folder>();
		this.targetFolder = null;
		this.compilationStatementLinesLeft = 0;
		this.unresolvedAssets = null;
	}
	
	/**
	 * The main functionality of the ProjectLoader is to load
	 * and create a Project based on a given text file. This
	 * method reads a given project file and iterates over its
	 * lines parsing each line using a Parser imported from 
	 * JOHNParser.
	 * 
	 * For each parsed line, the interpret()-method is called
	 * which takes in the ParsedCommand-instance representing 
	 * the read line and modifies the resulting Project-
	 * instance to reflect the contents of the file.
	 * 
	 * The resulting Project-instance is returned and stored 
	 * in the targetProject-field.
	 * 
	 * See the interpret()-method for more information on 
	 * interpreting the project file lines.
	 * 
	 * @param file Reference to the File-instance representing 
	 * the project file.
	 * 
	 * @return Returns a reference to the created Project.
	 */
	public Project loadProject(File file) {
		File projectFile = file;
		this.unresolvedAssets = new ArrayList<UnresolvedAsset>();
		
			// Invalid file -> fail
		if( !projectFile.exists() || projectFile.isDirectory() )
		return null;
		
		this.targetProject = new Project();
		
			// Set root Folder
		this.targetFolder = new Folder();
		this.targetProject.setRootFolder(this.targetFolder);
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(projectFile));
			Parser parser = new Parser();
			String line;
			
			this.expectedLineType = ANY;
			
				// Parse project file lines
			while( (line = br.readLine()) != null )
			{
					// Read compilation statement, do not use the Parser 
				if( this.expectedLineType == COMPILATION_STATEMENT )
				{
					Scene scene = this.targetScene;
					scene.setCompilationStatement(scene.getCompilationStatement() + line);
					
					this.compilationStatementLinesLeft--;
					
					if( this.compilationStatementLinesLeft <= 0 )
					this.expectedLineType = LAYER;
					else
					scene.setCompilationStatement(scene.getCompilationStatement() + "\n");
					
					continue;
				}
				
					// Emtpy line
				if( line.equals("") )
				continue;
				
				ParsedCommand pc = parser.parse(line);
				
					// Failed to parse line
				if( pc == null )
				continue;
				
				interpret(pc);
			}
			
			br.close();
			
				// Resolve Asset dependencies
			ResolutionContext rc = new ResolutionContext();
			rc.hostProject = this.targetProject;
			for( UnresolvedAsset ua : this.unresolvedAssets )
			{
				if( !ua.resolve(rc) )
				return null;
			}
		}
		catch( IOException e )
		{
			e.printStackTrace();
			return null;
		}
		
		this.targetProject.setFilepath(file.getPath());
		return this.targetProject;
	}
	
	/**
	 * Loads a project from a text file given its path.
	 * 
	 * See the loadProject(File)-method for more information
	 * on loading Projects.
	 * 
	 * @param path File path of the project file that is 
	 * to be loaded.
	 * 
	 * @return Reference to the resulting Project.
	 */
	public Project loadProject(String path) {
		return loadProject(new File(path));
	}
	
	/**
	 * Returns a reference to the last Project-instance that 
	 * was loaded in using this loader.
	 *  
	 * @return Reference to the last loaded Project.
	 */
	public Project getLastProject() {
		return this.targetProject;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * This method interprets a ParsedCommand that represents a
	 * line read by the loadProject()-method. The command name 
	 * is used to determine the appropriate course of action 
	 * within a simple switch-structure.
	 * 
	 * Because some of the objects contained in the project are 
	 * dynamic, they must be handled by their respective loaders
	 * found inside their modules. The default case of the 
	 * switch-statement is used to load in the dynamic aspects of 
	 * the Project.
	 * 
	 * @param pc ParsedCommand that represents a line read from the
	 * project file.
	 * 
	 * @return Returns a code indicating whether the parsing and
	 * interpretation of the command was successful.
	 */
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
					int compilationStatementLength = (int) pc.getNumeral(4);
					
					if( compilationStatementLength > 0 )
					{
						this.expectedLineType = COMPILATION_STATEMENT;
						this.compilationStatementLinesLeft = compilationStatementLength;
					}
					
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
					boolean isVisible = pc.getBoolean(5);
					
					createLayer(assetClass, name, cw, ch, opacity, isVisible);
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
					case ASSET: {
						switch( pc.getCommand() )
						{
								// Create a Folder
							case "folder": {
								Folder f = new Folder();
								String name = pc.getString(0);
								
								f.setName(name);
								this.folderStack.push(this.targetFolder);
								this.targetFolder.addAsset(f);
								this.targetFolder = f;
								
								break;
							}
								
								// Ending of the Folder
							case "/folder": this.targetFolder = this.folderStack.pop(); break;
								
								// Load an Asset in the currently targeted Folder
							default: {
								Asset loadedAsset = FactoryService.getFactories(pc.getCommand()).createLoader().loadAsset(pc);
								
								if( loadedAsset instanceof UnresolvedAsset )
								{
									UnresolvedAsset uloadedAsset = (UnresolvedAsset) loadedAsset;
									this.unresolvedAssets.add(uloadedAsset);
									loadedAsset = uloadedAsset.getUnresolvedAsset();
								}
								
								this.targetProject.addAsset(loadedAsset, this.targetFolder);
								
								break;
							}
						}
						
						break;
					}
					
						// Load a Placeable
					case PLACEABLE: {
						Asset sourceAsset = this.targetProject.getAsset(pc.getCommand());
						this.currentLoader.loadPlaceable(pc, sourceAsset, this.targetLayer);
						break;
					}
					
					default: return PARSE_FAILED;
				}
			}
		}
		
		return PARSE_SUCCESSFUL;
	}

	/**
	 * Sets the line type that is next expected by the loader when 
	 * reading a project file, but only if a given line type is 
	 * currently being expected by the loader.
	 * 
	 * @param expectation Line type that should currently be 
	 * expected by the loader in order to set the next line type 
	 * expectation.
	 * 
	 * @param set Line type expectation to switch to.
	 * 
	 * @return Whether the given line type was being expected, and 
	 * thus, the next expectation was set. 
	 */
	private int setExpectedLineTypeOrFail(int expectation, int set) {
		if( this.expectedLineType == expectation )
		this.expectedLineType = set;
		else
		return PARSE_FAILED;
		
		return PARSE_SUCCESSFUL;
	}
	
	/**
	 * Helper method that can be used to determine whether the 
	 * result of the interpret()-method was successful. Essentially,
	 * converts the integer-typed result codes to booleans.
	 * 
	 * @param outcome Result of the interpret()-method.
	 * 
	 * @return Whether the result is successful.
	 */
	private boolean checkSuccessful(int outcome) {
		return (outcome == PARSE_SUCCESSFUL);
	}
	
	/**
	 * Helper method that creates a Scene-instance with a given
	 * name, width and height and adds it to the resulting Project.
	 * 
	 * @param name Name of the Scene that is to be added.
	 * @param width Width of the Scene that is to be added.
	 * @param height Height of the Scene that is to be added.
	 */
	private void createScene(String name, int width, int height) {
		this.targetScene = new Scene(name);
		this.targetScene.setDimensions(width, height);
		
		Camera cam = new Camera();
		cam.setPortDimensions(358, 210);
		this.targetScene.setCamera(cam);
		
		this.targetProject.addScene(this.targetScene);
	}
	
	/**
	 * Helper method that creates a Layer and adds it to the 
	 * Scene whose contents are currently being loaded. 
	 * 
	 * @param assetClass Name of the asset class accepted by
	 * the Layer.
	 * @param name Name of the Layer.
	 * @param cw Cellular width of the Layer.
	 * @param ch Cellular height of the Layer.
	 * @param opacity The opacity value of the Layer.
	 * @param isVisible Whether the Layer is currently visible
	 * in the editor.
	 */
	private void createLayer(String assetClass,
							 String name,
							 int cw, int ch,
							 double opacity,
							 boolean isVisible) {
		this.targetLayer = FactoryService.getFactories(assetClass).createLayer(this.targetScene, cw, ch);
		this.targetLayer.setName(name);
		this.targetLayer.setOpacity(opacity);
		this.targetLayer.setVisible(isVisible);
		
		this.targetScene.addLayer(this.targetLayer);
	}
}
