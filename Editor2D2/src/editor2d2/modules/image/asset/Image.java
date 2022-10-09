package editor2d2.modules.image.asset;

import java.awt.image.BufferedImage;

import editor2d2.model.project.Asset;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.modules.image.placeable.Tile;

public class Image extends Asset {

		// BufferedImage of the asset
	private BufferedImage image;
	
		// Filepath to the underlying image file
	private String path;
	
	
	public Image() {
		this.image = null;
		this.path = "";
	}
	
	
	@Override
	public Placeable createPlaceable() {
		Tile tile = new Tile();
		tile.setImage(this);
		
		return tile;
	}
	
		// Returns a refernce to the BufferedImage representing the Image asset
	public BufferedImage getImage() {
		return this.image;
	}
	
		// Returns the width of the underlying BufferedImage
	public int getWidth() {
		return this.image.getWidth();
	}
	
		// Returns the width of the underlying BufferedImage divided by n
	public int getWidth(int n) {
		return this.image.getWidth() / n;
	}
	
		// Returns the height of the underlying BufferedImage
	public int getHeight() {
		return this.image.getHeight();
	}
	
		// Returns the height of the underlying BufferedImage divided by n
	public int getHeight(int n) {
		return this.image.getHeight() / n;
	}
	
		// Returns the filepath of the underlying image file
	public String getFilePath() {
		return this.path;
	}
	
		// Sets the BufferedImage representing the Image asset
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
		// Sets the filepath where the underlying image file is located
	public void setFilePath(String path) {
		if( path == null )
		return;
		
		this.path = path;
	}
}
