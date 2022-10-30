package editor2d2.model.project.writer;

import editor2d2.common.grid.Grid;
import editor2d2.common.grid.Gridable;
import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.placeable.Placeable;

public abstract class AbstractWriter {

	public String writeAsset(Asset a) {
		return a.getAssetClassName() + " \"" + a.getName() + "\" " + a.getIdentifier();
	}
	
	public String writeLayer(Layer l) {
		Grid ogrid = l.getObjectGrid();
		return "data \"" + l.getName() + "\" " + ogrid.getCellWidth() + " " + ogrid.getCellHeight() + " " + l.getOpacity();
	}
	
	public abstract String writePlaceable(Placeable p);
	
	public String writePlaceable(Gridable g) {
		return writePlaceable((Placeable) g);
	}
}
