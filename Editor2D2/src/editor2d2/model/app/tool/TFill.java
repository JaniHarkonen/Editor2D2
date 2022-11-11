package editor2d2.model.app.tool;

import editor2d2.Application;
import editor2d2.model.app.actions.fill.AFill;
import editor2d2.model.app.actions.fill.AFillContext;
import editor2d2.model.project.scene.placeable.Placeable;

public class TFill extends Tool {

	public TFill() {
		super();
		this.name = "Fill";
		this.description = "Fills a closed area with a tile."
						 + "\nLeft-click: fill";
		this.shortcutKey = "F";
		this.icon = Application.resources.getGraphic("icon-tool-fill");
	}
	
	
	@Override
	protected int usePrimary(ToolContext c) {
		Placeable selectedPlaceable = c.selection.get(0);
		
		if( c.targetLayer == null || selectedPlaceable == null )
		return USE_FAILED;
		
		(new AFill()).perform(new AFillContext(c));
		
		return USE_SUCCESSFUL;
	}
}
