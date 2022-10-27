package editor2d2.model.app.actions.place;

import java.util.ArrayList;

import editor2d2.model.app.actions.Action;
import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.modules.object.placeable.Instance;

public class APlace extends Action {
	
	private Placeable placement;
	
	private Placeable replacedPlaceable;
	
	private Layer placementLayer;
	
	
	public APlace() {
		this.placement = null;
		this.replacedPlaceable = null;
		this.placementLayer = null;
	}
	

	@Override
	public void undo() {
		this.placement.delete();
		
		if( this.replacedPlaceable != null )
		this.replacedPlaceable.changeLayer(this.placementLayer);
	}

	@Override
	public void redo() {
		if( this.replacedPlaceable != null )
		this.replacedPlaceable.delete();
		
		this.placement.changeLayer(this.placementLayer);
	}

	@Override
	public void performImpl(ActionContext c) {
		APlaceContext ac = (APlaceContext) c;
		
			// Save the replaced object
		if( !(ac.placeable instanceof Instance) )
		{
			ArrayList<Placeable> at = ac.target.selectPlaceables(ac.locationX, ac.locationY);
			
			if( at.size() > 0 )
			{
				this.replacedPlaceable = at.get(0);
				
				if( this.replacedPlaceable != null )
				this.replacedPlaceable.delete();
			}
		}
		
		ac.target.attemptPlace(ac.locationX, ac.locationY, ac.placeable);
		
		this.placementLayer = ac.target;
		this.placement = ac.placeable;
	}
}
