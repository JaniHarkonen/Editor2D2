package editor2d2.model.app.actions.delete;

import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.app.tool.ToolContext;
import editor2d2.model.project.scene.Layer;

public class ADeleteContext extends ActionContext {
	
	public Layer target;
	public double locationX;
	public double locationY;

	public ADeleteContext(ToolContext tc) {
		super(tc);
		this.target = tc.targetLayer;
		this.locationX = tc.locationX;
		this.locationY = tc.locationY;
	}
}
