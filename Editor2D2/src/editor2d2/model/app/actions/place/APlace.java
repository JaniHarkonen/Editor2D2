package editor2d2.model.app.actions.place;

import editor2d2.model.app.actions.Action;
import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.placeable.Placeable;

public class APlace extends Action {
	
	private Placeable placement;
	
	private Layer placementLayer;
	
	
	public APlace() {
		this.placement = null;
		this.placementLayer = null;
	}
	

	@Override
	public void undo() {
		this.placementLayer.delete(this.placement.getCellX(), this.placement.getCellY());
	}

	@Override
	public void redo() {
		this.placementLayer.attemptPlace(this.placement.getCellX(), this.placement.getCellY(), this.placement);
	}

	@Override
	protected void performImpl(ActionContext c) {
		APlaceContext ac = (APlaceContext) c;
		ac.target.attemptPlace((int) ac.locationX, (int) ac.locationY, ac.placeable);
		
		this.placementLayer = ac.target;
		this.placement = ac.placeable;
	}
}
