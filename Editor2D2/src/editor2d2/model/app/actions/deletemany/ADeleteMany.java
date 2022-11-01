package editor2d2.model.app.actions.deletemany;

import java.util.ArrayList;

import editor2d2.model.app.actions.Action;
import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.placeable.Placeable;

public class ADeleteMany extends Action {
	
	private ArrayList<Placeable> removedPlaceables;
	
	private Layer targetLayer;
	
	
	public ADeleteMany() {
		this.targetLayer = null;
		this.removedPlaceables = null;
	}

	@Override
	public void undo() {
		if( this.removedPlaceables == null || this.removedPlaceables.size() <= 0 )
		return;
		
		for( Placeable p : this.removedPlaceables )
		p.changeLayer(this.targetLayer);
	}

	@Override
	public void redo() {
		for( Placeable p : this.removedPlaceables )
		p.delete();
	}

	@Override
	public void performImpl(ActionContext c) {
		ADeleteManyContext ac = (ADeleteManyContext) c;
		this.targetLayer = ac.target;
		
		this.removedPlaceables = ac.selection;
		redo();
	}
}
