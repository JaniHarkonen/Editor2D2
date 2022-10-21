package editor2d2.modules.image.writer;

import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.model.project.writer.AbstractWriter;
import editor2d2.modules.image.asset.Image;

public class ImageWriter extends AbstractWriter {

	@Override
	public String writeAsset(Asset a) {
		Image image = (Image) a;
		
		return super.writeAsset(a) + " \"" + image.getFilePath() + "\"";
	}

	@Override
	public String writePlaceable(Placeable p) {
		return p.getAsset().getIdentifier() + " " + p.getCellX() + " " + p.getCellY();
	}

}
