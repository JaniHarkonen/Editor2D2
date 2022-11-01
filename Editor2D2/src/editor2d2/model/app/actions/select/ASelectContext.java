package editor2d2.model.app.actions.select;

import java.util.ArrayList;

import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.app.tool.ToolContext;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.placeable.Placeable;

public class ASelectContext extends ActionContext {
	
	public ArrayList<Placeable> initialSelection;
	public Layer target;
	public double startX;
	public double startY;
	public double endX;
	public double endY;
	public int type;

	public ASelectContext(ToolContext tc) {
		super(tc);

		this.target = tc.targetLayer;
		this.type = ASelect.TYPE_NORMAL;
	}

}
