package editor2d2.model.project;

import java.util.ArrayList;

public class WriteContext {

	private ArrayList<String> lines;
	
	private Writeable source;
	
	private String target;
	
	
	public WriteContext() {
		this.lines = new ArrayList<String>();
		this.source = null;
		this.target = "";
	}
	
	
	public void openWrite(Writeable source) {
		if( this.source == null )
		return;
		
		this.source = source;
	}
	
	public void closeWrite() {
		this.source = null;
		this.target = "";
	}
	
	public void writeProperty(Writeable source, String key, String value) {
		
	}
	
	
}
