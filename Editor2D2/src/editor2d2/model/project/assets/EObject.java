package editor2d2.model.project.assets;

import java.util.ArrayList;

import editor2d2.model.project.scene.placeables.Instance;
import editor2d2.model.project.scene.placeables.Placeable;

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
	
		// Properties of the object mapped with the name of the property fields
	//private Map<String, ObjectProperty> properties;
	private ArrayList<ObjectProperty> properties;
	
	
	public EObject() {
		this.sprite = null;
		this.width = 0;
		this.height = 0;
		this.rotation = 0;
		//this.properties = new HashMap<String, ObjectProperty>();
		this.properties = new ArrayList<ObjectProperty>();
	}
	
	
	@Override
	public Placeable createPlaceable() {
		Instance inst = new Instance();
		inst.setObject(this);
		inst.setSprite(this.sprite);
		
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
	
		// Returns the list of object properties
	/*public Map<String, ObjectProperty> getProperties() {
		return this.properties;
	}*/
	public ArrayList<ObjectProperty> getProperties() {
		return this.properties;
	}
	
		// Returns the property of a given name
	/*public ObjectProperty getProperty(String property) {
		return this.properties.get(property);
	}*/
	public ObjectProperty getProperty(String property) {
		for( ObjectProperty op : this.properties )
		{
			if( op.name.equals(property) )
			return op;
		}
		
		return null;
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
	
		// Sets the list of object properties
	/*public void setProperties(Map<String, ObjectProperty> properties) {
		this.properties = properties;
	}*/
	public void setProperties(ArrayList<ObjectProperty> properties) {
		this.properties = properties;
	}
	
		// Modifies a given property, or adds it if it doesn't exist
	/*public void modifyProperty(String property, ObjectProperty objectProperty) {
		this.properties.put(property, objectProperty);
	}*/
	public void modifyProperty(String property, ObjectProperty newProperty) {
		for( int i = 0; i < this.properties.size(); i++ )
		{
			if( !this.properties.get(i).name.equals(property) )
			continue;
			
			this.properties.set(i, newProperty);
			break;
		}
		
		this.properties.add(newProperty);
	}
	
		// Adds a given property, or modifies it if it exists already
	public void addProperty(ObjectProperty newProperty) {
		modifyProperty(newProperty.name, newProperty);
	}
	
		// Removes a given property
	/*public void removeProperty(String property) {
		this.properties.put(property, null);
	}*/
	public void removeProperty(String property) {
		for( int i = 0; i < this.properties.size(); i++ )
		{
			if( !this.properties.get(i).name.equals(property) )
			continue;
			
			this.properties.remove(i);
			break;
		}
	}
}