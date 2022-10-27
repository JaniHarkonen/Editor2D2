package editor2d2.model.app.actions.select;

import java.util.ArrayList;

import editor2d2.model.app.Controller;
import editor2d2.model.app.actions.Action;
import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.project.scene.placeable.Placeable;

public class ASelect extends Action {
	
	private ArrayList<Placeable> selection;
	
	private ArrayList<Placeable> initialSelection;
	
	private Controller controller;
	

	@Override
	public void undo() {
		this.controller.placeableSelectionManager.deselect();
		this.controller.placeableSelectionManager.setSelection(this.initialSelection);
	}

	@Override
	public void redo() {
		this.controller.placeableSelectionManager.deselect();
		this.controller.placeableSelectionManager.setSelection(this.selection);
	}

	@Override
	public void performImpl(ActionContext c) {
		ASelectContext ac = (ASelectContext) c;
		ArrayList<Placeable> selection = ac.target.selectPlaceables(ac.startX, ac.startY, ac.endX, ac.endY);
		
		this.controller = ac.controller;
		this.initialSelection = ac.initialSelection;
		
		if( selection == null || selection.size() <= 0 )
		this.controller.placeableSelectionManager.deselect();
		else
		{
			this.controller.placeableSelectionManager.setSelection(selection);
			this.selection = selection;
		}
	}

}
