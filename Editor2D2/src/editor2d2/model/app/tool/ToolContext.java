package editor2d2.model.app.tool;

import java.util.ArrayList;

import editor2d2.model.app.Controller;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.placeable.Placeable;

public class ToolContext {

		// The Layer that the Tool is to be used upon
	public Layer targetLayer;
	
		// Whether the usage of the Tool is a continuation
		// of earlier use-calls, e.g., if the Tool is being
		// held down vs. clicked once
	public boolean isContinuation;
	
		// Placeables that are in an active selection
	public ArrayList<Placeable> selection;
	
		// In-scene X-coordinate of the location that the Tool
		// is to be used at
	public double locationX;
	
		// In-scene Y-coordinate of the location that the Tool
		// is to be used at
	public double locationY;
	
		// The order of functionality that is to be used, e.g.,
		// 1 = primary functionality, 2 = secondary ...
	public int order;
	
		// Reference to the Controller that is controlling the
		// tool
	public Controller controller;
	
	
	public ToolContext(	Layer targetLayer,
						boolean isContinuation,
						ArrayList<Placeable> selection,
						double x,
						double y,
						int order,
						Controller controller) {
		this.targetLayer = targetLayer;
		this.isContinuation = isContinuation;
		this.selection = selection;
		this.locationX = x;
		this.locationY = y;
		this.order = order;
		this.controller = controller;
	}
	
	public ToolContext( Layer targetLayer,
						boolean isContinuation,
						ArrayList<Placeable> selection,
						double x,
						double y,
						Controller controller) {
		this(targetLayer, isContinuation, selection, x, y, 1, controller);
	}
	
	public ToolContext() {
		this(null, false, null, -1, -1, Tool.PRIMARY_FUNCTION, null);
	}
}
