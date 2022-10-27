package editor2d2.modules;

import editor2d2.gui.body.assetpane.AssetItem;
import editor2d2.gui.body.assetpane.AssetPane;
import editor2d2.gui.body.proppane.PropertiesPane;
import editor2d2.gui.modal.ModalView;
import editor2d2.gui.modal.ModalWindow;
import editor2d2.model.project.Asset;
import editor2d2.model.project.loader.AbstractLoader;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.Scene;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.model.project.writer.AbstractWriter;

public abstract class AbstractFactories<A extends Asset> {

	public final String assetClass = "";
	
	
	public abstract Asset createAsset();
	
	public abstract Layer createLayer(Scene scene, int cw, int ch);
	
	public abstract Placeable createPlaceable();
	
	public abstract ModalView<A> createModal(ModalWindow modal, boolean isFactorySettings);
	
	public abstract PropertiesPane createPropertiesPane(Placeable source);
	
	public abstract AbstractLoader<A> createLoader();
	
	public abstract AbstractWriter createWriter();
	
	public abstract AssetItem createAssetItem(AssetPane host, Asset source);
}
