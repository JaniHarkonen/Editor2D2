package editor2d2.model.app.actions.move;

import editor2d2.model.app.Controller;
import editor2d2.model.app.actions.Action;
import editor2d2.model.app.actions.ActionContext;

public class AMove extends Action {
	
	private Controller controller;
	

	@Override
	public void undo() {
		
	}

	@Override
	public void redo() {
		
	}

	@Override
	public void performImpl(ActionContext c) {
		AMoveContext ac = (AMoveContext) c;
		this.controller = ac.controller;
	}

}
