package editor2d2.model.project.layers;

import editor2d2.common.grid.Gridable;
import editor2d2.common.grid.NullCell;
import editor2d2.model.project.Scene;
import editor2d2.model.project.scene.placeables.Instance;

public class ObjectLayer extends Layer<Instance> {
	
		// Width of a grid cell in pixels
	public static int OBJECT_LAYER_GRID_CELL_WIDTH = 1920;
	
		// Height of a grid cell in pixels
	public static int OBJECT_LAYER_GRID_CELL_HEIGHT = 1080;
	
	
	public ObjectLayer(Scene scene) {
		super(scene, OBJECT_LAYER_GRID_CELL_WIDTH, OBJECT_LAYER_GRID_CELL_HEIGHT);
	}
	

		// Places an object to the given coordinates in the layer
		// and places it into the object grid
	public void place(double x, double y, Instance p) {
		Gridable cell = this.objectGrid.get(x, y);
		
		if( cell instanceof NullCell )
		return;
		
		if( cell == null )
		cell = new ObjectArray();
		
		p.setPosition(x, y);
		((ObjectArray) cell).add(p);
	}
}
