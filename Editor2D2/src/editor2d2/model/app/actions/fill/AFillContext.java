package editor2d2.model.app.actions.fill;

import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.app.tool.ToolContext;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.placeable.Placeable;

public class AFillContext extends ActionContext {

	public Layer target;
	public Placeable placeable;
	public double locationX;
	public double locationY;

	public AFillContext(ToolContext tc) {
		super(tc);
		this.target = tc.targetLayer;
		this.placeable = tc.selection.get(0).duplicate();
		this.locationX = tc.locationX;
		this.locationY = tc.locationY;
	}
}
