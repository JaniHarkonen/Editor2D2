package editor2d2.model.project.assets;

import java.awt.image.BufferedImage;

public class Image extends Asset {

		// BufferedImage of the asset
	private BufferedImage image;
	
	
	public Image() {
		this.image = null;
	}
	
	
		// Returns a refernce to the BufferedImage representing the Image asset
	public BufferedImage getImage() {
		return this.image;
	}
	
		// Sets the BufferedImage representing the Image asset
	public void setImage(BufferedImage image) {
		this.image = image;
	}
}
