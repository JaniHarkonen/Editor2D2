package editor2d2.modules.data;

import editor2d2.modules.GUIFactory;
import editor2d2.modules.data.modal.DataModal;
import editor2d2.modules.data.proppane.DataCellPropertiesPane;

public final class Declaration {
	
	public static void declare() {
		String assetClass = "data";
		GUIFactory.declareAsset(2, assetClass, "data cell");
		
		GUIFactory.declareModalFactory(assetClass, (host) -> {
			return new DataModal(host, true);
		});
		
		GUIFactory.declarePropertiesPaneFactory(assetClass, (source) -> {
			return new DataCellPropertiesPane(source);
		});
	}
	
	private Declaration() { }
}
