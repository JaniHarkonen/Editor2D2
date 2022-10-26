package editor2d2.model.app.actions.scale;

import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.app.tool.ToolContext;

public class AScaleContext extends ActionContext {
	
	public double scaleIncrement;
	

	public AScaleContext(ToolContext tc) {
		super(tc);
		this.scaleIncrement = 0;
	}

}
