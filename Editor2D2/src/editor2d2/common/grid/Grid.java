package editor2d2.common.grid;

import java.util.HashMap;
import java.util.Map;

public class Grid {
	
		// Mapping of coordinates to the elements stored in the grid
	protected Map<Integer, Gridable> grid;
	
		// Width of a cell
	protected int cellWidth;
	
		// Height of a cell
	protected int cellHeight;
	
		// Number of cells in a row
	protected int rowLength;
	
		// Number of rows in the grid
	protected int columnLength;

	
	public Grid(int width, int height) {
		this.grid = new HashMap<Integer, Gridable>();
		this.rowLength = width;
		this.columnLength = height;
	}
	
	public Grid(int width, int height, int cellWidth, int cellHeight) {
		this(width, height);
		
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
	}
	
	
	public static double snapToGrid(double value, int gridSize) {
		return ((int) (value / gridSize)) * gridSize;
	}
	
	
		// Returns the element stored in a given cell with no
		// additional checks for slightly faster performance
	public Gridable getFast(int cx, int cy) {
		return this.grid.get(cy * this.rowLength + cx);
	}
	
		// Returns the element stored in a given cell
		// or NULL if no element is in the cell
		// or NullCell if the cell was invalid
	public Gridable get(int cx, int cy) {
		if( !checkCellValid(cx, cy) )
		return new NullCell();
		
		return getFast(cx, cy);
	}
	
		// Returns the element stored in a cell given its
		// coordinate or NULL if no element is in the cell
		// or NullCell if the cell was invalid
	public Gridable get(double x, double y) {
		return get((int) x / this.cellWidth, (int) y / this.cellHeight);
	}
	
		// Puts an element in a given cell
	public void put(int cx, int cy, Gridable element) {
		if( !checkCellValid(cx, cy) )
		return;
		
		this.grid.put(cy * this.rowLength + cx, element);
	}
	
		// Puts an element in a cell given its coordinate
	public void put(double x, double y, Gridable element) {
		put((int) x / this.cellWidth, (int) y / this.cellHeight, element);
	}
	
	public void remove(int cx, int cy) {
		put(cx, cy, null);
	}
	
		// Removes an element in a cell by setting it to null
	public void remove(double x, double y) {
		put(x, y, null);
	}
	
		// Clamps a cellular X-coordinate to fit this Grid
	public int clampX(int cx) {
		return Math.min(this.rowLength, Math.max(0, cx));
	}
	
		// Clamps a cellular Y-coordinate to fit this Grid
	public int clampY(int cy) {
		return Math.min(this.columnLength, Math.max(0, cy));
	}
	
	
		// GETTERS/SETTERS
	
		// Returns the width of a cell in the grid
	public int getCellWidth() {
		return this.cellWidth;
	}
	
		// Returns the height of a cell in the grid
	public int getCellHeight() {
		return this.cellHeight;
	}
	
		// Returns the number of cells in a row in the grid
	public int getRowLength() {
		return this.rowLength;
	}
	
		// Returns the number of cells in a column in the grid
	public int getColumnLength() {
		return this.columnLength;
	}
	
		// Sets the width and the height of the cells
	public void setDimensions(int cellWidth, int cellHeight) {
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
	}
	
		// Sets the cellular dimensions of the grid
	public void setCellDimensions(int rowLength, int columnLength) {
		Map<Integer, Gridable> newMap = new HashMap<Integer, Gridable>();
		
		for( int x = 0; x < this.rowLength; x++ )
		{
			for( int y = 0; y < this.columnLength; y++ )
			newMap.put(y * rowLength + x, get(x, y));
		}
		
		this.rowLength = rowLength;
		this.columnLength = columnLength;
	}
	
	
		// Checks whether a cellular coordinate is valid
	protected boolean checkCellValid(int cx, int cy) {
		return !(cx < 0 || cy < 0 || cx > this.rowLength || cy > this.columnLength);
	}
}
