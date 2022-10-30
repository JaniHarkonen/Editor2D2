package editor2d2.model.app.tool;

import java.util.ArrayList;

import editor2d2.Application;
import editor2d2.model.app.actions.delete.ADelete;
import editor2d2.model.app.actions.delete.ADeleteContext;
import editor2d2.model.app.actions.place.APlace;
import editor2d2.model.app.actions.place.APlaceContext;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.placeable.Placeable;

public class TPlace extends Tool {

	public TPlace() {
		super();
		this.name = "Place";
		this.shortcutKey = "X";
		this.icon = Application.resources.getGraphic("icon-tool-place");
	}
	
	
	@Override
	protected int usePrimary(ToolContext c) {
		Placeable selectedPlaceable = c.selection.get(0);
		Layer l = c.targetLayer;
		
		if( l == null || selectedPlaceable == null )
		return USE_FAILED;
		
		if( /*selectedPlaceable instanceof Tile || selectedPlaceable instanceof DataCell*/true )
		{
			ArrayList<Placeable> placeablesAt = l.selectPlaceables(c.locationX, c.locationY);
			String selectedIdentifier = selectedPlaceable.getAsset().getIdentifier();
			
			if( placeablesAt.size() > 0 )
			{
				Placeable otherPlaceable = placeablesAt.get(0);
				if( otherPlaceable.getAsset().getIdentifier().equals(selectedIdentifier) )
				return USE_FAILED;
			}
		}
		
		(new APlace()).perform(new APlaceContext(c));
		
		return USE_SUCCESSFUL;
	}
	
	@Override
	protected int useTertiary(ToolContext c) {
		if( c.isContinuation )
		return USE_FAILED;
		
		return usePrimary(c);
	}
	
	@Override
	protected int useSecondary(ToolContext c) {
		if( c.targetLayer == null )
		return USE_FAILED;
		
		ArrayList<Placeable> placeablesAt = c.targetLayer.selectPlaceables(c.locationX, c.locationY);
		if( placeablesAt.size() > 0 )
		{
			(new ADelete()).perform(new ADeleteContext(c));
			return USE_SUCCESSFUL;
		}
		
		return USE_FAILED;
	}
}
