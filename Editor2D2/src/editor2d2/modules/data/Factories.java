package editor2d2.modules.data;

import editor2d2.gui.modal.ModalWindow;
import editor2d2.model.project.scene.Scene;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.modules.AbstractFactories;
import editor2d2.modules.FactoryService;
import editor2d2.modules.data.asset.Data;
import editor2d2.modules.data.layer.DataLayer;
import editor2d2.modules.data.loader.DataLoader;
import editor2d2.modules.data.modal.DataModal;
import editor2d2.modules.data.placeable.DataCell;
import editor2d2.modules.data.proppane.DataCellPropertiesPane;

public class Factories extends AbstractFactories<Data> {
	
	public static final String assetClass = "data";
	
	
	public static void declare() {
		FactoryService.declareAsset(2, assetClass, "data cell", new Factories());
	}
	
	
		// Do not instantiate
	private Factories() { }
	
	@Override
	public Data createAsset() {
		return new Data();
	}
	
	@Override
	public DataLayer createLayer(Scene scene, int cw, int ch) {
		return new DataLayer(scene, cw);
	}
	
	public DataLayer createLayer(Scene scene, int gridSize) {
		return createLayer(scene, gridSize, 0);
	}
	
	@Override
	public DataCell createPlaceable() {
		return new DataCell();
	}
	
	@Override
	public DataModal createModal(ModalWindow modal, boolean isFactorySettings) {
		return new DataModal(modal, isFactorySettings);
	}
	
	@Override
	public DataCellPropertiesPane createPropertiesPane(Placeable source) {
		return new DataCellPropertiesPane(source);
	}
	
	@Override
	public DataLoader createLoader() {
		return new DataLoader();
	}
}
