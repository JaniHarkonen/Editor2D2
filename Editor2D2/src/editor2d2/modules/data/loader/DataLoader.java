package editor2d2.modules.data.loader;

import editor2d2.gui.modal.ModalWindow;
import editor2d2.model.project.scene.Scene;
import editor2d2.modules.data.asset.Data;
import editor2d2.modules.data.layer.DataLayer;
import editor2d2.modules.data.modal.DataModal;
import editor2d2.modules.data.placeable.DataCell;
import editor2d2.modules.data.proppane.DataCellPropertiesPane;

public class DataLoader {

	public DataLayer createLayer(Scene scene, int gridSize) {
		return new DataLayer(scene, gridSize);
	}
	
	public Data createAsset() {
		return new Data();
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
