package editor2d2.model.app.actions.rotate;

import editor2d2.common.GeometryUtilities;
import editor2d2.model.app.actions.Action;
import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.modules.object.placeable.Instance;

public class ARotate extends Action {
	
	
	

	@Override
	public void undo() {
		
	}

	@Override
	public void redo() {
		
	}

	@Override
	public void performImpl(ActionContext c) {
		ARotateContext ac = (ARotateContext) c;
		double	tx = ac.locationX,
				ty = ac.locationY;
		
		for( Placeable p : ac.controller.placeableSelectionManager.getSelection() )
		{
			if( p instanceof Instance )
			((Instance) p).setRotation(GeometryUtilities.angleBetweenPoints(p.getX(), p.getY(), tx, ty));
		}
	}

}
