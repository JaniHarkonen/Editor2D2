package editor2d2.model.app;

import editor2d2.Application;
import editor2d2.DebugUtils;
import editor2d2.model.Handles;
import editor2d2.model.project.Asset;
import editor2d2.model.project.Project;
import editor2d2.model.project.scene.Layer;
import editor2d2.model.project.scene.Scene;
import editor2d2.model.project.scene.placeable.Placeable;
import editor2d2.subservice.SubscriptionService;
import editor2d2.subservice.Vendor;

public class Controller implements Vendor {

		// Reference to the AppState that the Controller manipulates
	private AppState appState;
	
		// Reference to the SubscriptionService that the Controller uses
		// to notify the GUI when the AppState changes
	public final SubscriptionService subscriptionService;
	
		// Whether the Controller has been instantiated
	private static boolean isInstantiated = false;
	
	
		// This class is a singleton, only instantiate once
	private Controller(AppState appState) {
		this.appState = appState;
		this.subscriptionService = new SubscriptionService();
		
		DebugUtils.controllerDebugSetup(this);
	}
	
		// Instantiates the Controller
	public static Controller instantiate(AppState appState) {
		if( isInstantiated == true )
		return null;
		
		return new Controller(appState);
	}
	
	
		// Returns a reference to the currently open project
	public Project getActiveProject() {
		return this.appState.activeProject;
	}
	
		// Returns a reference to the selected placeable
	public Placeable getSelectedPlaceable() {
		return this.appState.selectedPlaceable;
	}
	
		// Returns a reference to the currently active Layer
	public Layer getActiveLayer() {
		return this.appState.activeLayer;
	}
	
		// Opens a new project and sets it as the active one
	public void openProject(Project project) {
		this.appState.activeProject = project;
		this.subscriptionService.register(Handles.ACTIVE_PROJECT, this);
	}
	
		// Creates a new Scene of a given anme and adds it to the
		// currently active Project
	public void createNewScene(String name) {
		Scene newScene = new Scene();
		newScene.setName(name);
		getActiveProject().addScene(newScene);
	}
	
		// Adds a given Asset to the currently active Project
	public void addNewAsset(Asset newAsset) {
		getActiveProject().addAsset(newAsset);
		Application.window.getModalWindow().getAssetPane().update();
	}
	
		// Selects an Asset that is to be placed
	public void selectAsset(Asset asset) {
		this.appState.selectedPlaceable = asset.createPlaceable();
		this.subscriptionService.register(Handles.SELECTED_PLACEABLE, this);
	}
	
		// Sets the currently active Layer
	public void selectLayer(Layer layer) {
		this.appState.activeLayer = layer;
	}
}
