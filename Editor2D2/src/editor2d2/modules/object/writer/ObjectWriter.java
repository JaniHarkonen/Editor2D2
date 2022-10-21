package editor2d2.modules.object.writer;

import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.model.project.writer.AbstractWriter;
import editor2d2.modules.object.asset.EObject;

public class ObjectWriter extends AbstractWriter {

	@Override
	public String writeAsset(Asset a) {
		EObject object = (EObject) a;
		
		return super.writeAsset(a) + " " + object.getWidth() + " " + object.getHeight() + " " + object.getRotation();
	}

	@Override
	public String writePlaceable(Placeable p) {
		return p.getAsset().getIdentifier() + " " + p.getX() + " " + p.getY();
	}

}
