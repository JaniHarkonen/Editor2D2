package editor2d2.model.app.tool;

import editor2d2.Application;
import editor2d2.model.app.actions.place.APlace;
import editor2d2.model.app.actions.place.APlaceContext;
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
		
		if( c.targetLayer == null || selectedPlaceable == null )
		return USE_FAILED;
		
		(new APlace()).perform(new APlaceContext(c));
		
		return USE_SUCCESSFUL;
	}
}
