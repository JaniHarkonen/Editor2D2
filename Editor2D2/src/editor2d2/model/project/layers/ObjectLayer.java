package editor2d2.model.project.layers;

import editor2d2.common.grid.Gridable;
import editor2d2.common.grid.NullCell;
import editor2d2.model.project.Scene;
import editor2d2.model.project.scene.placeables.Instance;
import editor2d2.model.project.scene.placeables.Placeable;

public class ObjectLayer extends Layer {
	
		// Width of a grid cell in pixels
	public static int OBJECT_LAYER_GRID_CELL_WIDTH = 200;//1920;
	
		// Height of a grid cell in pixels
	public static int OBJECT_LAYER_GRID_CELL_HEIGHT = 200;//1080;
	
	
	public ObjectLayer(Scene scene) {
		super(scene, OBJECT_LAYER_GRID_CELL_WIDTH, OBJECT_LAYER_GRID_CELL_HEIGHT);
	}
	

		// Places an object to the given coordinates in the layer
		// and places it into the object grid
	@Override
	public void place(int x, int y, Placeable p) {
		place((double) x, y, (Instance) p);
	}
	
	public void place(double x, double y, Instance inst) {
		Gridable cell = this.objectGrid.get(x, y);
		
			// Out of bounds
		if( cell instanceof NullCell )
		return;
		
			// No objects exist in the cell, create a new object list
			// to represent the cell
		if( cell == null )
		{
			cell = new ObjectArray();
			this.objectGrid.put(x, y, cell);
		}
		
		inst.changeLayer(this);
		
		int cx = (int) (x / this.objectGrid.getCellWidth()),
			cy = (int) (y / this.objectGrid.getCellHeight());
		
		inst.setCellPosition(cx, cy);
		inst.setOffsets(x - cx, y - cy);
		
		((ObjectArray) cell).add(inst);
	}


	@Override
	protected boolean filterCheck(Gridable p) {
		return p instanceof Instance;
	}
}
