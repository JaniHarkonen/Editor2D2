package editor2d2.model.project.scene.placeables;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import editor2d2.Application;
import editor2d2.common.grid.Grid;
import editor2d2.model.project.assets.Image;
import editor2d2.model.project.scene.Camera;

public class Tile extends Placeable {

		// Reference to the Image asset the tile is based on
	private Image image;

	
	public Tile() {
		super();
		
		this.image = null;
	}
	
	
	@Override
	public void draw(RenderContext rctxt) {
		//if( sprite == null ) return;
		
		BufferedImage img = Application.resources.getGraphic("test-grass");

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
		//sprite.draw(ctxt.graphics, at);
	}
	
	
		// Returns a reference to the Image asset the tile is based on
	public Image getImage() {
		return image;
	}

		// Changes the Image asset of the tile
	public void setImage(Image image) {
		this.image = image;
	}
}
