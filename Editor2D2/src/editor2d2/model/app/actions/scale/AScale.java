package editor2d2.model.app.actions.scale;

import editor2d2.model.app.actions.Action;
import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.modules.object.placeable.Instance;

public class AScale extends Action {

	@Override
	public void undo() {
		
	}

	@Override
	public void redo() {
		
	}

	@Override
	public void performImpl(ActionContext c) {
		AScaleContext ac = (AScaleContext) c;
		double scaleIncrement = ac.scaleIncrement;
		
		for( Placeable p : ac.controller.placeableSelectionManager.getSelection() )
		{
			if( !(p instanceof Instance) )
			continue;
			
			Instance ip = (Instance) p;
			double 	w = ip.getWidth(),
					h = ip.getHeight();
			ip.setDimensions(w + scaleIncrement, h + scaleIncrement);
		}
	}

}
