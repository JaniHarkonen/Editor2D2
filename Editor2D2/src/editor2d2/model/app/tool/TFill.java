package editor2d2.model.app.tool;

import editor2d2.Application;

public class TFill extends Tool {

	public TFill() {
		super();
		this.name = "Fill";
		this.shortcutKey = "F";
		this.icon = Application.resources.getGraphic("icon-tool-fill");
	}
}
