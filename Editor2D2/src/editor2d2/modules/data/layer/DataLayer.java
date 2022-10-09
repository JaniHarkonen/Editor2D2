package editor2d2.modules.data.layer;

import editor2d2.common.grid.Gridable;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.Scene;
import editor2d2.modules.data.placeable.DataCell;

public class DataLayer extends Layer {
	
		// Size of a cell in the grid
	private int gridSize;
	
	
	public DataLayer(Scene scene, int gridSize) {
		super(scene, gridSize, gridSize);
		this.gridSize = gridSize;
	}
	

	@Override
	protected boolean filterCheck(Gridable p) {
		return p instanceof DataCell;
	}
	
	
		// Returns the size of a grid cell
	public int getGridSize() {
		return this.gridSize;
	}
}
