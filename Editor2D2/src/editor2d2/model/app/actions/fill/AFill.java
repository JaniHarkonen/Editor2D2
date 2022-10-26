package editor2d2.model.app.actions.fill;

import editor2d2.common.grid.Grid;
import editor2d2.model.app.actions.Action;
import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.modules.object.placeable.Instance;

public class AFill extends Action {

	@Override
	public void undo() {
		
	}

	@Override
	public void redo() {
		
	}

	@Override
	public void performImpl(ActionContext c) {
		AFillContext ac = (AFillContext) c;
		
		if( ac.placeable instanceof Instance )
		return;
		
		Grid ogrid = ac.target.getObjectGrid();
		int cw = ogrid.getCellWidth(),
			ch = ogrid.getCellHeight();
		
			// Fill the tiles using flood fill
		ac.placeable.changeLayer(ac.target);
		ac.placeable.setOffsets(0, 0);
		floodFill(ogrid, (int) (ac.locationX / cw), (int) (ac.locationY / ch), ac.placeable, 0, 100);
	}
	
	private int floodFill(Grid grid, int cx, int cy, Placeable p, int counter, int max) {
		if( !isFree(grid, cx, cy, p) || counter >= max )
		return counter;
		
		counter++;
		
		p.setCellPosition(cx, cy);
		grid.put(cx, cy, p);
		p = p.duplicate();
		
		if( isFree(grid, cx, cy - 1, p) )
		{
			counter = floodFill(grid, cx, cy - 1, p, counter, max);
			p = p.duplicate();
		}
		
		if ( isFree(grid, cx + 1, cy, p) )
		{
			counter = floodFill(grid, cx + 1, cy, p, counter, max);
			p = p.duplicate();
		}
		
		if ( isFree(grid, cx, cy + 1, p) )
		{
			counter = floodFill(grid, cx, cy + 1, p, counter, max);
			p = p.duplicate();
		}
		
		if ( isFree(grid, cx - 1, cy, p) )
		{
			counter = floodFill(grid, cx - 1, cy, p, counter, max);
			p = p.duplicate();
		}
		
		return counter;
	}
	
	private boolean isFree(Grid grid, int cx, int cy, Placeable p) {
		String testIdentifier= p.getAsset().getIdentifier();
		Placeable otherPlaceable = (Placeable) grid.get(cx, cy);
		
		if( otherPlaceable == null )
		return true;
		
		return !otherPlaceable.getAsset().getIdentifier().equals(testIdentifier);
	}
}
