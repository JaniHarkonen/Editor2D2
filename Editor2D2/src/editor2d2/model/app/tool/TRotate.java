package editor2d2.model.app.tool;

import editor2d2.Application;
import editor2d2.model.app.actions.rotate.ARotate;
import editor2d2.model.app.actions.rotate.ARotateContext;

public class TRotate extends Tool {

	public TRotate() {
		super();
		this.name = "Rotate";
		this.shortcutKey = "R";
		this.icon = Application.resources.getGraphic("icon-tool-rotate");
	}
	
	
	@Override
	protected int usePrimary(ToolContext c) {
		(new ARotate()).performImpl(new ARotateContext(c));
		return USE_SUCCESSFUL;
	}
	
	@Override
	public int stop(ToolContext c) {
		if( c.controller.placeableSelectionManager.getSelection().size() <= 0 )
		return USE_FAILED;
		
		(new ARotate()).perform(new ARotateContext(c));
		
		return USE_SUCCESSFUL;
	}
}
