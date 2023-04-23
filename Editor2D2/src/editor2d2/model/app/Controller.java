package editor2d2.model.app;

import java.util.ArrayList;

import editor2d2.Application;
import editor2d2.DebugUtils;
import editor2d2.model.Handles;
import editor2d2.model.app.tool.Tool;
import editor2d2.model.app.tool.ToolContext;
import editor2d2.model.project.Asset;
import editor2d2.model.project.Folder;
import editor2d2.model.project.Project;
import editor2d2.model.project.scene.Camera;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.Scene;
import editor2d2.subservice.SubscriptionService;
import editor2d2.subservice.Vendor;

/**
 * This class is one of the root components outlined in 
 * Application. When the application starts up the 
 * Application-class instantiates the Controller and 
 * passes it the AppState that is to represent the 
 * state of hte application.
 * <br/><br/>
 * 
 * Controller is designed to function as 
 * an interface between the GUI and the model. Whenever 
 * a GUI-component wishes to affect a change in the 
 * model, it should call the appropriate method through 
 * Application.controller. The Controller stores most 
 * of the relevant state information in an AppState 
 * instance.
 * <br/><br/>
 * 
 * The communication between the GUI and the model 
 * should be one way. GUI-components may reference the 
 * Controller, however, model components should NEVER 
 * reference the Controller. The idea is that the 
 * GUI and the Controller may be changed and even 
 * completely re-written without having it affect the 
 * model - especially the Project - in any way.
 * <br/><br/>
 * 
 * This class is a singleton, so only one instance 
 * can be instantiated via the instantiate-method.
 * 
 * See Application for more information on root 
 * components.
 * <br/><br/>
 * 
 * See AppState for more information on how the 
 * application state is stored.
 * 
 * @author User
 *
 */
public class Controller implements Vendor {

	/**
	 * The state of the application is stored here. 
	 * Typically things that are not considered to 
	 * be acutely related to the Controller should 
	 * be stored in the AppState.
	 * <br/><br/>
	 * 
	 * See AppState for more information on 
	 * application state.
	 */
	private AppState appState;
	
	/**
	 * The SelectionManager that keeps track of the 
	 * currently selected Placeables.
	 */
	public final PlaceableSelectionManager placeableSelectionManager;
	
	/**
	 * The SubscriptionService that the Controller 
	 * uses to communicate changes in the model to 
	 * the GUI-components.
	 * <br/><br/>
	 * 
	 * The handles for this SubscriptionService 
	 * can be found in the model-package inside the 
	 * Handles-class. See Handles for more 
	 * information on handles dealing with model 
	 * changes.
	 */
	public final SubscriptionService subscriptionService;
	
	/**
	 * Whether an instance of Controller has been 
	 * created. This class is a singleton, so only 
	 * a single instance is allowed.
	 */
	private static boolean isInstantiated = false;
	
	
	/**
	 * Constructs a Controller instance that uses 
	 * a given AppState object to store the state 
	 * of the application. Because the class is a 
	 * singleton, this constructor must remain 
	 * private.
	 * 
	 * @param appState Reference to the AppState 
	 * object that represents the state of the 
	 * application.
	 */
	private Controller(AppState appState) {
		this.appState = appState;
		this.subscriptionService = new SubscriptionService();
		this.placeableSelectionManager = new PlaceableSelectionManager();
		DebugUtils.controllerDebugSetup(this);	// DEBUG, DELETE
	}
	
	/**
	 * Creates an instance of the Controller if 
	 * one hasn't been created already. The 
	 * Controller will use a given AppState 
	 * object to store the state of the 
	 * application. This class is a singleton 
	 * so only a single instance is allowed.
	 * 
	 * @param appState Refrence to the AppState 
	 * object that will be used to store the 
	 * state of the application.
	 * 
	 * @return Reference to the created 
	 * Controller instance.
	 */
	public static Controller instantiate(AppState appState) {
		if( isInstantiated == true )
		return null;
		
		return new Controller(appState);
	}
	
	
	// REQUESTS - these methods are to be used by the 
	// GUI-components when requesting changes to the 
	// model
	
	/**
	 * Opens a given Project in the editor and updates 
	 * the GUI.
	 * <br/><br/>
	 * 
	 * <b>Notice: </b>this method does NOT handle 
	 * Project loading, rather, it simply opens an 
	 * already loaded Project. See ProjectLoader 
	 * for more information on loading Projects 
	 * from external files.
	 * 
	 * @param project Reference to the Project that 
	 * is to be opened.
	 */
	public void openProject(Project project) {
		this.appState.activeProject = project;
		
		openFolder(project.getRootFolder());
		
		this.subscriptionService.register(Handles.ACTIVE_PROJECT, this);
	}
	
	/**
	 * Opens a given Asset Folder in the AssetPane and 
	 * updates the GUI.
	 * 
	 * @param folder Reference to the Asset Folder that 
	 * is to be opened in the editor.
	 */
	public void openFolder(Folder folder) {
		this.appState.openFolder = folder;
		
		if( folder == null )
		this.appState.openFolder = getActiveProject().getRootFolder();
		
		this.subscriptionService.register(Handles.OPEN_FOLDER, this);
	}
	
	/**
	 * Creates a new Scene of a given name and default 
	 * settings. The Scene will then be added to the 
	 * active Project and the GUI will be updated.
	 * 
	 * @param name Name of the Scene that is to be 
	 * added to the Project.
	 */
	public void createNewScene(String name) {
		
			// Create a Camera for the Scene
		Camera sceneCamera = new Camera();
		sceneCamera.setPortDimensions(358, 210);
		
			// Create the Scene itself
		Scene newScene = new Scene();
		newScene.setName(name);
		newScene.setDimensions(200, 200);
		newScene.setCamera(sceneCamera);
		
		
		getActiveProject().addScene(newScene);
	}
	
	/**
	 * Adds a given Asset to the currently active 
	 * Project and updates the GUI.
	 * 
	 * @param newAsset Reference to the Asset that is 
	 * to be added to the Project.
	 */
	public void addNewAsset(Asset newAsset) {
		getActiveProject().addAsset(newAsset, getOpenFolder());
		Application.window.getModalWindow().getAssetPane().update();
	}
	
	/**
	 * Removes a given Asset from the active Project
	 * and updates the GUI.This method also accepts 
	 * Folders as they extend the PseudoAsset-class 
	 * which is also a type of Asset.
	 * 
	 * @param asset Reference to the Asset that is to 
	 * be removed from the Project.
	 */
	public void removeAsset(Asset asset) {
		getActiveProject().removeAsset(asset);
	}
	
	/**
	 * Opens a Scene with a given index in the 
	 * Scene list of the active Project and updates 
	 * the GUI.
	 * 
	 * @param index Index of the Scene in the Scene 
	 * list of the active Project.
	 */
	public void openScene(int index) {
		ArrayList<Scene> scenes = getActiveProject().getAllScenes();
		
		if( index >= scenes.size() )
		return;
		
		this.appState.activeScene = index;
		selectLayer(null);
		subscriptionService.register(Handles.ACTIVE_SCENE, this);
	}
	
	/**
	 * Resets the active Scene by closing it and 
	 * updates the GUI.
	 */
	public void resetScene() {
		this.appState.activeScene = -1;
		
		subscriptionService.register(Handles.ACTIVE_SCENE, this);
	}
	
	/**
	 * Renames the active Scene with a given name 
	 * and updates the GUI.
	 * 
	 * @param newName The new name of the Scene.
	 */
	public void renameActiveScene(String newName) {
		getActiveScene().setName(newName);
		
		this.subscriptionService.register(Handles.ACTIVE_SCENE, this);
	}
	
	/**
	 * Deletes the active Scene from the active 
	 * Project and updates the GUI.
	 */
	public void deleteActiveScene() {
		int index = getActiveSceneIndex();
		Project activeProject = getActiveProject();
		
		activeProject.removeScene(index);
		
		int sceneCount = activeProject.getAllScenes().size() - 1;
		int newIndex = Math.min(sceneCount, index);
		openScene(newIndex);
	}
	
	/**
	 * Deletes the active Layer from the active 
	 * Scene and updates GUI.
	 */
	public void removeActiveLayer() {
		Layer activeLayer = getActiveLayer();
		Scene activeScene = getActiveScene();
		
		if( activeScene == null || activeLayer == null )
		return;
		
		activeScene.removeLayer(activeLayer);
		
		selectLayer(null);
		this.placeableSelectionManager.deselect();
		this.subscriptionService.register(Handles.LAYER_DELETED, this);
	}
	
	// SELECTION - these requests pertain to selections
	
	/**
	 * Selects an Asset in the AssetPane. When 
	 * an Asset is selected it can be 
	 * referenced by the GUI-components, for 
	 * example, when placing Placeables derived 
	 * from the selected Asset. This method 
	 * will create a Placeable and selected that 
	 * Placeable using the 
	 * PlaceableSelectionManager. 
	 * <br/><br/>
	 * 
	 * See the placeableSelectionManager-field 
	 * for more information on managing selections.
	 * 
	 * @param asset Reference to the Asset that 
	 * is to be selected.
	 */
	public void selectAsset(Asset asset) {
		if( asset instanceof Folder )
		return;
		
		if( asset != null )
		this.placeableSelectionManager.setSelection(asset.createPlaceable());
		
		this.subscriptionService.register(Handles.SELECTED_PLACEABLE, this);
	}
	
	/**
	 * Selects a given Layer as the active one and 
	 * updates the GUI.
	 * 
	 * @param layer Reference to the Layer that is 
	 * to be selected as the active one.
	 */
	public void selectLayer(Layer layer) {
		this.appState.activeLayer = layer;
	}
	
	// TOOL - These requests pertain to editor tools
	
	/**
	 * Sets a given Tool as the active one and updates 
	 * the GUI. When the editor is interacted with, 
	 * this Tool will be used.
	 * <br/><br/>
	 * 
	 * See useTool-method for more information on 
	 * using Tools.
	 * 
	 * @param tool Reference to the Tool instance that 
	 * is to be used to interact with the editor.
	 */
	public void selectTool(Tool tool) {
		this.appState.selectedTool = tool;
		
		this.subscriptionService.register(Handles.SELECTED_TOOL, this);
	}
	
	/**
	 * Uses/stops using the active Tool inside a 
	 * given ToolContext. This method is called, for 
	 * example, when the user clicks the terrain in 
	 * the editor.
	 * <br/><br/>
	 * 
	 * The ToolContext object will be passed a 
	 * reference to the currently active Layer as 
	 * well as the current Placeable selection.
	 * <br/><br/>
	 * 
	 * See Tool for more information on editor tools.
	 * 
	 * @param tc ToolContext object that contains 
	 * all the relevant information needed by the 
	 * Tool to function properly.
	 * @param stop Either uses the tool (false), or 
	 * stops using it (true).
	 * 
	 * @return The result of using/stopping the 
	 * Tool.
	 */
	public int useTool(ToolContext tc, boolean stop) {
		tc.controller = this;
		
		if( tc.targetLayer == null )
		tc.targetLayer = this.appState.activeLayer;
		
		tc.selection = this.placeableSelectionManager.getSelection();
		
		if( stop )
		return this.appState.selectedTool.stop(tc);
		else
		return this.appState.selectedTool.use(tc);
	}
	
	/**
	 * Uses the active Tool inside a given 
	 * ToolContext. This method is called, for 
	 * example, when the user clicks the terrain in 
	 * the editor.
	 * <br/><br/>
	 * 
	 * The ToolContext object will be passed a 
	 * reference to the currently active Layer as 
	 * well as the current Placeable selection.
	 * <br/><br/>
	 * 
	 * See Tool for more information on editor tools.
	 * 
	 * @param tc ToolContext object that contains 
	 * all the relevant information needed by the 
	 * Tool to function properly.
	 * 
	 * @return The result of using the Tool.
	 */
	public int useTool(ToolContext tc) {
		return useTool(tc, false);
	}
	
	
	// ACTION - these requests pertain to tracked 
	// editor actions
	
	/**
	 * Undoes the latest Action tracked by the 
	 * ActionHistory.
	 * <br/><br/>
	 * 
	 * See the ActionHistory for more information 
	 * on tracking Actions.
	 */
	public void undoAction() {
		this.appState.actionHistory.undo();
	}
	
	/**
	 * Re-does the next Action tracked by 
	 * the ActionHistroy. 
	 * <br/><br/>
	 * 
	 * See the ActionHistory for more information
	 * on tracking Actions.
	 */
	public void redoAction() {
		this.appState.actionHistory.redo();
	}

	
	// GETTERS - these requests are basically 
	// getters for the application state
	
	/**
	 * Returns a refrence to the currently 
	 * active Project.
	 * 
	 * @return Reference to the active 
	 * Project.
	 */
	public Project getActiveProject() {
		return this.appState.activeProject;
	}
	
	/**
	 * Returns a refrence to the currently 
	 * active Layer.
	 * 
	 * @return Reference to the active 
	 * Layer.
	 */
	public Layer getActiveLayer() {
		return this.appState.activeLayer;
	}
	
	/**
	 * Returns a refrence to the currently 
	 * selected Tool.
	 * 
	 * @return Reference to the selected 
	 * Tool.
	 */
	public Tool getSelectedTool() {
		return this.appState.selectedTool;
	}
	
	/**
	 * Returns a refrence to the 
	 * ActionHistory instance that tracks 
	 * editor Actions.
	 * 
	 * @return Reference to the 
	 * ActionHistory tracking editor actions.
	 */
	public ActionHistory getActionHistory() {
		return this.appState.actionHistory;
	}
	
	/**
	 * Returns a refrence to the Folder that 
	 * is currently open in the AssetPane of 
	 * the editor.
	 * 
	 * @return Reference to the Folder open 
	 * in AssetPane.
	 */
	public Folder getOpenFolder() {
		return this.appState.openFolder;
	}
	
	/**
	 * Returns a refrence to the 
	 * HotkeyListener instance that is used 
	 * to track hotkey presses.
	 * 
	 * @return Reference to the 
	 * HotkeyListener tracking hotkey presses.
	 */
	public HotkeyListener getHotkeyListener() {
		return this.appState.hotkeyListener;
	}
	
	/**
	 * Returns a refrence to the currently 
	 * active Scene.
	 * 
	 * @return Reference to the active 
	 * Scene.
	 */
	public Scene getActiveScene() {
		int index = this.appState.activeScene;
		
		if( index == -1 )
		return null;
		
		return getActiveProject().getScene(index);
	}
	
	/**
	 * Returns the index of the currently 
	 * active Scene. The index indicates 
	 * the position of the Scene in the 
	 * Project's Scene list.
	 * 
	 * @return Scene index of the currently 
	 * active Scene.
	 */
	public int getActiveSceneIndex() {
		return this.appState.activeScene;
	}
}
