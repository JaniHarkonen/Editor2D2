package editor2d2.gui.components.requirements;

import editor2d2.gui.components.RequirementFilter;

public class RequireStringNonEmpty extends RequirementFilter<String> {

	@Override
	protected boolean validateInput() {
		
		if( this.input == null || this.input.equals("") )
		return false;
		
		return true;
	}
}
