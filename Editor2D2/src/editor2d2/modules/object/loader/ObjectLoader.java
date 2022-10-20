package editor2d2.modules.object.loader;

import editor2d2.model.project.loader.AbstractLoader;
import editor2d2.model.project.scene.Layer;
import editor2d2.modules.object.asset.EObject;
import editor2d2.modules.object.placeable.Instance;
import johnnyutils.johnparser.parser.ParsedCommand;

public class ObjectLoader extends AbstractLoader<EObject> {

	@Override
	public EObject loadAsset(ParsedCommand pc) {
		String name = pc.getString(0);
		String identifier = pc.getReference(1);
		double w = pc.getNumeral(2);
		double h = pc.getNumeral(3);
		double rot = pc.getNumeral(4);
		
		EObject object = new EObject();
		object.setName(name);
		object.setIdentifier(identifier);
		object.setWidth(w);
		object.setHeight(h);
		object.setRotation(rot);
		
		return object;
	}

	@Override
	public Instance loadPlaceable(ParsedCommand pc, EObject source, Layer targetLayer) {
		double x = pc.getNumeral(0);
		double y = pc.getNumeral(1);
		
		Instance target = source.createPlaceable();
		targetLayer.place(x, y, target);
		
		return target;
	}

}
