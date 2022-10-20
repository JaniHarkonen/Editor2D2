package editor2d2.modules.data.loader;

import java.awt.Color;

import editor2d2.model.project.loader.AbstractLoader;
import editor2d2.model.project.scene.Layer;
import editor2d2.modules.data.asset.Data;
import editor2d2.modules.data.placeable.DataCell;
import johnnyutils.johnparser.parser.ParsedCommand;

public class DataLoader extends AbstractLoader<Data> {

	@Override
	public Data loadAsset(ParsedCommand pc) {
		String name = pc.getString(0);
		String identifier = pc.getReference(1);
		String value = pc.getString(2);
		int colr = (int) pc.getNumeral(3),
			colg = (int) pc.getNumeral(4),
			colb = (int) pc.getNumeral(5);
		
		Data data = new Data();
		data.setName(name);
		data.setIdentifier(identifier);
		data.setValue(value);
		data.setColor(new Color(colr, colg, colb));
		
		return data;
	}

	@Override
	public DataCell loadPlaceable(ParsedCommand pc, Data source, Layer targetLayer) {
		int cx = (int) pc.getNumeral(0);
		int cy = (int) pc.getNumeral(1);
		
		DataCell target = source.createPlaceable();
		targetLayer.place(cx, cy, target);
		
		return target;
	}
}
