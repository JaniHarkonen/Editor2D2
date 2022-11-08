package editor2d2.gui.components.requirements;

import editor2d2.gui.components.RequirementFilter;

public class RequireStringName extends RequirementFilter<String> {
	
	public RequireStringName() {
		this.defaultValue = "";
	}
	

	@Override
	protected boolean validateInput() {
		if( this.input == null || this.input.trim().equals("") )
		return false;
		
		if( this.input.contains("\"") || this.input.contains("'") )
		return false;
		
		this.value = this.input;
		return true;
	}
}
