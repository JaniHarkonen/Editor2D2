package editor2d2.model.app;

import editor2d2.Application;
import editor2d2.DebugUtils;
import editor2d2.model.Handles;
import editor2d2.model.app.tool.Tool;
import editor2d2.model.app.tool.ToolContext;
import editor2d2.model.project.Asset;
import editor2d2.model.project.Project;
import editor2d2.model.project.scene.Camera;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.Scene;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.subservice.SubscriptionService;
import editor2d2.subservice.Vendor;

public class Controller implements Vendor {

		// Reference to the AppState that the Controller manipulates
	private AppState appState;
	
		// Reference to the SelectionManager that handles Placeable
		// selections
	public final SelectionManager<Placeable> placeableSelectionManager;
	
		// Reference to the SubscriptionService that the Controller uses
		// to notify the GUI when the AppState changes
	public final SubscriptionService subscriptionService;
	
		// Whether the Controller has been instantiated
	private static boolean isInstantiated = false;
	
	
		// This class is a singleton, only instantiate once
	private Controller(AppState appState) {
		this.appState = appState;
		this.subscriptionService = new SubscriptionService();
		this.placeableSelectionManager = new SelectionManager<Placeable>();
		
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
		this.subscriptionService.register(Handles.ACTIVE_PROJECT, this);
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
		getActiveProject().addAsset(newAsset);
		Application.window.getModalWindow().getAssetPane().update();
	}
	
		// Removes a given Asset from the currently active Project
	public void removeAsset(Asset asset) {
		getActiveProject().removeAsset(asset);
	}
	
	/******************* SELECTION *********************/
	
		// Selects an Asset that is to be placed
	public void selectAsset(Asset asset) {
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
}
