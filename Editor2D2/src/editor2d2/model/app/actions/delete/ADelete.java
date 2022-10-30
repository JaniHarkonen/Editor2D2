package editor2d2.model.app.actions.delete;

import editor2d2.model.app.actions.Action;
import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.placeable.Placeable;

public class ADelete extends Action {
	
	private Placeable removedPlaceable;
	
	private Layer targetLayer;
	
	
	public ADelete() {
		this.removedPlaceable = null;
		this.targetLayer = null;
	}
	

	@Override
	public void undo() {
		if( this.removedPlaceable == null )
		return;
		
		this.removedPlaceable.changeLayer(this.targetLayer);
	}

	@Override
	public void redo() {
		this.removedPlaceable.delete();
	}

	@Override
	public void performImpl(ActionContext c) {
		ADeleteContext ac = (ADeleteContext) c;
		this.targetLayer = ac.target;
		
		this.removedPlaceable = ac.target.delete(ac.locationX, ac.locationY);
	}
}
