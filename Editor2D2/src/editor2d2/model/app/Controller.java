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

public class Controller implements Vendor {

		// Reference to the AppState that the Controller manipulates
	private AppState appState;
	
		// Reference to the SelectionManager that handles Placeable
		// selections
	public final PlaceableSelectionManager placeableSelectionManager;
	
		// Reference to the SubscriptionService that the Controller uses
		// to notify the GUI when the AppState changes
	public final SubscriptionService subscriptionService;
	
		// Whether the Controller has been instantiated
	private static boolean isInstantiated = false;
	
	
		// This class is a singleton, only instantiate once
	private Controller(AppState appState) {
		this.appState = appState;
		this.subscriptionService = new SubscriptionService();
		this.placeableSelectionManager = new PlaceableSelectionManager();
		DebugUtils.controllerDebugSetup(this);
	}
	
		// Instantiates the Controller
	public static Controller instantiate(AppState appState) {
		if( isInstantiated == true )
		return null;
		
		return new Controller(appState);
	}
	
	
	/********************** REQUESTS ***************************/
	
		// Opens a new project and sets it as the active one
	public void openProject(Project project) {
		this.appState.activeProject = project;
		
		openFolder(project.getRootFolder());
		
		this.subscriptionService.register(Handles.ACTIVE_PROJECT, this);
	}
	
		// Opens a given Folder for display in the AssetPane
	public void openFolder(Folder folder) {
		this.appState.openFolder = folder;
		
		if( folder == null )
		this.appState.openFolder = getActiveProject().getRootFolder();
		
		this.subscriptionService.register(Handles.OPEN_FOLDER, this);
	}
	
		// Creates a new Scene of a given anme and adds it to the
		// currently active Project
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
	
		// Adds a given Asset to the currently active Project
	public void addNewAsset(Asset newAsset) {
		getActiveProject().addAsset(newAsset, getOpenFolder());
		Application.window.getModalWindow().getAssetPane().update();
	}
	
		// Removes a given Asset from the currently active Project
	public void removeAsset(Asset asset) {
		getActiveProject().removeAsset(asset);
	}
	
		// Sets the Scene with a given index as the active one
	public void openScene(int index) {
		ArrayList<Scene> scenes = getActiveProject().getAllScenes();
		
		if( index >= scenes.size() )
		return;
		
		this.appState.activeScene = index;
		selectLayer(null);
		subscriptionService.register(Handles.ACTIVE_SCENE, this);
	}
	
		// Resets the active Scene
	public void resetScene() {
		this.appState.activeScene = -1;
		
		subscriptionService.register(Handles.ACTIVE_SCENE, this);
	}
	
		// Renames the active Scene
	public void renameActiveScene(String newName) {
		getActiveScene().setName(newName);
		
		this.subscriptionService.register(Handles.ACTIVE_SCENE, this);
	}
	
		// Deletes the active Scene
	public void deleteActiveScene() {
		int index = getActiveSceneIndex();
		Project activeProject = getActiveProject();
		
		activeProject.removeScene(index);
		
		int sceneCount = activeProject.getAllScenes().size() - 1;
		int newIndex = Math.min(sceneCount, index);
		openScene(newIndex);
	}
	
		// Removes the active Layer
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
	
	/******************* SELECTION *********************/
	
		// Selects an Asset that is to be placed
	public void selectAsset(Asset asset) {
		if( asset instanceof Folder )
		return;
		
		if( asset != null )
		this.placeableSelectionManager.setSelection(asset.createPlaceable());
		
		this.subscriptionService.register(Handles.SELECTED_PLACEABLE, this);
	}
	
		// Sets the currently active Layer
	public void selectLayer(Layer layer) {
		this.appState.activeLayer = layer;
	}
	
	/****************** TOOL *********************/
	
		// Selects a tool that will be used to insert Placeables
		// into the active Scene
	public void selectTool(Tool tool) {
		this.appState.selectedTool = tool;
	}
	
		// Uses/stops using the currently selected Tool
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
	
		// Uses the currently selected Tool
	public int useTool(ToolContext tc) {
		return useTool(tc, false);
	}
	
	
	/******************** ACTION **********************/
	
		// Undoes the latest action
	public void undoAction() {
		this.appState.actionHistory.undo();
	}
	
		// Re-does the next action in the ActionHistory
	public void redoAction() {
		this.appState.actionHistory.redo();
	}

	
	/************************* GETTERS ************************/
	
		// Returns a reference to the currently open project
	public Project getActiveProject() {
		return this.appState.activeProject;
	}
	
		// Returns a reference to the currently active Layer
	public Layer getActiveLayer() {
		return this.appState.activeLayer;
	}
	
		// Returns a reference to the currently selected Tool
	public Tool getSelectedTool() {
		return this.appState.selectedTool;
	}
	
		// Returns a reference to the ActionHistory that tracks
		// performed Actions
	public ActionHistory getActionHistory() {
		return this.appState.actionHistory;
	}
	
		// Returns a reference to the currently open Folder
	public Folder getOpenFolder() {
		return this.appState.openFolder;
	}
	
		// Returns a refernece to the hotkey listener
	public HotkeyListener getHotkeyListener() {
		return this.appState.hotkeyListener;
	}
	
		// Returns a reference to the active Scene
	public Scene getActiveScene() {
		int index = this.appState.activeScene;
		
		if( index == -1 )
		return null;
		
		return getActiveProject().getScene(index);
	}
	
		// Returns the index of the active Scene in the
		// Project's Scene list
	public int getActiveSceneIndex() {
		return this.appState.activeScene;
	}
}
