package editor2d2.model.app.actions.move;

import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.app.tool.ToolContext;

public class AMoveContext extends ActionContext {
	
	public double startX;
	public double startY;
	public double endX;
	public double endY;

	public AMoveContext(ToolContext tc) {
		super(tc);
	}

}
