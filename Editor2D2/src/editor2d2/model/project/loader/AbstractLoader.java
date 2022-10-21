package editor2d2.model.project.loader;

import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.placeable.Placeable;
import johnnyutils.johnparser.parser.ParsedCommand;

public abstract class AbstractLoader<A extends Asset> {

	public abstract A loadAsset(ParsedCommand pc);
	
	public abstract Placeable loadPlaceable(ParsedCommand pc, A source, Layer targetLayer);
}
