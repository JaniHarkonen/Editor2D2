package editor2d2.modules.object.writer;

import editor2d2.common.grid.Gridable;
import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.model.project.writer.AbstractWriter;
import editor2d2.modules.object.asset.EObject;
import editor2d2.modules.object.layer.ObjectArray;
import editor2d2.modules.object.placeable.Instance;

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

	@Override
	public String writePlaceable(Gridable g) {
		if( g instanceof Placeable )
		return writePlaceable((Placeable) g);
		
		ObjectArray oa = (ObjectArray) g;
		String strAll = "";
		
		for( Instance inst : oa.objects )
		strAll += writePlaceable(inst) + "\n";
		
		return strAll;
	}
}
