package editor2d2.model.app.actions.select;

import java.util.ArrayList;

import editor2d2.model.app.Controller;
import editor2d2.model.app.SelectionManager;
import editor2d2.model.app.actions.Action;
import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.project.scene.placeable.Placeable;

public class ASelect extends Action {
	
	public static final int TYPE_NORMAL = 1;
	public static final int TYPE_ADD = 2;
	public static final int TYPE_SUBTRACT = 3;
	
	private ArrayList<Placeable> selection;
	
	private ArrayList<Placeable> initialSelection;
	
	private Controller controller;
	
	private int type;
	

	@Override
	public void undo() {
		SelectionManager<Placeable> mngr = this.controller.placeableSelectionManager;
		
		mngr.deselect();
		mngr.setSelection(this.initialSelection);
	}

	@Override
	public void redo() {
		SelectionManager<Placeable> mngr = this.controller.placeableSelectionManager;
		
		switch( this.type )
		{
			case TYPE_NORMAL:
				mngr.deselect();
				mngr.setSelection(this.selection);
				break;
				
			case TYPE_ADD:
				mngr.addSelection(this.selection);
				break;
			
			case TYPE_SUBTRACT:
				mngr.removeSelection(this.selection);
				break;
		}
	}

	@Override
	public void performImpl(ActionContext c) {
		ASelectContext ac = (ASelectContext) c;
		ArrayList<Placeable> selection = ac.target.selectPlaceables(ac.startX, ac.startY, ac.endX, ac.endY);
		
		this.controller = ac.controller;
		this.initialSelection = ac.initialSelection;
		this.type = ac.type;
		
		SelectionManager<Placeable> mngr = this.controller.placeableSelectionManager;
		
		switch( this.type )
		{
			case TYPE_NORMAL:
				if( selection == null || selection.size() <= 0 )
				mngr.deselect();
				else
				mngr.setSelection(selection);
				break;
				
			case TYPE_ADD:
				mngr.addSelection(selection);
				break;
				
			case TYPE_SUBTRACT:
				mngr.removeSelection(selection);
				break;
		}
		
		this.selection = selection;
	}
}
