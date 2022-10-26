package editor2d2.common.grid;

public class FloodFiller<T extends Gridable> {
	
		// Maximum number of flood fill iterations until the
		// FloodFiller terminates
	public static final int MAX_ITERATIONS = 100;
	

		// Grid that is to be filled
	protected Grid targetGrid;
	
		// Counter keeping track of the number of iterations
	protected int counter;
	
	
	public FloodFiller(Grid target) {
		this.targetGrid = target;
		this.counter = 0;
	}
	
	public FloodFiller() {
		this(null);
	}
	
	
		// Begins filling the target grid with given Gridable
		// starting at a given location (cellular)
	public Grid fillTarget(T g, int cx, int cy) {
		this.counter = 0;
		floodFill(g, cx, cy);
		
		return this.targetGrid;
	}
	
		// Runs a flood fill algorithm recursively
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
	
		// Checks whether a Gridable object can be placed in
		// a given cell in the target grid
	protected boolean isFree(T g, int cx, int cy) {
		if( this.targetGrid.get(cx, cy) instanceof NullCell )
		return false;
		
		return this.targetGrid.get(cx, cy) != g;
	}
	
		// Fills a given cell in the target Grid with a Gridable
		// if the cell is considered free
	protected boolean fillAt(T g, int cx, int cy) {
		if( !isFree(g, cx, cy) )
		return false;
		
		place(g, cx, cy);
		this.counter++;
		
		return true;
	}
	
		// Places a given Gridable at a given cellular position
		// in the target Grid
	protected void place(T g, int cx, int cy) {
		this.targetGrid.put(cx, cy, g);
	}
	
	
		// Sets the target Grid that is to be filled
	public void setTargetGrid(Grid targetGrid) {
		this.targetGrid = targetGrid;
	}
	
		// Returns a reference to the target Grid
	public Grid getTargetGrid() {
		return this.targetGrid;
	}
}
