package editor2d2.model.project.scene.placeables;

import editor2d2.common.grid.Gridable;
import editor2d2.model.project.assets.Data;

public class DataCell extends Placeable implements Gridable {

		// Cellular width of the data cell
	private int cellWidth;
	
		// Cellular height of the data cell
	private int cellHeight;
	
		// Reference to the Data object this cell is derived from
	private Data data;
	
}
