package editor2d2.model.project.scene.placeable;

import java.awt.Graphics2D;

import editor2d2.model.project.scene.camera.Camera;


/**
 * A container class used to pass information to <b>Drawable</b> instances
 * upon rendering. All <b>Drawable</b> classes implement a draw-method that 
 * is called by the <b>Camera</b> instance. The <b>draw</b>-method passes a 
 * <b>RenderContext</b> to the <b>Drawable</b> whose information the <b>Drawable</b>
 * may then use to properly render itself.<br /><br />
 * 
 * The information provided by the <b>RenderContext</b> includes: <br />
 * - reference to the <b>Graphics2D</b> context that is used to draw the
 * <b>Drawable</b> (essential for rendering) <br />
 * - reference to the <b>Camera</b> that is rendering the <b>Drawable</b>
 * 
 * @author User
 *
 */
public class RenderContext {

	/**
	 * The <b>Graphics2D</b> context the <b>Drawable</b> will be rendered in.
	 */
	public final Graphics2D gg;
	
	/**
	 * The <b>Camera</b> instance that renders the <b>Drawable</b>.
	 */
	public final Camera camera;
	
	
	/**
	 * Constructs a <b>RenderContext</b> that will be passed onto
	 * <b>Drawables</b> upon rendering.
	 * @param gg The <b>Graphics2D</b> context that the <b>Drawable</b> will be
	 * rendered in.
	 * @param camera The <b>Camera</b> instance that renders the <b>Drawable</b>.
	 */
	public RenderContext(Graphics2D gg, Camera camera) {
		this.gg = gg;
		this.camera = camera;
	}
}
