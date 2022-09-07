package editor2d2.model.project.assets;

import java.util.ArrayList;

public class EObject extends Asset {

		// Reference to the Image asset being used as the sprite
		// of the instances of the object
	private Image sprite;
	
		// Properties of the object
	private ArrayList<ObjectProperty> properties;
	
	
	public EObject() {
		this.sprite = null;
		this.properties = new ArrayList<ObjectProperty>();
	}
}
