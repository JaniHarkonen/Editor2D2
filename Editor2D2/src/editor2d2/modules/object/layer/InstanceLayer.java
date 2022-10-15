package editor2d2.modules.object.layer;

import editor2d2.common.grid.Gridable;
import editor2d2.common.grid.NullCell;
import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.Scene;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.modules.object.asset.EObject;
import editor2d2.modules.object.placeable.Instance;

public class InstanceLayer extends Layer {
	
		// Width of a grid cell in pixels
	public static int OBJECT_LAYER_GRID_CELL_WIDTH = 200;//1920;
	
		// Height of a grid cell in pixels
	public static int OBJECT_LAYER_GRID_CELL_HEIGHT = 200;//1080;
	
	
	public InstanceLayer(Scene scene) {
		super(scene, OBJECT_LAYER_GRID_CELL_WIDTH, OBJECT_LAYER_GRID_CELL_HEIGHT);
	}
	

		// Places an object to the given coordinates in the layer
		// and places it into the object grid
	@Override
	public void place(int x, int y, Placeable p) {
		place((double) x, y, (Instance) p);
	}
	
	@Override
	public void place(double x, double y, Placeable p) {
		Instance inst = (Instance) p;
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
	public Asset getReferencedAsset() {
		return new EObject();
	}
	
	
	@Override
	protected boolean filterCheck(Gridable p) {
		return p instanceof Instance;
	}
}
