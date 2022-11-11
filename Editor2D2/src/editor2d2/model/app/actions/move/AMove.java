package editor2d2.model.app.actions.move;

import java.awt.Point;
import java.util.ArrayList;

import editor2d2.model.app.actions.Action;
import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.placeable.Placeable;

public class AMove extends Action {
	
	private ArrayList<Placeable> selection;
	
	private ArrayList<Placeable> replacedPlaceables;
	
	private ArrayList<Point.Double> initialPositions;
	

	public void undo(boolean replace) {
		for( int i = 0; i < this.selection.size(); i++ )
		{
			Placeable p = this.selection.get(i);
			Layer targetLayer = p.getLayer();
			Point.Double off = this.initialPositions.get(i);
			
			double	prevX = p.getX(),
					prevY = p.getY();
			
			p.delete();
			targetLayer.place(off.x, off.y, p);
			off.x = prevX;
			off.y = prevY;
		}
		
		if( replace )
		{
			for( Placeable p : this.replacedPlaceables )
			p.getLayer().place(p.getX(), p.getY(), p);
		}
	}
	
	@Override
	public void undo() {
		undo(true);
	}

	@Override
	public void redo() {
		undo(false);
	}

	@Override
	public void performImpl(ActionContext c) {
		AMoveContext ac = (AMoveContext) c;
		this.initialPositions = new ArrayList<Point.Double>();
		this.replacedPlaceables = new ArrayList<Placeable>();
		this.selection = new ArrayList<Placeable>();
		
		for( int i = 0; i < ac.selection.size(); i++ )
		{
			Placeable p = ac.selection.get(i);
			Point.Double off = ac.offsets.get(i);
			
			p.delete();
			this.selection.add(p);
			this.initialPositions.add(new Point.Double(p.getX(), p.getY()));
			
			Placeable replaced = ac.targetLayer.attemptPlace(ac.locationX + off.x, ac.locationY + off.y, p);
			
			if( replaced != null )
			this.replacedPlaceables.add(replaced);
		}
	}
}
