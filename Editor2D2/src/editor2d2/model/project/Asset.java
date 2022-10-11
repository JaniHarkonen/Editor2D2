package editor2d2.model.project;

import editor2d2.model.project.scene.placeable.Placeable;

public abstract class Asset {
	
		// Name of the asset class that will be displayed in the editor
		// THIS SHOULD ONLY BE SET ONCE IN THE CONSTRUCTOR AS IT ONLY
		// PERTAINS TO THE CLASS
	protected final String assetClassName;
	
		// Name of the asset as displayed in the resources panel
	protected String name;
	
		// Identifier used to refer to the asset in the compiled map file
	protected String identifier;
	
	
	protected Asset(String assetClassName) {
		this.assetClassName = assetClassName;
		this.name = null;
		this.identifier = null;
	}
	
	
		// Creates a placeable based on the asset
		// CAN BE OVERRIDDEN
	public Placeable createPlaceable() {
		return null;
	}
	
	
		// Returns the name of the asset class
	public String getAssetClassName() {
		return this.assetClassName;
	}
	
		// Returns the name of the asset
	public String getName() {
		return this.name;
	}
	
		// Returns the identifier of the asset
	public String getIdentifier() {
		return this.identifier;
	}
	
		// Sets the name of the asset
	public void setName(String name) {
		this.name = name;
	}
	
		// Sets the identifier of the asset
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}
