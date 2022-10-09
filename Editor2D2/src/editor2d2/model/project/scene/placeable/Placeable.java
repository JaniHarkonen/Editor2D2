package editor2d2.model.project.scene.placeable;

import editor2d2.common.grid.Gridable;
import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.Layer;

public abstract class Placeable implements Gridable, Drawable {
	
		// Reference to the Asset that the Placeable is based on
	protected Asset asset;
	
		// X-coordinate offset from the placement layer cell
	protected double xOffset;
	
		// Y-coordinate offset from the placement layer cell
	protected double yOffset;
	
		// Cellular X-coordinate in the placement layer
	protected int cellX;
	
		// Cellular Y-coordinate in the placement layer
	protected int cellY;
	
		// Reference to the layer the placeable is placed in
	protected Layer layer;
	
	
	protected Placeable() {
		this.asset = null;
		this.xOffset = 0;
		this.yOffset = 0;
		this.cellX = -1;
		this.cellY = -1;
		this.layer = null;
	}
	
	
		// Copies the attributes of one Placeable to another
	public static void copyAttributes(Placeable src, Placeable dest) {
		dest.asset = src.asset;
		dest.xOffset = src.xOffset;
		dest.yOffset = src.yOffset;
		dest.cellX = src.cellX;
		dest.cellY = src.cellY;
		dest.layer = src.layer;
	}
	

		// Draws the placeable into a given Graphics2D-context
		// TO BE OVERRIDDEN
	@Override
	public abstract void draw(RenderContext rctxt);
	
	
		// Creates a duplicate of this Placeable
		// CAN BE OVERRIDDEN
	public Placeable duplicate() {
		return null;
	}
	
		// Moves the Placeable to a given layer
	public void changeLayer(Layer layer) {
		
			// A simple change can be used if the Placeable is not yet on any layer
		if( this.layer == null )
		this.layer = layer;
	}
	
	
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
	public Layer getLayer() {
		return this.layer;
	}
	
		// Returns a reference to the Asset this Placeable is based on
	public Asset getAsset() {
		return this.asset;
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
	
		// Sets the Asset that this Placeable is based on
	public void setAsset(Asset asset) {
		if( asset == null )
		return;
		
		this.asset = asset;
	}
}
