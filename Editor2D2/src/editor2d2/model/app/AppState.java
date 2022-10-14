package editor2d2.model.app;

import editor2d2.model.app.tool.TPlace;
import editor2d2.model.app.tool.Tool;
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
	
		// Reference to the selected Tool
	public Tool selectedTool;
	
		// Reference to the ActionHistory that tracks actions and
		// can be used to undo and redo them
	public ActionHistory actionHistory;
	
	public AppState() {
		this.selectedTool = new TPlace();			// DEBUG LINE, REMOVE
		this.actionHistory = new ActionHistory();
	}
}
