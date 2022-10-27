package editor2d2.modules.object.placeable;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import editor2d2.Application;
import editor2d2.common.Bounds;
import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.Camera;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.model.project.scene.placeable.RenderContext;
import editor2d2.modules.image.asset.Image;
import editor2d2.modules.object.asset.EObject;
import editor2d2.modules.object.layer.ObjectArray;

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
		
			// DEBUG, remove later
		if( Application.controller.placeableSelectionManager.checkSelected(this) )
		{
			rctxt.gg.setColor(Color.RED);
			rctxt.gg.drawRect((int) f_x, (int) f_y, (int) f_w, (int) f_h);
		}
	}
	
	@Override
	public Placeable duplicate() {
		Instance inst = new Instance();
		copyAttributes(this, inst);
		
		inst.sprite = this.sprite;
		inst.x = this.x;
		inst.y = this.y;
		inst.width = this.width;
		inst.height = this.height;
		inst.rotation = this.rotation;
		
		return inst;
	}
	
	@Override
	public void delete() {
		ObjectArray oa = (ObjectArray) this.layer.getObjectGrid().get(getCellX(), getCellY());
		oa.remove(this);
	}
	
	
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
	
		// Sets the rotation of the Instance (in degrees)
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
	
		// Sets the width and the height of the Instance
	public void setDimensions(double width, double height) {
		this.width = width;
		this.height = height;
	}
	
		// Returns the scene Object the instance is based on
	public EObject getObject() {
		return (EObject) this.asset;
	}
	
	@Override
	public Asset getReferencedAsset() {
		return getObject();
	}
	
	@Override
	public Bounds getBounds() {
		double	x = getX(),
				y = getY();
		
		return new Bounds(x, y, x + this.width, this.height);
	}
	
		// Returns the Image that represents the instance
	public Image getSprite() {
		return this.sprite;
	}
	
		// Returns the width of the Instance
	public double getWidth() {
		return this.width;
	}
	
		// Returns the height of the Instance
	public double getHeight() {
		return this.height;
	}
	
		// Returns the rotation of the Instance (in degrees)
	public double getRotation() {
		return this.rotation;
	}

}
