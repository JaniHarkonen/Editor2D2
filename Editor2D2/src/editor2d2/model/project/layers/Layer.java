package editor2d2.model.project.layers;

import editor2d2.common.grid.Grid;
import editor2d2.common.grid.Gridable;
import editor2d2.model.project.Scene;
import editor2d2.model.project.scene.placeables.Placeable;

public abstract class Layer<T extends Placeable> {
	
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
	
	
		// Places a given Gridable object into a cell in the object grid
		// CAN BE OVERRIDDEN FOR MORE COMPLICATED PLACEABLES
	public void place(int cx, int cy, Placeable p) {
		p.setCellPosition(cx, cy);
		p.setOffsets(0, 0);
		p.changeLayer(this);
		
		this.objectGrid.put(cx, cy, p);
	}
	
		// Attempts to place a Gridable object into a cell checking it first
		// using the layer filter
		// THIS METHOD SHOULD BE FAVORED OVER place
		// ONLY USE place WHEN YOU KNOW THAT THE PLACEABLE IS ACCEPTED BY THE LAYER
	public void attemptPlace(int cx, int cy, Placeable p) {
		if( filterCheck(p) )
		place(cx, cy, p);
	}
	
		// Removes a Gridable object from a given cell replacing it with NULL
	public void delete(int cx, int cy) {
		place(cx, cy, null);
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
