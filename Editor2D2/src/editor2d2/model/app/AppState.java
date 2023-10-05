package editor2d2.model.app;

import editor2d2.model.app.tool.Tool;
import editor2d2.model.project.Folder;
import editor2d2.model.project.Project;
import editor2d2.model.project.scene.Layer;

/**
 * AppState functions as the state of the Controller of 
 * the application. It holds references to the currently 
 * active Project, Layer and Scene index as well as the 
 * currently selected Tool, ActionHistory, 
 * HotkeyListener and the open Folder. This state should 
 * not be exposed to the application components, rather, 
 * the application Controller should interface with this 
 * state whenever a request requires it.
 * <br/><br/>
 * 
 * <b>Notice: </b>a part of the application state is 
 * stored directly in the application Controller as they 
 * are considered to be more acutely coupled to the 
 * functionalities of the Controller.
 * <br/><br/>
 * 
 * See Controller for more information on controlling 
 * the application state and model.
 * 
 * @author User
 *
 */
public class AppState {

	/**
	 * Reference to the Project that is currently active 
	 * in the editor.
	 */
	public Project activeProject;
	
	/**
	 * Reference to the Layer that is currently active 
	 * in the editor.
	 */
	public Layer activeLayer;
	
	/**
	 * Reference to the Tool that is currently selected 
	 * in the editor.
	 */
	public Tool selectedTool;
	
	/**
	 * Index of the Scene that is currently active in the 
	 * editor. The index indicates the position of the 
	 * Scene in the Project's ArrayList containing all 
	 * the Scenes of the project.
	 * <br/><br/>
	 * 
	 * See Project for more information on the listing of 
	 * Scenes inside the Project.
	 */
	public int activeScene;
	
	/**
	 * Reference to the ActionHistory that tracks the 
	 * actions that have been taken (and logged). 
	 * ActionHistory can be used by the application 
	 * Controller to undo and redo Actions.
	 * <br/><br/>
	 * 
	 * See ActionHistory for more information on 
	 * logging, undoing and re-doing Actions.
	 */
	public ActionHistory actionHistory;
	
	/**
	 * Reference to the Folder that is currently open 
	 * in the AssetPane.
	 * <br/><br/>
	 * 
	 * See AssetPane for more information on the GUI-
	 * component that renders the pane of Assets in 
	 * the editor.
	 */
	public Folder openFolder;
	
	/**
	 * Reference to the HotkeyListener that is used to 
	 * listen for hotkey presses in the application.
	 * <br/><br/>
	 * 
	 * See HotkeyListener for more information on 
	 * hotkeys.
	 */
	public HotkeyListener hotkeyListener;
	
	/**
	 * Constructs an AppState instance with default 
	 * settings and instantiates its ActionHistory and 
	 * HotkeyListeners.
	 */
	public AppState() {
		this.selectedTool = Tools.getAvailableTools()[0];			// DEBUG LINE, REMOVE
		this.actionHistory = new ActionHistory();
		this.hotkeyListener = new HotkeyListener();
		this.activeScene = -1;
	}
}
