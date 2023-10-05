package editor2d2.model.project;

import java.util.ArrayList;

/**
 * Folders are a type of PseudoAsset that consist of 
 * an ArrayList of Assets. Folders are used by the 
 * AssetPane to group Assets together in the GUI.
 * Each Folder should have a parent Folder in which 
 * the Folder is contained. If the parent Folder is 
 * NULL, this Folder will be considered the root of 
 * the hierarchy.
 * <br/><br/>
 * 
 * This class also provides methods for adding, 
 * removing and finding Assets from the Asset list.
 * <br/><br/>
 * 
 * See PseudoAsset for more information on Assets 
 * that are not actually Assets.
 * <br/>
 * 
 * See Asset for more information on project 
 * assets.
 * 
 * @author User
 *
 */
public class Folder extends PseudoAsset {

	/**
	 * ArrayList containing the references to all 
	 * the Assets found in this Folder.
	 */
	private ArrayList<Asset> assets;
	
	/**
	 * Reference to the Folder that this Folder is 
	 * located in. If NULL, this Folder is the root 
	 * Folder.
	 */
	private Folder parentFolder;
	
	/**
	 * Constructs a Folder instance with an empty 
	 * list of Assets. By default, a Folder is the 
	 * root.
	 */
	public Folder() {
		super();
		
		this.assets = new ArrayList<Asset>();
		this.parentFolder = null;
	}
	
	
	/**
	 * Moves a given Asset from a source Folder to a 
	 * destination Folder by first removing the Asset 
	 * from the source and then adding it to the 
	 * destination.
	 * 
	 * @param asset Reference to the Asset that is to 
	 * be moved.
	 * @param src Reference to the Source Folder FROM 
	 * which the Asset is to be moved.
	 * @param dest Reference to the destination Folder 
	 * TO which the Asset will be moved.
	 */
	public static void moveAssetToFolder(Asset asset, Folder src, Folder dest) {
		src.removeAsset(asset);
		dest.addAsset(asset);
	}
	
	/**
	 * Adds a given Asset to this Folder. This method 
	 * also accepts other Folders. When a Folder is 
	 * added, its parent Folder will also be set to 
	 * THIS.
	 * 
	 * @param asset Reference to the Asset or Folder 
	 * that is to be added to this Folder.
	 */
	public void addAsset(Asset asset) {
		if( asset == null )
		return;
		
		if( asset instanceof Folder )
		((Folder) asset).setParentFolder(this);
		
		this.assets.add(asset);
	}
	
	/**
	 * Removes a given Asset from the Folder. This 
	 * method also accepts Folders. When a Folder is 
	 * removed, its parent Folder will become NULL.
	 * <br/><br/>
	 * 
	 * <b>Notice: </b>this method simply removes the 
	 * Folder from this one. The contents of the 
	 * removed Folder do NOT change.
	 * 
	 * @param asset Reference to the Asset or Folder 
	 * that is to be removed.
	 */
	public void removeAsset(Asset asset) {
		if( asset instanceof Folder )
		((Folder) asset).setParentFolder(null);
		
		for( int i = 0; i < this.assets.size(); i++ )
		{
			if( this.assets.get(i) != asset )
			continue;
			
			this.assets.remove(i);
			break;
		}
	}
	
	/**
	 * Clears the Folder and all of its contents.
	 * <br/><br/>
	 * 
	 * <b>Warning: </b>this method WILL also clear 
	 * the contents of all the sub-folders 
	 * recursively.
	 */
	public void removeAll() {
		while( this.assets.size() > 0 )
		{
			Asset a = this.assets.get(0);
			
			if( a instanceof Folder )
			((Folder) a).removeAll();
			else
			this.assets.remove(0);
		}
	}
	
	/**
	 * Finds the Folder that a given Asset or 
	 * Folder is located in through a recursive 
	 * search. If no such Asset or Folder exists, 
	 * NULL will be returned.
	 * 
	 * @param asset Reference to the Asset or 
	 * Folder whose parent Folder is to be 
	 * determined.
	 * 
	 * @return
	 */
	public Folder findSubfolderOf(Asset asset) {
		if( asset instanceof Folder )
		return ((Folder) asset).getParentFolder();
		
		for( Asset a : this.assets )
		{
			if( a == asset )
			return this;
			
			if( a instanceof Folder )
			{
				Folder subfolder = ((Folder) a).findSubfolderOf(asset);
				
				if( subfolder != null )
				return subfolder;
			}
		}
		
		return null;
	}
	
	// GETTERS AND SETTERS
	
	/**
	 * Returns a reference to the Folder that this 
	 * Folder is contained in.
	 * 
	 * @return Reference to the parent folder this 
	 * Folder is in.
	 */
	public Folder getParentFolder() {
		return this.parentFolder;
	}
	
	/**
	 * Returns a reference to the ArrayList containing 
	 * all the Assets (and Folders) contained in this 
	 * Folder.
	 * <br/><br/>
	 * 
	 * <b>Notice: </b>this method does NOT list the 
	 * contents of the sub-folders.
	 * 
	 * @return Reference to the ArrayList of all the 
	 * Assets and Folders in this Folder.
	 */
	public ArrayList<Asset> getAllAssets() {
		return this.assets;
	}
	
	/**
	 * Sets the Folder that this Folder is contained 
	 * in.
	 * <br/><br/>
	 * 
	 * <b>Notice: </b>this method does not update the 
	 * parent Folder itself. It is possible that the 
	 * previous parent Folder - if there is one - still 
	 * maintains a reference to this Folder after 
	 * calling this method. 
	 * 
	 * @param parentFolder Reference to the new parent 
	 * Folder that this considered to be contained in.
	 */
	public void setParentFolder(Folder parentFolder) {
		this.parentFolder = parentFolder;
	}
	
	
		// DEBUG - prints the folder contents
	/*public void printFolder() {
		DebugUtils.log("folder: " + getName(), this);
		for( Asset a : this.assets )
		{
			if( a instanceof Folder )
			((Folder) a).printFolder();
			else
			System.out.println(a);
		}
	}*/
}
