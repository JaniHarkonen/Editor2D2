package editor2d2.model.project.assets;

import editor2d2.model.project.scene.placeables.Placeable;

public abstract class Asset {

		// Name of the asset as displayed in the resources panel
	protected String name;
	
		// Identifier used to refer to the asset in the compiled map file
	protected String identifier;
	
	
		// Creates a placeable based on the asset
		// CAN BE OVERRIDDEN
	public Placeable createPlaceable() {
		return null;
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
