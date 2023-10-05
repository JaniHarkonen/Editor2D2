package editor2d2.common.grid;

import java.util.HashMap;
import java.util.Map;

/**
 * Grid is a grid-like data structure that utilizes a Map
 * to represent a grid and its integer keys to represent 
 * the cellular coordinates. Grid only accepts instances of 
 * classes that implement Gridable. When a cell in the Grid 
 * contains a null value, it is considered empty. A NullCell 
 * instance can be used to mark a forbidden cell.
 * 
 * @author User
 *
 */
public class Grid {

	/**
	 * Map that functions as the grid where each cell is 
	 * paired with an integer key representing its cellular 
	 * coordinate. The Map accepts Gridables as contents for 
	 * its cells.
	 */
	protected Map<Integer, Gridable> grid;
	
	/**
	 * Width of a cell in the grid.
	 */
	protected int cellWidth;
	
	/**
	 * Height of a cell in the grid.
	 */
	protected int cellHeight;
	
	/**
	 * Number of horizontal cells in the grid (cellular width).
	 */
	protected int rowLength;

	/**
	 * Number of vertical cells in the grid (cellular height).
	 */
	protected int columnLength;

	
	/**
	 * Constructs a Grid instance with given cellular dimensions.
	 * By default, the Grid contains no values.
	 * 
	 * @param cellWidth Number of horizontal cells in the grid 
	 * (cellular width).
	 * @param cellHeight Number of vertical cells in the grid 
	 * (cellular height).
	 */
	public Grid(int cellWidth, int cellHeight) {
		this.grid = new HashMap<Integer, Gridable>();
		this.rowLength = cellWidth;
		this.columnLength = cellHeight;
	}
	
	/**
	 * Constructs a Grid instance with given cellular dimensions 
	 * and gives the cells given dimensions.
	 * 
	 * @param width Width of a cell.
	 * @param height Height of a cell.
	 * @param cellWidth Number of horizontal cells in the grid 
	 * (cellular width).
	 * @param cellHeight Number of vertical cells in the grid 
	 * (cellular height).
	 */
	public Grid(int width, int height, int cellWidth, int cellHeight) {
		this(width, height);
		
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
	}
	
	/**
	 * Snaps a value into a grid of a given size.
	 * 
	 * @param value Value to snap to the grid.
	 * @param gridSize The size of a cell in the grid.
	 * 
	 * @return Returns the value snapped into a grid.
	 */
	public static double snapToGrid(double value, int gridSize) {
		return ((int) (value / gridSize)) * gridSize;
	}
	
	/**
	 * Returns a reference to the Gridable object stored at 
	 * given cellular coordinates. <b>Notice:</b> this is a 
	 * fast method. It doesn't take the boundaries of the 
	 * grid into account and may cause an error. Use this 
	 * method only when high degree of optimization is needed
	 * and the cell is known to lie within the boundaries of 
	 * the grid.
	 * 
	 * If the cell is empty, NULL will be returned.
	 * 
	 * @param cx Cellular X-coordinate of the cell whose 
	 * contents to return.
	 * @param cy Cellular Y-coordinate of the cell whose
	 * contents to return.
	 * 
	 * @return Returns a reference to the Gridable object 
	 * stored at the cell.
	 */
	public Gridable getFast(int cx, int cy) {
		return this.grid.get(cy * this.rowLength + cx);
	}
	
	/**
	 * Returns a reference to the Gridable object stored at
	 * given cellular coordinate. If the cell is empty, NULL 
	 * will be returned. If the cell is out of bounds, a 
	 * NullCell instance will be created and returned.
	 * 
	 * @param cx Cellular X-coordinate of the cell whose 
	 * contents to return.
	 * @param cy Cellular Y-coordinate of the cell whose 
	 * contents to return.
	 * 
	 * @return Returns a reference to the Gridable object 
	 * stored at the cell.
	 */
	public Gridable get(int cx, int cy) {
		if( !checkCellValid(cx, cy) )
		return new NullCell();
		
		return getFast(cx, cy);
	}

	/**
	 * Returns a reference to the Gridable object stored at 
	 * given coordinates. This method accepts the X-and Y-
	 * coordinates and determines their cellular coordinates. 
	 * Finally the Gridable stored at the corresponding cell 
	 * will be returned.
	 * 
	 * If the cell is empty, NULL will be returned. If the 
	 * cell is out of boundsNullCell instance will be created 
	 * and returned.
	 * 
	 * @param x Actual X-coordinate at which the target cell 
	 * should lie.
	 * @param y Actual Y-coordinate at which the target cell 
	 * should lie. 
	 * 
	 * @return Returns a reference to the Gridable object 
	 * stored at a cell that the given coordinate lies at.
	 */
	public Gridable get(double x, double y) {
		return get((int) x / this.cellWidth, (int) y / this.cellHeight);
	}

	/**
	 * Puts a Gridable object at a cell at given cellular 
	 * coordinates. If the cell is out of bounds, nothing 
	 * happens.
	 * 
	 * @param cx Cellular X-coordinate of the cell to which 
	 * the Gridable objefct will be placed.
	 * @param cy Cellular Y-coordinate of the cell to which 
	 * the Gridable objefct will be placed.
	 * 
	 * @param element The Gridable object that should be 
	 * placed in the cell.
	 */
	public void put(int cx, int cy, Gridable element) {
		if( !checkCellValid(cx, cy) )
		return;
		
		this.grid.put(cy * this.rowLength + cx, element);
	}
	
	/**
	 * Puts a Gridable object at a cell at given X- and Y-
	 * coordinates. This method accepts the actual X- and 
	 * Y-coordinates and determines the cellular coordinates 
	 * based off of them. If the cell is out of bounds, 
	 * nothing happens.
	 * 
	 * @param x Actual X-coordinate where the target cell 
	 * should lie.
	 * @param y Actual Y-coordinate where the target cell 
	 * should lie.
	 * 
	 * @param element The Gridable object that should be 
	 * placed in the cell that lies at the given 
	 * coordinates.
	 */
	public void put(double x, double y, Gridable element) {
		put((int) x / this.cellWidth, (int) y / this.cellHeight, element);
	}
	
	/**
	 * Removes the Gridable object stored at a cell with 
	 * the given cellular coordinates. This method works 
	 * by putting a NULL object in the cell. If the cell 
	 * is out of bounds, nothing happens. 
	 * 
	 * @param cx Cellular X-coordinate of the cell whose 
	 * contents to remove.
	 * @param cy Cellular Y-coordinate of the cell whose 
	 * contents to remove.
	 */
	public void remove(int cx, int cy) {
		put(cx, cy, null);
	}
	
	/**
	 * Removes the Gridable object of a cell that lies 
	 * under the given X- and Y-coordinates. This method 
	 * accepts the actualy X- and Y-coordinates and 
	 * determines the cellular coordinates based off of 
	 * them. If the cell is out of bounds, nothing 
	 * happens.
	 * 
	 * @param x Actual X-coordinate at which the cell 
	 * lies.
	 * @param y Actual Y-coordinate at which the cell 
	 * lies.
	 */
	public void remove(double x, double y) {
		put(x, y, null);
	}
	
	/**
	 * Clamps a cellular X-coordinate to fit the cellular 
	 * dimensions of this grid.
	 * 
	 * @param cx Cellular X-coordinate to clamp.
	 * 
	 * @return The cellular X-coordinate clamped to this 
	 * grid's cellular width.
	 */
	public int clampX(int cx) {
		return Math.min(this.rowLength, Math.max(0, cx));
	}
	
	/**
	 * Clamps a cellular Y-coordinate to fit the cellular 
	 * dimensions of this grid.
	 * 
	 * @param cy Cellular Y-coordinate to clamp.
	 * 
	 * @return The cellular Y-coordinate clamped to this 
	 * grid's cellular height.
	 */
	public int clampY(int cy) {
		return Math.min(this.columnLength, Math.max(0, cy));
	}
	
	
		// GETTERS/SETTERS
	
	/**
	 * Returns the width of a cell in the grid.
	 * 
	 * @return Returns the cell width.
	 */
	public int getCellWidth() {
		return this.cellWidth;
	}
	
	/**
	 * Returns the height of a cell in the grid.
	 * 
	 * @return Returns the cell height.
	 */
	public int getCellHeight() {
		return this.cellHeight;
	}
	
	/**
	 * Retruns the number of horizontal cells in the 
	 * grid (cellular width).
	 * 
	 * @return Returns the cellular width.
	 */
	public int getRowLength() {
		return this.rowLength;
	}
	
	/**
	 * Retruns the number of vertical cells in the 
	 * grid (cellular height).
	 * 
	 * @return Returns the cellular height.
	 */
	public int getColumnLength() {
		return this.columnLength;
	}
	
	/**
	 * Sets the width and the height of the cells in the grid.
	 * <b>Notice: </b> this method does NOT affect the placement 
	 * of the Gridables already placed in the grid. That means 
	 * changing the dimensions of the cells will shift all the 
	 * contents of the grid by the same amount.
	 * 
	 * @param cellWidth The new width of a cell.
	 * @param cellHeight The new height of a cell.
	 */
	public void setDimensions(int cellWidth, int cellHeight) {
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
	}
	
	/**
	 * Resizes the cellular dimensions of the grid. <b>Notice: </b>
	 * this method WILL impact the placement of the Gridables in 
	 * the grid. If the number of cells increases some Gridables 
	 * may overlap resulting in a loss of data. Essentially, this 
	 * method creates a new Map and copies the Gridables of the 
	 * old map into it at new positions. The new Map will then 
	 * be used as the grid.
	 * 
	 * This method should be avoided in general as it may lead 
	 * to unexpected behavior, however, calling it on an empty 
	 * grid is safe.
	 * 
	 * @param rowLength The new number of horizontal cells 
	 * (new cellular width).
	 * @param columnLength The new number of vertical cells 
	 * (new cellular height).
	 */
	public void setCellDimensions(int rowLength, int columnLength) {
		Map<Integer, Gridable> newMap = new HashMap<Integer, Gridable>();
		
		int x2 = Math.min(rowLength, this.rowLength),
			y2 = Math.min(columnLength, this.columnLength);
		
		for( int x = 0; x < x2; x++ )
		{
			for( int y = 0; y < y2; y++ )
			newMap.put(y * rowLength + x, getFast(x, y));
		}
		
		this.rowLength = rowLength;
		this.columnLength = columnLength;
		this.grid = newMap;
	}
	
	
	/**
	 * Internal method to determine whether a given cellular 
	 * coorindate is valid. By default, only the boundaries of 
	 * the grid are tested. 
	 * 
	 * @param cx Cellular X-coorindate of the cell.
	 * @param cy Cellular Y-coordinate of the cell.
	 * 
	 * @return Returns whether the cell can be accessed.
	 */
	protected boolean checkCellValid(int cx, int cy) {
		return (cx >= 0 && cy >= 0 && cx < this.rowLength && cy < this.columnLength);
	}
}
