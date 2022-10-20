package editor2d2.modules.image.loader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import editor2d2.model.project.loader.AbstractLoader;
import editor2d2.model.project.scene.Layer;
import editor2d2.modules.image.asset.Image;
import editor2d2.modules.image.placeable.Tile;
import johnnyutils.johnparser.parser.ParsedCommand;

public class ImageLoader extends AbstractLoader<Image> {

	@Override
	public Image loadAsset(ParsedCommand pc) {
		String name = pc.getString(0);
		String identifier = pc.getReference(1);
		String path = pc.getString(2);
		BufferedImage img = null;
		
		try
		{
			img = ImageIO.read(new File(path));
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
		
		if( img == null )
		return null;
		
		Image image = new Image();
		image.setName(name);
		image.setIdentifier(identifier);
		image.setFilePath(path);
		image.setImage(img);
		
		return image;
	}

	@Override
	public Tile loadPlaceable(ParsedCommand pc, Image source, Layer targetLayer) {
		int cx = (int) pc.getNumeral(0);
		int cy = (int) pc.getNumeral(1);
		
		Tile target = source.createPlaceable();
		targetLayer.place(cx, cy, target);
		
		return target;
	}

	
}
