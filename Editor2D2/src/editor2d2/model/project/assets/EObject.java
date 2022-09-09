package editor2d2.model.project.assets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EObject extends Asset {

		// Reference to the Image asset being used as the sprite
		// of the instances of the object
	private Image sprite;
	
		// Properties of the object mapped with the name of the property fields
	private Map<String, ObjectProperty> properties;
	
	
	public EObject() {
		this.sprite = null;
		this.properties = new HashMap<String, ObjectProperty>();
	}
	
	
		// Returns a reference to the default Image asset used by the scene object
	public Image getSprite() {
		return this.sprite;
	}
	
		// Returns the list of object properties
	public Map<String, ObjectProperty> getProperties() {
		return this.properties;
	}
	
		// Returns the property of a given name
	public ObjectProperty getProperty(String property) {
		return this.properties.get(property);
	}
	
		// Sets the default Image asset used by the scene object
	public void setSprite(Image sprite) {
		this.sprite = sprite;
	}
	
		// Sets the list of object properties
	public void setProperties(Map<String, ObjectProperty> properties) {
		this.properties = properties;
	}
	
		// Modifies a given property, or adds it if it doesn't exist
	public void modifyProperty(String property, ObjectProperty objectProperty) {
		this.properties.put(property, objectProperty);
	}
	
		// Removes a given property
	public void removeProperty(String property) {
		this.properties.put(property, null);
	}
}
