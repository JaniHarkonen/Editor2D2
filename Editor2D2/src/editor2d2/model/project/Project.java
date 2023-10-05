package editor2d2.model.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.Scene;

/**
 * This class contains functions as the model for the 
 * project being operated on. When a project is loaded 
 * or saved, the instance of this class will be passed 
 * onto the ProjectLoader or ProjectWriter.
 * <br/><br/>
 * 
 * Project holds the name of the project, the file
 * path the project was previously saved at, an
 * ArrayList of the Scenes in the project, an 
 * ArrayList of the Assets in the project, the root
 * Folder of the project as well as a HashMap of the 
 * Assets where each Asset is coupled with their 
 * unique identifiers. While this introduces redundancy 
 * it greatly improves the look up times when the 
 * unique identifier of the Asset is known as the Asset
 * ArrayList does not need to be traversed. In total 
 * there are three sets of references to Assets: the 
 * ArrayList, the HashMap and the root Folder.
 * <br/><br/>
 * 
 * <b>Notice: </b>the HashMap is sometimes referred to 
 * as "quick-access map" in the comments.
 * <br/><br/>
 * 
 * Project also contains methods for adding and 
 * removing Scenes and Assets.
 * <br/><br/>
 * 
 * See ProjectLoader for more information on loading 
 * and opening Projects.
 * <br/><br/>
 * 
 * See ProjectWriter for more information on writing 
 * and saving Projects.
 * 
 * @author User
 *
 */
public class Project {

	/**
	 * Name of the Project.
	 */
	private String name;
	
	/**
	 * Filepath that hte Project was previously saved 
	 * at.
	 */
	private String filepath;
	
	/**
	 * ArrayList that holds all the Scenes in the 
	 * Project.
	 */
	private final ArrayList<Scene> scenes;
	
	/**
	 * ArrayList that holds all the Assets in the 
	 * Project in no particular order. This can be 
	 * used in cases when all the assets need to be 
	 * iterated over as iterating over an ArrayList is 
	 * faster than iterating over a HashMap.
	 */
	private final ArrayList<Asset> assets;
	
	/**
	 * Mapping of all Assets in the Project with their 
	 * unique identifiers. This improves the look up 
	 * times in cases where the identifier of an Asset 
	 * is known. Looking up an Asset in a HashMap is 
	 * much faster than having to iterate through an 
	 * ArrayList.
	 */
	private final Map<String, Asset> assetMap;
	
	/**
	 * Folder instance in which all the Assets of the 
	 * Project are contained.
	 */
	private Folder rootFolder;
	
	/**
	 * Constructs a Project instance with default 
	 * settings.
	 */
	public Project() {
		this.scenes = new ArrayList<Scene>();
		this.assets = new ArrayList<Asset>();
		this.assetMap = new HashMap<String, Asset>();
		this.rootFolder = new Folder();
		this.filepath = null;
	}
	
	/**
	 * Adds a given Scene to the Scene ArrayList of the 
	 * Project.
	 * 
	 * @param scene Reference to the Scene to be added 
	 * to the Project.
	 */
	public void addScene(Scene scene) {
		if( scene == null )
		return;
		
		this.scenes.add(scene);
	}
	
	/**
	 * Removes a given Scene from the Project's 
	 * ArrayList given its index.
	 * 
	 * @param index Index of the Scene in the Project's
	 * ArrayList.
	 */
	public void removeScene(int index) {
		if( index < 0 || index >= this.scenes.size() )
		return;
		
		this.scenes.remove(index);
	}
	
	/**
	 * Adds a given Asset to a given Folder inside the 
	 * Project. The Asset will also be placed in the 
	 * Project's ArrayList and HashMap.
	 * <br/><br/>
	 * 
	 * <b>Notice: </b>because Folder is a PseudoAsset 
	 * it too is accepted by this method, however, the 
	 * if the Asset is a Folder it will simply be added 
	 * into the given folder and not the Collections 
	 * mentioned above.
	 * 
	 * @param asset Reference to the Asset that is to 
	 * be added to the Project.
	 * @param folder Reference to the Folder that the 
	 * Asset is to be placed into.
	 */
	public void addAsset(Asset asset, Folder folder) {
		if( asset == null )
		return;
		
		folder.addAsset(asset);
		
			// Do not add Folders to the Asset collections
		if( !(asset instanceof Folder) )
		{
			this.assetMap.put(asset.getIdentifier(), asset);
			this.assets.add(asset);
		}
	}
	
	/**
	 * Removes a given Asset from the Project, however, the 
	 * Asset is NOT removed from its Folder. The Asset will 
	 * be removed from the ArrayList and the HashMap of the 
	 * Project. This method will also remove all Placeables 
	 * of this Asset from the Layers and Scenes they're 
	 * placed on.
	 * <br/><br/>
	 * 
	 * <b>Notice: </b>this method is only to be 
	 * called by the Project class itself when calling 
	 * removeAsset.
	 * <br/><br/>
	 * 
	 * See removeAsset-method for the public-facing method 
	 * for removing Assets.
	 * 
	 * @param asset Reference to the Asset that is to be 
	 * removed from the 
	 */
	private void removeNonFolderAsset(Asset asset) {
		
			// Remove from the quick-access map
		this.assetMap.remove(asset.getIdentifier());
		
			// Remove from the list of Assets
		for( int i = 0; i < this.assets.size(); i++ )
		{
			if( this.assets.get(i) != asset )
			continue;
			
			this.assets.remove(i);
			break;
		}
		
			// Remove Placeables derived from the Asset
		for( Scene s : this.scenes )
		for( Layer l : s.getLayers() )
		l.deleteByAsset(asset);
	}
	
	/**
	 * Removes a given Asset from the Project and a given 
	 * Folder. The Folder of the Asset must be known. If 
	 * a NULL folder is provided, nothing happens. This 
	 * method also accepts Folders given that they are 
	 * PseudoAssets that extend Asset, however, they will 
	 * be treated differently as the contents of the 
	 * Folder must also be removed from the Project.
	 * <br/><br/>
	 * 
	 * The Asset will be removed from its Folder, the 
	 * Project's ArrayList as well as HashMap.
	 * <br/><br/>
	 * 
	 * See removeAsset(Asset) if you want to remove an 
	 * Asset when the folder it's contained in is NOT known.
	 * 
	 * @param asset Asset or Folder that is to be removed 
	 * from the Project.
	 * @param folder Folder that the Asset or Folder is 
	 * contained in. If NULL, nothing happens.
	 */
	public void removeAsset(Asset asset, Folder folder) {
		if( folder == null )
		return;
		
		boolean isFolder = asset instanceof Folder;
		
		if( isFolder )
		{
				// When removing a Folder, its contents must
				// also be removed
			Folder f = (Folder) asset;
			ArrayList<Asset> folderAssets = f.getAllAssets();
			
			while( folderAssets.size() > 0 )
			removeAsset(folderAssets.get(0), f);
			
			f.getParentFolder().removeAsset(f);
		}
		else
		{
			removeNonFolderAsset(asset);
			folder.removeAsset(asset);
		}
	}
	
		// Determines the Folder that a given Asset is
		// located in and removes it from the Project
	/**
	 * Removes a given Asset from the Project. This 
	 * method also accepts Folders as they are 
	 * PseudoAssets that extend Asset. If the Asset is a 
	 * Folder its contents will also be removed from the 
	 * Project.
	 * 
	 * @param asset Reference to the Asset that is to be 
	 * removed from the Project.
	 */
	public void removeAsset(Asset asset) {
		Folder parentFolder = null;
		
		if( asset instanceof Folder )
		parentFolder = ((Folder) asset).getParentFolder();
		else
		parentFolder = getRootFolder().findSubfolderOf(asset);
		
		removeAsset(asset, parentFolder);
	}
	
	// GETTERS AND SETTERS
	
	/**
	 * Returns the name of the Project.
	 * 
	 * @return Name of the Project.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns a Scene of the Project given its name. If no 
	 * such Scene exists, NULL will be returned.
	 * 
	 * @param name Name of the Scene whose refrence to return.
	 * 
	 * @return Reference to the Scene.
	 */
	public Scene getScene(String name) {
		for( Scene s : this.scenes )
		if( s.getName().equals(name) )
		return s;
		
		return null;
	}
	
	/**
	 * Returns a Scene of the Project given its index in the 
	 * Scene ArrayList. If the index is out of bounds, NULL 
	 * will be returned.
	 * 
	 * @param index Index of the Scene in the Project's 
	 * ArrayList.
	 * 
	 * @return Reference to the Scene.
	 */
	public Scene getScene(int index) {
		if( index < 0 || index >= this.scenes.size() )
		return null;
		
		return this.scenes.get(index);
	}
	
	/**
	 * Returns a reference to the ArrayList of all the Scenes 
	 * in the Project.
	 * 
	 * @return Reference to the ArrayList of the Scenes.
	 */
	public ArrayList<Scene> getAllScenes() {
		return this.scenes;
	}
	
	/**
	 * Returns a reference to the ArrayList of all the Assets 
	 * in the Project.
	 * 
	 * @return Reference to the ArrayList of Assets.
	 */
	public ArrayList<Asset> getAllAssets() {
		return this.assets;
	}
	
	/**
	 * Returns a reference to an Asset given its unique 
	 * identifier from the Asset HashMap. If no Asset 
	 * matching the identifier exists, NULL will be 
	 * returned. 
	 * 
	 * @param identifier Unique identifier of the Asset.
	 * 
	 * @return Reference to the Asset with the given 
	 * identifier or NULL if no such Asset exists. 
	 */
	public Asset getAsset(String identifier) {
		return this.assetMap.get(identifier);
	}
	
	/**
	 * Returns a reference to the root Folder in which 
	 * all the Assets of the Project are contained in.
	 * 
	 * @return Reference to the root folder all Assets 
	 * of the Project are in.
	 */
	public Folder getRootFolder() {
		return this.rootFolder;
	}
	
	/**
	 * Returns the file path the project was previously 
	 * saved at or loaded from.
	 * 
	 * @return File path the project lies at.
	 */
	public String getFilepath() {
		return this.filepath;
	}
	
	/**
	 * Sets the name of the Project.
	 * 
	 * @param name New name of the Project.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Sets the root Folder containing all the Assets of 
	 * the Project.
	 * <br/><br/>
	 * 
	 * <b>Notice: </b>this does not update the Asset 
	 * ArrayList or HashMap. It is not recommended to call 
	 * this method on Projects that already contain Assets 
	 * as this may lead to discrepancies between the 
	 * collections and the root Folder.
	 * 
	 * @param folder Reference to the Folder that is to be 
	 * assigned as the root folder for the Project.
	 */
	public void setRootFolder(Folder folder) {
		this.rootFolder = folder;
	}
	
	/**
	 * Sets the file path of the Project.
	 * 
	 * @param path New file path.
	 */
	public void setFilepath(String path) {
		this.filepath = path;
	}
}
