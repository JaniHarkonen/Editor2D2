package editor2d2.gui.body.scene;

import javax.swing.JPanel;

import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.model.project.scene.Scene;

/**
 * This is a container class for the SceneView. It holds 
 * a reference to the Scene that is to be displayed in 
 * the view.
 * 
 * @author User
 *
 */
public class ScenePane extends GUIComponent {
	
	/**
	 * The Scene rendered in this pane.
	 */
	private final Scene scene;
	
	/**
	 * Constructs a ScenePane instance that will render 
	 * a given Scene.
	 * 
	 * @param scene Reference to the Scene that is to 
	 * be rendered by this ScenePane.
	 */
	public ScenePane(Scene scene) {
		this.scene = scene;
	}
	
	@Override
	protected JPanel draw() {
		JPanel container = GUIUtilities.createDefaultPanel();
		
		if( this.scene == null )
		return container;
		
		container.add((new SceneView()).render());
		
		return container;
	}
}
