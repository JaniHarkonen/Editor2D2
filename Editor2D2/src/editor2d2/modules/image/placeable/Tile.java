package editor2d2.modules.image.placeable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import editor2d2.Application;
import editor2d2.common.grid.Grid;
import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.Camera;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.model.project.scene.placeable.RenderContext;
import editor2d2.modules.image.asset.Image;

public class Tile extends Placeable {
	
		// X-coordinate of the area in the Image asset that will be drawn
	private int drawX;
	
		// Y-coordinate of the area in the Image asset that will be drawn
	private int drawY;
	
		// Width of the area in the Image asset that will be drawn
	private int drawWidth;
	
		// Height of the area in the Image asset that will be drawn
	private int drawHeight;
	
		// Image tile
	private BufferedImage tileImage;

	
	public Tile() {
		super();
		
		this.drawX = 0;
		this.drawY = 0;
		this.drawWidth = 100;
		this.drawHeight = 100;
		this.tileImage = null;
	}
	
	
	@Override
	public void draw(RenderContext rctxt) {
		Camera cam = rctxt.camera;
		Grid objs = getLayer().getObjectGrid();
		double cam_z = cam.getZ();
		
		double f_x = cam.getOnScreenX(getX());
		double f_y = cam.getOnScreenY(getY());
		double f_w = objs.getCellWidth() * cam_z;
		double f_h = objs.getCellHeight() * cam_z;
		
		drawPlaceable(rctxt.gg, f_x, f_y, f_w, f_h);
		
			// Highlight if selected
		rctxt.gg.setColor(Color.RED);
		drawSelection(rctxt.gg, (int) f_x, (int) f_y, (int) f_w - 1, (int) f_h - 1, 0d);
	}
	
	@Override
	public void drawPlaceable(Graphics2D gg, double dx, double dy, double dw, double dh) {
		BufferedImage img;
		Image src = getImage();
		
		if( src == null || tileImage == null )
		img = Application.resources.getGraphic("icon-null-texture");
		else
		img = tileImage;
		
		AffineTransform at = new AffineTransform();
		at.translate(dx, dy);
		at.scale(dw / img.getWidth(), dh / img.getHeight());
		
		gg.drawImage(img, at, null);
	}
	
	@Override
	public Placeable duplicate() {
		Tile dupl = new Tile();
		copyAttributes(this, dupl);
		
		dupl.tileImage = this.tileImage;
		dupl.drawX = this.drawX;
		dupl.drawY = this.drawY;
		dupl.drawWidth = this.drawWidth;
		dupl.drawHeight = this.drawHeight;
		
		return dupl;
	}
	
	
		// Returns a reference to the Image asset the tile is based on
	public Image getImage() {
		return (Image) this.asset;
	}
	
	@Override
	public Asset getReferencedAsset() {
		return getImage();
	}

		// Changes the Image asset of the tile
	public void setImage(Image image) {
		this.asset = image;
		
		setTileImageFromAsset();
	}
	
		// Sets the X- and Y-coordinates as well as the width and height
		// of the area on the Image asset that will be drawn
	public void setDrawArea(int x, int y, int w, int h) {
		Image src = getImage();
		
		if( src == null )
		return;
		
			// Overflow
		if( x < 0 || y < 0 || x + w > src.getWidth() || y + h > src.getHeight() )
		return;
		
		this.drawX = x;
		this.drawY = y;
		this.drawWidth = w;
		this.drawHeight = h;
		
		setTileImageFromAsset();
	}
	
		// Sets the BufferedImage of the source Image Asset as the tile
		// image or creates a subimage of a portion of the source if the
		// the tile image bounds are not the same as the source image's
	private void setTileImageFromAsset() {
		Image src = getImage();
		
		if( src == null )
		return;
		
		BufferedImage img = src.getImage();
		
		if( img == null )
		return;
		
		if(
			this.drawX == 0 &&
			this.drawY == 0 &&
			this.drawWidth == img.getWidth() &&
			this.drawHeight == img.getHeight()
		)
		this.tileImage = img;
		else
		this.tileImage = img.getSubimage(this.drawX, this.drawY, this.drawWidth, this.drawHeight);
	}
	
	
		// Returns a Rectangle representing the dimensions of the area
		// on the Image asset that will be drawn
	public Rectangle getDrawArea() {
		return new Rectangle(this.drawX, this.drawY, this.drawWidth, this.drawHeight);
	}
}
