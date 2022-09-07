package editor2d2.model.project.layers;

import editor2d2.common.grid.Grid;
import editor2d2.common.grid.Gridable;
import editor2d2.model.project.Scene;
import editor2d2.model.project.scene.placeables.Placeable;

public abstract class Layer<T extends Placeable> {
	
		// Reference to the Scene this layer belongs to
	protected Scene scene;

		// Objects placed on the layer
	protected Grid objectGrid;
	
		// Name of the layer
	protected String name;
	
		// Whether the layer and its contents are being drawn
	protected boolean isVisible;
	
		// The opacity of the layer
	protected double opacity;
	
	
	protected Layer(Scene scene, int cellWidth, int cellHeight) {
		this.scene = scene;
		this.objectGrid = new Grid(scene.getWidth() / cellWidth, scene.getHeight() / cellHeight);
	}
	
	
		// TO BE OVERRIDDEN
	public void place(int cx, int cy, Gridable p) {
		this.objectGrid.put(cx, cy, p);
	}
}
