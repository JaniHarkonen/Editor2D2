package editor2d2.modules.image.writer;

import java.awt.Rectangle;

import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.model.project.writer.AbstractWriter;
import editor2d2.modules.image.asset.Image;
import editor2d2.modules.image.placeable.Tile;

public class ImageWriter extends AbstractWriter {

	@Override
	public String writeAsset(Asset a) {
		Image image = (Image) a;
		
		return super.writeAsset(a) + " \"" + image.getFilePath() + "\"";
	}

	@Override
	public String writePlaceable(Placeable p) {
		Tile t = (Tile) p;
		Rectangle tileBounds = t.getDrawArea();
		
		return (
			t.getAsset().getIdentifier() + " " +
			t.getCellX() + " " +
			t.getCellY() + " " +
			tileBounds.x + " " +
			tileBounds.y + " " +
			tileBounds.width + " " +
			tileBounds.height
		);
	}
}
