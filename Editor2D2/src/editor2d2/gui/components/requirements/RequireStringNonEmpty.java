package editor2d2.gui.components.requirements;

import editor2d2.gui.components.RequirementFilter;

/**
 * This RequirementFilter forces an input to be a 
 * valid String that contains at least a single 
 * character that isn't a whitespace, blank 
 * Strings ("") are not allowed.
 * 
 * @author User
 *
 */
public class RequireStringNonEmpty extends RequirementFilter<String> {
	
	/**
	 * Constructs a RequireStringNonEmpty instance 
	 * that validates inputs that are non-blank 
	 * Strings with at least one character that 
	 * isn't a whitespace.
	 * 
	 * <b> Warning: </b>By default, this filter 
	 * returns a blank string ("") which may not 
	 * be allowed.
	 */
	public RequireStringNonEmpty() {
		this.defaultValue = "";
	}
	

	@Override
	protected boolean validateInput() {
		if( this.input == null || this.input.trim().equals("") )
		return false;
		
		this.value = this.input;
		return true;
	}
}
