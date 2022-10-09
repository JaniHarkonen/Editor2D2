package editor2d2.modules.image;

import editor2d2.common.grid.Gridable;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.Scene;

public class TileLayer extends Layer {
	
		// Size of a cell in the grid
	private int gridSize;
	
	
	public TileLayer(Scene scene, int gridSize) {
		super(scene, gridSize, gridSize);
		this.gridSize = gridSize;
	}
	
	public TileLayer(Scene scene) {
		this(scene, 32);
	}
	

	@Override
	protected boolean filterCheck(Gridable p) {
		return p instanceof Tile;
	}
	
	
		// Returns the size of a grid cell
	public int getGridSize() {
		return this.gridSize;
	}
}
