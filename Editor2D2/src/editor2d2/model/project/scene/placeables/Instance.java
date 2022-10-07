package editor2d2.model.project.scene.placeables;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import editor2d2.Application;
import editor2d2.model.project.assets.EObject;
import editor2d2.model.project.assets.Image;
import editor2d2.model.project.scene.Camera;

public class Instance extends Placeable {

		// Reference to the Image that represents the instance
	private Image sprite;

		// X-coordinate of the instance in the scene
	private double x;
	
		// Y-coordinate of the instance in the scene
	private double y;
	
		// Rotation of the instance (in degrees)
	private double rotation;
	
		// Width of the instance in pixels
	private double width;
	
		// Height of the instance in pixels
	private double height;
	
	
	public Instance() {
		this.sprite = null;
		this.x = 0;
		this.y = 0;
		this.width = 32;
		this.height = 32;
		this.rotation = 0;
	}
	
	
	@Override
	public void draw(RenderContext rctxt) {
		BufferedImage img;
		
		if( this.sprite == null )
		img = Application.resources.getGraphic("icon-null-object");
		else
		img = this.sprite.getImage();
		
		
		Camera cam = rctxt.camera;
		double cam_z = cam.getZ();
		
		double f_x = cam.getOnScreenX(getX());
		double f_y = cam.getOnScreenY(getY());
		double f_w = this.width * cam_z;
		double f_h = this.height * cam_z;
		
			// Apply a transform to rotate and scale the instance
		AffineTransform at = new AffineTransform();
		at.setToRotation(Math.toRadians(this.rotation), f_x + f_w / 2, f_y + f_h / 2);
		at.translate(f_x, f_y);
		at.scale(f_w / img.getWidth(), f_h / img.getHeight());
		
		rctxt.gg.drawImage(img, at, null);
	}
	
	@Override
	public Placeable duplicate() {
		Instance inst = new Instance();
		copyAttributes(this, inst);
		
		inst.sprite = this.sprite;
		inst.x = this.x;
		inst.y = this.y;
		
		return inst;
	}
	
	/*@Override
	public void place(Layer<? extends Placeable> target, int cx, int cy) {
		if( target == null )
		return;
		
		Grid ogrid = target.getObjectGrid();
		
		int tcw = ogrid.getCellWidth(),
			tch = ogrid.getCellHeight();
		
		int fcx = cx / tcw,
			fcy = cy / tch;
		
		target.place(fcx, fcy, this);
		setCellPosition(fcx, fcy);
		setOffsets(cx - (fcx * tcw), cy - (fcy * tch));
		this.layer = target;
	}
	
	public void place(Layer<? extends Placeable> target, double x, double y) {
		place(target, (int) x, (int) y);
		
		Grid ogrid = target.getObjectGrid();
		
		int tcw = ogrid.getCellWidth(),
			tch = ogrid.getCellHeight();
		
		double 	fcx = x / tcw,
				fcy = y / tch;
		
		setOffsets(x - (fcx * tcw), y - (fcy * tch));
	}*/
	
	
		// GETTERS/SETTERS
	
		// Sets the X- and Y-coordinates of the instance
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
		// Sets the scene Object the instance is based on
	public void setObject(EObject object) {
		this.asset = object;
	}
	
		// Sets the Image that represents the instance
	public void setSprite(Image sprite) {
		this.sprite = sprite;
	}
	
		// Returns the scene Object the instance is based on
	public EObject getObject() {
		return (EObject) this.asset;
	}
	
		// Returns the Image that represents the instance
	public Image getSprite() {
		return this.sprite;
	}
}