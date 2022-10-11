package editor2d2.modules.object;

import editor2d2.modules.GUIFactory;
import editor2d2.modules.object.modal.ObjectModal;
import editor2d2.modules.object.proppane.InstancePropertiesPane;

public final class Declaration {

	public static void declare() {
		String assetClass = "object";
		GUIFactory.declareAsset(1, assetClass, "instance");
		
		GUIFactory.declareModalFactory(assetClass, (host) -> {
			return new ObjectModal(host, true);
		});
		
		GUIFactory.declarePropertiesPaneFactory(assetClass, (source) -> {
			return new InstancePropertiesPane(source);
		});
	}
	
	private Declaration() { }
}
