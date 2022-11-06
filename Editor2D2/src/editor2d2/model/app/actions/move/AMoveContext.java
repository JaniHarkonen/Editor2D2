package editor2d2.model.app.actions.move;

import java.awt.Point;
import java.util.ArrayList;

import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.app.tool.ToolContext;
import editor2d2.model.project.scene.Layer;

public class AMoveContext extends ActionContext {
	
	public Layer targetLayer;
	
	public double locationX;
	
	public double locationY;
	
	public ArrayList<Point.Double> offsets;
	
	
	public AMoveContext(ToolContext tc) {
		super(tc);
	}
}
