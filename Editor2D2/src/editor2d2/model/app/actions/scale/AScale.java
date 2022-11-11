package editor2d2.model.app.actions.scale;

import java.util.ArrayList;

import editor2d2.model.app.actions.Action;
import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.modules.object.placeable.Instance;

public class AScale extends Action {
	
	private ArrayList<Placeable> initialSelection;
	
	private ArrayList<Placeable> finalSelection;
	

	@Override
	public void undo() {
		for( int i = 0; i < this.initialSelection.size(); i++ )
		{
			Instance initial = (Instance) this.initialSelection.get(i);
			Instance finalInst = (Instance) this.finalSelection.get(i);
			double	w = finalInst.getWidth(),
					h = finalInst.getHeight();
					
			finalInst.setDimensions(initial.getWidth(), initial.getHeight());
			initial.setDimensions(w, h);
		}
	}

	@Override
	public void redo() {
		undo();
	}

	@Override
	public void performImpl(ActionContext c) {
		AScaleContext ac = (AScaleContext) c;
		double horIncr = ac.horizontalScaleIncrement;
		double verIncr = ac.verticalScaleIncrement;
		
		this.initialSelection = ac.initialSelection;
		this.finalSelection = ac.selection;
		
		for( Placeable p : this.finalSelection )
		{
			if( !(p instanceof Instance) )
			continue;
			
			Instance ip = (Instance) p;
			double 	w = ip.getWidth(),
					h = ip.getHeight();
			ip.setDimensions(w + horIncr, h + verIncr);
		}
	}

}
