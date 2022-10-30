package editor2d2.modules.object.modal.requirements;

import editor2d2.gui.components.RequirementFilter;

public class RequireValidPropertyName extends RequirementFilter<String> {
	
	@Override
	protected boolean validateInput() {
		if( 
			this.input == null ||
			this.input.trim().equals("") ||
			this.input.contains("\"") ||
			this.input.contains("'")
		)
		return false;
		
		this.value = this.input;
		return true;
	}

}
