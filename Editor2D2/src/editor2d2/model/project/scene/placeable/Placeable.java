package editor2d2.model.project.scene.placeable;

import java.awt.Graphics2D;
import java.awt.Point;

import editor2d2.common.Bounds;
import editor2d2.common.GeometryUtilities;
import editor2d2.common.grid.Grid;
import editor2d2.common.grid.Gridable;
import editor2d2.model.project.Asset;
import editor2d2.model.project.HasAsset;
import editor2d2.model.project.scene.Layer;

public abstract class Placeable implements Gridable, Drawable, HasAsset {
	
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
	
		// Whether the Placeable has been selected
	protected boolean isSelected;
	
	
	protected Placeable() {
		this.asset = null;
		this.xOffset = 0;
		this.yOffset = 0;
		this.cellX = -1;
		this.cellY = -1;
		this.layer = null;
		this.isSelected = false;
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
	
	public abstract void drawPlaceable(Graphics2D gg, double dx, double dy, double dw, double dh);
	
	
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
		else
		{
			delete();
			
			Layer l = this.layer;
			double 	x = getX(),
					y = getY();
			
			this.layer = null;	// Reset Layer as attemptPlace modifies it 
			l.attemptPlace(x, y, this);
		}
	}
	
		// Deletes the Placeable from the Layer it's on
		// CAN BE OVERRIDDEN
	public void delete() {
		this.layer.delete(getCellX(), getCellY());
	}
	
		// Draws a rectangle around a given area if the Placeable
		// has been selected
	protected void drawSelection(Graphics2D g, int x, int y, int w, int h, double rotation) {
		if( !getSelected() )
		return;
		
		if( rotation == 0 )
		{
			g.drawRect(x, y, w, h);
			return;
		}
		
		int x1 = x;
		int y1 = y;
		int x2 = x;
		int y2 = y;
		
		for( int i = 0; i < 3; i++ )
		{
			x1 = x2;
			y1 = y2;
			Point.Double p = GeometryUtilities.pointAtDistance(x1, y1, h, rotation + i * 90);
			x2 = (int) p.x;
			y2 = (int) p.y;
			
			g.drawLine(x1, y1, x2, y2);
		}
		
		x1 = x2;
		y1 = y2;
		x2 = x;
		y2 = y;
		
		g.drawLine(x1, y1, x2, y2);
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
	
		// Returns the bounds of the Placeable
		// CAN BE OVERRIDDEN FOR PLACEABLES THAT DON'T RESPECT
		// CELLULAR COORDINATES
	public Bounds getBounds() {
		double 	x = getX(),
				y = getY();
		Grid og = this.layer.getObjectGrid();
		
		return new Bounds(x, y, x + og.getCellWidth(), getY() + og.getCellHeight());
	}
	
		// Returns a reference to the layer the placeable is placed in
	public Layer getLayer() {
		return this.layer;
	}
	
		// Returns a reference to the Asset this Placeable is based on
	public Asset getAsset() {
		return this.asset;
	}
	
		// Returns whether the Placeable has been selected
	public boolean getSelected() {
		return this.isSelected;
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
	
		// Sets the Placeable as selected
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}
