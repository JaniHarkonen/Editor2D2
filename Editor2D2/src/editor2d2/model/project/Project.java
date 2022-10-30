package editor2d2.model.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.Scene;

public class Project {

		// Name of the map project
	private String name;
	
		// Scenes the project consists of
	private final ArrayList<Scene> scenes;
	
		// List of external assets imported to the project
	private final ArrayList<Asset> assets;
	
		// Mapping of Assets to their identifiers
	private final Map<String, Asset> assetMap;
	
		// Folder in which all the Assets are contained
	private Folder rootFolder;
	
	
	public Project() {
		this.scenes = new ArrayList<Scene>();
		this.assets = new ArrayList<Asset>();
		this.assetMap = new HashMap<String, Asset>();
		this.rootFolder = new Folder();
	}
	
	
		// Adds a new Scene to the project
	public void addScene(Scene scene) {
		if( scene == null )
		return;
		
		this.scenes.add(scene);
	}
	
		// Adds an asset into the project
	public void addAsset(Asset asset, Folder folder) {
		if( asset == null )
		return;
		
		folder.addAsset(asset);
		
		if( !(asset instanceof Folder) )
		{
			this.assetMap.put(asset.getIdentifier(), asset);
			this.assets.add(asset);
		}
	}
	
		// Removes a given non-Folder Asset from the Project
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
	
		// Removes a given asset from the Project
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
	public void removeAsset(Asset asset) {
		Folder parentFolder = null;
		
		if( asset instanceof Folder )
		parentFolder = ((Folder) asset).getParentFolder();
		else
		parentFolder = getRootFolder().findSubfolderOf(asset);
		
		removeAsset(asset, parentFolder);
	}
	
	
		// Returns the name of the map project
	public String getName() {
		return this.name;
	}
	
		// Returns a reference to a Scene of a given name
	public Scene getScene(String name) {
		for( Scene s : this.scenes )
		if( s.getName().equals(name) )
		return s;
		
		return null;
	}
	
		// Returns a refrence to a Scene given its position in the
		// Scene list
	public Scene getScene(int index) {
		if( index < 0 || index >= this.scenes.size() )
		return null;
		
		return this.scenes.get(index);
	}
	
		// Returns a reference to the mapping of Scenes to the names of the Scenes
	public ArrayList<Scene> getAllScenes() {
		return this.scenes;
	}
	
		// Returns a reference to the list of Assets in the project
	public ArrayList<Asset> getAllAssets() {
		return this.assets;
	}
	
		// Returns a reference to an Asset given its identifier
	public Asset getAsset(String identifier) {
		return this.assetMap.get(identifier);
	}
	
		// Returns a reference to the root Folder in which all the
		// Assets are contained
	public Folder getRootFolder() {
		return this.rootFolder;
	}
	
		// Sets the name of the project
	public void setName(String name) {
		this.name = name;
	}
	
		// Sets the root Folder in which all the Assets are contained
	public void setRootFolder(Folder folder) {
		this.rootFolder = folder;
	}
}
