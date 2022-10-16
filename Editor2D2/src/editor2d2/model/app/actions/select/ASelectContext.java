package editor2d2.model.app.actions.select;

import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.app.tool.ToolContext;
import editor2d2.model.project.scene.Layer;

public class ASelectContext extends ActionContext {
	
	public Layer target;
	public double startX;
	public double startY;
	public double endX;
	public double endY;

	public ASelectContext(ToolContext tc) {
		super(tc);

		this.target = tc.targetLayer;
	}

}
