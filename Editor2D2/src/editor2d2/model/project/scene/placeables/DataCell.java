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
	
	
	@Override
	public void draw(RenderContext rctxt) {
		
	}
	
		// Returns the cellular width of the data cell
	public int getCellWidth() {
		return this.cellHeight;
	}
	
		// Returns the cellular height of the data cell
	public int getCellHeight() {
		return this.cellHeight;
	}
	
		// Returns a reference to the Data object this cell is derived from
	public Data getData() {
		return this.data;
	}
	
		// Sets the cellular dimensions of the data cell
	public void setCellDimensions(int cw, int ch) {
		this.cellWidth = cw;
		this.cellHeight = ch;
	}
	
		// Changes the Data object reference
	public void changeData(Data data) {
		this.data = data;
	}
}
