package editor2d2.gui.modal;

import editor2d2.gui.components.RequirementFilter;
import editor2d2.model.project.Asset;
import editor2d2.model.project.Project;

public class RequireUnusedIdentifier extends RequirementFilter<String> {
	
	private Project source;
	
	private Asset asset;
	
	
	public RequireUnusedIdentifier(Project source, Asset asset) {
		this.source = source;
		this.asset = asset;
	}
	
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
	
	
	public void setAsset(Asset asset) {
		this.asset = asset;
	}
}
