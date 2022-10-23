package editor2d2.model.project;

import java.util.ArrayList;

public class Folder extends PseudoAsset {

	
		// List of Assets contained in this Folder
	private ArrayList<Asset> assets;
	
		// Reference to the Folder this Folder is located
		// in, NULL if root
	private Folder parentFolder;
	
	
	public Folder() {
		super();
		
		this.assets = new ArrayList<Asset>();
		this.parentFolder = null;
	}
	
	
		// Moves a given Asset from a source Folder to a
		// destination Folder
	public static void moveAssetToFolder(Asset asset, Folder src, Folder dest) {
		src.removeAsset(asset);
		dest.addAsset(asset);
	}
	
	
		// Adds a given Asset to this Folder
	public void addAsset(Asset asset) {
		if( asset == null )
		return;
		
		if( asset instanceof Folder )
		((Folder) asset).setParentFolder(this);
		
		this.assets.add(asset);
	}
	
		// Removes a given Asset from this Folder
	public void removeAsset(Asset asset) {
		for( int i = 0; i < this.assets.size(); i++ )
		{
			if( this.assets.get(i) != asset )
			continue;
			
			this.assets.remove(i);
		}
	}
	
		// Removes all Assets from this Folder
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
	
		// Returns the sub-folder in which a given
		// Asset is located
	public Folder findSubfolderOf(Asset asset) {
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
	
	
		// Returns the parent Folder this Folder is contained
		// in
	public Folder getParentFolder() {
		return this.parentFolder;
	}
	
		// Returns the Assets (and Folders) contained in this
		// Folder
	public ArrayList<Asset> getAllAssets() {
		return this.assets;
	}
	
	
		// Sets the parent Folder
	public void setParentFolder(Folder parentFolder) {
		this.parentFolder = parentFolder;
	}
}
