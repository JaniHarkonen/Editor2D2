package editor2d2.modules.object.asset;

public class ObjectProperty {

		// Name of the property
	public String name;
	
		// Value of the property
	public String value;
	
		// Whether the property is compiled
	public boolean isCompiled;
	
	
	public ObjectProperty(String name, String value, boolean isCompiled) {
		this.name = name;
		this.value = value;
		this.isCompiled = isCompiled;
	}
	
		// Copy constructor
	public ObjectProperty(ObjectProperty op) {
		this.name = op.name;
		this.value = op.value;
		this.isCompiled = op.isCompiled;
	}
	
	
	public void set(ObjectProperty op) {
		this.name = op.name;
		this.value = op.value;
		this.isCompiled = op.isCompiled;
	}
}
