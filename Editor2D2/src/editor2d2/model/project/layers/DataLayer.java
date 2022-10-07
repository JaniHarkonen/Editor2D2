package editor2d2.model.project.layers;

import editor2d2.common.grid.Gridable;
import editor2d2.model.project.Scene;
import editor2d2.model.project.scene.placeables.DataCell;
import editor2d2.model.project.scene.placeables.Placeable;

public class DataLayer extends Layer<DataCell> {
	
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
}
