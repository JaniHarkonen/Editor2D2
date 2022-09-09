package editor2d2.model.project.assets;

public class Data extends Asset {

		// Data contained by the data cells
	private String value;
	
	
		// Returns the data represented by the Data object
	public String getValue() {
		return this.value;
	}
	
		// Sets the data represented by the Data object
	public void setValue(String value) {
		this.value = value;
	}
}
