package editor2d2.modules.image;

import editor2d2.modules.GUIFactory;
import editor2d2.modules.image.modal.ImageModal;
import editor2d2.modules.image.proppane.TilePropertiesPane;

public final class Declaration {

	public static void declare() {
		String assetClass = "image";
		GUIFactory.declareAsset(0, assetClass, "tile");
		
		GUIFactory.declareModalFactory(assetClass, (host) -> {
			return new ImageModal(host, true);
		});
		
		GUIFactory.declarePropertiesPaneFactory(assetClass, (source) -> {
			return new TilePropertiesPane(source);
		});
	}
	
	private Declaration() { }
}
