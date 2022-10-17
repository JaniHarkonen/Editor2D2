package editor2d2.model.project.scene;

import java.util.ArrayList;

import editor2d2.common.grid.Grid;
import editor2d2.common.grid.Gridable;
import editor2d2.model.project.HasAsset;
import editor2d2.model.project.scene.placeable.Placeable;

public abstract class Layer implements HasAsset {
	
		// Reference to the Scene this layer belongs to
	protected Scene scene;

		// Objects placed on the layer
	protected final Grid objectGrid;
	
		// Name of the layer
	protected String name;
	
		// Whether the layer and its contents are being drawn
	protected boolean isVisible;
	
		// The opacity of the layer (0 - 1)
	protected double opacity;
	
		// Placement of the layer in the layer list of a Scene
	protected int index;
	
	
	protected Layer(Scene scene, int cellWidth, int cellHeight) {
		this.scene = scene;
		this.objectGrid = new Grid(scene.getWidth() / cellWidth, scene.getHeight() / cellHeight, cellWidth, cellHeight);
		this.name = null;
		this.isVisible = true;
		this.opacity = 1.0;
		this.index = -1;
	}
	
	
	/**
	 * Converts an opacity value between 0 and 255 into a percentage
	 * between 0.0 and 1.0.
	 * @param opacity255 Opacity value between 255.
	 * @return Returns a percentage value between 0.0 - 1.0.
	 */
	public static double opacity255ToPercentage(double opacity255) {
		return Math.min(1d, Math.max(0d, opacity255 / 255d));
	}
	
	/**
	 * Converts a percentage value between 0.0 and 1.0 into an opacity
	 * value between 0 and 255.
	 * @param percentage Percentage value between 0.0 and 1.0.
	 * @return Returns an opacity value between 0 and 255.
	 */
	public static double opacityPercentageTo255(double percentage) {
		return Math.min(255d, Math.max(0d, percentage * 255d));
	}
	
	
		// Places a given Gridable object into a cell in the object grid
		// CAN BE OVERRIDDEN FOR MORE COMPLICATED PLACEABLES
	public void place(int cx, int cy, Placeable p) {
		p.setCellPosition(cx, cy);
		p.setOffsets(0, 0);
		p.changeLayer(this);
		
		this.objectGrid.put(cx, cy, p);
	}
	
		// Places a given Placeable object into a cell in the object grid
		// converting its coordinates into cellular coordinates
		// CAN BE OVERRIDDEN FOR LAYERS THAT NEED TO RESPECT THE ACTUAL
		// COORDINATES
	public void place(double x, double y, Placeable p) {
		place((int) (x / this.objectGrid.getCellWidth()), (int) (y / this.objectGrid.getCellHeight()), p);
	}
	
		// Attempts to place a Gridable object into a cell checking it first
		// using the layer filter
		// THIS METHOD SHOULD BE FAVORED OVER place
		// ONLY USE place WHEN YOU KNOW THAT THE PLACEABLE IS ACCEPTED BY THE LAYER
	public void attemptPlace(int cx, int cy, Placeable p) {
		if( filterCheck(p) )
		place(cx, cy, p);
	}
	
		// Attempts to place a Placeable object into a cell checking it first
		// using the layer filter. The coordinates will be converted into
		// cellular coordinates.
		// CAN BE OVERRIDDEN FOR LAYERS THAT NEED TO RESPECT THE ACTUAL
		// COORDIANTES
	public void attemptPlace(double x, double y, Placeable p) {
		if( filterCheck(p) )
		place(x, y, p);
	}
	
	
		// Removes a Gridable object from a given cell replacing it with NULL
	public void delete(int cx, int cy) {
		place(cx, cy, null);
	}
	
	public ArrayList<Placeable> selectPlaceables(int cx1, int cy1, int cx2, int cy2) {
		ArrayList<Placeable> selection = new ArrayList<Placeable>();
		
		for( int x = cx1; x < cx2; x++ )
		for( int y = cy1; y < cy2; y++ )
		selection.add((Placeable) this.objectGrid.get(x, y));
		
		return selection;
	}
	
		// Returns a list of Placeables in the Placeable grid inside
		// a given rectangle
		// CAN BE OVERRIDDEN, BY DEFAULT THIS ONLY TAKES INTO ACCOUNT
		// THE CELLULAR COORDINATES OF THE RECTANGLE
	public ArrayList<Placeable> selectPlaceables(double x1, double y1, double x2, double y2) {
		Grid grid = this.objectGrid;
		
		int cx1 = grid.clampX((int) (x1 / grid.getCellWidth())),
			cy1 = grid.clampY((int) (y1 / grid.getCellHeight())),
			cx2 = grid.clampX((int) (x2 / grid.getCellWidth()) + 1),
			cy2 = grid.clampY((int) (y2 / grid.getCellHeight()) + 1);
		
		return selectPlaceables(cx1, cy1, cx2, cy2);
	}
	
	public ArrayList<Placeable> selectPlaceables(double x, double y) {
		return selectPlaceables(x, y, x, y);
	}
	
	
		// Checks whether a Gridable object can be placed onto the layer
		// TO BE OVERRIDDEN
	protected abstract boolean filterCheck(Gridable p);
	
	
		// Returns a reference to the Scene the layer belongs to
	public Scene getScene() {
		return this.scene;
	}
	
		// Returns a reference to the object grid
	public Grid getObjectGrid() {
		return this.objectGrid;
	}
	
		// Returns the cellular width of the object grid
	public int getObjecGridRowLength() {
		return this.objectGrid.getRowLength();
	}
	
		// Returns the cellular height of the object grid
	public int getObjectGridColumnLength() {
		return this.objectGrid.getColumnLength();
	}
	
		// Returns the name of the layer
	public String getName() {
		return this.name;
	}
	
		// Returns whether the layer is visible
	public boolean checkVisible() {
		return this.isVisible;
	}
	
		// Returns the opacity of the layer
	public double getOpacity() {
		return this.opacity;
	}
	
		// Returns the placement of the layer in the Scene's layer list
	public int getIndex() {
		return this.index;
	}
	
	
		// Sets the Scene the layer belongs to
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
		// Sets the name of the layer
	public void setName(String name) {
		this.name = name;
	}
	
		// Sets the visibility of the layer
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
		// Toggles the visibility of the layer and returns the new visibility
	public boolean toggleVisibility() {
		return this.isVisible = !this.isVisible;
	}
	
		// Sets the opacity of the layer
	public void setOpacity(double opacity) {
		this.opacity = opacity;
	}
	
		// Sets the placement of the layer in the Scene's layer list
	public void setIndex(int index) {
		if( index >= 0 )
		this.index = index;
	}
	
		// Adjusts the opacity by a given amount
	public void adjustOpacity(double adjustment) {
		this.opacity += adjustment;
	}
	
		// Increases the opacity by a given amount
	public void increaseOpacity(double increment) {
		adjustOpacity(increment);
	}
	
		// Decreases the opacity by a given amount
	public void decreaseOpacity(double decrement) {
		adjustOpacity(decrement);
	}
}
