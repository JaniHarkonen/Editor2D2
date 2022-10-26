package editor2d2.model.app.tool;

import java.util.ArrayList;

import editor2d2.Application;
import editor2d2.model.app.actions.place.APlace;
import editor2d2.model.app.actions.place.APlaceContext;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.modules.data.placeable.DataCell;
import editor2d2.modules.image.placeable.Tile;

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
		
		if( selectedPlaceable instanceof Tile || selectedPlaceable instanceof DataCell )
		{
			ArrayList<Placeable> placeablesAt = l.selectPlaceables(c.locationX, c.locationY);
			String selectedIdentifier = selectedPlaceable.getAsset().getIdentifier();
			
			
			if( placeablesAt.size() > 0 )
			{
				Placeable otherPlaceable = placeablesAt.get(0);
				if( otherPlaceable != null && otherPlaceable.getAsset().getIdentifier().equals(selectedIdentifier) )
				return USE_FAILED;
			}
		}
		
		(new APlace()).perform(new APlaceContext(c));
		
		return USE_SUCCESSFUL;
	}
}
