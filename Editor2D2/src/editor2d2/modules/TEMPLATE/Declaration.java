package editor2d2.modules.TEMPLATE;

// Remove the multiline comment markers to use
/*
import editor2d2.modules.GUIFactory;
import editor2d2.modules.object.modal.ObjectModal;
import editor2d2.modules.object.proppane.InstancePropertiesPane;

public final class Declaration {

	public static void declare() {
		String assetClass = "<ASSET CLASS NAME HERE>";
		
			// Declares the asset to the GUIFactory
			// GUIFactory.declareAsset(x, assetClass) can be used in cases
			// where the developer wants to influence the ordering of the
			// assets in GUI elements, for example, in the "Asset"-menu in
			// the window toolbar
		GUIFactory.declareAsset(assetClass); //GUIFactory.declareAsset(x, assetClass);
		
			// This factory function will be called by the GUI when the user clicks
			// on a menu item in the "Asset"-menu in the window toolbar. It creates
			// the ModalView-instance representing the Asset that is about to be
			// created.
			//
			// Replace "ASSET" in "new ASSETModal" with the asset's class name. For
			// example, "Fake3DObjectModal".
		GUIFactory.declareModalFactory(assetClass, (host) -> {
			return new ASSETModal(host, true);
		});
		
			// This factory function will be called by the GUI either when the user
			// clicks on an asset in the asset pane, or when the user clicks on a
			// placeable inside the scene view. The function creates a PropertiesPane-
			// instance representing the source Placeable that is being inspected.
			//
			// Replace "PLACEABLE" in "new PLACEABLEPropertiesPane" with the placeable's
			// class name. For example, "Fake3DModelPropertiesPane".
		GUIFactory.declarePropertiesPaneFactory(assetClass, (source) -> {
			return new PLACEABLEPropertiesPane(source);
		});
	}
	
		// Do not instantiate these classes
	private Declaration() { }
}
*/