package editor2d2.model.project.scene;

import java.util.ArrayList;

import editor2d2.common.grid.Grid;
import editor2d2.common.grid.Gridable;
import editor2d2.common.grid.NullCell;
import editor2d2.model.project.Asset;
import editor2d2.model.project.HasAsset;
import editor2d2.model.project.scene.placeable.Placeable;

public abstract class Layer implements HasAsset {
	
		// Reference to the Scene this layer belongs to
	protected Scene scene;

		// Objects placed on the layer
	protected Grid objectGrid;
	
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
		this.objectGrid = new Grid(Math.max(1, scene.getWidth() / cellWidth), Math.max(1, scene.getHeight() / cellHeight), cellWidth, cellHeight);
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
	public Placeable delete(int cx, int cy) {
		Gridable deleted = this.objectGrid.get(cx, cy);
		
		if( deleted == null || deleted instanceof NullCell )
		return null;
		
		this.objectGrid.remove(cx, cy);
		return (Placeable) deleted;
	}
	
	public Placeable delete(double x, double y) {
		return delete((int) (x / this.objectGrid.getCellWidth()), (int) (y / this.objectGrid.getCellHeight()));
	}
	
		// Removes all Placeables derived from a given Asset
		// CAN BE OVERRIDDEN FOR LAYERS THAT DONT STORE PLACEABLES
		// DIRECTLY
	public void deleteByAsset(Asset asset) {
		int cw = getObjecGridRowLength(),
			ch = getObjectGridColumnLength();
	
		for( int cx = 0; cx < cw; cx++ )
		for( int cy = 0; cy < ch; cy++ )
		{
			Placeable p = (Placeable) this.objectGrid.getFast(cx, cy);
			
			if( p == null || p.getAsset() != asset )
			continue;
			
			p.delete();
		}
	}
	
		// Resizes the object grid to fit the Scene this Layer
		// is a part of
	@SuppressWarnings("unchecked")
	public void resizeObjectGrid() {
		Grid grid = this.objectGrid;
		
			// Old cell dimensions
		double 	cw = grid.getCellWidth(),
				ch = grid.getCellHeight();
		
			// Old cellular dimensions
		int oldCols = grid.getRowLength(),
			oldRows = grid.getColumnLength();
		
			// New scene dimensions
		int sceneWidth = this.scene.getWidth(),
			sceneHeight = this.scene.getHeight();
		
			// New cellullar Scene dimensions
		int cols = (int) (this.scene.getWidth() / cw),
			rows = (int) (this.scene.getHeight() / ch);
		
		Grid resizedGrid = new Grid(cols, rows, (int) cw, (int) ch);
		
		for( int x = 0; x < oldCols; x++ )
		for( int y = 0; y < oldRows; y++ )
		{
			Gridable g = grid.getFast(x, y);
			
			if( g == null )
			continue;
			
			ArrayList<Placeable> collection = new ArrayList<Placeable>();
			
				// Handle Placeables and arrays of Placeables
			if( g instanceof Placeable )
			collection.add((Placeable) g);
			else
			collection = (ArrayList<Placeable>) g.getCollection();
			
			for( Placeable p : collection )
			{
				if( p == null )
				continue;
				
				if( p.getX() > sceneWidth || p.getY() > sceneHeight )
				continue;
				
				p.setCellPosition(x, y);
				resizedGrid.put(x, y, p);
			}
		}
		
		this.objectGrid = resizedGrid;
	}
	
	public ArrayList<Placeable> selectPlaceables(int cx, int cy) {
		ArrayList<Placeable> selection = new ArrayList<Placeable>();
	
		Placeable p = (Placeable) this.objectGrid.get(cx, cy);
		
		if( p != null )
		selection.add(p);
		
		return selection;
	}
	
	public ArrayList<Placeable> selectPlaceables(int cx1, int cy1, int cx2, int cy2) {
		ArrayList<Placeable> selection = new ArrayList<Placeable>();
		
		for( int x = cx1; x < cx2; x++ )
		for( int y = cy1; y < cy2; y++ )
		{
			Placeable p = (Placeable) this.objectGrid.get(x, y);
			if( p != null )
			selection.add(p);
		}
		
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
	
		// Returns a given X-coordinate snapped to the corresponding
		// cellular coordinate in the object Grid
	public int getCellX(double x) {
		return (int) (x / this.objectGrid.getCellWidth());
	}
	
		// Returns a given Y-coordinate snapped to the corresponding
		// cellular coordinate in the object Grid
	public int getCellY(double y) {
		return (int) (y / this.objectGrid.getCellHeight());
	}
	
	
		// Checks whether a Gridable object can be placed onto the layer
		// TO BE OVERRIDDEN
	protected abstract boolean filterCheck(Gridable p);
	
	
	
	/************************** GETTERS / SETTERS ***************************/
	
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
