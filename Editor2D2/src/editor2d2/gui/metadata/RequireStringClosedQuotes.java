package editor2d2.gui.metadata;

import editor2d2.gui.components.RequirementFilter;

public class RequireStringClosedQuotes extends RequirementFilter<String> {
	
	public RequireStringClosedQuotes() {
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
