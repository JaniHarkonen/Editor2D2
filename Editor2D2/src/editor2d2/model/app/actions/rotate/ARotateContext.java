package editor2d2.model.app.actions.rotate;

import java.util.ArrayList;

import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.app.tool.ToolContext;
import editor2d2.model.project.scene.placeable.Placeable;

public class ARotateContext extends ActionContext {
	
	public double locationX;
	public double locationY;
	public ArrayList<Placeable> initialSelection;
	

	public ARotateContext(ToolContext tc) {
		super(tc);
		this.initialSelection = null;
		this.locationX = tc.locationX;
		this.locationY = tc.locationY;
	}

}
