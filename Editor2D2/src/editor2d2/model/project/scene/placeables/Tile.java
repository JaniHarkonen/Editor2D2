package editor2d2.model.project.scene.placeables;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import editor2d2.Application;
import editor2d2.common.grid.Grid;
import editor2d2.model.project.assets.Image;
import editor2d2.model.project.scene.Camera;

public class Tile extends Placeable {

		// Reference to the Image asset the tile is based on
	//private Image image;
	
		// X-coordinate of the area in the Image asset that will be drawn
	private int drawX;
	
		// Y-coordinate of the area in the Image asset that will be drawn
	private int drawY;
	
		// Width of the area in the Image asset that will be drawn
	private int drawWidth;
	
		// Height of the area in the Image asset that will be drawn
	private int drawHeight;

	
	public Tile() {
		super();
		
		this.drawX = 0;
		this.drawY = 0;
		this.drawWidth = 100;
		this.drawHeight = 100;
	}
	
	
	@Override
	public void draw(RenderContext rctxt) {
		BufferedImage img;
		Image src = getImage();
		
		if( src == null )
		img = Application.resources.getGraphic("icon-null-texture");
		else
		img = src.getImage().getSubimage(this.drawX, this.drawY, this.drawWidth, this.drawHeight);

		Camera cam = rctxt.camera;
		Grid objs = getLayer().getObjectGrid();
		double cam_z = cam.getZ();
		
		double f_x = cam.getOnScreenX(getX());
		double f_y = cam.getOnScreenY(getY());
		double f_w = objs.getCellWidth() * cam_z;
		double f_h = objs.getCellHeight() * cam_z;
		
		AffineTransform at = new AffineTransform();
		at.translate(f_x, f_y);
		at.scale(f_w / img.getWidth(), f_h / img.getHeight());
		
		rctxt.gg.drawImage(img, at, null);
	}
	
	@Override
	public Placeable duplicate() {
		Tile dupl = new Tile();
		copyAttributes(this, dupl);
		
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

		// Changes the Image asset of the tile
	public void setImage(Image image) {
		this.asset = image;
	}
	
		// Sets the X- and Y-coordinates as well as the width and height
		// of the area on the Image asset that will be drawn
	public void setDrawArea(int x, int y, int w, int h) {
		Image img = getImage();
		
			// Overflow
		if( x < 0 || y < 0 || x + w > img.getWidth() || y + h > img.getHeight() )
		return;
		
		this.drawX = x;
		this.drawY = y;
		this.drawWidth = w;
		this.drawHeight = h;
	}
}