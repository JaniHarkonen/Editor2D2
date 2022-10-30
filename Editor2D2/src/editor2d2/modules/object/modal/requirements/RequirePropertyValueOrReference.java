package editor2d2.modules.object.modal.requirements;

import editor2d2.gui.components.RequirementFilter;

public class RequirePropertyValueOrReference extends RequirementFilter<String> {

	public static final String[] VALID_REFERENCES = {
			"x",
			"y",
			"w",
			"h",
			"rot"
	};
	
	
	@Override
	protected boolean validateInput() {
		if( this.input == null || this.input.trim().equals("") )
		return false;
		
		boolean accepted = false;
		
		if( this.input.charAt(0) != '&' )
		accepted = true;
		else
		{
			String ref = this.input.substring(1);
			for( String valid : VALID_REFERENCES )
			{
				if( !ref.equals(valid) )
				continue;
				
				accepted = true;
				break;
			}
		}
		
		if( !accepted )
		return false;
		
		this.value = this.input;
		return true;
	}

}
