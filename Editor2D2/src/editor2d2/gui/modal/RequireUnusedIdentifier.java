package editor2d2.gui.modal;

import editor2d2.gui.components.RequirementFilter;
import editor2d2.model.project.Asset;
import editor2d2.model.project.Project;

/**
 * This RequirementFilter forces an input to be a identifier 
 * for an Asset. The identifier must be unique meaning no 
 * Asset with the same identifier can exist in the Project 
 * that is currently active. The input must also not contain 
 * spaces.
 * 
 * @author User
 *
 */
public class RequireUnusedIdentifier extends RequirementFilter<String> {
	
	/**
	 * The Project instance where the Asset is to exist.
	 * This Project will be used to check for Asset 
	 * identifier matches.
	 */
	private Project source;
	
	/**
	 * The Asset for whom the identifier is to be 
	 * validated.
	 */
	private Asset asset;
	

	/**
	 * Constructs a RequireUnusedIdentifier instance 
	 * that validates inputs that are considered valid 
	 * identifiers for a given target Asset.
	 * 
	 * @param source Reference to the Project where 
	 * the Asset will exist.
	 * @param asset Reference to the Asset whose 
	 * identifier is to be validated.
	 */
	public RequireUnusedIdentifier(Project source, Asset asset) {
		this.source = source;
		this.asset = asset;
	}
	
	/**
	 * Constructs a RequireUnusedIdentifier instance 
	 * that validates inputs that are considered valid 
	 * identifiers for a given target Asset.
	 * 
	 * @param source Reference to the Project where 
	 * the Asset will exist.
	 */
	public RequireUnusedIdentifier(Project source) {
		this(source, null);
	}

	@Override
	protected boolean validateInput() {
		if( this.input == null || this.input.equals("") || this.input.contains(" ") )
		return false;
		
		Asset found = this.source.getAsset(this.input);
		
		if( found == null || found == asset )
		{
			this.value = this.input;
			return true;
		}
		
		return false;
	}
	
	// GETTERS AND SETTERS
	
	/**
	 * Sets the Asset whose identifier is to be 
	 * validated.
	 * 
	 * @param asset Reference to the new target 
	 * Asset.
	 */
	public void setAsset(Asset asset) {
		this.asset = asset;
	}
}
