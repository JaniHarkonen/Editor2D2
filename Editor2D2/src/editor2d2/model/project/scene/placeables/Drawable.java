package editor2d2.model.project.scene.placeables;

/**
 * This interface will be implemented by all placeables and other
 * classes that wish to be rendered in the SceneView by the Camera.
 * @author User
 *
 */
public interface Drawable {

	/**
	 * Draws the placeable with settings provided by the given
	 * RenderContext-object.
	 * 
	 * @param rctxt RenderContext-object that will provide the
	 * settings necessary for drawing the placeable including
	 * the Graphics2D-context.
	 */
	public void draw(RenderContext rctxt);
}
