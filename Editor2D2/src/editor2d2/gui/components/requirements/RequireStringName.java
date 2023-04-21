package editor2d2.gui.components.requirements;

import editor2d2.gui.components.RequirementFilter;

/**
 * This RequirementFilter forces an input to be a 
 * valid name for an Asset. The input must not 
 * non-blank (not ""), must not contain slashes
 * (/ or \) or single quotation marks (').
 * 
 * @author User
 *
 */
public class RequireStringName extends RequirementFilter<String> {
	
	/**
	 * Constructs a RequireStringName instance 
	 * that validates inputs that are considered 
	 * valid names for an Asset.
	 * 
	 * <b> Warning: </b>By default, this filter 
	 * returns a blank value "" which may be 
	 * dangerous when dealing with Assets.
	 */
	public RequireStringName() {
		this.defaultValue = "";
	}
	

	@Override
	protected boolean validateInput() {
			// Non-blank check
		if( this.input == null || this.input.trim().equals("") )
		return false;
		
			// Check for illegal characters
		if( this.input.contains("\"") || this.input.contains("'") )
		return false;
		
		this.value = this.input;
		return true;
	}
}
