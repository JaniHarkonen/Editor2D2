package editor2d2.gui.components.requirements;

import editor2d2.gui.components.RequirementFilter;

public class RequireStringNonEmpty extends RequirementFilter<String> {
	
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
