package editor2d2.model.app;

import editor2d2.model.app.tool.TFill;
import editor2d2.model.app.tool.TMove;
import editor2d2.model.app.tool.TPlace;
import editor2d2.model.app.tool.TRotate;
import editor2d2.model.app.tool.TScale;
import editor2d2.model.app.tool.TSelect;
import editor2d2.model.app.tool.Tool;

public final class Tools {

		// Array of Tools available Tools
	private static final Tool[] availableTools = {
			new TSelect(),
			new TMove(),
			new TPlace(),
			new TFill(),
			new TRotate(),
			new TScale(),
	};
	
	
		// Do not instantiate
	private Tools() { }
	
	
		// Returns an array of all the available Tools
	public static Tool[] getAvailableTools() {
		return availableTools;
	}
}
