package editor2d2.model.project.scene.placeables;

import java.awt.Graphics2D;

import editor2d2.model.project.scene.Camera;

/**
 * Container class whose instance will be passed onto drawables
 * upon rendering. This class will contain information used to
 * properly render the drawable.
 * @author User
 *
 */
public class RenderContext {

	/**
	 * The Graphics2D context the drawable will be rendered in.
	 */
	public final Graphics2D gg;
	
	/**
	 * The Camera instance that renders the drawable.
	 */
	public final Camera camera;
	
	
	/**
	 * Constructs a Render Context that will be passed onto
	 * drawables upon rendering.
	 * @param gg
	 * @param camera
	 */
	public RenderContext(Graphics2D gg, Camera camera) {
		this.gg = gg;
		this.camera = camera;
	}
}
