package editor2d2.gui.body.scene;

import javax.swing.JPanel;

import editor2d2.gui.GUIComponent;
import editor2d2.gui.GUIUtilities;
import editor2d2.model.project.scene.Scene;

public class ScenePane extends GUIComponent {
	
		// Reference to the scene represented by this pane
	private final Scene scene;
	
	
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
