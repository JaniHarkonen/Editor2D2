package editor2d2.model.project.scene.placeables;

import editor2d2.common.grid.Gridable;
import editor2d2.model.project.layers.Layer;

public abstract class Placeable implements Gridable, Drawable {
	
		// X-coordinate offset from the placement layer cell
	protected double xOffset;
	
		// Y-coordinate offset from the placement layer cell
	protected double yOffset;
	
		// Cellular X-coordinate in the placement layer
	protected int cellX;
	
		// Cellular Y-coordinate in the placement layer
	protected int cellY;
	
		// Reference to the layer the placeable is placed in
	protected Layer<? extends Placeable> layer;
	
	
	protected Placeable() {
		this.xOffset = 0;
		this.yOffset = 0;
		this.cellX = -1;
		this.cellY = -1;
		this.layer = null;
	}
	

		// Draws the placeable into a given Graphics2D-context
		// TO BE OVERRIDDEN
	@Override
	public void draw(RenderContext rctxt) { }
	
		// Returns the X-coordinate of the placeable
		// CAN BE OVERRIDDEN
	public double getX() {
		return this.cellX * this.layer.getObjectGrid().getCellWidth() + this.xOffset;
	}
	
		// Returns the X-coordinate of the placeable
		// CAN BE OVERRIDDEN
	public double getY() {
		return this.cellY * this.layer.getObjectGrid().getCellHeight() + this.yOffset;
	}
	
		// Places the placeable onto a given layer
		// CAN BE OVERRIDDEN
	public void place(Layer<? extends Placeable> target, int cx, int cy) {
		if( target == null )
		return;
		
		target.place(cx, cy, this);
		setCellPosition(cx, cy);
		setOffsets(0, 0);
		this.layer = target;
	}
	
	
		// Returns the offset on the X-axis
	public double getXOffset() {
		return this.xOffset;
	}
	
		// Returns the offset on the Y-axis
	public double getYOffset() {
		return this.yOffset;
	}
	
		// Returns the cellular X-coordinate
	public int getCellX() {
		return this.cellX;
	}
	
		// Returns the cellular Y-coordinate
	public int getCellY() {
		return this.cellY;
	}
	
		// Returns a reference to the layer the placeable is placed in
	public Layer<? extends Placeable> getLayer() {
		return this.layer;
	}
	
		// Sets the coordinate offsets
	public void setOffsets(double xoff, double yoff) {
		this.xOffset = xoff;
		this.yOffset = yoff;
	}
	
		// Sets the cellular position
	public void setCellPosition(int cx, int cy) {
		this.cellX = cx;
		this.cellY = cy;
	}
}
