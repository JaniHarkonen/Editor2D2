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
		floodFill(0, ogrid, (int) (ac.locationX / cw), (int) (ac.locationY / ch), ac.placeable, 0, 100);
	}
	
	private static final int LAST_MOVE_UP = 1;
	private static final int LAST_MOVE_RIGHT = 2;
	private static final int LAST_MOVE_DOWN = 3;
	private static final int LAST_MOVE_LEFT = 4;
	
	private int floodFill(int lastMove, Grid grid, int cx, int cy, Placeable p, int counter, int max) {
		if( grid.get(cx, cy) != null || counter >= max )
		return counter;
		
		counter++;
		
		p.setCellPosition(cx, cy);
		grid.put(cx, cy, p);
		p = p.duplicate();
		
		if( lastMove != LAST_MOVE_DOWN && grid.get(cx, cy - 1) == null )
			counter = floodFill(LAST_MOVE_UP, grid, cx, cy - 1, p, counter, max);
		if ( lastMove != LAST_MOVE_LEFT && grid.get(cx + 1, cy) == null )
			counter = floodFill(LAST_MOVE_RIGHT, grid, cx + 1, cy, p, counter, max);
		if ( lastMove != LAST_MOVE_UP && grid.get(cx, cy + 1) == null )
			counter = floodFill(LAST_MOVE_DOWN, grid, cx, cy + 1, p, counter, max);
		if ( lastMove != LAST_MOVE_RIGHT && grid.get(cx - 1, cy) == null )
			counter = floodFill(LAST_MOVE_LEFT, grid, cx - 1, cy, p, counter, max);
		
		return counter;
	}
}
