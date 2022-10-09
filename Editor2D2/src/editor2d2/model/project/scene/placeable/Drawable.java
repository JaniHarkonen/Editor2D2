package editor2d2.model.project.scene.placeable;

/**
 * This interface will be implemented by all placeables and other
 * classes that are to be rendered in the <b>SceneView</b> GUI-component
 * by the <b>Camera</b>.
 * @author User
 *
 */
public interface Drawable {

	/**
	 * Draws the <b>Drawable</b> with settings provided by the given
	 * <b>RenderContext</b>. This method is typically called by the
	 * <b>Camera</b> instance of the scene.
	 * 
	 * @param rctxt <b>RenderContext</b> object that will provide the
	 * settings necessary for drawing the <b>Drawable</b> including
	 * the <b>Graphics2D</b> context.
	 */
	public void draw(RenderContext rctxt);
}
