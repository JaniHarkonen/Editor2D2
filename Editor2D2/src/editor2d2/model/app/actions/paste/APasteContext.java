package editor2d2.model.app.actions.paste;

import editor2d2.model.app.Controller;
import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.project.scene.Layer;

public class APasteContext extends ActionContext {

	public Layer targetLayer;
	
	
	public APasteContext(Controller controller, Layer targetLayer) {
		this.controller = controller;
		this.targetLayer = targetLayer;
	}
}
