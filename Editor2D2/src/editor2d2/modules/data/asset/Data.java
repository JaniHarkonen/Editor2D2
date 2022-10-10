package editor2d2.modules.data.asset;

import java.awt.Color;

import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.modules.data.placeable.DataCell;

public class Data extends Asset {

		// Data contained by the data cells
	private String value;
	
		// Color of the data cell
	private Color color;
	
	
	public Data() {
		super("data");
		
		this.value = "0";
		this.color = Color.WHITE;
	}
	
	
	@Override
	public Placeable createPlaceable() {
		DataCell dcell = new DataCell();
		dcell.setData(this);
		
		return dcell;
	}
	
	
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
