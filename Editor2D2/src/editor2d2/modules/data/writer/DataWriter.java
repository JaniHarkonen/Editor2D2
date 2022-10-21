package editor2d2.modules.data.writer;

import java.awt.Color;

import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.model.project.writer.AbstractWriter;
import editor2d2.modules.data.asset.Data;

public class DataWriter extends AbstractWriter {

	@Override
	public String writeAsset(Asset a) {
		Data data = (Data) a;
		Color color = data.getColor();
		
		return super.writeAsset(data) + " \"" + data.getValue() + "\" " + color.getRed() + " " + color.getGreen() + " " + color.getBlue();
	}

	@Override
	public String writePlaceable(Placeable p) {
		return p.getAsset().getIdentifier() + " " + p.getCellX() + " " + p.getCellY();
	}

}
