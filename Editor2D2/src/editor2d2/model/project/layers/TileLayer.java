package editor2d2.model.project.layers;

import editor2d2.common.grid.Gridable;
import editor2d2.model.project.Scene;
import editor2d2.model.project.scene.placeables.Placeable;
import editor2d2.model.project.scene.placeables.Tile;

public class TileLayer extends Layer<Tile> {
	
		// Size of a cell in the grid
	private int gridSize;
	
	
	public TileLayer(Scene scene, int gridSize) {
		super(scene, gridSize, gridSize);
		this.gridSize = gridSize;
	}
	

	@Override
	protected boolean filterCheck(Gridable p) {
		return p instanceof Tile;
	}
}