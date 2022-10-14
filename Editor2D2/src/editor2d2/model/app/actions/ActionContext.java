package editor2d2.model.app.actions;

import editor2d2.model.app.Controller;
import editor2d2.model.app.tool.ToolContext;

public class ActionContext {

	public Controller controller;
	
	protected ActionContext(ToolContext tc) {
		this.controller = tc.controller;
	}
}
