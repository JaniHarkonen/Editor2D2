package editor2d2.common.grid;

/**
 * Instances of this class can be used to flood fill a Grid with 
 * given Gridables. A FloodFIller will continue filling a Grid 
 * until there are no more cells left to fill, an enclosed space 
 * has been filled or the MAX_ITERATIONS limit has been reached.
 * 
 * The flood fill implementation is standard, however, it has 
 * been split across multiple methods so each part can be 
 * overridden by possible subclasses.
 * 
 * This class can be extended, however, it is designed to function 
 * on its own as long as it targets Grids and uses Gridables. All 
 * subclasses should only expose the fillTarget-method (in addition 
 * to the constructors) to the "public" whereas the parts of the 
 * flood fill implementation should remain private or protected.
 * 
 * @author User
 *
 * @param <T> A Gridable object that the target Grid is
 * to be filled with.
 */
public class FloodFiller<T extends Gridable> {
	
	/**
	 * Maximum number of flood fill iterations until the 
	 * FloodFiller terminates.
	 */
	public static final int MAX_ITERATIONS = 1000;
	

	/**
	 * Target Grid object that is to be flood filled.
	 */
	protected Grid targetGrid;
	
	/**
	 * Counter that keeps track of the number of flood fill 
	 * iterations performed on the target Grid so far.
	 */
	protected int counter;
	
	
	/**
	 * Constructs a FloodFiller instance with a given target 
	 * Grid that is to be filled.
	 * 
	 * @param target Grid to be filled.
	 */
	public FloodFiller(Grid target) {
		this.targetGrid = target;
		this.counter = 0;
	}
	
	/**
	 * Constructs a FloodFiller instance with default settings 
	 * and a NULL target.
	 */
	public FloodFiller() {
		this(null);
	}
	
	
	/**
	 * Fills the current target Grid with a given Gridable using 
	 * flood fill algorithm. The fill will start at the given 
	 * cellular coordinates. If the given coordinate is already 
	 * filled with the given Gridable the algorithm will terminate.
	 * 
	 * @param g Gridable to fill the target Grid with.
	 * @param cx Cellular X-coordinate from which to start the fill.
	 * @param cy Cellular Y-coordinate from which to start the fill.
	 * 
	 * @return Returns the target Grid.
	 */
	public Grid fillTarget(T g, int cx, int cy) {
		this.counter = 0;
		floodFill(g, cx, cy);
		
		return this.targetGrid;
	}

	/**
	 * Flood fill algorithm implementation that attempts to fill a 
	 * given cellular coordinate in the target Grid and returns the 
	 * current counter count keeping track of the number of flood 
	 * fill iterations thus far.
	 * 
	 * If the cellular coordinate is already filled with the given 
	 * Gridable, no filling occurs and the counter is returned. 
	 * Otherwise, this method will be called recursively for all 
	 * surrounding cellular coordinates.
	 * 
	 * The flood fill algorithm has been split to floodFill, fillAt, 
	 * isFree and place. This way subclasses may override any or all 
	 * parts of the flood fill implementation making it versatile.
	 * 
	 * @param g Gridable to fill the target Grid with.
	 * @param cx Cellular X-coordinate to fill at.
	 * @param cy Cellular Y-coordinate to fill at.
	 * 
	 * @return Returns the counter keeping track of the number of 
	 * flood fill iterations thus far.
	 */
	protected int floodFill(T g, int cx, int cy) {
		if( counter < MAX_ITERATIONS && fillAt(g, cx, cy) == true )
		{
			floodFill(g, cx, cy - 1);
			floodFill(g, cx + 1, cy);
			floodFill(g, cx, cy + 1);
			floodFill(g, cx - 1, cy);
		}
		
		return counter;
	}
	
	/**
	 * Checks whether a given Gridable object can be placed in a 
	 * cell in the target Grid given its cellular coordinates. By 
	 * default, the cell is checked for a reference to the given 
	 * Gridable. If the cell is NULL, it is considered free. The 
	 * target Grid should utilize instances of NullCell to mark 
	 * forbidden cells.
	 * 
	 * @param g A Gridable to check the cellular coordinate of the 
	 * target Grid for.
	 * @param cx Cellular X-coordinate of the cell to check.
	 * @param cy Cellular Y-coordinate of the cell to check.
	 * 
	 * @return Returns whether the cellular coordinate is free for 
	 * flood fill.
	 */
	protected boolean isFree(T g, int cx, int cy) {
		if( this.targetGrid.get(cx, cy) instanceof NullCell )
		return false;
		
		return this.targetGrid.get(cx, cy) != g;
	}
	
	/**
	 * Attempts to fill a given cellular coordinate of the target 
	 * Grid with a Gridable. The isFree-method is first used to 
	 * determine if the cell is free and, thus, can be filled. 
	 * If the cell is free, then the Gridable will be placed in it 
	 * using the place-method. Returns whether the cell was filled.
	 * 
	 * @param g Gridable to fill the target Grid with.
	 * @param cx Cellular X-coordinate to fill at.
	 * @param cy Cellular Y-coordinate to fill at.
	 * 
	 * @return Returns whether the cell was filled.
	 */
	protected boolean fillAt(T g, int cx, int cy) {
		if( !isFree(g, cx, cy) )
		return false;
		
		place(g, cx, cy);
		this.counter++;
		
		return true;
	}

	/**
	 * Places a Gridable in the target Grid at a given cellular 
	 * coordinate.
	 * 
	 * @param g Gridable to place in the target Grid.
	 * @param cx Cellular X-coordinate in which to place the Gridable.
	 * @param cy Cellular Y-coordinate in which to place the Gridable.
	 */
	protected void place(T g, int cx, int cy) {
		this.targetGrid.put(cx, cy, g);
	}
	
	// GETTERS AND SETTERS
	
	/**
	 * Sets the target Grid that will be filled once fillTarget is 
	 * called.
	 * 
	 * @param targetGrid The target Grid that will be filled.
	 */
	public void setTargetGrid(Grid targetGrid) {
		this.targetGrid = targetGrid;
	}
	
	/**
	 * Returns a reference to the current target Grid.
	 * 
	 * @return Returns the target Grid.
	 */
	public Grid getTargetGrid() {
		return this.targetGrid;
	}
}
