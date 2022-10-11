package editor2d2.modules.image.layer;

import editor2d2.common.grid.Gridable;
import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.Scene;
import editor2d2.modules.image.asset.Image;
import editor2d2.modules.image.placeable.Tile;

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

	@Override
	public Asset getReferencedAsset() {
		return new Image();
	}
}
