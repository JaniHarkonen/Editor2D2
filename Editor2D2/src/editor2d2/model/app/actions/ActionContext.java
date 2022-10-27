package editor2d2.model.app.actions;

import java.util.ArrayList;

import editor2d2.model.app.Controller;
import editor2d2.model.app.tool.ToolContext;
import editor2d2.model.project.scene.placeable.Placeable;

public class ActionContext {

	public Controller controller;
	public ArrayList<Placeable> selection;
	
	protected ActionContext(ToolContext tc) {
		this.controller = tc.controller;
		this.selection = tc.selection;
	}
}
