package editor2d2.model.app;

import editor2d2.model.project.Project;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.placeable.Placeable;

public class AppState {

		// Reference to the currently open Project
	public Project activeProject;
	
		// Reference to the currently active Layer
	public Layer activeLayer;
	
		// Reference to the selected Placeable
	public Placeable selectedPlaceable;
}
