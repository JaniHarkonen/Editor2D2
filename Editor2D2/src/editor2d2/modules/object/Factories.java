package editor2d2.modules.object;

import editor2d2.gui.modal.ModalWindow;
import editor2d2.model.project.scene.Scene;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.modules.AbstractFactories;
import editor2d2.modules.FactoryService;
import editor2d2.modules.object.asset.EObject;
import editor2d2.modules.object.layer.InstanceLayer;
import editor2d2.modules.object.modal.ObjectModal;
import editor2d2.modules.object.placeable.Instance;
import editor2d2.modules.object.proppane.InstancePropertiesPane;

public class Factories extends AbstractFactories<EObject> {

	public static final String assetClass = "object";
	
	
	public static void declare() {
		FactoryService.declareAsset(1, assetClass, "instance", new Factories());
	}
	
	
	@Override
	public EObject createAsset() {
		return new EObject();
	}

	@Override
	public InstanceLayer createLayer(Scene scene, int cw, int ch) {
		return new InstanceLayer(scene);
	}
	
	public InstanceLayer createLayer(Scene scene) {
		return createLayer(scene);
	}

	@Override
	public Instance createPlaceable() {
		return new Instance();
	}

	@Override
	public ObjectModal createModal(ModalWindow modal, boolean isFactorySettings) {
		return new ObjectModal(modal, isFactorySettings);
	}

	@Override
	public InstancePropertiesPane createPropertiesPane(Placeable source) {
		return new InstancePropertiesPane(source);
	}

}
