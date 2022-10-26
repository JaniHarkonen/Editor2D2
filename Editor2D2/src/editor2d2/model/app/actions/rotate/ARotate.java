package editor2d2.model.app.actions.rotate;

import java.util.ArrayList;

import editor2d2.common.GeometryUtilities;
import editor2d2.model.app.actions.Action;
import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.modules.object.placeable.Instance;

public class ARotate extends Action {
	
	private ArrayList<Placeable> initialSelection;
	
	private ArrayList<Placeable> finalSelection;
	

	@Override
	public void undo() {
		for( int i = 0; i < this.initialSelection.size(); i++ )
		{
			Instance initial = (Instance) this.initialSelection.get(i);
			Instance finalInst = (Instance) this.finalSelection.get(i);
			double rot = finalInst.getRotation();
					
			finalInst.setRotation(initial.getRotation());
			initial.setRotation(rot);
		}
	}

	@Override
	public void redo() {
		undo();
	}

	@Override
	public void performImpl(ActionContext c) {
		ARotateContext ac = (ARotateContext) c;
		double	tx = ac.locationX,
				ty = ac.locationY;
		
		this.initialSelection = ac.initialSelection;
		this.finalSelection = ac.controller.placeableSelectionManager.getSelection();
		
		for( Placeable p : this.finalSelection )
		{
			if( p instanceof Instance )
			((Instance) p).setRotation(GeometryUtilities.angleBetweenPoints(p.getX(), p.getY(), tx, ty));
		}
	}

}
