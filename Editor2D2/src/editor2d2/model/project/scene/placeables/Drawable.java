package editor2d2.model.project.scene.placeables;

/**
 * This interface will be implemented by all placeables and other
 * classes that wish to be rendered in the SceneView by the Camera.
 * @author User
 *
 */
public interface Drawable {

	public void draw(RenderContext rctxt);
}
