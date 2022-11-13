package editor2d2.modules.object.asset;

import editor2d2.model.project.Asset;
import editor2d2.modules.image.asset.Image;
import editor2d2.modules.object.placeable.Instance;

public class EObject extends Asset {

		// Reference to the Image asset being used as the sprite
		// of the instances of the object
	private Image sprite;
	
		// Default width of the instances of the object
	private double width;
	
		// Default height of the instances of the object
	private double height;
	
		// Default rotation of the instances of the object (degrees)
	private double rotation;
	
		// A wrapper for the properties of the object
	private PropertyManager propertyManager;
	
	
	public EObject() {
		super("object");
		
		this.sprite = null;
		this.width = 0;
		this.height = 0;
		this.rotation = 0;
		this.propertyManager = new PropertyManager();
	}
	
	
	@Override
	public Instance createPlaceable() {
		return createPlaceable(true);
	}
	
	public Instance createPlaceable(boolean copyProperties) {
		Instance inst = new Instance();
		inst.setObject(this);
		inst.setDimensions(this.width, this.height);
		inst.setRotation(this.rotation);
		
		if( copyProperties )
		this.propertyManager.copyProperties(inst.getPropertyManager());
		
		return inst;
	}
	
	
		// Returns a reference to the default Image asset used by the scene object
	public Image getSprite() {
		return this.sprite;
	}
	
		// Returns the default width of the instances of the object
	public double getWidth() {
		return this.width;
	}
	
		// Returns the default height of the instances of the object
	public double getHeight() {
		return this.height;
	}
	
		// Returns the default rotation (degrees) of the instances of the object
	public double getRotation() {
		return this.rotation;
	}
	
		// Returns a reference to the PropertyManager that wraps the
		// object properties
	public PropertyManager getPropertyManager() {
		return this.propertyManager;
	}
	
	
		// Sets the default Image asset used by the scene object
	public void setSprite(Image sprite) {
		this.sprite = sprite;
	}
	
		// Sets the default width of the instances of the object
	public void setWidth(double width) {
		this.width = width;
	}
	
		// Sets the default height of the instances of the object
	public void setHeight(double height) {
		this.height = height;
	}
	
		// Sets the default rotation of the instances of the object
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
}
