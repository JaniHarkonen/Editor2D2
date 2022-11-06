package editor2d2.model.app.actions.paste;

import java.util.ArrayList;

import editor2d2.DebugUtils;
import editor2d2.model.app.actions.Action;
import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.placeable.Placeable;

public class APaste extends Action {
	
	private Layer targetLayer;
	
	private ArrayList<Placeable> initialSelection;
	

	@Override
	public void undo() {
		for( Placeable p : this.initialSelection )
		p.delete();
	}

	@Override
	public void redo() {
		for( Placeable p : this.initialSelection )
		p.changeLayer(this.targetLayer);
	}

	@Override
	public void performImpl(ActionContext c) {
		APasteContext ac = (APasteContext) c;
		this.targetLayer = ac.targetLayer;
		this.initialSelection = new ArrayList<Placeable>();
		
		for( Placeable p : ac.selection )
		{
			Placeable dp = p.duplicate();
			this.initialSelection.add(dp);
			DebugUtils.log(p + " vs " + dp, this);
		}
		
		ac.controller.placeableSelectionManager.setSelection(this.initialSelection);
	}

}
