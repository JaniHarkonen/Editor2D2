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
		this.description = "Places an instance of the selected asset.\n"
						 + "Left-click: place\n"
						 + "Left-click + CTRL: place single\n"
						 + "Left-click + SHIFT: stack objects\n"
						 + "Right-click: delete";
		this.shortcutKey = "X";
		this.icon = Application.resources.getGraphic("icon-tool-place");
	}
	
	@Override
	public int use(ToolContext c) {
		int outcome = super.use(c);
		
			// Tertiary functionality also handles stacking
			// (4th order functionality)
		if( outcome != USE_SUCCESSFUL )
		return useTertiary(c);
		
		return outcome;
	}
	
	
	@Override
	protected int usePrimary(ToolContext c) {
		if( c.isContinuation )
		return USE_FAILED;
		
		return useTertiary(c);
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
	
	@Override
	protected int useTertiary(ToolContext c) {
		if( c.selection == null || c.selection.size() <= 0 )
		return USE_FAILED;
		
		Placeable selectedPlaceable = c.selection.get(0);
		Layer l = c.targetLayer;
		
		if( l == null || selectedPlaceable == null )
		return USE_FAILED;
		
		ArrayList<Placeable> placeablesAt = l.selectPlaceables(c.locationX + 1, c.locationY + 1);
		String selectedIdentifier = selectedPlaceable.getAsset().getIdentifier();
		
		if( placeablesAt.size() > 0 && c.order != 4 )
		{
			Placeable otherPlaceable = placeablesAt.get(0);
			if( otherPlaceable.getAsset().getIdentifier().equals(selectedIdentifier) )
			return USE_FAILED;
		}
		
		(new APlace()).perform(new APlaceContext(c));
		
		return USE_SUCCESSFUL;
	}
}
