package editor2d2.model.app.actions.scale;

import java.util.ArrayList;

import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.app.tool.ToolContext;
import editor2d2.model.project.scene.placeable.Placeable;

public class AScaleContext extends ActionContext {
	
	public double horizontalScaleIncrement;
	public double verticalScaleIncrement;
	public ArrayList<Placeable> initialSelection;
	

	public AScaleContext(ToolContext tc) {
		super(tc);
		this.horizontalScaleIncrement = 0;
		this.verticalScaleIncrement = 0;
		this.initialSelection = null;
	}

}
