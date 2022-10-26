package editor2d2.model.app.actions.scale;

import java.util.ArrayList;

import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.app.tool.ToolContext;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.placeable.Placeable;

public class AScaleContext extends ActionContext {
	
	public double scaleIncrement;
	public ArrayList<Placeable> initialSelection;
	

	public AScaleContext(ToolContext tc) {
		super(tc);
		this.scaleIncrement = 0;
		this.initialSelection = null;
	}

}
