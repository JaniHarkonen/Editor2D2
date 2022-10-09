package editor2d2.modules.object;

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
}
