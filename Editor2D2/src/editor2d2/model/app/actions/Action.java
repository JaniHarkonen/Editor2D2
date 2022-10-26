package editor2d2.model.app.actions;

public abstract class Action {
	
	public final void perform(ActionContext c) {
		performImpl(c);
		c.controller.getActionHistory().log(this);
	}
	
	public abstract void undo();
	
	public abstract void redo();
	
	
	public abstract void performImpl(ActionContext c);
}
