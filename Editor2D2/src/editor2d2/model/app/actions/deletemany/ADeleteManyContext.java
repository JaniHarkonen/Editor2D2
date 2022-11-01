package editor2d2.model.app.actions.deletemany;

import editor2d2.model.app.Controller;
import editor2d2.model.app.actions.ActionContext;
import editor2d2.model.project.scene.Layer;

public class ADeleteManyContext extends ActionContext {
	
	public Layer target;

	public ADeleteManyContext(Controller controller, Layer targetLayer) {
		this.controller = controller;
		this.selection = controller.placeableSelectionManager.getSelection();
		this.target = targetLayer;
	}
}
