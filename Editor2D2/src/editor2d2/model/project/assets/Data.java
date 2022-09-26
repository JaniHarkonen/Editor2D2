package editor2d2.model.project.assets;

import java.awt.Color;

public class Data extends Asset {

		// Data contained by the data cells
	private String value;
	
		// Color of the data cell
	private Color color;
	
	
		// Returns the data represented by the Data object
	public String getValue() {
		return this.value;
	}
	
		// Returns a reference to the color of the Data object cells
	public Color getColor() {
		return this.color;
	}
	
		// Sets the data represented by the Data object
	public void setValue(String value) {
		this.value = value;
	}
	
		// Sets the color of the Data object cells
	public void setColor(Color color) {
		this.color = color;
	}
}
