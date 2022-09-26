package editor2d2.model.project.assets;

import java.awt.image.BufferedImage;

public class Image extends Asset {

		// BufferedImage of the asset
	private BufferedImage image;
	
		// Filepath to the underlying image file
	private String path;
	
	
	public Image() {
		this.image = null;
		this.path = "";
	}
	
	
		// Returns a refernce to the BufferedImage representing the Image asset
	public BufferedImage getImage() {
		return this.image;
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
