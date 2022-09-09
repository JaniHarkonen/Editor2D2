package editor2d2.model.project.scene.placeables;

import editor2d2.common.grid.Gridable;
import editor2d2.model.project.assets.Image;

public class Tile extends Placeable implements Gridable {

		// Reference to the Image asset the tile is based on
	private Image image;

	
		// Returns a reference to the Image asset the tile is based on
	public Image getImage() {
		return image;
	}

		// Changes the Image asset of the tile
	public void setImage(Image image) {
		this.image = image;
	}
}
