package editor2d2.model.app.actions.rotate;

import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.app.tool.ToolContext;

public class ARotateContext extends ActionContext {
	
	public double locationX;
	public double locationY;
	

	public ARotateContext(ToolContext tc) {
		super(tc);
		this.locationX = tc.locationX;
		this.locationY = tc.locationY;
	}

}
