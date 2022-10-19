package editor2d2.modules.data;

import editor2d2.gui.modal.ModalWindow;
import editor2d2.model.project.scene.Scene;
import editor2d2.modules.FactoryService;
import editor2d2.modules.data.asset.Data;
import editor2d2.modules.data.layer.DataLayer;
import editor2d2.modules.data.modal.DataModal;
import editor2d2.modules.data.placeable.DataCell;
import editor2d2.modules.data.proppane.DataCellPropertiesPane;

public class Factories {
	
	public static final String assetClass = "data";
	
	
	public static void declare() {
		FactoryService.declareAsset(2, assetClass, "data cell", new Factories());
	}
	
	
		// Do not instantiate
	private Factories() { }
	
	
	public Data createAsset() {
		return new Data();
	}
	
	public DataLayer createLayer(Scene scene, int gridSize) {
		return new DataLayer(scene, gridSize);
	}
	
	public DataCell createPlaceable() {
		return new DataCell();
	}
	
	public DataModal createModal(ModalWindow modal, boolean isFactorySettings) {
		return new DataModal(modal, isFactorySettings);
	}
	
	public DataCellPropertiesPane createPropertiesPane(DataCell source) {
		return new DataCellPropertiesPane(source);
	}
}
