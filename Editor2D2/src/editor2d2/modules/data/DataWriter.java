package editor2d2.modules.data;

import java.util.ArrayList;

import editor2d2.modules.data.asset.Data;
import editor2d2.modules.data.layer.DataLayer;
import editor2d2.modules.data.placeable.DataCell;

public class DataWriter {

	public ArrayList<String> write(Data target) {
		ArrayList<String> lines = new ArrayList<String>();
		
		lines.add("data 'collision data' DATA00001 '0' 112100100");
		
		return lines;
	}
	
	public ArrayList<String> write(DataCell target) {
		ArrayList<String> lines = new ArrayList<String>();
		
		lines.add("DATA00001 5 5");
		
		return lines;
	}
	
	public ArrayList<String> write(DataLayer target) {
		ArrayList<String> lines = new ArrayList<String>();
		
		lines.add("layer data 'data layer' 32 32");
		
		return lines;
	}
}
