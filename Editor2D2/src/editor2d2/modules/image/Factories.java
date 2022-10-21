package editor2d2.modules.image;

import editor2d2.gui.modal.ModalWindow;
import editor2d2.model.project.scene.Scene;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.modules.AbstractFactories;
import editor2d2.modules.FactoryService;
import editor2d2.modules.image.asset.Image;
import editor2d2.modules.image.layer.TileLayer;
import editor2d2.modules.image.loader.ImageLoader;
import editor2d2.modules.image.modal.ImageModal;
import editor2d2.modules.image.placeable.Tile;
import editor2d2.modules.image.proppane.TilePropertiesPane;
import editor2d2.modules.image.writer.ImageWriter;

public class Factories extends AbstractFactories<Image> {

	public static final String assetClass = "image";
	
	
	public static void declare() {
		FactoryService.declareAsset(0, assetClass, "tile", new Factories());
	}
	
	
	@Override
	public Image createAsset() {
		return new Image();
	}

	@Override
	public TileLayer createLayer(Scene scene, int cw, int ch) {
		return new TileLayer(scene, cw);
	}
	
	public TileLayer createLayer(Scene scene, int gridSize) {
		return createLayer(scene, gridSize, 0);
	}

	@Override
	public Tile createPlaceable() {
		return new Tile();
	}

	@Override
	public ImageModal createModal(ModalWindow modal, boolean isFactorySettings) {
		return new ImageModal(modal, isFactorySettings);
	}

	@Override
	public TilePropertiesPane createPropertiesPane(Placeable source) {
		return new TilePropertiesPane(source);
	}

	@Override
	public ImageLoader createLoader() {
		return new ImageLoader();
	}
	
	@Override
	public ImageWriter createWriter() {
		return new ImageWriter();
	}
}
